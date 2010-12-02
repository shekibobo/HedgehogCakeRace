package application;

import linkedlists.DataStructureException;
import java.util.Random;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/** Class GameBoard: <br />
 * Creates a game grid of cells that can hold stacks of player tokens.
 * Provides methods for moving and placing tokens, tracking players,
 * tracking turns, and keeping track of what moves have been performed.
 * @author Joshua Kovach
 * @version 1.0
 */
public class GameBoard {
	/** Placement stage */
	public final int PLACEMENT = 0;
	/** Game play (movement) stage */
	public final int PLAY = 1;
	/** Game over stage.  Nothing can be done */
	public final int GAME_OVER = 2;
	
	/** A final cell */
	public final int FINISHED = -2;
	/** A normal cell, movement enabled */
	public final int NORMAL = -1;
	/** Standard pit. No token contained can move until tied for last */
	public final int PITS = 0;
	/** Black hole pit.  No token contained can move. Ever. */
	public final int BLACK_HOLES = 1;
	/** Worm hole pit.  Token is transported to a random location on the board. */
	public final int WORM_HOLES = 2;
	/** Origin pit.  Transports the token to the first cell in that row. */
	public final int ORIGINS = 3;
	
	private int rows;					// vertical size of the grid
	private int cols;					//horizontal size of the grid
	private GameBoardCell [][] grid;	//the grid itself
	private boolean[][] pitGrid;		//the arrangements of pits on the grid
	private PlayerTracker tracker;		//the players and their tokens
	private int pitMode;				//what type of pits
	
	private Random dieRoll;				//a <row> sided die
	private int forwardRow;				//the row selected from the die roll
	
	private int turnCounter;			//increments at the beginning of every turn
	private int currentPlayer;			//relative to the number of turns mod number of players
	private int currentRound;			//increments after every player has had a complete turn
	private int stage;					//placement, play, or game over
	
	private boolean sideMoved;			//indicates whether a side move has been made
	private boolean forwardMoved;		//indicates whether a forward move has been made
	
	/** Default Constructor: <br />
	 * Instantiates a new game board and creates a cell object
	 * in each grid location.
	 */
	public GameBoard(int rows, int cols, int playerCount, int tokenCount, int winCount, int pitMode) {
		this.rows = rows;
		this.cols = cols;
		this.pitMode = pitMode;
		grid = new GameBoardCell[rows][cols];
		pitGrid = PitGridGenerator.newHedgeHogGrid(rows);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (pitGrid[i][j]) {
					grid[i][j] = new GameBoardCell(false, pitMode);
				}
				else grid[i][j] = new GameBoardCell(true, pitMode);
			}
		}
		
		tracker = new PlayerTracker(playerCount, tokenCount, winCount);
		
		for (int i = 0; i < playerCount; i++) {
			System.out.print("Tokens belonging to player " + i + ": ");
			for (int j = 0; j < tokenCount; j++) {
				System.out.print(j + " ");
			}
			System.out.println("");
		}
		
		dieRoll = new Random();
		
		turnCounter = 0;
		currentPlayer = 0;
		currentRound = 0;
		stage = GAME_OVER;
	}
	
	/** Method stackToken: <br />
	 * Allows stacking of player tokens into a cell on the grid based on specific rules.
	 * @param row - row of cell to set
	 * @param col - column of cell to set
	 * @param pt - player token to stack in the given cell
	 * @param stage - 0 for token placement stage, 1 for game play stage
	 * @return true if stacked, false if not placed
	 */
	public boolean stackToken (int row, int col, PlayerToken pt) {
		//Restrictions on placing tokens during the stacking stage.
		if (stage == PLACEMENT) {
			for (int i = 0; i < rows; i++) {
				try {
					if (grid[i][col].getNumberOfItems() < grid[row][col].getNumberOfItems()) return false;
				}
				catch (ArrayIndexOutOfBoundsException aie) { // if the location to stack is bad
					System.out.println("Error: cell (" + row + ", " + col + ") does not exist");
					return false;
				}
			}
		}
		
		try {
			if (pitMode == PITS) {
				if (prevColsAreEmpty(col)) {
					/* PITS is the only mode where tokens in a disabled 
					 * cell will remain in the cell and can come out later.
					 * BLACK_HOLE will remain in the cell but never come out.
					 * WORM_HOLE and ORIGINS never stay in the cell.
					 * Therefore, PITS will be the only mode to enable a disabled cell.
					 */
					for (int i = 0; i < rows; i++) {
						grid[i][col].setEnabled(true);
						try {
							grid[i][col].peek().setEnabled();
						}
						catch (DataStructureException dse) {
							//do nothing, nothing to do.
						}
					}
				}
			}
			
			//Disable the top token of the location of the stack if it exists
			try {
				if ( !grid[row][col].isEmpty() ) {
					grid[row][col].peek().setDisabled();
				}
			}
			catch (DataStructureException dse) {
				System.out.println("No token in cell (" + row + ", " + col + ") to disable.");
			}
			catch (ArrayIndexOutOfBoundsException aie) { // if the location to stack is bad
				System.out.println("Error: cell (" + row + ", " + col + ") does not exist");
				return false;
			}
			
			//Stack the new token and change it's location
			if (pitMode == PITS || pitMode == BLACK_HOLES) {
				grid[row][col].push(pt);
				if ( ! grid[row][col].isEnabled() )	{
					pt.setDisabled();
					if (pitMode == BLACK_HOLES) pt.setUnwinnable();
				}
				
				else pt.setEnabled();
				pt.setLocation(row, col);
			}
			else if (pitMode == WORM_HOLES) {
				//generate a random new location for the little guy
				if ( !grid[row][col].isEnabled() ) {
					int newRow, newCol;
					do {
						newRow = dieRoll.nextInt(rows);
						newCol = dieRoll.nextInt(cols - 1);	//don't want an auto-win
					} while ( !grid[newRow][newCol].isEnabled() );	//don't want to move to another pit
					stackToken(newRow, newCol, pt);
					pt.setEnabled();
					pt.setLocation(newRow, newCol);
				}
				else {
					grid[row][col].push(pt);
					pt.setEnabled();
					pt.setLocation(row, col);
				}
			}
			else if (pitMode == ORIGINS) {
				//send the little guy back to the beginning
				if ( !grid[row][col].isEnabled() ) {
					grid[row][0].push(pt);
					pt.setLocation(row, 0);
				}
				else {
					grid[row][col].push(pt);
					pt.setEnabled();
					pt.setLocation(row, col);
				}
			}
			
			//flag a token in the last column as finished.
			try {
				if (col == cols - 1) grid[row][col].peek().setFinished();
			}
			catch (DataStructureException dse) {
				System.out.println("Error flagging token in cell (" + row + ", " + col + ") as finished.");
			}
			catch (ArrayIndexOutOfBoundsException aie) { // if the location to stack is bad
				System.out.println("Error: cell (" + row + ", " + col + ") does not exist");
				return false;
			}
			//updateTurnCounter(stage);
			return true;
		}
		catch (ArrayIndexOutOfBoundsException aie) { // if the location to stack is bad
			System.out.println("Error: cell (" + row + ", " + col + ") does not exist");
			return false;
		}
		
	}
	
	/** Method getPitMode: <br />
	 * @return PITS(0), BLACK_HOLES(1), WORM_HOLES(2), or ORIGINS(3)
	 * So that you don't have go so deep to get the info.
	 */
	public int getPitMode() {
		return pitMode;
	}
	
	/** Method moveToken: <br />
	 * Takes an existing enabled token from one cell and moves it to a new cell.
	 * @param startRow - starting position x
	 * @param startCol - starting position y
	 * @param endRow - ending position x
	 * @param endCol - ending position y
	 */
	public void moveToken(int startRow, int startCol, int endRow, int endCol) {
		try {
			//make sure the token is allowed to be moved
			if (grid[startRow][startCol].peek().isEnabled()) {
				try {
					//Move the token from the old location to the new location
					stackToken(endRow, endCol, grid[startRow][startCol].pop());
					
					//Enable the top token of the starting location stack
					if ( !grid[startRow][startCol].isEmpty() ) {
						grid[startRow][startCol].peek().setEnabled();
					}
					
				}
				catch (DataStructureException dse) {
					System.out.println("Error: no token to move at (" + startRow + ", " + startCol + ")");
				}
				catch (ArrayIndexOutOfBoundsException aie) {
					System.out.println("Error: cell (" + endRow + ", " + endCol + ") does not exist");
				}
			}
		}
		catch (DataStructureException dse) {
			System.out.println("Error: no tokens in cell (" + startRow + ", " + startCol + ")");
		}
	}
	
	/** Method moveTokenUp: <br />
	 * Moves a token up on the grid by one space
	 * @param startRow - int starting row location
	 * @param startCol - int starting column location
	 * @throws InvalidCellActionException - if a sidemove was already performed, you can't do it again
	 */
	public void moveTokenUp(int startRow, int startCol) throws InvalidCellActionException{
		if (sideMoved == false) {
			moveToken(startRow, startCol, startRow - 1, startCol);
			sideMoved = true;
		}
		else throw new InvalidCellActionException("MoveTokenDown Failed: Sideways move already performed");
	}
	
	/** Method moveTokenDown: <br />
	 * Moves a token down on the grid by one space
	 * @param startRow - int starting row location
	 * @param startCol - int starting column location
	 * @throws InvalidCellActionException - if a sidemove was already performed, you can't do it again
	 */
	public void moveTokenDown(int startRow, int startCol) throws InvalidCellActionException {
		if (sideMoved == false) {
			moveToken(startRow, startCol, startRow + 1, startCol);
			sideMoved = true;
		}
		else throw new InvalidCellActionException("MoveTokenDown Failed: Sideways move already performed");
	}
	
	/** Method moveTokenForward: <br />
	 * Moves a token forward on the grid by one space
	 * @param startRow - int starting row location
	 * @param startCol - int starting column location
	 * @throws InvalidCellActionException - if a forward move was already done, you can't do it again
	 */
	public void moveTokenForward(int startRow, int startCol) throws InvalidCellActionException {

		if (forwardMoved == false) {
			moveToken(startRow, startCol, startRow, startCol + 1);
			forwardMoved = true;
		}
		else throw new InvalidCellActionException("MoveTokenForward Failed: Forward move already performed");

		updateTurnCounter();
	}
	
	/** Method prevColsAreEmpty: <br />
	 * Tests to see if columns before the current selection are empty.
	 * **For use with cells defined as pits.
	 * @param col - column to test up to (exclusive)
	 * @return true if they are empty, false otherwise
	 */
	public boolean prevColsAreEmpty(int col) {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < col; j++) {
				if (!grid[i][j].isEmpty()) return false;
			}
		}
		return true;
	}
	
	/** Method getRows: <br />
	 * @return number of rows on the gameboard
	 */
	public int getRows() {
		return rows;
	}
	
	/** Method getCols: <br />
	 * @return number of columns on the gameboard
	 */
	public int getCols() {
		return cols;
	}
	
	/** Method getStackCount: <br />
	 * Returns the number of items stacked in a given cell.
	 * @param row row location
	 * @param col column location
	 * @return number of items stacked in the cell
	 */
	public int getStackCount(int row, int col) {
		return grid[row][col].getNumberOfItems();
	}
	
	/** Method updateTurnCounter: <br />
	 * Updates the turn counter, the current round and the current player.
	 * Resets sideMoved and forwardMoved flags, and rolls the die
	 */
	public void updateTurnCounter() {
		//check for end of turn
		if (stage == PLACEMENT || forwardMoved || getTokensFromForwardRow().length == 0) {
			System.out.println("Turn has been updated:");
			turnCounter++;
			currentPlayer = turnCounter % tracker.getPlayerCount();
			currentRound = turnCounter / tracker.getPlayerCount();
			
			sideMoved = false;
			forwardMoved = false;
			//roll the die
			forwardRow = dieRoll.nextInt(rows);
			System.out.println("turnCounter: " + turnCounter +
					"\ncurrentPlayer: " + currentPlayer + 
					"\ncurrentRound: " + currentRound + 
					"\nDieRoll: " + forwardRow);
		}
	}
	
	/** Method resetTurnCounter: <br />
	 * Restarts the turn counter (for a new game)
	 */
	public void resetTurnCounter() {
		turnCounter = -1;
		//reset everything
		updateTurnCounter();
	}
	
	/** Method getTokensFromForwardRow: <br />
	 * @return PlayerToken array - collection of tokens present in
	 * the forward movement row
	 */
	public PlayerToken[] getTokensFromForwardRow() {
		PlayerToken[] returnForwardTokens;
		//find any tokens in the row, add them to the arraylist
		ArrayList<PlayerToken> forwardTokens = new ArrayList<PlayerToken>();
		for (int i = 0; i < tracker.getPlayerCount(); i++) {
			for (int j = 0; j < tracker.getTokenCount(); j++) {
				if (tracker.getTokensForPlayer(i)[j].getLocationRow() == getForwardRow() 
						&& tracker.getTokensForPlayer(i)[j].isEnabled()) {
					forwardTokens.add(tracker.getTokensForPlayer(i)[j]);
				}
			}
		}
		//convert the arraylist to a standard array of the proper size
		returnForwardTokens = new PlayerToken[forwardTokens.size()];
		return forwardTokens.toArray(returnForwardTokens);
	}
	
	public void canPlayerWin() {
		if (getPitMode() == BLACK_HOLES) {
			Integer[] lostPlayers = getTracker().getPlayersThatCannotWin();
			for (int i = 0; i < lostPlayers.length; i++) {
				JOptionPane.showMessageDialog(null,
						getTracker().getPlayerColorName(lostPlayers[i]) + " cannot win the game.");
			}
			
		}
	}
	
	/** Method getCell: <br />
	 * Returns a GameBoardCell stack for further operations
	 * @param row - row location of the cell
	 * @param col - column location of the cell
	 * @return GameBoardCell the specified cell
	 */
	public GameBoardCell getCell(int row, int col) {
		return grid[row][col];
	}
	
	/** Method getTracker: <br />
	 * returns the player tracker for operations involving player tokens
	 * @return The full array of players and their tokens
	 */
	public PlayerTracker getTracker() {
		return tracker;
	}
	
	/** Method getCurrentPlayer: <br />
	 * @return the playerID of the current player
	 */
	public int getCurrentPlayer() {
		return currentPlayer;
	}
	
	/** Method getCurrentRound: <br />
	 * @return the current round (number of moves made by the first player)
	 */
	public int getCurrentRound() {
		return currentRound;
	}
	
	/** Method getForwardRow: <br />
	 * @return int row location corresponding to the die roll
	 */
	public int getForwardRow() {
		return forwardRow;
	}
	
	/** Method getStage: <br />
	 * @return int the game stage identifier
	 */
	public int getStage() {
		return stage;
	}
	
	/** Method setGameStage: <br />
	 * sets the game stage identifier to PLAY
	 */
	public void setGameStage() {
		stage = PLAY;
		System.out.println("Stage: HedgeHog Movement");
	}

	/** Method setPlacementStage: <br />
	 * sets the game stage identifier to PLACEMENT
	 */
	public void setPlacementStage() {
		stage = PLACEMENT;
		System.out.println("Stage: HedgeHog Placement");
	}
	
	/** Method setGameOver: <br />
	 * sets the game stage identifier to GAME_OVER
	 */
	public void setGameOver() {
		stage = GAME_OVER;
		System.out.println("Stage: Game Over");
	}
	
	/** Method setSideMoved: <br />
	 * sets the sideMoved flag to true
	 */
	public void setSideMoved() {
		sideMoved = true;
	}
	
	/** Method sideWasMoved: <br />
	 * @return boolean flag indicating whether a side move has been made
	 */
	public boolean sideWasMoved() {
		return sideMoved;
	}
	
	/** Method forwardWasMoved: <br />
	 * @return boolean flag indicating whether a forward move has been made
	 */
	public boolean forwardWasMoved() {
		return forwardMoved;
	}
	
	/** Method printGame: <br />
	 * prints a text representation of the board layout to the java terminal
	 */
	public void printGame() {
		for (int a = 0; a < cols; a++) {
			System.out.print("*****");
		}
		System.out.println("*");
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				try {
					if (i == forwardRow )
						System.out.print(">" + grid[i][j].peek().toString(1));
					else
						System.out.print("*" + grid[i][j].peek().toString(1));
				}
				catch (DataStructureException dse) {
					if ( i == forwardRow ) {
						if ( !grid[i][j].isEnabled() ) System.out.print("> XX ");
						else System.out.print(">    ");
					}
					else {
						if ( !grid[i][j].isEnabled() ) System.out.print("* XX ");
						else System.out.print("*    ");
					}
				}
			}
			System.out.println("*");
			for (int a = 0; a < cols; a++) {
				System.out.print("*****");
			}
			System.out.println("*");
		}
		System.out.println("");
		
	}
		
	/** Method toString: <br />
	 * @return String - grid format text output of the top element of each cell.
	 */
	public String toString() {
		String str = "";

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				try {
					str += grid[i][j].peek().toString(1) + " | ";
				}
				catch (DataStructureException dse) {
					str += " * | ";
				}
			}
			str += ("\n");
		}
		return "";
	}
}
