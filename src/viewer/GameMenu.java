package viewer;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.KeyEvent;

/** Class GameMenu: <br />
 * Holds the structure of the drop down menu bar for the game 
 * The Great Hedgehog Cake Race.  Contains the following structure:
 * Game -> New Game
 * Game -> Quit Game
 * Help -> Rules
 * Help -> About
 * @author Joshua Kovach
 * @version 1.0
 *
 */
public class GameMenu extends JMenuBar {
	/**
	 * gets rid of that weird error again
	 */
	private static final long serialVersionUID = 1L;
	private JMenu game;
	private JMenu help;
	
	private JMenuItem newGame, quitGame;
	private JMenuItem rules, about;
	
	/** Default Constructor: <br />
	 * Creates the game structure of menus with mnemonic keys assigned.
	 */
	public GameMenu() {
		game = new JMenu("Game");
		game.setMnemonic(KeyEvent.VK_G);
		help = new JMenu("Help");
		help.setMnemonic(KeyEvent.VK_H);
		add(game);
		add(help);
		
		newGame = new JMenuItem("New Game");
		newGame.setMnemonic(KeyEvent.VK_N);
		quitGame = new JMenuItem("Quit Game");
		quitGame.setMnemonic(KeyEvent.VK_Q);
		game.add(newGame);
		game.add(quitGame);
		
		rules = new JMenuItem("Rules");
		rules.setMnemonic(KeyEvent.VK_R);
		about = new JMenuItem("About");
		about.setMnemonic(KeyEvent.VK_A);
		help.add(rules);
		help.add(about);
	}
	
	/** Method getNewGame: <br />
	 * Allows the game class to assign actions to the 
	 * "New Game" menu item
	 * @return JMenuItem New Game
	 */
	public JMenuItem getNewGame() {
		return newGame;
	}
	
	/** Method getQuitGame: <br />
	 * Allows the game class to assign actions to the
	 * "Quit Game" menu item
	 * @return JMenuItem Quit Game
	 */
	public JMenuItem getQuitGame() {
		return quitGame;
	}
	
	/** Method getRules: <br />
	 * Allows the game class to assign actions to the 
	 * "Rules" menu item
	 * @return JMenuItem Rules
	 */
	public JMenuItem getRules() {
		return rules;
	}
	
	/** Method getAbout: <br />
	 * Allows the game class to assign actions to the 
	 * "About" menu item
	 * @return JMenuItem About
	 */
	public JMenuItem getAbout() {
		return about;
	}

}
