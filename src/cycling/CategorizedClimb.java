package cycling;

/**
 * Class to represent categorized climb segments in stages.
 * 
 * @author Charlie Goldstraw, Charlie MacDonald-Smith
 * @version 1.0
 *
 */
public class CategorizedClimb extends Segment {
    
    private double length;
    private double averageGradient;

    public CategorizedClimb(int stageId, int segmentId, double length, double location, double averageGradient, SegmentType type) {
        super(stageId, segmentId, location, type);
        this.length = length;
        this.averageGradient = averageGradient;
    }

    /**
	 * Get the length of the climb.
	 * 
	 * @return The length of the climb.
	 * 
	 */
    public double getLength() {
        return this.length;
    }
}
