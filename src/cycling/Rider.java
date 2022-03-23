package cycling;

import java.io.Serializable;

/**
 * Class to represent riders in teams.
 * 
 * @author Charlie Goldstraw, Charlie MacDonald-Smith
 * @version 1.0
 *
 */

public class Rider implements Serializable {
    private int riderId;
    private int teamId;
    private String name;
    private int yearOfBirth;
    
    public Rider(int riderId, int teamId, String name, int yearOfBirth) {
        this.riderId = riderId;
        this.teamId = teamId;
        this.name = name;
        this.yearOfBirth = yearOfBirth;
    }

    /**
	 * Get the rider's team ID.
	 * 
	 * @return The rider's team ID.
	 * 
	 */
    public int getTeamId() {
        return this.teamId;
    }

    /**
	 * Get the rider's ID.
	 * 
	 * @return The rider's ID.
	 * 
	 */
    public int getId() {
        return this.riderId;
    }

    /**
	 * Get the rider's name.
	 * 
	 * @return The rider's name.
	 * 
	 */
    public String getName() {
        return this.name;
    }
}
