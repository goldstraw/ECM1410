package cycling;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;

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

    public int getId() {
        return this.stageId;
    }

    public String getName() {
        return this.name;
    }

    public int getRaceId() {
        return this.raceId;
    }

    public double getLength() {
        return this.length;
    }

    public StageType getStageType() {
        return this.type;
    }

    public String getState() {
        return this.state;
    }

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

    public void removeSegment(Segment segment) {
        this.segments.remove(segment);
    }

    public Segment[] getSegments() {
        return this.segments.toArray(new Segment[0]);
    }

    public int[] getSegmentIds() {
        int[] segmentIds = new int[this.segments.size()];
        for (int i = 0; i < segmentIds.length; i++) {
            segmentIds[i] = this.segments.get(i).getId();
        }
        return segmentIds;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void assertNotWaitingForResults() throws InvalidStageStateException {
        // Ensure the stage isn't waiting for results, throw an
        // InvalidStageStateException if it is.
        if (this.state.equals("waiting for results")) {
			String errorMessage = "The stage was waiting for results.";
			throw new InvalidStageStateException(errorMessage);
		}
    }

    public void assertWaitingForResults() throws InvalidStageStateException {
        // Ensure the stage is waiting for results, throw an
        // InvalidStageStateException if it is.
        if (!this.state.equals("waiting for results")) {
			String errorMessage = "The stage was waiting for results.";
			throw new InvalidStageStateException(errorMessage);
		}
    }

    public void addResults(int riderId, LocalTime[] checkpoints) {
        ArrayList<LocalTime> resultList = new ArrayList<LocalTime>();
        for (LocalTime result : checkpoints) {
            resultList.add(result);
        }
		this.results.put(riderId, resultList);
    }

    public void deleteResults(int riderId) {
		this.results.remove(riderId);
    }

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

    public long getRiderElapsedTime(int riderId) {
        LocalTime startTime = this.results.get(riderId).get(0);
        int endIndex = this.segments.size() + 1;
        LocalTime endTime = this.results.get(riderId).get(endIndex);
        long elapsedTime = endTime.toNanoOfDay() - startTime.toNanoOfDay();
        if (elapsedTime < 0) {
            elapsedTime += 24L*60L*60L*1000000000L;
        }
        return elapsedTime;
    }

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

    public int[] getSortedElapsedTimeIndices() {
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
}
