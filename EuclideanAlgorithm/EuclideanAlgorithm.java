package edu.smith.euclid;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;

/**
 * This class contains a recursive and an iterative method for calculating
 * the gcd of two integers, using the Euclidean Algorithm.
 * It runs and times those methods; results are graphed in Python.
 * @author sivan
 *
 */
public class EuclideanAlgorithm {
	/**
	 * Calculates the greatest common divisor of a and b recursively.
	 * Note that the gcd of a negative number m and another number n is the same as gcd(-m, n).
	 * @param a - a positive integer
	 * @param b - a positive integer
	 * @return gcd(a, b)
	 */
	public static int gcdRecursive(int a, int b) {
		if (a < b) {
			return gcdRecursive(b, a);
		}
		int mod = a%b;
		if (mod == 0 || b == 1) {
			return b;
		}
		else {
			return gcdRecursive(b, mod);
		}
	}
	
	/**
	 * Calculates the greatest common divisor of a and b iteratively.
	 * Note that the gcd of a negative number m and another number n is the same as gcd(-m, n).
	 * @param a - a positive integer
	 * @param b - a positive integer
	 * @return gcd(a, b)
	 */
	public static int gcdIterative(int a, int b) {
		if (a < b) {
			int temp = a;
			a = b;
			b = temp;
		}
		int mod;
		while(b > 1) {
			mod = a%b;
			a = b;
			b = mod;
		}
		if (b == 1) {
			return b;
		}
		// b == 0
		else {
			return a;
		}
	}
	
	public static void main(String[] args) {
		// Generate 200 random integers in [1, 1000]
		Random random = new Random();
		int randRange = 1000;
		int numTrials = 100;
		int numInts = numTrials * 2;
		int[] numbers = new int[numInts];
		for (int i = 0; i < numInts; i++) {
			numbers[i] = random.nextInt(randRange) + 1;
		}
		
		// Test 1: Different while loops
		// Track how long the recursive method vs. the iterative method takes
		double[] timesRecursive1 = new double[numTrials];
		double[] timesIterative1 = new double[numTrials];
		
		for (int i = 0; i < numInts; i = i+2) {
			long start = System.nanoTime();
			gcdRecursive(numbers[i], numbers[i+1]);
			long end = System.nanoTime();
			// Time is in seconds
			double time = (end - start) / 1e9;
			timesRecursive1[i/2] = time;
		}
		for (int i = 0; i < numInts; i = i+2) {
			long start = System.nanoTime();
			gcdIterative(numbers[i], numbers[i+1]);
			long end = System.nanoTime();
			// Time is in seconds
			double time = (end - start) / 1e9;
			timesIterative1[i/2] = time;
		}
		
		// Test 2: Same while loop
		// Track how long the recursive method vs. the iterative method takes
		double[] timesRecursive2 = new double[numTrials];
		double[] timesIterative2 = new double[numTrials];
				
		for (int i = 0; i < numInts; i = i+2) {
			long start = System.nanoTime();
			gcdRecursive(numbers[i], numbers[i+1]);
			long end = System.nanoTime();
			// Time is in seconds
			double time = (end - start) / 1e9;
			timesRecursive2[i/2] = time;
			
			start = System.nanoTime();
			gcdIterative(numbers[i], numbers[i+1]);
			end = System.nanoTime();
			// Time is in seconds
			time = (end - start) / 1e9;
			timesIterative2[i/2] = time;
		}
					
		// Write the generated data to a file, to be graphed in Python
		// Referenced https://stackoverflow.com/questions/30073980/java-writing-strings-to-a-csv-file
		try (PrintWriter br = new PrintWriter(new File("myfile.csv"))){
			StringBuilder sb = new StringBuilder();
	
			// Append the times from each array
			for (double element : timesRecursive1) {
				sb.append(element);
			 	sb.append(",");
			}
			sb.append("\n");
			for (double element : timesIterative1) {
				sb.append(element);
				sb.append(",");
			}
			sb.append("\n");
			for (double element : timesRecursive2) {
				sb.append(element);
				sb.append(",");
			}
			sb.append("\n");
			for (double element : timesIterative2) {
				sb.append(element);
				sb.append(",");
			}
			sb.append("\n");
			// Write the random numbers generated to the file
			// so they can be used in Python versions of the methods for testing
			for (int number : numbers) {
				sb.append(number);
				sb.append(",");
			}
			br.write(sb.toString());
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
}
