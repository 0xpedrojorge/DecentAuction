package ssd.assignment.communication.grpc;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import ssd.assignment.communication.NetworkNode;
import ssd.assignment.communication.kademlia.KContact;
import ssd.assignment.util.Standards;
import ssd.assignment.util.Utils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class DecentAuctionServer {
    private static final Logger logger = Logger.getLogger(DecentAuctionServer.class.getName());

    private Server server;

    public void start(NetworkNode localNode, int port) throws IOException {
        server = ServerBuilder.forPort(port)
                .addService(new NetworkServerImpl(localNode))
                .build()
                .start();
        logger.info("Server started, listening on " + port);

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

    static class NetworkServerImpl extends NetworkServerGrpc.NetworkServerImplBase {
        NetworkNode localNode;

        public NetworkServerImpl(NetworkNode localNode) {
            this.localNode = localNode;
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
            ProtoNode self = buildSelf();

            ProtoContent reply = ProtoContent.newBuilder()
                    .setSendingNode(self)
                    .setKey(req.getKey())
                    .setValue(req.getValue())
                    .build();

            responseObserver.onNext(reply);
            responseObserver.onCompleted();

        }

        @Override
        //public void findNode(ProtoTarget req, StreamObserver<FoundNode> responseObserver) {
        public void findNode(ProtoTarget req, StreamObserver<FindNodeResponse> responseObserver) {
            handleIncomingContact(req.getSendingNode());

            /*
            Get the k closest contacts to the target that I have stored
             */
            /*
            for (KContact c : localNode.getRoutingTable()
                    .getNClosestContacts(req.getTarget().toByteArray(), Standards.KADEMLIA_K)) {

                ProtoNode protoNode = buildOffKContact(c);

                FoundNode protoFoundNode = FoundNode.newBuilder()
                        .setSendingNode(protoNode)
                        .setLastSeen(c.getLastSeen())
                        .build();

                responseObserver.onNext(protoFoundNode);
            }
             */

            List<KContact> list = localNode.getRoutingTable()
                    .getNClosestContacts(req.getTarget().toByteArray(), Standards.KADEMLIA_K);

            FindNodeResponse reply = FindNodeResponse.newBuilder()
                    .setSendingNode(buildSelf())
                    .setFoundNodes(buildOffKContactList(list))
                    .build();

            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }


        @Override
        public void findValue(ProtoTarget req, StreamObserver<FoundValue> responseObserver) {
            handleIncomingContact(req.getSendingNode());

            byte[] target = req.getTarget().toByteArray();
            byte[] toReturn = localNode.getDht().getValueByKey(target);

            ProtoNode self = buildSelf();
            if (toReturn != null) {
                /*
                If the target key was found, return its value
                 */
                FoundValue protoFoundValue = FoundValue.newBuilder()
                        .setSendingNode(self)
                        .setDataType(DataType.FOUND_VALUE)
                        .setKey(ByteString.copyFrom(target))
                        .setValue(ByteString.copyFrom(toReturn))
                        .build();
                responseObserver.onNext(protoFoundValue);
            } else {
                /*
                Get the k closest contacts to the target that I have stored
                */
                for (KContact c : localNode.getRoutingTable()
                        .getNClosestContacts(req.getTarget().toByteArray(), Standards.KADEMLIA_K)) {

                    FoundValue protoFoundValue = FoundValue.newBuilder()
                            .setSendingNode(self)
                            .setDataType(DataType.FOUND_NODES)
                            .setKey(ByteString.copyFrom(c.getId()))
                            .build();

                    responseObserver.onNext(protoFoundValue);
                }
            }
            responseObserver.onCompleted();
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

        private ProtoNode buildSelf() {
            return ProtoNode.newBuilder()
                    .setNodeIpAddress(Utils.getLocalAddressAsString())
                    .setNodePort(localNode.getPort())
                    .setNodeId(ByteString.copyFrom(localNode.getNodeId()))
                    .build();
        }
    }
}
