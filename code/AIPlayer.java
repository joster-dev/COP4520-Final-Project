import java.util.ArrayList;
import java.util.Random;

/**
 * This AIPlayer class is used whenever it is the AI's turn the move in the game
 * @author Cindy
 *
 */
public class AIPlayer{
	private Node _root;
	private int _depth;
	private int INF = Integer.MAX_VALUE;
	public AIPlayer(){
		_depth = 5;
	}
	
	/**
	 * 
	 * @param board - current board configuration
	 * @return
	 * @throws CloneNotSupportedException 
	 */
	public int move(Board board) throws CloneNotSupportedException
	{
		_root = new Node(null);
		
		//  run minimax algorithm
		minimax(_depth, Board.WHITE, board, _root);
		
		// compute alpha beta scores
		minimax(_depth, Board.WHITE, board, _root, -INF, +INF);
		
		// run rbfm algorithm using minimax tree
		RBFM randomBFM = new RBFM(_root);
		randomBFM.root_decision();
		
		// return best position
		return randomBFM.getRoot().getPosition();
	}
	
	private static ArrayList<Integer> generateMoves(char[] board)
	{
		// get all possible moves for this particular board configuration
		ArrayList<Integer> moves = new ArrayList<Integer>();
		
		for (int i=0;i<board.length;i++){
			if (board[i] == Board.EMPTY)
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
	 * @throws CloneNotSupportedException 
	 */
	public static int[] minimax(int depth, char player, Board board, Node root) throws CloneNotSupportedException
	{	
		// Get all possible moves
		ArrayList<Integer> nextMoves = generateMoves(board.board);
		
		// Create array to hold all new children of current root
		ArrayList<Node> children = new ArrayList<Node>();
		
		// AI must maximize score, whereas, opponent must minimize score
		int bestScore = (player == Board.BLACK) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
		int currentScore;
		int bestPosition = -1;
		if (depth != 0 && !nextMoves.isEmpty())
		{
			for (int move : nextMoves)
			{
				// create board for this sequence of moves
				AIGameBoard gameBoard = new AIGameBoard((Board)board.clone());
				
				// try this move for the current player
				int score = gameBoard.turn(player, move);
				
				if (player == Board.WHITE)
				{
					// end state not reached, generate new states
					if (score==0){
						// Create child for root
						Node child = new Node(root);
						child.setPosition(move);
						
						int[] arr = minimax(depth-1, Board.BLACK, (Board)gameBoard.getBoard().clone(), child);
						currentScore = arr[0];
						child.setScore(currentScore);
						child.setBoard((Board)board.clone());
						child.setPlayer(player);
						children.add(child);
						
						if (currentScore > bestScore)
						{
							bestScore = currentScore;
							bestPosition = arr[1];
						}
						
					}
					else{	
						
						if (score > bestScore)
						{
							bestScore = score;
							bestPosition = move;
						}
						
						Node child = new Node(root, score, move);
						child.setBoard((Board)board.clone());
						child.setPlayer(player);
						children.add(child);
					}
					
				}
				else
				{
					// end state not reached, generate new states
					if (score==0)
					{
						// Create child for root
						Node child = new Node(root);
						int[] arr =  minimax(depth-1, Board.WHITE, gameBoard.getBoard(), child);
						currentScore = arr[0];
						child.setPosition(move);
						child.setScore(currentScore);

						child.setBoard((Board)board.clone());
						child.setPlayer(player);
						children.add(child);
						
						if (currentScore < bestScore)
						{
							bestScore = currentScore;
							bestPosition = arr[1];
						}
					}
					else{
						if (score < bestScore)
						{
							bestScore = score;
							bestPosition = move;
						}
						// end state reached
						// add child to list
						Node child = new Node(root, score, move);
						child.setBoard((Board)board.clone());
						child.setPlayer(player);
						children.add(child);
					}
					
				}

			}
		}
		else{
			// leaf node
			// store board so that it can be expanded on later
			root.setBoard((Board)board.clone());
			root.setPlayer(player);
		}
			
		if (bestPosition != -1)
		{
			// store values in root
			root.setScore(bestScore);
			root.setPosition(bestPosition);
			root.setChildren(children);
		}
		// Best position was not chosen because they all have the same minimax score
		// Choose random child
		else if (children.size() != 0 && bestPosition == -1)
		{
			root.setChildren(children);
			Node child = root.choose_random_child();
			root.setPosition(child.getPosition());
			root.setScore(child.getScore());
		}
		else if (children.size() != 0){
			root.setChildren(children);
		}
		else if (bestScore == Integer.MIN_VALUE && player == Board.WHITE)
		{
			bestScore = Math.max(root.getScore(), bestScore);
			bestPosition = root.getPosition();
		}
		else if(bestScore == Integer.MAX_VALUE && player == Board.BLACK)
		{
			bestScore = Math.min(root.getScore(), bestScore);
			bestPosition = root.getPosition();
		}

		return new int[]{bestScore,bestPosition};
		
	}	
	
	public static int minimax(int depth, char player, Board board, Node root, int alpha, int beta) throws CloneNotSupportedException
	{			
		// Create array to hold all new children of current root
		ArrayList<Node> children = root.getChildren();
		
		// AI must maximize score, whereas, opponent must minimize score
		int bestScore = (player == Board.BLACK) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
		int currentScore;
		int bestPosition = -1;
		
		if (depth != 0 && !children.isEmpty())
		{
			for (Node c : children)
			{
				int move = c.getPosition();
				
				// create board for this sequence of moves
				AIGameBoard gameBoard = new AIGameBoard((Board)board.clone());
				
				// try this move for the current player
				int score = gameBoard.turn(player, move);
				
				if (player == Board.WHITE){
					// end state not reached, generate new states
					if (score==0){
						
						currentScore = minimax(depth-1, Board.BLACK, (Board)gameBoard.getBoard().clone(), c, alpha, beta);
						
						if ( currentScore > alpha){
							alpha = currentScore;
							bestPosition = move;
						}
						
					}
					else{
						if ( score > alpha){
							alpha = score;
							bestPosition = move;
						}
					}
					
				}
				else{
					// end state not reached, generate new states
					if (score==0){
						// Create child for root
						currentScore = minimax(depth-1, Board.WHITE, gameBoard.getBoard(), c, alpha, beta);
						
						if (currentScore < beta){
							beta = currentScore;
							bestPosition = move;
						}
					}
					else{
						
						if (score < beta){
							beta = score;
							bestPosition = move;
						}
						
					}
					
					if (alpha >= beta) break;
					
				}

			}
		}
		
		bestScore =  (player == Board.BLACK) ? beta : alpha;
			
		if (bestPosition != -1)
		{
			root.setAlphaBetaScore(bestScore);
		}
		else if (children.size() != 0 && bestPosition == -1)
		{
			root.setAlphaBetaScore(root.getScore());
		}

		return bestScore;
		
	}	


}
