import cycling.CyclingPortal;
import cycling.CyclingPortalInterface;
import cycling.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * A short program to illustrate an app testing some minimal functionality of a
 * concrete implementation of the CyclingPortalInterface interface -- note you
 * will want to increase these checks, and run it on your CyclingPortal class
 * (not the BadCyclingPortal class).
 *
 * 
 * @author Diogo Pacheco
 * @version 1.0
 */
public class CyclingPortalInterfaceTestApp {

	public static HashMap<String, String> riderAndTeamManagementTests() {
		HashMap<String, String> results = new HashMap<String, String>();
		StringWriter sw = new StringWriter();
		CyclingPortalInterface portal = new CyclingPortal();

		String nullTeamRejected = "FAILED";
		try {
			portal.createTeam(null, null);
		} catch (InvalidNameException e) {
			nullTeamRejected = "Passed";
		} catch (IllegalNameException e) {
			e.printStackTrace(new PrintWriter(sw));
			nullTeamRejected += "\n" + sw.toString();
		}
		results.put("nullTeamRejected", nullTeamRejected);

		String longTeamNameRejected = "FAILED";
		try {
			portal.createTeam("1234567890123456789012345678901", null);
		} catch (InvalidNameException e) {
			longTeamNameRejected = "Passed";
		} catch (IllegalNameException e) {
			e.printStackTrace(new PrintWriter(sw));
			longTeamNameRejected += "\n" + sw.toString();
		}
		results.put("longTeamNameRejected", longTeamNameRejected);
		

		String duplicateTeamRejected = "FAILED";
		try {
			int firstTeam = portal.createTeam("BicycleMen", "Men that bicycle.");
			int duplicateTeam = portal.createTeam("BicycleMen", "Impostors that bicycle.");
		} catch (IllegalNameException e) {
			duplicateTeamRejected = "Passed";
		} catch (InvalidNameException e) {
			e.printStackTrace(new PrintWriter(sw));
			duplicateTeamRejected += "\n" + sw.toString();
		}
		results.put("duplicateTeamRejected", duplicateTeamRejected);
		
		String nullRiderNameRejected = "FAILED";
		try {
			int newTeam = portal.createTeam("WheelieWomen", "Women who wheelie");
			int nullRider = portal.createRider(newTeam, null, 2000);
		} catch (IllegalArgumentException e) {
			nullRiderNameRejected = "Passed";
		} catch (IDNotRecognisedException | IllegalNameException | InvalidNameException e) {
			e.printStackTrace(new PrintWriter(sw));
			nullRiderNameRejected += "\n" + sw.toString();
		}
		results.put("nullRiderNameRejected", nullRiderNameRejected);

		String deadRiderRejected = "FAILED";
		try {
			int newTeam = portal.createTeam("BicycleBoys", "Boys who bicycle");
			int deadRider = portal.createRider(newTeam, "Ozymandias", -1279);
		} catch (IllegalArgumentException e) {
			deadRiderRejected = "Passed";
		} catch (IDNotRecognisedException | IllegalNameException | InvalidNameException e) {
			e.printStackTrace(new PrintWriter(sw));
			deadRiderRejected += "\n" + sw.toString();
		}
		results.put("deadRiderRejected", deadRiderRejected);

		String unknownTeamRejected = "FAILED";
		try {
			int unknownRider = portal.createRider(123456789, "Impostor", 2000);
		} catch (IDNotRecognisedException e) {
			unknownTeamRejected = "Passed";
		} catch (IllegalArgumentException e) {
			e.printStackTrace(new PrintWriter(sw));
			unknownTeamRejected += "\n" + sw.toString();
		}
		results.put("unknownTeamRejected", unknownTeamRejected);

		String unknownRiderRemovalRejected = "FAILED";
		try {
			portal.removeRider(123456789);
		} catch (IDNotRecognisedException e) {
			unknownRiderRemovalRejected = "Passed";
		}
		results.put("unknownRiderRemovalRejected", unknownRiderRemovalRejected);

		String newRiderJoinsTeam = "FAILED";
		try {
			int newTeam = portal.createTeam("BikeBuddies", "Buddies who bike");
			int newRider = portal.createRider(newTeam, "Buddy", 2000);
			int[] playersInTeam = portal.getTeamRiders(newTeam);
			if (playersInTeam[0] == newRider) {
				newRiderJoinsTeam = "Passed";
			}
		} catch (IllegalArgumentException | IDNotRecognisedException | IllegalNameException | InvalidNameException e) {
			e.printStackTrace(new PrintWriter(sw));
			newRiderJoinsTeam += "\n" + sw.toString();
		}
		results.put("newRiderJoinsTeam", newRiderJoinsTeam);

		String oldRiderRemovedFromTeam = "FAILED";
		String oldRiderReplaceable = "FAILED";
		try {
			int newTeam = portal.createTeam("BikeBestFriends", "Best Friends who bike");
			int newRider = portal.createRider(newTeam, "Buddy", 2000);
			portal.removeRider(newRider);
			int[] playersInTeam = portal.getTeamRiders(newTeam);
			if (playersInTeam.length == 0) {
				oldRiderRemovedFromTeam = "Passed";
			}
			int newerRider = portal.createRider(newTeam, "Buddy", 2000);
			oldRiderReplaceable = "Passed";
		} catch (IllegalArgumentException | IDNotRecognisedException | IllegalNameException | InvalidNameException e) {
			e.printStackTrace(new PrintWriter(sw));
			oldRiderReplaceable += "\n" + sw.toString();
		}
		results.put("oldRiderRemovedFromTeam", oldRiderRemovedFromTeam);
		results.put("oldRiderReplaceable", oldRiderReplaceable);

		String unknownTeamRemovalRejected = "FAILED";
		try {
			portal.removeTeam(123456789);
		} catch (IDNotRecognisedException e) {
			unknownTeamRemovalRejected = "Passed";
		}
		results.put("unknownTeamRemovalRejected", unknownTeamRemovalRejected);

		String oldTeamRidersRemoved = "FAILED";
		try {
			int newTeam = portal.createTeam("BicycleBrothers", "Brothers who bicycle");
			int newRider = portal.createRider(newTeam, "Brother", 2000);
			portal.removeTeam(newTeam);
			portal.removeRider(newRider);
		} catch (IDNotRecognisedException e) {
			oldTeamRidersRemoved = "Passed";
		} catch (IllegalArgumentException | IllegalNameException | InvalidNameException e) {
			e.printStackTrace(new PrintWriter(sw));
			oldTeamRidersRemoved += "\n" + sw.toString();
		}
		results.put("oldTeamRidersRemoved", oldTeamRidersRemoved);
		
		String oldTeamReplaceable = "FAILED";
		try {
			int newTeam = portal.createTeam("CyclingChums", "Chums who cycle");
			portal.removeTeam(newTeam);
			int newerTeam = portal.createTeam("CyclingChums", "Chums who cycle");
			oldTeamReplaceable = "Passed";
		} catch (IllegalArgumentException | IDNotRecognisedException | IllegalNameException | InvalidNameException e) {
			e.printStackTrace(new PrintWriter(sw));
			oldTeamReplaceable += "\n" + sw.toString();
		}
		results.put("oldTeamReplaceable", oldTeamReplaceable);

		String teamsFetchedSuccessfully = "FAILED";
		try {
			int newTeam = portal.createTeam("CyclingCompanions", "Companions who Cycle");
			int[] newTeamIDs = portal.getTeams();
			for (int i = 0; i < newTeamIDs.length; i++) {
				if (newTeamIDs[i] == newTeam) {
					teamsFetchedSuccessfully = "Passed";
				}
			}
		} catch (IllegalArgumentException | IllegalNameException | InvalidNameException e) {
			e.printStackTrace(new PrintWriter(sw));
			teamsFetchedSuccessfully += "\n" + sw.toString();
		}
		results.put("teamsFetchedSuccessfully", teamsFetchedSuccessfully);

		return results;
	}

	public static HashMap<String, String> raceAndStageManagementTests() {
		HashMap<String, String> results = new HashMap<String, String>();
		StringWriter sw = new StringWriter();
		CyclingPortalInterface portal = new CyclingPortal();

		String nullRaceNameRejected = "FAILED";
		try {
			int newRace = portal.createRace(null, null);
		} catch (InvalidNameException e) {
			nullRaceNameRejected = "Passed";
		} catch (IllegalNameException e) {
			e.printStackTrace(new PrintWriter(sw));
			nullRaceNameRejected += "\n" + sw.toString();
		}
		results.put("nullRaceNameRejected", nullRaceNameRejected);

		String spaceRaceNameRejected = "FAILED";
		try {
			int newRace = portal.createRace("This has spaces", "Spaces this has");
		} catch (InvalidNameException e) {
			spaceRaceNameRejected = "Passed";
		} catch (IllegalNameException e) {
			e.printStackTrace(new PrintWriter(sw));
			spaceRaceNameRejected += "\n" + sw.toString();
		}
		results.put("spaceRaceNameRejected", spaceRaceNameRejected);

		String raceCreatedSuccessfully = "FAILED";
		try {
			int newRace = portal.createRace("ValidRace", "This is valid");
			for (int raceID : portal.getRaceIds()) {
				if (raceID == newRace) {
					raceCreatedSuccessfully = "Passed";
				}
			}
		} catch (IllegalNameException | InvalidNameException e) {
			e.printStackTrace(new PrintWriter(sw));
			raceCreatedSuccessfully += "\n" + sw.toString();
		}
		results.put("raceCreatedSuccessfully", raceCreatedSuccessfully);

		String unknownRaceStageRejected = "FAILED";
		try {
			int newStage = portal.addStageToRace(123456789, "stageName", "description", 10, LocalDateTime.now(), StageType.FLAT);
		} catch (IDNotRecognisedException e) {
			unknownRaceStageRejected = "Passed";
		} catch (IllegalNameException | InvalidNameException | InvalidLengthException e) {
			e.printStackTrace(new PrintWriter(sw));
			unknownRaceStageRejected += "\n" + sw.toString();
		}
		results.put("unknownRaceStageRejected", unknownRaceStageRejected);

		String duplicateStageRejected = "FAILED";
		try {
			int newRace = portal.createRace("ValidRace2", "This is valid");
			int newStage = portal.addStageToRace(newRace, "stageName", "description", 10, LocalDateTime.now(), StageType.FLAT);
			int duplicateStage = portal.addStageToRace(newRace, "stageName", "description", 10, LocalDateTime.now(), StageType.FLAT);
		} catch (IllegalNameException e) {
			duplicateStageRejected = "Passed";
		} catch (IDNotRecognisedException | InvalidNameException | InvalidLengthException e) {
			e.printStackTrace(new PrintWriter(sw));
			duplicateStageRejected += "\n" + sw.toString();
		}
		results.put("duplicateStageRejected", duplicateStageRejected);

		String nullStageRejected = "FAILED";
		try {
			int newRace = portal.createRace("ValidRace3", "This is valid");
			int newStage = portal.addStageToRace(newRace, null, null, 10, LocalDateTime.now(), StageType.FLAT);
		} catch (InvalidNameException e) {
			nullStageRejected = "Passed";
		} catch (IDNotRecognisedException | IllegalNameException | InvalidLengthException e) {
			e.printStackTrace(new PrintWriter(sw));
			nullStageRejected += "\n" + sw.toString();
		}
		results.put("nullStageRejected", nullStageRejected);

		String shortStageRejected = "FAILED";
		try {
			int newRace = portal.createRace("ValidRace4", "This is valid");
			int newStage = portal.addStageToRace(newRace, "ValidStage", "This is valid", 2, LocalDateTime.now(), StageType.FLAT);
		} catch (InvalidLengthException e) {
			shortStageRejected = "Passed";
		} catch (IDNotRecognisedException | IllegalNameException | InvalidNameException e) {
			e.printStackTrace(new PrintWriter(sw));
			shortStageRejected += "\n" + sw.toString();
		}
		results.put("shortStageRejected", shortStageRejected);

		String getUnknownStagesRejected = "FAILED";
		try {
			int[] unknownStages = portal.getRaceStages(123456789);
		} catch (IDNotRecognisedException e) {
			getUnknownStagesRejected = "Passed";
		}
		results.put("getUnknownStagesRejected", getUnknownStagesRejected);

		String stageAddedSuccessfully = "FAILED";
		try {
			int newRace = portal.createRace("ValidRace5", "This is valid");
			int newStage = portal.addStageToRace(newRace, "ValidStage5", "This is valid", 10, LocalDateTime.now(), StageType.FLAT);
			int[] stages = portal.getRaceStages(newRace);
			if (stages.length == 1) {
				stageAddedSuccessfully = "Passed";
			}
		} catch (InvalidLengthException | IDNotRecognisedException | IllegalNameException | InvalidNameException e) {
			e.printStackTrace(new PrintWriter(sw));
			stageAddedSuccessfully += "\n" + sw.toString();
		}
		results.put("stageAddedSuccessfully", stageAddedSuccessfully);

		String raceDetailsRetrievedSuccessfully = "FAILED";
		try {
			int newRace = portal.createRace("ValidRace6", "This is valid");
			int newStage = portal.addStageToRace(newRace, "ValidStage6", "This is valid", 10, LocalDateTime.now(), StageType.FLAT);

			String expectedDetails = "";
			expectedDetails += String.format("Race ID : %d\n", newRace);
			expectedDetails += "Race Name : ValidRace6\n";
			expectedDetails += "Description : This is valid\n";
			expectedDetails += "Num. of Stages : 1\n";
			expectedDetails += "Total Length : 10.00";

			// Check if the created race is equal to the expected value
			if (portal.viewRaceDetails(newRace).equals(expectedDetails)) {
				raceDetailsRetrievedSuccessfully = "Passed";
			}
		} catch (IDNotRecognisedException | InvalidLengthException | IllegalNameException | InvalidNameException e) {
			e.printStackTrace(new PrintWriter(sw));
			raceDetailsRetrievedSuccessfully += "\n" + sw.toString();
		}
		results.put("raceDetailsRetrievedSuccessfully", raceDetailsRetrievedSuccessfully);

		String raceRemovedSuccessfully = "FAILED";
		try {
			int newRace = portal.createRace("ValidRace7", "This is valid");
			portal.removeRaceById(newRace);
			int duplicateRace = portal.createRace("ValidRace7", "This is valid");
			raceRemovedSuccessfully = "Passed";
		} catch (IDNotRecognisedException | IllegalNameException | InvalidNameException e) {
			e.printStackTrace(new PrintWriter(sw));
			raceRemovedSuccessfully += "\n" + sw.toString();
		}
		results.put("raceRemovedSuccessfully", raceRemovedSuccessfully);

		String stageRemovedSuccessfully = "FAILED";
		try {
			int newRace = portal.createRace("ValidRace8", "This is valid");
			int newStage = portal.addStageToRace(newRace, "ValidStage8", "This is valid", 10, LocalDateTime.now(), StageType.FLAT);
			portal.removeStageById(newStage);
			int newStage2 = portal.addStageToRace(newRace, "ValidStage8", "This is valid", 10, LocalDateTime.now(), StageType.FLAT);
			stageRemovedSuccessfully = "Passed";
		} catch (IDNotRecognisedException | InvalidLengthException | IllegalNameException | InvalidNameException e) {
			e.printStackTrace(new PrintWriter(sw));
			stageRemovedSuccessfully += "\n" + sw.toString();
		}
		results.put("stageRemovedSuccessfully", stageRemovedSuccessfully);

		String numberOfStagesRetrieved = "FAILED";
		try {
			int newRace = portal.createRace("ValidRace9", "This is valid");
			int noStages = portal.getNumberOfStages(newRace);
			int newStage = portal.addStageToRace(newRace, "ValidStage9", "This is valid", 10, LocalDateTime.now(), StageType.FLAT);
			int oneStage = portal.getNumberOfStages(newRace);
			if (noStages == 0 && oneStage == 1) {
				numberOfStagesRetrieved = "Passed";
			}
		} catch (IDNotRecognisedException | InvalidLengthException | IllegalNameException | InvalidNameException e) {
			e.printStackTrace(new PrintWriter(sw));
			numberOfStagesRetrieved += "\n" + sw.toString();
		}
		results.put("numberOfStagesRetrieved", numberOfStagesRetrieved);

		String stageLengthRetrieved = "FAILED";
		try {
			int newRace = portal.createRace("ValidRaceA", "This is valid");
			int newStage = portal.addStageToRace(newRace, "ValidStageA", "This is valid", 10.2, LocalDateTime.now(), StageType.FLAT);
			if (portal.getStageLength(newStage) == 10.2) {
				stageLengthRetrieved = "Passed";
			}
		} catch (IDNotRecognisedException | InvalidLengthException | IllegalNameException | InvalidNameException e) {
			e.printStackTrace(new PrintWriter(sw));
			stageLengthRetrieved += "\n" + sw.toString();
		}
		results.put("stageLengthRetrieved", stageLengthRetrieved);

		String climbAddedSuccessfully = "FAILED";
		try {
			int newRace = portal.createRace("ValidRaceB", "This is valid");
			int newStage = portal.addStageToRace(newRace, "ValidStageB", "description", 10, LocalDateTime.now(), StageType.FLAT);
			int newClimb = portal.addCategorizedClimbToStage(newStage, 5D, SegmentType.C4, 1D, 2D);
			climbAddedSuccessfully = "Passed";
		} catch (InvalidLengthException | InvalidNameException | IllegalNameException | IDNotRecognisedException | InvalidLocationException | InvalidStageStateException | InvalidStageTypeException e) {
			e.printStackTrace(new PrintWriter(sw));
			climbAddedSuccessfully += "\n" + sw.toString();
		}
		results.put("climbAddedSuccessfully", climbAddedSuccessfully);

		String sprintAddedSuccessfully = "FAILED";
		try {
			int newRace = portal.createRace("ValidRaceC", "This is valid");
			int newStage = portal.addStageToRace(newRace, "ValidStageC", "description", 10, LocalDateTime.now(), StageType.FLAT);
			int newSprint = portal.addIntermediateSprintToStage(newStage, 5);
			sprintAddedSuccessfully = "Passed";
		} catch (InvalidLengthException | InvalidNameException | IllegalNameException | IDNotRecognisedException | InvalidLocationException | InvalidStageStateException | InvalidStageTypeException e) {
			e.printStackTrace(new PrintWriter(sw));
			sprintAddedSuccessfully += "\n" + sw.toString();
		}
		results.put("sprintAddedSuccessfully", sprintAddedSuccessfully);

		String segmentsReturnedSuccessfully = "FAILED";
		try {
			int newRace = portal.createRace("ValidRaceD", "This is valid");
			int newStage = portal.addStageToRace(newRace, "ValidStageD", "description", 10, LocalDateTime.now(), StageType.FLAT);
			int newSprint = portal.addIntermediateSprintToStage(newStage, 5D);
			int[] segments = portal.getStageSegments(newStage);
			if (segments.length == 1) {
				segmentsReturnedSuccessfully = "Passed";
			}
		} catch (InvalidLengthException | InvalidNameException | IllegalNameException | IDNotRecognisedException | InvalidLocationException | InvalidStageStateException | InvalidStageTypeException e) {
			e.printStackTrace(new PrintWriter(sw));
			segmentsReturnedSuccessfully += "\n" + sw.toString();
		}
		results.put("segmentsReturnedSuccessfully", segmentsReturnedSuccessfully);

		String segmentsRemovedSuccessfully = "FAILED";
		try {
			int newRace = portal.createRace("ValidRaceE", "This is valid");
			int newStage = portal.addStageToRace(newRace, "ValidStageE", "description", 10, LocalDateTime.now(), StageType.FLAT);
			int newSprint = portal.addIntermediateSprintToStage(newStage, 5);
			portal.removeSegment(newSprint);
			int[] segments = portal.getStageSegments(newStage);
			if (segments.length == 0) {
				segmentsRemovedSuccessfully = "Passed";
			}
		} catch (InvalidLengthException | InvalidNameException | IllegalNameException | IDNotRecognisedException | InvalidLocationException | InvalidStageStateException | InvalidStageTypeException e) {
			e.printStackTrace(new PrintWriter(sw));
			segmentsRemovedSuccessfully += "\n" + sw.toString();
		}
		results.put("segmentsRemovedSuccessfully", segmentsRemovedSuccessfully);

		return results;
	}

	public static HashMap<String, String> resultManagementTests() {
		HashMap<String, String> results = new HashMap<String, String>();
		StringWriter sw = new StringWriter();
		CyclingPortalInterface portal = new CyclingPortal();

		String resultsAddedSuccessfully = "FAILED";
		try {
			int newTeam = portal.createTeam("Team", "Team Description");
			int newRider = portal.createRider(newTeam, "Rider", 2000);
			int newRace = portal.createRace("Race", "Race Description");
			int newStage = portal.addStageToRace(newRace, "Stage", "Stage Description", 10, LocalDateTime.now(), StageType.FLAT);
			int newSprint = portal.addIntermediateSprintToStage(newStage, 5);
			portal.concludeStagePreparation(newStage);
			LocalTime[] checkpoints = {LocalTime.of(14, 0, 0), LocalTime.of(15, 0, 0), LocalTime.of(16, 10, 45)};
			portal.registerRiderResultsInStage(newStage, newRider, checkpoints);
			resultsAddedSuccessfully = "Passed";
		} catch (InvalidLengthException | InvalidNameException | IllegalNameException | IDNotRecognisedException | InvalidLocationException | InvalidStageStateException | InvalidStageTypeException | DuplicatedResultException | InvalidCheckpointsException e) {
			e.printStackTrace(new PrintWriter(sw));
			resultsAddedSuccessfully += "\n" + sw.toString();
		}
		results.put("resultsAddedSuccessfully", resultsAddedSuccessfully);

		String resultsRetrievedSuccessfully = "FAILED";
		try {
			int newTeam = portal.createTeam("Team2", "Team Description");
			int newRider = portal.createRider(newTeam, "Rider2", 2000);
			int newRace = portal.createRace("Race2", "Race Description");
			int newStage = portal.addStageToRace(newRace, "Stage2", "Stage Description", 10, LocalDateTime.now(), StageType.FLAT);
			int newSprint = portal.addIntermediateSprintToStage(newStage, 5);
			portal.concludeStagePreparation(newStage);
			LocalTime[] checkpoints = {LocalTime.of(14, 0, 0), LocalTime.of(15, 0, 0), LocalTime.of(16, 10, 45)};
			portal.registerRiderResultsInStage(newStage, newRider, checkpoints);
			LocalTime[] returnCheckpoints = portal.getRiderResultsInStage(newStage, newRider);
			if (returnCheckpoints.length == 2) {
				resultsRetrievedSuccessfully = "Passed";
			}
		} catch (InvalidLengthException | InvalidNameException | IllegalNameException | IDNotRecognisedException | InvalidLocationException | InvalidStageStateException | InvalidStageTypeException | DuplicatedResultException | InvalidCheckpointsException e) {
			e.printStackTrace(new PrintWriter(sw));
			resultsRetrievedSuccessfully += "\n" + sw.toString();
		}
		results.put("resultsRetrievedSuccessfully", resultsRetrievedSuccessfully);

		String resultsDeletedSuccessfully = "FAILED";
		try {
			int newTeam = portal.createTeam("Team3", "Team Description");
			int newRider = portal.createRider(newTeam, "Rider3", 2000);
			int newRace = portal.createRace("Race3", "Race Description");
			int newStage = portal.addStageToRace(newRace, "Stage3", "Stage Description", 10, LocalDateTime.now(), StageType.FLAT);
			int newSprint = portal.addIntermediateSprintToStage(newStage, 5);
			portal.concludeStagePreparation(newStage);
			LocalTime[] checkpoints = {LocalTime.of(14, 0, 0), LocalTime.of(15, 0, 0), LocalTime.of(16, 10, 45)};
			portal.registerRiderResultsInStage(newStage, newRider, checkpoints);
			portal.deleteRiderResultsInStage(newStage, newRider);
			LocalTime[] returnCheckpoints = portal.getRiderResultsInStage(newStage, newRider);
			if (returnCheckpoints.length == 0) {
				resultsDeletedSuccessfully = "Passed";
			}
		} catch (InvalidLengthException | InvalidNameException | IllegalNameException | IDNotRecognisedException | InvalidLocationException | InvalidStageStateException | InvalidStageTypeException | DuplicatedResultException | InvalidCheckpointsException e) {
			e.printStackTrace(new PrintWriter(sw));
			resultsDeletedSuccessfully += "\n" + sw.toString();
		}
		results.put("resultsDeletedSuccessfully", resultsDeletedSuccessfully);

		String elapsedTimeCorrect = "FAILED";
		try {
			int newTeam = portal.createTeam("Team4", "Team Description");
			int newRider = portal.createRider(newTeam, "Rider4", 2000);
			int newRace = portal.createRace("Race4", "Race Description");
			int newStage = portal.addStageToRace(newRace, "Stage4", "Stage Description", 10, LocalDateTime.now(), StageType.TT);
			portal.concludeStagePreparation(newStage);
			LocalTime[] checkpoints = {LocalTime.of(14, 0, 0), LocalTime.of(16, 10, 45)};
			portal.registerRiderResultsInStage(newStage, newRider, checkpoints);
			LocalTime elapsed = portal.getRiderAdjustedElapsedTimeInStage(newStage, newRider);
			int seconds = elapsed.toSecondOfDay();
			if (seconds == 7845) {
				elapsedTimeCorrect = "Passed";
			}
		} catch (InvalidLengthException | InvalidNameException | IllegalNameException | IDNotRecognisedException | InvalidStageStateException | DuplicatedResultException | InvalidCheckpointsException e) {
			e.printStackTrace(new PrintWriter(sw));
			elapsedTimeCorrect += "\n" + sw.toString();
		}
		results.put("elapsedTimeCorrect", elapsedTimeCorrect);

		String overflowElapsedTimeCorrect = "FAILED";
		try {
			int newTeam = portal.createTeam("Team5", "Team Description");
			int newRider = portal.createRider(newTeam, "Rider5", 2000);
			int newRace = portal.createRace("Race5", "Race Description");
			int newStage = portal.addStageToRace(newRace, "Stage5", "Stage Description", 10, LocalDateTime.now(), StageType.FLAT);
			int newSprint = portal.addIntermediateSprintToStage(newStage, 5);
			portal.concludeStagePreparation(newStage);
			LocalTime[] checkpoints = {LocalTime.of(22, 0, 0), LocalTime.of(1, 0, 0), LocalTime.of(2, 30, 20)};
			portal.registerRiderResultsInStage(newStage, newRider, checkpoints);
			LocalTime elapsed = portal.getRiderAdjustedElapsedTimeInStage(newStage, newRider);
			int seconds = elapsed.toSecondOfDay();
			if (seconds == 16220) {
				overflowElapsedTimeCorrect = "Passed";
			}
		} catch (InvalidLengthException | InvalidNameException | IllegalNameException | IDNotRecognisedException | InvalidLocationException | InvalidStageStateException | InvalidStageTypeException | DuplicatedResultException | InvalidCheckpointsException e) {
			e.printStackTrace(new PrintWriter(sw));
			overflowElapsedTimeCorrect += "\n" + sw.toString();
		}
		results.put("overflowElapsedTimeCorrect", overflowElapsedTimeCorrect);

		String adjustedElapsedTimeCorrect = "FAILED";
		try {
			int newTeam = portal.createTeam("Team6", "Team Description");
			int newRider1 = portal.createRider(newTeam, "Rider6", 2000);
			int newRider2 = portal.createRider(newTeam, "Rider7", 2000);
			int newRider3 = portal.createRider(newTeam, "Rider8", 2000);
			int newRace = portal.createRace("Race6", "Race Description");
			int newStage = portal.addStageToRace(newRace, "Stage6", "Stage Description", 10, LocalDateTime.now(), StageType.FLAT);
			portal.concludeStagePreparation(newStage);
			LocalTime[] checkpoints1 = {LocalTime.of(14, 0, 0), LocalTime.of(15, 0, 2)};
			portal.registerRiderResultsInStage(newStage, newRider1, checkpoints1);
			LocalTime[] checkpoints2 = {LocalTime.of(14, 0, 0), LocalTime.of(15, 0, 1)};
			portal.registerRiderResultsInStage(newStage, newRider2, checkpoints2);
			LocalTime[] checkpoints3 = {LocalTime.of(14, 0, 0), LocalTime.of(15, 0, 0)};
			portal.registerRiderResultsInStage(newStage, newRider3, checkpoints3);
			LocalTime elapsed = portal.getRiderAdjustedElapsedTimeInStage(newStage, newRider1);
			int seconds = elapsed.toSecondOfDay();
			if (seconds == 3600) {
				adjustedElapsedTimeCorrect = "Passed";
			}
		} catch (InvalidLengthException | InvalidNameException | IllegalNameException | IDNotRecognisedException | InvalidStageStateException | DuplicatedResultException | InvalidCheckpointsException e) {
			e.printStackTrace(new PrintWriter(sw));
			adjustedElapsedTimeCorrect += "\n" + sw.toString();
		}
		results.put("adjustedElapsedTimeCorrect", adjustedElapsedTimeCorrect);
		
		String timeTrialNotAdjusted = "FAILED";
		try {
			int newTeam = portal.createTeam("Team7", "Team Description");
			int newRider1 = portal.createRider(newTeam, "Rider9", 2000);
			int newRider2 = portal.createRider(newTeam, "RiderA", 2000);
			int newRider3 = portal.createRider(newTeam, "RiderB", 2000);
			int newRace = portal.createRace("Race7", "Race Description");
			int newStage = portal.addStageToRace(newRace, "Stage7", "Stage Description", 10, LocalDateTime.now(), StageType.TT);
			portal.concludeStagePreparation(newStage);
			LocalTime[] checkpoints1 = {LocalTime.of(14, 0, 0), LocalTime.of(15, 0, 2)};
			portal.registerRiderResultsInStage(newStage, newRider1, checkpoints1);
			LocalTime[] checkpoints2 = {LocalTime.of(14, 0, 0), LocalTime.of(15, 0, 1)};
			portal.registerRiderResultsInStage(newStage, newRider2, checkpoints2);
			LocalTime[] checkpoints3 = {LocalTime.of(14, 0, 0), LocalTime.of(15, 0, 0)};
			portal.registerRiderResultsInStage(newStage, newRider3, checkpoints3);
			LocalTime elapsed = portal.getRiderAdjustedElapsedTimeInStage(newStage, newRider1);
			int seconds = elapsed.toSecondOfDay();
			if (seconds == 3602) {
				timeTrialNotAdjusted = "Passed";
			}
		} catch (InvalidLengthException | InvalidNameException | IllegalNameException | IDNotRecognisedException | InvalidStageStateException | DuplicatedResultException | InvalidCheckpointsException e) {
			e.printStackTrace(new PrintWriter(sw));
			timeTrialNotAdjusted += "\n" + sw.toString();
		}
		results.put("timeTrialNotAdjusted", timeTrialNotAdjusted);

		String emptyRankReturned = "FAILED";
		String ridersRankedSuccessfully = "FAILED";
		String ridersTimedSuccessfully = "FAILED";
		try {
			int newTeam = portal.createTeam("Team8", "Team Description");
			int newRider1 = portal.createRider(newTeam, "RiderC", 2000);
			int newRider2 = portal.createRider(newTeam, "RiderD", 2000);
			int newRider3 = portal.createRider(newTeam, "RiderE", 2000);
			int newRace = portal.createRace("Race8", "Race Description");
			int newStage = portal.addStageToRace(newRace, "Stage8", "Stage Description", 10, LocalDateTime.now(), StageType.TT);
			portal.concludeStagePreparation(newStage);
			int[] emptyRanks = portal.getRidersRankInStage(newStage);
			if (emptyRanks.length == 0) {
				emptyRankReturned = "Passed";
			}
			LocalTime[] checkpoints1 = {LocalTime.of(14, 0, 0), LocalTime.of(15, 0, 2)};
			portal.registerRiderResultsInStage(newStage, newRider1, checkpoints1);
			LocalTime[] checkpoints2 = {LocalTime.of(14, 0, 0), LocalTime.of(15, 0, 1)};
			portal.registerRiderResultsInStage(newStage, newRider2, checkpoints2);
			LocalTime[] checkpoints3 = {LocalTime.of(14, 0, 0), LocalTime.of(15, 0, 0)};
			portal.registerRiderResultsInStage(newStage, newRider3, checkpoints3);
			int[] ranks = portal.getRidersRankInStage(newStage);
			if (ranks[0] == newRider3 && ranks[1] == newRider2 && ranks[2] == newRider1) {
				ridersRankedSuccessfully = "Passed";
			}
			LocalTime[] times = portal.getRankedAdjustedElapsedTimesInStage(newStage);
			if (times[0].toSecondOfDay() == 3600 && times[1].toSecondOfDay() == 3601 && times[2].toSecondOfDay() == 3602) {
				ridersTimedSuccessfully = "Passed";
			}
		} catch (InvalidLengthException | InvalidNameException | IllegalNameException | IDNotRecognisedException | InvalidStageStateException | DuplicatedResultException | InvalidCheckpointsException e) {
			e.printStackTrace(new PrintWriter(sw));
			emptyRankReturned += "\n" + sw.toString();
			ridersRankedSuccessfully += "\n" + sw.toString();
			ridersTimedSuccessfully += "\n" + sw.toString();
		}
		results.put("emptyRankReturned", emptyRankReturned);
		results.put("ridersRankedSuccessfully", ridersRankedSuccessfully);
		results.put("ridersTimedSuccessfully", ridersTimedSuccessfully);

		String pointsCalculatedSuccessfully = "FAILED";
		String mountainPointsCalculatedSuccessfully = "FAILED";
		try {
			int newTeam = portal.createTeam("Team9", "Team Description");
			int newRider1 = portal.createRider(newTeam, "RiderF", 2000);
			int newRider2 = portal.createRider(newTeam, "RiderG", 2000);
			int newRider3 = portal.createRider(newTeam, "RiderH", 2000);
			int newRace = portal.createRace("Race9", "Race Description");
			int newStage = portal.addStageToRace(newRace, "Stage9", "Stage Description", 10, LocalDateTime.now(), StageType.FLAT);
			int newSegment = portal.addIntermediateSprintToStage(newStage, 5);
			int newSegment2 = portal.addCategorizedClimbToStage(newStage, 7D, SegmentType.C2, 4D, 1D);
			int newSegment3 = portal.addCategorizedClimbToStage(newStage, 8D, SegmentType.HC, 4D, 1D);
			portal.concludeStagePreparation(newStage);
			LocalTime[] checkpoints1 = {LocalTime.of(14, 0, 0), LocalTime.of(14, 10, 0), LocalTime.of(14, 20, 0), LocalTime.of(15, 10, 0), LocalTime.of(15, 20, 0)};
			portal.registerRiderResultsInStage(newStage, newRider1, checkpoints1);
			LocalTime[] checkpoints2 = {LocalTime.of(14, 0, 0), LocalTime.of(14, 20, 0), LocalTime.of(14, 30, 0), LocalTime.of(15, 0, 0), LocalTime.of(15, 10, 0)};
			portal.registerRiderResultsInStage(newStage, newRider2, checkpoints2);
			LocalTime[] checkpoints3 = {LocalTime.of(14, 0, 0), LocalTime.of(14, 30, 0), LocalTime.of(14, 40, 0), LocalTime.of(14, 50, 0), LocalTime.of(15, 0, 0)};
			portal.registerRiderResultsInStage(newStage, newRider3, checkpoints3);
			int[] ranks = portal.getRidersRankInStage(newStage);
			int[] points = portal.getRidersPointsInStage(newStage);
			if (points[0] == 65 && points[1] == 47 && points[2] == 40) {
				pointsCalculatedSuccessfully = "Passed";
			}
			System.out.println(Arrays.toString(points));
			int[] mPoints = portal.getRidersMountainPointsInStage(newStage);
			if (mPoints[0] == 22 && mPoints[1] == 18 && mPoints[2] == 17) {
				mountainPointsCalculatedSuccessfully = "Passed";
			}
			System.out.println(Arrays.toString(mPoints));
		} catch (InvalidLengthException | InvalidNameException | IllegalNameException | InvalidLocationException | InvalidStageTypeException | IDNotRecognisedException | InvalidStageStateException | DuplicatedResultException | InvalidCheckpointsException e) {
			e.printStackTrace(new PrintWriter(sw));
			pointsCalculatedSuccessfully += "\n" + sw.toString();
		}
		results.put("pointsCalculatedSuccessfully", pointsCalculatedSuccessfully);
		results.put("mountainPointsCalculatedSuccessfully", mountainPointsCalculatedSuccessfully);

		// String raceRemovedByNameSuccessfully = "FAILED";
		// try {
		// 	int newRace = portal.createRace("ValidRace7", "This is valid");
		// 	portal.removeRaceByName("ValidRace7");
		// 	int duplicateRace = portal.createRace("ValidRace7", "This is valid");
		// 	raceRemovedByNameSuccessfully = "Passed";
		// } catch (NameNotRecognisedException | IllegalNameException | InvalidNameException e) {
		// 	e.printStackTrace(new PrintWriter(sw));
		// 	raceRemovedByNameSuccessfully += "\n" + sw.toString();
		// }
		// results.put("raceRemovedByNameSuccessfully", raceRemovedByNameSuccessfully);

		// String generalClassificationTimesCorrect = "FAILED";
		// try {
		// 	int newTeam = portal.createTeam("TeamA", "Team Description");
		// 	int newRider1 = portal.createRider(newTeam, "RiderI", 2000);
		// 	int newRider2 = portal.createRider(newTeam, "RiderJ", 2000);
		// 	int newRace = portal.createRace("RaceA", "Race Description");
		// 	int newStage = portal.addStageToRace(newRace, "StageA", "Stage Description", 10, LocalDateTime.now(), StageType.FLAT);
		// 	int newStage2 = portal.addStageToRace(newRace, "StageB", "Stage Description", 10, LocalDateTime.now(), StageType.FLAT);
		// 	portal.concludeStagePreparation(newStage);
		// 	portal.concludeStagePreparation(newStage2);
		// 	LocalTime[] checkpoints1 = {LocalTime.of(14, 0, 0), LocalTime.of(15, 20, 0)};
		// 	portal.registerRiderResultsInStage(newStage, newRider1, checkpoints1);
		// 	LocalTime[] checkpoints2 = {LocalTime.of(14, 0, 0), LocalTime.of(15, 20, 1)};
		// 	portal.registerRiderResultsInStage(newStage, newRider2, checkpoints2);
		// 	LocalTime[] checkpoints3 = {LocalTime.of(14, 0, 0), LocalTime.of(15, 10, 0)};
		// 	portal.registerRiderResultsInStage(newStage2, newRider1, checkpoints3);
		// 	LocalTime[] checkpoints4 = {LocalTime.of(14, 0, 0), LocalTime.of(15, 0, 0)};
		// 	portal.registerRiderResultsInStage(newStage2, newRider2, checkpoints4);
		// 	LocalTime[] times = portal.getGeneralClassificationTimesInRace(newRace);
		// 	if (times[0].toSecondOfDay() == 8400 && times[1].toSecondOfDay() == 9000) {
		// 		generalClassificationTimesCorrect = "Passed";
		// 	}

		// } catch (InvalidLengthException | InvalidNameException | IllegalNameException | IDNotRecognisedException | InvalidStageStateException | DuplicatedResultException | InvalidCheckpointsException e) {
		// 	e.printStackTrace(new PrintWriter(sw));
		// 	generalClassificationTimesCorrect += "\n" + sw.toString();
		// }
		// results.put("generalClassificationTimesCorrect", generalClassificationTimesCorrect);

		// String ridersPointsInRaceCorrect = "FAILED";
		// try {
		// 	int newTeam = portal.createTeam("TeamB", "Team Description");
		// 	int newRider1 = portal.createRider(newTeam, "RiderK", 2000);
		// 	int newRider2 = portal.createRider(newTeam, "RiderL", 2000);
		// 	int newRace = portal.createRace("RaceB", "Race Description");
		// 	int newStage = portal.addStageToRace(newRace, "StageC", "Stage Description", 10, LocalDateTime.now(), StageType.FLAT);
		// 	int newStage2 = portal.addStageToRace(newRace, "StageD", "Stage Description", 10, LocalDateTime.now(), StageType.FLAT);
		// 	portal.concludeStagePreparation(newStage);
		// 	portal.concludeStagePreparation(newStage2);
		// 	LocalTime[] checkpoints1 = {LocalTime.of(14, 0, 0), LocalTime.of(15, 20, 5)};
		// 	portal.registerRiderResultsInStage(newStage, newRider1, checkpoints1);
		// 	LocalTime[] checkpoints2 = {LocalTime.of(14, 0, 0), LocalTime.of(15, 20, 0)};
		// 	portal.registerRiderResultsInStage(newStage, newRider2, checkpoints2);
		// 	LocalTime[] checkpoints3 = {LocalTime.of(14, 0, 0), LocalTime.of(15, 10, 0)};
		// 	portal.registerRiderResultsInStage(newStage2, newRider1, checkpoints3);
		// 	LocalTime[] checkpoints4 = {LocalTime.of(14, 0, 0), LocalTime.of(15, 0, 0)};
		// 	portal.registerRiderResultsInStage(newStage2, newRider2, checkpoints4);
		// 	int[] points = portal.getRidersPointsInRace(newRace);
		// 	if (points[0] == 100 && points[1] == 60) {
		// 		ridersPointsInRaceCorrect = "Passed";
		// 	}

		// } catch (InvalidLengthException | InvalidNameException | IllegalNameException | IDNotRecognisedException | InvalidStageStateException | DuplicatedResultException | InvalidCheckpointsException e) {
		// 	e.printStackTrace(new PrintWriter(sw));
		// 	ridersPointsInRaceCorrect += "\n" + sw.toString();
		// }
		// results.put("ridersPointsInRaceCorrect", ridersPointsInRaceCorrect);

		return results;
	}

	public static HashMap<String, String> portalManagementTests() {
		HashMap<String, String> results = new HashMap<String, String>();
		StringWriter sw = new StringWriter();
		CyclingPortalInterface savedPortal = new CyclingPortal();

		try {
			int savedRace = savedPortal.createRace("ValidRace", "This is valid");
			int savedStage = savedPortal.addStageToRace(savedRace, "ValidStage", "This is valid", 10, LocalDateTime.now(), StageType.FLAT);
			int savedTeam = savedPortal.createTeam("savedTeam", "This team will be saved");
		} catch (InvalidNameException | IDNotRecognisedException | InvalidLengthException | IllegalNameException e) {
			e.printStackTrace();
		}
		String savedCyclingPortalSuccessfully = "FAILED";
		try {
			savedPortal.saveCyclingPortal("portal");
			savedCyclingPortalSuccessfully = "Passed";
		} catch (IOException e) {
			e.printStackTrace(new PrintWriter(sw));
			savedCyclingPortalSuccessfully += "\n" + sw.toString();
		}
		results.put("savedCyclingPortalSuccessfully", savedCyclingPortalSuccessfully);

		String erasedCyclingPortalSuccessfully = "FAILED";
		savedPortal.eraseCyclingPortal();
		if (savedPortal.getTeams().length == 0 && savedPortal.getRaceIds().length == 0) {
			erasedCyclingPortalSuccessfully = "Passed";
		}
		results.put("erasedCyclingPortalSuccessfully",erasedCyclingPortalSuccessfully);

		String loadedCyclingPortalSuccessfully = "FAILED";
		try {
			savedPortal.loadCyclingPortal("portal");
			if (savedPortal.getRaceIds().length == 1 && savedPortal.getTeams().length == 1) {
				loadedCyclingPortalSuccessfully = "Passed";
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace(new PrintWriter(sw));
			loadedCyclingPortalSuccessfully += "\n" + sw.toString();
		}
		results.put("loadedCyclingPortalSuccessfully", loadedCyclingPortalSuccessfully);

		return results;
	}

	public static void printTestResults(String name, HashMap<String, String> results) {
		int passed = 0;
		int failed = 0;
		for (String result : results.values()) {
			passed += result.equals("Passed") ? 1 : 0;
			failed += result.equals("Passed") ? 0 : 1;
		}
		double percentScore = ((double)passed / (double)(passed + failed)) * 100;
		
		System.out.println("====================================");
		String header = String.format("%s, %d Passed, %d Failed, %3.1f%%", name, passed, failed, percentScore);
		System.out.println(header);
		for (Map.Entry<String, String> entry : results.entrySet()) {
			String test = entry.getKey();
			String result = entry.getValue();
			String resultStr = String.format("%s : %s", test, result);
			System.out.println(resultStr);
		}
		System.out.println();
	}

	/**
	 * Test method.
	 * 
	 * @param args Not used.
	 */
	public static void main(String[] args) {
		System.out.println("The system compiled and started the unit tests...");

		HashMap<String, String> riderAndTeamManagement = riderAndTeamManagementTests();
		HashMap<String, String> raceAndStageManagement = raceAndStageManagementTests();
		HashMap<String, String> resultManagement = resultManagementTests();
		//HashMap<String, String> portalManagement = portalManagementTests();
		
		printTestResults("RIDER AND TEAM MANAGEMENT", riderAndTeamManagement);
		printTestResults("RACE AND STAGE MANAGEMENT", raceAndStageManagement);
		printTestResults("RESULT MANAGEMENT", resultManagement);
		//printTestResults("PORTAL MANAGEMENT", portalManagement);
	}

}
