package ssd.assignment.blockchain.blocks;

import com.google.gson.GsonBuilder;
import lombok.Getter;
import ssd.assignment.blockchain.TransactionPool;
import ssd.assignment.blockchain.transactions.Transaction;
import ssd.assignment.blockchain.transactions.TxInput;
import ssd.assignment.blockchain.transactions.TxOutput;
import ssd.assignment.util.Utils;
import ssd.assignment.util.Standards;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
public class BlockChain {

    private final ArrayList<Block> blocks;
    private final TransactionPool transactionPool;
    //TODO move utxo's out of here
    private final HashMap<String, TxOutput> UTXOs;

    public BlockChain() {
        blocks = new ArrayList<>();
        transactionPool = new TransactionPool();
        UTXOs = new HashMap<>();
    }

    public void addBlock(Block newBlock) {
        blocks.add(newBlock);
    }

    public Block getLatestBlock() {
        if (blocks.size() == 0) return null;
        return blocks.get(blocks.size()-1);
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }

    public String toPrettyString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
