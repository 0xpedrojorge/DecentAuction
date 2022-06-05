package ssd.assignment.blockchain;

import ssd.assignment.blockchain.transactions.Transaction;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class TransactionPool {

    private final List<Transaction> pendingTransactions;
    private final List<Consumer<Integer>> consumers;

    public TransactionPool() {
        this.pendingTransactions = new ArrayList<>();
        this.consumers = new LinkedList<>();
    }

    public void addTransaction(Transaction transaction) {
        if (transaction == null) return;

        pendingTransactions.add(transaction);
        for (Consumer<Integer> consumer : consumers) {
            consumer.accept(pendingTransactions.size());
        }
    }

    public LinkedList<Transaction> getTransactions(int n) {
        LinkedList<Transaction> transactionsToRemove = new LinkedList<>();
        for (int i = 0; i < n && !pendingTransactions.isEmpty(); i++) {
            //if (pendingTransactions.get(0).validateTransaction()) {
                transactionsToRemove.add(pendingTransactions.remove(0));
            //}
        }
        return transactionsToRemove;
    }

    public int getPoolSize() {
        return pendingTransactions.size();
    }

    public void registerSubscriber(Consumer<Integer> consumer) {
        consumers.add(consumer);
    }

}
