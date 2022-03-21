package cycling;

public class CategorizedClimb extends Segment {
    
    private double length;
    private double averageGradient;

    public CategorizedClimb(int stageId, int segmentId, double length, double location, double averageGradient, SegmentType type) {
        super(stageId, segmentId, location, type);
        this.length = length;
        this.averageGradient = averageGradient;
    }

    public double getLength() {
        return this.length;
    }
}
