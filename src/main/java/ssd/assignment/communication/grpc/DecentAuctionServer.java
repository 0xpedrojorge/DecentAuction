package ssd.assignment.communication.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class DecentAuctionServer {

    private static final Logger logger = Logger.getLogger(DecentAuctionServer.class.getName());

    /* The port on which the server should run */
    private static final int PORT= 50051;

    private Server server;

    public void start() throws IOException {
        server = ServerBuilder.forPort(PORT)
                .addService(new P2PServerImpl())
                .build()
                .start();
        logger.info("Server started, listening on " + PORT);
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

    /**
     * Main launches the server from the command line.
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        final DecentAuctionServer server = new DecentAuctionServer();
        server.start();
        server.blockUntilShutdown();
    }

    static class P2PServerImpl extends P2PServerGrpc.P2PServerImplBase {

        @Override
        public void ping(Ping req, StreamObserver<Pong> responseObserver) {
            Pong reply = Pong.newBuilder().setMessage(req.getName()).build();
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
