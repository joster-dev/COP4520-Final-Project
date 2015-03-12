import java.util.*;

public class Go {

	public static boolean game = true;
	public static int[][] grid;
	public static int humanCaptured = 0;
	public static int botCaptured = 0;
	public static int size = 5;

	public static void reset() {
		grid = new int[size][size];

		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				grid[i][j] = -1;
			}
		}
	}

	public static void printGrid(int[][] grid) {
		for(int i = 0; i < size; i++) {
			System.out.print("--\t");
		}
		System.out.print("\n");
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				System.out.print(grid[i][j] + "\t");
			}
			System.out.print("\n");
		}
		for(int i = 0; i < size; i++) {
			System.out.print("--\t");
		}
		System.out.print("\n");
	}

	public static boolean score(int[][] grid) {
		ArrayList<String> checked = new ArrayList<String>();

		for(int i = 0; i < size; i++) {
			found:
			for(int j = 0; j < size; j++) {
				for(int k = 0; k < checked.size(); k++) {
					String[] parts = checked.get(k).split(" ");
					int x = Integer.parseInt(parts[0]);
					int y = Integer.parseInt(parts[1]);
					if(i == x && j == y) {
						break found;
					}
				}
				// If we haven't counted these cells, find a group
				if(grid[i][j] == -1) {

				}
			}
		}
		return true;
	}

	public static void botMove(int[][] board) {

		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				if(board[i][j] == -1) {
					board[i][j] = 1;
					printGrid(board);
					return;
				}
			}
		}
	}

	public static void main(String args[]) {

		try {
			if(args.length > 0) {
				size = Integer.parseInt(args[0]); 
			}
		} catch (NumberFormatException e) { }

		reset();

		while(game) {
			System.out.println("Enter coordinates ---> x y <---");
			String input = System.console().readLine();
			String[] parts = input.split(" ");
			if(parts.length == 2) {
				int x = Integer.parseInt(parts[0]);
				int y = Integer.parseInt(parts[1]);
				if(x >= 0 && y >= 0 && x < size && y < size) {
					if(grid[x][y] == -1) {
						grid[x][y] = 0;
						printGrid(grid);
						botMove(grid);
					}
				}
			}
		}
	}
}