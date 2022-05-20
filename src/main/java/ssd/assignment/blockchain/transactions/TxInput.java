package ssd.assignment.blockchain.transactions;

import com.google.gson.GsonBuilder;

public class TxInput {

    private final String transactionId;
    private final int outputIndex;
    private final String scriptSig;

    public TxInput(String transactionId, int outputIndex, String scriptSig) {
        this.transactionId = transactionId;
        this.outputIndex = outputIndex;
        this.scriptSig = scriptSig;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }
}
