import java.util.ArrayList;

/**
 * This AIPlayer class is used whenever it is the AI's turn the move in the game
 * @author Cindy
 *
 */
public class AIPlayer{

	private int _depth;
	public AIPlayer(){
		_depth = 5;
	}
	
	/**
	 * 
	 * @param board - current board configuration
	 * @return
	 */
	public int move(Board board)
	{
		// Need to create root of minimax tree
		Node root = new Node(null);
		
		//  run minimax algorithm
		minimax(_depth, Board.WHITE, board, root);
		
		// run rbfm algorithm using minimax tree
		RBFM randomBFM = new RBFM(root);
		
		// return best position
		return randomBFM.getRoot().getPosition();
	}
	
	private static ArrayList<Integer> generateMoves(char[] board)
	{
		// get all possible moves for this particular board configuration
		ArrayList<Integer> moves = new ArrayList<Integer>();
		for (int i=0; i<board.length; i++){
			if (board[i]==Board.EMPTY)
				moves.add(i);
		}
		return moves;
	};
	
	/**
	 * @param depth - depth at which algorithm should run to
	 * @param player - current players turn to move
	 * @param board - board that is current being played on
	 * @param root - node at which minimax is being evaluated
	 * @return best score from a particular position for the AI
	 */
	public static int minimax(int depth, char player, Board board, Node root)
	{	
		// Get all possible moves
		ArrayList<Integer> nextMoves = generateMoves(board.board);
		
		// Create array to hold all new children of current root
		ArrayList<Node> children = new ArrayList<Node>();
		
		// AI must maximize score, whereas, opponent must minimize score
		int bestScore = (player == Board.WHITE) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
		int currentScore;
		int bestPosition = -1;
		
		if (depth != 0 && !nextMoves.isEmpty())
		{
			for (int move : nextMoves)
			{
				// create board for this sequence of moves
				AIGameBoard gameBoard = new AIGameBoard(board);
				
				// try this move for the current player
				int score = gameBoard.turn(player, move);
				
				if (player == Board.WHITE)
				{
					// end state not reached, generate new states
					if (score==0){
						// Create child for root
						Node child = new Node(root);
						currentScore = minimax(depth-1, Board.BLACK, gameBoard.getBoard(), child);
						
						root.addChild(child);
						
						if (currentScore > bestScore)
						{
							bestScore = currentScore;
							bestPosition = move;
						}
						
					}
					else{	
						// end state reached
						// add child to list
						root.addChild(new Node(root, score, move));
					}
					
				}
				else
				{
					// end state not reached, generate new states
					if (score==0)
					{
						// Create child for root
						Node child = new Node(root);
						currentScore = minimax(depth-1, Board.WHITE, gameBoard.getBoard(), child);
						
						root.addChild(child);
						
						if (currentScore < bestScore)
						{
							bestScore = currentScore;
							bestPosition = move;
						}
					}
					else{
						// end state reached
						// add child to list
						children.add(new Node(root, score, move));
					}
					
				}

			}
		}
		else{
			// leaf node
			// store board so that it can be expanded on later
			root.setBoard(board);
			root.setPlayer(player);
		}
			
		// store values in root
		root.setScore(bestScore);
		root.setPosition(bestPosition);
		
		return bestScore;
		
	}	

}
