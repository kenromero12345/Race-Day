/*
 * TCSS 305 - Autumn 2018
 * Assignment 5 - Raceday
 */
package controller;

import static model.controls.PropertyChangeEnabledRaceControls.PROPERTY_TIME;
import static model.controls.PropertyChangeEnabledRaceControls.PROP_NAME_RACER;
import static model.controls.PropertyChangeEnabledRaceControls.PROP_NAME_RACE_DONE;
import static model.controls.PropertyChangeEnabledRaceControls.PROP_NAME_RACE_INFO;
import static model.controls.PropertyChangeEnabledRaceControls.PROP_NAME_RACE_MAX_TIMESTAMP;
import static model.controls.PropertyChangeEnabledRaceControls.PROP_NAME_MSG;

import controller.action.NonToggleAction;
import controller.action.ToggleAction;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.Timer;
import javax.swing.text.DefaultCaret;
import model.RaceDayModel;
import model.Racer;
import model.controls.PropertyChangeEnabledRaceControls;
import model.message.LeaderboardMessage;
import model.message.Message;
import view.RaceDayView;

/**
 * Controller class of the Raceday classes.
 * 
 * @author Charles Bryan
 * @author Ken Gil Romero 
 * @version Fall 18
 */
public class RaceDayController extends JFrame {

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
    
    /** Major ticks once the race is loaded for the slider. */
    private static final int MAJOR_TICKS_PER_MILLISECOND = 60000;

    /** Minor ticks once the race is loaded for the slider. */
    private static final int MINOR_TICKS_PER_MILLISECOND = 10000;
        
    /** My timer label. */
    private static JLabel myTimerLabel;
    
    /** My speed times 1. */
    private static final int SPEED_TIMES_1 = 1;

    /** My speed times 4. */
    private static final int SPEED_TIME_4 = 4;
    
    /** Clear image file.  */
    private static final String IMAGES_IC_CLEAR_PNG = "./images/ic_clear.png";
    
    /** Clear text. */
    private static final String COMP_NAME_CLEAR = "Clear";
    
    /** Loop race text. */
    private static final String COMP_NAME_LOOP_RACE = "Loop Race";

    /** Single race text. */
    private static final String COMP_NAME_SINGLE_RACE = "Single Race";
    
    /** Repeat image file. */
    private static final String IMAGES_IC_REPEAT_PNG = "./images/ic_repeat.png";
    
    /** Restart image file. */
    private static final String IMAGES_IC_RESTART_PNG = "./images/ic_restart.png";
    
    /** Pause image file. */
    private static final String IMAGES_IC_PAUSE_PNG = "./images/ic_pause.png";

    /** Play image file. */
    private static final String IMAGES_IC_PLAY_PNG = "./images/ic_play.png";

    /** Play text. */
    private static final String COMP_NAME_PLAY = "Play";

    /** Pause text. */
    private static final String COMP_NAME_PAUSE = "Pause";

    /** Restart text. */
    private static final String COMP_NAME_RESTART = "Restart";
    
    /** Repeat color image file. */
    private static final String IMAGES_IC_REPEAT_COLOR_PNG 
        = "./images/ic_repeat_color.png";
    
    /** Four times image file. */
    private static final String IMAGES_IC_FOUR_TIMES_PNG = "./images/ic_four_times.png";
    
    /** One times image file. */
    private static final String IMAGES_IC_ONE_TIMES_PNG = "./images/ic_one_times.png";
    
    /** Times four text. */
    private static final String COMP_NAME_TIMES_FOUR = "Times Four";

    /** Times one text. */
    private static final String COMP_NAME_TIMES_ONE = "Times One";
    
    /** The height of the images depends on the width. */
    private static final int IMAGE_HEIGHT = -1;
    
    /** Icon for the jframe and about menu item. */
    private static final String IMAGES_FLAG_PNG = "./images/flag.png";
    
    /** The number of rows in the text area. */
    private static final int TEXT_AREA_ROWS = 10;
    
    /** The number of columns in the text area. */
    private static final int TEXT_AREA_COLS = 50;
        
    /** A generated serial version UID for object Serialization. */
    private static final long serialVersionUID = -8347165752415211283L;
    
    /** Padding for the border around the label. */
    private static final int PADDING = 5;
    
    /** Padding for the border around the label. */
    private static final Dimension MARGIN_UPDOWN = new Dimension(500, 25);
    
    /** Padding for the border around the label. */
    private static final Dimension MARGIN_LEFTRIGHT = new Dimension(10, 10);
    
    /** Amount of milliseconds between each call to the timer. */
    private static final int TIMER_FREQUENCY = 30; 
    
    /** The information for the about menu item. */
    private static final String ABOUT_INFO = 
                    "Ken Gil Romero\nAutumn 2018\nTCSS 305 Assignment 5";
    
    /** The width of the large images. */
    private static final int IMAGE_LARGE_WIDTH = 24;
    
    /** My frame icon in 24 by 24. */
    private final ImageIcon myJFrameIcon;

    /** The file chooser for opening and saving an image. */
    private JFileChooser myJFileChooser;
    
    /** List of the control's items. */
    private List<JMenuItem> myJMenuControlItems;
    
    /** List of the toolbar's buttons. */
    private List<JButton> myJToolbarJButtons;
    
    /** Information about the race's menu item. */
    private JMenuItem myJMenuItemRaceInfo;
    
    /** Tabbed pane where the output and participant's checkbox is. */
    private JTabbedPane myJTabbedPane;
    
    /** Controller Slider for the race. */
    private JSlider myJSlider;
    
    /** The Actions for the ToolBar and File Menu. */
    private List<Action> myActions;
    
    /** The model for reference. */
    private PropertyChangeEnabledRaceControls myModel;
   
    /** The view for reference. */
    private RaceDayView myView;
    
    /** The timer to control how often to advance the Time object. */ 
    private Timer myTimer;
    
    /** The time multiplier. */
    private int myMultiplier;
    
    /** The information for the race info menu item. */
    private String myRaceInfo;
    
    /** The text area messages. */
    private JTextArea myJTextAreaOutput;

    /** The time of the race. */
    private int myTime;
    
    /** If the race is loopable. */
    private boolean myIsLoop;
    
    /** JPanel for the participants' checkbox. */
    private JPanel myJPanelParticipantsCb;

    /** The participants size. */
    private int myParticipantSize;
    
    /** The check box for select all. */
    private JCheckBox mySelectAllCkBx;

    /** Flag for when the application loaded succesfully at least once. */
    private boolean myFlag;
    
    /** 
     * Salvages participant size so that if there is an error when loading a file,
     * participant size could get this.
     */
    private int myTempParticipantSize;

    /** Start flag to allow the leaderboard message to change view's panel. */
    private boolean myStartFlag;
    
    /** Helps start flag when there is an error. */
    private boolean myTempStartFlag;
     
    /**
     * Constructor for setting up most of the fields.
     * 
     * @param theModel for our model to access
     * @param theView for our view to access
     */
    public RaceDayController(final RaceDayModel theModel, final RaceDayView theView) {
        super("Race Day!");
        myJFrameIcon = new ImageIcon((new ImageIcon(IMAGES_FLAG_PNG)).getImage().
              getScaledInstance(IMAGE_LARGE_WIDTH, IMAGE_HEIGHT, java.awt.Image.SCALE_SMOOTH));
        setUpFields(theModel, theView);      
        myRaceInfo = "";
    }

    /**
     * Setting up fields.
     * 
     * @param theModel for our model to access
     * @param theView for our view to access
     */
    private void setUpFields(final RaceDayModel theModel, final RaceDayView theView) {
        myTimer = new Timer(TIMER_FREQUENCY, this::handleTimer);
        
        myModel = theModel;
        myView = theView;
        
        myJFileChooser = new JFileChooser(".");
        
        myJMenuControlItems = new ArrayList<JMenuItem>();
        
        myJToolbarJButtons = new ArrayList<JButton>();
        
        myJPanelParticipantsCb = new JPanel();
        
        setActions();
        
        myJTextAreaOutput = new JTextArea(TEXT_AREA_ROWS, TEXT_AREA_COLS);
        myJTextAreaOutput.setEditable(false);
        
        myMultiplier = 1;
    }
    
    /**
     * Event handler for the timer. 
     * @param theEvent the fired event
     */
    private void handleTimer(final ActionEvent theEvent) { //NOPMD
        myModel.advance(TIMER_FREQUENCY * myMultiplier);
    }

    /**
     * Setting up the actions list.
     */
    private void setActions() {
        myActions = new ArrayList<Action>();
        
        myActions.add(new NonToggleAction(COMP_NAME_RESTART, 
                                          IMAGES_IC_RESTART_PNG, 
            () -> myModel.moveTo(0)));
        myActions.add(new ToggleAction(COMP_NAME_PLAY, 
                                       COMP_NAME_PAUSE, 
                                       IMAGES_IC_PLAY_PNG, 
                                       IMAGES_IC_PAUSE_PNG, 
            () -> {
                myTimer.stop();
                myJSlider.setEnabled(true);
            }, 
            () -> {
                myTimer.start(); 
                myJSlider.setEnabled(false);
            }));
        myActions.add(new ToggleAction(COMP_NAME_TIMES_ONE, 
                                       COMP_NAME_TIMES_FOUR, 
                                       IMAGES_IC_ONE_TIMES_PNG, 
                                       IMAGES_IC_FOUR_TIMES_PNG, 
            () -> myMultiplier = SPEED_TIMES_1 , 
            () -> myMultiplier = SPEED_TIME_4));
        myActions.add(new ToggleAction(COMP_NAME_SINGLE_RACE, 
                                       COMP_NAME_LOOP_RACE, 
                                       IMAGES_IC_REPEAT_PNG, 
                                       IMAGES_IC_REPEAT_COLOR_PNG, 
            () -> myIsLoop = false, 
            () -> myIsLoop = true));
        myActions.add(new NonToggleAction(COMP_NAME_CLEAR, 
                                          IMAGES_IC_CLEAR_PNG, 
            () -> myJTextAreaOutput.setText("")));
    }   

    /**
     * Start of the application.
     */
    public static void start() {        
        final RaceDayModel model = new RaceDayModel();
        final RaceDayView view = new RaceDayView();
        final RaceDayController controller = new RaceDayController(model, view);
        
        view.start();
        
        controller.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        controller.setUpComponents();
        
        controller.pack();
        controller.setResizable(false);
        controller.setVisible(true);
        controller.setIconImage((new ImageIcon(IMAGES_FLAG_PNG)).getImage());
        
        model.addPropertyChangeListener(controller::propertyChange);
        model.addPropertyChangeListener(view);
        
        controller.toggleComponents(false);
        
    }

    /**
     * Setting up the components.
     */
    private void setUpComponents() {
        setUpMenuBar();
        
        setLayout(new BorderLayout());
        
        final JPanel jPanelCenter = setUpCenterPanel();
        add(jPanelCenter, BorderLayout.CENTER);
        
        final JToolBar jToolBar = setUpToolbar();
        add(jToolBar, BorderLayout.SOUTH);
    }

    /**
     * Setting up the center components.
     * 
     * @return the center panel
     */
    private JPanel setUpCenterPanel() {
        final JPanel jPanelCenterFlow = new JPanel();
        
        final JPanel jPanelCenter = new JPanel();
        jPanelCenter.setLayout(new BorderLayout());
        
        final JPanel jPanelCenterNorth = setUpSliderAndLabel();
        
        jPanelCenter.add(jPanelCenterNorth, BorderLayout.NORTH);
        
        final JPanel jTabbedPanePanel = setUpTabbedPane();
        jPanelCenter.add(jTabbedPanePanel, BorderLayout.CENTER);
        
        jPanelCenterFlow.add(jPanelCenter);
        return jPanelCenterFlow;
    }

    /**
     * Setting up the tabbed pane.
     * 
     * @return the tabbed pane.
     */
    private JPanel setUpTabbedPane() {
        final JPanel jPanelRaceDetails = new JPanel();
        myJTabbedPane = new JTabbedPane(); 
        
        addToJTabbedPaneTextArea();
         
        addToJTabbedPaneCheckBoxes();
        
        jPanelRaceDetails.add(myJTabbedPane);
        return jPanelRaceDetails;
    }

    /**
     * Adding the Text Area to the Jtabbedpane.
     */
    private void addToJTabbedPaneTextArea() {
        final JScrollPane jTextAreaScrollPane = new JScrollPane(myJTextAreaOutput, 
                                            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
                                            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        final DefaultCaret caret = (DefaultCaret) myJTextAreaOutput.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        myJTabbedPane.addTab("Data Output Stream", jTextAreaScrollPane);
    }

    /**
     * Adding the Check boxes to the Jtabbedpane.
     */
    private void addToJTabbedPaneCheckBoxes() {
        setJpanelParticipantsCheckBox();
        
        final JScrollPane scrollPane = new JScrollPane(myJPanelParticipantsCb, 
                                                 JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
                                                 JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(scrollPane.getSize());
        myJTabbedPane.addTab("Race Participants", scrollPane);
    }

    /**
     * Adding the checkboxes to the Jtabbedpane.
     */
    private void setJpanelParticipantsCheckBox() {
        myJPanelParticipantsCb = new JPanel();
        myJPanelParticipantsCb.setLayout(new BoxLayout(myJPanelParticipantsCb, 
                                                       BoxLayout.Y_AXIS));
        setSelectAllCheckBox();
        myJPanelParticipantsCb.add(mySelectAllCkBx);
    }

    /**
     * Setting up the select all checkbox.
     */
    private void setSelectAllCheckBox() {
        mySelectAllCkBx = new JCheckBox("Select All");
        mySelectAllCkBx.setSelected(true);
        mySelectAllCkBx.addActionListener(theEvent -> {
            setSelectAllCheckBoxAction();
        });
    }

    /**
     * The method when the select all check box is clicked.
     */
    private void setSelectAllCheckBoxAction() {
        for (int i = 1; i < myParticipantSize + 1; i++) {
            final JCheckBox cb = (JCheckBox) myJPanelParticipantsCb.getComponent(i);
            if (cb.isSelected() != mySelectAllCkBx.isSelected()) {
                cb.doClick();
            } 
        }
    }

    /**
     * Setting up the slider and its label and their panel.
     * 
     * @return the panel of the slider and its label.
     */
    private JPanel setUpSliderAndLabel() {
        final JPanel jPanelCenterNorth = new JPanel();
        
        final JPanel jPanelSlider = new JPanel(new BorderLayout());
        
        setMarginsToSlider(jPanelSlider);
        
        addAndSetSlider();
               
        jPanelSlider.add(myJSlider, BorderLayout.CENTER);
        
        jPanelCenterNorth.add(jPanelSlider); //, BorderLayout.WEST);
        
        final JPanel jPanelTimer = new JPanel(new BorderLayout());
        
        setMarginToTimerLabel(jPanelTimer);
        
        setTimerLabel(); 
        
        jPanelTimer.add(myTimerLabel, BorderLayout.WEST);
        jPanelCenterNorth.add(jPanelTimer); //, BorderLayout.EAST);
        
        return jPanelCenterNorth;   
    }

    /**
     * Add and set the sliders.
     */
    private void addAndSetSlider() {
        myJSlider = new JSlider();
        myJSlider.setValue(0);
        myJSlider.addChangeListener(theEvent -> {
            // the difference between the two is passed to advance
            // we are not using moveTo because we made it to only function for 
            // when the race is done and loops, or when the race restarts.
            myModel.advance(myJSlider.getValue() - myTime);
            //myModel.moveTo(myJSlider.getValue());
            // dont use moveTo, it has a functionality that when the passed argument is a
            // 0, some message won't run. This is because for looping and restarting
            // 
        });
    }

    /**
     * Set the timer label.
     */
    private void setTimerLabel() {
        final String formattedTime = formatTime(0);
        myTimerLabel = new JLabel(formattedTime);
        myTimerLabel.setBorder(BorderFactory.createCompoundBorder(
                                                BorderFactory.createEtchedBorder(), 
                                                BorderFactory.createEmptyBorder(PADDING, 
                                                                                PADDING, 
                                                                                PADDING, 
                                                                                PADDING)));
    }

    /**
     * Set the margin of the timer label.
     * 
     * @param theJPanelTimer the panel for the timer.
     */
    private void setMarginToTimerLabel(final JPanel theJPanelTimer) {
        JPanel margin = new JPanel();
        margin.setPreferredSize(MARGIN_LEFTRIGHT);
        theJPanelTimer.add(margin, BorderLayout.NORTH);
        margin = new JPanel();
        margin.setPreferredSize(MARGIN_LEFTRIGHT);
        theJPanelTimer.add(margin, BorderLayout.SOUTH);
        margin = new JPanel();
        margin.setPreferredSize(MARGIN_LEFTRIGHT);
        theJPanelTimer.add(margin, BorderLayout.EAST);
    }

    /**
     * Sets the margin of the slider.
     * 
     * @param theJPanelSlider the panel for the slider
     */
    private void setMarginsToSlider(final JPanel theJPanelSlider) {
        JPanel margin = new JPanel();
        margin.setPreferredSize(MARGIN_UPDOWN);
        theJPanelSlider.add(margin, BorderLayout.NORTH);
        margin = new JPanel();
        margin.setPreferredSize(MARGIN_UPDOWN);
        theJPanelSlider.add(margin, BorderLayout.SOUTH);
        margin = new JPanel();
        margin.setPreferredSize(MARGIN_LEFTRIGHT);
        theJPanelSlider.add(margin, BorderLayout.WEST);
        margin = new JPanel();
        margin.setPreferredSize(MARGIN_LEFTRIGHT);
        theJPanelSlider.add(margin, BorderLayout.EAST);
    }

    /**
     * Setting up the menu bar.
     */
    private void setUpMenuBar() {
        final JMenuBar jMenuBar = new JMenuBar();
        final JMenu jMenuFile = setJMenuFile();
        jMenuBar.add(jMenuFile);
        
        final JMenu jMenuControls = setJMenuControls();
        jMenuBar.add(jMenuControls);
        
        final JMenu jMenuHelp = setJMenuHelp();
        jMenuBar.add(jMenuHelp);
        
        setJMenuBar(jMenuBar);
    }

    /**
     * Sets the JMenuItems for help.
     * 
     * @return the setted JMenu for help
     */
    private JMenu setJMenuHelp() {
        final JMenu jMenuHelp = new JMenu("Help");
        
        setJMenuItemRaceInfo();
        
        jMenuHelp.add(myJMenuItemRaceInfo);
        
        final JMenuItem jMenuItemAbout = setJMenuItemAbout();
        jMenuHelp.add(jMenuItemAbout);
        
        return jMenuHelp;
    }

    /**
     * @return the setted JMenuitem for about
     */
    private JMenuItem setJMenuItemAbout() {
        final JMenuItem jMenuItemAbout = new JMenuItem("About...");
        jMenuItemAbout.addActionListener(theEvent -> {
            JOptionPane.showMessageDialog(this,
                                          ABOUT_INFO,
                                          getTitle(),
                                          JOptionPane.PLAIN_MESSAGE,
                                          myJFrameIcon);
        });
        return jMenuItemAbout;
    }

    /**
     * Setted the JMenuItem for race info.
     */
    private void setJMenuItemRaceInfo() {
        myJMenuItemRaceInfo = new JMenuItem("Race Info...");
        myJMenuItemRaceInfo.addActionListener(theEvent -> {
            JOptionPane.showMessageDialog(this,
                                          myRaceInfo);
        });
    }

    /**
     * Sets the JMenuItems for controls.
     * 
     * @return the setted JMenu for controls
     */
    private JMenu setJMenuControls() {
        final JMenu jMenuControls = new JMenu("Controls");
        
        for (int i = 0; i < myActions.size(); i++) {
            final JMenuItem temp = new JMenuItem(myActions.get(i));
            myJMenuControlItems.add(temp);
            jMenuControls.add(myJMenuControlItems.get(i));
        }
        return jMenuControls;
    }

    /**
     * Sets the JMenuItems for file.
     * 
     * @return a setted JMenu for file.
     */
    private JMenu setJMenuFile() {
        final JMenu jMenuFile = new JMenu("File");
        
        final JMenuItem jMenuItemLoadRace = addAndSetsMenuItemOpen();
        jMenuFile.add(jMenuItemLoadRace);
        
        final JMenuItem jMenuItemExit = addAndSetsMenuItemExit();
        jMenuFile.add(jMenuItemExit);
        
        return jMenuFile;
    }

    /**
     * @return adds and sets the JMenuItem exit 
     */
    private JMenuItem addAndSetsMenuItemExit() {
        final JMenuItem jMenuItemExit = new JMenuItem("Exit");
        jMenuItemExit.addActionListener(theEven -> {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });
        return jMenuItemExit;
    }

    /**
     * @return adds and sets the JMenuItem open
     */
    private JMenuItem addAndSetsMenuItemOpen() {
        final JMenuItem jMenuItemLoadRace = new JMenuItem("Load Race...");
        jMenuItemLoadRace.addActionListener(theEvent -> {
            final Runnable r = () -> {
                if (myJFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    try {                  
                        this.setCursor(Cursor.getPredefinedCursor
                                       (Cursor.WAIT_CURSOR)); //change cursor
                        myModel.moveTo(0);
                        myTempParticipantSize = myParticipantSize;
                        myParticipantSize = 0;
                        myStartFlag = false;
                        myTempStartFlag = myStartFlag;
                        toggleComponents(false);
                        myModel.loadRace(myJFileChooser.getSelectedFile()); 
                        toggleComponents(true);
                        setSliderTicks();
                        myFlag = true;
                        myView.repaint();
                        
                    } catch (final IOException e) {
                        JOptionPane.showMessageDialog(this,
                                        "Error loading file.");
                        if (myFlag) {
                            toggleComponents(true); 
                        }
                        myParticipantSize = myTempParticipantSize;
                        myStartFlag = myTempStartFlag;
                        //myView.repaint();
                        // already reapainted at view through listener
                    }
                    // change cursor back to orig
                    this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            };
            final Thread t = new Thread(r);
            t.start();
        });
        return jMenuItemLoadRace;
    }

    /**
     * Sets the sliders ticking.
     */
    private void setSliderTicks() {
        myJSlider.setMajorTickSpacing(MAJOR_TICKS_PER_MILLISECOND);
        myJSlider.setMinorTickSpacing(MINOR_TICKS_PER_MILLISECOND);
        myJSlider.setPaintTicks(true);
    }

    /**
     * Toggling some components.
     * 
     * @param theBool the boolean to set the components
     */
    private void toggleComponents(final boolean theBool) {
        for (final JMenuItem item: myJMenuControlItems) {
            item.setEnabled(theBool);
        }
        
        for (final JButton button: myJToolbarJButtons) {
            button.setEnabled(theBool);
        }
        
        myJMenuItemRaceInfo.setEnabled(theBool);
        
        myJTabbedPane.setEnabledAt(1, theBool);
        
        myJSlider.setEnabled(theBool);
    }

    /**
     * Setting up the toolbar.
     * 
     * @return the toolbar.
     */
    private JToolBar setUpToolbar() {
        final JToolBar jToolBar = new JToolBar();
        
        for (int i = 0; i < myActions.size(); i++) {
            final JButton tempButton = new JButton(myActions.get(i));
            tempButton.setHideActionText(true);
            myJToolbarJButtons.add(tempButton);
            
            jToolBar.add(myJToolbarJButtons.get(i));
        }
        
        return jToolBar;
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
     * Property change for components that needs it.
     * 
     * @param theEvent the event received
     */
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if (PROPERTY_TIME.equals(theEvent.getPropertyName())) {
            setTimerPropertyChage(theEvent);
        } else if (PROP_NAME_RACE_INFO.equals(theEvent.getPropertyName())) {
            final String info = (String) theEvent.getNewValue();            
            myRaceInfo = info; // gets info
        } else if (PROP_NAME_MSG.equals(theEvent.getPropertyName())) {
            final Message msg = (Message) theEvent.getNewValue();
            addTextAreaMsg(msg);
        } else if (PROP_NAME_RACER.equals(theEvent.getPropertyName())) {
            setCheckBoxesPropertyChange(theEvent); // change method so that it can cast racer
        } else if (PROP_NAME_RACE_DONE.equals(theEvent.getPropertyName())) {
            setRaceDonePropertyChange(theEvent);
        } else if (PROP_NAME_RACE_MAX_TIMESTAMP.equals(theEvent.getPropertyName())) {
            myJSlider.setMaximum((int) theEvent.getNewValue());
        } 
    }

    /**
     * Add text area message but the first seen leaderboard should be ignored.
     * 
     * @param theMsg the message to be added
     */
    private void addTextAreaMsg(final Message theMsg) {
        if (myStartFlag && theMsg instanceof LeaderboardMessage) {
            addingTextAreaMsg(theMsg);
        } else if (!(theMsg instanceof LeaderboardMessage)) {
            addingTextAreaMsg(theMsg);
        } else {
            myStartFlag = true;
        }
    }

    /**
     * Add text area message.
     * 
     * @param theMsg the message to be added
     */
    private void addingTextAreaMsg(final Message theMsg) {
        myJTextAreaOutput.append(theMsg.toString());
        myJTextAreaOutput.append("\n");
    }

    /**
     * Sets the checkboxes property change.
     * @param theEvent the event to be listened
     */
    private void setCheckBoxesPropertyChange(final PropertyChangeEvent theEvent) {
        final Racer racer = (Racer) theEvent.getNewValue();
        final JCheckBox cb = new JCheckBox(racer.getMyName());
        cb.setSelected(true);
        cb.addActionListener(e -> {
            myModel.toggleParticipant(racer.getMyId(), cb.isSelected());
            if (!cb.isSelected()) {
                mySelectAllCkBx.setSelected(false);
            }
        });
        myJPanelParticipantsCb.add(cb);
        myParticipantSize++;
    }

    /**
     * Sets the timer property change.
     * @param theEvent the event to be listened
     */
    private void setTimerPropertyChage(final PropertyChangeEvent theEvent) {
        myTime = (Integer) theEvent.getNewValue();
        final String formattedTime = formatTime(myTime);
        myTimerLabel.setText(formattedTime);
        myJSlider.setValue(myTime);
    }

    /**
     * When race is done method.
     * @param theEvent the event for when the race is done
     */
    private void setRaceDonePropertyChange(final PropertyChangeEvent theEvent) {
        final boolean isDone = (boolean) theEvent.getNewValue();
        if (myIsLoop && isDone) {
            myModel.moveTo(0);
        } else if (isDone) {
            myTimer.stop();
            ((ToggleAction) myActions.get(1)).firstToggleAction();
        }
    }
}
