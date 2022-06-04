package ssd.assignment.auctions;

import ssd.assignment.blockchain.Wallet;

import java.util.logging.Logger;

public class Client implements Runnable{

    private static final Logger logger = Logger.getLogger(Client.class.getName());

    static Wallet wallet;


    public static void setClientWallet(Wallet wallet){
        Client.wallet= wallet;
    }


    @Override
    public void run() {


    }
}
