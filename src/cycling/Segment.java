package cycling;

/**
 * Class to represent general segments, which is extended by
 * CategorizedClimb and IntermediateSprint.
 * 
 * @author Charlie Goldstraw, Charlie MacDonald-Smith
 * @version 1.0
 *
 */

public class Segment {

    private int stageId;
    private int segmentId;
    private double location;
    private SegmentType type;

    public Segment(int stageId, int segmentId, double location, SegmentType type) {
        this.stageId = stageId;
        this.segmentId = segmentId;
        this.location = location;
        this.type = type;
    }

    /**
	 * Get the segment's stage ID.
	 * 
	 * @return The segment's stage ID.
	 * 
	 */
    public int getStageId() {
        return this.stageId;
    }

    /**
	 * Get the segment's ID.
	 * 
	 * @return The segment's ID.
	 * 
	 */
    public int getId() {
        return this.segmentId;
    }

    /**
	 * Get the segment's location.
	 * 
	 * @return The segment's location.
	 * 
	 */
    public double getLocation() {
        return this.location;
    }

    /**
	 * Get the segment's type.
	 * 
	 * @return The SegmentType of the segment.
	 * 
	 */
    public SegmentType getSegmentType() {
        return this.type;
    }
}
