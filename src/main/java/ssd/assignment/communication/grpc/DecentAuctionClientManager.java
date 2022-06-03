package ssd.assignment.communication.grpc;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ssd.assignment.communication.NetworkNode;
import ssd.assignment.communication.kademlia.KContact;
import ssd.assignment.util.Utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


public class DecentAuctionClientManager {

    private static final Logger logger = Logger.getLogger(DecentAuctionClientManager.class.getName());

    private final Map<byte[], ManagedChannel> channelsMap;

    public DecentAuctionClientManager() {
        this.channelsMap = new ConcurrentHashMap<>();

        /*
        TODO remove this example where I send a ping to myself

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
         */
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


    public void ping(NetworkNode localNode, KContact contact) {
        logger.info("Ping from " + Utils.toHexString(localNode.getNodeId()));
        NetworkServerGrpc.NetworkServerStub contactStub = newStub(contact);

        Ping request = Ping.newBuilder()
                .setNodeId(ByteString.copyFrom(localNode.getNodeId()))
                .build();

        contactStub.ping(request,
                new StreamObserver<Pong>() {
                    @Override
                    public void onNext(Pong value) {
                        logger.info("Pong from " + Utils.toHexString(value.getNodeId().toByteArray()));
                        localNode.getKRoutingTable().insert(contact);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        voidChannel(contact);
                        localNode.getKRoutingTable().warnUnresponsiveContact(contact);
                    }

                    @Override
                    public void onCompleted() {
                    }
                });


        /*
        Ping request = Ping.newBuilder().setNodeipAddress(l).build();
        Pong response;
        try {
            response = blockingStub.ping(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info(response.getNodeipAddress());

         */
    }

}
