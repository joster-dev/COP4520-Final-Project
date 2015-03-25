import java.util.*;
/**
 * RBFM algorithm: a game-search algorithm that randomizes Best-First Minimax Search
 * @author Cindy
 *
 */

public class RBFM {
	
	private Node _root;
	//REMOVE!!!
	public int value;
	public RBFM(Node root) throws CloneNotSupportedException
	{
		_root = root;
		value = 10;
	}
	
	public Node getRoot(){
		return _root;
	}
	
	public void extend_down(Node n) throws CloneNotSupportedException{
		n.expanded();
		
		if (n.isLeaf()){
			expand_leaf(n);
			backup(n);
		}
		else{
			Node v = n.best_child();
			extend_up(v);
		}
	}
	
	public void extend_up(Node n) throws CloneNotSupportedException{
		n.expanded();
		
		if (n.isLeaf()){
			expand_leaf(n);
			backup(n);
		}
		else{
			Node v = n.choose_random_child();
			extend_down(v);
		}
	}
	
	public void root_decision() throws CloneNotSupportedException
	{
		while (stopping_criterion() != false)
		{
			Node v = _root.choose_random_child();
			
			if (v == _root.best_child()){
				extend_up(v);
			}
			else{
				extend_down(v);
			}
		}
	}

	public void expand_leaf(Node parent) throws CloneNotSupportedException
	{
		parent.expanded();
		
		// create children for node v
		AIPlayer.minimax(1, parent.getPlayer(), parent.getBoard(), parent);
		
		// set negamax score
		int[] info = parent.getNegaMaxScore();
		parent.setScore(info[0]);
		parent.setPosition(info[1]);
		
		// send score up to the root
		backup(parent);
	}
	
	/**
	 * Updated negamax score of a node is updated all the way up to the root
	 * @param v - node to update
	 */
	public void backup(Node v)
	{
		while (v != null)
		{
			// set negamax score
			int[] info = v.getNegaMaxScore();
			v.setScore(info[0]);
			v.setPosition(info[1]);
			v = v.getParent();
		}
	}
	
	// not done, figure out stopping criteria
	public boolean stopping_criterion()
	{
		if (value!=0){
			value--;
			return true;
		}
		return false;
	}


}
