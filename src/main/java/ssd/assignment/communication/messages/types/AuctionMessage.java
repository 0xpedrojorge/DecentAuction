package ssd.assignment.communication.messages.types;

import lombok.Getter;
import ssd.assignment.auctions.Auction;
import ssd.assignment.communication.messages.MessageData;
import ssd.assignment.communication.messages.MessageType;

@Getter
public class AuctionMessage extends MessageData {

    private final Auction auction;

    public AuctionMessage(Auction auction) {
        this.auction = auction;
    }

    @Override
    public MessageType getType() {
        return MessageType.BROADCAST_AUCTION;
    }

}
