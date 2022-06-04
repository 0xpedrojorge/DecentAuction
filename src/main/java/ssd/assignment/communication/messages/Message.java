package ssd.assignment.communication.messages;

import lombok.Getter;

@Getter
public final class Message {

    private final MessageData data;

    public Message(MessageData data) {
        this.data = data;
    }

}
