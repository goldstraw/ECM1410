package cycling;

import java.io.Serializable;

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

    public int getTeamId() {
        return this.teamId;
    }

    public int getId() {
        return this.riderId;
    }

    public String getName() {
        return this.name;
    }
}
