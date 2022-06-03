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
    private final Map<String, StoredData> storedPairs;
    @Getter
    private final Map<String, StoredData> selfPublishedPairs;

    public KDistributedHashTable(NetworkNode self) {
        this.self = self;
        this.storedPairs = new ConcurrentHashMap<>();
        this.selfPublishedPairs = new ConcurrentHashMap<>();
    }

    public void storePair(byte[] key, byte[] value, byte[] originalPublisherId) {
        String keyAsString = Utils.toHexString(key);
        if (Arrays.equals(self.getNodeId(), originalPublisherId)) {
            this.selfPublishedPairs.put(keyAsString, new StoredData(key, value, self.getNodeId()));
        }

        if (this.storedPairs.containsKey(keyAsString)) {
            storedPairs.get(keyAsString).updateValue(value);
        } else {
            storedPairs.put(keyAsString, new StoredData(key, value, originalPublisherId));
        }
    }

    public byte[] getValueByKey(byte[] key) {
        String keyAsString = Utils.toHexString(key);
        System.out.println(keyAsString);
        for(String k : storedPairs.keySet()) {
            System.out.println("------- " + k);
        }
        return storedPairs.get(keyAsString).getValue();
    }

    public void removeValueByKey(byte[] key) {
        String keyAsString = Utils.toHexString(key);
        storedPairs.remove(keyAsString);
        selfPublishedPairs.remove(keyAsString);
    }
}
