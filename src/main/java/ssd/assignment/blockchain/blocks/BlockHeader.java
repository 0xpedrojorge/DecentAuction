package ssd.assignment.blockchain.blocks;

import com.google.gson.GsonBuilder;
import lombok.Getter;
import ssd.assignment.blockchain.transactions.Transaction;
import ssd.assignment.util.Crypto;
import ssd.assignment.util.CustomExclusionStrategy;

import java.util.ArrayList;

@Getter
public class BlockHeader {

    public String hash;
    public String parentHash;
    public String merkleRoot;
    public long timeStamp;
    public int nonce;

    public BlockHeader(String parentHash) {
        this.parentHash = parentHash;
        this.timeStamp = System.currentTimeMillis();

        this.hash = calculateHash();
    }

    public String calculateHash() {
        String blockWithoutId = new GsonBuilder().setExclusionStrategies(new CustomExclusionStrategy()).create().toJson(this);
        return Crypto.hash(blockWithoutId);
    }
}
