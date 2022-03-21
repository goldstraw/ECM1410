package cycling;

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

    public int getStageId() {
        return this.stageId;
    }

    public int getId() {
        return this.segmentId;
    }

    public double getLocation() {
        return this.location;
    }

    public SegmentType getSegmentType() {
        return this.type;
    }
}
