package ssd.assignment.blockchain.blocks;

import com.google.gson.GsonBuilder;
import ssd.assignment.util.Standards;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {

    public List<Block> blocks;

    public Blockchain() {
        blocks = new ArrayList<>();
    }

    public boolean isValid() {
        Block currentBlock, previousBlock;

        for(int i=1; i < blocks.size(); i++) {
            currentBlock = blocks.get(i);
            previousBlock = blocks.get(i - 1);

            //check if hash is solved
            String target = new String(new char[Standards.MINING_DIFFICULTY]).replace('\0', '0');
            if(!currentBlock.getHeader().getHash().substring( 0, Standards.MINING_DIFFICULTY).equals(target)) {
                System.out.println("This block hasn't been mined");
                return false;
            }

            if (!currentBlock.getHeader().getHash().equals(currentBlock.calculateHash())) {
                System.out.println("Current hash mismatch");
                return false;
            }

            if (!previousBlock.getHeader().getHash().equals(currentBlock.getHeader().getParentHash())) {
                System.out.println("Parent hash mismatch @ " + i);
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }

    public String toPrettyString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
