package game.player;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import app.AppRunner;
import app.GameState;
import enums.MoveType;
import enums.PlayerColor;
import game.model.Move;

public class RBFMNode {
	private Move move;
	private ArrayList<RBFMNode> children;
	private RBFMNode parent;
	private int _negamaxScore;
	private int _minimaxScore; 
	private int _alphaBetaScore;
	private double _distribution;
	private int _num_expanded;
	private Random r;
	private PlayerColor playerJustMoved;
	private Set<Move> untriedMoves;

	public RBFMNode(Move move, RBFMNode parent, GameState state) {
		this.move = move;
		this.parent = parent;
		children = new ArrayList<RBFMNode>();
		_negamaxScore = 0;
		_minimaxScore = 0;
		_alphaBetaScore = 0;
		
		_num_expanded = 0;
		_distribution = 0;
		playerJustMoved = state.getLastMoved().getColor();
		untriedMoves = state.getPossibleMoves(PlayerColor.BLACK.equals(playerJustMoved) ? PlayerColor.WHITE : PlayerColor.BLACK);
		r = new Random();
	}

	public int[] getNegaMaxScore() {
		int max = Integer.MIN_VALUE;
		int bestPosition = -1;
		for(RBFMNode child : children) {
			max = Math.max(max, -child.getScore());
			if((-child.getScore()) > max) {
				// max = -child.getScore;
				// bestPosition = child.getPosition();
			}
		}

		if(bestPosition == -1) {
			RBFMNode child = choose_random_child();
			max = child.getScore();
			//bestPosition = child.getPosition();
		}
		return new int[]{max, bestPosition};
	}

	public void expanded() {
		_num_expanded++;
	}

	public RBFMNode addChild(Move move, GameState state) {
		untriedMoves.remove(move);
		RBFMNode node = new RBFMNode(move, this, state);
		children.add(node);
		return node;
	}

	public RBFMNode choose_random_child() {
		return children.get(r.nextInt(children.size()));
	}

	public RBFMNode best_child() {
		double min = Double.MAX_VALUE;
		RBFMNode best = null;
		for(RBFMNode n : children) {
			if(n._distribution < min) {
				min = n._distribution;
				best = n;
			}
		}
		return best;
	}

	public void setParent(RBFMNode parent) {
		this.parent = parent;
	}

	public RBFMNode getParent() {
		return parent;
	}

	public boolean isLeaf() {
		return (children.size() == 0);
	}

	public Move getMove() {
		return move;
	}

	public void setMove(Move move) {
		this.move = move;
	}

	public void setScore(int score) {
		_minimaxScore = score;
	}

	public int getScore() {
		return _minimaxScore;
	}

	public double getDistribution() {
		return _alphaBetaScore - _minimaxScore;
	}

	public ArrayList<RBFMNode> getChildren() {
		return children;
	}

	public Set<Move> getUntriedMoves() {
		return untriedMoves;
	}
}