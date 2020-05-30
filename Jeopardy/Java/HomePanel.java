package edu.smith.jeopardy;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class creates a HomePanel that states
 * "This is Jeopardy" and includes options to get to other panels.
 * @author sivan
 *
 */
public class HomePanel extends JPanel {
	// This needs to be public so that a class that extends HomePanel, namely EndPanel
	// can also have control over where elements appear on it.
	public GridBagConstraints c;
	
	private static final long serialVersionUID = 1L;
	// Set a size for buttons that appear on a HomePanel
	private static final Dimension homeButtonSize = new Dimension(JeopardyGUI.xSize*4/5, JeopardyGUI.ySize/4);
	
	/**
	 * A HomePanel uses a GridBagLayout, and will always say "This is Jeopardy."
	 */
	public HomePanel() {
		super(new GridBagLayout());
		c = new GridBagConstraints();
		c.insets = new Insets(10, 5, 5, 5);
		this.addText();
		this.addImage();
	}
	
	/**
	 * Add the text that says "This is" and align it in the center
	 */
	public void addText() {
		// <br/> = line break
		String homeText = "<html>This is<br/> </html>";
		// Center-alignment for the text
		JLabel homeTextLabel = new JLabel("<html><div style='text-align: center;'>" + homeText + "</div></html>");
		homeTextLabel.setFont(JeopardyGUI.defaultFont);
		homeTextLabel.setForeground(JeopardyGUI.bl);
		// Arrange the text / JLabel on the HomePanel
		c.gridx = 0;
		c.gridy = 0;
		this.add(homeTextLabel, c);
	}
	
	/**
	 * Add the image that says "Jeopardy!"
	 */
	public void addImage() {
		ImageIcon image = new ImageIcon("jeopardy.png");
		// Align the image in the center of its containing JLabel without any text.
		JLabel jeopardyImage = new JLabel("", image, JLabel.CENTER);
		// Arrange the image / JLabel on the HomePanel
		c.gridx = 0;
		c.gridy = 2;
		this.add(jeopardyImage, c);
	}
	
	/**
	 * Add a button to take the user from the current HomePanel object
	 * to an Instructions panel.
	 * @param instructions - an InstructionsPanel object, i.e. a JPanel
	 * @param text - the text for the button to display
	 * @param x - the c.gridx value for the button
	 * @param y - the c.gridy value for the button
	 * @param col - the color for the button
	 */
	public void addGameButton(InstructionsPanel instructions, String text, int x, int y, Color col) {
		// Add a button on the home panel to get to the Instructions panel
	    JButton instructionsButton = new JButton(text);
	    instructionsButton.setPreferredSize(homeButtonSize);
	    instructionsButton.setFont(JeopardyGUI.defaultFont2);
	    instructionsButton.setForeground(JeopardyGUI.wh);
	    instructionsButton.setBackground(col);
	    // Arrange the button on the HomePanel
	    c.fill = GridBagConstraints.BOTH; // fill horizontal and vertical space
	    c.gridx = x;
	    c.gridy = y;
	    this.add(instructionsButton, c);
	    
	    // Have the button make the HomePanel invisible and
	    // the given InstructionsPanel visible.
	    instructionsButton.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		setVisible(false);
	    		instructions.setVisible(true);
	    	}
	    });
	}
}
