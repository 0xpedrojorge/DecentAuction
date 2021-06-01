package ssd.assignment.blockchain;

import ssd.assignment.crypto.Crypto;

import static ssd.assignment.config.Constraints.MINING_DIFFICULTY;

public class Block{

    private static Crypto crypto = new Crypto();

    private int index;
    private long timestamp;
    private int difficulty;
    private int nonce = 0;
    private String transactions;
    private String hash;
    private String parentHash;

    public Block() {
        this.index = 0;
        this.difficulty = MINING_DIFFICULTY;
        this.transactions = "";
        this.parentHash = "0";
        this.hash = crypto.sha256(this.toString());
    }

    public Block (int index, String transactions, String parentHash) {
        this.index = index;
        this.difficulty = MINING_DIFFICULTY;
        this.transactions = transactions;
        this.parentHash = parentHash;
        this.hash = crypto.sha256(this.toString());
    }

    public String mineBlock() {
        String prefix = new String(new char[this.difficulty]).replace('\0', '0');
        while(!hash.substring(0, this.difficulty).equals(prefix)) {
            nonce++;
            hash = crypto.sha256(this.toString());
        }
        this.timestamp = System.currentTimeMillis();
        return hash;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public int getDifficulty() {
        return this.difficulty;
    }

    public int getNonce() {
        return this.nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    public String getTransactions() {
        return transactions;
    }

    public void setTransactions(String transactions) {
        this.transactions = transactions;
    }

    public String getHash() {
        return hash;
    }
    public void setHash(String hash) {
        this.hash = hash;
    }
    public String getParentHash() {
        return parentHash;
    }

    public void setParentHash(String parentHash) {
        this.parentHash = parentHash;
    }

    @Override
    public String toString() {
        return this.index + this.timestamp + this.difficulty + this.nonce + this.transactions + this.parentHash;
    }
}
