package ssd.assignment.blockchain.miners;

import ssd.assignment.blockchain.TransactionPool;
import ssd.assignment.blockchain.blocks.Block;
import ssd.assignment.blockchain.blocks.BlockChain;
import ssd.assignment.blockchain.transactions.Transaction;
import ssd.assignment.util.Standards;

import java.util.LinkedList;

public class MiningManager {

    private final BlockChain blockChain;
    private final TransactionPool transactionPool;
    private Miner miner;
    private boolean working;

    public MiningManager(BlockChain blockchain) {
        this.blockChain = blockchain;
        this.transactionPool = blockchain.getTransactionPool();
        this.working = false;

        if (transactionPool != null) transactionPool.registerSubscriber(this::handleNewTransactionInPool);
    }

    private void handleNewTransactionInPool(int nTransactions) {
        if (transactionPool.getPoolSize() >= Standards.MIN_TRANSACTIONS_PER_BLOCK && !this.working) {
            System.out.println("Enough transactions in pool, starting to mine.");
            this.working = true;

            int n = Math.min(transactionPool.getPoolSize(), Standards.MAX_TRANSACTIONS_PER_BLOCK);
            LinkedList<Transaction> newTransactions = transactionPool.getTransactions(n);

            Block newBlock;
            if (blockChain.getLatestBlock() == null) {
                newBlock = new Block("0");
            } else {
                newBlock = new Block(blockChain.getLatestBlock().getHeader().hash);
            }
            newBlock.addTransactions(newTransactions);

            stopMiner(); // just to make sure
            working = true;

            miner = new Miner(this, newBlock);
            miner.start();
        }
    }

    public void stopMiner() {
        if (miner != null) miner.interrupt();
        working = false;
    }

    protected void notifyMinedBlock(Block newBlock) {
        blockChain.addBlock(newBlock);
        stopMiner();
    }

}
