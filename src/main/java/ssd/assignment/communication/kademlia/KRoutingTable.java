package ssd.assignment.communication.kademlia;

import ssd.assignment.util.Standards;

public class KRoutingTable {

    private final NetworkNode MyNode;
    private transient KBucket[] buckets;


    public KRoutingTable(NetworkNode self) {
        MyNode = self;
        this.iniciate();


    }


    public final void iniciate(){
        this.buckets = new KBucket[Standards.B];

        for(int i=0;i<Standards.B;i++){
            buckets[i]=new KBucket(i);
        }
    }

    public final void insert(NetworkNode n){

        //TODO check this.buckets[this.getBucketId(n.getNodeId())].insert(n);

    }


    public final int getBucketId(byte[] nid)
    {
        int bId = this.MyNode.getDistance(nid) - 1;
        return Math.max(bId, 0);
    }



}
