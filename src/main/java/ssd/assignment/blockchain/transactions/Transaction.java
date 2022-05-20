package ssd.assignment.blockchain.transactions;

import com.google.gson.GsonBuilder;
import lombok.Getter;
import ssd.assignment.util.Crypto;
import ssd.assignment.util.CustomExclusionStrategy;
import ssd.assignment.util.Helper;

@Getter
public class Transaction {

    private final String id;
    private final short version;

    private final TxInput[] inputs;
    private final TxOutput[] outputs;

    public Transaction(short version, TxInput[] inputs, TxOutput[] outputs) {
        this.version = version;
        this.inputs = inputs;
        this.outputs = outputs;

        this.id = calculateTxId(this);
    }



    public static String calculateTxId(Transaction transaction) {
        String txWithoutId = new GsonBuilder().setExclusionStrategies(new CustomExclusionStrategy()).create().toJson(transaction);
        return Helper.toHexString(Crypto.hash(txWithoutId));
    }

    public static boolean checkTxId(Transaction transaction) {
        return transaction.getId().equals(calculateTxId(transaction));
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }

}
