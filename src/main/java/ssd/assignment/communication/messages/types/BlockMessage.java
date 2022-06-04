package ssd.assignment.communication.messages.types;

import lombok.Getter;
import ssd.assignment.blockchain.blocks.Block;
import ssd.assignment.communication.messages.Message;
import ssd.assignment.communication.messages.MessageType;

@Getter
public class BlockMessage extends Message {

    private final Block block;

    public BlockMessage(Block block) {
        super(MessageType.BROADCAST_BLOCK);
        this.block = block;
    }
}
