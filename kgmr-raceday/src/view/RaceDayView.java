/*
 * TCSS 305 - Autumn 2018
 * Assignment 5 - Raceday
 */
package view;

import static model.controls.PropertyChangeEnabledRaceControls.PROPERTY_TIME;
import static model.controls.PropertyChangeEnabledRaceControls.PROP_NAME_MSG;
import static model.controls.PropertyChangeEnabledRaceControls.PROP_NAME_RACER;
import static model.controls.PropertyChangeEnabledRaceControls.PROP_NAME_RACE_HEIGHT;
import static model.controls.PropertyChangeEnabledRaceControls.PROP_NAME_RACE_INFO;
import static model.controls.PropertyChangeEnabledRaceControls.PROP_NAME_RACE_WIDTH;
import static model.controls.PropertyChangeEnabledRaceControls.PROP_NAME_RACER_SEEN;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import model.Racer;
import model.message.LeaderboardMessage;
import model.message.LoadingMessage;
import model.message.Message;
import model.message.TelemetryMessage;
import track.VisibleRaceTrack;

/**
 * Class that will show the race.
 * 
 * @author Ken Gil Romero kgmr@uw.edu
 * @version Fall 18 
 */
public class RaceDayView extends JPanel implements PropertyChangeListener {

    /** The separator for formatted. */
    public static final String SEPARATOR = ":";
    
    /** The number of milliseconds in a second. */
    public static final int MILLIS_PER_SEC = 1000;
    
    /** The number of seconds in a minute. */
    public static final int SEC_PER_MIN = 60;
    
    /** The number of minute in a hour. */
    public static final int MIN_PER_HOUR = 60;
        
    /** A formatter to require at least 1 digit, leading 0. */
    public static final DecimalFormat ONE_DIGIT_FORMAT = new DecimalFormat("0");
    
    /** A formatter to require at least 2 digits, leading 0s. */
    public static final DecimalFormat TWO_DIGIT_FORMAT = new DecimalFormat("00");
    
    /** A formatter to require at least 3 digits, leading 0s. */
    public static final DecimalFormat THREE_DIGIT_FORMAT = new DecimalFormat("000");
    
    /**
     * Title name.
     */
    private static final String TITLE_NAME = "Race Track";

    /**
     * A generated serial version UID for object Serialization.
     */
    private static final long serialVersionUID = 3157781623977265044L;
    
    /**
     * The empty border for the leaderboard label.
     */
    private static final EmptyBorder LEADERBOARD_LABEL_BORDER = new EmptyBorder(0, 25, 0, 0);
    
    /**
     * The empty border for the racer info label.
     */
    private static final EmptyBorder RACER_INFO_LABEL_BORDER = new EmptyBorder(0, 15, 0, 0);
    
    /**
     * The empty border for the timer info label.
     */
    private static final EmptyBorder TIMER_INFO_LABEL_BORDER = new EmptyBorder(0, 0, 0, 15);
    
    /**
     * The size of the leaderboard panel.
     */
    private static final Dimension LEADERBOARD_PANEL_SIZE = new Dimension(250, 400);

    /**
     * The size of the racer info panel.
     */
    private static final Dimension RACER_INFO_PANEL_SIZE = new Dimension(700, 20);
    
    /**
     * The track panel size.
     */
    private static final Dimension TRACK_PANEL_SIZE = new Dimension(500, 400);
    
    /**
     * The size of the leaderboard panel.
     */
    private static final Dimension LEADERBOARD_RACER_PANEL_SIZE = new Dimension(200, 24);
    
    /**
     * The racer info label size.
     */
    private static final Dimension RACER_INFO_LABEL_SIZE = new Dimension(500, 20);
    
    /**
     * The timer label size.
     */
    private static final Dimension TIMER_LABEL_SIZE = new Dimension(200, 20);
    
    /** The size of participants moving around the track. */
    private static final int OVAL_SIZE = 20;
    
    /** The stroke width in pixels. */
    private static final int STROKE_WIDTH = 20;

    /**
     * The size multiplier for the track of the race.
     */
    private static final int TRACK_SIZE_MULTIPLIER = 100;    
    
    /**
     * The time label format for its text.
     */
    private static final String TIME_FORMAT = "Time: ";
    
    /**
     * The race info label format for its text.
     */
    private static final String PARTICIPANT_TEXT = "Participant: ";
    
    /** The offset of the track. */
    private static final double OFF_SET_PERCENT = .85;
    
    /**
     * The formatted time text.
     */
    private String myFormattedTime;
    
    /**
     * The width of the track.
     */
    private int myWidth;
    
    /**
     * The height of the track.
     */
    private int myHeight;
    
    /**
     * The distance of the track.
     */
    private int myDistance;

    /**
     * The panel for the leaderboard.
     */
    private JPanel myLeaderboard;
    
    /**
     * The label for the timer.
     */
    private JLabel myTimerLabel;
    
    /**
     * The laberl for the racer info.
     */
    private JLabel myRacerInfo;

    /**
     * The list of racers.
     */
    private List<Racer> myRacers;

    /**
     * The helper for the list of racers when there is an error.
     */
    private List<Racer> myTempRacers;
    
    /**
     * The list of id of the racers.
     */
    private List<Integer> myIds;
    
    /**
     * The helper for the list of id when there is an error.
     */
    private List<Integer> myTempIds;
    
    /**
     * The list of the leaderboard messages.
     */
    private Stack<LeaderboardMessage> myLeaderboardMessages;
    
    /**
     * The helper for the list of the leaderboard messages when there is an error.
     */
    private Stack<LeaderboardMessage> myTempLeaderboardMessages;
    
    /**
     * The flag for the leaderboard messages.
     */
    private boolean myMessageFlag;

    /**
     * The helper for the flag for the leaderboard messages when there is an error.
     */
    private boolean myTempMessageFlag;
    
    /**
     * The flag for the racer info text if there should be a status there.
     */
    private boolean myRacerInfoFlag;
    
    /**
     * The helper for the flag for the racer info text if there should be a status there
     *  when there is an error.
     */
    private boolean myTempRaceInfoFlag;
    
    /**
     * The racer id that should be displayed in the status bar.
     */
    private int myRacerIdForStatusBar;
    
//    /**
//     * Manager for Property Change Listeners. 
//     */
//    private final PropertyChangeSupport myPcs;
    
    /**
     * RaceDayView Constructor.
     */
    public RaceDayView() {
        super();
        setUpComponents();
        setLayout(new BorderLayout());
        setUpCollections();
    }

    /**
     * Setting up the collections.
     */
    private void setUpCollections() {
        myRacers = new ArrayList<Racer>();
        myIds = new ArrayList<Integer>();
        myLeaderboardMessages = new Stack<LeaderboardMessage>();
        myTempLeaderboardMessages = new Stack<LeaderboardMessage>();
    }

    /**
     * Setting up the components of the view.
     */
    private void setUpComponents() {
        final JPanel trackPanel = new TrackPanel();
        add(trackPanel, BorderLayout.WEST);
        
        myLeaderboard = new JPanel(); //new LeaderboardPanel();
        myLeaderboard.setPreferredSize(LEADERBOARD_PANEL_SIZE);
        add(myLeaderboard, BorderLayout.EAST);
        
        final JPanel racerInfo = new RacerInfoPanel();
        add(racerInfo, BorderLayout.SOUTH);
    }
    
    /**
     * Updates the leaderboard depending on the message received.
     * 
     * @param theMsg the message to be depended for the change of the leaderboard
     */
    private void updateLeaderboardLabels(final LeaderboardMessage theMsg) {
        myLeaderboard.removeAll();
        for (int i = 0; i < myRacers.size(); i++) {
            final JPanel p = new JPanel();
            p.setBackground(Color.LIGHT_GRAY);
            
            final JLabel l = new RacerLabel(myRacers.get(myIds.
                                indexOf(Integer.parseInt(theMsg.getIdList()[i]))));
            p.add(l);
            myLeaderboard.add(p);
            myLeaderboard.revalidate();
        }
    }
    
    /**
     * A bunch of methods that prepares the shape for the racer to be drawn.
     * 
     * @param theG2d the graphics of the paint component
     * @param theShape the shape of the racer
     * @param theRacer the racer to be drawn
     */
    private void modifiedShapeDrawn(final Graphics2D theG2d, 
                                    final Shape theShape, 
                                    final Racer theRacer) {
        theG2d.setStroke(new BasicStroke());
        theG2d.setPaint(theRacer.getMyColor());
        theG2d.fill(theShape);
        theG2d.draw(theShape);
    }
    
    /**
     * The method for setting up the racer info label's text.
     * 
     * @param theRacer the racer's info to be setted
     */
    private void setRacerInfoLabelText(final Racer theRacer) {
        final StringBuilder s = new StringBuilder(64);
        s.append("Participant: #");
        s.append(theRacer.getMyId());
        s.append(" -------");
        s.append(theRacer.getMyName());
        s.append("------- Lap: ");
        s.append(theRacer.getMyLap());
        s.append(" - Distance: ");
        s.append(theRacer.getMyCurrentPosition());
        
        myRacerInfo.setText(s.toString());
    }
    
    /**
     * The start of the application.
     */
    public void start() {
        //setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final JFrame frame = new JFrame(TITLE_NAME);
                frame.setContentPane(RaceDayView.this);
                frame.setLocationByPlatform(true);
                frame.setResizable(false);
                frame.pack();
                frame.setIconImage(new ImageIcon("./images/Sports_Car.png").getImage());
                frame.setVisible(true);
            }
        });
       
        setUpComponents();
        
        setFormattedTime(formatTime(0));
        setVisible(true);
    }
    
    /**
     * Setting up the formatted time.
     * 
     * @param theFormattedTime the formatted time in a string
     */
    private void setFormattedTime(final String theFormattedTime) {
        myFormattedTime = theFormattedTime;
        myTimerLabel.setText(TIME_FORMAT + myFormattedTime);
    }
    
    /**
     * Clears fields but stores them to a temp if there is an error.
     */
    private void reset() {
        myTempRacers = new ArrayList<Racer>(myRacers);
        myTempIds = new ArrayList<Integer>(myIds);
        myTempLeaderboardMessages.addAll(myLeaderboardMessages);
        myTempMessageFlag = myMessageFlag;
        myTempRaceInfoFlag = myRacerInfoFlag;
        
        myRacers.clear();   
        myIds.clear();
        myLeaderboardMessages.clear();
        myMessageFlag = false;
        myRacerInfoFlag = false;
        
        myRacerInfo.setText(PARTICIPANT_TEXT);
    }
    
    /**
     * If there is an error, puts back the old values for the fields.
     */
    public void salvage() {
        myRacers = new ArrayList<Racer>(myTempRacers);
        for (final Racer r : myRacers) {
            r.setMyCurrentPosition(r.getMyStartPosition());
        }
        myIds = new ArrayList<Integer>(myTempIds);
        myLeaderboardMessages.addAll(myTempLeaderboardMessages);
        myMessageFlag = myTempMessageFlag;
        myRacerInfoFlag = myTempRaceInfoFlag;
    }
    
    /**
     * Setting the width of the track.
     * 
     * @param theWidth the width of the track
     */
    private void setWidth(final int theWidth) {
        myWidth = theWidth * TRACK_SIZE_MULTIPLIER;
    }
    
    /**
     * Setting the width of the track.
     *  
     * @param theHeight the height of the track
     */
    private void setHeight(final int theHeight) {
        myHeight = theHeight * TRACK_SIZE_MULTIPLIER;
    }
    
    /**
     * Setting the distance of the track.
     * 
     * @param theDistance the distance of the track
     */
    private void setDistance(final int theDistance) {
        myDistance = theDistance;
    }
    
    /**
     * Adding a racer and its id to a list.
     * 
     * @param theRacer the racer to be added
     */
    private void addRacerAndId(final Racer theRacer) {
        myRacers.add(theRacer);
        myIds.add(theRacer.getMyId());
    }
    
    /**
     * Changes the racer's distance.
     * 
     * @param theMsg the message that could change our racers
     */
    private void changePosition(final TelemetryMessage theMsg) {
        for (final Racer r : myRacers) {
            if (r.getMyId() == theMsg.getId()) { 
                r.setMyCurrentPosition(theMsg.getMyDistance());
                r.setMyLap(theMsg.getMyLap());

                // if the clicked racer
                if (myRacerInfoFlag && r.getMyId() == myRacerIdForStatusBar) {
                    setRacerInfoLabelText(r);
                }
                //TODO: break;
            }
        }
    }
    
    /**
     * Changes the leaderboard depending if a leaderboard msg showed up.
     * 
     * @param theMsg the msg that contains the ids
     */
    private void changeLeaderboard(final LeaderboardMessage theMsg) {
        if (myLeaderboardMessages.contains(theMsg)) { // change label
            if (myLeaderboardMessages.indexOf(theMsg) != 0) {
                myLeaderboardMessages.pop();
                updateLeaderboardLabels(myLeaderboardMessages.peek());
            }
        } else {                       
            if (myMessageFlag) { // only push for distinct, flag helps that
                myLeaderboardMessages.push(theMsg);  // because two timestamp 0 are fired
            }
            myMessageFlag = true;
            updateLeaderboardLabels(theMsg);
        }
    }
    
    /**
     * Toggles if the racer is seen.
     * 
     * @param theRacerId the racer's id we are changing if its able to be seen
     */
    public void toggleRacerIsSeen(final int theRacerId) {
        myRacers.get(myIds.indexOf(theRacerId)).toggleMyIsSeen();
        repaint();
    }
    
    /**
     * Change view's racer position and leaderboard.
     * @param theMsg the message the potentially changes the view
     */
    private void changeView(final Message theMsg) {
        if (theMsg instanceof TelemetryMessage) {
            changePosition((TelemetryMessage) theMsg);
            repaint();
        } else if (theMsg instanceof LeaderboardMessage) {
            changeLeaderboard((LeaderboardMessage) theMsg);
        }
    }
    
    /**
     * Property change for components that needs it.
     * 
     * @param theEvent the event received
     */
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if (PROPERTY_TIME.equals(theEvent.getPropertyName())) {
            setFormattedTime(formatTime((Integer) theEvent.getNewValue()));
        } else if (PROP_NAME_RACE_INFO.equals(theEvent.getPropertyName())) {
            // sends distance to view
            final Scanner scan = new Scanner((String) theEvent.getNewValue()); 
            scan.useDelimiter("\nLap distance: ");
            scan.next();
            setDistance(scan.nextInt());
            scan.close();
        } else if (PROP_NAME_MSG.equals(theEvent.getPropertyName())) {
            messageListened(theEvent);
        } else if (PROP_NAME_RACER.equals(theEvent.getPropertyName())) {
            addRacerAndId((Racer) theEvent.getNewValue());
        } else if (PROP_NAME_RACE_WIDTH.equals(theEvent.getPropertyName())) {
            setWidth((int) theEvent.getNewValue());
        } else if (PROP_NAME_RACE_HEIGHT.equals(theEvent.getPropertyName())) {
            setHeight((int) theEvent.getNewValue());
        } else if (PROP_NAME_RACER_SEEN.equals(theEvent.getPropertyName())) {
            toggleRacerIsSeen((int) theEvent.getNewValue());
        }
    }

    /**
     * Property change when a message is fired.
     * 
     * @param theEvent the event received
     */
    private void messageListened(final PropertyChangeEvent theEvent) {
        final Message msg = (Message) theEvent.getNewValue();
        if (msg instanceof LoadingMessage) {
            if (msg.toString().equals("There is an error to the file")) {
                salvage();
            } else if (msg.toString().equals("Load file: Start - "
                            + "This may take a while. Please be patient.")) {
                reset();
            } //not working
            /*else if  (msg.toString().equals("Load file: Complete!")) {
            repaint();
             }*/
            
        } else {
            changeView(msg);
        }
    }
    
    /**
     * Formating the label to be time.
     * 
     * @param theTime the label's time.
     * @return the formatted time.
     */
    private String formatTime(final long theTime) {
        long time = theTime;
        final long milliseconds = time % MILLIS_PER_SEC;
        time /= MILLIS_PER_SEC;
        final long seconds = time % SEC_PER_MIN;
        time /= SEC_PER_MIN;
        final long min = time % MIN_PER_HOUR;
        time /= MIN_PER_HOUR;
        return TWO_DIGIT_FORMAT.format(min) + SEPARATOR
                        + TWO_DIGIT_FORMAT.format(seconds) 
                        + SEPARATOR + THREE_DIGIT_FORMAT.format(milliseconds);
    }
    
    /**
     * The class for the track panel.
     */
    class TrackPanel extends JPanel {
        
        /**
         * A generated serial version UID for object Serialization.
         */
        private static final long serialVersionUID = 3880334732058315961L;
        
        /**
         * Track panel constructor.
         */
        TrackPanel() {
            super();
            setPreferredSize(TRACK_PANEL_SIZE);
            setBackground(Color.WHITE);
            myWidth = 0; 
            myHeight = 0; 
            myDistance = 0;
            final TitledBorder titled = new TitledBorder(TITLE_NAME);
            setBorder(titled);
            addMouseListener(new RaceMouseAdapter());
        }
        
        /**
         * Paints the track and the racers.
         */
        @Override
        protected void paintComponent(final Graphics theG) {
            super.paintComponent(theG);
            final Graphics2D g2d = (Graphics2D) theG;
            
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                 RenderingHints.VALUE_ANTIALIAS_ON);
            
            final Shape ellipse = modifiedTrackDrawnAndAccess(g2d);
            
            prepareAndDrawRacers(g2d, ellipse);
        }

        /**
         * Prepares the racer for drawing, the draw them.
         * 
         * @param theG2d the graphics of the paint component
         * @param theTrack the track for the racers
         */
        private void prepareAndDrawRacers(final Graphics2D theG2d, final Shape theTrack) {
            for (final Racer r : myRacers) {
                if (r.isMyIsSeen()) {
                    final Point2D.Double pointToDrawRacer = ((VisibleRaceTrack) 
                                    theTrack).getPointAtDistance(r.getMyCurrentPosition());
                    r.setMyShape(new Ellipse2D.Double(pointToDrawRacer.getX() 
                                                                    - OVAL_SIZE / 2, 
                                                                pointToDrawRacer.getY() 
                                                                    - OVAL_SIZE / 2, 
                                                                OVAL_SIZE, 
                                                                OVAL_SIZE));
                    modifiedShapeDrawn(theG2d, r.getMyShape(), r);     
                }
            }
        }

        /**
         * Prepares the track for drawing, then draw it, the access it.
         * 
         * @param theG2d the graphics of the paint component
         * @return the track
         */
        private Shape modifiedTrackDrawnAndAccess(final Graphics2D theG2d) {
            final int width = (int) (myWidth * OFF_SET_PERCENT);
            final int height = (int) (myHeight * OFF_SET_PERCENT);
            
            final Shape ellipse = new VisibleRaceTrack((getSize().width - width) / 2, 
                                                       (getSize().height - height) / 2,  
                                                       width, 
                                                       height, 
                                                       myDistance);
            
            theG2d.setStroke(new BasicStroke(STROKE_WIDTH));
            theG2d.setPaint(Color.BLACK);
            theG2d.draw(ellipse);
            return ellipse;
        }
    }
    
    /**
     * The racer label class.
     */
    class RacerLabel extends JLabel {

        /**
         * A generated serial version UID for object Serialization.
         */
        private static final long serialVersionUID = -2531220127468938185L;
        
        /**
         * The racer for the label.
         */
        private final Racer myRacer;
        
        /**
         * RacerPanel contsructor.
         * 
         * @param theRacer the racer for the panel
         */
        RacerLabel(final Racer theRacer) {
            super();
            myRacer = theRacer;
            addMouseListener(new RaceMouseAdapter());
            setBorder(LEADERBOARD_LABEL_BORDER);
            setText(myRacer.getMyId() + ": " + myRacer.getMyName());
            setPreferredSize(LEADERBOARD_RACER_PANEL_SIZE);
            repaint();
        }
        
        /**
         * Paints the shape of the racer.
         */
        @Override
        protected void paintComponent(final Graphics theG) {
            super.paintComponent(theG);
            final Graphics2D g2d = (Graphics2D) theG;
            final Shape circle = new Ellipse2D.Double(0, 
                                                        (getSize().height - OVAL_SIZE) / 2 , 
                                                        OVAL_SIZE, 
                                                        OVAL_SIZE);
            modifiedShapeDrawn(g2d, circle, myRacer);
        }
    }
    
    /**
     * The racer info panel class.
     */
    class RacerInfoPanel extends JPanel {
        
        /**
         * A generated serial version UID for object Serialization.
         */
        private static final long serialVersionUID = 1798991085980550996L;

        /**
         * The racer info panel constructor.
         */
        RacerInfoPanel() {
            super(new BorderLayout());
            setPreferredSize(RACER_INFO_PANEL_SIZE);
            setBackground(Color.LIGHT_GRAY);
            
            setRacerInfoLabel();
            
            setTimeLabel();
        }

        /**
         * Setting up the time label.
         */
        private void setTimeLabel() {
            myTimerLabel = new JLabel(TIME_FORMAT + myFormattedTime, SwingConstants.RIGHT);
            myTimerLabel.setVerticalAlignment(SwingConstants.CENTER);
            myTimerLabel.setPreferredSize(TIMER_LABEL_SIZE);
            myTimerLabel.setBorder(TIMER_INFO_LABEL_BORDER);
            add(myTimerLabel, BorderLayout.EAST);
        }

        /**
         * Setting up the racer info label.
         */
        private void setRacerInfoLabel() {
            myRacerInfo = new JLabel(PARTICIPANT_TEXT);
            myRacerInfo.setPreferredSize(RACER_INFO_LABEL_SIZE);
            myRacerInfo.setBorder(RACER_INFO_LABEL_BORDER);
            add(myRacerInfo, BorderLayout.WEST);
        }
    }
    
    /**
     * The mouse clicker class for the race.
     */
    class RaceMouseAdapter extends MouseAdapter {
        @Override
        public void mouseClicked(final MouseEvent theEvent) {
            super.mouseClicked(theEvent);
            myRacerInfoFlag = true;
            if (theEvent.getSource() instanceof RacerLabel) {
                final Racer r = ((RacerLabel) theEvent.getSource()).myRacer;
                myRacerIdForStatusBar = r.getMyId();
                setRacerInfoLabelText(r);
            } else if (theEvent.getSource() instanceof TrackPanel) {
                for (final Racer r : myRacers) {
                    if (r.getMyShape().contains(theEvent.getPoint()) && r.isMyIsSeen()) {
                        myRacerIdForStatusBar = r.getMyId();
                        setRacerInfoLabelText(r);
                        // TODO: break;
                    }
                }
            }
        }
    }
}
