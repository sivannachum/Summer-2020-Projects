package edu.smith.jeopardy;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

/**
 * This class creates a OnePlayerGamePanel that only has one score to show
 * @author sivan
 *
 */
public class OnePlayerGamePanel extends GamePanel {
	private static final long serialVersionUID = 1L;
	private String name;
	private JLabel score = new JLabel("");
	
	/**
	 * Set up the Game Panel, including the place to display the player's score
	 * @param line - The line of the text file that the GamePanel is referencing,
	 * already split in the proper places. This will always have length 7.
	 * @param input - the player's name
	 * @param id - The GamePanel's question number in the game
	 */
	OnePlayerGamePanel(String[] line, String input, int id){
		super(line, id);
		name = input;
		
		this.setUpScore();
	}
	
	/**
	 * Add an area to display the player's score
	 */
	public void setUpScore() {
		score.setFocusable(false);
		
		score.setFont(JeopardyGUI.instructionsFont);
		score.setForeground(JeopardyGUI.sil);
		
		displayScore();
		
		super.c.gridx = 0;
		super.c.gridy = 5;
		super.c.gridwidth = 2;
		super.c.fill = GridBagConstraints.BOTH;
		this.add(score, super.c);
	}
	
	/**
	 * This method adds functionality to update the score text
	 * when the user's score is updated.
	 */
	public void displayScore() {
		score.setText(name + ": $" + JeopardyGUI.score1);
	}
	
	/**
	 * Add the functionality to go to the next page of the game.
	 * 
	 * Only add functionality to go to the next page after all the timers go + 
	 * after the user tells you if they got a question right
	 * (if they buzzed in AND answered in time).
	 * If they did not answer in time, we know that we must deduce points,
	 * so we just do so from the GameTimer class and call this method there.
	 */
	public void addNextBinding() {
		// Go to the next page using the 'x' key
		class NextScreen extends AbstractAction {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			    next.setVisible(true);
			    next.grabFocus();
			    
			    // Note: this functionality could have also been done by looking at the
			    // Game Panel's questionNumber, but we have to cast anyway so I like
			    // this way better.
			    // If next is a Game Panel, start its timer.
			    if (next instanceof GamePanel) {
			    	((GamePanel) next).displayScore();
					GameTimer nextTimer = new GameTimer((GamePanel) next);
					nextTimer.startReading();
				}
			    // Otherwise it must be the end panel, so conclude the game!
			   	else if (next instanceof EndPanel) {
					((EndPanel)next).concludeOnePlayerGame(name);
				}
		    	}
			}
		NextScreen next1 = new NextScreen();
		this.getInputMap().put(KeyStroke.getKeyStroke('x'),"nextScreen");
		this.getActionMap().put("nextScreen",next1);
		
		// Go to the next page using the next button
		ActionListener nextAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				next.setVisible(true);
				next.grabFocus();
				// If next is a Game Panel, start its timer
				if (next instanceof GamePanel) {
					((GamePanel)next).displayScore();
					GameTimer nextTimer = new GameTimer((GamePanel) next);
					nextTimer.startReading();
				}
				// Otherwise it must be the end panel, so conclude the game!
				else if (next instanceof EndPanel) {
					((EndPanel)next).concludeOnePlayerGame(name);
				}
			}
		};
			
		nextButton.addActionListener(nextAction);
		nextButton.setVisible(true);
		nextButton.setFocusable(true);
	}
}
