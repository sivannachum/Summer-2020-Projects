package edu.smith.jeopardy;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

/**
 * This class creates a TwoPlayerInstructions panel that gives instructions
 * for a two-player game and can start the game.
 * @author sivan
 *
 */
public class TwoPlayerInstructions extends InstructionsPanel {
	private static final long serialVersionUID = 1L;
	private JTextArea nameInput1;
	private JTextArea nameInput2;
	
	/**
	 * Add the instructions, name input area, and enter button to the TwoPlayerInstructions,
	 * as well as the enter button's functionality.
	 * The game must be passed in because the enter functionality will add panels to it.
	 * The HomePanel is needed because the EndPanel produced within the Enter key's
	 * functionality has to refer back to it.
	 * The GameSetup object is needed to start the game!
	 * @param game - the JFrame that holds the GUI
	 * @param homepanel - the home panel of the GUI
	 * @setup - the GameSetup object for the GUI
	 */
	public TwoPlayerInstructions(JFrame game, HomePanel homepanel, GameSetup setup) {
		super();
		String twoInstructions = "<html>The game has 20 questions. <br/>"
				+ "1. You will have 5 seconds to read a question. <br/> "
				+ "2. You will then have 5 seconds to respond to the question. <br/>"
				+ "3. Player 1 can buzz in by pressing the 'q' key, player 2 can use the 'p' key.<br/>"
				// Tabs
				+ "&#8195; The computer will tell you who buzzed in first. They should then say the answer aloud.<br/>"
				+ "&#8195; Use the spacebar to reveal the correct response. You must click the spacebar<br/>"
				+ "&#8195; within 5 seconds, or your time to answer will run out.<br/> "
				+ "4. You will then be prompted to tell the computer if your response was correct. <br/>"
				+ "5. If you responded correctly, you will gain as much money as the question is worth. <br/> "
				+ "6. If you responded incorrectly, you will lose as much money as the question is worth. <br/>"
				+ "7. If you do not buzz in, you will not gain or lose any money.<br/>"
				+ "8. Use the 'x' key to proceed to the next question when ready.<br/><br/>"
				+ "Please enter your names below. Hitting Enter will take you to the first question.</html>";
		this.addInstructions(twoInstructions);
		
		nameInput1 = this.addNameInput1();
		nameInput2 = this.addNameInput2();
		this.setTab();
		JButton enter = this.addEnterButton();
		addEnterFunctionality(game, homepanel, setup, enter);
	}
	
	/**
	 * Add an area to receive player one's name
	 * @return - the area for nameInput
	 */
	public JTextArea addNameInput1() {
		// Create a text area for input and add it to the panel
		JTextArea name = new JTextArea(1, 10);
		
		name.setText("Player 1");
		name.setFont(JeopardyGUI.defaultFont2);
		name.setEditable(true);
		name.setLineWrap(true);
		
		super.c.gridx = 0;
		super.c.gridy = 1;
		super.c.gridwidth = 2;
		super.c.fill = GridBagConstraints.NONE;
		this.add(name, super.c);
		
		return name;
	}
	
	/**
	 * Add an area to receive player two's name
	 * @return - the area for nameInput
	 */
	public JTextArea addNameInput2() {
		// Create a text area for input and add it to the panel
		JTextArea name = new JTextArea(1, 10);
		
		name.setText("Player 2");
		name.setFont(JeopardyGUI.defaultFont2);
		name.setEditable(true);
		name.setLineWrap(true);
		
		super.c.gridx = 1;
		super.c.gridy = 1;
		super.c.gridwidth = 2;
		super.c.fill = GridBagConstraints.NONE;
		this.add(name, super.c);
		
		return name;
	}
	
	/**
	 * Allow the user to go from inputting Player 1's name to inputting Player 2's name
	 * by hitting the Tab key.
	 */
	public void setTab() {
		class TabName extends AbstractAction {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				nameInput1.transferFocus();
	    	}
		}
		TabName tabHit = new TabName();
		nameInput1.getInputMap().put(KeyStroke.getKeyStroke
								(KeyEvent.VK_TAB, 0, true),"tabHit");
		nameInput1.getActionMap().put("tabHit",tabHit);
	}
	
	@Override
	public void addEnterFunctionality(JFrame game, HomePanel homepanel, GameSetup setup, JButton enter) {
		// Add the enter button's functionality
		enter.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				// Get rid of excess whitespace on the input
				String name1 = nameInput1.getText().trim();
				String name2 = nameInput2.getText().trim();
				// If the user hasn't entered anything, prompt them to do so
				if (name1.equals("") || name2.equals("") || name1.equals("Player 1") || name2.equals("Player 2")) {
					nameInput1.setText("Please input both names");
				}
				// Otherwise hide this panel and start the game
				else {
					setVisible(false);
					setup.start(game, homepanel, name1, name2, false);
				}
			}
		});
		
		class EnterName extends AbstractAction {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				// Get rid of excess whitespace on the input
				String name1 = nameInput1.getText().trim();
				String name2 = nameInput2.getText().trim();
				// If the user hasn't entered anything, prompt them to do so
				if (name1.equals("") || name2.equals("") || name1.equals("Player 1") || name2.equals("Player 2")) {
					nameInput1.setText("Please input names");
				}
				// Otherwise hide this panel and start the game
				else {
					setVisible(false);
					setup.start(game, homepanel, name1, name2, false);
				}
	    	}
		}
		EnterName enterHit = new EnterName();
		nameInput2.getInputMap().put(KeyStroke.getKeyStroke
								(KeyEvent.VK_ENTER, 0, true),"enterHit");
		nameInput2.getActionMap().put("enterHit",enterHit);
	}
}
