package cycling;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;

/**
 * Class to represent stages in races.
 * 
 * @author Charlie Goldstraw, Charlie MacDonald-Smith
 * @version 1.0
 *
 */

public class Stage implements Serializable {
    private int raceId;
    private int stageId;
    private String name;
    private String description;
    private double length;
    private LocalDateTime startTime;
    private StageType type;
    private String state;
    private ArrayList<Segment> segments = new ArrayList<Segment>();
    private LinkedHashMap<Integer, ArrayList<LocalTime>> results = new LinkedHashMap<Integer, ArrayList<LocalTime>>();

    public Stage(int raceId, int stageId, String name, String description, double length, LocalDateTime startTime, StageType type) {
        this.raceId = raceId;
        this.stageId = stageId;
        this.description = description;
        this.name = name;
        this.length = length;
        this.startTime = startTime;
        this.type = type;
        this.state = "preparation";
    }

    /**
	 * Get the stage's ID.
	 * 
	 * @return The stage's ID.
	 * 
	 */
    public int getId() {
        return this.stageId;
    }

    /**
	 * Get the stage's name.
	 * 
	 * @return The stage's name.
	 * 
	 */
    public String getName() {
        return this.name;
    }

    /**
	 * Get the race's ID.
	 * 
	 * @return The stage's race ID.
	 * 
	 */
    public int getRaceId() {
        return this.raceId;
    }

    /**
	 * Get the stage's length.
	 * 
	 * @return The stage's length.
	 * 
	 */
    public double getLength() {
        return this.length;
    }

    /**
	 * Get the stage's type.
	 * 
	 * @return The stage's StageType.
	 * 
	 */
    public StageType getStageType() {
        return this.type;
    }

    /**
	 * Get the stage's state.
	 * 
	 * @return The stage's state.
	 * 
	 */
    public String getState() {
        return this.state;
    }

    /**
	 * Add a segment to the stage.
	 * 
     * @param segment The Segment object to add.
	 * 
	 */
    public void addSegment(Segment segment) {
        // Ensures that the segments are stored in chronological order
        int sortedIndex = 0;
        for (Segment comparison : this.segments) {
            if (comparison.getLocation() > segment.getLocation()) {
                break;
            }
            sortedIndex++;
        }

        this.segments.add(sortedIndex, segment);
    }

    /**
	 * Remove a segment from the stage.
	 * 
     * @param segment The Segment object to remove.
	 * 
	 */
    public void removeSegment(Segment segment) {
        this.segments.remove(segment);
    }

    /**
	 * Get the array of segments in the stage.
	 * 
     * @return The array of Segments in the stage.
	 * 
	 */
    public Segment[] getSegments() {
        return this.segments.toArray(new Segment[0]);
    }

    /**
	 * Get the array of segment IDs in the stage.
	 * 
     * @return The array of segment IDs in the stage.
	 * 
	 */
    public int[] getSegmentIds() {
        int[] segmentIds = new int[this.segments.size()];
        for (int i = 0; i < segmentIds.length; i++) {
            segmentIds[i] = this.segments.get(i).getId();
        }
        return segmentIds;
    }

    /**
	 * Set the state of the stage.
	 * 
     * @param state The state to change to.
	 * 
	 */
    public void setState(String state) {
        this.state = state;
    }

	/**
	 * Assert if the stage is not waiting for results.
	 * 
	 * @throws InvalidStageStateException If the stage is waiting for results.
	 * 
	 */
    public void assertNotWaitingForResults() throws InvalidStageStateException {
        if (this.state.equals("waiting for results")) {
			String errorMessage = "The stage was waiting for results.";
			throw new InvalidStageStateException(errorMessage);
		}
    }

	/**
	 * Assert if the stage is waiting for results.
	 * 
	 * @throws InvalidStageStateException If the stage is not waiting for results.
	 * 
	 */
    public void assertWaitingForResults() throws InvalidStageStateException {
        // Ensure the stage is waiting for results, throw an
        // InvalidStageStateException if it is.
        if (!this.state.equals("waiting for results")) {
			String errorMessage = "The stage was waiting for results.";
			throw new InvalidStageStateException(errorMessage);
		}
    }

	/**
	 * Add a rider's results to the stage.
	 * 
	 * @param riderId Rider's ID.
     * @param checkpoints The LocalTime array of checkpoints.
	 * 
	 */
    public void addResults(int riderId, LocalTime[] checkpoints) {
        ArrayList<LocalTime> resultList = new ArrayList<LocalTime>();
        for (LocalTime result : checkpoints) {
            resultList.add(result);
        }
		this.results.put(riderId, resultList);
    }

	/**
	 * Delete a rider's results from the stage
	 * 
	 * @param riderId Rider's ID.
	 * 
	 */
    public void deleteResults(int riderId) {
		this.results.remove(riderId);
    }

    /**
	 * Get a rider's results.
	 * 
	 * @param riderId Rider's ID.
	 * @return A LocalTime array of the rider's results.
	 * 
	 */
    public LocalTime[] getResults(int riderId) {
        if (!this.results.containsKey(riderId)) {
            return new LocalTime[0];
        }
        ArrayList<LocalTime> riderResults = this.results.get(riderId);
        LocalTime[] returnResults = new LocalTime[riderResults.size()-1];
        for (int i = 1; i < riderResults.size()-1; i++) {
            returnResults[i-1] = riderResults.get(i);
        }
        LocalTime elapsed = LocalTime.ofNanoOfDay(getRiderElapsedTime(riderId));
        returnResults[riderResults.size()-2] = elapsed;
        return returnResults;
    }

    /**
	 * Get a Rider's elapsed time in the stage.
	 * 
	 * @param riderId Rider's ID.
	 * @return The rider's elapsed time in nanoseconds.
	 * 
	 */
    public long getRiderElapsedTime(int riderId) {
        assert (this.results.containsKey(riderId));
        LocalTime startTime = this.results.get(riderId).get(0);
        int endIndex = this.segments.size() + 1;
        LocalTime endTime = this.results.get(riderId).get(endIndex);
        long elapsedTime = endTime.toNanoOfDay() - startTime.toNanoOfDay();
        if (elapsedTime < 0) {
            elapsedTime += 24L*60L*60L*1000000000L;
        }
        return elapsedTime;
    }

    /**
	 * Get a rider's adjusted elapsed time. If the rider finished within 1 second
     * of another rider, then both rider's have the elapsed time of the quicker
     * result.
	 * 
	 * @param riderId Rider's ID.
	 * @return The Rider's adjusted elapsed time in nanoseconds.
	 * 
	 */
    public LocalTime getRiderAdjustedElapsedTime(int riderId) {
        if (!this.results.containsKey(riderId)) {
            return null;
        }
        
        long elapsedTime = getRiderElapsedTime(riderId);
        if (this.type == StageType.TT) {
			return LocalTime.ofNanoOfDay(elapsedTime);
		}
        boolean timeAdjusted = false;
		do {
			timeAdjusted = false;
			for (Integer comparisonRiderId : this.results.keySet()) {
                long otherElapsedTime = getRiderElapsedTime(comparisonRiderId);
                long difference = elapsedTime - otherElapsedTime;
                if (difference > 0L && difference <= 1000000000L) {
                    timeAdjusted = true;
                    elapsedTime = otherElapsedTime;
                }
			}
		} while (timeAdjusted);

		return LocalTime.ofNanoOfDay(elapsedTime);
    }

    /**
	 * Return the array of indices which sorts the riders by their
     * elapsed time when accessed in the order of the stage's
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
        for (Integer riderId : this.results.keySet()) {
            long elapsedTime = getRiderElapsedTime(riderId);
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
	 * Return the array of rider's IDs when sorted by their elapsed
     * time in the stage.
	 * 
	 * @return An integer array containing the rider's IDs sorted
     * in ascending order by their elapsed time.
	 * 
	 */
    public int[] getRidersRanks() {
        int[] order = getSortedElapsedTimeIndices();
        int[] ranks = new int[this.results.size()];
        int index = 0;
        for (Integer riderId : this.results.keySet()) {
            ranks[order[index]] = riderId;
            index++;
        }
        return ranks;
    }

    /**
	 * Return the array of rider's elapsed times when sorted by their elapsed
     * time in the stage.
	 * 
	 * @return An integer array containing the rider's elapsed times sorted
     * in ascending order by their elapsed time.
	 * 
	 */
    public LocalTime[] getRankedAdjustedTimes() {
        int[] order = getSortedElapsedTimeIndices();
        LocalTime[] times = new LocalTime[this.results.size()];
        int index = 0;
        for (Integer riderId : this.results.keySet()) {
            times[order[index]] = getRiderAdjustedElapsedTime(riderId);
            index++;
        }
        return times;
    }

    /**
	 * Return the rank of the rider's finish time in the segment.
	 * 
     * @param riderId The rider's ID.
     * @param segment The segment to rank.
	 * @return An integer of the rank of the rider in the segment.
	 * 
	 */
    public int getRidersRankInSegment(int riderId, Segment segment) {
        int resultIndex = this.segments.indexOf(segment) + 1;

        long result = this.results.get(riderId).get(resultIndex).toNanoOfDay();
        int rank = 0;
        for (ArrayList<LocalTime> resultTimes : results.values()) {
            long comparison = resultTimes.get(resultIndex).toNanoOfDay();
            if (comparison < result) {
                rank++;
            }
        }
        return rank;
    }

    /**
	 * Return the array of rider's points when sorted by their elapsed
     * time in the stage.
	 * 
	 * @return An integer array containing the rider's points sorted
     * in ascending order by their elapsed time.
	 * 
	 */
    public int[] getRidersPoints() {
        HashMap<StageType, int[]> finishPoints = new HashMap<StageType, int[]>();
		finishPoints.put(StageType.FLAT, new int[] {50, 30, 20, 18, 16, 14, 12, 10, 8, 7, 6, 5, 4, 3, 2});
		finishPoints.put(StageType.MEDIUM_MOUNTAIN, new int[] {30, 25, 22, 19, 17, 15, 13, 11, 9, 7, 6, 5, 4, 3, 2});
		finishPoints.put(StageType.HIGH_MOUNTAIN, new int[] {20, 17, 15, 13, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1});
		finishPoints.put(StageType.TT, new int[] {20, 17, 15, 13, 11, 10 , 9, 8, 7, 6, 5, 4, 3, 2, 1});
		int[] sprintPoints = {20, 17, 15, 13, 11, 10 , 9, 8, 7, 6, 5, 4, 3, 2, 1};

        int[] order = getSortedElapsedTimeIndices();
        int[] points = new int[this.results.size()];
        Segment[] segments = getSegments();
        int i = 0;
        for (Integer riderId : this.results.keySet()) {
            points[order[i]] = (order[i] < 15) ? finishPoints.get(this.type)[order[i]] : 0;
            for (Segment segment : segments) {
				int rank = getRidersRankInSegment(riderId, segment);
				if (segment.getSegmentType() == SegmentType.SPRINT) {
					if (rank < 15) {
                        points[order[i]] += sprintPoints[rank];
					}
				}
			}
            i++;
        }

        return points;
    }

    /**
	 * Return the rider's points in the stage.
	 * 
	 * @return An integer of the rider's points.
	 * 
	 */
    public int getRiderPoints(int riderId) {
        int[] ranks = getRidersRanks();
        int i;
        for (i = 0; i < ranks.length; i++) {
            if (ranks[i] == riderId) {
                break;
            }
        }
        return getRidersPoints()[i];
    }

    /**
	 * Return the array of rider's mountain points when sorted by their
     * elapsed time in the stage.
	 * 
	 * @return An integer array containing the rider's mountain points
     * sorted in ascending order by their elapsed time.
	 * 
	 */
    public int[] getRidersMountainPoints() {
        HashMap<SegmentType, int[]> mountainPoints = new HashMap<SegmentType, int[]>();
		mountainPoints.put(SegmentType.C4, new int[] {1});
		mountainPoints.put(SegmentType.C3, new int[] {2, 1});
		mountainPoints.put(SegmentType.C2, new int[] {5, 3, 2, 1});
		mountainPoints.put(SegmentType.C1, new int[] {10, 8, 6, 4, 2, 1});
		mountainPoints.put(SegmentType.HC, new int[] {20, 15, 12, 10, 8, 6, 4, 2});

        int[] order = getSortedElapsedTimeIndices();
        int[] points = new int[this.results.size()];
        Segment[] segments = getSegments();
        int i = 0;
        for (Integer riderId : this.results.keySet()) {
            points[order[i]] = 0;
            for (Segment segment : segments) {
				int rank = getRidersRankInSegment(riderId, segment);
                SegmentType segmentType = segment.getSegmentType();
				if (segment.getSegmentType() != SegmentType.SPRINT) {
					if (rank < mountainPoints.get(segmentType).length) {
                        points[order[i]] += mountainPoints.get(segmentType)[rank];
					}
				}
			}
            i++;
        }

        return points;
    }

    /**
	 * Return the rider's mountain points in the stage.
	 * 
	 * @return An integer of the rider's mountain points.
	 * 
	 */
    public int getRiderMountainPoints(int riderId) {
        int[] ranks = getRidersRanks();
        int i;
        for (i = 0; i < ranks.length; i++) {
            if (ranks[i] == riderId) {
                break;
            }
        }
        return getRidersMountainPoints()[i];
    }
}
