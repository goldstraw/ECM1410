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

    /**
	 * Get the name of the race.
	 * 
	 * @return The name of the race.
	 * 
	 */
    public String getName() {
        return this.name;
    }

    /**
	 * Get the ID of the race.
	 * 
	 * @return The ID of the race.
	 * 
	 */
    public int getId() {
        return this.raceId;
    }
    
    /**
	 * Add a stage to the race.
	 * 
	 * @param id Stage object to add.
	 * 
	 */
    public void addStage(Stage stage) {
        this.stages.add(stage);
    }

    /**
	 * Removes a stage from the race.
	 * 
	 * @param id Stage object to remove.
	 * 
	 */
    public void removeStage(Stage stage) {
        this.stages.remove(stage);
    }

    /**
	 * Returns a list of stages in the race
	 * 
	 * @return A list containing the race's stages.
	 * 
	 */
    public Stage[] getStages() {
        return this.stages.toArray(new Stage[0]);
    }

    /**
	 * Returns a list of stage IDs in the race
	 * 
	 * @return A list containing the race's stages' IDs.
	 * 
	 */
    public int[] getStageIds() {
        int[] stageIds = new int[this.stages.size()];
        for (int i = 0; i < stageIds.length; i++) {
            stageIds[i] = this.stages.get(i).getId();
        }
        return stageIds;
    }

    /**
	 * Returns the description of the race
	 * 
	 * @return A String of the description of the race.
	 * 
	 */
    public String getDescription() {
        return this.description;
    }

    /**
	 * Return the array of indices which sorts the riders by their
     * elapsed time when accessed in the order of the first stage's
     * results.
	 * 
	 * @return An integer array containing the indices which sort
     * the riders by elapsed time.
	 * 
	 */
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

    /**
	 * Return the array of general classification times for riders
     * sorted by the riders' elapsed times.
	 * 
	 * @return An LocalTime array containing the GC times of the riders.
	 * 
	 */
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

    /**
	 * Return the array of points for riders sorted by the riders'
     * elapsed times.
	 * 
	 * @return A LocalTime array containing the points of the riders.
	 * 
	 */
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

    /**
	 * Return the array of mountain points for riders
     * sorted by the riders' elapsed times.
	 * 
	 * @return An int array containing the mountain points of the riders.
	 * 
	 */
    public int[] getRidersMountainPoints() {
        int[] order = getSortedElapsedTimeIndices();
        int[] points = new int[order.length];
        int index = 0;
        for (Integer riderId : this.stages.get(0).getRidersRanks()) {
            int riderPoints = 0;
            for (Stage stage : this.stages) {
                riderPoints += stage.getRiderMountainPoints(riderId);
            }
            points[order[index]] = riderPoints;
            index++;
        }
        return points;
    }

    /**
	 * Return the array of rider IDs sorted by the riders' elapsed times.
	 * 
	 * @return An int array containing the sorted rider IDs.
	 *
	 */
    public int[] getRidersGeneralClassificationRank() {
        int[] order = getSortedElapsedTimeIndices();
        int[] ranks = new int[order.length];
        int index = 0;
        for (Integer riderId : this.stages.get(0).getRidersRanks()) {
            ranks[order[index]] = riderId;
            index++;
        }
        return ranks;
    }

    /**
	 * Return the array of rider IDs sorted in descending order
     * by the riders' points.
	 * 
	 * @return An int array containing the sorted rider IDs.
	 *
	 */
    public int[] getRidersPointClassificationRank() {
		ArrayList<Integer> points = new ArrayList<Integer>();
        ArrayList<Integer> sortedIds = new ArrayList<Integer>();
        for (Integer riderId : this.stages.get(0).getRidersRanks()) {
            // Calculate points
            int riderPoints = 0;
            for (Stage stage : this.stages) {
                riderPoints += stage.getRiderPoints(riderId);
            }
            // Find sorted (descending) position in arraylist
            int index = 0;
            for (index = 0; index < points.size(); index++) {
                if (points.get(index) < riderPoints) {
                    break;
                }
            }
            // Insert into arraylist
            points.add(index, riderPoints);
            sortedIds.add(index, riderId);
        }

        int[] sortedArr = new int[sortedIds.size()];
        for (int i = 0; i < sortedIds.size(); i++) {
            sortedArr[i] = sortedIds.get(i).intValue();
        }
        return sortedArr;
    }

    /**
	 * Return the array of rider IDs sorted in descending order
     * by the riders' mountain points.
	 * 
	 * @return An int array containing the sorted rider IDs.
	 *
	 */
    public int[] getRidersMountainPointClassificationRank() {
        ArrayList<Integer> points = new ArrayList<Integer>();
        ArrayList<Integer> sortedIds = new ArrayList<Integer>();
        for (Integer riderId : this.stages.get(0).getRidersRanks()) {
            // Calculate points
            int riderPoints = 0;
            for (Stage stage : this.stages) {
                riderPoints += stage.getRiderMountainPoints(riderId);
            }
            // Find sorted (descending) position in arraylist
            int index = 0;
            for (index = 0; index < points.size(); index++) {
                if (points.get(index) < riderPoints) {
                    break;
                }
            }
            // Insert into arraylist
            points.add(index, riderPoints);
            sortedIds.add(index, riderId);
        }

        int[] sortedArr = new int[sortedIds.size()];
        for (int i = 0; i < sortedIds.size(); i++) {
            sortedArr[i] = sortedIds.get(i).intValue();
        }
        return sortedArr;
    }
}
