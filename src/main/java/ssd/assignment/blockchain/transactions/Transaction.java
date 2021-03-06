package ssd.assignment.blockchain.transactions;

import com.google.gson.GsonBuilder;
import lombok.Getter;
import ssd.assignment.DecentAuctionLedger;
import ssd.assignment.blockchain.blocks.Block;
import ssd.assignment.util.Crypto;
import ssd.assignment.util.CustomExclusionStrategy;
import ssd.assignment.util.Standards;
import ssd.assignment.util.Utils;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;

@Getter
public class Transaction implements Serializable {

    public String id;
    public transient PublicKey sender;
    public transient PublicKey reciepient;
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
        String data = Utils.getStringFromKey(sender) + Utils.getStringFromKey(reciepient) + amount;
        signature = Crypto.sign(data, privateKey);
    }

    public boolean verifiySignature() {
        String data = Utils.getStringFromKey(sender) + Utils.getStringFromKey(reciepient) + amount;
        return Crypto.verifySignature(data, signature, sender);
    }


    public boolean validateTransaction() {
        if(!this.verifiySignature()) {
            return false;
        }

        if(this.getInputsAmount() != this.getOutputsAmount()) {
            return false;
        }

        for(TxInput input : this.getInputs()) {
            TxOutput tempOutput = DecentAuctionLedger.getBlockchain().getUTXOs().get(input.transactionOutputId);

            if (tempOutput == null) {
                return false;
            }

            if (input.unspentTransactionOutput.amount != tempOutput.amount) {
                return false;
            }

            DecentAuctionLedger.getBlockchain().getUTXOs().remove(input.transactionOutputId);
        }
        return true;
    }

    public float getInputsAmount() {
        float total = 0;
        for(TxInput i : inputs) {
            if(i.unspentTransactionOutput == null) continue;
            total += i.unspentTransactionOutput.amount;
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
        return new GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).create().toJson(this);
    }
}
