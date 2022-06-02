package ssd.assignment.communication.kademlia;

import lombok.Getter;
import ssd.assignment.communication.grpc.DecentAuctionClientManager;
import ssd.assignment.communication.grpc.DecentAuctionServer;

import java.io.IOException;

@Getter
public class NetworkNode {

    private final byte[] nodeId;
    private final int port;

    private final DecentAuctionServer server;
    private final DecentAuctionClientManager clientManager;

    private final KRoutingTable KRoutingTable;

    public NetworkNode(byte[] nodeId, int port) {
        this.nodeId = nodeId;
        this.port = port;

        server = new DecentAuctionServer();
        Thread serverBlockedThread = new Thread(() -> {
            try {
                server.start(this);
                server.blockUntilShutdown();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
        serverBlockedThread.start();
        this.clientManager = new DecentAuctionClientManager();

        this.KRoutingTable = new KRoutingTable(this);

    }

}
