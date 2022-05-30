package ssd.assignment.communication.kademlia;

import com.google.gson.GsonBuilder;
import lombok.Getter;

import java.net.InetAddress;

@Getter
public class NodeTriple {

    private final InetAddress ip;
    private final int port;
    private final byte[] id;

    public NodeTriple(InetAddress ip, int port, byte[] id) {
        this.ip = ip;
        this.port = port;
        this.id = id;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }

}
