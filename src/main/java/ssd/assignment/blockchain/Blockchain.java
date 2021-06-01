package ssd.assignment.blockchain;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {

    public List<Block> chain = new ArrayList<Block>();

    public Blockchain() {
        this.append(new Block());
    }

    public List<Block> getChain() {
        return chain;
    }

    public Block getRootBlock() {
        return this.chain.get(0);
    }

    public void append(Block block) {
        if (this.chain.size() != 0)
            block.setParentHash(this.chain.get(this.chain.size()-1).getHash());
        else
            block.setParentHash("0");
        block.mineBlock();
        this.chain.add(block);
    }
}
