// Referenced https://stackoverflow.com/questions/6811064/jlabel-displaying-countdown-java
package edu.smith.jeopardy;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.Timer;

/**
 * This class creates a Game Timer that gives the player time to read the question,
 * time to buzz in, and time to respond.
 * It makes sure to display these to the user so they know what's going on.
 * @author sivan
 *
 */
public class GameTimer {
	private final int TIMER_PERIOD = 1000;
	// How long the player has to perform an action
	private final int MAX_COUNT = 5;
	private GamePanel displayOn;
	private int count = 0;
	private String text;
	private boolean stopped = false;
	private boolean player1stopped = false;
	private boolean player2stopped = false;
	private String name1 = "";
	private String name2 = "";
	private boolean answered = false;
	private String readQuestion = "Read Question: ";
	private String respondQuestion = "Buzz In: ";
	private String stateAnswer = "State your answer and click the spacebar: ";

	/**
	 * Start a Game Timer on the given Game Panel by initializing the time to read the question
	 * @param displayOn - the panel to time
	 */
	public GameTimer(GamePanel displayOn) {
		this.displayOn = displayOn;
	    text = readQuestion + (MAX_COUNT - count);
	    displayOn.setCountdownLabelText(text);
	}
	
	/**
	 * Start a Game Timer on the given Game Panel by initializing the time to read the question
	 * @param displayOn - the panel to time
	 * @param name1 - the name of player 1
	 * @param name2 - the name of player 2
	 */
	public GameTimer(GamePanel displayOn, String name1, String name2) {
		this.displayOn = displayOn;
		this.name1 = name1;
		this.name2 = name2;
	    text = readQuestion + (MAX_COUNT - count);
	    displayOn.setCountdownLabelText(text);
	}
	
	/**
	 * Start timing the time to read the question
	 */
	public void startReading() {
		new Timer(TIMER_PERIOD, new ActionListener() {
			@Override
	        public void actionPerformed(ActionEvent e) {
				// While they still have time to read, increment the count.
				// Tell them how much time they have left.
				if (count < MAX_COUNT) {
					count++;
					String text = readQuestion + (MAX_COUNT - count);
					displayOn.setCountdownLabelText(text);
	            } 
				// Once they run out of time, start giving them time to buzz in
				else {
	            	((Timer) e.getSource()).stop();
	            	count = 0;
	            	text = respondQuestion + (MAX_COUNT - count);
	            	displayOn.setCountdownLabelText(text);
	            	
	            	// If this is a one-player game, set up the Enter button to buzz in
	            	if (displayOn instanceof OnePlayerGamePanel) {
		            	class RespondingTime extends AbstractAction {
		        			private static final long serialVersionUID = 1L;
		        			public void actionPerformed(ActionEvent e) {
		        		    	stopped = true;
		        	    	}
		        		}
		        		RespondingTime respond = new RespondingTime();
		        		// Referenced http://www.java2s.com/Tutorial/Java/0260__Swing-Event/KeyStrokeSampleVKEnter.htm
		        		displayOn.getInputMap().put(KeyStroke.getKeyStroke
		        								(KeyEvent.VK_ENTER, 0, true),"respondingTime");
		        		displayOn.getActionMap().put("respondingTime",respond);
	            	}
	            	
	            	// If this is a two-player game, set up the q and p buttons to buzz in
	            	if (displayOn instanceof TwoPlayerGamePanel) {
		            	class RespondingTime1 extends AbstractAction {
		        			private static final long serialVersionUID = 1L;
		        			public void actionPerformed(ActionEvent e) {
		        				// Remove the functionality for the other player to buzz in
		        				displayOn.getInputMap().put(KeyStroke.getKeyStroke('p'),
		        											"none");
		        		    	player1stopped = true;
		        	    	}
		        		}
		        		RespondingTime1 respond1 = new RespondingTime1();
		        		displayOn.getInputMap().put(KeyStroke.getKeyStroke
		        								('q'),"respondingTime1");
		        		displayOn.getActionMap().put("respondingTime1",respond1);
		        		
		        		class RespondingTime2 extends AbstractAction {
		        			private static final long serialVersionUID = 1L;
		        			public void actionPerformed(ActionEvent e) {
		        				// Remove the functionality for the other player to buzz in
		        				displayOn.getInputMap().put(KeyStroke.getKeyStroke('q'),
		        											"none");
		        		    	player2stopped = true;
		        	    	}
		        		}
		        		RespondingTime2 respond2 = new RespondingTime2();
		        		displayOn.getInputMap().put(KeyStroke.getKeyStroke
		        								('p'),"respondingTime2");
		        		displayOn.getActionMap().put("respondingTime2",respond2);
	            	}
	            	
	            	// Start counting down the time to buzz in
	            	buzzIn();
	            }
	        }
		}).start();
	}
	
	/**
	 * Start timing the time to buzz in
	 */
	public void buzzIn() {
		new Timer(TIMER_PERIOD, new ActionListener() {
			@Override
	        public void actionPerformed(ActionEvent e) {
				// While they still have not buzzed in and still have time to do so,
				// increment the count and tell them how much time they have left.
				if (!(stopped || player1stopped || player2stopped) && count < MAX_COUNT) {
					count++;
					String text = respondQuestion + (MAX_COUNT - count);
					displayOn.setCountdownLabelText(text); // uses the reference to Welcome
	            }
				// If someone has buzzed in
				else if (stopped || player1stopped || player2stopped) {
	            	((Timer) e.getSource()).stop();
	            	count = 0;
	            	
	            	// If this is a two-player game, check who buzzed in.
	            	// Tell *that* player that they get to answer.
	            	if (player1stopped) {
	            		stateAnswer = name1 + ", " + stateAnswer;
	            	}
	            	else if (player2stopped) {
	            		stateAnswer = name2 + ", " + stateAnswer;
	            	}
	            	
	            	// Whether a one- or two-player game,
	            	// start timing the user's time to answer
		            text = stateAnswer + (MAX_COUNT - count);
		            displayOn.setCountdownLabelText(text);
	            	
	            	// Set up the action to reveal the answer when the space bar is hit
	            	class RevealAnswer extends AbstractAction {
	        			private static final long serialVersionUID = 1L;
	        			public void actionPerformed(ActionEvent e) {
	        		    	answered = true;
	        	    	}
	        		}
	        		RevealAnswer reveal = new RevealAnswer();
	        		// Referenced http://www.java2s.com/Tutorial/Java/0260__Swing-Event/KeyStrokeSampleVKEnter.htm
	        		displayOn.getInputMap().put(KeyStroke.getKeyStroke
	        								(KeyEvent.VK_SPACE, 0, true),"revealAnswer");
	        		displayOn.getActionMap().put("revealAnswer",reveal);
	            	countRespond();
				} 
				// If they do not buzz in, just reveal the answer and allow them
				// to proceed to the next page of the game.
				else {
	            	((Timer) e.getSource()).stop();
	            	displayOn.revealAnswer();
	            	displayOn.addNextBinding();
	            }
	        }
		}).start();
	}
	
	/**
	 * Start timing the time to give their response
	 */
	public void countRespond() {
		new Timer(TIMER_PERIOD, new ActionListener() {
			@Override
	        public void actionPerformed(ActionEvent e) {
				// While they have not stated their answer and still have time to do so,
				// increment the count and tell them how much time they have left.
				if (!answered && count < MAX_COUNT) {
					count++;
					text = stateAnswer + (MAX_COUNT - count);
	            	displayOn.setCountdownLabelText(text);
	            }
				// If they have answered and this is a one-player game,
				// ask them if they answered correctly and that method will do the rest.
				else if (answered && displayOn instanceof OnePlayerGamePanel) {
	            	displayOn.revealAnswer();
	            	displayOn.promptYN(true);
				}
				// If they ran out of time and did not answer and this is a one-player game,
				// reveal the answer and deduce the points for the question since
				// they buzzed in
				else if (displayOn instanceof OnePlayerGamePanel) {
					displayOn.revealAnswer();
					displayOn.changeScore(false, true);
					displayOn.addNextBinding();
	            	((Timer) e.getSource()).stop();
	            }
				// If they have answered and this is a two-player game,
				// ask them if they answered correctly and that method will do the rest.
				else if (answered && displayOn instanceof TwoPlayerGamePanel) {
	            	displayOn.revealAnswer();
	            	displayOn.promptYN(player1stopped);
				}
				// If they ran out of time and did not answer and this is a two-player game,
				// reveal the answer and deduce the points for the question since
				// they buzzed in
				else if (displayOn instanceof TwoPlayerGamePanel) {
					displayOn.revealAnswer();
					displayOn.changeScore(false, player1stopped);
					displayOn.addNextBinding();
	            	((Timer) e.getSource()).stop();
	            }
	        }
		}).start();
	}
}
