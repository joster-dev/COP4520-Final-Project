/**
 * This game board is used by the AI to simulate game play starting
 * at a particular board configuration
 * @author Cindy
 *
 */

public class AIGameBoard{
	
	private Board _board;
	
	public AIGameBoard(Board board){
		
		// store copy of current board
		_board = board;
	}
	
	public Board getBoard(){
		return _board;
	}
	
	/**
	 * @param player - current player
	 * @param position - position that the current player would like to move to
	 * @return minimax score gained at a particular position
	 */
	public int turn(char player, int position){
		
		// store the original score before a piece is added to the board
		int originalScore = (player == _board.BLACK) ? _board.getBlacksCaptured() : _board.getWhiteCaptured();
		
		// place stone at this position
		_board.add_stone(player, position);
		
		// check if pieces were captured by player or opponent
		int newScore = (player == _board.BLACK) ? _board.getBlacksCaptured() : _board.getWhiteCaptured();
		
		int score = 0;
		
		
		// black player is the opponent
		// opponent wants to minimize the AI's score
		if (player == _board.BLACK && (newScore > originalScore)){
			score = -10;
		}
		// white player is the AI
		// need to maximize score
		else if((player == _board.WHITE) && (newScore > originalScore)){
			score = 10;
		}

		return score;
	}

}