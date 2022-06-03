package ssd.assignment.communication.grpc;

import com.google.protobuf.ByteString;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import ssd.assignment.communication.NetworkNode;
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
        public void ping(Ping req, StreamObserver<Pong> responseObserver) {
            Pong reply = Pong.newBuilder()
                    .setNodeId(ByteString.copyFrom(node.getNodeId()))
                    .build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }

        @Override
        public void findNode(exampleRequest request, StreamObserver<exampleReply> responseObserver) {
            super.findNode(request, responseObserver);
        }

        @Override
        public void store(exampleRequest request, StreamObserver<exampleReply> responseObserver) {
            super.store(request, responseObserver);
        }

        @Override
        public void findValue(exampleRequest request, StreamObserver<exampleReply> responseObserver) {
            super.findValue(request, responseObserver);
        }
    }

}
