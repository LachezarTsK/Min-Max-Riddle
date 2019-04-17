import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Stack;
import java.util.StringTokenizer;

public class Solution {
	/**
	 * "Stack<Operation> undo" stores operations "append" and "delete" in the order
	 * of their occurrence so that they could be undone at a later stage.
	 * 
	 * If the operation is "append", the object "Operation" stores the length of the
	 * appended string.
	 * 
	 * If the operation is "delete", the object "Operation" stores the actual
	 * appended string.
	 */
	private static Stack<Operation> undo;
	private static StringBuilder currentString;

	/**
	 * Instead of Scanner, BufferedReader and BufferedWriter are applied for faster
	 * input and output.
	 */
	public static void main(String[] args) throws IOException {

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
		BufferedWriter bufferedWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(java.io.FileDescriptor.out), "ASCII"), 512);
		int numberOfOperations = Integer.parseInt(stringTokenizer.nextToken());
		currentString = new StringBuilder();
		undo = new Stack<Operation>();

		for (int i = 0; i < numberOfOperations; i++) {
			stringTokenizer = new StringTokenizer(bufferedReader.readLine());
			switch (Integer.parseInt(stringTokenizer.nextToken())) {
			case 1:
				append(stringTokenizer.nextToken());
				break;
			case 2:
				delete(Integer.parseInt(stringTokenizer.nextToken()));
				break;
			case 3:
				print(Integer.parseInt(stringTokenizer.nextToken()), bufferedWriter);
				break;
			case 4:
				undo();
				break;
			default:
				break;
			}
		}
		bufferedReader.close();
		bufferedWriter.flush();
		bufferedWriter.close();
	}

	private static void append(String stringToAppend) {
		Operation op = new Operation();
		op.codeOfOperation = 1;
		op.lengthOfAppendedString = stringToAppend.length();
		undo.push(op);
		currentString.append(stringToAppend);
	}

	private static void delete(int numberOfChar) {
		Operation op = new Operation();
		op.codeOfOperation = 2;
		int fromIndex = currentString.length() - numberOfChar;
		int toIndex = currentString.length();
		op.deletedString = currentString.substring(fromIndex);
		undo.push(op);
		currentString.delete(fromIndex, toIndex);
	}

	private static void print(int positionOfChar, BufferedWriter bufferedWriter) throws IOException {
		bufferedWriter.write(currentString.charAt(positionOfChar - 1));
		bufferedWriter.newLine();
	}

	private static void undo() {
		Operation op = undo.pop();

		if (op.codeOfOperation == 1) {
			int fromIndex = currentString.length() - op.lengthOfAppendedString;
			int toIndex = currentString.length();
			currentString.delete(fromIndex, toIndex);
		} else if (op.codeOfOperation == 2) {
			currentString.append(op.deletedString);
		}
	}
}

class Operation {
	int codeOfOperation;
	String deletedString;
	int lengthOfAppendedString;
}
