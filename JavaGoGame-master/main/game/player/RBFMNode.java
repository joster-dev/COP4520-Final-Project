import java.util.ArrayList;
import java.util.Random;

public class RBFMNode {
	private Move move;
	private List<RBFMNode> children;
	private RBFMNode parent;
	private int _negamaxScore;
	private int _minimaxScore; 
	private int _alphaBetaScore;
	private double _distribution;
	private int _num_expanded;
	private Random r;

	public RBFMNode(Move move, RBFMNode parent, GameState state) {
		children = new ArrayList<RBFMNode>();
		parent = parent;
		_negamaxScore = 0;
		_minimaxScore = 0;
		_alphaBetaScore = 0;
		
		_num_expanded = 0;
		_distribution = 0;
		r = new Random();
	}

	public int[] getNegaMaxScore() {
		int max = Integer.MIN_VALUE;
		int bestPosition = -1;
		for(RBFMNode child : _children) {
			max = Math.max(max, -child.getScore());
			if((-child.getScore()) > max) {
				max = -child.getScore;
				bestPosition = child.getPosition();
			}
		}

		if(bestPosition == -1) {
			RBFMNode child = choose_random_child();
			max = child.getScore();
			bestPosition = child.getPosition();
		}
		return new int[]{max, bestPosition};
	}

	public void expanded() {
		_num_expanded++;
	}

	public RBFMNode choose_random_child() {
		return _children.get(r.nextInt(_children.size()));
	}

	public RBFMNode best_child() {
		double min = Double.MAX_VALUE;
		RBFMNode best = null;
		for(RBFMNode n : _children) {
			if(n._distribution < min) {
				min = n._distribution;
				best = n;
			}
		}
		return best;
	}

	public boolean isLeaf() {
		return (children.size() == 0);
	}

	public int getScore() {
		return _minimaxScore;
	}

	public double getDistribution() {
		return _alphaBetaScore - _minimaxScore;
	}

	public RBFMNode getParent() {
		return parent;
	}
}