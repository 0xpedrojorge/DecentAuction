package ssd.assignment.communication.grpc;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;
import io.grpc.Context;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import ssd.assignment.communication.NetworkNode;
import ssd.assignment.communication.kademlia.KContact;
import ssd.assignment.communication.kademlia.StoredData;
import ssd.assignment.communication.operations.BroadcastMessageOperation;
import ssd.assignment.util.Standards;
import ssd.assignment.util.Utils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

public class DecentAuctionServer {
    private static final Logger logger = Logger.getLogger(DecentAuctionServer.class.getName());

    private Server server;
    private NetworkServerImpl serverImpl;

    public void start(NetworkNode localNode, int port) throws IOException {
        serverImpl = new NetworkServerImpl(localNode);
        server = ServerBuilder.forPort(port)
                .addService(serverImpl)
                //.addService()
                .build()
                .start();
        //logger.info("Server started, listening on " + port); // commented for asthetics reasons (should log for registory in log file, if needed to serve as prove for forensic analises)
        //System.err.println("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Use stderr here since the logger may have been reset by its JVM shutdown hook.
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            try {
                DecentAuctionServer.this.stop();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            System.err.println("*** server shut down");
        }));
    }

    private void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public void registerMessageConsumer(BiConsumer<KContact, byte[]> consumer) {
        serverImpl.registerConsumer(consumer);
    }

    static class NetworkServerImpl extends NetworkServerGrpc.NetworkServerImplBase {
        private final NetworkNode localNode;
        private final List<BiConsumer<KContact, byte[]>> messageConsumers;

        public NetworkServerImpl(NetworkNode localNode) {
            this.localNode = localNode;
            this.messageConsumers = new LinkedList<>();
        }

        public void registerConsumer(BiConsumer<KContact, byte[]> consumer) {
            messageConsumers.add(consumer);
        }

        @Override
        public void ping(ProtoNode req, StreamObserver<ProtoNode> responseObserver) {
            handleIncomingContact(req);

            ProtoNode reply = buildSelf();

            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }

        @Override
        public void store(ProtoContent req, StreamObserver<ProtoContent> responseObserver) {
            handleIncomingContact(req.getSendingNode());

            localNode.getDht().storePair(req.getKey().toByteArray(),
                    req.getValue().toByteArray(), req.getOriginalPublisherId().toByteArray());

            ProtoContent reply = ProtoContent.newBuilder()
                    .setSendingNode(buildSelf())
                    .setKey(req.getKey())
                    .setValue(req.getValue())
                    .build();

            responseObserver.onNext(reply);
            responseObserver.onCompleted();

        }

        @Override
        public void findNode(ProtoTarget req, StreamObserver<ProtoFindNodeResponse> responseObserver) {
            logger.info("Incoming lookup request from " + Utils.toHexString(req.getSendingNode().getNodeId().toByteArray()));
            handleIncomingContact(req.getSendingNode());

            List<KContact> list = localNode.getRoutingTable()
                    .getNClosestContacts(req.getTarget().toByteArray(), Standards.KADEMLIA_K);

            ProtoFindNodeResponse reply = ProtoFindNodeResponse.newBuilder()
                    .setSendingNode(buildSelf())
                    .setFoundNodes(buildOffKContactList(list))
                    .build();

            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }


        @Override
        public void findValue(ProtoTarget req, StreamObserver<ProtoFindValueResponse> responseObserver) {
            handleIncomingContact(req.getSendingNode());

            byte[] target = req.getTarget().toByteArray();
            StoredData toReturn = localNode.getDht().getValueByKey(target);

            if (toReturn != null) {
                /*
                If the target key was found, return its value
                 */
                ProtoFindValueResponse protoFoundValue = ProtoFindValueResponse.newBuilder()
                        .setSendingNode(buildSelf())
                        .setDataType(DataType.FOUND_VALUE)
                        .setFoundValue(buildOffStoredData(toReturn))
                        .build();
                responseObserver.onNext(protoFoundValue);
            } else {
                /*
                Get the k closest contacts to the target that I have stored
                */
                List<KContact> list = localNode.getRoutingTable()
                        .getNClosestContacts(req.getTarget().toByteArray(), Standards.KADEMLIA_K);

                ProtoFindValueResponse protoFoundValue = ProtoFindValueResponse.newBuilder()
                        .setSendingNode(buildSelf())
                        .setDataType(DataType.FOUND_NODES)
                        .setFoundNodes(buildOffKContactList(list))
                        .build();

                responseObserver.onNext(protoFoundValue);
            }
            responseObserver.onCompleted();
        }

        @Override
        public void sendMessage(ProtoMessage req, StreamObserver<ProtoMessageResponse> responseObserver) {
            handleIncomingContact(req.getSendingNode());

            KContact sendingNode = buildOffProtoNode(req.getSendingNode());
            byte[] message = req.getMessage().toByteArray();

            ProtoMessageResponse protoMessageResponse = ProtoMessageResponse.newBuilder()
                            .setSendingNode(buildSelf())
                            .build();

            responseObserver.onNext(protoMessageResponse);
            responseObserver.onCompleted();

            for (BiConsumer<KContact, byte[]> messageConsumer : this.messageConsumers) {
                messageConsumer.accept(sendingNode, message);
            }
        }

        @Override
        public void broadcastMessage(ProtoBroadcastMessage req, StreamObserver<ProtoNode> responseObserver) {
            handleIncomingContact(req.getSendingNode());

            /*
            Warn about reception before dealing with message
             */
            ProtoNode reply = buildSelf();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();

            byte[] messageId = req.getMessageId().toByteArray();

            if (localNode.addToSeenMessages(messageId)) {
                byte[] message = req.getMessage().toByteArray();
                new BroadcastMessageOperation(localNode, req.getDepth(), messageId, message);

                KContact sendingNode = buildOffProtoNode(req.getSendingNode());

                Context context = Context.current().fork();
                context.run(() -> {
                    for (BiConsumer<KContact, byte[]> messageConsumer : this.messageConsumers) {
                        messageConsumer.accept(sendingNode, message);
                    }
                });
            }
        }

        private void handleIncomingContact(GeneratedMessageV3 req) {
            Thread t = new Thread(() -> {
                if (req instanceof ProtoNode) {
                    KContact incomingContact = new KContact(Utils.getAddressFromString(((ProtoNode) req).getNodeIpAddress()),
                            ((ProtoNode) req).getNodePort(), ((ProtoNode) req).getNodeId().toByteArray(), System.currentTimeMillis());

                    localNode.getRoutingTable().insert(incomingContact);
                }
            });
            t.start();
        }

        private ProtoNode buildOffKContact(KContact contact) {
            return ProtoNode.newBuilder()
                    .setNodeIpAddress(contact.getIp().getHostAddress())
                    .setNodePort(contact.getPort())
                    .setNodeId(ByteString.copyFrom(contact.getId()))
                    .build();
        }

        private ProtoNodeList buildOffKContactList(List<KContact> contacts) {
            ProtoNodeList.Builder protoListBuilder = ProtoNodeList.newBuilder();

            for (KContact c : contacts) {
                protoListBuilder.addNodes(buildOffKContact(c));
            }

            return protoListBuilder.build();
        }

        private KContact buildOffProtoNode(ProtoNode protoNode) {
            return new KContact(Utils.getAddressFromString(protoNode.getNodeIpAddress()),
                    protoNode.getNodePort(), protoNode.getNodeId().toByteArray(),
                    System.currentTimeMillis());
        }

        private ProtoContent buildOffStoredData(StoredData data) {
            return ProtoContent.newBuilder()
                    .setSendingNode(buildSelf())
                    .setKey(ByteString.copyFrom(data.getKey()))
                    .setValue(ByteString.copyFrom(data.getValue()))
                    .setOriginalPublisherId(ByteString.copyFrom(data.getOriginalPublisherId()))
                    .build();
        }

        private ProtoNode buildSelf() {
            return ProtoNode.newBuilder()
                    .setNodeIpAddress(Utils.getLocalAddressAsString())
                    .setNodePort(localNode.getPort())
                    .setNodeId(ByteString.copyFrom(localNode.getNodeId()))
                    .build();
        }
    }
}
