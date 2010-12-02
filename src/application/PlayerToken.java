package application;

/** Class PlayerToken: <br />
 * A player token belonging to a specified player at location(x, y).
 * Location is defined as a two-dimensional point of (x = row, y = column),
 * and has provided methods for defining and changing the location accessible 
 * by the game class.
 * 
 * @author Joshua Kovach
 * @version 1.0
 *
 */
public class PlayerToken extends Player {
	private int locationRow;
	private int locationCol;
	private boolean finished;
	private boolean enabled;
	private boolean winnable;
	
	/** Constructor: <br />
	 * Creates an unplaced player token belonging to the given player.
	 * @param playerID
	 */
	public PlayerToken(int playerID) {
		super(playerID);
		locationRow = -1;
		locationCol = -1;
		winnable = true;
	}
	
	/** Constructor: <br />
	 * Creates a new token for a player at the given location
	 * @param playerID
	 * @param newX row to be placed
	 * @param newY column to be placed
	 */
	public PlayerToken(int playerID, int newX, int newY) {
		super(playerID);
		locationRow = newX;
		locationCol = newY;
		winnable = true;
	}
	
	/** Constructor: <br />
	 * Creates a new player token from an existing player token.
	 * @param newPlayerToken
	 */
	public PlayerToken(PlayerToken newPlayerToken) {
		super(newPlayerToken.getPlayerID());
		locationRow = newPlayerToken.getLocationRow();
		locationCol = newPlayerToken.getLocationCol();
	}	
	
	/** Method setLocation: <br />
	 * Sets the location of the token on the grid.
	 * @param x row
	 * @param y column
	 */
	public void setLocation(int x, int y) {
		locationRow = x;
		locationCol = y;
	}
	
	/** Method getLocationRow: <br />
	 * @return int row location of the token
	 */
	public int getLocationRow() {
		return locationRow;
	}

	/** Method getLocationCol: <br />
	 * @return int column location of the token
	 */
	public int getLocationCol() {
		return locationCol;
	}
	
	/** Method getLocationString: <br />
	 * @return String (row, column) representation
	 */
	public String getLocationString() {
		return "(" + locationRow + ", " + locationCol + ")";
	}
	
	/** Method setEnabled: <br />
	 * Sets the token to enabled
	 */
	public void setEnabled() {
		enabled = true;
	}
	
	/** Method setDisabled: <br />
	 * Sets the token to disabled (cannot move)
	 */
	public void setDisabled() {
		enabled = false;
	}
	
	/** Method setUnwinnable: <br />
	 * tokens are set to winnable by default.  However, certain 
	 * modes of the game permanently disable tokens.  This is
	 * used to indicate that the token can no longer be used.
	 */
	public void setUnwinnable() {
		winnable = false;
	}
	
	/** Method isWinnable: <br />
	 * @return true if the token is able to continue moving
	 * 			false if it is disabled for the remainder of the game
	 */
	public boolean isWinnable() {
		return winnable;
	}
	
	/** Method isEnabled(): <br />
	 * @return true if enabled, false if disabled
	 */
	public boolean isEnabled() {
		return enabled;
	}
	
	/** Method setFinished(): <br />
	 * sets the token to finished (it has reached the end of the board.
	 */
	public void setFinished() {
		finished = true;
		enabled = false;
	}
	
	/** Method isFinished: <br />
	 * @return true if the token is finished, false if still in play
	 */
	public boolean isFinished() {
		return finished;
	}
	
	/** Method toString: <br />
	 * Returns the player ID, color, and location.
	 * @param 0 for full description of player token
	 * @param 1 for player ID of the token
	 */
	public String toString(int fullOrBasic) {
		if(fullOrBasic == 0) {
			if (isEnabled()) {
				return (super.toString() + "\nToken Location: (" + locationRow + ", " + locationCol + ") [Enabled]");
			}
			else if (isFinished()){
				return (super.toString() + "\nToken Location: (" + locationRow + ", " + locationCol + ") [Finished]");
			}
			else {
				return (super.toString() + "\nToken Location: (" + locationRow + ", " + locationCol + ") [Disabled]");
			}
		}
		else {
			//prints the player ID and it's status (if not enabled)
			if (isEnabled()) return " " + super.getPlayerID() + "  ";
			else if (isFinished()) return " " + super.getPlayerID() + "F ";
			else return " " + super.getPlayerID() + "D ";
		}
	}
}
