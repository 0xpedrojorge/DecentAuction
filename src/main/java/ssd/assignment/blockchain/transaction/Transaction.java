package ssd.assignment.blockchain.transaction;

import ssd.assignment.util.Crypto;

import java.util.List;

public class Transaction {

    private final String id;
    private final int version;
    private final TxType type;

    private final List<TxIn> inputs;
    private final List<TxOut> outputs;

    public Transaction(int version, TxType type, List<TxIn> inputs, List<TxOut> outputs) {
        this.version = version;
        this.type = type;
        this.inputs = inputs;
        this.outputs = outputs;

        this.id = this.getHash();
    }

    @Override
    public String toString() {
        return "Transaction: {id=" + this.id +
                ", version=" + this.version +
                ", type=" + this.type.name() +
                ", inputs=" + this.inputs.toString() +
                ", outputs=" + this.outputs.toString() + "}";
    }

    public String getHash() {
        Crypto crypto = new Crypto();
        return crypto.hash(this.toString());
    }
}