package ssd.assignment.communication.messages.types;

import lombok.Getter;
import ssd.assignment.auctions.Bid;
import ssd.assignment.communication.messages.MessageData;
import ssd.assignment.communication.messages.MessageType;

@Getter
public class RequestPaymentMessage extends MessageData {

    private final Bid bid;

    public RequestPaymentMessage(Bid bid) {
        this.bid = bid;
    }

    @Override
    public MessageType getType() {
        return MessageType.REQUEST_PAYMENT;
    }
}
