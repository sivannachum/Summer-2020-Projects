package edu.smith.jeopardy;

import java.awt.Font;
// Colors! https://teaching.csse.uwa.edu.au/units/CITS1001/colorinfo.html
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JFrame;

/**
 * This class creates a Jeopardy GUI to display an interactive Jeopardy game.
 * There are one-player and two-player options.
 * @author sivan
 *
 */
public class JeopardyGUI {
	// Determine the size and location of the JFrame
	public static final int xSize = 1000;
	public static final int ySize = 600;
	public static final int xLoc = 170;
	public static final int yLoc = 70;
	
	// Determine the number of questions in the game
	public static final int numQuestions = 20;
	
	// Determine colors and fonts to be used in the game
	public static final Color bl = new Color(0, 0, 190);
	public static final Color sil = Color.GRAY;
	public static final Color wh = Color.WHITE;
	public static final Font defaultFont = new Font("Helvetica", Font.PLAIN,40);
	public static final Font defaultFont2 = new Font("Helvetica", Font.PLAIN,30);
	public static final Font instructionsFont = new Font("Helvetica", Font.PLAIN,21);
	// Keep track of player's scores
	public static int score1 = 0;
	public static int score2 = 0;
	
	// Create the game!
	public static void main(String[] args) {
		// Create the JFrame that will hold everything
		JFrame game = new JFrame("Jeopardy Game");
		// Allow the game to toggle between panels
		game.setLayout(new FlowLayout());
		// Set the frame's size and location
		game.setSize(xSize, ySize);
		game.setLocation(xLoc,yLoc);
		// Stop running the code when the user exes out of the frame!
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		HomePanel homepanel = new HomePanel();
		// Add the home panel to the game
		game.add(homepanel);
		
		GameSetup setup = new GameSetup();
		// Add the buttons for one-player and two-player games to the home panel.
		// Those buttons lead to instructions for the games,
		// which include back buttons to the home panel.
		OnePlayerInstructions oneP = new OnePlayerInstructions(game, homepanel, setup);
		game.add(oneP);
		oneP.addBackButton(homepanel);
	    homepanel.addGameButton(oneP, "One-Player Game", 0, 4, bl);
	    
	    TwoPlayerInstructions twoP = new TwoPlayerInstructions(game, homepanel, setup);
	    game.add(twoP);
	    twoP.addBackButton(homepanel);
	    homepanel.addGameButton(twoP, "Two-Player Game", 0, 5, sil);
	    
		// Only make the homepanel (and of course the game) visible at first
	    oneP.setVisible(false);
	    twoP.setVisible(false);
		homepanel.setVisible(true);
		game.setVisible(true);
	}
	
}