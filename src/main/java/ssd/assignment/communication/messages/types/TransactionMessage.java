package ssd.assignment.communication.messages.types;

import lombok.Getter;
import ssd.assignment.blockchain.transactions.Transaction;
import ssd.assignment.communication.messages.Message;
import ssd.assignment.communication.messages.MessageType;

@Getter
public class TransactionMessage extends Message {

    private final Transaction transaction;

    public TransactionMessage(Transaction transaction) {
        super(MessageType.BROADCAST_TRANSACTION);
        this.transaction = transaction;
    }
}
