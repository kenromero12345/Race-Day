/*
 * TCSS 305 - Autumn 2018
 * Assignment 5 - Raceday
 */
package model.message;

/**
 * Message interface.
 * 
 * @author Ken Gil Romero kgmr@uw.edu
 * @version Fall 18 
 */
public interface Message {
    
    /**
     * @return the type of messages
     */
    String getType();
    
    /**
     * @return the timestamp of the messages
     */
    int getTimeStamp();
}
