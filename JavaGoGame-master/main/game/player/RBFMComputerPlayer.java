package game.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import app.GameState;
import enums.MoveType;
import enums.PlayerColor;
import game.model.Board;
import game.model.Move;

public class RBFMComputerPlayer extends Player {
	// Other player's move
	private RBFMNode root;
	private int depth;

	public RBFMComputerPlayer(PlayerColor color) {
		super(color);
	}

	@Override
	public Move getMove(GameState state) {
		if(state.getLastMoved().equals(color)) {
			return Move.getMoveInstance(MoveType.PASS, 0, 0);
		}

		Move lastMove = state.getLastMove();
		if(root != null && lastMove != null && !lastMove.getType().equals(MoveType.PASS)){
			pruneTree(lastMove, state);
		} else {
			root = new RBFMNode(null, null, state);
		}

		try {

		}
	}

	private Move minimax(GameState state) {
		ArrayList<Move> allMoves = new ArrayList(state.getPossibleMoves(color));
		if(!allMoves.isEmpty()) {
			for(Move move : allMoves) {
				
			}
		}
	}

	public void extend_down(RBFMNode n) {
		n.expanded();

		if(n.isLeaf()) {
			expand_leaf(n);
			backup(n);
		} else {
			RBFMNode v = n.best_child();
			extend_up(v);
		}
	}

	public void extend_up(RBFMNode n) {
		n.expanded();

		if(n.isLeaf()) {
			expand_leaf(n);
			backup(n);
		} else {
			RBFMNode v = n.choose_random_child();
			extend_down(v);
		}
	}

	public void expand_leaf(RBFMNode parent) {
		parent.expanded();
		minimax(1, parent.getPlayer(), parent.getBoard(), parent);

		int[] info = parent.getNegaMaxScore();
		parent.setScore(info[0]);
		parent.setPosition(info[1]);

		backup(parent);
	}

	public void backup(Node v) {
		while(v != null) {
			int[] info = v.getNegaMaxScore();
			v.setScore(info[0]);
			v.setPosition(info[1]);
			v = v.getParent();
		}
	}
}