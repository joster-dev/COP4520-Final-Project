import java.util.*;
public class AIPlayer extends RBFM{

	private Board board;
	private int depth;
	private RBFM randomBFM;
	public AIPlayer(Board b)
	{
		board = b;
		depth = 3;
		randomBFM = new RBFM();
	}
	
	public int move()
	{
		// get next best move for computer
		int[] result = minimax(2, board.WHITE, new int[]{board.getBlacksCaptured(),board.getWhiteCaptured()});
		return result[1];
	}
	
	private ArrayList<Integer> generateMoves()
	{
		char[] b = board.board;
		ArrayList<Integer> moves = new ArrayList<Integer>();
		for (int i=0; i<b.length;i++){
			if (b[i]==Board.EMPTY)
				moves.add(i);
		}
		return moves;
	}
	
	private int[] minimax(int depth, char player, int[] originalScores)
	{
		
		ArrayList<Integer> nextMoves = generateMoves();
		
		int bestScore = (player == Board.WHITE) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
		int currentScore;
		int bestPosition = -1;
		
		if (nextMoves.isEmpty() || depth == 0)
		{
			bestScore = evaluate(originalScores);
		}
		else
		{
			for (int move : nextMoves)
			{
				// try this move for the current player
				board.board[move]=player;
				if (player == Board.WHITE)
				{
					currentScore = minimax(depth-1, Board.BLACK, originalScores)[0];
					
					if (currentScore > bestScore)
					{
						bestScore = currentScore;
						bestPosition = move;
					}
					else
					{
						currentScore = minimax(depth -1, Board.WHITE, originalScores)[0];
						if (currentScore < bestScore)
						{
							bestScore = currentScore;
							bestPosition = move;
						}
					}
					
					// undo move
					board.board[move] = Board.EMPTY;
				}
			}
		}
			
		return new int[] {bestScore, bestPosition};
		
	}
	
	public int evaluate(int[] originalScores){
		int blackCaptured = board.getBlacksCaptured()-originalScores[0];
		int whiteCaptured = board.getWhiteCaptured()-originalScores[1];
		
		if (blackCaptured > whiteCaptured) 
			return -10;
		else if (blackCaptured < whiteCaptured)
			return 10;
		else return 0;
	}
	
	

}
