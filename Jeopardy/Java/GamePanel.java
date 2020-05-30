package edu.smith.jeopardy;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

/**
 * This class creates a GamePanel that has a clue and its answer,
 * information about the question (category, a concept of how much money it was worth
 * in the original game+, etc.) and information on the show it is from (date,
 * if any notable players were in it, etc.)
 * 
 * None of the elements of the panel are focusable (except the Next Button - ONLY
 * WHEN ITS VISIBLE) because we want the user to be able to click Keyboard buttons
 * to buzz in and have the KeyBindings for those buttons live in the JPanel.
 * We don't want the user to accidentally click somewhere on the screen
 * then not have their Keyboard clicks be registered :(
 * 
 * + The money value may not be exactly correct as Jeopardy changed its money values
 * over the years whereas I kept them consistent in the code ($200-$1000 in Jeopardy,
 * $400-$2000 in Double Jeopardy; I made all Final Jeopardy clues worth just $1000).
 * @author sivan
 *
 */
public abstract class GamePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private String showTitle;
	private String showComments;
	private String whichRound;
	private String category;
	private int amount;
	private String question;
	private String answer;
	private int questionNumber;
	private JLabel answerLabel;
	private JLabel countdownLabel = new JLabel("");
	
	// These need to be public so children classes, namely OnePlayerGamePanel and
	// TwoPlayerGamePanel, can access them.
	public JPanel next = new JPanel();
	public JButton nextButton = new JButton("Next Page");
	public GridBagConstraints c;
	
	/**
	 * Set up the game panel with everything except a place to display the players' scores
	 * and the next button's functionality.
	 * @param line - The line of the text file that the GamePanel is referencing,
	 * already split in the proper places. This will always have length 7.
	 * @param id - The GamePanel's question number in the game
	 */
	GamePanel(String[] line, int id){
		super(new GridBagLayout());
		
		c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		
		showTitle = line[0];
		showComments = line[1];
		whichRound = line[2];
		category = line[3];
		amount = Integer.parseInt(line[4]);
		question = line[5];
		answer = line[6];
		questionNumber = id;
		
		this.addCountdownLabel();
		this.addQuestionInformation();
		this.addQuestion();
		this.addAnswer();
		this.addShowInfo();
		this.setUpNextButton();
	}
	
	/**
	 * Add a place for the countdowns from the GameTimer to display.
	 */
	public void addCountdownLabel() {
		countdownLabel.setFocusable(false);
		
		countdownLabel.setFont(JeopardyGUI.instructionsFont);
		countdownLabel.setForeground(JeopardyGUI.sil);
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 4;
		c.fill = GridBagConstraints.BOTH;
		this.add(countdownLabel, c);
	}
	
	/**
	 * Add details about the question (its number in THIS game, as well as
	 * its category, how much its worth, and what round it was played in in the real Jeopardy).
	 */
	public void addQuestionInformation() {
		JLabel questionInfo = new JLabel("<html>Question Number: " + questionNumber + "<br/>" +
										 "Category: " + category + 
										 ", Amount: $" + amount + 
										 ", " + whichRound + "</html>");
		
		questionInfo.setFocusable(false);
		
		questionInfo.setFont(JeopardyGUI.instructionsFont);
		questionInfo.setForeground(JeopardyGUI.bl);
		
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 4;
		c.fill = GridBagConstraints.BOTH;
		this.add(questionInfo, c);
	}
	
	/**
	 * Add the question to the GamePanel
	 */
	public void addQuestion() {
		// Make the JTextArea look more like a JLabel + text-wrapping, breaking lines on spaces:
		// https://stackoverflow.com/questions/7861724/is-there-a-word-wrap-property-for-jlabel
		
		JTextArea questionText = new JTextArea(question);
		questionText.setEditable(false);
		questionText.setLineWrap(true);
		questionText.setWrapStyleWord(true);
		questionText.setPreferredSize(new Dimension(JeopardyGUI.xSize*7/8, JeopardyGUI.ySize*11/40));
		questionText.setOpaque(false);
		
		questionText.setFocusable(false);
		
		questionText.setFont(JeopardyGUI.defaultFont);
		questionText.setForeground(JeopardyGUI.bl);
		
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 4;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		this.add(questionText, c);
	}
	
	/**
	 * Add the answer to the GamePanel, make it hidden at first
	 */
	public void addAnswer() {
		// The answer may be HTML formatted from the website, ex. <i> for italics
		// Unfortunately, wrapping in HTML here does not change \' to just ' though
		answerLabel = new JLabel("<html>" + answer + "</html>");

		answerLabel.setPreferredSize(new Dimension(JeopardyGUI.xSize*4/5, JeopardyGUI.ySize*1/6));
		answerLabel.setOpaque(false);
		
		answerLabel.setFocusable(false);
		
		answerLabel.setFont(JeopardyGUI.defaultFont2);
		answerLabel.setForeground(JeopardyGUI.bl);
		
		answerLabel.setVisible(false);
		
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 4;
		c.fill = GridBagConstraints.BOTH;
		this.add(answerLabel, c);
	}
	
	/**
	 * Add details about the show (its date and number,
	 * and the significance of the show, if any).
	 */
	public void addShowInfo() {
		JTextArea showInfo = new JTextArea("From " + showTitle + "\n" + showComments);
		showInfo.setEditable(false);
		showInfo.setLineWrap(true);
		showInfo.setWrapStyleWord(true);
		showInfo.setOpaque(false);
		
		showInfo.setFocusable(false);
		
		showInfo.setFont(JeopardyGUI.instructionsFont);
		showInfo.setForeground(JeopardyGUI.bl);
		
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 4;
		c.fill = GridBagConstraints.BOTH;
		this.add(showInfo, c);
	}
	
	/**
	 * Create the button to go to the next page of the game, but without its functionality yet
	 */
	public void setUpNextButton() {
		nextButton.setPreferredSize(new Dimension(JeopardyGUI.xSize/4, JeopardyGUI.ySize/10));
		nextButton.setForeground(JeopardyGUI.wh);
		nextButton.setBackground(JeopardyGUI.sil);
		
		// This is later set to true in the child class
		nextButton.setFocusable(false);
		
		nextButton.setVisible(false);
		
		c.gridx = 3;
		c.gridy = 5;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		this.add(nextButton, c);
	}
	
	/**
	 * Change/update the countdowns according to the timer
	 * @param text
	 */
	public void setCountdownLabelText(String text) {
		countdownLabel.setText(text);
	}
	
	/**
	 * Set this GamePanel's next panel reference (who it will go to next in the game),
	 * sort of like a linked list!
	 * @param next
	 */
	public void setNextScreen(JPanel next) {
		this.next = next;
	}
	
	/**
	 * Reveal the answer when appropriate!
	 */
	public void revealAnswer() {
		answerLabel.setVisible(true);
	}
	
	/**
	 * Ask the user if they got the question right or not,
	 * only if they have buzzed in and responded in time!
	 * @param player1 - if the user is player1 or not (not = they are player2)
	 */
	public void promptYN(boolean player1) {
		// Ask the user if the responded correctly
		String text = "Did you respond correctly? (y/n)";
		// Put this question where the countdowns used to be!
		setCountdownLabelText(text);
		
		// If y, then add the question points to their score
		// Also now allow the user to proceed to the next page
		class CorrectAnswer extends AbstractAction {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
		    	changeScore(true, player1);
		    	addNextBinding();
	    	}
		}
		CorrectAnswer correct = new CorrectAnswer();
		this.getInputMap().put(KeyStroke.getKeyStroke('y'),"correctAnswer");
		this.getActionMap().put("correctAnswer",correct);
		
		// If n, then deduce the question points from their score
		// Also now allow the user to proceed to the next page
		class IncorrectAnswer extends AbstractAction {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
		    	changeScore(false, player1);
		    	addNextBinding();
	    	}
		}
		IncorrectAnswer incorrect = new IncorrectAnswer();
		this.getInputMap().put(KeyStroke.getKeyStroke('n'),"incorrectAnswer");
		this.getActionMap().put("incorrectAnswer",incorrect);
	}
	
	/**
	 * Change the user's score if they buzzed in.
	 * Player 1 corresponds to score1 and player 2 corresponds to score2.
	 * @param correct - if the user was correct or not
	 * @param player1 - if the user is player1 or not (not = they are player2)
	 */
	public void changeScore(boolean correct, boolean player1) {
		// Increment player one's score
		if (correct && player1) {
			JeopardyGUI.score1 += amount;
		}
		// Increment player two's score
		else if (correct && !player1) {
			JeopardyGUI.score2 += amount;
		}
		// Decrement player one's score
		else if (!correct && player1) {
			JeopardyGUI.score1 -= amount;
		}
		// Decrement player two's score
		else {
			JeopardyGUI.score2 -= amount;
		}
		displayScore();
	}
	
	/**
	 * Set up the area where the users' score(s) will display
	 */
	public abstract void setUpScore();
	/**
	 * Display the score (needs to be called after the score is updated so the change
	 * is reflected in the GamePanel).
	 */
	public abstract void displayScore();
	/**
	 * Allow the user to go to the next page by hitting 'x' or by clicking the next button.
	 * Allow the next button to be focusable now!
	 */
	public abstract void addNextBinding();
}
