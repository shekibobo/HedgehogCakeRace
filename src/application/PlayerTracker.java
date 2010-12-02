package application;

import java.awt.Color;
import java.util.ArrayList;

/** Class PlayerTracker: <br />
 * Contains an array of PlayerTokens for the specified number of players
 * with the specified number of tokens.  Provides methods for checking for
 * a winner, the number of players, number of tokens, and getting player 
 * identifiers.
 * @author Joshua Kovach
 * @version 1.0
 *
 */
public class PlayerTracker {
	private PlayerToken [][] playerArray;	//holds the tokens belonging to each player
	private int playerCount;				//number of players
	private int tokenCount;					//number of tokens
	private int winCount;					//number of tokens finished to win
	private int winner;						//playerID of the winner
	
	/** Indicates that there is no winner to the game */
	final public int NO_WINNER = -1;
	
	/** Constructor: <br />
	 * Creates a tracker for the specified number of players and tokens,
	 * watches for a win
	 * @param playerCount
	 * @param tokenCount
	 * @param winCount
	 */
	public PlayerTracker(int playerCount, int tokenCount, int winCount) {
		this.playerCount = playerCount;
		this.tokenCount = tokenCount;
		this.winCount = winCount;
		this.winner = NO_WINNER;
		
		this.playerArray = new PlayerToken[playerCount][tokenCount];
		
		for (int i = 0; i < playerCount; i++) {
			for (int j = 0; j < tokenCount; j++) {
				playerArray[i][j] = new PlayerToken(i);
			}
		}
	}
	
	/** Method getTokensForPlayer: <br />
	 * Returns the collection of tokens belonging to a given player.
	 * @param playerID unique player identifier
	 * @return PlayerToken[] array of tokens belonging to the player.
	 */
	public PlayerToken[] getTokensForPlayer(int playerID) {
		return playerArray[playerID];
	}
	
	/** Method getTokensThatCanWin: <br />
	 * Returns the tokens belonging to the player that are
	 * still able to win.  For use in "Black Holes" variations
	 * of the game.
	 * @param playerID
	 * @return array of the tokens that can still win.
	 */
	private PlayerToken[] getTokensThatCanWin(int playerID) {
		PlayerToken[] winnableTokens;
		ArrayList<PlayerToken> wTokens = new ArrayList<PlayerToken>();
		for (int i = 0; i < tokenCount; i++) {
			if (playerArray[playerID][i].isWinnable()) {
				wTokens.add(playerArray[playerID][i]);
			}
		}
		winnableTokens = new PlayerToken[wTokens.size()];
		wTokens.toArray(winnableTokens);
		return winnableTokens;
	}
	
	/** Method getPlayersThatCanWin: <br />
	 * Returns an array of playerIDs of players that can still win.
	 * @return array of PlayerIDs
	 */
	public Integer[] getPlayersThatCanWin() {
		Integer [] playersThatCanWin;
		ArrayList<Integer> pWinnable = new ArrayList<Integer>();
		for (int i = 0; i < playerCount; i++) {
			if ( ! (getTokensThatCanWin(i).length < winCount) ) {
				pWinnable.add(i);
			}
		}
		playersThatCanWin = new Integer[pWinnable.size()];
		pWinnable.toArray(playersThatCanWin);
		return playersThatCanWin;
	}
	
	/** Method getPlayersThatCannotWin: <br />
	 * 
	 * @return array of players that are unable to win the game
	 */
	public Integer[] getPlayersThatCannotWin() {
		Integer [] playersThatCannotWin;
		ArrayList<Integer> pUnwinnable = new ArrayList<Integer>();
		for (int i = 0; i < playerCount; i++) {
			if (getTokensThatCanWin(i).length < winCount) {
				pUnwinnable.add(i);
			}
		}
		playersThatCannotWin = new Integer[pUnwinnable.size()];
		pUnwinnable.toArray(playersThatCannotWin);
		return playersThatCannotWin;
	}
	
	/** Method getNumberOfPlayers(): <br />
	 * @return int number of players
	 */
	public int getPlayerCount() {
		return playerCount;
	}
	
	/** Method getNumberOfTokens(): <br />
	 * @return int number of tokens per player
	 */
	public int getTokenCount() {
		return tokenCount;
	}
	
	/** Method getWinAmount(): <br />
	 * @return int number of tokens behind the finish line required to win.
	 */
	public int getWinCount() {
		return winCount;
	}
	
	/** Method getPlayerColor: <br />
	 * @param playerID
	 * @return color value assigned to the player
	 */
	public Color getPlayerColor(int playerID) {
		return playerArray[playerID][0].getPlayerColor();
	}
	
	/** Method getPlayerColorName: <br />
	 * @param playerID
	 * @return the string representation of the player's color name
	 */
	public String getPlayerColorName(int playerID) {
		return playerArray[playerID][0].getPlayerColorName();
	}
	
	/** Method hasWinner: <br />
	 * Tests to see if the game either has a winner or has 
	 * enough losers to declare a winner.
	 * @return true if a winner is present, false otherwise
	 */
	public boolean hasWinner() {
		int[] finishedTokens = new int [playerCount];
		if (getPlayersThatCanWin().length == 1) {
			winner = getPlayersThatCanWin()[0];
			return true;
		}
		else {
			for (int i = 0; i < playerCount; i++) {
				for (int j = 0; j < tokenCount; j++) {
					if (playerArray[i][j].isFinished())
						finishedTokens[i]++;
				}
				if (finishedTokens[i] >= winCount) {
					winner = i;
					return true;
				}
			}
		}
		return false;
	}
	
	/** Method getWinner: <br />
	 * @return playerID of the winning player
	 */
	public int getWinner() {
		return winner;
	}

}
