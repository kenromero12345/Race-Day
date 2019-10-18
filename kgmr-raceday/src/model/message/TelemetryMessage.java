/*
 * TCSS 305 - Autumn 2018
 * Assignment 5 - Raceday
 */
package model.message;

/**
 * Telemetry message concrete class.
 * 
 * @author Ken Gil Romero kgmr@uw.edu
 * @version Fall 18 
 */
public class TelemetryMessage extends AbstractMessageWithId {
    
    /**
     * The racer's lap.
     */
    private final int myLap;
    
    /**
     * The racer's distance.
     */
    private final double myDistance;
    
    /**
     * Telemetry message constructor for view.
     * 
     * @param theMessage the passed message
     * @param theTimestamp the timestamp
     * @param theId the id passed
     * @param theDistance the distane of the racer
     * @param theLap the lap of the racer
     */
    public TelemetryMessage(final String theMessage, 
                            final int theTimestamp, 
                            final int theId,
                            final double theDistance, 
                            final int theLap) {
        super(theMessage, theTimestamp, theId);
        myLap = theLap;
        myDistance = theDistance;
    }

    @Override
    public String getType() {
        return "Telemetry";
    }

    /**
     * @return the racer's lap
     */
    public int getMyLap() {
        return myLap;
    }

    /**
     * @return the message's distance
     */
    public double getMyDistance() {
        return myDistance;
    }
}
