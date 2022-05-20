package ssd.assignment.blockchain.blocks;

import ssd.assignment.util.Crypto;

import static ssd.assignment.util.Standards.MINING_DIFFICULTY;

public class BlockHeader {
    private static final Crypto crypto = new Crypto();

    private int version;
    private long timestamp;
    private int difficulty;
    private int nonce;
    private String merkleRoot;
    private byte[] parentHash;

    public BlockHeader() {
        this.version = 0;
        this.timestamp = 0;
        this.difficulty = MINING_DIFFICULTY;
        this.nonce = 0;
        this.merkleRoot = "";
        //this.parentHash;
    }

    public byte[] getHash() {
        return crypto.hash(this.toString());
    }

    public String toString() {
        return this.version + this.timestamp + this.difficulty + this.nonce + this.merkleRoot + this.parentHash;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    public String getMerkleRoot() {
        return merkleRoot;
    }

    public void setMerkleRoot(String merkleRoot) {
        this.merkleRoot = merkleRoot;
    }

    public byte[] getParentHash() {
        return parentHash;
    }

    public void setParentHash(byte[] parentHash) {
        this.parentHash = parentHash;
    }
}
