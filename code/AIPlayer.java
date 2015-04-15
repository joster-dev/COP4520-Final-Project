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
		_depth = 6;
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
		int[] pos = minimax(_depth, Board.WHITE, board, _root);
		
		int a = minimax(_depth,Board.WHITE,_root,-INF,+INF);
		
		System.out.println(pos[0] + " vs " + a);
		
		RBFM randomBFM = new RBFM(_root);
		randomBFM.root_decision();
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
						
						int[] arr = minimax(depth-1, Board.BLACK, gameBoard.getBoard(), child);
						currentScore = arr[0];
						child.setScore(currentScore);
						child.setBoard((Board)gameBoard.getBoard().clone());
						child.setPlayer(player);
						children.add(child);
						
						
					}
					else{	
						
						Node child = new Node(root, Math.max(score, bestScore), move);
						child.setBoard((Board)gameBoard.getBoard().clone());
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

						child.setBoard((Board)gameBoard.getBoard().clone());
						child.setPlayer(player);
						children.add(child);
					
					}
					else{
						// end state reached
						// add child to list
						
						Node child = new Node(root, Math.min(score, bestScore), move);
						child.setBoard((Board)gameBoard.getBoard().clone());
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
			
		if (children.size() != 0){
			root.setChildren(children);
			for (Node c : children){
				if (player == Board.WHITE)
				{
					if (c.getScore() > bestScore){
						bestScore = c.getScore();
						bestPosition = c.getPosition();
					}
				}
				else{
					if (c.getScore() < bestScore){
						bestScore = c.getScore();
						bestPosition = c.getPosition();
					}
				}
				
			}
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
	
	public static int minimax(int depth, char player, Node root, int alpha, int beta) throws CloneNotSupportedException
	{			
		// Create array to hold all new children of current root
		ArrayList<Node> children = root.getChildren();
		
		// AI must maximize score, whereas, opponent must minimize score
		int bestScore = (player == Board.BLACK) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
		int currentScore;
		int bestPosition = -1;
		
		if (depth != 0 && !children.isEmpty())
		{
			for (int i=0;i<children.size();i++)
			{
				Node child = children.get(i);
				
				// try this move for the current player
				int score = child.getScore();
				
				if (player == Board.WHITE)
				{
					// end state not reached, generate new states
					if (score==0){	
						currentScore = minimax(depth-1, Board.BLACK, child, alpha,beta);				
						child.setAlphaBetaScore(currentScore);

					}
					else{	
						child.setAlphaBetaScore(score);
					}
					
				}
				else
				{
					// end state not reached, generate new states
					if (score==0){	
						currentScore = minimax(depth-1, Board.WHITE, child, alpha,beta);				
						child.setAlphaBetaScore(currentScore);

					}
					else{	
						child.setAlphaBetaScore(score);
					}
					
				}

			}
		}

			
		if (children.size() != 0){
			root.setChildren(children);
			for (Node c : children){
				if (player == Board.WHITE)
				{
					if (c.getAlphaBetaScore() > alpha){
						bestScore = c.getScore();
						bestPosition = c.getPosition();
					}
				}
				else{
					if (c.getAlphaBetaScore() < beta){
						bestScore = c.getScore();
						bestPosition = c.getPosition();
					}
				}
				
			}
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

		return bestScore;
		
	}	


}
