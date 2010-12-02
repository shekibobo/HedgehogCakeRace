package viewer;

import application.GameBoard;
import application.ConsoleIn;
import application.PlayerToken;
import application.InvalidCellActionException;

/** Class TextGameClient: <br />
 * Provides a text user interface for playing Hurry Up Hedgehog.
 * @author Joshua Kovach
 * @version 1.0
 *
 */
public class TextGameClient {
	/** provides an error checking scanner utility to get input from the user. */
	static ConsoleIn scan = new ConsoleIn();
	
	/** Method main: <br />
	 * Makes the game playable.  Keeps the loops going through each stage of the game.
	 * @param args
	 */
	public static void main (String[] args) {
		/** creates the data board */
		GameBoard board;
		
		/** flag to see if the user will continue playing */
		boolean playing = true;
		
		/** the game will run while playing is true */
		while (playing) {
			board = gameBoardSetup();
			
			//loop while placing tokens
			placeTokens(board);
				
			System.out.println("\n\nTokens have been placed. Commencing game...\n\n");
			
			//loop through the turns until there is a winner
			do {
				playTheGame(board);
				board.printGame();
			} while ( ! board.getTracker().hasWinner() );
			System.out.println(board.getTracker().getPlayerColorName(board.getTracker().getWinner()) + 
					" has won the game!");
			
			playing = playOptionMenu();
		}
		
	}
	
	/** Method gameBoardSetup: <br />
	 * Creates a new game board for Hurry Up HedgeHog based on user
	 * specified attributes, including grid size, player count, 
	 * token count, and win count.
	 * @return GameBoard the data tracker for playing the game
	 */
	private static GameBoard gameBoardSetup() {
		int rows = 0;
		int cols = 0;
		int playerCount = 0;
		int tokenCount = 0;
		int winCount = 0;
		int pitMode = -1;	//invalid value
		
		while (rows < 4 || rows > 12) {
			rows = scan.readInt("How many rows? [4-12] (Board will be rows x rows + 3)");
			if (rows < 4 || rows > 12) {
				System.out.print("Invalid board size. ");
			}
			cols = rows + 3;
		}
		
		while (playerCount < 2 || playerCount > 6) {
			playerCount = scan.readInt("How many players? [2-6]");
			if (playerCount < 2 || playerCount > 6) {
				System.out.print("Invalid number of players. ");
			}
		}
		
		while (tokenCount < 2 || tokenCount > 8) {
			tokenCount = scan.readInt("How many tokens per player? [2-8]");
			if (tokenCount < 2 || tokenCount > 8) {
				System.out.print("Invalid token count. ");
			}
		}
		
		while (winCount < 1 || winCount > tokenCount) {
			winCount = scan.readInt("How many tokens finished to win? [1-" + tokenCount + "]");
			if (winCount < 1 || winCount > tokenCount) {
				System.out.print("Invalid win count. ");
			}
		}
		
		String pitMenu = "What type of pits?" + "\n0. Standard Pits" + 
			"\n1. Black Holes" + "\n2. Worm Holes" + "\n3. Origins" + "\n";
		while (pitMode < 0 || pitMode > 3) {
			pitMode = scan.readInt(pitMenu);
			if (pitMode < 0 || pitMode > 3) {
				System.out.print("Invalid selection. ");
			}
		}
		return new GameBoard(rows, cols, playerCount, tokenCount, winCount, pitMode);
	}
	
	/** Method placeTokens: <br />
	 * Provides a loop during which players may place their hedgehogs into 
	 * a cell in the first row of the board until all tokens are placed.
	 * @param board - passes the game board to this method so it can be manipulated
	 */
	private static void placeTokens(GameBoard board) {
		board.setPlacementStage();
		do {				
			//keep trying until it's been placed
			while (! board.stackToken(scan.readInt("Player " + board.getCurrentPlayer() + ", choose a row to place token " + (board.getCurrentRound() + 1)), 0, 
					board.getTracker().getTokensForPlayer(board.getCurrentPlayer())[board.getCurrentRound()]) ) {
				System.out.println("Couldn't place token: please choose a different location.");
			}
			board.updateTurnCounter();
			board.printGame();
			
			
		} while (board.getCurrentRound() < board.getTracker().getTokenCount());
		board.setGameStage();
	}
	
	/** Method playTheGame: <br />
	 * Builds the menus and takes input from the user to move tokens
	 * across the board until a winner is found.
	 * @param board
	 */
	private static void playTheGame(GameBoard board) {
		PlayerToken [] token = board.getTracker().getTokensForPlayer(board.getCurrentPlayer());
		String movableTokenMenu = "";
		String moveMenu = "";
		int choice = -1;
		
		
		System.out.println("Die Roll: " + board.getForwardRow());
		
		//Build the menu of movable tokens
		System.out.println("Movable hedgehogs for player " + board.getCurrentPlayer() + ":");
		for (int i = 0; i < token.length; i++) {
			if ( ! board.sideWasMoved() ) {
				if (token[i].isEnabled()) {
					movableTokenMenu += (i + ". Hedgehog at " + token[i].getLocationString() + "[Sideways]");
					if (token[i].getLocationRow() == board.getForwardRow()) movableTokenMenu += "[Foward]";
					movableTokenMenu += "\n";
				}
			}
			else {
				if (token[i].isEnabled() && token[i].getLocationRow() == board.getForwardRow()) {
					movableTokenMenu += (i + ". Hedgehog at " + token[i].getLocationString() + "[Forward]\n");
				}
			}
		}
		
		movableTokenMenu += "9. Skip Side Move\nPlease choose a token to move";
		
		//Get the players choice for token to move
		while ((choice < 0 || (choice > token.length - 1 && choice != 9) || (choice != 9 && !token[choice].isEnabled()))) {
			choice = scan.readInt(movableTokenMenu);
			if ((choice < 0 || (choice > token.length - 1 && choice != 9) || (choice != 9 && !token[choice].isEnabled()))) {
				System.out.print("Invalid selection.\n");
			}
		}
		if (choice != 9) {
			//Build menu of possible moves
			int moveOption = -1;
			int moveChoice = -1; //ensure entry into the move choice loop
			
			if (token[choice].getLocationRow() < board.getRows() - 1) {
				moveOption++;
				moveMenu += (moveOption + ". Move Down\n");
			}
			if (token[choice].getLocationRow() > 0) {
				moveOption++;
				moveMenu += (moveOption + ". Move Up\n");
			}
			if (token[choice].getLocationRow() == board.getForwardRow()) {
				moveOption++;
				moveMenu += (moveOption + ". Move Forward\n");
			}
			
			//get the player's moves
			while (moveChoice < 0 || moveChoice > moveOption) {
				moveChoice = scan.readInt(moveMenu + "Please choose a move.");
				if (moveChoice < 0 || moveChoice > moveOption) {
					System.out.println("Invalid move.");
				}
			}
			
			//process the move choices
		
			if (moveOption == 0) {
				if (token[choice].getLocationRow() < board.getRows() - 1) {
					try {
						board.moveTokenDown(token[choice].getLocationRow(), token[choice].getLocationCol());
					}
					catch (InvalidCellActionException ica) {
						System.out.println(ica.getMessage());
					}
				}
				else if (token[choice].getLocationRow() > 0) {
					try {
						board.moveTokenUp(token[choice].getLocationRow(), token[choice].getLocationCol());
					}
					catch (InvalidCellActionException ica) {
						System.out.println(ica.getMessage());
					}
				}
				else if (token[choice].getLocationRow() == board.getForwardRow()) {
					try {
						board.moveTokenForward(token[choice].getLocationRow(), token[choice].getLocationCol());
					}
					catch (InvalidCellActionException ica) {
						System.out.println(ica.getMessage());
					}
				}
			}
			else if (moveOption == 1) {
				if (token[choice].getLocationRow() < board.getRows() - 1 && token[choice].getLocationRow() > 0) {
					try {
						if (moveChoice == 0) board.moveTokenDown(token[choice].getLocationRow(), token[choice].getLocationCol());
						else if (moveChoice == 1) board.moveTokenUp(token[choice].getLocationRow(), token[choice].getLocationCol());
					}
					catch (InvalidCellActionException ica) {
						System.out.println(ica.getMessage());
					}
				}
				else if (token[choice].getLocationRow() < board.getRows() - 1 && token[choice].getLocationRow() == board.getForwardRow()) {
					try {
						if (moveChoice == 0) board.moveTokenDown(token[choice].getLocationRow(), token[choice].getLocationCol());
						else if (moveChoice == 1) board.moveTokenForward(token[choice].getLocationRow(), token[choice].getLocationCol());
					}
					catch (InvalidCellActionException ica) {
						System.out.println(ica.getMessage());
					}
				}
				else if (token[choice].getLocationRow() > 0 && token[choice].getLocationRow() == board.getForwardRow()) {
					try {
						if (moveChoice == 0) board.moveTokenUp(token[choice].getLocationRow(), token[choice].getLocationCol());
						else if (moveChoice == 1) board.moveTokenForward(token[choice].getLocationRow(), token[choice].getLocationCol());
					}
					catch (InvalidCellActionException ica) {
						System.out.println(ica.getMessage());
					}
				}
			}
			else if (moveOption == 2) {
				if (moveChoice == 0) {
					try {
						board.moveTokenDown(token[choice].getLocationRow(), token[choice].getLocationCol());
					}
					catch (InvalidCellActionException ica) {
						System.out.println(ica.getMessage());
					}
				}
				else if (moveChoice == 1) {
					try {
						board.moveTokenUp(token[choice].getLocationRow(), token[choice].getLocationCol());
					}
					catch (InvalidCellActionException ica) {
						System.out.println(ica.getMessage());
					}
				}
				else if (moveChoice == 2) {
					try {
						board.moveTokenForward(token[choice].getLocationRow(), token[choice].getLocationCol());
					}
					catch (InvalidCellActionException ica) {
						System.out.println(ica.getMessage());
					}
				}
			}
		}
		else board.setSideMoved();
		
		forwardMoveMenu(board);
		
		
	}
	
	/** Method forwardMoveMenu: <br />
	 * If a forward move has already been performed, passes the board to a new player.
	 * Otherwise, creates a menu of forward movable tokens for the end of the turn.
	 * @param board - pass the board for manipulation
	 */
	private static void forwardMoveMenu(GameBoard board) {
		PlayerToken [] forwardToken;
		board.printGame();
		
		//if there are no tokens to move, end the turn, otherwise get
		//the movement command for one of the tokens to move forward and update the turn
		if ( board.sideWasMoved() && ! board.forwardWasMoved() ) {
			forwardToken = board.getTokensFromForwardRow();
			if (forwardToken.length == 0) {
				board.canPlayerWin();
				board.updateTurnCounter();
			}
			else {
				System.out.println("Die Roll: " + board.getForwardRow());
				String forwardMenu = "Player " + board.getCurrentPlayer() + ", choose a hedgehog to advance:\n";
				int forwardOption = -1;
				for (int i = 0; i < forwardToken.length; i++) {
					forwardMenu += (i + ". " + "[Player " + forwardToken[i].getPlayerID() + "] " 
							+ forwardToken[i].getLocationString() + "\n");
				}
				
				while (forwardOption < 0 || forwardOption > forwardToken.length - 1) {
					forwardOption = scan.readInt(forwardMenu + "Choose a hedgehog to advance.");
					if (forwardOption < 0 || forwardOption > forwardToken.length - 1) {
						System.out.println("Invalid hedgehog.\n");
					}
				}
				try {
					board.moveTokenForward(forwardToken[forwardOption].getLocationRow(), forwardToken[forwardOption].getLocationCol());
				}
				catch (InvalidCellActionException ica) {
					System.out.println(ica.getMessage());
				}
				board.canPlayerWin();
			}
		}
	}
	
	/** Method playOptionMenu: <br />
	 * Asks the user if they will play another game
	 * @return true if playing a second game, false if quitting.
	 */
	private static boolean playOptionMenu() {
		char choice = ' ';	//dummy value
		while (choice != 'y' && choice != 'n') {
			choice = scan.readChar("Would you like to play again? [y/n]");
		}
		if (choice == 'y') return true;
		else return false;		
	}

}
