package ssd.assignment.communication.kademlia.util;

import ssd.assignment.communication.kademlia.KContact;

import java.util.Comparator;

public class LastSeenNodeComparator implements Comparator<KContact> {

    @Override
    public int compare(KContact c1, KContact c2) {

        if (c1.equals(c2)) return 0;

        return c1.getLastSeen() > c2.getLastSeen() ? 1 : -1;
    }

}
