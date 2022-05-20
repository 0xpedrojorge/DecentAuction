package ssd.assignment.blockchain.transaction;

import ssd.assignment.util.Crypto;
import ssd.assignment.util.Helper;

import java.util.List;

public class Transaction {

    Crypto crypto = new Crypto();

    private final byte[] id;

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
        return "Transaction: {id=" + Helper.toHexString(this.id) +
                ", version=" + this.version +
                ", type=" + this.type.name() +
                ", inputs=" + this.inputs.toString() +
                ", outputs=" + this.outputs.toString() + "}";
    }

    public byte[] getHash() {
        return crypto.hash(this.toString());
    }

    public byte[] getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public TxType getType() {
        return type;
    }

    public List<TxIn> getInputs() {
        return inputs;
    }

    public List<TxOut> getOutputs() {
        return outputs;
    }
}