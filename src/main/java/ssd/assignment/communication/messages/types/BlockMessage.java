package ssd.assignment.communication.messages.types;

import lombok.Getter;
import ssd.assignment.blockchain.blocks.Block;
import ssd.assignment.communication.messages.MessageData;
import ssd.assignment.communication.messages.MessageType;

@Getter
public class BlockMessage extends MessageData {

    private final Block block;

    public BlockMessage(Block block) {
        this.block = block;
    }

    @Override
    public MessageType getType() {
        return MessageType.BROADCAST_BLOCK;
    }
}
