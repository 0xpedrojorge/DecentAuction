package ssd.assignment.communication.kademlia;

import com.google.gson.GsonBuilder;

import java.net.InetAddress;

public class KademliaNode {

    private final InetAddress ip;
    private final int port;
    private final byte[] nodeId;

    public KademliaNode(InetAddress ip, int port, byte[] nodeId) {
        this.ip = ip;
        this.port = port;
        this.nodeId = nodeId;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }

}
