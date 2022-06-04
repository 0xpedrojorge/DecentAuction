package ssd.assignment.communication.messages.types;

import lombok.Getter;
import ssd.assignment.communication.messages.MessageData;
import ssd.assignment.communication.messages.MessageType;

@Getter
public class TestMessage extends MessageData {

    private final String stuff;

    public TestMessage(String stuff) {
        this.stuff = stuff;
    }

    @Override
    public MessageType getType() {
        return MessageType.BRADCAST_TEST_MESSAGE;
    }
}
