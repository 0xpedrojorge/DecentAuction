import org.junit.Test;

public class BlockchainTests {

    //startBlockchain();

        /*
        //Create wallets:
        Wallet walletA = new Wallet();
        Wallet walletB = new Wallet();
        Wallet coinbase = new Wallet();

        //create genesis transaction, which sends 100 NoobCoin to walletA:
        Transaction genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 100f, null);
        genesisTransaction.generateSignature(coinbase.privateKey);
        genesisTransaction.id = "0";
        genesisTransaction.getOutputs().add(new TxOutput(genesisTransaction.reciepient, genesisTransaction.amount, genesisTransaction.id));
        blockchain.getUTXOs().put(genesisTransaction.getOutputs().get(0).id, genesisTransaction.getOutputs().get(0));
        blockchain.getTransactionPool().addTransaction(genesisTransaction);

        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");

        blockchain.getTransactionPool().addTransaction(walletA.createTransaction(walletB.publicKey, 40f));
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
        blockchain.getTransactionPool().addTransaction(walletA.createTransaction(walletB.publicKey, 1000f));
        System.out.println("Pending transactions: " + blockchain.getTransactionPool().getPoolSize());
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
        blockchain.getTransactionPool().addTransaction(walletA.createTransaction(walletB.publicKey, 20f));
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.print("Is blockchain valid? " + blockchain.isValid());

        //System.out.println(blockchain.toPrettyString());

         */
}