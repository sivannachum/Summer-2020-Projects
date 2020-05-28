import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Stream;
import java.util.List;
import java.awt.event.KeyAdapter;
import java.awt.Font;
// Colors! https://teaching.csse.uwa.edu.au/units/CITS1001/colorinfo.html
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.GridBagConstraints;
import java.awt.BorderLayout;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.JScrollPane;
import javax.swing.JLabel;

public class Jeopardy {
	// Determine the size and location of the JFrame
	private static final int xSize = 1000;
	private static final int ySize = 600;
	private static final int xLoc = 170;
	private static final int yLoc = 70;
	private static final int numQuestions = 20;
	private static final Color bl = new Color(0, 0, 190);
	private static final Color sil = Color.GRAY;
	private static final Color wh = Color.WHITE;
	private static final Font defaultFont = new Font("Helvetica", Font.PLAIN,40);
	private static final Font defaultFont2 = new Font("Helvetica", Font.PLAIN,30);
	private static final Font instructionsFont = new Font("Helvetica", Font.PLAIN,25);
	private static int score1 = 0;
	private static int score2 = 0;
	
	public static void main(String[] args) {
		JFrame game = new JFrame("Jeopardy Game");
		// Allow the game to toggle between panels
		game.setLayout(new FlowLayout());
		game.setSize(xSize, ySize);
		game.setLocation(xLoc,yLoc);
		// Stop running the code when the user exes out of the frame!
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Create the home panel
		// Use GridBagLayout to control the layout of elements of the panel
		JPanel homepanel = new JPanel(new GridBagLayout());
		// Add the panel to the game
		game.add(homepanel);
		// Initialize the GridBagConstraints
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10, 5, 5, 5);
		
		// <br/> = line break
		String homeText = "<html>This is<br/> </html>";
		// Center-alignment for the text
		final JLabel homeTextLabel = new JLabel("<html><div style='text-align: center;'>" + homeText + "</div></html>");
		homeTextLabel.setFont(defaultFont);
		homeTextLabel.setForeground(bl);
		// Arrange the text / JLabel on the home panel
		c.gridx = 0;
		c.gridy = 0;
		homepanel.add(homeTextLabel, c);
		
		ImageIcon image = new ImageIcon("jeopardy.png");
		JLabel jeopardyImage = new JLabel("", image, JLabel.CENTER);
		c.gridx = 0;
		c.gridy = 2;
		homepanel.add(jeopardyImage, c);
		
		JPanel oneP = onePlayerInstructions(game, homepanel);
		
		// Add a button on the home panel to get to the quick panel
	    final JButton onePlayer = new JButton("One-Player Game");
	    Dimension homeButtonSize = new Dimension(xSize*4/5, ySize/4);
	    onePlayer.setPreferredSize(homeButtonSize);
	    onePlayer.setFont(defaultFont2);
	    onePlayer.setForeground(wh);
	    onePlayer.setBackground(bl);
	    // Arrange the button on the home panel
	    c.fill = GridBagConstraints.BOTH; // fill horizontal and vertical space
	    c.gridx = 0;
	    c.gridy = 4;
	    homepanel.add(onePlayer, c);
	    // Have the button make the home panel invisible and
	    // the quick panel visible.
	    onePlayer.addActionListener(new ActionListener() {
	
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		homepanel.setVisible(false);
	    		oneP.setVisible(true);
	    	}
	    });
	    
	    // Do the same for a button from the home panel to the hw panel
	    final JButton twoPlayer = new JButton("Two-Player Game");
	    twoPlayer.setPreferredSize(homeButtonSize);
	    twoPlayer.setFont(defaultFont2);
	    twoPlayer.setForeground(wh);
	    twoPlayer.setBackground(sil);
	    // Make the button's display area on the home panel be relative to 
	    // that of the previous button.
	    c.gridx = 0;
	    c.gridy = 5;
	    homepanel.add(twoPlayer, c);
	    twoPlayer.addActionListener(new ActionListener() {
	
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		homepanel.setVisible(false);
	    		// Also set another panel true
	    	}
	    });
	    
	    oneP.setVisible(false);
		homepanel.setVisible(true);
		game.setVisible(true);
	}

	public static JPanel onePlayerInstructions(JFrame game, JPanel homepanel) {
		JPanel oneP = new JPanel(new GridBagLayout());
		game.add(oneP);
		// Initialize the GridBagConstraints
		GridBagConstraints cOneP = new GridBagConstraints();
		cOneP.insets = new Insets(10, 5, 5, 5);
		
		String oneInstructions = "<html>Each game has 20 questions. <br/>"
								+ "1. You will have 10 seconds to read each question. <br/> "
								+ "2. You will then have 10 seconds to respond to each question. <br/>"
								+ "3. Respond to a question by pressing the 'Enter' key. Then say the answer aloud.<br/>"
								+ "Use the spacebar to reveal the correct response. <br/> "
								+ "4. You will then be prompted to tell the computer if your response was correct. <br/>"
								+ "5. If you responded correctly, you will gain as much money as the question is worth. <br/> "
								+ "6. If you responded incorrectly, you will lose as much money as the question is worth. <br/> "
								+ "7. Use the 'x' key to proceed to the next page.<br/><br/>"
								+ "Please enter your name below.</html>";
		// Center-alignment for the text
		final JLabel oneInstructionsLabel = new JLabel("<html><div style='text-align: left;'>" 
													+ oneInstructions + "</div></html>");
		oneInstructionsLabel.setFont(instructionsFont);
		oneInstructionsLabel.setForeground(bl);
		cOneP.gridx = 0;
		cOneP.gridy = 0;
		cOneP.gridwidth = 3;
		cOneP.fill = GridBagConstraints.BOTH;
		oneP.add(oneInstructionsLabel, cOneP);		
		
		ActionListener backAction = new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				// Hide the game instructions panel and make the home panel visible again
				oneP.setVisible(false);
				homepanel.setVisible(true);
			}
		};
		
		// Back button is used to get back to the home panel from a different panel.
		final JButton backFromOne = new JButton("Back to Home Screen");
		backFromOne.setPreferredSize(new Dimension(xSize/4, ySize/10));
		backFromOne.setForeground(wh);
		backFromOne.setBackground(sil);
		backFromOne.addActionListener(backAction);
		// Arrange the back button on the one player instructions panel
		cOneP.gridx = 0;
		cOneP.gridy = 2;
		cOneP.gridwidth = 1;
		cOneP.fill = GridBagConstraints.NONE;
		oneP.add(backFromOne, cOneP);
		
		// Create text areas for input and add them to the panel
		final JTextArea nameInput = new JTextArea(1, 10);
		nameInput.setFont(defaultFont2);
		nameInput.setEditable(true);
		nameInput.setLineWrap(true);
		cOneP.gridx = 1;
		cOneP.gridy = 1;
		cOneP.fill = GridBagConstraints.BOTH;
		oneP.add(nameInput, cOneP);
		
		// Create an enter button for the input and add it to the panel
		final JButton enter = new JButton("Enter");
		enter.setPreferredSize(new Dimension(xSize/3, ySize/8));
		enter.setForeground(wh);
		enter.setBackground(bl);
		enter.setFont(defaultFont2);
		cOneP.gridx = 2;
		cOneP.gridy = 1;
		oneP.add(enter, cOneP);
		
		// Add the enter button's functionality
	    enter.addActionListener(new ActionListener() {
	
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		// Get rid of excess whitespace on inputs
	    		String input = nameInput.getText().trim();
	    		if (input.equals("")) {
	    			nameInput.setText("Please input name");
	    		}
	    		else {
	    			oneP.setVisible(false);
	    			onePEnterSetup(game, input);
	    		}
	    	}
	    });
		
		return oneP;
	}
	
	public static void onePEnterSetup(JFrame game, String input) {
		score1 = 0;
		Set<Integer> rows = new HashSet<>();
		
		String file = "jeopardy.txt";
	    String delimiter = "\\{\\+~\\}";
	    String line;
	    // String[] has length 7
	    List<String[]> lines = new ArrayList<String[]>();
	    int numRows = 0;
	    
	    try (BufferedReader br = new BufferedReader(new FileReader(new File(file)))) {
	    	while((line = br.readLine()) != null){
	            String[] values = line.split(delimiter);
	            lines.add(values);
	            numRows++;
	        }
	    } catch (Exception e){
	        System.out.println(e);
	    }
	    int i = 0;
		while (i < numQuestions) {
			Random rand = new Random();
			int row = rand.nextInt(numRows);
			if (!rows.contains(row)) {
				rows.add(row);
				i++;
			}
		}
		Iterator<Integer> rowsIterator = rows.iterator();
		
		JPanel[] gamescreens = new JPanel[20];
		for (i = 0; i < numQuestions; i++) {
			JPanel gamescreen = onePEnterFunctionality(game, lines.get(rowsIterator.next()), input);
			game.add(gamescreen);
			gamescreen.setVisible(false);
			// This code works
			gamescreen.setFocusable(true);
			gamescreens[i] = gamescreen;
		}
		
		// After they're all created, give each the functionality to go to the next
				// and the last the functionality to go to the end screen
		for (i = 0; i < numQuestions-1; i++) {
			addNextBinding(game, gamescreens[i], gamescreens[i+1]);
		}
		gamescreens[0].setVisible(true);
		gamescreens[0].grabFocus();
		
		// Container.remove(Component)
		
	}
	
	public static JPanel onePEnterFunctionality(JFrame game, String[] line, String input) {
		JPanel gamescreen = new JPanel();
		String showTitle = line[0];
		String showComments = line[1];
		String whichRound = line[2];
		String category = line[3];
		int amount = Integer.parseInt(line[4]);
		String question = line[5];
		String answer = line[6];
		
		// Make the JTextArea look more like a JLabel + text-wrapping, breaking lines on spaces:
		// https://stackoverflow.com/questions/7861724/is-there-a-word-wrap-property-for-jlabel
		final JTextArea questionText = new JTextArea(question);
		questionText.setEditable(false);
		questionText.setLineWrap(true);
		questionText.setWrapStyleWord(true);
		questionText.setPreferredSize(new Dimension(xSize*4/5, ySize*1/3));
		questionText.setOpaque(false);
		//questionText.
		questionText.setFocusable(false);
		//final JLabel questionLabel = new JLabel("<html><div style='text-align: center;'>" + question + "</div></html>");
		questionText.setFont(defaultFont);
		questionText.setForeground(bl);
		gamescreen.add(questionText);
		
		return gamescreen;
	}
	
	public static void addNextBinding(JFrame game, JPanel first, JPanel next) {
		class NextScreen extends AbstractAction {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
		    	game.remove(first);
		    	next.setVisible(true);
		    	next.grabFocus();
	    	}
		}
		NextScreen next1 = new NextScreen();
		first.getInputMap().put(KeyStroke.getKeyStroke('x'),"nextScreen");
		first.getActionMap().put("nextScreen",next1);
	}
	
}