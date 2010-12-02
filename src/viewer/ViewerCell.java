package viewer;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

import java.util.ArrayList;


/** Class ViewerCell: <br />
 * @author Joshua Kovach
 * @version 1.0
 * Extends JPanel. Provides a display for an individual cell 
 * of a grid, 
 *
 */
public class ViewerCell extends JPanel {
	
	/**
	 * gets rid of that warning
	 */
	private static final long serialVersionUID = 2290527077051726114L;
	
	/** holds the icon indicating the contents of the cell */
	private JLabel backgroundCell;
	private JLabel cellImage;
	/** transparent .png that defines the size of a cell */
	private ImageIcon standardBackgroundImage = ImageLoader.loadIcon("standardEnabledCell.png");
	private ImageIcon standardFinishedBackgroundImage = ImageLoader.loadIcon("finishedCell.png");
	private ImageIcon [] backgroundImage = { ImageLoader.loadIcon("standardPit.png"), 
			ImageLoader.loadIcon("blackHole.png"),
			ImageLoader.loadIcon("wormHole.png"),
			ImageLoader.loadIcon("origin.png") };
			
	
	/** holds the hidden stack indicators */
	private JPanel leftPanel;
	/** hidden stack indicators */
	private ArrayList<JPanel> hiddenColor;
	
	/** Movement buttons */
	private JButton upButton, downButton, forwardButton;
	
	/** borders */
	private Border hiliteBorder, stdBorder;
	
	/** Constructor: <br />
	 * Sets up the fields and the layout of each cell.
	 */
	public ViewerCell(int pitMode) {

		super.setLayout(new BorderLayout());
		
		backgroundCell = new JLabel();
		setBackgroundImage(pitMode);
		add(backgroundCell, BorderLayout.CENTER);
		this.setComponentZOrder(backgroundCell, 0);
		
		cellImage = new JLabel();
		
		leftPanel = new JPanel(new GridLayout());
		leftPanel.setSize(new Dimension(12, 70));
		add(leftPanel, BorderLayout.WEST);
		
		hiddenColor = new ArrayList<JPanel>();
		
		upButton = new JButton("UP");
		downButton = new JButton("DOWN");
		forwardButton = new JButton(">");
		add(upButton, BorderLayout.NORTH);
		add(downButton, BorderLayout.SOUTH);
		add(forwardButton, BorderLayout.EAST);
		
		stdBorder = BorderFactory.createLineBorder(Color.black);
		setBorder(stdBorder);
	}
	
	/** Method setBackgroundImage: <br />
	 * sets the background image of the cell based on
	 * pit type and cell status
	 * @param cellType FINISHED(-2), STANDARD(-1),
	 * PITS(0), BLACK_HOLES(1), WORM_HOLES(2), or ORIGINS(3)
	 */
	public void setBackgroundImage(int cellType) {
		if (cellType == -1) {
			backgroundCell.setIcon(standardBackgroundImage);
		}
		else if (cellType == -2) {
			backgroundCell.setIcon(standardFinishedBackgroundImage);
		}
		else {
			backgroundCell.setIcon(backgroundImage[cellType]);
		}
	}
	
	/** Method setHiddenColor: <br />
	 * Adds a token of the color of the first hidden token on the stack
	 * to the left area of the cell.
	 * @param color - player color of the first hidden token
	 */
	public void setHiddenColor(Color color) {
		hiddenColor.add(new JPanel());
		hiddenColor.get(hiddenColor.size() - 1).setBackground(color);
		hiddenColor.get(hiddenColor.size() -1).setBorder(stdBorder);
		leftPanel.add(hiddenColor.get(hiddenColor.size() - 1), BorderLayout.WEST);
		setComponentZOrder(leftPanel, 0);
		this.repaint();
	}
	
	/** Method resetHiddenColor: <br />
	 * Removes the first hidden token from the hidden stack viewer area.
	 */
	public void resetHiddenColor() {
		if (hiddenColor.size() >= 1) {
			leftPanel.remove(hiddenColor.get(hiddenColor.size() - 1));
			hiddenColor.remove(hiddenColor.size() - 1);
			this.repaint();
		}		
		System.out.println("ArrayList hiddenColor count: " + hiddenColor.size());
	}
	
	/** Method setStdBorder: <br />
	 * sets the border of the cell to the standard black
	 */
	public void setStdBorder() {
		setBorder(stdBorder);
		this.repaint();
	}
	
	/** Method setHiLiteBorder: <br />
	 * highlights the cell border in the color of the current player
	 * @param color - color of the current player
	 */
	public void setHiLiteBorder(Color color) {
		hiliteBorder = BorderFactory.createLineBorder(color, 3);
		setBorder(hiliteBorder);
		this.repaint();
	}
	
	/** Method addButtonListeners: <br />
	 * Adds action listeners to the movement buttons in the cell
	 * @param al - action listener from the game controller
	 */
	public void addButtonListeners(ActionListener al) {
		upButton.addActionListener(al);
		downButton.addActionListener(al);
		forwardButton.addActionListener(al);
	}
	
	/** Method enableMoveUp: <br />
	 * enables and makes visible the up button
	 */
	public void enableMoveUp() {
		upButton.setVisible(true);
		setComponentZOrder(upButton, 0);
	}
	
	/** Method enableMoveDown: <br />
	 * enables and makes visible the down button
	 */
	public void enableMoveDown() {
		downButton.setVisible(true);
		setComponentZOrder(downButton, 0);
	}
	
	/** Method enableMoveForward: <br />
	 * enables and makes visible the forward button
	 */
	public void enableMoveForward() {
		forwardButton.setVisible(true);
		setComponentZOrder(forwardButton, 0);
	}
	
	/** Method disableSideMoves: <br />
	 * disables and makes invisible the up and down buttons
	 */
	public void disableSideMoves() {
		upButton.setVisible(false);
		downButton.setVisible(false);
	}
	
	/** Method disableAllMoves: <br />
	 * disables and makes invisible all movement buttons
	 */
	public void disableAllMoves() {
		forwardButton.setVisible(false);
		upButton.setVisible(false);
		downButton.setVisible(false);
	}

	/** Method getUpButton: <br />
	 * @return the up button (for source tracking)
	 */
	public JButton getUpButton() {
		return upButton;
	}
	
	/** Method getDownButton: <br />
	 * @return the down button (for source tracking)
	 */
	public JButton getDownButton() {
		return downButton;
	}
	
	/** Method getForwardButton: <br />
	 * @return the forward button (for source tracking)
	 */
	public JButton getForwardButton() {
		return forwardButton;
	}
	
	/** Method setCellImage: <br />
	 * Sets the image of the cell to represent the player at the top of the stack
	 * @param image
	 */
	public void setCellImage(ImageIcon image) {
		cellImage.setIcon(image);
		add(cellImage, BorderLayout.CENTER);
		setComponentZOrder(cellImage, 0);
		cellImage.setHorizontalAlignment(SwingConstants.CENTER);
		this.repaint();
	}
	
	/** Method resetCellImage: <br />
	 * Sets the cell image to represent an empty cell
	 */
	public void resetCellImage() {
		cellImage.setIcon(null);
		remove(cellImage);
		this.repaint();
	}
}
