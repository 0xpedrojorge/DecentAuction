package ssd.assignment;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import static ssd.assignment.config.Constraints.MINING_DIFFICULTY;

public class Block {

    private int blockNumber;
    private long timestamp;
    private int difficulty;
    private int nonce = 0;
    private String transactions;
    private String hash;
    private String parentHash;

    public Block (int blockNumber, String transactions, String parentHash) {
        this.blockNumber = blockNumber;
        this.difficulty = MINING_DIFFICULTY;
        this.transactions = transactions;
        this.parentHash = parentHash;
        this.hash = calculateHash();
    }

    private String calculateHash() {
        String data = blockNumber + timestamp + nonce + transactions;
        MessageDigest digest;
        byte[] bytes = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            bytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        StringBuilder builder = new StringBuilder();
        assert bytes != null;
        for (byte b: bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    public String mineBlock() {
        String prefix = new String(new char[this.difficulty]).replace('\0', '0');
        while(!hash.substring(0, this.difficulty).equals(prefix)) {
            nonce++;
            hash = calculateHash();
        }
        Date date = new Date();
        this.timestamp = date.getTime();
        return hash;
    }

    public int getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(int blockNumber) {
        this.blockNumber = blockNumber;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getDifficulty() {
        return difficulty;
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
}
