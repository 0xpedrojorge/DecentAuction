package ssd.assignment.communication.messages.types;

import ssd.assignment.communication.messages.MessageData;
import ssd.assignment.communication.messages.MessageType;

public class RequestLiveAuctionMessage extends MessageData {

    @Override
    public MessageType getType() {
        return MessageType.REQUEST_LIVE_AUCTIONS;
    }
}
