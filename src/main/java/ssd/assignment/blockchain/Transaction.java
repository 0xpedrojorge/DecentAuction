package ssd.assignment.blockchain;

import java.util.List;

public class Transaction {

    private short version;
    private int id;
    private final TxType type;
    private List<TxIn> inputs;
    private List<TxOut> outputs;
    private String hash;

    public Transaction(List<TxOut> outputs) {
        this.version = 1;
        this.type = TxType.COINBASE;
        this.outputs = outputs;
    }

    public Transaction(List<TxIn> inputs, List<TxOut> outputs) {
        this.version = 1;
        this.type = TxType.NORMAL;
        this.inputs = inputs;
        this.outputs = outputs;
    }

    enum TxType {
        COINBASE(0),
        NORMAL(1);

        private int value;

        TxType(int value) {
            this.value = value;
        }
    }

    class TxIn {
        private int nSequence ;
        private String outpoint; //Store corresponding output block num, tx id and input id
        private String sigScript;

        TxIn() {

        }
    }

    class TxOut {
        private int value;
        private String pubkeyScript;

        TxOut() {

        }
    }

}