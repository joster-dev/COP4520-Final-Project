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
	private int depth = 5;
	private int value = 10;

	public RBFMComputerPlayer(PlayerColor color) {
		super(color);
	}

	@Override
	public Move getMove(GameState state) {
		if(state.getLastMoved().equals(color)) {
			//we shouldn't have been called, it's not our turn
			System.out.println("Wrong Turn");
			return Move.getMoveInstance(MoveType.PASS, 0, 0);
		}

		Move lastMove = state.getLastMove();
		if(root != null && lastMove != null && !lastMove.getType().equals(MoveType.PASS)){
			pruneTree(lastMove, state);
		} else {
			root = new RBFMNode(null, null, state);
		}

		int[] pos = minimax(state, depth)

		return Move.getMoveInstance(MoveType.PASS, 0, 0);
	}

	private void minimax(GameState state, int depth) {
		ArrayList<Move> allMoves = new ArrayList(state.getPossibleMoves(color));

		PlayerColor playerJustMoved = state.getLastMoved().getColor();

		int bestScore = Integer.MIN_VALUE;
		int currentScore;
		int bestPosition = -1;

		if(depth != 0 && !allMoves.isEmpty()) {
			for(Move move : allMoves) {
				
			}
		}

		return Move.getMoveInstance(MoveType.PASS, 0, 0);
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
		// minimax(1, parent.getPlayer(), parent.getBoard(), parent);

		int[] info = parent.getNegaMaxScore();
		parent.setScore(info[0]);
		// parent.setPosition(info[1]);

		backup(parent);
	}

	public void root_decision() {
		while(stopping_criterion() != false) {
			RBFMNode v = root.choose_random_child();

			if(v == root.best_child()) {
				extend_up(v);
			} else {
				extend_down(v);
			}
		}
	}

	public void backup(RBFMNode v) {
		while(v != null) {
			int[] info = v.getNegaMaxScore();
			v.setScore(info[0]);
			// v.setPosition(info[1]);
			v = v.getParent();
		}
	}

	public boolean stopping_criterion() {
		if(value != 0) {
			value--;
			return true;
		}
		return false;
	}

	private void stripBadMoves(Set<Move> possibleMoves, GameState state) {
		for (Iterator<Move> it = possibleMoves.iterator(); it.hasNext();){
			Move move = it.next();
			if (state.isEye(move, state.getNextToMove().getColor())){
				it.remove();
			}
		}
	}

	private void pruneTree(Move lastMove, GameState state) {
		RBFMNode newNode = new RBFMNode(lastMove, null, state);
		for(RBFMNode node : root.getChildren()) {
			if(node.getMove().equals(lastMove)) {
				newNode = node;
			}
			node.setParent(null);
		}
		if(root != null) {
			root.getChildren().clear();
		}
		root = newNode;
	}
}