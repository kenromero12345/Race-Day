/*
 * TCSS 305 - Autumn 2018
 * Assignment 5 - Raceday
 */
package controller.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

/**
 * Toggle Action for Buttons.
 * 
 * @author Charles Bryan
 * @author Ken Gil Romero kgmr@uw.edu
 * @version 11/16/18
 */
public class ToggleAction extends AbstractAction {
    
    /**
     * The width of the large images.
     */
    private static final int IMAGE_LARGE_WIDTH = 24;
    
    /**
     * The width of the small images.
     */
    private static final int IMAGE_SMALL_WIDTH = 12;
    
    /**
     * The height of the images depends on the width.
     */
    private static final int IMAGE_HEIGHT = -1;

    /**
     * A generated serial version UID for object Serialization.
     */
    private static final long serialVersionUID = -6384308058499443135L;
    
    /**
     * The first text to display.
     */
    private final String myFirstText;
    
    /**
     * The first big icon to display.
     */
    private ImageIcon myBigFirstIcon;
    
    /**
     * The first small icon to display.
     */
    private ImageIcon mySmallFirstIcon;
    
    /**
     * The first method to run when clicked.
     */
    private final Runnable myFirstMethod;
    
    /**
     * The second text to display.
     */
    private final String mySecondText;
    
    /**
     * The second big icon to display.
     */
    private ImageIcon myBigSecondIcon;
    
    /**
     * The second small icon to display.
     */
    private ImageIcon mySmallSecondIcon;
    
    /**
     * The second method to run when clicked..
     */
    private final Runnable mySecondMethod;
    
    /** A flag for the toggle. */
    private boolean myFlag;
    
    /**
     * Constructor for toggle action.
     * 
     * @param theFirstText the first text
     * @param theSecondText the second text
     * @param theFirstIconText the first icon text
     * @param theSecondIconText the second icon text
     * @param theFirstMethod the first method
     * @param theSecondMethod the second method
     */
    public ToggleAction(final String theFirstText,
                 final String theSecondText,
                 final String theFirstIconText,
                 final String theSecondIconText,
                 final Runnable theFirstMethod,
                 final Runnable theSecondMethod) {
        super();
        
        myFirstText = theFirstText;
        myFirstMethod = theFirstMethod;
        mySecondText = theSecondText;
        mySecondMethod = theSecondMethod;      
        
        settingIcons(theFirstIconText, theSecondIconText);
        
        putValueAll(myBigFirstIcon, mySmallFirstIcon, myFirstText);
        
        myFlag = true;
    }

    /**
     * Putting the necessary values to their own key.
     * 
     * @param theBigIcon the big icon
     * @param theSmallIcon the small icon
     * @param theText the text
     */
    private void putValueAll(final ImageIcon theBigIcon, 
                             final ImageIcon theSmallIcon, 
                             final String theText) {
        putValue(Action.LARGE_ICON_KEY, theBigIcon);
        putValue(Action.SMALL_ICON, theSmallIcon);
        putValue(Action.NAME, theText);
    }

    /**
     * Setting the big and small icons.
     * 
     * @param theFirstIconText the first icon text
     * @param theSecondIconText the second icon text
     */
    private void settingIcons(final String theFirstIconText, final String theSecondIconText) {
        ImageIcon icon = new ImageIcon(theFirstIconText);
        mySmallFirstIcon = new ImageIcon(icon.getImage().getScaledInstance(
            IMAGE_SMALL_WIDTH, IMAGE_HEIGHT, java.awt.Image.SCALE_SMOOTH));
        myBigFirstIcon = new ImageIcon(icon.getImage().getScaledInstance(
            IMAGE_LARGE_WIDTH, IMAGE_HEIGHT, java.awt.Image.SCALE_SMOOTH));
        
        icon = new ImageIcon(theSecondIconText); 
        mySmallSecondIcon = new ImageIcon(icon.getImage().getScaledInstance(
            IMAGE_SMALL_WIDTH, IMAGE_HEIGHT, java.awt.Image.SCALE_SMOOTH));
        myBigSecondIcon = new ImageIcon(icon.getImage().getScaledInstance(
            IMAGE_LARGE_WIDTH, IMAGE_HEIGHT, java.awt.Image.SCALE_SMOOTH));
    }

    /**
     * When toggled, display the right icon, text, and method.
     */
    @Override
    public void actionPerformed(final ActionEvent theE) {
        if (myFlag) {
            secondToggleAction();
        } else {
            firstToggleAction();
        }
    }
    
    /**
     * The first action.
     */
    public void firstToggleAction() {
        putValueAll(myBigFirstIcon, mySmallFirstIcon, myFirstText);
        myFirstMethod.run();
        myFlag = true;
    }

    /**
     * The second action.
     */
    public void secondToggleAction() {
        putValueAll(myBigSecondIcon, mySmallSecondIcon, mySecondText);
        mySecondMethod.run();
        myFlag = false;
    }
}
