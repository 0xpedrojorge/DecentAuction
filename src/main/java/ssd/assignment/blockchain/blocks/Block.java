package ssd.assignment.blockchain.blocks;

import com.google.gson.GsonBuilder;
import lombok.Getter;
import ssd.assignment.blockchain.transactions.Transaction;
import ssd.assignment.util.Helper;

import java.util.ArrayList;
import java.util.LinkedList;

@Getter
public class Block implements Cloneable {

    private final BlockHeader header;
    private final ArrayList<Transaction> transactions = new ArrayList<>();

    public Block(String parentHash) {
        this.header = new BlockHeader(parentHash);
    }

    public String calculateHash() {
        return header.calculateHash();
    }

    //Add transactions to this block
    public void addTransactions(LinkedList<Transaction> transactionList) {
        for (Transaction transaction : transactionList) {
            //process transaction and check if valid, unless block is genesis block then ignore.
            if(transaction == null)  {
                System.out.println("Transaction failed to process. Discarded.");
                continue;
            }
            if((!header.parentHash.equals("0"))) {
                if((!transaction.verifiySignature())) {
                    System.out.println("Transaction Signature failed to verify. Discarded.");
                    continue;
                }
            }
            transactions.add(transaction);
            System.out.println("Transaction Successfully added to Block");
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }
}
