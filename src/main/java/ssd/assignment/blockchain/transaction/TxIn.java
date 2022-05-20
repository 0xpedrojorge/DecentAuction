package ssd.assignment.blockchain.transaction;

public class TxIn {

    private String outpoint; // Store corresponding output block num, tx id and input id
    private int vout ; // output index
    private String scriptSig;

    TxIn() {

    }

}
