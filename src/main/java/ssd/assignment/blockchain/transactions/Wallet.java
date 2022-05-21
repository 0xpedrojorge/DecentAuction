package ssd.assignment.blockchain.transactions;

import ssd.assignment.DecentAuction;
import ssd.assignment.util.Crypto;

import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wallet {

    public PrivateKey privateKey;
    public PublicKey publicKey;

    public HashMap<String,TxOutput> UTXOs = new HashMap<>(); //only UTXOs owned by this wallet.

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

    //returns balance and stores the UTXO's owned by this wallet in this.UTXOs
    public float getBalance() {
        float total = 0;
        for (Map.Entry<String, TxOutput> item: DecentAuction.blockchain.UTXOs.entrySet()){
            TxOutput UTXO = item.getValue();
            if(UTXO.isMine(publicKey)) { //if output belongs to me ( if coins belong to me )
                UTXOs.put(UTXO.id,UTXO); //add it to our list of unspent transactions.
                total += UTXO.amount ;
            }
        }
        return total;
    }

    //Generates and returns a new transaction from this wallet.
    public Transaction sendFunds(PublicKey _recipient, float value ) {

        if(getBalance() < value) { //gather balance and check funds.
            System.out.println("Not Enough funds to send transaction. Transaction Discarded.");
            return null;
        }

        //create array list of inputs
        ArrayList<TxInput> inputs = new ArrayList<>();

        float total = 0;
        for (Map.Entry<String, TxOutput> item: UTXOs.entrySet()){
            TxOutput UTXO = item.getValue();
            total += UTXO.amount;
            inputs.add(new TxInput(UTXO.id));
            if(total > value) break;
        }

        Transaction newTransaction = new Transaction(publicKey, _recipient , value, inputs);
        newTransaction.generateSignature(privateKey);

        for(TxInput input: inputs){
            UTXOs.remove(input.transactionOutputId);
        }
        return newTransaction;
    }

}
