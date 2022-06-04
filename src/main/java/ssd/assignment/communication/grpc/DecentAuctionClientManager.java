package ssd.assignment.communication.grpc;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import ssd.assignment.communication.NetworkNode;
import ssd.assignment.communication.kademlia.KContact;
import ssd.assignment.communication.kademlia.StoredData;
import ssd.assignment.communication.operations.*;
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

    private NetworkServerGrpc.NetworkServerBlockingStub newBlockingStub(KContact contact) {
        ManagedChannel channel = getChannel(contact);
        return NetworkServerGrpc.newBlockingStub(channel);
    }

    public void ping(NetworkNode localNode, KContact recipient, PingOperation operation) {
        //logger.info("Ping from " + Utils.toHexString(localNode.getNodeId()));
        NetworkServerGrpc.NetworkServerBlockingStub stub = newBlockingStub(recipient);

        ProtoNode req = buildSelf(localNode);

        try {
            ProtoNode response = stub.ping(req);
            logger.info("Pong from " + Utils.toHexString(response.getNodeId().toByteArray()));
            operation.handleSuccessfulRequest(recipient);
        } catch (StatusRuntimeException e) {
            //e.printStackTrace();
            voidChannel(recipient);
            operation.handleFailedRequest(recipient);
        }

    }

    public void store(NetworkNode localNode, KContact recipient, StoreOperation operation, StoredData data) {
        //logger.info("Starting store from " + Utils.toHexString(localNode.getNodeId()));
        NetworkServerGrpc.NetworkServerBlockingStub stub = newBlockingStub(recipient);

        ProtoNode self = buildSelf(localNode);
        ProtoContent protoContent = ProtoContent.newBuilder()
                .setSendingNode(self)
                .setOriginalPublisherId(ByteString.copyFrom(data.getOriginalPublisherId()))
                .setKey(ByteString.copyFrom(data.getKey()))
                .setValue(ByteString.copyFrom(data.getValue()))
                .build();

        try {
            ProtoContent response = stub.store(protoContent);
            operation.handleSuccessfulRequest(recipient);
        } catch (StatusRuntimeException e) {
            //e.printStackTrace();
            voidChannel(recipient);
            operation.handleFailedRequest(recipient);
        }
    }

    public void lookup(NetworkNode localNode, KContact recipient, LookupOperation operation, byte[] target) {
        //logger.info("Starting lookup from " + Utils.toHexString(localNode.getNodeId()));
        NetworkServerGrpc.NetworkServerBlockingStub stub = newBlockingStub(recipient);

        ProtoNode self = buildSelf(localNode);

        ProtoTarget protoTarget = ProtoTarget.newBuilder()
                .setSendingNode(self)
                .setTarget(ByteString.copyFrom(target))
                .build();


        try {
            ProtoFindNodeResponse response = stub.findNode(protoTarget);
            List<KContact> returnedContacts = buildOffProtoNodeList(response.getFoundNodes());
            operation.handleSuccessfulRequest(recipient, returnedContacts);
        } catch (StatusRuntimeException e) {
            //e.printStackTrace();
            voidChannel(recipient);
            operation.handleFailedRequest(recipient);
        }
    }

    public void findValue(NetworkNode localNode, KContact recipient, ContentLookupOperation operation, byte[] keyToLookup) {
        //logger.info("Starting findValue from " + Utils.toHexString(localNode.getNodeId()));
        NetworkServerGrpc.NetworkServerBlockingStub stub = newBlockingStub(recipient);

        ProtoTarget protoTarget = ProtoTarget.newBuilder()
                .setSendingNode(buildSelf(localNode))
                .setTarget(ByteString.copyFrom(keyToLookup))
                .build();

        /*
        Had to use write-only element because of an error,
        still don't fully understand it
         */
        try {
            ProtoFindValueResponse response = stub.findValue(protoTarget);
            if (response.getDataType() == DataType.FOUND_VALUE) {
                StoredData returnedData = buildOffProtoContent(response.getFoundValue());
                operation.handleFoundValue(recipient, returnedData);
            } else {
                List<KContact> list = buildOffProtoNodeList(response.getFoundNodes());
                operation.handleReturnedNodes(recipient, list);
            }
        } catch (StatusRuntimeException e) {
            //e.printStackTrace();
            voidChannel(recipient);
            operation.handleFailedRequest(recipient);
        }
    }

    public void sendMessage(NetworkNode localNode, KContact recipient, SendMessageOperation operation, byte[] message) {
        //logger.info("Starting sendMessage from " + Utils.toHexString(localNode.getNodeId()));
        NetworkServerGrpc.NetworkServerBlockingStub stub = newBlockingStub(recipient);

        ProtoMessage protoMessage = ProtoMessage.newBuilder()
                .setSendingNode(buildSelf(localNode))
                .setMessage(ByteString.copyFrom(message))
                .build();

        try {
            ProtoMessageResponse response = stub.sendMessage(protoMessage);
            operation.handleSuccessfulRequest(recipient);
        } catch (StatusRuntimeException e) {
            //e.printStackTrace();
            voidChannel(recipient);
            operation.handleFailedRequest(recipient);
        }

    }

    public void broadcastMessage(NetworkNode localNode, KContact recipient, BroadcastMessageOperation operation, int depth, byte[] messageId, byte[] message) {
        logger.info("Starting broadcastMessage from " + Utils.toHexString(localNode.getNodeId()));
        NetworkServerGrpc.NetworkServerBlockingStub stub = newBlockingStub(recipient);

        ProtoBroadcastMessage broadcastMessage = ProtoBroadcastMessage.newBuilder()
                .setSendingNode(buildSelf(localNode))
                .setDepth(depth)
                .setMessageId(ByteString.copyFrom(messageId))
                .setMessage(ByteString.copyFrom(message))
                .build();
        try {
            ProtoNode response = stub.broadcastMessage(broadcastMessage);
            operation.handleSuccessfulRequest(recipient);
        }catch (StatusRuntimeException e) {
            //e.printStackTrace();
            voidChannel(recipient);
            operation.handleFailedRequest(recipient);
        }

    }

    private ProtoNode buildSelf(NetworkNode self) {
        return ProtoNode.newBuilder()
                .setNodeIpAddress(Utils.getLocalAddressAsString())
                .setNodePort(self.getPort())
                .setNodeId(ByteString.copyFrom(self.getNodeId()))
                .build();
    }

    private KContact buildOffProtoNode(ProtoNode proto) {
        InetAddress address;
        try {
            address = InetAddress.getByName(proto.getNodeIpAddress());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        return new KContact(address, proto.getNodePort(), proto.getNodeId().toByteArray(), System.currentTimeMillis());
    }

    private List<KContact> buildOffProtoNodeList(ProtoNodeList protoList) {
        List<KContact> list = new ArrayList<>();

        for(ProtoNode protoNode : protoList.getNodesList()) {
            list.add(buildOffProtoNode(protoNode));
        }
        return list;
    }

    private StoredData buildOffProtoContent(ProtoContent protoContent) {
        return new StoredData(protoContent.getKey().toByteArray(),
                protoContent.getValue().toByteArray(),
                protoContent.getOriginalPublisherId().toByteArray());
    }
}
