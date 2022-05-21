package ssd.assignment.blockchain.blocks;

import com.google.gson.GsonBuilder;
import lombok.Getter;
import ssd.assignment.blockchain.transactions.Transaction;
import ssd.assignment.util.Crypto;
import ssd.assignment.util.CustomExclusionStrategy;
import ssd.assignment.util.Helper;

import java.util.ArrayList;

@Getter
public class Block {

    public String hash;
    public String parentHash;
    public String merkleRoot;
    public ArrayList<Transaction> transactions = new ArrayList<>();
    public long timeStamp;
    public int nonce;

    public Block(String parentHash) {
        this.parentHash = parentHash;
        this.timeStamp = System.currentTimeMillis();

        this.hash = calculateHash();
    }

    public String calculateHash() {
        String blockWithoutIdAndTransactions = new GsonBuilder().setExclusionStrategies(new CustomExclusionStrategy()).create().toJson(this);
        return Crypto.hash(blockWithoutIdAndTransactions);
    }

    //Increases nonce value until hash target is reached.
    public void mineBlock(int difficulty) {
        merkleRoot = Helper.getMerkleRoot(transactions);
        String target = Helper.getDificultyString(difficulty);
        while(!hash.substring( 0, difficulty).equals(target)) {
            nonce ++;
            hash = calculateHash();
        }
        System.out.println("Block Mined!!! : " + hash);
    }

    //Add transactions to this block
    public boolean addTransaction(Transaction transaction) {
        //process transaction and check if valid, unless block is genesis block then ignore.
        if(transaction == null) return false;
        if((!parentHash.equals("0"))) {
            if((!transaction.processTransaction())) {
                System.out.println("Transaction failed to process. Discarded.");
                return false;
            }
        }
        transactions.add(transaction);
        System.out.println("Transaction Successfully added to Block");
        return true;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }
}
