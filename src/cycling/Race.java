package cycling;

import java.io.Serializable;
import java.util.ArrayList;

public class Race implements Serializable {
    private int raceId;
    private String name;
    private String description;
    private ArrayList<Stage> stages = new ArrayList<Stage>();
    
    public Race(int raceId, String name, String description) {
        this.raceId = raceId;
        this.description = description;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.raceId;
    }
    
    public void addStage(Stage stage) {
        this.stages.add(stage);
    }

    public void removeStage(Stage stage) {
        this.stages.remove(stage);
    }

    public Stage[] getStages() {
        return this.stages.toArray(new Stage[0]);
    }

    public int[] getStageIds() {
        int[] stageIds = new int[this.stages.size()];
        for (int i = 0; i < stageIds.length; i++) {
            stageIds[i] = this.stages.get(i).getId();
        }
        return stageIds;
    }

    public String getDescription() {
        return this.description;
    }
}
