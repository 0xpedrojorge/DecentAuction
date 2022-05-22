package ssd.assignment.blockchain;

import ssd.assignment.blockchain.transactions.Transaction;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TransactionPool {

    private final List<Transaction> pendingTransactions;

    public TransactionPool() {
        this.pendingTransactions = new ArrayList<>();
    }

    //Add transactions to the pool
    public boolean addTransaction(Transaction transaction) {
        if(transaction == null) return false;

        pendingTransactions.add(transaction);
        System.out.println("Transaction Successfully added to the pool");
        return true;
    }

    // Get at most N transactions from pool
    public LinkedList<Transaction> getTransactions(int n) {
        LinkedList<Transaction> transactionsToRemove = new LinkedList<>();
        for (int i = 0; i < n && !pendingTransactions.isEmpty(); i++) {
            transactionsToRemove.add(pendingTransactions.remove(0));
        }
        return transactionsToRemove;
    }

    public int getPoolSize() {
        return pendingTransactions.size();
    }

}
