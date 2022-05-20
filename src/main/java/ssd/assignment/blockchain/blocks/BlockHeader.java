package ssd.assignment.blockchain.blocks;

import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlockHeader {

    private long blockNumber;
    private short version;
    public String hash;
    public String parentHash;
    private long timeStamp;
    private int nonce;
    private String merkleRoot;


    public BlockHeader(long blockNumber, short version, String parentHash) {
        this.blockNumber = blockNumber;
        this.version = version;
        this.parentHash = parentHash;
        this.timeStamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }
}
