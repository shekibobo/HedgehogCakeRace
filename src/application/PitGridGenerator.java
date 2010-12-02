package application;

import java.util.Random;

/** Class PitGridGenerator: <br />
 * Creates a grid of cells containing enabled cells <true> and 
 * disabled cells <false> using the two-queens rule:
 * that is, no two disabled cells will be in the same row or the same column.
 * Also ensures that a continuous diagonal line of disabled cells is not generated.
 * @author Joshua Kovach
 * @version 1.0
 *
 */
public class PitGridGenerator {
	private static Random random = new Random();
	
	/** Method newPitGrid: <br />
	 * Creates a square grid of disabled cells and enabled cells
	 * where no two disabled cells are in the same row or column,
	 * and continuous diagonal lines (from one corner to another)
	 * are never generated.
	 * @param size - number of rows and columns
	 * @return a boolean double array of the given size
	 */
	public static boolean [][] newPitGrid(int size) {
		boolean [][] pitGrid = new boolean[size][size];
		int row;
		int col;
		boolean fail;
		int successCount;
		int diagonalConnections;
		
		boolean impassDown, impassUp;
		
		
		
		do {
			impassDown = false;
			impassUp = false;
			
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					pitGrid[i][j] = false;
				}
			}
			
			successCount = 0;
			while (successCount < size) {
				
				fail = false;
				row = random.nextInt(size);
				col = random.nextInt(size);
				
				for (int i = 0; i < size; i++) {
					if (pitGrid[i][col]) fail = true;
				}
				for (int i = 0; i < size; i++) {
					if (pitGrid[row][i]) fail = true;
				}
				if (! fail) {
					pitGrid[row][col] = true;
					successCount++;
				}
				/*
				for (int i = 0; i < size; i++) {
					for (int j = 0; j < size; j++) {
						System.out.print(pitGrid[i][j] + " ");
					}
					System.out.println("");
				}*/
			}
			
			
			//CHECK FOR IMPASSES
			diagonalConnections = 0;
			for (int i = 0; i < size; i++) {
				if ( pitGrid[0][0] && pitGrid[i][i] ) {
					diagonalConnections++;
				}
				else diagonalConnections = 0;
			}
			if (diagonalConnections == size) {
				impassDown = true;
				System.out.println("Impass generated (\\).  Reconfiguring...");
			}
			
			diagonalConnections = 0;
			for (int i = 0; i < size; i++) {
				if ( pitGrid[size - 1][0] && pitGrid[(size - 1) - i][i] ) {
					diagonalConnections++;
				}
				else diagonalConnections = 0;
			}
			if (diagonalConnections == size) {
				impassUp = true;
				System.out.println("Impass generated(/).  Reconfiguring...");
			}
		} while ( impassDown || impassUp);
		
		return pitGrid;
	}
	
	/** Method newHedgeHogGrid: <br />
	 * Returns a grid for the game Hurry Up HedgeHog
	 * @param size - int the number of rows on the board
	 * @return boolean [][] grid of enabled or disabled cells
	 */
	public static boolean [][] newHedgeHogGrid(int size) {
		boolean [][] pitGrid = newPitGrid(size);
		boolean [][] hedgeHogGrid = new boolean[size][size + 3];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (pitGrid[i][j]) {
					hedgeHogGrid[i][j + 2] = pitGrid[i][j];
				}
			}
		}

		/*
		System.out.println("Hedge Hog Grid");
		for (int i = 0; i < hedgeHogGrid.length; i++){
			for (int j = 0; j < hedgeHogGrid[i].length; j++) {
				System.out.print(hedgeHogGrid[i][j] + " ");
			}
			System.out.println("");
		}
		*/
		return hedgeHogGrid;
	}
	
	
}
