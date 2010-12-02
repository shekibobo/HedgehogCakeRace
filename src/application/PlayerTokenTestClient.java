package application;

/** Class PlayerTokenTestClient: <br />
 * Generates six tokens from different players at different 
 * locationsand outputs their attributes.
 * @author Joshua Kovach
 * @version 1.0
 *
 */
public class PlayerTokenTestClient {
	public static void main(String [] args) {
		PlayerToken[] tokenList = new PlayerToken[6];
		
		for (int i = 0; i < tokenList.length; i++) {
			tokenList[i] = new PlayerToken(i, i, 0);
			System.out.println(tokenList[i].toString(0));
		}
	}
}
