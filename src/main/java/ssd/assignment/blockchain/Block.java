package ssd.assignment.blockchain;

import ssd.assignment.crypto.Crypto;

import static ssd.assignment.config.Constraints.MINING_DIFFICULTY;

public class Block{

    private static final Crypto crypto = new Crypto();

    private final BlockHeader header;
    private String transactions;

    public Block() {
        this.header = new BlockHeader();
        this.transactions = "";
    }

    public Block (int index, String transactions, String parentHash) {
        this.header = new BlockHeader(index, parentHash);
        this.transactions = transactions;
    }

    public String mineBlock() {
        String prefix = new String(new char[this.header.difficulty]).replace('\0', '0');
        this.header.timestamp = System.currentTimeMillis();
        while(!this.header.hash.substring(0, this.header.difficulty).equals(prefix)) {
            this.header.nonce ++;
            this.header.hash = crypto.hash(this.toString());
        }
        return this.header.hash;
    }

    public int getIndex() {
        return this.header.id;
    }

    public void setIndex(int index) {
        this.header.id = index;
    }

    public long getTimestamp() {
        return this.header.timestamp;
    }

    public int getDifficulty() {
        return this.header.difficulty;
    }

    public int getNonce() {
        return this.header.nonce;
    }

    public void setNonce(int nonce) {
        this.header.nonce = nonce;
    }

    public String getTransactions() {
        return transactions;
    }

    public void setTransactions(String transactions) {
        this.transactions = transactions;
    }

    public String getHash() {
        return this.header.hash;
    }
    public void setHash(String hash) {
        this.header.hash = hash;
    }
    public String getParentHash() {
        return this.header.parentHash;
    }

    public void setParentHash(String parentHash) {
        this.header.parentHash = parentHash;
    }

    @Override
    public String toString() {
        return this.header.toString() + this.transactions;
    }

    public static class BlockHeader {

        private int id;
        private long timestamp;
        private final int difficulty;
        private int nonce = 0;
        private String hash;
        private String parentHash;

        public BlockHeader() {
            this.id = 0;
            this.difficulty = MINING_DIFFICULTY;
            this.parentHash = "0";
            this.hash = crypto.hash(this.toString());
        }

        public BlockHeader(int id, String parentHash) {
            this.id = id;
            this.difficulty = MINING_DIFFICULTY;
            this.parentHash = parentHash;
            this.hash = crypto.hash(this.toString());
        }

        public String toString() {
            return this.id + this.timestamp + this.difficulty + this.nonce + this.parentHash;
        }

    }
}
