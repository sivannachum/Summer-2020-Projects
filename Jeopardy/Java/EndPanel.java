package edu.smith.jeopardy;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextArea;

/**
 * This class creates an EndPanel that states
 * "This is Jeopardy" and gives the results of the game.
 * @author sivan
 *
 */
public class EndPanel extends HomePanel {
	private static final long serialVersionUID = 1L;
	private JTextArea conclusions = new JTextArea("");
	
	/**
	 * Use super() to get the "This is Jeopardy" part,
	 * then set up a conclusions JTextArea below that.
	 */
	public EndPanel() {
		super();
		
		conclusions.setEditable(false);
		conclusions.setLineWrap(true);
		conclusions.setWrapStyleWord(true);
		conclusions.setPreferredSize(new Dimension(JeopardyGUI.xSize*7/8, JeopardyGUI.ySize*11/30));
		conclusions.setOpaque(false);
		conclusions.setFont(JeopardyGUI.defaultFont2);
		conclusions.setForeground(JeopardyGUI.bl);
		
		super.c.gridx = 0;
		super.c.gridy = 4;
		super.c.gridwidth = 1;
		super.c.fill = GridBagConstraints.BOTH;
		this.add(conclusions, super.c);
	}
	
	/**
	 * Add the conclusions for a one-player game to the panel.
	 * @param name - the name of the player
	 */
	public void concludeOnePlayerGame(String name) {
		String text = "";
		// If their score is low, wish them better luck next time
		if (JeopardyGUI.score1 <= 0) {
			text = "Uh oh, " + name + ". Looks like this game didn't go too well for you." +
					"You've ended with $" + JeopardyGUI.score1 + ". Better luck next time.";
		}
		// Otherwise congratulate them for a good job
		else {
			text = "Good game, " + name + "! You ended with $" + JeopardyGUI.score1;
		}
		// Add the text to the JTextArea
		conclusions.setText(text);
	}
	
	/**
	 * Add the conclusions for a two-player game to the panel.
	 * @param name1 - the name of the first player
	 * @param name2 - the name of the second player
	 */
	public void concludeTwoPlayerGame(String name1, String name2) {
		String text = "";
		// If they tied, congratulate them both
		if (JeopardyGUI.score1 == JeopardyGUI.score2) {
			text = "Wow, you have equal intelligence, " + name1 + " and " + name2 + "! ";
		}
		// If their scores are low, comment on that
		else if (JeopardyGUI.score1 <= 0 && JeopardyGUI.score2 <= 0) {
			text = "This game didn't go too well for anybody. Remember, you can't win " + 
					"real Jeopardy in the red. Nonetheless, congratulations to ";
			// Player 1 won
			if (JeopardyGUI.score1 > JeopardyGUI.score2) {
				text = text + name1;
			}
			// Player 2 won
			else {
				text = text + name2;
			}
			text = text + " for winning! ";
		}
		// Otherwise, just congratulate the winner!
		else {
			text = "Great game, but in the end there must be a winner. Congratulations to ";
			// Player 1 won
			if (JeopardyGUI.score1 > JeopardyGUI.score2) {
				text = text + name1;
			}
			// Player 2 won
			else {
				text = text + name2;
			}
			text = text + " for winning! ";
		}
		
		text = text + name1 + " ended with $" + JeopardyGUI.score1 + " and " +
				name2 + " ended with $" + JeopardyGUI.score2 + ".";
		// Add the text to the JTextArea
		conclusions.setText(text);
	}
	
	/**
	 * Add a button to get from this panel back to the home panel
	 * @param homepanel - the home panel of the whole GUI
	 */
	public void addBackButton(HomePanel homepanel) {
		ActionListener backAction = new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				// Hide the game end panel and make the home panel visible again
				setVisible(false);
				homepanel.setVisible(true);
			}
		};
		
		// Back button is used to get back to the home panel from the end panel.
		JButton backToHome = new JButton("Back to Home Screen");
		backToHome.setPreferredSize(new Dimension(JeopardyGUI.xSize/4, JeopardyGUI.ySize/10));
		backToHome.setForeground(JeopardyGUI.wh);
		backToHome.setBackground(JeopardyGUI.sil);
		backToHome.addActionListener(backAction);
		// Arrange the back button on the end panel
		super.c.gridx = 0;
		super.c.gridy = 5;
		super.c.gridwidth = 1;
		super.c.fill = GridBagConstraints.NONE;
		this.add(backToHome, super.c);
	}
}
