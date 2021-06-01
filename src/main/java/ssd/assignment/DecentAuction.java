package ssd.assignment;

import java.util.ArrayList;
import java.util.List;

public class DecentAuction {

    public static void main(String[] args) {
        List<Block> blockchain = new ArrayList<>();

        Block genesisBlock = new Block(0, "First Block", "0");
        genesisBlock.mineBlock();
        blockchain.add(genesisBlock);
        Block secondBlock = new Block(genesisBlock.getBlockNumber()+1, "Second block", genesisBlock.getHash());
        secondBlock.mineBlock();
        blockchain.add(secondBlock);

        for (Block b : blockchain) {
            System.out.println("Block " + b.getBlockNumber() + ": " + b.getHash());
        }
    }

}
