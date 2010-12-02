package application;

import java.awt.Color;
import javax.swing.ImageIcon;

import viewer.ImageLoader;

/** Class Player: <br />
 * Player class that allows for up to six players with automatically defined
 * ID colors.
 * @author Joshua Kovach
 * @version 1.0
 *
 */
public class Player {
	private int playerID;
	private Color[] colorOptions = { Color.red, Color.green, Color.blue, Color.orange, Color.magenta, Color.yellow };
	private String[] colorNames = { "Red", "Green", "Blue", "Orange", "Magenta", "Yellow" };
	private Color playerColor;
	private String[] playerImageName = {"pI1.png", "pI2.png", "pI3.png", "pI4.png", "pI5.png", "pI6.png", "pI.png" };
	private ImageIcon playerImage;
	
	public Player(int newID) {
		setPlayerID(newID);
		setPlayerColor(playerID);
		
	}
	
	/** Method getPlayerID: <br />
	 * Returns the player ID integer value.
	 * @return int player identification number
	 */
	public int getPlayerID() {
		return playerID;
	}
	
	/** Method setPlayerID: <br />
	 * Allows the client to define up to six unique players.
	 * @param newPlayerID - between zero and 5 (inclusive)
	 */
	public void setPlayerID(int newPlayerID) {
		if (newPlayerID < 0 || newPlayerID > 5) {
			System.out.println("Error: Invalid Player ID");
		}
		else playerID = newPlayerID;
	}
	
	/** Method getPlayerColor: <br />
	 * @return Color player color
	 */
	public Color getPlayerColor() {
		return playerColor;
	}
	
	/** Method getPlayerColorName: <br />
	 * @return String the name of the player's color
	 */
	public String getPlayerColorName() {
		return colorNames[playerID];
	}
	
	/** Method setPlayerColor: <br />
	 * @param playerID Sets the color and image icon of the player
	 */
	public void setPlayerColor(int playerID) {
		playerColor = colorOptions[playerID];
		playerImage = ImageLoader.loadIcon(playerImageName[playerID]);
	}
	
	/** Method getPlayerImage: <br />
	 * @return ImageIcon icon representing the player
	 */
	public ImageIcon getPlayerImage() {
		return playerImage;
	}
	
	/** Method toString: <br />
	 * returns the player ID and the player color
	 */
	public String toString() {
		return ("Player ID: " + playerID + "\nPlayer Color: " + getPlayerColorName());
	}
}