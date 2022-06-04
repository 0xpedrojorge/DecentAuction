package ssd.assignment.communication.messages;

import lombok.Getter;

@Getter
public abstract class Message {

    private MessageType type;

    public Message(MessageType type) {
        this.type = type;
    }

}
