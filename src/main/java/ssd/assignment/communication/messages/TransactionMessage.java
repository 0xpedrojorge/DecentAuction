package ssd.assignment.communication.messages;

import lombok.Getter;
import ssd.assignment.blockchain.transactions.Transaction;

@Getter
public class TransactionMessage extends Message {

    private final Transaction transaction;

    public TransactionMessage(Transaction transaction) {
        super(MessageType.BROADCAST_TRANSACTION);
        this.transaction = transaction;
    }
}
