package viewer;

import java.awt.event.*;

import javax.swing.*;

/** Class OptionPanel: <br />
 * Creates a JPanel containing drop down menus and gameboard interaction buttons.
 * @author Joshua Kovach
 * @version 1.0
 *
 */
public class OptionPanel extends JPanel implements ActionListener {
	/**
	 * again with that serial error...
	 */
	private static final long serialVersionUID = 6814531378744666174L;
	/* because drop down menus are so much better for player input */
	private JLabel playerSelectLabel;						//Player Count
	private JComboBox playerSelect;							//Drop-down menu
	private Integer[] playerCounts = { 2, 3, 4, 5, 6 };		//contents of the drop down menu
	
	private JLabel tokenSelectLabel;							//Token Count
	private JComboBox tokenSelect;								//Drop down menu for tokens
	private Integer[] tokenCounts = { 2, 3, 4, 5, 6, 7, 8 };	//Contents of the drop down menu
	
	private JLabel winSelectLabel;								//Tokens to win
	private JComboBox winSelect;								//Drop down menu for tokens
	private Integer[] winCounts = { 1, 2, 3, 4, 5, 6, 7, 8 };	//contents of the drop down menu
	
	private JLabel sizeSelectLabel;								//Size
	private JComboBox sizeSelect;								//drop down menu for rows
	private Integer[] boardSizes = { 4, 5, 6, 7, 8, 9, 10 };	//content of rows
	private JLabel sizeQualifier;								//x (rows + 3)
	
	private JLabel modeSelectLabel;								//pit mode
	private JComboBox modeSelect;								//drop down menu for pit mode
	private String[] modes = { "Pits", "Black Holes", "Worm Holes", "Origin" };	//content of drop down menu
	
	/* buttons for creating the game and some game options */
	private JButton startButton;			//Start Game or New Game
	private JButton skipSideButton;			//Skip Side Step
	
	/** Constructor: <br />
	 * Sets up the drop-down menus and the buttons
	 * in the option panel
	 */
	public OptionPanel() {
		super();
		
		//add player count selection box
		playerSelectLabel = new JLabel("Players:");
		playerSelect = new JComboBox(playerCounts);
		add(playerSelectLabel);
		add(playerSelect);
		
		//add token count selection box
		tokenSelectLabel = new JLabel("Tokens: ");
		tokenSelect = new JComboBox(tokenCounts);
		tokenSelect.addActionListener(this);
		tokenSelect.setSelectedItem(4);				//default token count
		add(tokenSelectLabel);
		add(tokenSelect);
		
		//add win count selection box
		winSelectLabel = new JLabel("To win: ");
		winSelect = new JComboBox(winCounts);
		winSelect.setSelectedItem(3);				//default win condition
		add(winSelectLabel);
		add(winSelect);
		
		//add game board size selection box
		sizeSelectLabel = new JLabel("Board Size: ");
		sizeSelect = new JComboBox(boardSizes);
		sizeSelect.setSelectedItem(6);				//default game size
		sizeSelect.addActionListener(this);
		sizeQualifier = new JLabel("x " + ((Integer)sizeSelect.getSelectedItem() + 3));
		add(sizeSelectLabel);
		add(sizeSelect);
		add(sizeQualifier);
		
		//add mode selection boxes
		modeSelectLabel = new JLabel("Pit Mode: ");
		modeSelect = new JComboBox(modes);
		modeSelect.setSelectedItem("Pits");
		add(modeSelectLabel);
		add(modeSelect);
		
		/* No default action listener
		 * must be added to interact with items beyond 
		 * scope of this class.
		 */
		startButton = new JButton("Start Game");
		add(startButton);
		
		skipSideButton = new JButton("Skip Side Move");
		skipSideButton.setVisible(false);
		add(skipSideButton);
	}

	/** Method addStartButtonListener: <br />
	 * Sets an action listener to interact with the information
	 * in this class.
	 * @param al - ActionListener
	 */
	public void addStartButtonListener(ActionListener al) {
		startButton.addActionListener(al);
		skipSideButton.addActionListener(al);
	}
	
	/** Method getStartButton(): <br />
	 * @return Start Button (for getSource)
	 */
	public JButton getStartButton() {
		return startButton;
	}
	
	/** Method getSkipSideButton: <br />
	 * @return Skip Side Move button (for source tracking)
	 */
	public JButton getSkipSideButton() {
		return skipSideButton;
	}
	
	/** Method getPlayerCount(): <br />
	 * @return int number of players selected
	 */
	public int getPlayerCount() {
		return (Integer) playerSelect.getSelectedItem();
	}
	
	/** Method getTokenCount(): <br />
	 * @return int number of tokens selected per player
	 */
	public int getTokenCount() {
		return (Integer) tokenSelect.getSelectedItem();
	}
	
	/** Method getWinCount(): <br />
	 * @return int number of tokens required to win
	 * ensures that the win count is less than or equal to the number of tokens
	 */
	public int getWinCount() {
		if ((Integer) winSelect.getSelectedItem() > (Integer) tokenSelect.getSelectedItem()) {
			winSelect.setSelectedItem((Integer) tokenSelect.getSelectedItem() - 1);
		}
		return (Integer) winSelect.getSelectedItem();
	}
	
	/** Method getBoardSiz(): <br />
	 * @return int number of rows in the game board.
	 */
	public int getBoardSize() {
		return (Integer) sizeSelect.getSelectedItem();
	}
	
	/** Method getMode: <br />
	 * @return int mode identifier
	 * use x.PITS, x.BLACK_HOLES, x.WORM_HOLES, x.ORIGINS to test result
	 */
	public int getMode() {
		return modeSelect.getSelectedIndex();
	}
	
	/** Method ActionPerformed(): <br />
	 * listens for changes to update on the various option boxes
	 */
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == tokenCounts) {
			winCounts = new Integer[(Integer) tokenSelect.getSelectedItem()];
			for (int i = 0; i < winCounts.length; i++) {
				winCounts[i] = i + 1;
			}
			winSelect.setModel(new DefaultComboBoxModel(winCounts));
		}
		
		if (ae.getSource() == sizeSelect) {
			sizeQualifier.setText("x " + ((Integer)sizeSelect.getSelectedItem() + 3));
		}
	}
}
