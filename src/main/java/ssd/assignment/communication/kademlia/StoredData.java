package ssd.assignment.communication.kademlia;

import lombok.Getter;

import java.util.Arrays;

@Getter
public class StoredData {

    private final byte[] key;
    private byte[] value;

    private final byte[] originalPublisherId;
    private long lastRepublish;
    private long lastUpdate;

    public StoredData(byte[] key, byte[] value, byte[] originalPublisherId) {
        this.key = key;
        this.value = value;
        this.originalPublisherId = originalPublisherId;

        this.lastRepublish = System.currentTimeMillis();
        this.lastUpdate = System.currentTimeMillis();
    }

    public void updateValue(byte[] value) {
        this.value = value;
        this.lastUpdate = System.currentTimeMillis();
    }

    public void setLastRepublish() {
        lastRepublish = System.currentTimeMillis();
    }

    public void setLastUpdate() {
        lastUpdate = System.currentTimeMillis();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (this == o) return true;
        StoredData that = (StoredData) o;
        return Arrays.equals(this.getKey(), that.getKey());
    }

}
