package ssd.assignment.blockchain;

import lombok.Getter;
import lombok.Setter;
import ssd.assignment.util.Crypto;

import static ssd.assignment.config.Constraints.MINING_DIFFICULTY;

@Getter
@Setter
class BlockHeader {
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

public class Block {

    private final BlockHeader header;
    private String transactions;

    public Block() {
        this.header = new BlockHeader();
        this.transactions = "";
    }

    public String mineBlock() {
        String prefix = new String(new char[this.header.getDifficulty()]).replace('\0', '0');
        this.header.setTimestamp(System.currentTimeMillis());
        while(!this.header.getHash().substring(0, this.header.getDifficulty()).equals(prefix)) {
            this.header.setNonce(this.header.getNonce()+1);
        }
        return this.header.getHash();
    }

    public int getVersion() {
        return this.header.getVersion();
    }

    public void setVersion(int version) {
        this.header.setVersion(version);
    }

    public long getTimestamp() {
        return this.header.getTimestamp();
    }

    public void setTimestamp(long timestamp) {
        this.header.setTimestamp(timestamp);
    }

    public int getDifficulty() {
        return this.header.getDifficulty();
    }

    public void setDifficulty(int difficulty) {
        this.header.setDifficulty(difficulty);
    }

    public int getNonce() {
        return this.header.getNonce();
    }

    public void setNonce(int nonce) {
        this.header.setNonce(nonce);
    }

    public String getTransactions() {
        return transactions;
    }

    public void setTransactions(String transactions) {
        this.transactions = transactions;
    }

    public String getParentHash() {
        return this.header.getParentHash();
    }

    public void setParentHash(String parentHash) {
        this.header.setParentHash(parentHash);
    }

    public String getHash() {
        return this.header.getHash();
    }

    public String toString() {
        return this.header.toString() + this.transactions;
    }
}
