package application;

import java.util.Scanner;

public class ConsoleIn {

	private Scanner scan;
	
	/** Default constructor
	 * instantiates scanner associated with the console.	 *
	 */
	public ConsoleIn() {
		scan = new Scanner(System.in);
	}
	
	/** Method readInt: <br />
	 * Scans for proper int input from the console.
	 * @param prompt
	 * @return int
	 */
	public int readInt(String prompt) {
		System.out.print(prompt + " > ");
		
		while ( ! scan.hasNextInt() ) {
			String garbage = scan.nextLine();
			System.out.println("Input \"" + garbage + "\" is not an integer.");
			System.out.print(prompt + " > ");
		}
		
		return scan.nextInt();
	}
	
	/** Method readDouble: <br />
	 * Scans for proper double input from the console.
	 * @param prompt
	 * @return double
	 */
	public double readDouble(String prompt) {
		System.out.print(prompt + " > ");
		
		while ( ! scan.hasNextDouble() ) {
			String garbage = scan.nextLine();
			System.out.println("Input \"" + garbage + "\" is not a double.");
			System.out.print(prompt + " > ");
		}
		
		return scan.nextDouble();
	}

	/** Method readFloat: <br />
	 * Scans for proper float input from the console
	 * @param prompt
	 * @return float
	 */
	public float readFloat(String prompt) {
		System.out.print(prompt + " > ");
		
		while ( ! scan.hasNextFloat() ) {
			String garbage = scan.nextLine();
			System.out.println("Input \"" + garbage + "\" is not a float.");
			System.out.print(prompt + " > ");
		}
		
		return scan.nextFloat();
	}
	
	/** Method readBoolean: <br />
	 * Scans for proper boolean input from the console
	 * @param prompt
	 * @return boolean
	 */
	public boolean readBoolean(String prompt) {
		System.out.print(prompt + " > ");
		
		while ( ! scan.hasNextBoolean() ) {
			String garbage = scan.nextLine();
			System.out.println("Input \"" + garbage + "\" is not a boolean.");
			System.out.print(prompt + " > ");
		}
		
		return scan.nextBoolean();
	}
	
	/** Scans for proper short input from the console
	 * 
	 * @param prompt
	 * @return short
	 */
	public short readShort(String prompt) {
		System.out.print(prompt + " > ");
		
		while ( ! scan.hasNextDouble() ) {
			String garbage = scan.nextLine();
			System.out.println("Input \"" + garbage + "\" is not a short.");
			System.out.print(prompt + " > ");
		}
		
		return scan.nextShort();
	}
	
	/** Method readLong: <br />
	 * Scans for proper long input from the console
	 * @param prompt
	 * @return long
	 */
	public double readLong(String prompt) {
		System.out.print(prompt + " > ");
		
		while ( ! scan.hasNextLong() ) {
			String garbage = scan.nextLine();
			System.out.println("Input \"" + garbage + "\" is not a long.");
			System.out.print(prompt + " > ");
		}
		
		return scan.nextLong();
	}
	
	/** Method readByte: <br />
	 * Scans for proper byte input from the console
	 * @param prompt
	 * @return byte
	 */
	public double readByte(String prompt) {
		System.out.print(prompt + " > ");
		
		while ( ! scan.hasNextByte() ) {
			String garbage = scan.nextLine();
			System.out.println("Input \"" + garbage + "\" is not a byte.");
			System.out.print(prompt + " > ");
		}
		
		return scan.nextByte();
	}
	
	/** Method readChar: <br />
	 * Scans for a single char input from the console
	 * @param prompt
	 * @return byte
	 */
	public char readChar(String prompt) {
		System.out.print(prompt + " > ");
		return scan.next().charAt(0);
	}
	
	/** Method readString: <br />
	 * Scans for a String input from the console
	 * @param prompt
	 * @return String
	 */
	public String readString(String prompt) {
		System.out.print(prompt + " > ");
		return scan.nextLine();
	}
	
}