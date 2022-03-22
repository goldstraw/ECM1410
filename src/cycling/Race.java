package cycling;

import java.io.Serializable;
import java.util.ArrayList;
import java.time.LocalTime;

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

    private int[] getSortedElapsedTimeIndices() {
		ArrayList<Long> results = new ArrayList<Long>();
        ArrayList<Integer> sortedIndices = new ArrayList<Integer>();
        int unsortedIndex = 0;
        for (Integer riderId : this.stages.get(0).getRidersRanks()) {
            long elapsedTime = 0;
            for (Stage stage : this.stages) {
                elapsedTime += stage.getRiderAdjustedElapsedTime(riderId).toNanoOfDay();
            }
            int index = 0;
            for (index = 0; index < results.size(); index++) {
                if (results.get(index) > elapsedTime) {
                    break;
                }
            }
            results.add(index, elapsedTime);
            sortedIndices.add(index, unsortedIndex);
            unsortedIndex++;
        }

        int[] sortedArr = new int[sortedIndices.size()];
        for (int i = 0; i < sortedIndices.size(); i++) {
            sortedArr[i] = sortedIndices.get(i).intValue();
        }
        return sortedArr;
    }

    public LocalTime[] getGeneralClassificationTimes() {
        int[] order = getSortedElapsedTimeIndices();
        LocalTime[] times = new LocalTime[order.length];
        int index = 0;
        for (Integer riderId : this.stages.get(0).getRidersRanks()) {
            long elapsedTime = 0;
            for (Stage stage : this.stages) {
                elapsedTime += stage.getRiderAdjustedElapsedTime(riderId).toNanoOfDay();
            }
            times[order[index]] = LocalTime.ofNanoOfDay(elapsedTime);
            index++;
        }
        return times;
    }

    public int[] getRidersPoints() {
        int[] order = getSortedElapsedTimeIndices();
        int[] points = new int[order.length];
        int index = 0;
        for (Integer riderId : this.stages.get(0).getRidersRanks()) {
            int riderPoints = 0;
            for (Stage stage : this.stages) {
                riderPoints += stage.getRiderPoints(riderId);
            }
            points[order[index]] = riderPoints;
            index++;
        }
        return points;
    }
}
