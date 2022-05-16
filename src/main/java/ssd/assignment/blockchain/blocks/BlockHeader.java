package ssd.assignment.blockchain.blocks;

import lombok.Getter;
import lombok.Setter;
import ssd.assignment.util.Crypto;

import static ssd.assignment.util.Standards.MINING_DIFFICULTY;

@Getter
@Setter
public class BlockHeader {
    private static final Crypto crypto = new Crypto();

    private int version;
    private long timestamp;
    private int difficulty;
    private int nonce;
    private String merkleRoot;
    private String parentHash;

    public BlockHeader() {
        this.version = 0;
        this.timestamp = 0;
        this.difficulty = MINING_DIFFICULTY;
        this.nonce = 0;
        this.merkleRoot = "";
        this.parentHash = "";
    }

    public String getHash() {
        return crypto.hash(this.toString());
    }

    public String toString() {
        return this.version + this.timestamp + this.difficulty + this.nonce + this.merkleRoot + this.parentHash;
    }
}
