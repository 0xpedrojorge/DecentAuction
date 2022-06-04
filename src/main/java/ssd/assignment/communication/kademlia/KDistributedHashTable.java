package ssd.assignment.communication.kademlia;

import com.google.gson.GsonBuilder;
import lombok.Getter;
import ssd.assignment.communication.NetworkNode;
import ssd.assignment.util.Utils;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KDistributedHashTable {

    @Getter
    private final Map<byte[], StoredData> storedPairs;

    public KDistributedHashTable() {
        this.storedPairs = new ConcurrentHashMap<>();
    }

    public void storePair(byte[] key, byte[] value, byte[] originalPublisherId) {
        boolean isAlreadyStored = false;
        for(byte[] k : storedPairs.keySet()) {
            if (Arrays.equals(k, key)) {
                isAlreadyStored = true;
                break;
            }
        }

        if (isAlreadyStored) {
            for(byte[] k : storedPairs.keySet()) {
                if (Arrays.equals(k, key)) {
                    storedPairs.get(k).updateValue(value);
                    break;
                }
            }
        } else {
            storedPairs.put(key, new StoredData(key, value, originalPublisherId));
        }
    }

    public StoredData getValueByKey(byte[] key) {
        for(byte[] k : storedPairs.keySet()) {
            if (Arrays.equals(k, key)) {
                return storedPairs.get(k);
            }
        }
        return null;
    }

    public void removeValueByKey(byte[] key) {
        for(byte[] k : storedPairs.keySet()) {
            if (Arrays.equals(k, key)) {
                storedPairs.remove(k);
            }
        }
    }
}
