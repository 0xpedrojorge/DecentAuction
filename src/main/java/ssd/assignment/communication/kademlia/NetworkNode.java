package ssd.assignment.communication.kademlia;

import ssd.assignment.communication.grpc.DecentAuctionServer;

import java.io.IOException;

public class NetworkNode {

    private byte[] nodeId;
    private int port;

    private DecentAuctionServer server;

    public NetworkNode(byte[] nodeId, int port) {
        this.nodeId = nodeId;
        this.port = port;

        server = new DecentAuctionServer();
        Thread serverBlockedThread = new Thread(() -> {
            try {
                server.start();
                server.blockUntilShutdown();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });

        serverBlockedThread.start();

    }

}
