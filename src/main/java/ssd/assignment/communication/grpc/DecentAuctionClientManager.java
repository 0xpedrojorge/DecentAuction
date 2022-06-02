package ssd.assignment.communication.grpc;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DecentAuctionClientManager {

    private static final Logger logger = Logger.getLogger(DecentAuctionClientManager.class.getName());

    private final P2PServerGrpc.P2PServerBlockingStub blockingStub;

    public DecentAuctionClientManager() {

        /*
        TODO remove this example where I send a ping to myself
         */
        ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:50051")
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext()
                .build();

        // Passing Channels to code makes code easier to test and makes it easier to reuse Channels.
        blockingStub = P2PServerGrpc.newBlockingStub(channel);

        try {
            ping("Ping");
        } finally {
            // ManagedChannels use resources like threads and TCP connections. To prevent leaking these
            // resources the channel should be shut down when it will no longer be used. If it may be used
            // again leave it running.
            try {
                channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void ping(String name) {
        logger.info("Ping");
        Ping request = Ping.newBuilder().setNodeipAddress(name).build();
        Pong response;
        try {
            response = blockingStub.ping(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info(response.getNodeipAddress());
    }

    /*
    TODO replace main with constructor
     */
    public static void main(String[] args) {
        DecentAuctionClientManager client = new DecentAuctionClientManager();
    }

}
