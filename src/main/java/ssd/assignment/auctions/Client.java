package ssd.assignment.auctions;

import ssd.assignment.DecentAuctionLedger;
import ssd.assignment.blockchain.Wallet;

import java.util.Scanner;
import java.util.logging.Logger;

import static javax.management.Query.not;

public class Client implements Runnable{

    private static final Logger logger = Logger.getLogger(Client.class.getName());

    static Wallet wallet;

    private static Scanner stdin=new Scanner(System.in);

    public static Bid bet(String ItemID, long Amount){
        Auction auction = LiveAuctions.getLiveAuction(ItemID);

        if(auction== null){
            System.out.println(" Auction: "+ ItemID +" not found!");
            return null;
        }
        Bid bid = new Bid(auction, wallet, Amount);

        //  anunciar a Bid
        /*if( true ) {

        }*/


        return bid;
    }

    public static void newBid(Auction auction){

        //TODO contiue

    }

    public static void setClientWallet(Wallet wallet){
        Client.wallet= wallet;
    }


    public void NewAuction(){
        System.out.println(" Create New Auction! ");
    }

    public void JoinAuctions(){
        System.out.println(" \n\n Join Auctions! \n");

        DecentAuctionLedger.getLiveAuctions().pritnLiveAuctions();

        System.out.print(" \n Witch Auction do you whish to bet? \n -> ");

        String answer = stdin.nextLine();
        String auctionID=answer;

        while(!LiveAuctions.hasAuctionItem(answer)){
            System.out.print(" Invalid Option, try again: \n -> ");
            answer=stdin.nextLine();
        }

        System.out.println("\n Currnte Bid Status: ");
        LiveAuctions.pritnLastBidForAuction(answer);

        System.out.println("\n Do you wich to Bet? (yes/no only) \n -> ");
        answer=stdin.nextLine();
        while(!answer.equals("yes") && !answer.equals("no")){
            System.out.print(" Invalid Option, try again: \n -> ");
            answer=stdin.nextLine();
        }

        if (answer.equals("no")){
            System.out.println("\n Leaving Actions! Bye! ");
            return;
        }else {
            newBid(LiveAuctions.getLiveAuction(auctionID));
        }

        stdin.nextLine();

    }

    @Override
    public void run() {
        System.out.println(" Welcome to DecentAuctions! ");
        System.out.println(" Pealse choose one of teh following option: ");

        System.out.print(" 1) New Auctions\n 2) Participate in Auction\n 3) Exit!\n -> ");
        int answer=stdin.nextInt();

        while(answer < 1 || answer>3){
            System.out.print(" Invalid Option, try again: \n -> ");
            answer=stdin.nextInt();
        }

        stdin.nextLine();

        switch (answer){
            case 1:
                NewAuction();
                break;
            case 2:
                JoinAuctions();
                break;
            case 3:
                System.exit(0);
                break;
            default:
                break;
        }






    }
}
