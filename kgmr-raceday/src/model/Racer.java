package model;

import java.awt.Color;
import java.awt.Shape;
import java.util.Comparator;
import java.util.Objects;
import java.util.Random;

/**
 * The racer class.
 * 
 * @author Ken Gil Romero 
 * @version Fall 18
 */
public class Racer implements Comparable<Racer> {
    
    /**
     * The max value of a color.
     */
    private static final int MAX_COLOR_VALUE = 255;

    /**
     * The multiplier for color that depends on id.
     */
    private static final int ID_MULTIPLIER_FOR_COLOR = 2;

    /**
     * The racer's id.
     */
    private int myId;
    
    /**
     * The racer's name.
     */
    private String myName;
    
    /**
     * The racer's start position.
     */
    private double myStartPosition;
    
    /**
     * The racer's current position.
     */
    private double myCurrentPosition;
    
    /**
     * The racer's lap.
     */
    private int myLap;
    
    /**
     * If the racer is viewable.
     */
    private boolean myIsSeen;
    
    /**
     * The color of the racer.
     */
    private Color myColor;
    
    /**
     * The shape of the racer.
     */
    private Shape myShape;
    
    /**
     * Constructor for the racer class.
     * 
     * @param theId the id of the racer to be setted
     * @param theName the name of the racer to be setted
     * @param theStartPosition the starting position of the racer to be setted
     */
    public Racer(final int theId, final String theName, final double theStartPosition) {
        setMyId(theId);
        setMyName(theName);
        setMyStartPosition(theStartPosition);
        setMyCurrentPosition(theStartPosition);
        myIsSeen = true;
        myLap = 0;
        setColor();
    }

    /**
     * Setting the racer's color.
     */
    private void setColor() {
        final Random rand = new Random();
        
        int redValue = setColor(rand);
        int greenValue =  setColor(rand);
        int blueValue = setColor(rand);
        
        if ((redValue == 0 && greenValue == 0 && blueValue == 0) // if colors are 0s or equals
                        || (redValue ==  greenValue && blueValue == redValue)) {
            redValue = setRandomColorValue(rand); 
            greenValue = setRandomColorValue(rand);
            blueValue = setRandomColorValue(rand);
        } 
        
        myColor = new Color(redValue, greenValue, blueValue);
    }

    /**
     * @param theRand the random object
     * @return a random color value
     */
    private int setRandomColorValue(final Random theRand) {
        return theRand.nextInt(MAX_COLOR_VALUE + 1);
    }

    /**
     * Sets the color value depending on the id, but still tries to be random.
     * 
     * @param theRand the random object
     * @return a random color value that still depends on the id
     */
    private int setColor(final Random theRand) {
        final int colorValueBaseOnId = MAX_COLOR_VALUE - myId * ID_MULTIPLIER_FOR_COLOR;
        final int color;
        if (theRand.nextBoolean()) {
            color = colorValueBaseOnId;
        } else if (theRand.nextBoolean()) {
            color = MAX_COLOR_VALUE;
        } else {
            color = 0;
        }
        return color;
    }

    /**
     * @return the starting position
     */
    public double getMyStartPosition() {
        return myStartPosition;
    }

    /**
     * Setting the starting positon.
     * 
     * @param theStartPosition the starting postion to be setted
     */
    private void setMyStartPosition(final double theStartPosition) {
        myStartPosition = theStartPosition;
    }

    /**
     * @return the racer's name
     */
    public String getMyName() {
        return myName;
    }

    /**
     * Setting the name of the racer.
     * 
     * @param theName the name to be setted
     */
    private void setMyName(final String theName) {
        myName = theName;
    }

    /**
     * @return the racer's id
     */
    public int getMyId() {
        return myId;
    }

    /**
     * Sets the id of the racer.
     * 
     * @param theId the id to be setted
     */
    private void setMyId(final int theId) {
        myId = theId;
    }

    /**
     * Compares by the id.
     * 
     * @param theOtherRacer we are comparing its id with our racer's id.
     */
    @Override
    public int compareTo(final Racer theOtherRacer) {
        return getMyId() - theOtherRacer.getMyId();
    }

    /**
     * @return the current position of the racer
     */
    public double getMyCurrentPosition() {
        return myCurrentPosition;
    }

    /**
     * Setting the current position of the racer.
     * 
     * @param theCurrentPosition the position to be setted
     */
    public void setMyCurrentPosition(final double theCurrentPosition) {
        myCurrentPosition = theCurrentPosition;
    }

    /**
     * @return if the racer is viewable
     */
    public boolean isMyIsSeen() {
        return myIsSeen;
    }

    /**
     * Setting if the racer is viewable.
     * 
     * @param theIsSeen the boolean if to be setted
     */
    public void setMyIsSeen(final boolean theIsSeen) {
        myIsSeen = theIsSeen;
    }
    
    /**
     * Toggle if the racer is viewable.
     */
    public void toggleMyIsSeen() {
        myIsSeen = !myIsSeen;
    }

    /**
     * @return the lap of the racer
     */
    public int getMyLap() {
        return myLap;
    }

    /**
     * Setting the lap of the racer.
     * 
     * @param theLap the lap to be setted
     */
    public void setMyLap(final int theLap) {
        myLap = theLap;
    }

    /**
     * Access the color of the racer.
     * 
     * @return the color of the racer
     */
    public Color getMyColor() {
        return myColor;
    }

    /**
     * Sets the color of the racer.
     * 
     * @param theColor the new color for the racer
     */
    public void setMyColor(final Color theColor) {
        myColor = theColor;
    }
    
    /**
     * If the given id is equal to our id, return a boolean.
     * 
     * @param theId the given id to compare our id
     * @return a boolean
     */
    public boolean equalsId(final int theId) {
        boolean bool = false;
        if (myId == theId) {
            bool = false;
        }
        return bool;
    }
    
    /**
     * Compares two racers with their position.
     * 
     * @return positive or negative or 0
     */
    public static Comparator<Racer> sortByPosition() {
        return (r1, r2) -> Double.compare(r1.myCurrentPosition, r2.myCurrentPosition);
    }
    
    
    @Override
    public boolean equals(final Object theOther) {
        boolean bool = false;
        if (theOther == this) {
            bool = true;
        } else if (theOther instanceof Racer) {
            final Racer other = (Racer) theOther;
            if (other.myId == myId && other.myName.equals(myName)) {
                bool = true;
            } else {
                bool = false;
            }
        } else {
            bool = false;
        }
        return bool;        
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(myId, myName, myStartPosition);
        
    }

    /**
     * @return the racer's shape
     */
    public Shape getMyShape() {
        return myShape;
    }

    /**
     * Sets the racer's shape.
     * 
     * @param theShape the shape of the racer
     */
    public void setMyShape(final Shape theShape) {
        myShape = theShape;
    }
}
