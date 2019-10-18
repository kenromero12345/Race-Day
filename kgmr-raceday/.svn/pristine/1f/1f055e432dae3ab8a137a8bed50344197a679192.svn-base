/*
 * TCSS 305 - Autumn 2018
 * Assignment 5 - Raceday
 */
package model.message;

/**
 * Message abstract with id class.
 * 
 * @author Ken Gil Romero 
 * @version Fall 18
 */
public abstract class AbstractMessageWithId extends AbstractMessage {
    
    /**
     * The first id of the message.
     */
    protected int myId;

    /**
     * Contructor of the message.
     * 
     * @param theMessage the message passed
     * @param theTimestamp the timestamp passed
     * @param theId the id passed
     */
    public AbstractMessageWithId(final String theMessage, 
                           final int theTimestamp, 
                           final int theId) {
        super(theMessage, theTimestamp);
        myId = theId;
    }
    
    /**
     * @return the id
     */
    public int getId() {
        return myId;
    }
}
