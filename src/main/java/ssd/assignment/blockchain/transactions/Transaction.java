package ssd.assignment.blockchain.transactions;

import com.google.gson.GsonBuilder;
import lombok.Getter;
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

    private final ArrayList<TxInput> inputs;
    private final ArrayList<TxOutput> outputs;

    public Transaction(PublicKey sender, PublicKey reciepient, float amount, ArrayList<TxInput> inputs) {
        this.sender = sender;
        this.reciepient = reciepient;
        this.amount = amount;
        this.inputs = inputs;
        this.outputs = new ArrayList<>();
    }

    public void generateSignature(PrivateKey privateKey) {
        String data = Helper.getStringFromKey(sender) + Helper.getStringFromKey(reciepient) + amount;
        signature = Crypto.sign(data, privateKey);
    }

    public boolean verifiySignature() {
        String data = Helper.getStringFromKey(sender) + Helper.getStringFromKey(reciepient) + amount;
        return Crypto.verifySignature(data, signature, sender);
    }

    public float getInputsAmount() {
        float total = 0;
        for(TxInput i : inputs) {
            if(i.UTXO == null) continue; //if Transaction can't be found skip it
            total += i.UTXO.amount;
        }
        return total;
    }

    public float getOutputsAmount() {
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
