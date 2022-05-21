package ssd.assignment.blockchain.blocks;

import com.google.gson.GsonBuilder;
import lombok.Getter;
import ssd.assignment.blockchain.transactions.Transaction;
import ssd.assignment.util.Helper;

import java.util.ArrayList;

@Getter
public class Block {

    private final BlockHeader header;
    private final ArrayList<Transaction> transactions = new ArrayList<>();

    public Block(String parentHash) {
        this.header = new BlockHeader(parentHash);
    }

    public String calculateHash() {
        return header.calculateHash();
    }

    //Increases nonce value until hash target is reached.
    public void mineBlock(int difficulty) {
        header.merkleRoot = Helper.getMerkleRoot(transactions);
        String target = Helper.getDificultyString(difficulty);
        while(!header.hash.substring( 0, difficulty).equals(target)) {
            header.nonce ++;
            header.hash = calculateHash();
        }
        System.out.println("Block Mined!!! : " + header.hash);
    }

    //Add transactions to this block
    public boolean addTransaction(Transaction transaction) {
        //process transaction and check if valid, unless block is genesis block then ignore.
        if(transaction == null) return false;
        if((!header.parentHash.equals("0"))) {
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
