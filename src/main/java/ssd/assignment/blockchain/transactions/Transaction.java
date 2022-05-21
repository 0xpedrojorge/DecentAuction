package ssd.assignment.blockchain.transactions;

import com.google.gson.GsonBuilder;
import lombok.Getter;
import ssd.assignment.DecentAuction;
import ssd.assignment.util.Crypto;
import ssd.assignment.util.CustomExclusionStrategy;
import ssd.assignment.util.Helper;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

@Getter
public class Transaction {

    public String id;
    public PublicKey sender;
    public PublicKey reciepient;
    public float amount;
    public byte[] signature;

    private ArrayList<TxInput> inputs;
    private ArrayList<TxOutput> outputs= new ArrayList<>();

    private static int sequence = 0; // a rough count of how many transactions have been generated.

    public Transaction(PublicKey sender, PublicKey reciepient, float amount, ArrayList<TxInput> inputs) {
        this.sender = sender;
        this.reciepient = reciepient;
        this.amount = amount;
        this.inputs = inputs;
    }

    //Signs all the data we don't wish to be tampered with.
    public void generateSignature(PrivateKey privateKey) {
        String data = Helper.getStringFromKey(sender) + Helper.getStringFromKey(reciepient) + amount;
        signature = Crypto.sign(data, privateKey);
    }

    //Verifies the data we signed hasn't been tampered with
    public boolean verifiySignature() {
        String data = Helper.getStringFromKey(sender) + Helper.getStringFromKey(reciepient) + amount;
        return Crypto.verifySignature(data, signature, sender);
    }

    //Returns true if new transaction could be created
    public boolean processTransaction() {

        if(!verifiySignature()) {
            System.out.println("Transaction Signature failed to verify");
            return false;
        }

        //gather transaction inputs (Make sure they are unspent):
        for(TxInput i : inputs) {
            i.UTXO = DecentAuction.blockchain.UTXOs.get(i.transactionOutputId);
        }

        //generate transaction outputs:
        float leftOver = getInputsValue() - amount; //get value of inputs then the leftover change:
        id = calculateTxId(this);
        outputs.add(new TxOutput(this.reciepient, amount, id)); //send value to recipient
        outputs.add(new TxOutput(this.sender, leftOver, id)); //send the left over 'change' back to sender

        //add outputs to Unspent list
        for(TxOutput o : outputs) {
            DecentAuction.blockchain.UTXOs.put(o.id , o);
        }

        //remove transaction inputs from UTXO lists as spent:
        for(TxInput i : inputs) {
            if(i.UTXO == null) continue; //if Transaction can't be found skip it
            DecentAuction.blockchain.UTXOs.remove(i.UTXO.id);
        }

        return true;
    }

    //returns sum of inputs(UTXOs) values
    public float getInputsValue() {
        float total = 0;
        for(TxInput i : inputs) {
            if(i.UTXO == null) continue; //if Transaction can't be found skip it
            total += i.UTXO.amount;
        }
        return total;
    }

    //returns sum of outputs:
    public float getOutputsValue() {
        float total = 0;
        for(TxOutput o : outputs) {
            total += o.amount;
        }
        return total;
    }

    public static String calculateTxId(Transaction transaction) {
        String txWithoutId = new GsonBuilder().setExclusionStrategies(new CustomExclusionStrategy()).create().toJson(transaction);
        return Crypto.hash(txWithoutId);
    }

    public static boolean checkTxId(Transaction transaction) {
        return transaction.getId().equals(calculateTxId(transaction));
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }

}
