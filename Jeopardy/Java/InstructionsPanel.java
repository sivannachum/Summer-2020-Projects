package edu.smith.jeopardy;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class creates an InstructionPanel that gives instructions/rules for the game.
 * It has a back button to get back to the home panel and
 * an enter button to perform some functionality specified in the child class.
 * @author sivan
 *
 */
public abstract class InstructionsPanel extends JPanel {
	// This needs to be public so that a class that extends InstructionsPanel,
	// namely OnePlayerInstructions and TwoPlayerInstructions,
	// can also have control over where elements appear on them.
	public GridBagConstraints c;
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * An InstructionsPanel uses a GridBagLayout.
	 */
	public InstructionsPanel() {
		super(new GridBagLayout());
		c = new GridBagConstraints();
		// parameters: top, left, bottom, right
		c.insets = new Insets(5, 5, 5, 5);
	}
	
	/**
	 * Add the instructions for this game
	 * @param text - the instructions, in HTML
	 */
	public void addInstructions(String text) {
		// Center-alignment for the text
		JLabel oneInstructionsLabel = new JLabel("<html><div style='text-align: left;'>" 
									+ text + "</div></html>");
		oneInstructionsLabel.setFont(JeopardyGUI.instructionsFont);
		oneInstructionsLabel.setForeground(JeopardyGUI.bl);
		
		// Arrange the text / JLabel on the InstructionsPanel
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 4;
		c.fill = GridBagConstraints.BOTH;
		this.add(oneInstructionsLabel, c);		
	}
	
	/**
	 * Add a back button to get back from this panel to the home panel
	 * @param homepanel - the home panel for the whole GUI
	 */
	public void addBackButton(HomePanel homepanel) {
		ActionListener backAction = new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				// Hide the game instructions panel and make the home panel visible again
				setVisible(false);
				homepanel.setVisible(true);
			}
		};
		
		// Back button is used to get back to the home panel from a different panel.
		JButton backButton = new JButton("Back to Home Screen");
		backButton.setPreferredSize(new Dimension(JeopardyGUI.xSize/4, JeopardyGUI.ySize/10));
		backButton.setForeground(JeopardyGUI.wh);
		backButton.setBackground(JeopardyGUI.sil);
		backButton.addActionListener(backAction);
		
		// Arrange the back button on the instructions panel
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		this.add(backButton, c);
	}
	
	/**
	 * Add an enter button to start the game
	 * @return - the enter button
	 */
	public JButton addEnterButton() {
		// Create an enter button for the name input(s) and add it to the panel
		JButton enter = new JButton("Enter");
		enter.setPreferredSize(new Dimension(JeopardyGUI.xSize/4, JeopardyGUI.ySize/8));
		enter.setForeground(JeopardyGUI.wh);
		enter.setBackground(JeopardyGUI.bl);
		enter.setFont(JeopardyGUI.defaultFont2);
		
		// Add the button on the screen in the x-axis relative to what was last added,
		// i.e. the name input area(s)
		c.gridx = GridBagConstraints.RELATIVE;
		c.gridy = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.BOTH;
		this.add(enter, c);
		
		return enter;
	}
	
	/**
	 * Add the enter button's functionality
	 * @param game - the JFrame that holds the whole GUI
	 * @homepanel - the home panel for the GUI
	 * @setup - the GameSetup object for the GUI
	 * @param enter - the enter button
	 */
	public abstract void addEnterFunctionality(JFrame game, HomePanel homepanel, GameSetup setup, JButton enter);
}
