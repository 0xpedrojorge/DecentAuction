package ssd.assignment.communication.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ssd.assignment.communication.messages.types.BlockMessage;
import ssd.assignment.communication.messages.types.TestMessage;
import ssd.assignment.communication.messages.types.TransactionMessage;

@AllArgsConstructor
@Getter
public enum MessageType {
    BRADCAST_TEST_MESSAGE(TestMessage.class),
    BROADCAST_TRANSACTION(TransactionMessage.class),
    BROADCAST_BLOCK(BlockMessage.class);

    private final Class<? extends MessageData> typeClass;
}
