package ssd.assignment.communication.kademlia;

import lombok.Getter;
import ssd.assignment.communication.NetworkNode;
import ssd.assignment.util.Utils;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KDistributedHashTable {

    private final NetworkNode self;

    @Getter
    private final Map<byte[], StoredData> storedPairs;
    @Getter
    private final Map<byte[], StoredData> selfPublishedPairs;

    public KDistributedHashTable(NetworkNode self) {
        this.self = self;
        this.storedPairs = new ConcurrentHashMap<>();
        this.selfPublishedPairs = new ConcurrentHashMap<>();
    }

    public void storePair(byte[] key, byte[] value, byte[] originalPublisherId) {
        if (Arrays.equals(self.getNodeId(), originalPublisherId)) {
            this.selfPublishedPairs.put(key, new StoredData(key, value, self.getNodeId()));
        }

        if (this.storedPairs.containsKey(key)) {
            storedPairs.get(key).updateValue(value);
        } else {
            storedPairs.put(key, new StoredData(key, value, originalPublisherId));
        }
    }

    public byte[] getValueByKey(byte[] key) {
        return storedPairs.get(key).getValue();
    }

    public void removeValueByKey(byte[] key) {
        String keyAsString = Utils.toHexString(key);
        storedPairs.remove(key);
        selfPublishedPairs.remove(key);
    }
}
