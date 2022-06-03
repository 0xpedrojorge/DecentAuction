package ssd.assignment.communication.grpc;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ssd.assignment.communication.NetworkNode;
import ssd.assignment.communication.kademlia.KContact;
import ssd.assignment.communication.kademlia.StoredData;
import ssd.assignment.communication.operations.ContentLookupOperation;
import ssd.assignment.communication.operations.LookupOperation;
import ssd.assignment.communication.operations.PingOperation;
import ssd.assignment.communication.operations.StoreOperation;
import ssd.assignment.util.Utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;


public class DecentAuctionClientManager {

    private static final Logger logger = Logger.getLogger(DecentAuctionClientManager.class.getName());

    private final Map<byte[], ManagedChannel> channelsMap;

    public DecentAuctionClientManager() {
        this.channelsMap = new ConcurrentHashMap<>();
    }

    private ManagedChannel getChannel(KContact contact) {
        ManagedChannel channelToReturn;

        /*
        We start checking if the channel already exists in the map
        and check that is not terminated or shutdown
         */
        if (channelsMap.containsKey(contact.getId())) {
            channelToReturn = channelsMap.get(contact.getId());
            if (channelToReturn.isTerminated() || channelToReturn.isShutdown()) {
                channelToReturn = null;
            }
        } else {
            channelToReturn = null;
        }

        /*
        If the channel is still null, we create a new channel and save
        it in the map
         */
        if (channelToReturn == null) {
            channelToReturn = ManagedChannelBuilder.forAddress(contact.getIp().getHostAddress(), contact.getPort())
                    .usePlaintext()
                    .build();

            channelsMap.put(contact.getId(), channelToReturn);
        }

        return channelToReturn;
    }

    private void voidChannel(KContact contact) {
        ManagedChannel channel = channelsMap.remove(contact.getId());

        if (channel == null) return;

        if (channel.isTerminated() || channel.isShutdown()) return;

        try {
            channel.shutdownNow().awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private NetworkServerGrpc.NetworkServerStub newStub(KContact contact) {
        ManagedChannel channel = getChannel(contact);
        return NetworkServerGrpc.newStub(channel);
    }

    public void ping(NetworkNode localNode, KContact recipient, PingOperation operation) {
        logger.info("Ping from " + Utils.toHexString(localNode.getNodeId()));
        NetworkServerGrpc.NetworkServerStub stub = newStub(recipient);

        ProtoNode req = buildSelf(localNode);

        stub.ping(req, new StreamObserver<ProtoNode>() {
            @Override
            public void onNext(ProtoNode res) {
                logger.info("Pong from " + Utils.toHexString(res.getNodeId().toByteArray()));
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                voidChannel(recipient);
                operation.handleFailedRequest(recipient);
            }

            @Override
            public void onCompleted() {
                operation.handleSuccessfulRequest(recipient);
            }
        });
    }

    public void store(NetworkNode localNode, KContact recipient, StoreOperation operation, StoredData data) {
        logger.info("Starting store from " + Utils.toHexString(localNode.getNodeId()));
        NetworkServerGrpc.NetworkServerStub stub = newStub(recipient);

        ProtoNode self = buildSelf(localNode);
        ProtoContent protoContent = ProtoContent.newBuilder()
                .setSendingNode(self)
                .setOriginalPublisherId(ByteString.copyFrom(data.getOriginalPublisherId()))
                .setKey(ByteString.copyFrom(data.getKey()))
                .setValue(ByteString.copyFrom(data.getValue()))
                .build();

        stub.store(protoContent, new StreamObserver<ProtoContent>() {
            @Override
            public void onNext(ProtoContent value) {
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                voidChannel(recipient);
                operation.handleFailedRequest(recipient);
            }

            @Override
            public void onCompleted() {
                operation.handleSuccessfulRequest(recipient);
            }
        });

    }

    public void lookup(NetworkNode localNode, KContact recipient, LookupOperation operation, byte[] target) {
        logger.info("Starting lookup from " + Utils.toHexString(localNode.getNodeId()));
        NetworkServerGrpc.NetworkServerStub stub = newStub(recipient);

        ProtoNode self = buildSelf(localNode);

        ProtoTarget protoTarget = ProtoTarget.newBuilder()
                .setSendingNode(self)
                .setTarget(ByteString.copyFrom(target))
                .build();

        List<KContact> returnedContacts = new ArrayList<>();

        stub.findNode(protoTarget, new StreamObserver<FoundNode>() {
            @Override
            public void onNext(FoundNode value) {
                InetAddress foundAddress;
                try {
                    foundAddress = InetAddress.getByName(value.getSendingNode().getNodeIpAddress());
                } catch (UnknownHostException e) {
                    throw new RuntimeException(e);
                }
                int foundPort = value.getSendingNode().getNodePort();
                byte[] foundId = value.getSendingNode().getNodeId().toByteArray();
                long foundLastSeen = value.getLastSeen();
                returnedContacts.add(new KContact(foundAddress, foundPort, foundId, foundLastSeen));
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                voidChannel(recipient);
                operation.handleFailedRequest(recipient);
            }

            @Override
            public void onCompleted() {
                operation.handleSuccessfulRequest(recipient, returnedContacts);
            }
        });
    }

    public void findValue(NetworkNode localNode, KContact recipient, ContentLookupOperation operation, byte[] keyToLookup) {
        logger.info("Starting findValue from " + Utils.toHexString(localNode.getNodeId()));
        NetworkServerGrpc.NetworkServerStub stub = newStub(recipient);

        ProtoNode self = buildSelf(localNode);

        ProtoTarget protoTarget = ProtoTarget.newBuilder()
                .setSendingNode(self)
                .setTarget(ByteString.copyFrom(keyToLookup))
                .build();

        /*
        Had to use write-only element because of an error,
        still don't fully understand it
         */
        AtomicReference<byte[]> returnedValue = new AtomicReference<>(null);
        List<KContact> returnedContacts = new ArrayList<>();

        stub.findValue(protoTarget, new StreamObserver<FoundValue>() {
            @Override
            public void onNext(FoundValue value) {
                if (value.getDataType() == DataType.FOUND_VALUE) {
                    returnedValue.set(value.getValue().toByteArray());
                } else {
                    InetAddress foundAddress;
                    try {
                        foundAddress = InetAddress.getByName(value.getSendingNode().getNodeIpAddress());
                    } catch (UnknownHostException e) {
                        throw new RuntimeException(e);
                    }
                    int foundPort = value.getSendingNode().getNodePort();
                    byte[] foundId = value.getSendingNode().getNodeId().toByteArray();
                    returnedContacts.add(new KContact(foundAddress, foundPort, foundId, System.currentTimeMillis()));
                }
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                voidChannel(recipient);
                operation.handleFailedRequest(recipient);
            }

            @Override
            public void onCompleted() {
                if (returnedValue.get() != null) {
                    operation.handleFoundValue(recipient, returnedValue.get());
                } else {
                    operation.handleReturnedNodes(recipient, returnedContacts);
                }
            }
        });
    }

    private ProtoNode buildSelf(NetworkNode self) {
        return ProtoNode.newBuilder()
                .setNodeIpAddress(Utils.getLocalAddressAsString())
                .setNodePort(self.getPort())
                .setNodeId(ByteString.copyFrom(self.getNodeId()))
                .build();
    }

}
