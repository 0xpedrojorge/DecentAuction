package ssd.assignment.communication.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ssd.assignment.communication.messages.types.*;

@AllArgsConstructor
@Getter
public enum MessageType {
    BRADCAST_TEST_MESSAGE(TestMessage.class),
    BROADCAST_TRANSACTION(TransactionMessage.class),
    BROADCAST_BLOCK(BlockMessage.class),
    BROADCAST_BID(BidMessage.class),
    BROADCAST_AUCTION(AuctionMessage.class),
    REQUEST_LIVE_AUCTIONS(RequestLiveAuctionMessage.class);

    private final Class<? extends MessageData> typeClass;
}
