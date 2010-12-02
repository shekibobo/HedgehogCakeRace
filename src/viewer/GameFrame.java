package viewer;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import linkedlists.DataStructureException;

import application.*;


public class GameFrame extends JFrame implements ActionListener, MouseListener {

	/**
	 * gets rid of some warning
	 */
	private static final long serialVersionUID = 1L;
	//the game state tracker
	private GameBoard dataBoard;
	
	//frame layout items
	private GameMenu gameMenu;
	private OptionPanel optionPanel;
	private JPanel playArea;
	private JLabel splashLogo;
	private ViewerCell[][] playBoard;
	private JTextArea statusBar;
	
	// this is the initial size of the window
	private int height = 600;
	private int width = 800;
	
	/** Constructor: <br />
	 * Creates a new window 800x600 with the basic gui 
	 * components required to start a game.
	 */
	public GameFrame() {
		super("The Great Hedgehog Cake Race");
		this.setSize(new Dimension(width, height));
		
		//generate the menu bar
		MenuHandler mh = new MenuHandler();
		gameMenu = new GameMenu();
		setJMenuBar(gameMenu);
		gameMenu.getNewGame().addActionListener(mh);
		gameMenu.getQuitGame().addActionListener(mh);
		gameMenu.getRules().addActionListener(mh);
		gameMenu.getAbout().addActionListener(mh);
		
		createGUI();
	}
	
	/** Method createGUI: <br />
	 * Creates the gui inside the window consisting of an 
	 * option panel, a play area, and a status bar.
	 * Adds this class as an action listener to the buttons
	 * on the option panel.
	 */
	private void createGUI() {
		this.setLayout(new BorderLayout());
		
		optionPanel = new OptionPanel();
		optionPanel.addStartButtonListener(this);
		add(optionPanel, BorderLayout.NORTH);
		
		playArea = new JPanel();
		playArea.setBackground(Color.black);
		splashLogo = new JLabel(ImageLoader.loadIcon("logo.png"));
		playArea.add(splashLogo);
		add(playArea, BorderLayout.CENTER);
				
		statusBar = new JTextArea("Please select your game settings and press the 'Start Game' button.");
		statusBar.setSize(new Dimension(width, 150));
		add(statusBar, BorderLayout.SOUTH);
		
		setVisible(true);
	}
	
	/** Method createNewGame: <br />
	 * Creates a new game, a new game viewer grid, 
	 * resizes the window, and triggers the first stage of the game.
	 */
	public void createNewGame() {
		System.out.println("Players: " + optionPanel.getPlayerCount() + 
				"\nTokens: " + optionPanel.getTokenCount() + 
				"\nWin Length: " + optionPanel.getWinCount() + 
				"\nGame Size: " + optionPanel.getBoardSize() + " x " + (optionPanel.getBoardSize() + 3));
		
		playArea.removeAll();
		
		dataBoard = new GameBoard(optionPanel.getBoardSize(), optionPanel.getBoardSize() + 3, 
				optionPanel.getPlayerCount(), optionPanel.getTokenCount(), optionPanel.getWinCount(), optionPanel.getMode());
		
		dataBoard.printGame();
		
		//Instantiate the grid of viewable cells
		playBoard = new ViewerCell[dataBoard.getRows()][dataBoard.getCols()];
		System.out.println("PlayBoard Created");
		playArea.setLayout(new GridLayout(dataBoard.getRows(), dataBoard.getCols()));
		System.out.println("Grid Layout implemented");
		
		//Go through each cell of the dataBoard
		for (int i = 0; i < playBoard.length; i++) {
			for (int j = 0; j < playBoard[i].length; j++) {
				//Create a new cell
				playBoard[i][j] = new ViewerCell(dataBoard.NORMAL);
				playBoard[i][j].addButtonListeners(this);
				//add mouse listener if it's in the first column
				//This is to allow players to place a token by clicking
				if (j == 0) {
					playBoard[i][j].addMouseListener(this);
				}
				
				//add it to the board (next open grid spot
				playArea.add(playBoard[i][j]);
			}
		}
		drawBoard();
		add(playArea, BorderLayout.CENTER);
		dataBoard.setPlacementStage();
		statusBar.setText(dataBoard.getTracker().getPlayerColorName(dataBoard.getCurrentPlayer())
				+ ", place your hedgehog somewhere in the first column");
		pack();
		setVisible(true);
		
	}
	
	/** Method beginMovementTurn: <br />
	 * Checks for a winner at the beginning of the turn.
	 * Highlights movable tokens by the player, or allows 
	 * them to skip their sideways move.
	 */
	private void beginMovementTurn() {
		if (dataBoard.getTracker().hasWinner()) {
			
			drawBoard();
			dataBoard.canPlayerWin();
			
			JOptionPane.showMessageDialog(null, "Congratulations, " + 
					dataBoard.getTracker().getPlayerColorName(dataBoard.getTracker().getWinner()) + "!\n" + 
					"You win all the cake!");
			
			dataBoard.setGameOver();
			optionPanel.getSkipSideButton().setVisible(false);
			
			statusBar.setText("The game is now over.  Click \"New Game\" to play again.");
		}	
		else {
			
			optionPanel.getSkipSideButton().setVisible(true);
			
			statusBar.setText(dataBoard.getTracker().getPlayerColorName(dataBoard.getCurrentPlayer())
					+ ", make your move.");
			
			drawBoard();
			
			highlightForwardRow();
			
			highlightPlayerMoves();
			
			highlightForwardMoves();
		}
	}
	
	/** Method highlightForwardRow: <br />
	 * Highlights the cells in the selected row in the 
	 * color of the current player.
	 */
	private void highlightForwardRow() {
		for (int i = 0; i < dataBoard.getCols(); i++) {
			if (dataBoard.getCell(dataBoard.getForwardRow(), i).isEnabled()) {
				playBoard[dataBoard.getForwardRow()][i].setBackground(Color.lightGray);
				playBoard[dataBoard.getForwardRow()][i].setHiLiteBorder(dataBoard.getTracker().getPlayerColor(dataBoard.getCurrentPlayer()));
			}
		}
	}
	
	
	/** Method drawBoard: <br />
	 * draws the board with no buttons, with cells colored based on type.
	 * Only changes the buttons available and the border of the cell.
	 */
	private void drawBoard() {
		for (int i = 0; i < dataBoard.getRows(); i++) {
			for (int j = 0; j < dataBoard.getCols(); j++) {
				//draw cells as standard, pits, or finished
				if (dataBoard.getCell(i, j).isEnabled()) {
					if (j == dataBoard.getCols() - 1) {
						playBoard[i][j].setBackground(Color.darkGray);	//finished cells
						playBoard[i][j].setBackgroundImage(dataBoard.FINISHED);
					}
					else {				
						playBoard[i][j].setBackground(Color.gray); //standard cells
						playBoard[i][j].setBackgroundImage(dataBoard.NORMAL);
					}
				}
				else {
					playBoard[i][j].setBackground(Color.black);	//pits
					playBoard[i][j].setBackgroundImage(dataBoard.getPitMode());
				}
				
				playBoard[i][j].disableAllMoves();
				playBoard[i][j].setStdBorder();
				playBoard[i][j].repaint();
			}
		}
	}
	
	/** Method highlightPlayerMoves: <br />
	 * Highlights the moves allowed by the current player,
	 * enables the buttons for allowed token movements.
	 */
	private void highlightPlayerMoves() {
		int playerID = dataBoard.getCurrentPlayer();
		for (int i = 0; i < dataBoard.getTracker().getTokenCount(); i++) {
			if (dataBoard.getTracker().getTokensForPlayer(playerID)[i].isEnabled()) {
				int row = dataBoard.getTracker().getTokensForPlayer(playerID)[i].getLocationRow();
				int col = dataBoard.getTracker().getTokensForPlayer(playerID)[i].getLocationCol();
				if (row > 0) playBoard[row][col].enableMoveUp();
				if (row < dataBoard.getRows() - 1) playBoard[row][col].enableMoveDown();
				if (row == dataBoard.getForwardRow()) playBoard[row][col].enableMoveForward();
				
				playBoard[row][col].repaint();
			}
		}
	}
	
	/** Method forwardMoveStage: <br />
	 * disables all moves and repaints the board, 
	 * then highlights only the forward movable tokens.
	 */
	private void forwardMoveStage() {
		
		for (int a = 0; a < dataBoard.getRows(); a++) {
			for (int b = 0; b < dataBoard.getCols(); b++) {
				playBoard[a][b].disableAllMoves();
				playBoard[a][b].repaint();
			}
		}
		
		PlayerToken[] forwardToken;				//get forward movable tokens
		if ( dataBoard.sideWasMoved() && ! dataBoard.forwardWasMoved() ) {
			playArea.repaint();
			forwardToken = dataBoard.getTokensFromForwardRow();
			if (forwardToken.length == 0) {
				dataBoard.updateTurnCounter();	//end the turn
				beginMovementTurn();			//start a new turn
			}
			else {
				highlightForwardMoves();			//allow new forward moves
				playArea.repaint();
			}
		}
	}
	
	
	
	/** Method highlightForwardRow: <br />
	 * Highlights and enables tokens that can move forward.
	 */
	private void highlightForwardMoves() {
		PlayerToken[] forwardToken;
		forwardToken = dataBoard.getTokensFromForwardRow();
		
		for (int i = 0; i < forwardToken.length; i++) {					
			if (forwardToken[i].isEnabled()) {
				int row = forwardToken[i].getLocationRow();
				int col = forwardToken[i].getLocationCol();
				playBoard[row][col].enableMoveForward();
				playArea.repaint();
			}
		}
			
	}
	
	/** Method moveTokenView: <br />
	 * duplicates token movement in the dataBoard on the gui gameBoard,
	 * updating the visual information in each of the effected cells.
	 * @param startRow	int starting row location
	 * @param startCol  int starting column location
	 * @param endRow    int ending row location
	 * @param endCol    int ending column location
	 */
	private void moveTokenView(int startRow, int startCol, int endRow, int endCol) {
		try {
			//update starting cell image
			playBoard[startRow][startCol].setCellImage(dataBoard.getCell(startRow, startCol).peek().getPlayerImage());
		}
		catch (DataStructureException dse) {
			playBoard[startRow][startCol].resetCellImage();
			
		}
		
		//reset hidden color
		playBoard[startRow][startCol].resetHiddenColor();
		playBoard[startRow][startCol].repaint();
		
		
		//update end cell image
		try {
			playBoard[endRow][endCol].setCellImage(dataBoard.getCell(endRow, endCol).peek().getPlayerImage());
		}
		catch (DataStructureException dse) {
			playBoard[startRow][startCol].resetCellImage();
			System.out.println("Something bad happened");
		}
		
		//set hidden color of the end cell
		this.setHiddenColor(endRow, endCol);
		playBoard[endRow][endCol].repaint();
	}
	
	/** Method setHiddenColor: <br />
	 * Adds a virtual token to the viewable stack, indicating which 
	 * color tokens are stacked beneath the first token.
	 * @param row int the row of the cell to update
	 * @param col int the column of the cell to update
	 */
	private void setHiddenColor(int row, int col) {
		try {
			if (dataBoard.getCell(row, col).getNumberOfItems() > 1) {
				playBoard[row][col].setHiddenColor(dataBoard.getCell(row, col).peekNext().getData().getPlayerColor());
			}
		}
		catch (DataStructureException dse) {
			playBoard[row][col].setHiddenColor(Color.lightGray);
		}
		playBoard[row][col].repaint();
	}

	@Override
	/** Method actionPerformed: <br />
	 * Defines actions to perform when each button in the interface is clicked.
	 * Buttons included:
	 * 		optionPanel.startButton
	 * 		optionPanel.skipSideButton
	 * 		playBoard[row][col].upButton
	 * 		playBoard[row][col].downButton
	 * 		playBoard[row][col].forwardButton
	 * 
	 * different actions are performed based on the 
	 * status of the game stage, as determined by dataBoard.getStage()
	 */
	public void actionPerformed(ActionEvent ae) {
		/** Create a new game when "New Game" button is pressed **/
		if (ae.getSource() == optionPanel.getStartButton()) {
			createNewGame();
			optionPanel.getStartButton().setText("New Game");
		}
		
		if (ae.getSource() == optionPanel.getSkipSideButton()) {
			if ( ! dataBoard.sideWasMoved() && ! dataBoard.forwardWasMoved() ) {
				dataBoard.setSideMoved();
				System.out.println("Side Move Skipped");
				forwardMoveStage();
			}
		}
		
		//Listen for cell buttons
		if (dataBoard.getStage() == dataBoard.PLAY) {
			for (int i = 0; i < dataBoard.getRows(); i++) {
				for (int j = 0; j <  dataBoard.getCols(); j++) {
					if (ae.getSource() == playBoard[i][j].getUpButton()) {
						//token has been moved
						try {
							PlayerToken tokenToMove;
							tokenToMove = dataBoard.getCell(i, j).peek();
							dataBoard.moveTokenUp(i, j);
							this.moveTokenView(i, j, tokenToMove.getLocationRow(), tokenToMove.getLocationCol());
						}
						catch (InvalidCellActionException ica) {
							JOptionPane.showMessageDialog(null, ica.getMessage());
						}
						catch (DataStructureException dse) {
							JOptionPane.showMessageDialog(null, dse.getMessage());
						}
						forwardMoveStage();
					}
					
					if (ae.getSource() == playBoard[i][j].getDownButton()) {
						//token has been moved
						try {
							PlayerToken tokenToMove;
							tokenToMove = dataBoard.getCell(i, j).peek();
							dataBoard.moveTokenDown(i, j);
							this.moveTokenView(i, j, tokenToMove.getLocationRow(), tokenToMove.getLocationCol());
						}
						catch (InvalidCellActionException ica) {
							JOptionPane.showMessageDialog(null, ica.getMessage());
						}
						catch (DataStructureException dse) {
							JOptionPane.showMessageDialog(null, dse.getMessage());
						}
						forwardMoveStage();
					}
					
					if (ae.getSource() == playBoard[i][j].getForwardButton()) {		
						if (!dataBoard.sideWasMoved()) dataBoard.setSideMoved();
						//token has been moved
						try {
							PlayerToken tokenToMove;
							tokenToMove = dataBoard.getCell(i, j).peek();
							dataBoard.moveTokenForward(i, j);	//turn is updated here
							this.moveTokenView(i, j, tokenToMove.getLocationRow(), tokenToMove.getLocationCol());
						}
						catch (InvalidCellActionException ica) {
							JOptionPane.showMessageDialog(null, ica.getMessage());
						}
						catch (DataStructureException dse) {
							JOptionPane.showMessageDialog(null, dse.getMessage());
						}
						//start a new turn
						beginMovementTurn();
					}
				}
			}
		}
		playArea.repaint();
	}

	@Override
	/** Method mouseClicked: <br />
	 * Allows tokens to be placed during the PLACEMENT stage of the game
	 * by clicking directly on the desired cell.
	 */
	public void mouseClicked(MouseEvent me) {
		if (dataBoard.getStage() == dataBoard.PLACEMENT) {	//if we're in the placement stage, do this
			for (int i = 0; i < dataBoard.getRows(); i++) {
				//If the cell isn't empty, set the hidden player indicator to the current color
				if (me.getSource() == playBoard[i][0]) {
					//If token placement was successful
					if (dataBoard.stackToken(i, 0, dataBoard.getTracker().getTokensForPlayer(dataBoard.getCurrentPlayer())[dataBoard.getCurrentRound()])) {
						this.setHiddenColor(i, 0);
						playBoard[i][0].setStdBorder();
						//update the turn counter
						dataBoard.updateTurnCounter();
						statusBar.setText(dataBoard.getTracker().getPlayerColorName(dataBoard.getCurrentPlayer())
								+ ", place your hedgehog somewhere in the first column");
						//check if all tokens have been placed, if so, set to the movement
						if ( ! (dataBoard.getCurrentRound() < dataBoard.getTracker().getTokenCount()) ) {
							dataBoard.setGameStage();
							beginMovementTurn();
						}
					}
					else { //token placement failed
						System.out.println("\007");
						statusBar.setText("Error: can't stack a hedgehog that high yet!");
					}
					//display the current player's token
					try {
						playBoard[i][0].setCellImage(dataBoard.getCell(i, 0).peek().getPlayerImage());
					}
					catch (DataStructureException dse) {
						JOptionPane.showMessageDialog(null, "Error: cell is empty (even though there should be something there.");
					}
				}
			}
		}
	}

	@Override
	/** Method mouseEntered: <br />
	 * During the PLACEMENT stage of the game, highlights the cells
	 * where tokens can be placed in the color of the current player
	 * when the mouse enters that cell.  Logic included to ensure that
	 * only cells with equal or lesser stack counts will be highlighted 
	 * and allow tokens to be placed.
	 */
	public void mouseEntered(MouseEvent me) {
		if (dataBoard.getStage() == dataBoard.PLACEMENT) {
			//Start with an arbitrarily high number
			int lowestCount = dataBoard.getTracker().getTokenCount() * dataBoard.getTracker().getPlayerCount();
			
			//find the smallest stack
			for (int i = 0; i < dataBoard.getRows(); i++) {				
				lowestCount = Math.min(lowestCount, dataBoard.getCell(i, 0).getNumberOfItems());
			}
			
			//compare the current stack to the lowest stack.
			//If the current stack is not bigger than the smallest stack,
			//you can place a token there (it gets highlighted)
			for (int i = 0; i < dataBoard.getRows(); i++) {
				if (me.getSource() == playBoard[i][0]) {
					if (! (dataBoard.getCell(i, 0).getNumberOfItems() > lowestCount) )
						playBoard[i][0].setHiLiteBorder(dataBoard.getTracker().getPlayerColor(dataBoard.getCurrentPlayer()));
				}
			}
			
		}
	}

	@Override
	/** Method mouseExited: <br />
	 * Ensures that the cell that was highlighted during the 
	 * PLACEMENT stage is reset to the standard unhighlighted 
	 * border type.
	 */
	public void mouseExited(MouseEvent me) {
		
		if (dataBoard.getStage() == dataBoard.PLACEMENT) {
			for (int i = 0; i < dataBoard.getRows(); i++) {
				if (me.getSource() == playBoard[i][0]) {
					playBoard[i][0].setStdBorder();
				}
			}
		}
		
	}

	@Override
	/** Method mousePressed: <br />
	 * does nothing
	 */
	public void mousePressed(MouseEvent me) {
		//do nothing
	}

	@Override
	/** Method mouseReleased: <br />
	 * does nothing
	 */
	public void mouseReleased(MouseEvent me) {
		//do nothing
	}
	
	/** Class MenuHandler: <br />
	 * provides action event handling specifically for the menu
	 * items in the game frame.
	 * @author Joshua Kovach
	 * @version 1.0
	 *
	 */
	private class MenuHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource() == gameMenu.getNewGame()) {
				createNewGame();
			}
			if (ae.getSource() == gameMenu.getQuitGame()) {
				JOptionPane.showMessageDialog(null, "Thanks for playing!  Good bye.");
				System.exit(1);
			}
			if (ae.getSource() == gameMenu.getRules()) {
				String ruleListing = 
				"Rules of the HedgeHog Race:" + 
				"\n\nBeginning Stage:" + 
				"\n   Each player places a hedgehog in the first row until all their hedgehogs are placed." + 
				"\n   Hedgehogs can't be stacked until all other rows are filled." +
				
				"\n\nMovement Stage:" + 
				"\n   Each player has the opportunity to make a sideways move before moving a hedgehog forward." +
				"\n   A hedgehog in the highlighted row MUST move forward, even if it doesn't belong to the current player." + 
				"\n   If you don't want to move sideways, move a token forward or click the 'Skip Sideways Move' button." +
				"\n   The goal is to reach the cake, but watch out for obstacles!" +
				
				"\n\nObstacles:" +
				"\n   Pits: a hedgehog in a pit can't move until it's tied for last place." +
				"\n   Black Holes: a hedgehog in a black hole is stuck there forever.  Be extra careful around them." +
				"\n   Worm Holes: a wormhole will randomly transport a hedgehog to another location on the board." + 
				"\n   Origins: an origin pit will bounce a hedgehog back to the beginning of the board." +
				
				"\n\nHave Fun!";
				JOptionPane.showMessageDialog(null, ruleListing, "Rules", JOptionPane.PLAIN_MESSAGE);
			}
			if (ae.getSource() == gameMenu.getAbout()) {
				String about = "The Great Hedgehog Cake Race" +
					"\n Create by: Joshua Kovach" +
					"\n Version: 1.0" +
					"\n Release Date: 10 December 2009" +
					"\n Description: " +
					"\n    Teams of hedgehogs race through a field of terrifying " +
					"\n    obstacles to reach a bountiful feast of their favorite food:" +
					"\n\n                              CAKE!\n" +
					"\n    It's your job to lead your team of hedgehogs to victory.  Use" +
					"\n    the environment to your advantage, and to your opponent's demise." +
					"\n\n" +
					"\n Special thanks to:" +
					"\n    Erin Steigmeyer: Hedgehog Concept Design" + 
					"\n    and Cake, which doesn't always have to be a lie.";
				JOptionPane.showMessageDialog(null, about, "About", JOptionPane.PLAIN_MESSAGE);
			}
		}
	}
	
	/** Method main: <br />
	 * Enables this class as runnable, and creates the game window.
	 * @param args
	 */
	public static void main(String[] args) {
		GameFrame game = new GameFrame();
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
