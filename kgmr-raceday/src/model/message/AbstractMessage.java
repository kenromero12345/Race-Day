/*
 * TCSS 305 - Autumn 2018
 * Assignment 5 - Raceday
 */
package model.message;

/**
 * Message abstract class.
 * 
 * @author Ken Gil Romero 
 * @version Fall 18
 */
public abstract class AbstractMessage implements Message {
    
    /**
     * The message.
     */
    protected String myMessage;
    
    /**
     * The timestamp of the message.
     */
    protected int myTimestamp;

    /**
     * Contructor of the message.
     * 
     * @param theMessage the message passed
     * @param theTimestamp the timestamp passed
     */
    public AbstractMessage(final String theMessage, 
                           final int theTimestamp) {
        myMessage = theMessage;
        myTimestamp = theTimestamp;
        
    }

    @Override
    public abstract String getType();
    
    @Override
    public int getTimeStamp() {
        return myTimestamp;
    }
    
    @Override
    public String toString() {
        return myMessage;
    }
}
