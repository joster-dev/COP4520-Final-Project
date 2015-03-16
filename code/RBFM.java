import java.util.*;

public class RBFM {
	
	public node root;
	public RBFM()
	{
		root = new node(null);
	}
	
	public class node
	{
		public ArrayList<node> children;
		private node parent;
		private double score;
		private double distribution;
		public int num_expanded;
		
		public node(node p){
			children = new ArrayList<node>();
			distribution = 0;
			score=0;
			parent = p;
			num_expanded=0;
		}
		
		public boolean isLeaf()
		{
			return (children.size() == 0);
		}
		
		public node choose_random_child()
		{
			Random r = new Random();
			int randomIndex = r.nextInt(children.size());
			return children.get(randomIndex);
		}
		
		public node best_child()
		{
			double min=Double.MAX_VALUE;
			node best = null;
			for (node n : children)
			{
				if (n.distribution < min)
				{
					min = n.distribution;
					best=n;
				}
			}
			return best;
		}
		
		
		public double getScore()
		{
			return score;
		}
		
		public double getDistribution()
		{
			return distribution;
		}
		
		public node getParent()
		{
			return parent;
		}
	}
	
	public void extend_down(node n, ArrayList<Integer> moves){
		if (n.isLeaf()){
			expand_leaf(n, moves);
			backup(n);
		}
		else{
			node v = n.best_child();
			extend_up(v, moves);
		}
	}
	
	public void extend_up(node n, ArrayList<Integer> moves){
		if (n.isLeaf()){
			expand_leaf(n, moves);
			backup(n);
		}
		else{
			node v = n.choose_random_child();
			extend_down(v, moves);
		}
	}
	
	public void root_decision(ArrayList<Integer> moves)
	{
		while (stopping_criterion() != false)
		{
			node v = root.choose_random_child();
			if (v == root.best_child()){
				extend_up(v, moves);
			}
			else{
				extend_down(v, moves);
			}
		}
	}
	
	public void extend_node(node v, ArrayList<Integer> moves)
	{
		while (!v.isLeaf())
		{
			v = v.choose_random_child();
		}
		expand_leaf(v,moves);
		backup(v);
	}

	// not done
	public void expand_leaf(node v, ArrayList<Integer> moves)
	{
		v.num_expanded++;
		ArrayList<node> children = new ArrayList<node>();
		for (Integer m : moves){
			node u = new node(v);
			children.add(u);
		}
		
		
	}
	
	// not done
	public double negamax_score(node v)
	{
		return 0;
	}
	
	// almost done
	public void backup(node v)
	{
		while (v != null)
		{
			v.score = negamax_score(v);
			v.distribution = update_distribution(v);
			v = v.getParent();
		}
	}
	
	// not done
	public double update_distribution(node v)
	{
		return -1;
	}
	
	// not done
	public boolean stopping_criterion()
	{
		return false;
	}


}
