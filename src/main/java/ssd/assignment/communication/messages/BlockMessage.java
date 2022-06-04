package ssd.assignment.communication.messages;

import lombok.Getter;
import ssd.assignment.blockchain.blocks.Block;

@Getter
public class BlockMessage extends Message {

    private final Block block;

    public BlockMessage(Block block) {
        super(MessageType.BROADCAST_BLOCK);
        this.block = block;
    }
}
