import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Stack;
import java.util.StringTokenizer;

public class Solution {
	/**
	 * Stores initial input data.
	 */
	private static int[] input;

	/**
	 * BufferedReader and BufferedWriter for faster input and output.
	 */
	public static void main(String[] args) throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		BufferedWriter out = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(java.io.FileDescriptor.out), "ASCII"), 512);

		int size = Integer.parseInt(st.nextToken());
		input = new int[size];

		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < size; i++) {
			input[i] = Integer.parseInt(st.nextToken());
		}

		int[] minMax = findMaxMinForLargestWindow(findMinSearchingBackward(), findMinSearchingForward());
		printResults(minMax, out);

		br.close();
		out.flush();
		out.close();
	}

	/**
	 * Finding the maximum backward window for which an element is minimum.
	 * 
	 * Thus the method finds the first half of the potential maximum window for
	 * which an element is minimum.
	 * 
	 * Applying stack to keep track of indexes reduces the time complexity to O(n).
	 */
	private static int[] findMinSearchingBackward() {
		int[] minBackward = new int[input.length];
		Stack<Integer> indexes = new Stack<Integer>();
		indexes.push(0);
		minBackward[0] = 1;

		for (int i = 1; i < input.length; i++) {
			while (!indexes.isEmpty() && input[indexes.peek()] >= input[i]) {
				indexes.pop();
			}
			int firstHalfWindow = 0;
			if (indexes.isEmpty()) {
				firstHalfWindow = i + 1;
			} else {
				firstHalfWindow = i - indexes.peek();
			}
			minBackward[i] = firstHalfWindow;
			indexes.push(i);
		}
		return minBackward;
	}

	/**
	 * Finding the maximum forward window for which an element is minimum.
	 * 
	 * Thus the method finds the second half of the potential maximum window for
	 * which an element is minimum.
	 * 
	 * Applying stack to keep track of indexes reduces the time complexity to O(n).
	 */
	private static int[] findMinSearchingForward() {
		int[] minForward = new int[input.length];
		Stack<Integer> indexes = new Stack<Integer>();
		indexes.push(input.length - 1);
		minForward[input.length - 1] = 0;

		for (int i = input.length - 2; i >= 0; i--) {
			while (!indexes.isEmpty() && input[indexes.peek()] >= input[i]) {
				indexes.pop();
			}
			int secondHalfWindow = 0;
			if (indexes.isEmpty()) {
				secondHalfWindow = input.length - i - 1;
			} else {
				secondHalfWindow = indexes.peek() - i - 1;
			}
			minForward[i] = secondHalfWindow;
			indexes.push(i);
		}
		return minForward;
	}

	/**
	 * Combining the results for the first and second half of the potential maximum
	 * windows.
	 * 
	 * Thus the method finds the whole maximum window that surrounds an element for
	 * which this element is minimum.
	 */
	private static int[] findMaxMinForLargestWindow(int[] firstHalf, int[] secondHalf) {
		int[] minMax = new int[input.length];
		/**
		 * If there are two or more windows of equal size, take the element with the
		 * highest value as a minimum for this window.
		 */
		for (int i = 0; i < input.length; i++) {
			int window = firstHalf[i] + secondHalf[i];
			if (minMax[window - 1] < input[i]) {
				minMax[window - 1] = input[i];
			}
		}

		/**
		 * In the thus formed array 'minMax' that stores the highest value of all the
		 * possible minimums of each window size:
		 * 
		 * If a window with size 'n+1' has an element with value that is higher than the
		 * value of a window with size 'n' then the window with size 'n' takes the value
		 * of window with size 'n+1'.
		 */
		for (int i = input.length - 2; i >= 0; i--) {
			if ((i + 1) < input.length && minMax[i] < minMax[i + 1]) {
				minMax[i] = minMax[i + 1];
			}
		}
		return minMax;
	}

	/**
	 * Print results in ascending order of window size.
	 */
	private static void printResults(int[] minMax, BufferedWriter out) throws IOException {
		for (int i = 0; i < minMax.length; i++) {
			out.write(minMax[i] + " ");
		}
	}
}
