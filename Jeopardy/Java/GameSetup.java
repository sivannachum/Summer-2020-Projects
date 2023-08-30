package edu.smith.jeopardy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.JFrame;

/**
 * This class provides an object that reads the jeopardy.txt file
 * to extract all the question info!
 * This object can then be used to begin a game (get it up and running)!
 * @author sivan
 *
 */
public class GameSetup {
	// Count the number of rows in the text file
	private int numRows = 0;
	// Store the contents of each row of the text file
	private List<String[]> lines = new ArrayList<String[]>();
	// This variable will make sure each question in a single game is unique (no repeats)
	private Set<Integer> rows = new HashSet<>();
	
	public GameSetup() {
		// Read the jeopardy text file
		String file = "jeopardy.txt";
		// Split its context according to the following regex,
		// which simply represents the string {+~}
	    String delimiter = "\\{\\+~\\}";
	    String line;
	    
	    // Read the file!
	    try (BufferedReader br = new BufferedReader(new FileReader(new File(file)))) {
	    	while((line = br.readLine()) != null){
	    		// Split each line on the delimiter, add the values to lines
	            String[] values = line.split(delimiter);
	            lines.add(values);
	            // Keep track of the number of rows
	            numRows++;
	        }
	    } catch (Exception e){
	        System.out.println(e);
	    }
	}
	
	/**
	 * Do all the set up for the game (make the panels, add flow, etc.) and
	 * start the game (on the first panel).
	 * @param game - the JFrame that holds the whole GUI
	 * @param name1 - the name of the first player
	 * @param name2 - the name of the second player
	 * @param onePlayer - true if this is a one-player game, false if this is a two-player game
	 */
	public void start(JFrame game, HomePanel homepanel, String name1, String name2, boolean onePlayer) {
		// Initialize the scores to 0
		// If this is a one-player game, only score1 will actually be used
		JeopardyGUI.score1 = 0;
		JeopardyGUI.score2 = 0;
		// A user can get the same question as in a previous game
		rows.clear();
		
		// Generate random indices into the lines List to create questions for the game
	    // Make sure no index is repeated (each question is unique)
	    int i = 0;
		while (i < JeopardyGUI.numQuestions) {
			Random rand = new Random();
			int row = rand.nextInt(numRows);
			if (!rows.contains(row)) {
				rows.add(row);
				i++;
			}
		}
		// Create an iterator through the Set containing the indices
		Iterator<Integer> rowsIterator = rows.iterator();
		
		// Create an array containing all the question panels
		GamePanel[] gamescreens = new GamePanel[20];
		// Actually generate the question panels
		for (i = 0; i < JeopardyGUI.numQuestions; i++) {
			GamePanel gamescreen;

			if (onePlayer) {
				gamescreen = new OnePlayerGamePanel(lines.get(rowsIterator.next()), name1, i+1);
			} else {
				gamescreen = new TwoPlayerGamePanel(lines.get(rowsIterator.next()), name1, name2, i+1);
			}

			game.add(gamescreen);
			gamescreen.setVisible(false);
			gamescreen.setFocusable(true);
			gamescreens[i] = gamescreen;
		}
		
		// After all the question panels are created, give each the functionality
		// to go to the next panel,
		// and give the last one the functionality to go to the end screen.
		for (i = 0; i < JeopardyGUI.numQuestions-1; i++) {
			gamescreens[i].setNextScreen(gamescreens[i+1]);
		}
		
		// Also create the end screen :)
		EndPanel ending = new EndPanel();
		ending.addBackButton(homepanel);
		ending.setVisible(false);
		game.add(ending);
		
		gamescreens[JeopardyGUI.numQuestions-1].setNextScreen(ending);
		
		// This starts the game!
		// Set the first GamePanel visible, have it grab the focus, and start its timer.
		gamescreens[0].setVisible(true);
		gamescreens[0].grabFocus();
		GameTimer firstTimer;
		if (onePlayer) {
			firstTimer = new GameTimer(gamescreens[0]);
		}
		else {
			firstTimer = new GameTimer(gamescreens[0], name1, name2);
		}
		firstTimer.startReading();
	}
}
