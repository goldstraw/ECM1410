package cycling;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class to represent teams in the cycling portal.
 * 
 * @author Charlie Goldstraw, Charlie MacDonald-Smith
 * @version 1.0
 *
 */

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

    /**
	 * Add a Rider to the team.
	 * 
	 * @param rider The Rider object to add.
	 * 
	 */
    public void addRider(Rider rider) {
        this.riders.add(rider);
    }

    /**
	 * Remove a Rider from the team.
	 * 
	 * @param rider The Rider object to remove.
	 * 
	 */
    public void removeRider(Rider rider) {
        this.riders.remove(rider);
    }

    /**
	 * Returns an array of riders in the team.
     *
	 * @return The Rider array containing the team riders.
	 * 
	 */
    public Rider[] getRiders() {
        return this.riders.toArray(new Rider[0]);
    }

    /**
	 * Get an array of the teams rider IDs.
	 * 
	 * @return The IDs of the riders in an array.
	 * 
	 */
    public int[] getRiderIds() {
        int[] riderIds = new int[this.riders.size()];
        for (int i = 0; i < riderIds.length; i++) {
            riderIds[i] = this.riders.get(i).getId();
        }
        return riderIds;
    }

    /**
	 * Get the name of the team.
	 * 
	 * @return The String containing the name of the team.
	 * 
	 */
    public String getName() {
        return this.name;
    }

    /**
	 * Get the ID of the team.
	 * 
	 * @return The int of the team's ID.
	 * 
	 */
    public int getId() {
        return this.teamId;
    }
}
