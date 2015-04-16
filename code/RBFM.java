import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;
/**
 * RBFM algorithm: a game-search algorithm that randomizes Best-First Minimax Search
 * @author Cindy
 *
 */

public class RBFM {
	
	ArrayList<Thread> threads = new ArrayList<>();

	private Node _root;
	//REMOVE!!!
	public int totalThreads;
	public int value;
	public BufferedWriter _outputFile;
	public RBFM(Node root, BufferedWriter outputFile) throws CloneNotSupportedException
	{
		_root = root;
		value = 10;
		totalThreads = 2;
		_outputFile = outputFile;
	}
	
	public BufferedWriter getOutputFile(){
		return _outputFile;
	}
	
	public Node getRoot(){
		return _root;
	}
	
	public int countExpansions(Node root){
		int total = root.getNumExpanded();
		if (root!= null){
			
			for (Node n : root.getChildren()){
				total+= countExpansions(n);
			}
		}
		return total;
	}
	
	public void resetNodeExpansions(Node root){
		if (root != null) {
			root.resetNodeExpansions();
			for (int i=0;i<root.getChildren().size();i++)
			{
				resetNodeExpansions(root.getChildren().get(i));
			}
		}
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
	
	public class WorkerThread extends Thread
	{
		private Thread t;
		private int id;
		private int current_value;
		
		public WorkerThread(int id)
		{
			this.id = id;
		}
		
		public void run() {
			Node v = _root.choose_random_child();

			v.lockNode();

			if (v == _root.best_child()){
				try {
					extend_up(v);
					}
				catch (CloneNotSupportedException e){
				}
			}
			else{
				try {
					extend_down(v);
					}
				catch (CloneNotSupportedException e){
				}
			}
			v.unlockNode();
		}
		
		public void start()
		{
			if (t == null)
			{
				t = new Thread(this, Integer.toString(id));
				t.start();
			}
		}
	}
 
	
	public void root_decision() throws CloneNotSupportedException, InterruptedException, IOException
	{ 
		while (stopping_criterion() != false)
		{
			resetNodeExpansions(_root);
			WorkerThread[] workers = new WorkerThread[totalThreads];
			for (int i=0; i<totalThreads;i++){
				workers[i] = new WorkerThread(i);
				workers[i].start();
			}
			
			for (int i=0; i<totalThreads;i++){
				workers[i].t.join();
			}
		}
		
		if (_outputFile != null){
			int totalExpansions = countExpansions(_root);
			String output = "Number of node expansions " + totalExpansions;
			_outputFile.write(output);
			_outputFile.newLine();
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
