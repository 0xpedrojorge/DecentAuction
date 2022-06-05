package ssd.assignment.communication.messages.types;

import lombok.Getter;
import ssd.assignment.auctions.LiveAuction;
import ssd.assignment.communication.messages.MessageData;
import ssd.assignment.communication.messages.MessageType;

@Getter
public class AuctionMessage extends MessageData {

    private final LiveAuction liveAuction;

    public AuctionMessage(LiveAuction liveAuction) {
        this.liveAuction = liveAuction;
    }

    @Override
    public MessageType getType() {
        return MessageType.BROADCAST_AUCTION;
    }

}
