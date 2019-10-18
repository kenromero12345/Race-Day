/*
 * TCSS 305 - Autumn 2018
 * Assignment 5 - Raceday
 */
package application;

import controller.RaceDayController;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Class that will run the controller.
 * 
 * @author Ken Gil Romero kgmr@uw.edu
 * @version Fall 18 
 */
public final class Main {
    
    /**
     * Private constructor, to prevent instantiation of this class.
     */
    private Main() {
        throw new IllegalStateException();
    }

    /**
     * The main method, invokes the RaceDay controller. Command line arguments are
     * ignored.
     * 
     * @param theArgs Command line arguments.
     */
    public static void main(final String[] theArgs) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (final ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final UnsupportedLookAndFeelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                RaceDayController.start();
            }
        });
    }
}
