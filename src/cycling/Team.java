package cycling;

import java.io.Serializable;
import java.util.ArrayList;

public class Team implements Serializable {
    private int teamId;
    private String name;
    private String description;
    private ArrayList<Rider> riders = new ArrayList<Rider>();
    
    public Team(int teamId, String name, String description) {
        this.teamId = teamId;
        this.name = name;
        this.description = description;
    }

    public void addRider(Rider rider) {
        this.riders.add(rider);
    }

    public void removeRider(Rider rider) {
        this.riders.remove(rider);
    }

    public Rider[] getRiders() {
        return this.riders.toArray(new Rider[0]);
    }

    public int[] getRiderIds() {
        int[] riderIds = new int[this.riders.size()];
        for (int i = 0; i < riderIds.length; i++) {
            riderIds[i] = this.riders.get(i).getId();
        }
        return riderIds;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.teamId;
    }
}
