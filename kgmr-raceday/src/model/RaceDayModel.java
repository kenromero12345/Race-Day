/*
 * TCSS 305 - Autumn 2018
 * Assignment 5 - Raceday
 */
package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.StringTokenizer;
import model.controls.PropertyChangeEnabledRaceControls;
import model.message.AbstractMessageWithId;
import model.message.CrossingMessage;
import model.message.LeaderboardMessage;
import model.message.LoadingMessage;
import model.message.Message;
import model.message.TelemetryMessage;

/**
 * Class that will run the controller.
 * 
 * @author Charles Bryan
 * @author Ken Gil Romero kgmr@uw.edu
 * @version Fall 18 
 */
public class RaceDayModel implements PropertyChangeEnabledRaceControls {

    /** The default starting time. */
    public static final int DEFAULT_START_TIME = 0;
    
    /**
     * The max number a racer's id can be.
     */
    private static final int RACER_ID_MAX = 99;

    /**
     * Race format type leaderboard.
     */
    private static final String RACE_FORMAT_LEADERBOARD = "$L:";  
    
    /**
     * Race format type telemetry.
     */
    private static final String RACE_FORMAT_TELEMETRY = "$T:";
    
    /**
     * Race format type crossing.
     */
    private static final String RACE_FORMAT_CROSSING = "$C:";
    
    /**
     * Participants format length.
     */
    private static final int PARTICIPANTS_FORMAT_LENGTH = 3;
    
    /**
     * Race format type leaderboard and crossing length.
     */
    private static final int RACE_FORMAT_LENGTH_LC = 4;
    
    /**
     * Delimeter for the race.
     */
    private static final String SEPARATOR = ":";
    
    /**
     * Number of lines when reading the race for a message to fire.
     */
    private static final int NUMBER_LINES_FOR_FIRE = 40000;
    
    /**
     * The message when there is an error to the file.
     */
    private static final String ERROR_MSG = "There is an error to the file";
    
    /**
     * Message for loading the race.
     */
    private static final String LOAD_TELEM_STILL_LOADING_MSG = "Load file: Still loading...";

    /**
     * Message for start of reading.
     */
    private static final String LOAD_TELEM_START_MSG = "Load file: Loading "
                    + "telemetry information...";
    
    /**
     * Message for completion.
     */
    private static final String LOAD_FILE_COMPLETE = "Load file: Complete!";

    /**
     * Message for the race's information.
     */
    private static final String LOAD_RACE_INFO_MSG = "Load file: Race information loaded.";

    /**
     * Message for the start of loading.
     */
    private static final String LOAD_START_MSG = "Load file: Start - "
                    + "This may take a while. Please be patient.";
    
    /**
     * Start format of lap distance for race info.
     */
    private static final String FORMAT_LAP_DISTANCE = "\nLap distance: ";

    /**
     * Start format of total time for race info.
     */
    private static final String FORMAT_TOTAL_TIME = "\nTotal time: ";

    /**
     * Start format of track type for race info.
     */
    private static final String FORMAT_TRACK_TYPE = "\nTrack type: ";
    
    /** An error message for illegal arguments. */
    private static final String ERROR_MESSAGE = "Time may not be less than 0.";
   
    /**
     * Race's format start line.
     */
    private final String[] myRaceFormat = 
        {"#RACE:", "#TRACK:", "#WIDTH:", "#HEIGHT:"
                                , "#DISTANCE:", "#TIME:", "#PARTICIPANTS:"};
    /**
     * Manager for Property Change Listeners. 
     */
    private final PropertyChangeSupport myPcs;
    
    /**
     * Race information.
     */
    private String myRaceInfo;
    
    /**
     * Race name.
     */
    private String myRaceName;
    
    /**
     * Race track.
     */
    private String myRaceTrack;
    
    /**
     * Race width.
     */
    private int myRaceWidth; // depends on the View
    
    /**
     * Race height.
     */
    private int myRaceHeight; // depends on the View
    
    /**
     * Race distance.
     */
    private int myRaceDistance;
    
    /**
     * Race time.
     */
    private int myRaceMaxTime;
    
    /**
     * Race participants size.
     */
    private int myRaceParticipantsSize;
    
    /** Stores this objects time. */
    private int myCurrentTime;
    
    /**
     * Racer's id list of racers that are toggled off.
     */
    private List<Integer> myOffRacerIdList;
    
    /**
     * The list of race's with their info.
     */
    private List<Racer> myRacers;
    
    /**
     * The messages list.
     */
    private List<ArrayList<Message>> myMessages;
    
    /**
     * To help my messages list when it has an error.
     */
    private List<ArrayList<Message>> myTempMessages;  
    
    /**
     * Sorted racer ids.
     */
    private List<Integer> mySortedRacerIds;

    /**
     * To help my off racer list when it has an error.
     */
    private List<Integer> myTempOffRacerIdList;

    /**
     * To help my racer list when it has an error.
     */
    private List<Racer> myTempRacers;

    /**
     * To help my sorted racer id list when it has an error.
     */
    private List<Integer> myTempSortedRacerIds;

    /**
     * The current timestamp.
     */
    private int myCurrentTimestamp;
    
    /**
     * Helps the reading of file to have less instantiation for the strings.
     */
    private String myCurrentLine;
    
    /**
     * Helps the reading of file to have less instantiation for the tokenizer.
     */
    private StringTokenizer myTokenizer;
    
    /**
     * If 0 should be printed.
     */
    private boolean myZeroFlag;
    
    /**
     * Temp for myZeroFlag if there was an error in loading.
     */
    private boolean myTempZeroFlag;
    
    /**
     * No arg Constructor for RaceDayModel.
     */
    public RaceDayModel() {
        this(DEFAULT_START_TIME);
    }
    
    /**
     * Constructor for RaceDayModel.
     * 
     * @param theStartTime the time we start
     */
    public RaceDayModel(final int theStartTime) {        
        myOffRacerIdList = new ArrayList<Integer>();
        myPcs = new PropertyChangeSupport(this);
        myRacers = new ArrayList<Racer>();
        myMessages = new ArrayList<ArrayList<Message>>();
        mySortedRacerIds = new ArrayList<Integer>();
        
        if (theStartTime < 0) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
        myCurrentTime = theStartTime;
    }

    /**
     * Change's time to 1 + time.
     */
    @Override
    public void advance() {
        advance(1);
    }

    /**
     * Advances time by the millisecond passed. Can be negative to reverse.
     * 
     * @param theMillisecond the time we are adding to our current time
     */
    @Override
    public void advance(final int theMillisecond) {
        if (myCurrentTime != myCurrentTime + theMillisecond) { 
            changeTime(myCurrentTime + theMillisecond);
        }
    }

    /**
     * Handles the race's time and what to print.
     * Note: If the arg equals 0, it means we don't want to print reverse until 0.
     */
    @Override
    public void moveTo(final int theMillisecond) {
        if (theMillisecond < 0) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        } else if (myRaceMaxTime > 0) {
            // condition above stops the problem when setting the value of slider
            // to 0 even when there is no race loaded. (Slider Action listener)
            if (theMillisecond == 0) {
                changeTimeToZero();
                myZeroFlag = true;
            } else {
                changeTime(theMillisecond);
            }
        }
    }
    
    /**
     * Handles time for when race loops or restart.
     * This doesn't print the timestamps at current time to 0, but just 0.
     */
    private void changeTimeToZero() {
        final int old = myCurrentTime;
        myCurrentTime = 0;
        fireValidSpecificTimeMsg(0);
        myPcs.firePropertyChange(PROPERTY_TIME, old, myCurrentTime);
    }
    
    /**
     * Helper method to change the value of time and notify observers. 
     * Functional decomposition. 
     * 
     * @param theMillisecond the time to change to
     */
    private void changeTime(final int theMillisecond) {  
        final int old = myCurrentTime;
        
        changeCurrentTime(theMillisecond);
        
        if (old < myCurrentTime) { // forward advance
            if (myZeroFlag) {
                forwardFireMsg(old + 1);
            } else {
                forwardFireMsg(old);
                myZeroFlag = true;
            }
        } else if (old > myCurrentTime) { // backward advance 
            backwardFireMsg(old);
        }
        
        ifDoneFire(old);
        
        // change time label
        myPcs.firePropertyChange(PROPERTY_TIME, old, myCurrentTime);
    }

    /**
     * Change current time but won't go past the race's max time.
     * 
     * @param theMillisecond the time passed that should change current time
     */
    private void changeCurrentTime(final int theMillisecond) {
        if (theMillisecond > myRaceMaxTime) { //change myTime to max race time
            myCurrentTime = myRaceMaxTime;
        } else { // default
            myCurrentTime = theMillisecond;
        }
    }

    /**
     * Fire if done.
     * 
     * @param theOld the old time
     */
    private void ifDoneFire(final int theOld) {
        // Race is done, so fire 
        if (myRaceMaxTime <= theOld) {
            myPcs.firePropertyChange(PROP_NAME_RACE_DONE, null, true);
            // fire back false to avoid done when slider moves back
            myPcs.firePropertyChange(PROP_NAME_RACE_DONE, null, false);
        }
    }

    /**
     * Fire messages from the old time to the current time.
     * 
     * @param theOld the old time
     */
    private void forwardFireMsg(final int theOld) {
        for (int i = theOld; i < myCurrentTime + 1; i++) {  // +1 for both
            fireValidSpecificTimeMsg(i);
        }
    }

    /**
     * Fire messages form the current time to the old time.
     * 
     * @param theOld the old time
     */
    private void backwardFireMsg(final int theOld) {
        for (int i = theOld - 1; i > myCurrentTime - 1 && i > -1; i--) {               
            fireValidSpecificTimeMsg(i);
        }
    }

    /**
     * The message/s to be fired at the specific message's index.
     * 
     * @param theIndex the index of the messages we need to fire
     */
    private void fireValidSpecificTimeMsg(final int theIndex) {
        for (final Message message: myMessages.get(theIndex)) {
            if (msgShouldBeFired(message)) {
                fireRaceMessage(message);
            }
        }
    }

    /** 
     * @param theMessage the message
     * @return if the message should be fired
     */
    private boolean msgShouldBeFired(final Message theMessage) {
        return ((theMessage.getType().equals("Crossing") 
                        || theMessage.getType().equals("Telemetry")) 
                        && !myOffRacerIdList.contains
                            (((AbstractMessageWithId) theMessage).getId())) 
                        || theMessage.getType().contains("Leaderboard");
    }

    @Override
    public void toggleParticipant(final int theParticpantID, final boolean theToggle) {
        if (theToggle) {
            myOffRacerIdList.remove(myOffRacerIdList.indexOf(theParticpantID));
        } else {
            myOffRacerIdList.add(theParticpantID);
        }
        myPcs.firePropertyChange(PROP_NAME_RACER_SEEN, null, theParticpantID);
    }

    @Override
    public void loadRace(final File theRaceFile) throws IOException {
        salvageAndClear();
        myTempZeroFlag = myZeroFlag;
        myZeroFlag = false;
        
        
        final Scanner scan = new Scanner(theRaceFile); // for reading the file

        fireRaceMessage(new LoadingMessage(LOAD_START_MSG));
        
        readingStartFormat(scan);
        
        readingParticipants(scan);
        
        fireRaceMessage(new LoadingMessage(LOAD_RACE_INFO_MSG));
        fireRaceMessage(new LoadingMessage(LOAD_TELEM_START_MSG));
                
        readingRaceMessages(scan);
        
        fireRaceMessage(new LoadingMessage(LOAD_FILE_COMPLETE));
        
        fireRaceInfo();
        
        fireRaceParticipants();
        
        fireRaceMaxTimestamp(); 
        
        myPcs.firePropertyChange(PROP_NAME_MSG, null, myMessages.get(0).get(0));
        
        scan.close();
    }

    /**
     * Fire the race's max timestamp.
     */
    private void fireRaceMaxTimestamp() {
        myPcs.firePropertyChange(PROP_NAME_RACE_MAX_TIMESTAMP, null, myRaceMaxTime);
    }

    /**
     * Clear's all the collections and salvage them to another lists 
     * when the file is not valid.
     */
    private void salvageAndClear() {
        myTempOffRacerIdList = new ArrayList<Integer>(myOffRacerIdList);
        myTempRacers = new ArrayList<Racer>(myRacers);
        myTempMessages = new ArrayList<ArrayList<Message>>(myMessages); 
        myTempSortedRacerIds = new ArrayList<Integer>(mySortedRacerIds);
                
        myOffRacerIdList.clear();
        myRacers.clear();
        myMessages.clear();
        mySortedRacerIds.clear();        
    }

    /**
     * Fire a race participant.
     */
    private void fireRaceParticipants() {
        for (final Racer racer : myRacers) {
            fireParticipant(racer);
        }
    }

    /**
     * Fire the race info message.
     */
    private void fireRaceInfo() {
        final StringBuilder builder = setRaceInfo();
        
        final String oldRaceInfo = myRaceInfo;
        myRaceInfo =  builder.toString();
        myPcs.firePropertyChange(PROP_NAME_RACE_INFO, oldRaceInfo, myRaceInfo);
    }
    
    /**
     * Fire a race message.
     * 
     * @param theMsg the new message to fire
     */
    private void fireRaceMessage(final Message theMsg) {
        myPcs.firePropertyChange(PROP_NAME_MSG, null, theMsg);
    }
    
    /**
     * Fire a participant.
     * 
     * @param theParticipant the participant to be fired
     */
    private void fireParticipant(final Racer theParticipant) {
        myPcs.firePropertyChange(PROP_NAME_RACER, null, theParticipant);
    }

    /**
     * Setting race information.
     * 
     * @return the race information.
     */
    private StringBuilder setRaceInfo() {
        final StringBuilder builder = new StringBuilder(128);
        builder.append(myRaceName);
        builder.append(FORMAT_TRACK_TYPE);
        builder.append(myRaceTrack);
        builder.append(FORMAT_TOTAL_TIME);
        builder.append(myRaceMaxTime);
        builder.append(FORMAT_LAP_DISTANCE);
        builder.append(myRaceDistance);
        return builder;
    }

    /**
     * Reading the race of the file.
     * 
     * @param theScan the file to read
     * @throws IOException throws the exception
     */
    private void readingRaceMessages(final Scanner theScan) throws IOException {
        myCurrentTimestamp = -1;
        for (int i = 1; theScan.hasNextLine(); i++) { 
            // starts at 1 to avoid firing loading msg the 1st time
            myCurrentLine = theScan.nextLine();
            if (myCurrentLine.startsWith(RACE_FORMAT_TELEMETRY)) {
                readingRaceMessage(RACE_FORMAT_TELEMETRY
                            , RACE_FORMAT_LENGTH_LC);
                //System.out.println(myCurrentLine);
            } else if (myCurrentLine.startsWith(RACE_FORMAT_LEADERBOARD)) {
                readingRaceMessage(RACE_FORMAT_LEADERBOARD
                            , myRacers.size() + 1);
                //System.out.println(myCurrentLine);
            } else if (myCurrentLine.startsWith(RACE_FORMAT_CROSSING)) {
                readingRaceMessage(RACE_FORMAT_CROSSING
                            , RACE_FORMAT_LENGTH_LC);
                //System.out.println(myCurrentLine);
            } else {
                fireErrorThrowIOEx();
            }
            
            if (i % NUMBER_LINES_FOR_FIRE == 0) {
                fireRaceMessage(new LoadingMessage(LOAD_TELEM_STILL_LOADING_MSG));
            }
        }
    }

    /**
     * Reading the starting format of the race.
     * 
     * @param theScan the file to read
     * @throws IOException throws the exception
     */
    private void readingStartFormat(final Scanner theScan) throws IOException {
        final Queue<String> formatted = new LinkedList<String>();
        int i = 0;
                
        readingStartFormatString(theScan, formatted, i++);
        readingStartFormatString(theScan, formatted, i++);
        
        readingStartFormatInt(theScan, formatted, i++);
        readingStartFormatInt(theScan, formatted, i++);
        readingStartFormatInt(theScan, formatted, i++);
        readingStartFormatInt(theScan, formatted, i++); 
        readingStartFormatInt(theScan, formatted, i); 
        
        settingFields(formatted);
    }

    /**
     * Reading the string start format of the race.
     * 
     * @param theScan the file to read
     * @param theFormatted the formatted race info list that could be shared 
     * @param theI the integer for what of the raceFormat to call
     * @throws IOException throws the exception
     */
    private void readingStartFormatInt(final Scanner theScan, 
                                       final Queue<String> theFormatted,
                                       final int theI)
                    throws IOException {
        myCurrentLine = theScan.nextLine();
        try {
            if (myCurrentLine.startsWith(myRaceFormat[theI]) && Integer.
                    parseInt(myCurrentLine.substring(myRaceFormat[theI].length())) > 0) {
                theFormatted.add(myCurrentLine.substring(myRaceFormat[theI].length()));
            } else {
                fireErrorThrowIOEx();
            }
        } catch (final NumberFormatException nfe) {
            fireErrorThrowIOEx();
        }
    }

    /**
     * Reading the int start format of the race.
     * 
     * @param theScan the file to read
     * @param theFormatted the formatted race info list that could be shared 
     * @param theI the integer for what of the raceFormat to call
     * @throws IOException throws the exception
     */
    private void readingStartFormatString(final Scanner theScan, 
                                          final Queue<String> theFormatted,
                                          final int theI)
                    throws IOException {
        if (!theScan.hasNextLine()) {
            fireErrorThrowIOEx();
        }
        
        myCurrentLine = theScan.nextLine();
        if (myCurrentLine.startsWith(myRaceFormat[theI])) {
            theFormatted.add(myCurrentLine.substring(myRaceFormat[theI].length()));
        } else {
            fireErrorThrowIOEx();
        }
    }

    /**
     * Reading the participants of the file.
     * 
     * @param theScan the file to read
     * @throws IOException throws the exception
     */
    private void readingParticipants(final Scanner theScan) throws IOException {        
        for (int i = 0; i < myRaceParticipantsSize; i++) {
            myCurrentLine = theScan.nextLine();
            if (myCurrentLine.charAt(0) == '#') {
                myTokenizer = new StringTokenizer(myCurrentLine.substring(1), SEPARATOR);
                if (myTokenizer.countTokens() != PARTICIPANTS_FORMAT_LENGTH) {
                    fireErrorThrowIOEx();
                }
                try {
                    final int racerId = Integer.parseInt(myTokenizer.nextToken());
                    if (racerId < 0 || racerId > RACER_ID_MAX) {
                        fireErrorThrowIOEx();
                    }
                    final String racerName = myTokenizer.nextToken();
                    final double racerTime = Double.parseDouble(myTokenizer.nextToken());
                    if ((BigDecimal.valueOf(racerId)).scale() > 2) {
                        fireErrorThrowIOEx();
                    }
                   
                    myRacers.add(new Racer(racerId,
                                           racerName,
                                           racerTime));
                } catch (final NumberFormatException nfe) {
                    fireErrorThrowIOEx();
                }
            } else {
                fireErrorThrowIOEx();
            }
        }
        
        settingUpSortedIdList();
    }

    /**
     * Sorting the racers by its id and making that ids into a list.
     */
    private void settingUpSortedIdList() {
        Collections.sort(myRacers);
        
        mySortedRacerIds = new ArrayList<Integer>();
        for (final Racer racer : myRacers) {
            mySortedRacerIds.add(racer.getMyId());
        }
    }
    
    /**
     * Reading races of the Leaderboard, Telemetry, and Crossing type.
     * 
     * @param theStartFormat the starting format of line
     * @param theFormatTypeLength the number of format type 
     * @throws IOException throws the exception
     */
    private void readingRaceMessage(final String theStartFormat
                             , final int theFormatTypeLength) 
                                             throws IOException {
        // getting the tokenizer ready
        myTokenizer = new StringTokenizer(myCurrentLine.substring(
                                    theStartFormat.length()), SEPARATOR);

        // if the number of tokens doesn't match theFormatTypeLength
        // this depends on what type the messages
        if (myTokenizer.countTokens() != theFormatTypeLength) {
            fireErrorThrowIOEx();
        }

        // next token is already valid from the readingRaceMessages method
        // this if statement handles adding the right messages in the right index
        final int timestamp = Integer.parseInt(myTokenizer.nextToken());
        while (timestamp != myCurrentTimestamp) {
            myMessages.add(new ArrayList<Message>());
            myCurrentTimestamp++;
        }       
        
        if (RACE_FORMAT_TELEMETRY.equals(theStartFormat)) {
            readTType();
        } else if (RACE_FORMAT_LEADERBOARD.equals(theStartFormat)) {
            readLType();
        } else if (RACE_FORMAT_CROSSING.equals(theStartFormat)) {
            readCType();
        } else {
            fireErrorThrowIOEx();
        }           
    }

    /**
     * Read's the crossing type messages.
     * 
     * @throws IOException throws the exception
     */
    private void readCType() throws IOException {      
        myMessages.get(myCurrentTimestamp).
            add(new CrossingMessage(myCurrentLine, myCurrentTimestamp, readRaceId()));
        readRaceInt(); // new lap
        readRaceBoolean(); // finish? 
    }

    /**
     * Checks if the next token is a boolean.
     * 
     * @throws IOException throws the exception
     * @return the boolean
     */
    private boolean readRaceBoolean() throws IOException {
        boolean bool = false;
        try {
            bool = Boolean.parseBoolean(myTokenizer.nextToken()); 
        } catch (final NumberFormatException nfe) {
            fireErrorThrowIOEx();
        }
        return bool;
    }

    /**
     * Checks if the int from the scanner is valid.
     * 
     * @throws IOException throws the exception
     * @return the int
     */
    private int readRaceInt() throws IOException {
        int i = 0;
        try {
            i = Integer.parseInt(myTokenizer.nextToken());
            if (i < 0) {
                fireErrorThrowIOEx();
            }
        } catch (final NumberFormatException nfe) {
            fireErrorThrowIOEx();
        }
        return i;
    }

    /**
     * Read's the leaderboard type messages. Checks if the leaderoard ids are identical 
     * to our racer list id
     * 
     * @throws IOException throws the exception
     */
    private void readLType() throws IOException {
        final List<Integer> formattedInts = new ArrayList<Integer>();
        
        try {
            while (myTokenizer.hasMoreTokens()) {
                formattedInts.add(Integer.parseInt(myTokenizer.nextToken()));
            }
        } catch (final NumberFormatException nfe) {
            fireErrorThrowIOEx();
        }
        
        // sorts the list to check if its equal to the sortedRacerId list
        Collections.sort(formattedInts);
        if (!mySortedRacerIds.equals(formattedInts)) {
            fireErrorThrowIOEx();
        }
        
        myMessages.get(myCurrentTimestamp).add(new LeaderboardMessage(myCurrentLine, 
                                                                      myCurrentTimestamp));
    }
    
    /**
     * Read's the telemetry type messages.
     * 
     * @throws IOException throws the exception
     */
    private void readTType() throws IOException {        
        myMessages.get(myCurrentTimestamp).add(new TelemetryMessage(myCurrentLine, 
                                                                    myCurrentTimestamp, 
                                                                    readRaceId(),
                                                                    readRaceDouble(), 
                                                                    readRaceInt()));
    }

    /**
     * Reads and checks the double for its validity.
     * 
     * @throws IOException throw the exception
     * @return the double
     */
    private double readRaceDouble() throws IOException {
        double d = 0.0;
        try {
            // this code doesn't check how many decimals
            d = Double.parseDouble(myTokenizer.nextToken()); 
// if you want to check for decimals            
//            if ((new BigDecimal(myTokenizer.nextToken())).scale() > 2) {
//                fireErrorThrowIOEx();
//            }
        } catch (final NumberFormatException nfe) {
            fireErrorThrowIOEx();
        }
        return d;
    }

    /**
     * Reads and checks the ID for its validity.
     * 
     * @throws IOException the exception thrown
     * @return the id
     */
    private int readRaceId() throws IOException {
//        if (!containsId(theTemp.nextInt())) {
//            fireErrorThrowIOEx();
//        }  
        int i = 0;
        try {
            i = Integer.parseInt(myTokenizer.nextToken());
            if (!containsId(i)) {
                fireErrorThrowIOEx();
            }
        } catch (final NumberFormatException nfe) {
            fireErrorThrowIOEx();
        }
        return i;
    }
    
    /**
     * If theId is in the racers.
     * 
     * @param theId the Id we are comparing with
     * @return the boolean if the id is in the racers.
     */
    private boolean containsId(final int theId) {
        boolean bool = false;
        for (final Racer racer : myRacers) {
            if (theId == racer.getMyId()) {
                bool = true;
            }
        }
        return bool;
    }
    
    /**
     * Closes the scan and throw IOException.
     * 
     * @throws IOException the exception thrown.
     */
    private void fireErrorThrowIOEx() throws IOException {
        fireRaceMessage(new LoadingMessage(ERROR_MSG));
        myOffRacerIdList = new ArrayList<Integer>(myTempOffRacerIdList);
        myRacers = new ArrayList<Racer>(myTempRacers);
        myMessages = new ArrayList<ArrayList<Message>>(myTempMessages); 
        mySortedRacerIds = new ArrayList<Integer>(myTempSortedRacerIds);
        myZeroFlag = myTempZeroFlag;
        throw new IOException();
    }

    /**
     * Setting up some fields.
     * 
     * @param theFormatted the formatted string for the fields
     */
    private void settingFields(final Queue<String> theFormatted) {
        myRaceName = theFormatted.remove();
        myRaceTrack = theFormatted.remove();
        
        setAndFireWidthHeight(theFormatted);
        
        myRaceDistance = Integer.parseInt(theFormatted.remove());
        myRaceMaxTime = Integer.parseInt(theFormatted.remove());
        myRaceParticipantsSize = Integer.parseInt(theFormatted.remove());
    }

    /**
     * Sets and fire the width and height.
     * 
     * @param theFormatted the queue where the width and height are
     */
    private void setAndFireWidthHeight(final Queue<String> theFormatted) {
        final int oldRaceWidth = myRaceWidth;
        final int oldRaceHeight = myRaceHeight;
        
        myRaceWidth = Integer.parseInt(theFormatted.remove());
        myRaceHeight = Integer.parseInt(theFormatted.remove());
        
        myPcs.firePropertyChange(PROP_NAME_RACE_WIDTH, oldRaceWidth, myRaceWidth);
        myPcs.firePropertyChange(PROP_NAME_RACE_HEIGHT, oldRaceHeight, myRaceHeight);
    }
    
    @Override
    public void addPropertyChangeListener(final PropertyChangeListener theListener) {
        myPcs.addPropertyChangeListener(theListener);

    }

    @Override
    public void addPropertyChangeListener(final String thePropertyName,
                                          final PropertyChangeListener theListener) {
        myPcs.addPropertyChangeListener(thePropertyName, theListener);

    }

    @Override
    public void removePropertyChangeListener(final PropertyChangeListener theListener) {
        myPcs.removePropertyChangeListener(theListener);

    }

    @Override
    public void removePropertyChangeListener(final String thePropertyName,
                                             final PropertyChangeListener theListener) {
        myPcs.removePropertyChangeListener(thePropertyName, theListener);

    }
}
