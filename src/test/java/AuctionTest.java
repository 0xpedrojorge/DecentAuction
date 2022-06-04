import org.junit.Test;
import ssd.assignment.auctions.Auction;
import ssd.assignment.auctions.Client;
import ssd.assignment.auctions.AuctionManager;
import ssd.assignment.blockchain.Wallet;

public class AuctionTest {

    @Test
    public void TestAuction(){

        Wallet wallet = new Wallet();

        Auction auction = new Auction(wallet,"banana", "José", 1L, 0.5F, (long) 0.1, 1);

        AuctionManager auctionManager = new AuctionManager();

        Runnable run = new Client();



    }


}
