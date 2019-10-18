/*
 * TCSS 305 - Autumn 2018
 * Assignment 5 - Raceday
 */
package model.message;

import java.util.Arrays;

/**
 * Leaderboard message concrete class.
 * 
 * @author Ken Gil Romero kgmr@uw.edu
 * @version Fall 18 
 */
public class LeaderboardMessage extends AbstractMessage {
    
    /**
     * Leaderboard message contructor.
     * 
     * @param theMessage the message passed
     * @param theTimestamp the timestamp passed
     */
    public LeaderboardMessage(final String theMessage, 
                              final int theTimestamp) {
        super(theMessage, theTimestamp);
       
    }

    @Override
    public String getType() {
        return "Leaderboard";
    }
    
    /**
     * @return the tokenizer with the list of id
     */
    public String[] getIdList() {
        return Arrays.copyOfRange(myMessage.split(":"), 2, myMessage.length());
    }
}
