package ssd.assignment.communication.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import ssd.assignment.util.Standards;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class DecentAuctionServer {

    private static final Logger logger = Logger.getLogger(DecentAuctionServer.class.getName());

    private Server server;

    public void start() throws IOException {
        server = ServerBuilder.forPort(Standards.DEFAULT_PORT)
                .addService(new P2PServerImpl())
                .build()
                .start();
        logger.info("Server started, listening on " + Standards.DEFAULT_PORT);
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

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    /*
    TODO remove this main
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        final DecentAuctionServer server = new DecentAuctionServer();
        server.start();
        server.blockUntilShutdown();
    }

    static class P2PServerImpl extends P2PServerGrpc.P2PServerImplBase {

        @Override
        public void ping(Ping req, StreamObserver<Pong> responseObserver) {
            logger.info("Hit by a " + req.getName());
            Pong reply = Pong.newBuilder().setMessage("Pong").build();
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
