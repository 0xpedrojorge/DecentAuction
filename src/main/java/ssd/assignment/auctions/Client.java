package ssd.assignment.auctions;

import ssd.assignment.blockchain.Wallet;

import java.util.Scanner;
import java.util.logging.Logger;

import static ssd.assignment.DecentAuctionLedger.auctionManager;

public class Client implements Runnable {

    private static final Logger logger = Logger.getLogger(Client.class.getName());

    static Wallet wallet;

    private static final Scanner stdin=new Scanner(System.in);

    public static void newBid(LiveAuction liveauction){

        System.out.print(" \n Menu to Bid Item: "+ liveauction.auction.getItemID()+"\n");
        System.out.println(" By the Seller: "+liveauction.auction.getSellerID());
        System.out.println(" The Start Amount is: "+liveauction.auction.getMinAmount());
        if(liveauction.getLastBid()==null){
            System.out.println(" The Current Amount is: yet to be started! ");
        }
        else{
            System.out.println(" The Current Amount is: "+liveauction.getLastBid().getAmount());
        }
        System.out.println(" The Minimun increment Amount is: "+liveauction.auction.getMinIncrement());
        System.out.print(" Place your bid amount: ");
        String aux = stdin.nextLine();
        Float newAmount = Float.parseFloat(aux);

        if(liveauction.getLastBid()!=null){
            float currAmount=liveauction.getLastBid().getAmount();
            float minIncrement= liveauction.auction.getMinIncrement();
            float soma = currAmount+minIncrement;

            System.out.print(" Min amount alowed: "+ soma);
            while (newAmount<=(liveauction.getLastBid().getAmount()+liveauction.auction.getMinIncrement())){
                System.out.println(" your Amount:"+liveauction.getLastBid().getAmount()+" is not enough, place a bigger amount: ");
                aux = stdin.nextLine();
                newAmount = Float.parseFloat(aux);
            }

        }
        else{
            while (newAmount<=(liveauction.auction.getMinAmount()+liveauction.auction.getMinIncrement())){
                System.out.print(" your Amount:"+newAmount+"€ is not enough, place a bigger amount: ");
                aux = stdin.nextLine();
                newAmount = Float.parseFloat(aux);
            }
        }


        Bid currentBid = new Bid(liveauction.auction.getItemID(),liveauction.auction.getSellerID(),wallet.walletOwner ,newAmount, wallet.publicKey, wallet.getPublicKeyHash());
        auctionManager.addLocalBid(currentBid);

        System.out.print(" Successfully Bided! ");

        System.out.println(" Your Bid: "+currentBid);
        System.out.print(" Press Double enter to return to the menu. ");
        stdin.nextLine();
    }

    public static void setClientWallet(Wallet wallet){
        Client.wallet= wallet;
    }


    public void NewAuction(){
        System.out.println(" Create New Auction! \n");
        System.out.print(" Insert Item Name: ");
        String ItemID=stdin.nextLine();
        System.out.print(" Minimun Amount: ");
        String aux = stdin.nextLine();
        Long minAmount=Long.parseLong(aux);
        System.out.print(" Minimun Increment: ");
        aux = stdin.nextLine();
        Float minIncrement=Float.parseFloat(aux);
        System.out.print(" Duration of the Auction (in min): ");
        aux = stdin.nextLine();
        Long timeout=Long.parseLong(aux);

        Auction auction=new Auction(ItemID,wallet.walletOwner, minAmount, minIncrement,timeout, wallet.publicKey, wallet.getPublicKeyHash());

        auctionManager.addLocalAuction(auction);

        System.out.print(" Successfully Added a new Auction! ");

        System.out.println(" Your Auction: "+auction.toString());

        System.out.print(" Press Double enter to return to the menu. ");
        stdin.nextLine();

    }

    public void JoinAuctions(){
        System.out.println(" \n\n Join Auctions! \n");

        //TODO Rectify origin LiveAuction
        auctionManager.pritnLiveAuctions();

        System.out.print(" \n Which Auction do you whish to bet? \n -> ");

        String answer = stdin.nextLine();
        String auctionID = answer;

        while(!auctionManager.hasAuctionItem(answer)) {
            System.out.print(" Invalid Option, try again: \n -> ");
            answer = stdin.nextLine();
        }

        System.out.println("\n Currente Bid Status: ");
        auctionManager.pritnLastBidForAuction(answer);

        System.out.print("\n Do you wich to Bet? (yes/no only) \n -> ");
        answer=stdin.nextLine();
        while(!answer.equals("yes") && !answer.equals("no")){
            System.out.print(" Invalid Option, try again: \n -> ");
            answer=stdin.nextLine();
        }

        if (answer.equals("no")){
            System.out.println("\n Leaving Actions! Bye! ");
            return;
        }else {
            newBid(auctionManager.getLiveAuction(auctionID));
        }

        stdin.nextLine();

    }

    @Override
    public void run() {
        while(true) {
            System.out.println(" \n\n Welcome to DecentAuctions! ");
            System.out.println(" Please choose one of the following option: ");

            System.out.print(" 1) New Auctions\n 2) Participate in Auction\n 3) Print Publik Key\n 4) Exit!\n -> ");
            int answer = stdin.nextInt();

            while (answer < 1 || answer > 4) {
                System.out.print(" Invalid Option, try again: \n -> ");
                answer = stdin.nextInt();
            }

            stdin.nextLine();

            switch (answer) {
                case 1:
                    NewAuction();
                    break;
                case 2:
                    JoinAuctions();
                    break;
                case 3:
                    System.out.println("Wallet PulicKey: " + wallet.publicKey);
                    break;
                case 4:
                    System.exit(0);
                    break;
                default:
                    break;
            }
        }
    }
}
