public class Go {
	public static class Game {

		// 0 - Black, 1 - White
		private int currentPlayer = 0;
		private boolean playerHasPassed = false;

		private int grid[][];

		public Game(int size) {
			this.size = size;

			this.grid = new int[size][size];
			for(int i = 0; i < size; i++) {
				for(int j = 0; j < size; j++) {
					this.grid[i][j] = -1;
				}
			}
		}

		public int get(int x, int y) {
			return grid[x][y];
		}

		public void switchPlayer() {
			currentPlayer = (currentPlayer == 0) ? 1 : 0;
		}

		// Returns true if piece was able to be placed
		public boolean placePiece(int x, int y) {
			if(grid[x][y] != 0) {
				return false;
			}
			grid[x][y] = currentPlayer;
			switchPlayer();
			return true;
		}

		public boolean pass() {
			if(playerHasPassed) {
				scoreGame();
			}
			playerHasPassed = true;
			switchPlayer();
		}

		// Return tuple with respective player scores 
		public int[] scoreGame() {

		}
	}

	public static void main(String args[]) {

	}
}