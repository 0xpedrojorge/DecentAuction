package ssd.assignment.blockchain;

import ssd.assignment.DecentAuctionLedger;
import ssd.assignment.blockchain.transactions.Transaction;
import ssd.assignment.blockchain.transactions.TxInput;
import ssd.assignment.blockchain.transactions.TxOutput;
import ssd.assignment.communication.messages.types.TransactionMessage;
import ssd.assignment.util.Crypto;

import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wallet {

    public PrivateKey privateKey;
    public PublicKey publicKey;
    public String walletOwner;

    public HashMap<String, TxOutput> UTXOs = new HashMap<>();

    public Wallet() {
        generateKeyPair();
    }

    public void generateKeyPair() {
        KeyPair keyPair = Crypto.generateKeyPair();
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();
    }

    public String getPublicKeyHash() {
        return Crypto.hash(publicKey.toString());
    }

    public float getBalance() {
        float total = 0;
        for (Map.Entry<String, TxOutput> item: DecentAuctionLedger.getBlockchain().getUTXOs().entrySet()){
            TxOutput UTXO = item.getValue();
            if(UTXO.isMine(publicKey)) { //if output belongs to me ( if coins belong to me )
                UTXOs.put(UTXO.id,UTXO); //add it to our list of unspent transactions.
                total += UTXO.amount ;
            }
        }
        return total;
    }


    public Transaction createTransaction(PublicKey recipient, float amount) {

        if(getBalance() < amount) {
            System.out.println("Not Enough funds to send transaction. Transaction Discarded.");
            return null;
        }

        ArrayList<TxInput> inputs = new ArrayList<>();

        float total = 0;
        for (Map.Entry<String, TxOutput> item: UTXOs.entrySet()){
            TxOutput UTXO = item.getValue();
            total += UTXO.amount;
            inputs.add(new TxInput(UTXO.id));
            if(total > amount) break;
        }

        Transaction newTransaction = new Transaction(publicKey, recipient , amount, inputs);
        newTransaction.generateSignature(privateKey);

        for(TxInput input: inputs){
            UTXOs.remove(input.transactionOutputId);
        }

        //------------------------

        //gather transaction inputs (Make sure they are unspent):
        for(TxInput i : inputs) {
            i.UTXO = DecentAuctionLedger.getBlockchain().getUTXOs().get(i.transactionOutputId);
        }

        //generate transaction outputs:
        float leftOver = newTransaction.getInputsAmount() - amount; //get value of inputs then the leftover change:
        newTransaction.id = Transaction.calculateTxId(newTransaction);
        newTransaction.getOutputs().add(new TxOutput(newTransaction.getReciepient(), amount, newTransaction.getId()));
        newTransaction.getOutputs().add(new TxOutput(newTransaction.getSender(), leftOver, newTransaction.getId()));

        //add outputs to Unspent list
        for(TxOutput o : newTransaction.getOutputs()) {
            DecentAuctionLedger.getBlockchain().getUTXOs().put(o.id , o);
        }

        //remove transaction inputs from UTXO lists as spent:
        for(TxInput i : inputs) {
            if(i.UTXO == null) continue;
            DecentAuctionLedger.getBlockchain().getUTXOs().remove(i.UTXO.id);
        }

        //TODO broadcast transaction
        TransactionMessage transactionMessage = new TransactionMessage(newTransaction);
        DecentAuctionLedger.getMessageManager().publishMessage(transactionMessage);

        return newTransaction;
    }

}
