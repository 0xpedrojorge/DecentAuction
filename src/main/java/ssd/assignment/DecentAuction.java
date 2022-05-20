package ssd.assignment;

import ssd.assignment.blockchain.blocks.Block;
import ssd.assignment.blockchain.blocks.Blockchain;
import ssd.assignment.blockchain.transactions.Transaction;
import ssd.assignment.blockchain.transactions.TxInput;
import ssd.assignment.blockchain.transactions.TxOutput;
import ssd.assignment.blockchain.transactions.Wallet;
import ssd.assignment.util.Helper;
import ssd.assignment.util.Standards;

import java.util.Arrays;

import static ssd.assignment.blockchain.transactions.Transaction.calculateTxId;
import static ssd.assignment.blockchain.transactions.Transaction.checkTxId;

public class DecentAuction {

    public static void main(String[] args) {

        /*
        Blockchain blockchain = new Blockchain();
        Wallet wallet = new Wallet();

        blockchain.blocks.add(new Block((long) 0, (short )1,
                "0",
                "Hi im the first block"));
        System.out.println("Trying to Mine block 1... ");
        blockchain.blocks.get(0).mineBlock(Standards.MINING_DIFFICULTY);

        blockchain.blocks.add(new Block((long) (blockchain.blocks.size()), (short) 1,
                blockchain.blocks.get(blockchain.blocks.size()-1).getHeader().getHash(),
                "Yo im the second block"));
        System.out.println("Trying to Mine block 2... ");
        blockchain.blocks.get(1).mineBlock(Standards.MINING_DIFFICULTY);

        blockchain.blocks.add(new Block((long) (blockchain.blocks.size()), (short) 1,
                blockchain.blocks.get(blockchain.blocks.size()-1).getHeader().getHash(),
                "Hey im the third block"));
        System.out.println("Trying to Mine block 3... ");
        blockchain.blocks.get(2).mineBlock(Standards.MINING_DIFFICULTY);

        System.out.println("\nBlockchain is Valid: " + blockchain.isValid());

        System.out.println("\nThe block chain: " );
        System.out.println(blockchain.toPrettyString());
         */

        Transaction t = new Transaction((short) 0, new TxInput[]{new TxInput("txId", 0, "signature")}, new TxOutput[]{new TxOutput(0.5F, "pubkey")});
        System.out.println(t);
        System.out.println(t.getId());
        System.out.println(calculateTxId(t));
        System.out.println(checkTxId(t));

    }
}
