package ssd.assignment.communication;

import lombok.Getter;
import ssd.assignment.communication.grpc.DecentAuctionClientManager;
import ssd.assignment.communication.grpc.DecentAuctionServer;
import ssd.assignment.communication.kademlia.KContact;
import ssd.assignment.communication.kademlia.KRoutingTable;
import ssd.assignment.util.Standards;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

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
                server.start(this, port);
                server.blockUntilShutdown();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
        serverBlockedThread.start();
        this.clientManager = new DecentAuctionClientManager();

        try {
            this.KRoutingTable = new KRoutingTable(
                    new KContact(InetAddress.getLocalHost(), port, nodeId, System.currentTimeMillis())
            );
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

    }

}
