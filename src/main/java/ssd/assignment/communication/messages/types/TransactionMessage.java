package ssd.assignment.communication.messages.types;

import lombok.Getter;
import ssd.assignment.blockchain.transactions.Transaction;
import ssd.assignment.communication.messages.MessageData;
import ssd.assignment.communication.messages.MessageType;

@Getter
public class TransactionMessage extends MessageData {

    private final Transaction transaction;

    public TransactionMessage(Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public MessageType getType() {
        return MessageType.BROADCAST_TRANSACTION;
    }
}
