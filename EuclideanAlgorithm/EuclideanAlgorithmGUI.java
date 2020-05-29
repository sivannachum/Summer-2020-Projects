package edu.smith.euclid;
// Referenced https://stackoverflow.com/questions/5118701/how-to-create-a-gui-in-java
// Referenced https://www.youtube.com/watch?v=-sOqzUs1Hqk (and subsequent corresponding videos)
// I read a stack overflow (https://stackoverflow.com/questions/9554636/the-use-of-multiple-jframes-good-or-bad-practice?lq=1)
// about how it's better to use JPanels over JFrames, so I did so here.

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.Font;
// Colors! https://teaching.csse.uwa.edu.au/units/CITS1001/colorinfo.html
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
/**
 * This class creates a GUI for a user to get the gcd of two integers.
 * It offers a way of just showing the answer (the gcd) and a way of showing
 * the steps of the Euclidean Algorithm in calculating the answer.
 * @author sivan
 *
 */
public class EuclideanAlgorithmGUI{
	// Determine the size and location of the JFrame
	private static final int xSize = 800;
	private static final int ySize = 320;
	private static final int xLoc = 300;
	private static final int yLoc = 200;
	// Make the GridBagConstraints of the quick and hw panels global
	// because they will be used over several methods.
	private static final GridBagConstraints quickConstraints = new GridBagConstraints();
	private static final GridBagConstraints hwConstraints = new GridBagConstraints();
	
	/**
	 * Create all the frames and panels necessary for the app and make them visible.
	 */
	public static void homeScreen() {
		// Give a title to this frame.
		JFrame homescreen = new JFrame("Euclidean Algorithm/GCD Helper");
		
		// Allow the frame to toggle/flow between panels:
		// https://stackoverflow.com/questions/5600051/java-swing-how-to-toggle-panels-visibility
		homescreen.setLayout(new FlowLayout());
		homescreen.setSize(xSize, ySize);
		homescreen.setLocation(xLoc,yLoc);
		// Stop running the code when the user exes out of the frame!
		homescreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Create the home panel
		// Use GridBagLayout to control the layout of elements of the panel
		JPanel homepanel = new JPanel(new GridBagLayout());
		// Add the panel to the homescreen
		homescreen.add(homepanel);
		// Initialize the GridBagConstraints
		GridBagConstraints c = new GridBagConstraints();
		
		// Create the quick and hw panels and add them to the homescreen
		JPanel quick = quickAnswer();
		homescreen.add(quick);
		
		JPanel hw = homeworkHelp();
		homescreen.add(hw);
		
		// Create an action for buttons to go back to the home panel
		ActionListener backAction = new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				// Hide the quick/hw panels and make the home panel visible again
				quick.setVisible(false);
			    hw.setVisible(false);
			    homepanel.setVisible(true);
			}
		};
		
		// Two panels cannot share a button, but their buttons can share
		// functionality, i.e. ActionListeners.
		// Back button is used to get back to the home panel from a different panel.
		final JButton backFromQuick = new JButton("Back to Home Screen");
		// How to change a button's color: https://stackoverflow.com/questions/4172940/how-to-set-background-color-of-a-button-in-java-gui
		backFromQuick.setBackground(new Color(200, 0, 130));
		backFromQuick.addActionListener(backAction);
		// Arrange the back button on the quick panel
		quickConstraints.gridx = 2;
	    quickConstraints.gridy = 4;
	    quickConstraints.fill = GridBagConstraints.NONE;
	    quick.add(backFromQuick, quickConstraints);
	    
	    final JButton backFromHW = new JButton("Back to Home Screen");
	    backFromHW.setBackground(new Color(200, 0, 130));
	    backFromHW.addActionListener(backAction);
	    // Arrange the back button on the hw panel
	    hwConstraints.gridx = 2;
	    hwConstraints.gridy = 4;
	    hwConstraints.fill = GridBagConstraints.NONE;
	    hw.add(backFromHW, hwConstraints);
		

		
		// About JPanel text / JLabel https://stackoverflow.com/questions/31388790/jframe-text-size
		// HTML: https://stackoverflow.com/questions/7447691/is-there-any-multiline-jlabel-exist
	    // <br/> = line break
	    String homeText = "<html>Welcome to the Euclidean Algorithm/GCD Helper! <br/> "
	    					+ "What are you looking for today?<br/> </html>";
		// Center-alignment for the text
	    final JLabel homeTextLabel = new JLabel("<html><div style='text-align: center;'>" + homeText + "</div></html>");
	    // What fonts does Java have? https://alvinalexander.com/blog/post/jfc-swing/swing-faq-list-fonts-current-platform/
		// Change the font and font size
	    homeTextLabel.setFont(new Font("Curlz MT", Font.PLAIN,35));
	    // Arrange the text / JLabel on the home panel
		c.gridx = 1;
	    c.gridy = 0;
		homepanel.add(homeTextLabel, c);
	    
		// Add a button on the home panel to get to the quick panel
	    final JButton answer = new JButton("A Quick Answer");
	    answer.setBackground(Color.PINK);
	    // Arrange the button on the home panel
	    c.fill = GridBagConstraints.BOTH; // fill horizontal and vertical space
	    c.ipady = 40;      //make this component tall
	    c.weightx = 0.0;
	    c.gridwidth = 2;
	    c.gridx = 0;
	    c.gridy = 1;
	    homepanel.add(answer, c);
	    // Have the button make the home panel invisible and
	    // the quick panel visible.
	    answer.addActionListener(new ActionListener() {
	
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		homepanel.setVisible(false);
	    		quick.setVisible(true);
	    	}
	    });
	    
	    // Do the same for a button from the home panel to the hw panel
	    final JButton steps = new JButton("Homework Help");
	    steps.setBackground(Color.MAGENTA);
	    // Make the button's display area on the home panel be relative to 
	    // that of the previous button.
	    c.gridx = GridBagConstraints.RELATIVE;
	    c.gridy = 5;
	    homepanel.add(steps, c);
	    steps.addActionListener(new ActionListener() {
	
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		homepanel.setVisible(false);
	    		hw.setVisible(true);
	    	}
	    });
	    
	    // When the homescreen first begins we just want to see the home panel
	    // (and the homescreen JFrame itself!)
	    quick.setVisible(false);
	    hw.setVisible(false);
	    homepanel.setVisible(true);
	    homescreen.setVisible(true);
	}
	
	/***
	 * Creates a JPanel that can take user input and outputs the gcd of that input.
	 * Adds text, input and output textboxes, a Clear Output button and an Enter button.
	 * Back to Homescreen button is added in the homeScreen() method.
	 * @return the aformentioned JPanel
	 */
	public static JPanel quickAnswer() {
		JPanel quick = new JPanel(new GridBagLayout());
		String quickText = "<html>Input the numbers of which you would like to calculate the gcd:</html>";
		final JLabel quickTextLabel = new JLabel("<html><div style='text-align: center;'>" + quickText + "</div></html>");
		// Change the font and font size
		quickTextLabel.setFont(new Font("Curlz MT", Font.PLAIN,21));
		// Add the text to the panel in a certain place
		quickConstraints.gridx = 0;
		quickConstraints.gridy = 0;
		quickConstraints.gridwidth = 3;
		quick.add(quickTextLabel, quickConstraints);
	    
		// Create text areas for input and add them to the panel
		// Getting text input + setting text areas un/editable: https://stackoverflow.com/questions/16390503/java-swing-getting-input-from-a-jtextfield
		// Wrapping text: https://stackoverflow.com/questions/1691161/how-do-i-wrap-long-lines-of-text-in-a-java-textbox
		final JTextArea firstInput = new JTextArea(1, 10);
		firstInput.setEditable(true);
		firstInput.setLineWrap(true);
		final JTextArea secondInput = new JTextArea(1, 10);
		secondInput.setEditable(true);
		secondInput.setLineWrap(true);
		
		quickConstraints.gridx = 0;
	    quickConstraints.gridy = 2;
	    quickConstraints.gridwidth = 1;
		quick.add(firstInput, quickConstraints);
		quickConstraints.gridx = 1;
		quickConstraints.gridy = 2;
		quick.add(secondInput, quickConstraints);
		
		// Make the cursor go from firstInput to secondInput by using the tab key
	    // https://kodejava.org/how-do-i-move-focus-from-jtextarea-using-tab-key/
	    firstInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_TAB) {
                	// Consume the tab
                    e.consume();
                	// Go to the secondInput text box
                    firstInput.transferFocus();
                }
            }
        });
		
		// Create an enter button for the input and add it to the panel
		final JButton enter = new JButton("Enter");
		enter.setBackground(Color.PINK);
		quickConstraints.gridx = 2;
		quickConstraints.gridy = 2;
		quickConstraints.gridwidth = 2;
		quickConstraints.ipady = 10; 
		quickConstraints.fill = GridBagConstraints.BOTH;
	    quick.add(enter, quickConstraints);
	    
	    // Add an output area to output the gcd of the input
	    final JTextArea quickOutput = new JTextArea(10, 40);
	    // Scroll functionality when output expands beyond given space
	    // https://stackoverflow.com/questions/29924478/how-can-i-make-my-panel-wrap-text-and-increase-in-height-instead-of-width-when-c
	    final JScrollPane scrollQuickOutput = new JScrollPane(quickOutput, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
	    		JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    // Do not let the user edit the output area
	    quickOutput.setEditable(false);
	    quickConstraints.gridx = 0;
	    quickConstraints.gridy = 3;
	    quick.add(scrollQuickOutput, quickConstraints);
	    
	    // Add the enter button's functionality
	    enter.addActionListener(new ActionListener() {
	
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		// Get rid of excess whitespace on inputs
	    		String input1 = firstInput.getText().trim();
	    		String input2 = secondInput.getText().trim();
	    		quickEnterFunctionality(input1, input2, quickOutput);
	    		// Clear the input text areas
	    		firstInput.setText(null);
	    		secondInput.setText(null);
	    	}
	    });
	    
	    // Make secondInput have the same functionality as the enter button
	    // if someone hits the Enter or Tab key there.
	    secondInput.addKeyListener(new KeyAdapter() {
	    	@Override
	    	public void keyPressed(KeyEvent e) {
	    		if (e.getKeyCode() == KeyEvent.VK_ENTER 
	    				|| e.getKeyCode() == KeyEvent.VK_TAB) {
		    		// Get rid of excess whitespace on inputs
		    		String input1 = firstInput.getText().trim();
		    		String input2 = secondInput.getText().trim();
		    		quickEnterFunctionality(input1, input2, quickOutput);
		    		// Consume the enter/tab
		    		e.consume();
		    		// Clear the input text areas
		    		firstInput.setText(null);
		    		secondInput.setText(null);
		    		// Go back to the firstInput text box
		    		secondInput.transferFocusBackward();
	    		}
	    	}
	    });
	    
	    // Create clear button for the output and add it to the panel
	    final JButton clear = new JButton("Clear Output Text");
	    clear.setBackground(new Color(180, 0, 200));
	    quickConstraints.gridx = 2;
		quickConstraints.gridy = 3;
		quickConstraints.fill = GridBagConstraints.HORIZONTAL;
	    quick.add(clear, quickConstraints);
	    clear.addActionListener(new ActionListener() {
	    	
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		// Clear the output text
	    		quickOutput.setText(null);
	    	}
	    });
	    
	    // Return the JPanel that has been created
	    return quick;
	}
	
	/***
	 * Creates a JPanel that can take user input and outputs the gcd of that input
	 * as well as the steps taken in the Euclidean Algorithm to calculate the gcd.
	 * Adds text, input and output textboxes, a Clear Output button and an Enter button.
	 * Back to Homescreen button is added in the homeScreen() method.
	 * @return the aformentioned JPanel
	 */
	public static JPanel homeworkHelp() {
		JPanel hw = new JPanel(new GridBagLayout());
		String hwText = "<html>Input the numbers of which you would like to calculate the gcd:</html>";
		final JLabel hwTextLabel = new JLabel("<html><div style='text-align: center;'>" + hwText + "</div></html>");
		// Change the font and font size
		hwTextLabel.setFont(new Font("Curlz MT", Font.PLAIN,21));
		hwConstraints.gridx = 0;
		hwConstraints.gridy = 0;
		hwConstraints.gridwidth = 3;
		hw.add(hwTextLabel, hwConstraints);
		hw.setSize(xSize, ySize);
		hw.setLocation(xLoc,yLoc);
	    
		// Create text areas for input and add them to the panel
		final JTextArea firstInput = new JTextArea(1, 10);
		firstInput.setEditable(true);
		firstInput.setLineWrap(true);
		final JTextArea secondInput = new JTextArea(1, 10);
		secondInput.setEditable(true);
		secondInput.setLineWrap(true);
		
		hwConstraints.gridx = 0;
	    hwConstraints.gridy = 2;
	    hwConstraints.gridwidth = 1;
	    hw.add(firstInput, hwConstraints);
		hwConstraints.gridx = 1;
		hwConstraints.gridy = 2;
		hw.add(secondInput, hwConstraints);
		
		// Make the cursor go from firstInput to secondInput by using the tab key
		firstInput.addKeyListener(new KeyAdapter() {
			@Override
	        public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_TAB) {
					// Consume the tab
	                e.consume();
					// Go to the secondInput text box
					firstInput.transferFocus();
	            }
	        }
	    });
		
		// Create an enter button for the input and add it to the panel
		final JButton enter = new JButton("Enter");
		enter.setBackground(Color.MAGENTA);
		hwConstraints.gridx = 2;
		hwConstraints.gridy = 2;
		hwConstraints.gridwidth = 2;
		hwConstraints.ipady = 10; 
		hwConstraints.fill = GridBagConstraints.BOTH;
	    hw.add(enter, hwConstraints);
	    
	    // Add an output area to output the gcd of the input
	    final JTextArea hwOutput = new JTextArea(10, 40);
	    // Scroll functionality when output expands beyond given space
	    final JScrollPane scrollHwOutput = new JScrollPane(hwOutput, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
	    		JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    // Do not let the user edit the output area
	    hwOutput.setEditable(false);
	    hwConstraints.gridx = 0;
	    hwConstraints.gridy = 3;
	    hw.add(scrollHwOutput, hwConstraints);
	    
	    // Add the enter button's functionality
	    enter.addActionListener(new ActionListener() {
	
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		// Get rid of excess whitespace on inputs
	    		String input1 = firstInput.getText().trim();
	    		String input2 = secondInput.getText().trim();
	    		hwEnterFunctionality(input1, input2, hwOutput);
	    		// Clear the input text areas
	    		firstInput.setText(null);
	    		secondInput.setText(null);
	    	}
	    });
	    
	    // Make secondInput have the same functionality as the enter button
	    // if someone hits the Enter or Tab key there
	    secondInput.addKeyListener(new KeyAdapter() {
	    	@Override
	    	public void keyPressed(KeyEvent e) {
	    		if (e.getKeyCode() == KeyEvent.VK_ENTER 
	    				|| e.getKeyCode() == KeyEvent.VK_TAB) {
	    			String input1 = firstInput.getText().trim();
	    			String input2 = secondInput.getText().trim();
		    		hwEnterFunctionality(input1, input2, hwOutput);
		    		// Consume the enter/tab
		    		e.consume();
		    		// Clear the input text areas
		    		firstInput.setText(null);
		    		secondInput.setText(null);
		    		// Go back to the firstInput text box
		    		secondInput.transferFocusBackward();
	    		}
	    	}
	    });
	    
	    // Create clear button for the output and add it to the panel
	    final JButton clear = new JButton("Clear Output Text");
	    clear.setBackground(new Color(180, 0, 200));
	    hwConstraints.gridx = 2;
		hwConstraints.gridy = 3;
		hwConstraints.fill = GridBagConstraints.HORIZONTAL;
	    hw.add(clear, hwConstraints);
	    clear.addActionListener(new ActionListener() {
	    	
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		hwOutput.setText(null);
	    	}
	    });
	    
	    // Return the JPanel that has been created
	    return hw;
	}
	
	/**
	 * Assure input1 and input2 are integers, 
	 * calculate and output their gcd on the quick JPanel.
	 * Otherwise output an error.
	 * @param quickOutput - the text area for output
	 */
	public static void quickEnterFunctionality(String input1, String input2, JTextArea quickOutput) {
		int a = 0;
		int b = 0;
		// Try to convert the input strings to numbers
		// Catch conversion error and output error message
		// Try-catch block for String to Integer conversion:
		// https://stackoverflow.com/questions/5585779/how-do-i-convert-a-string-to-an-int-in-java
		try {
			a = Integer.parseInt(input1);
			b = Integer.parseInt(input2);
			// If both inputs are zero there is no gcd
			if (a == 0 && b == 0) {
				quickOutput.append("0 and 0 have no gcd, as they both divide everything.\n");
			}
			// If only one input is zero, the other is the gcd
			else if (a == 0) {
				String output = String.format("The gcd of 0 and %d is %d, since 0 divides everything.\n", b, Math.abs(b));
				quickOutput.append(output);
			}
			else if (b == 0) {
				String output = String.format("The gcd of %d and 0 is %d, since 0 divides everything.\n", a, Math.abs(a));
				quickOutput.append(output);
			}
			// Otherwise (neither input is 0), calculate and output the gcd
			else {
				String output = String.format("The gcd of %d and %d is %d.\n", a, b, gcdQuick(a, b));
				quickOutput.append(output);
			}
		} 
		// If the conversion from String to Integer failed,
		// either the user input non-integer text or they put in a number that is too big.
		catch (NumberFormatException error) {
			quickOutput.append("I cannot perform calculations on text, only numbers!\n");
			quickOutput.append("I cannot perform calculations on numbers with absolute value greater than\n2147483647.\n");
		}
	}
	
	/**
	 * Assure input1 and input2 are integers, 
	 * calculate and output their gcd + the steps taken to calculate it
	 * on the hw JPanel.
	 * Otherwise output an error.
	 * @param hwOutput - the text area for output
	 */
	public static void hwEnterFunctionality(String input1, String input2, JTextArea hwOutput) {
		int a = 0;
		int b = 0;
		// Try to convert the input strings to numbers
		// Catch conversion error and output error message
		try {
			a = Integer.parseInt(input1);
			b = Integer.parseInt(input2);
			// If both inputs are zero there is no gcd
			if (a == 0 && b == 0) {
				hwOutput.append("0 and 0 have no gcd, as they both divide everything.\n");
			}
			// If only one input is zero, the other is the gcd
			else if (a == 0) {
				String output = String.format("The gcd of 0 and %d is %d, since 0 divides everything.\n", b, Math.abs(b));
				hwOutput.append(output);
			}
			else if (b == 0) {
				String output = String.format("The gcd of %d and 0 is %d, since 0 divides everything.\n", a, Math.abs(a));
				hwOutput.append(output);
			}
			// Otherwise (neither input is 0), calculate and output the gcd
			// as well as the steps taken in the Euclidean Algorithm to calculate it
			else {
				hwOutput.append(gcdSteps(a, b));
			}
		}
		// If the conversion from String to Integer failed,
		// either the user input non-integer text or they put in a number that is too big.
		catch (NumberFormatException error) {
			hwOutput.append("I cannot perform calculations on text, only numbers!\n");
			hwOutput.append("I cannot perform calculations on numbers with absolute value greater than\n2147483647.\n");
		}
    }
	
	/**
	 * Calculates the greatest common divisor of a and b (recursively).
	 * @param a - an integer
	 * @param b - an integer
	 * @return gcd(a, b)
	 */
	public static int gcdQuick(int a, int b) {
		// Make negative numbers positive,
		// the gcd will be the same
		if (a < 0 || b < 0) {
			a = Math.abs(a);
			b = Math.abs(b);
		}
		// Put the bigger number as the first number
		if (a < b) {
			return gcdQuick(b, a);
		}
		int mod = a%b;
		if (mod == 0 || b == 1) {
			return b;
		}
		else {
			return gcdQuick(b, mod);
		}
	}
	
	/**
	 * Calculates the greatest common divisor of a and b (iteratively).
	 * @param a - an integer
	 * @param b - an integer
	 * @return a string representing the steps taken in calculating gcd(a, b)
	 */
	public static String gcdSteps(int a, int b) {
		String toReturn = String.format("The gcd of %d and %d", a, b);
		// Keep track of if we are still on the first line of gcd calculation or not
		boolean firstLine = true;
		// Make negative numbers positive,
		// the gcd will be the same
		if (a < 0 || b < 0) {
			a = Math.abs(a);
			b = Math.abs(b);
			// Record this step
			toReturn = toReturn + String.format(" = gcd(%d, %d)\n", a, b);
			firstLine = false;
		}
		// Put the bigger number as the first number
		if (a < b) {
			int temp = a;
			a = b;
			b = temp;
			// Record this step
			if (firstLine) {
				toReturn = toReturn + String.format(" = gcd(%d, %d)\n", a, b);
				firstLine = false;
			}
			else {
				toReturn = toReturn + String.format("\t = gcd(%d, %d)\n", a, b);
				firstLine = false;
			}
		}
		int mod;
		// Proceed to calculate the gcd, all the while recording the steps taken to get there
		while(b > 1) {
			mod = a%b;
			a = b;
			b = mod;
			if (firstLine) {
				toReturn = toReturn + String.format(" = gcd(%d, %d)\n", a, b);
				firstLine = false;
			}
			else {
				toReturn = toReturn + String.format("\t = gcd(%d, %d)\n", a, b);
				firstLine = false;
			}
		}
		// b is the gcd
		if (b == 1) {
			toReturn = toReturn + String.format("\t = %d\n", b);
		}
		// b == 0, a is the gcd
		else {
			// a is gcd
			toReturn = toReturn + String.format("\t = %d.\n", a);
		}
		return toReturn;
	}
	
	/**
	 * Run the GUI. Note that all of the code in homeScreen()
	 * could have just been written here.
	 */
	public static void main(String[] args) {
		homeScreen();	
	}
}