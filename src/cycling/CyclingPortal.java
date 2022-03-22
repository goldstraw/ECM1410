package cycling;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.time.LocalDateTime;
import java.time.LocalTime;

import java.util.HashMap;

/**
 * CyclingPortal implements all of the functions in the CyclingPortalInterface.
 * 
 * @author Charlie Goldstraw, Charlie MacDonald-Smith
 * @version 1.0
 *
 */
public class CyclingPortal implements CyclingPortalInterface {
	
	private int nextId = 0;
	private HashMap<Integer, Race> races = new HashMap<Integer, Race>();
	private HashMap<Integer, Team> teams = new HashMap<Integer, Team>();

	/**
	 * Get a Race object by its ID.
	 * 
	 * @param id Race's ID.
	 * @throws IDNotRecognisedException If the ID does not match to any race in the
	 *                                  system.
	 * @return The Race object with the given ID.
	 * 
	 */
	public Race getRace(int id) throws IDNotRecognisedException {
		if (!races.containsKey(id)) {
			String errorMessage = String.format("Race ID '%d' did not exist.", id);
			throw new IDNotRecognisedException(errorMessage);
		}
		return races.get(id);
	}

	/**
	 * Get a Stage object by its ID.
	 * 
	 * @param id Stage's ID.
	 * @throws IDNotRecognisedException If the ID does not match to any stage in the
	 *                                  system.
	 * @return The Stage object with the given ID.
	 * 
	 */
	public Stage getStage(int id) throws IDNotRecognisedException {
		for (Race race : races.values()) {
			for (Stage stage : race.getStages()) {
				if (stage.getId() == id) {
					return stage;
				}
			}
		}
		String errorMessage = String.format("Stage ID '%d' did not exist.", id);
		throw new IDNotRecognisedException(errorMessage);
	}

	/**
	 * Get a Segment object by its ID.
	 * 
	 * @param id Segment's ID.
	 * @throws IDNotRecognisedException If the ID does not match to any segment in the
	 *                                  system.
	 * @return The Segment object with the given ID.
	 * 
	 */
	public Segment getSegment(int id) throws IDNotRecognisedException {
		for (Race race : races.values()) {
			for (Stage stage : race.getStages()) {
				for (Segment segment : stage.getSegments()) {
					if (segment.getId() == id) {
						return segment;
					}
				}
			}
		}
		String errorMessage = String.format("Segment ID '%d' did not exist.", id);
		throw new IDNotRecognisedException(errorMessage);
	}

	/**
	 * Get a Team object by its ID.
	 * 
	 * @param id Team's ID.
	 * @throws IDNotRecognisedException If the ID does not match to any team in the
	 *                                  system.
	 * @return The Team object with the given ID.
	 * 
	 */
	public Team getTeam(int id) throws IDNotRecognisedException {
		if (!teams.containsKey(id)) {
			String errorMessage = String.format("Team ID '%d' did not exist.", id);
			throw new IDNotRecognisedException(errorMessage);
		}
		return teams.get(id);
	}

	/**
	 * Get a Rider object by its ID.
	 * 
	 * @param id Rider's ID.
	 * @throws IDNotRecognisedException If the ID does not match to any rider in the
	 *                                  system.
	 * @return The Rider object with the given ID.
	 * 
	 */
	public Rider getRider(int id) throws IDNotRecognisedException {
		for (Team team : teams.values()) {
			for (Rider rider : team.getRiders()) {
				if (rider.getId() == id) {
					return rider;
				}
			}
		}
		String errorMessage = String.format("Rider ID '%d' did not exist.", id);
		throw new IDNotRecognisedException(errorMessage);
	}

	/**
	 * Perform checks upon a name to ensure it is unique in the system, it is not empty,
	 * it is shorter than 30 characters, and it doesn't have any spaces.
	 * 
	 * @param name Name to validate.
	 * @throws IllegalNameException If the name already exists in the system.
	 * @throws InvalidNameException If the name does not match the formatting required.
	 * 
	 */
	public void validateName(String name) throws IllegalNameException, InvalidNameException {
		boolean usedName = false;
		for (Race race : races.values()) {
			if (race.getName().equals(name)) {
				usedName = true;
			}
			for (Stage stage : race.getStages()) {
				if (stage.getName().equals(name)) {
					usedName = true;
				}
			}
		}
		for (Team team : teams.values()) {
			if (team.getName().equals(name)) {
				usedName = true;
			}
			for (Rider rider : team.getRiders()) {
				if (rider.getName().equals(name)) {
					usedName = true;
				}
			}
		}

		if (usedName) {
			String errorMessage = String.format("The name '%s' already exists.", name);
			throw new IllegalNameException(errorMessage);
		}
		if (name == null || name.length() == 0) {
			throw new InvalidNameException("The name was empty.");
		}
		if (name.length() > 30) {
			throw new InvalidNameException("The name was too long. (30 char limit).");
		}
		if (name.contains(" ")) {
			throw new InvalidNameException("The name contains spaces.");
		}
	}

	/**
	 * Check if a proposed segment is within the stage's boundaries, and the relevant
	 * stage is not a time trial or "waiting for results".
	 * 
	 * @param stageId The ID of the stage.
	 * @param location The location of the end of segment.
	 * @param type The SegmentType of the proposed segment.
	 * @param length The length of the segment.
	 * @throws IDNotRecognisedException If the ID does not match to any stage in the
	 *                                  system.
	 * @throws InvalidLocationException If the segment is not within the stage's bounds.
	 * @throws InvalidStageStateException If the stage is "waiting for results".
	 * @throws InvalidStageTypeException If the stage is a time trial.
	 * 
	 */
	public void validateSegmentAddition(int stageId, Double location, SegmentType type,
			Double length) throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException,
			InvalidStageTypeException {
		Stage stage = getStage(stageId);
		if (location > stage.getLength() || location-length < 0) {
			String errorMessage = "The location of the segment was invalid.";
			throw new InvalidLocationException(errorMessage);
		}
		stage.assertNotWaitingForResults();
		if (stage.getStageType() == StageType.TT) {
			String errorMessage = "Time trials cannot have segments.";
			throw new InvalidStageTypeException(errorMessage);
		}
	}

	@Override
	public int[] getRaceIds() {
		int[] raceIds = new int[races.size()];
		int i = 0;
		for (int id : races.keySet()) {
			raceIds[i] = id;
			i++;
		}
		return raceIds;
	}

	@Override
	public int createRace(String name, String description) throws IllegalNameException, InvalidNameException {
		validateName(name);

		int raceId = nextId++;
		Race newRace = new Race(raceId, name, description);
		races.put(raceId, newRace);
		return raceId;
	}

	@Override
	public String viewRaceDetails(int raceId) throws IDNotRecognisedException {
		Race race = getRace(raceId);
		double raceLength = 0;
		for (Stage stage : race.getStages()) {
			raceLength += stage.getLength();
		}
		String details = "";
		details += String.format("Race ID : %d\n", raceId);
		details += String.format("Race Name : %s\n", race.getName());
		details += String.format("Description : %s\n", race.getDescription());
		details += String.format("Num. of Stages : %d\n", race.getStages().length);
		details += String.format("Total Length : %.2f", raceLength);

		return details;
	}

	@Override
	public void removeRaceById(int raceId) throws IDNotRecognisedException {
		Race race = getRace(raceId);
		
		for (Stage stage : race.getStages()) {
			race.removeStage(stage);
		}
		races.remove(race.getId());
	}

	@Override
	public int getNumberOfStages(int raceId) throws IDNotRecognisedException {
		Race race = getRace(raceId);

		return race.getStages().length;
	}

	@Override
	public int addStageToRace(int raceId, String stageName, String description, double length, LocalDateTime startTime,
			StageType type)
			throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException {
		
		Race race = getRace(raceId);
		validateName(stageName);
		if (stageName.length() > 30) {
			throw new InvalidLengthException("The stage name was too long. (30 char limit).");
		}
		if (length < 5) {
			throw new InvalidLengthException("The stage was too short (5km minimum).");
		}
		int stageId = nextId++;
		Stage stage = new Stage(raceId, stageId, stageName, description, length, startTime, type);
		race.addStage(stage);

		return stageId;
	}

	@Override
	public int[] getRaceStages(int raceId) throws IDNotRecognisedException {
		Race race = getRace(raceId);
		return race.getStageIds();
	}

	@Override
	public double getStageLength(int stageId) throws IDNotRecognisedException {
		Stage stage = getStage(stageId);
		return stage.getLength();
	}

	@Override
	public void removeStageById(int stageId) throws IDNotRecognisedException {
		Stage stage = getStage(stageId);
		int raceId = stage.getRaceId();
		races.get(raceId).removeStage(stage);
	}

	@Override
	public int addCategorizedClimbToStage(int stageId, Double location, SegmentType type, Double averageGradient,
			Double length) throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException,
			InvalidStageTypeException {

		validateSegmentAddition(stageId, location, type, length);
		Stage stage = getStage(stageId);
		int segmentId = nextId++;
		CategorizedClimb climb = new CategorizedClimb(stageId, segmentId, length, location, averageGradient, type);
		stage.addSegment(climb);

		return segmentId;
	}

	@Override
	public int addIntermediateSprintToStage(int stageId, double location) throws IDNotRecognisedException,
			InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
		
		validateSegmentAddition(stageId, location, SegmentType.SPRINT, 0d);
		Stage stage = getStage(stageId);
		int segmentId = nextId++;
		IntermediateSprint sprint = new IntermediateSprint(stageId, segmentId, location, SegmentType.SPRINT);
		stage.addSegment(sprint);

		return segmentId;
	}

	@Override
	public void removeSegment(int segmentId) throws IDNotRecognisedException, InvalidStageStateException {
		Segment segment = getSegment(segmentId);
		int stageId = segment.getStageId();
		Stage stage = getStage(stageId);
		stage.assertNotWaitingForResults();
		stage.removeSegment(segment);

	}

	@Override
	public void concludeStagePreparation(int stageId) throws IDNotRecognisedException, InvalidStageStateException {
		Stage stage = getStage(stageId);
		stage.assertNotWaitingForResults();
		stage.setState("waiting for results");
	}

	@Override
	public int[] getStageSegments(int stageId) throws IDNotRecognisedException {
		Stage stage = getStage(stageId);
		return stage.getSegmentIds();
	}

	@Override
	public int createTeam(String name, String description) throws IllegalNameException, InvalidNameException {
		validateName(name);
		int teamId = nextId++;
		Team team = new Team(teamId, name, description);
		teams.put(teamId, team);
		return teamId;
	}

	@Override
	public void removeTeam(int teamId) throws IDNotRecognisedException {
		getTeam(teamId);
		teams.remove(teamId);
	}

	@Override
	public int[] getTeams() {
		int[] teamIds = new int[teams.size()];
		int i = 0;
		for (Team team : teams.values()) {
			teamIds[i] = team.getId();
			i++;
		}
		return teamIds;
	}

	@Override
	public int[] getTeamRiders(int teamId) throws IDNotRecognisedException {
		Team team = getTeam(teamId);
		return team.getRiderIds();
	}

	@Override
	public int createRider(int teamID, String name, int yearOfBirth)
			throws IDNotRecognisedException, IllegalArgumentException {
		if (name == null || name.length() == 0) {
			throw new IllegalArgumentException("The rider's name was empty.");
		}
		if (yearOfBirth < 1900) {
			throw new IllegalArgumentException("The year of birth was invalid, it must be 1900 or later.");
		}
		Team team = getTeam(teamID);
		
		int riderId = nextId++;
		Rider rider = new Rider(riderId, teamID, name, yearOfBirth);
		team.addRider(rider);

		return riderId;
	}

	@Override
	public void removeRider(int riderId) throws IDNotRecognisedException {
		Rider rider = getRider(riderId);
		Team team = getTeam(rider.getTeamId());
		team.removeRider(rider);
	}

	@Override
	public void registerRiderResultsInStage(int stageId, int riderId, LocalTime... checkpoints)
			throws IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointsException,
			InvalidStageStateException {
		getRider(riderId);
		Stage stage = getStage(stageId);
		if (stage.getSegments().length+2 != checkpoints.length) {
			String errorMessage = "There were an invalid number of checkpoints.";
			throw new InvalidCheckpointsException(errorMessage);
		}
		if (stage.getResults(riderId).length != 0) {
			String errorMessage = "The rider already has results for this stage.";
			throw new DuplicatedResultException(errorMessage);
		}
		stage.assertWaitingForResults();
		
		stage.addResults(riderId, checkpoints);
	}

	@Override
	public LocalTime[] getRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		getRider(riderId);
		Stage stage = getStage(stageId);
		return stage.getResults(riderId);
	}

	@Override
	public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId) throws IDNotRecognisedException {
		getRider(riderId);
		Stage stage = getStage(stageId);
		
		LocalTime elapsedTime = stage.getRiderAdjustedElapsedTime(riderId);
		return elapsedTime;
	}

	@Override
	public void deleteRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		getRider(riderId);
		Stage stage = getStage(stageId);

		stage.deleteResults(riderId);
	}

	@Override
	public int[] getRidersRankInStage(int stageId) throws IDNotRecognisedException {
		Stage stage = getStage(stageId);
		return stage.getRidersRanks();
	}

	@Override
	public LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId) throws IDNotRecognisedException {
		Stage stage = getStage(stageId);
		return stage.getRankedAdjustedTimes();
	}

	@Override
	public int[] getRidersPointsInStage(int stageId) throws IDNotRecognisedException {
		Stage stage = getStage(stageId);
		return stage.getRidersPoints();
	}

	@Override
	public int[] getRidersMountainPointsInStage(int stageId) throws IDNotRecognisedException {
		Stage stage = getStage(stageId);
		return stage.getRidersMountainPoints();
	}

	@Override
	public void eraseCyclingPortal() {
		this.nextId = 0;
		this.teams.clear();
		this.races.clear();
	}

	@Override
	public void saveCyclingPortal(String filename) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(filename);
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

		ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedOutputStream);

		objectOutputStream.writeObject(this);
		objectOutputStream.close();
	}

	@Override
	public void loadCyclingPortal(String filename) throws IOException, ClassNotFoundException {
		FileInputStream fileInputStream = new FileInputStream(filename);
		BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
		ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);
		CyclingPortal loadedCyclingPortal = (CyclingPortal)objectInputStream.readObject();
		objectInputStream.close();

		this.nextId = loadedCyclingPortal.nextId;
		this.teams = loadedCyclingPortal.teams;
		this.races = loadedCyclingPortal.races;
	}

	@Override
	public void removeRaceByName(String name) throws NameNotRecognisedException {
		boolean found = false;
		for (Race race : races.values()) {
			if (race.getName().equals(name)) {
				races.remove(race.getId());
				found = true;
				break;
			}
		}

		if (!found) {
			String errorMessage = String.format("Race name '%s' did not exist.", name);
			throw new NameNotRecognisedException(errorMessage);
		} else {
		}
	}

	@Override
	public LocalTime[] getGeneralClassificationTimesInRace(int raceId) throws IDNotRecognisedException {
		Race race = getRace(raceId);
		return race.getGeneralClassificationTimes();
	}

	@Override
	public int[] getRidersPointsInRace(int raceId) throws IDNotRecognisedException {
		Race race = getRace(raceId);
		return race.getRidersPoints();
	}

	@Override
	public int[] getRidersMountainPointsInRace(int raceId) throws IDNotRecognisedException {
		Race race = getRace(raceId);
		return race.getRidersMountainPoints();
	}

	@Override
	public int[] getRidersGeneralClassificationRank(int raceId) throws IDNotRecognisedException {
		Race race = getRace(raceId);
		return race.getRidersGeneralClassificationRank();
	}

	@Override
	public int[] getRidersPointClassificationRank(int raceId) throws IDNotRecognisedException {
		Race race = getRace(raceId);
		return race.getRidersPointClassificationRank();
	}

	@Override
	public int[] getRidersMountainPointClassificationRank(int raceId) throws IDNotRecognisedException {
		Race race = getRace(raceId);
		return race.getRidersMountainPointClassificationRank();
	}

}
