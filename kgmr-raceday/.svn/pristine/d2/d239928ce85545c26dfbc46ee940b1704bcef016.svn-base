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
 * Non Toggle Action for components.
 * 
 * @author Ken Gil Romero kgmr@uw.edu
 * @version Fall 18 
 */
public class NonToggleAction extends AbstractAction {
    
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
    private static final long serialVersionUID = 1772959480451494926L;
    
    /**
     * The text to display.
     */
    private final String myText;
    
    /**
     * The big icon to display.
     */
    private ImageIcon myBigIcon;
    
    /**
     * The small icon to display.
     */
    private ImageIcon mySmallIcon;
    
    /**
     * The method to run when clicked.
     */
    private final Runnable myMethod;
    
    /**
     * Constructor for non toggle action.
     * 
     * @param theText the text
     * @param theIconText the icon text
     * @param theMethod the method
     */
    public NonToggleAction(final String theText,
                 final String theIconText,
                 final Runnable theMethod) {
        super();
        
        myText = theText;
        myMethod = theMethod;
        
        settingIcons(theIconText);
        
        putValueAll(myBigIcon, mySmallIcon, myText);
    }

    /**
     * Setting the big and small icons.
     * 
     * @param theIconText the icon text
     */
    private void settingIcons(final String theIconText) {
        final ImageIcon icon = new ImageIcon(theIconText);
        mySmallIcon = new ImageIcon(icon.getImage().getScaledInstance(
            IMAGE_SMALL_WIDTH, IMAGE_HEIGHT, java.awt.Image.SCALE_SMOOTH));
        myBigIcon = new ImageIcon(icon.getImage().getScaledInstance(
            IMAGE_LARGE_WIDTH, IMAGE_HEIGHT, java.awt.Image.SCALE_SMOOTH));
    }

    /**
     * Sets the icons, text, and run the method when clicked.
     */
    @Override
    public void actionPerformed(final ActionEvent theE) {
        putValueAll(myBigIcon, mySmallIcon, myText);
        myMethod.run();
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
}
