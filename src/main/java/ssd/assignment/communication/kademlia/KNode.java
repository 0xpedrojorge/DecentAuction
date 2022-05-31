package ssd.assignment.communication.kademlia;

import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;

import java.net.InetAddress;
import java.util.Arrays;

@Getter
public class KNode {

    private final InetAddress ip;
    private final int port;
    private final byte[] id;

    @Setter
    private long lastSeen;
    private int staleCount;

    public KNode(InetAddress ip, int port, byte[] id, long lastSeen) {
        this.ip = ip;
        this.port = port;
        this.id = id;

        this.lastSeen = lastSeen;
        this.staleCount = 0;
    }

    public void incrementStaleCount() {
        staleCount ++;
    }

    public void resetStaleCount() {
        staleCount = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof KNode) {
            return ((KNode) o).getIp().equals(this.ip) &&
                    ((KNode) o).getPort() == this.port &&
                    Arrays.equals(((KNode) o).getId(), this.id);
        }
        return false;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }

}