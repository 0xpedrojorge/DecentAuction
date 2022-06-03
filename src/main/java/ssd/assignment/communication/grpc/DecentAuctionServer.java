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
        NetworkNode node;

        public NetworkServerImpl(NetworkNode node) {
            this.node = node;
        }

        @Override
        public void ping(ProtoNode req, StreamObserver<ProtoNode> responseObserver) {
            handleIncomingContact(req);

            ProtoNode reply = ProtoNode.newBuilder()
                    .setNodeIpAddress(Utils.getLocalAddressAsString())
                    .setNodePort(node.getPort())
                    .setNodeId(ByteString.copyFrom(node.getNodeId()))
                    .build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }

        @Override
        public void store(exampleRequest req, StreamObserver<exampleReply> responseObserver) {
            super.store(req, responseObserver);
        }

        @Override
        public void findNode(ProtoTarget req, StreamObserver<FoundNode> responseObserver) {
            handleIncomingContact(req.getSendingNode());

            /*
            Get the k closest contacts to the target that I have stored
             */
            for (KContact c : node.getRoutingTable().getNClosestContacts(req.getTarget().toByteArray(), Standards.KADEMLIA_K)) {
                ProtoNode foundProtoNode = buildOffKContact(c);
                FoundNode foundNode = FoundNode.newBuilder()
                        .setFoundNode(foundProtoNode)
                        .setLastSeen(c.getLastSeen())
                        .build();

                responseObserver.onNext(foundNode);
            }
            responseObserver.onCompleted();

        }

        @Override
        public void findValue(ProtoTarget req, StreamObserver<FoundValue> responseObserver) {
            super.findValue(req, responseObserver);
        }

        private void handleIncomingContact(GeneratedMessageV3 req) {
            Thread t = new Thread(() -> {
                if (req instanceof ProtoNode) {
                    System.out.println("Received message from " + Utils.toHexString(((ProtoNode) req).getNodeId().toByteArray()) + ", adding to kBucket");
                    KContact incomingContact = new KContact(Utils.getAddressFromString(((ProtoNode) req).getNodeIpAddress()),
                            ((ProtoNode) req).getNodePort(), ((ProtoNode) req).getNodeId().toByteArray(), System.currentTimeMillis());

                    node.getRoutingTable().insert(incomingContact);
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
    }

}
