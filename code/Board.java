import java.util.*;

public class Board implements Cloneable{
	static final char EMPTY = '0';
	static final char WHITE = '1';
	static final char BLACK = '2';
 
	public int ko_point;

	private int size;
	public char board[];  //this is bad needs to be

	private int black_captured;
	private int white_captured;
	private char player_turn; //Player whose turn it is

	public Object clone() throws CloneNotSupportedException{
		Board cloned = (Board)super.clone();
		cloned.board = cloned.board.clone();
		return cloned;
	}

	Board(int s, char color){
		size = s;
		board = new char[size*size];
		player_turn = color;
		black_captured = 0;
		white_captured = 0;

		for(int i=0;i<size*size;++i)
			board[i] = EMPTY;
	}

	/*
		Checks to makes sure the move is not a ko point
		suicide point, or that there isnt already a piece there
	*/
	public boolean is_legal(char color, int pos){
		if((pos > 0) && board[pos] != EMPTY)
			return false;
		if(pos == ko_point)
			return false;
 
		boolean suicide = true;
		for(int n : get_Neighbors(pos) ){
			if(board[n] == EMPTY)
				suicide = false;

			else if(board[n] == color)
				if( !in_Atari(n) )
					suicide = false;

			else if( board[n] != color )
				if( in_Atari(n) )
					suicide = false;
		}

		if(suicide)
			return false;

		return true;
	}

	public char opposite(char color){
		if(color == WHITE)
			return BLACK;
		if(color == BLACK)
			return WHITE;
		return EMPTY;
	}

	Set<Integer> get_Chain_Points(Board b, int pos){
		Set<Integer> stones = new HashSet<Integer>();
		stones.add(pos);

		char color = b.board[pos];
		char flood_color = opposite(color);
		b.board[pos] = flood_color;

		List<Integer> nghbs = b.get_Neighbors(pos);
		for(int n : nghbs){
			if(b.board[n] == color)
				if(!stones.contains(n))
					stones.addAll(get_Chain_Points(b,n));
		}

		return stones;
	}

	Chain get_Chain(int pos){

		try{
			Board copy = (Board) this.clone();

			Set<Integer> stones = get_Chain_Points(copy, pos);

			Set<Integer> liberties = new HashSet<Integer>();
			for(int stone : stones){
				for(int n : get_Neighbors(stone))
					if(board[n] == EMPTY)
						liberties.add(n);
			}

			return new Chain(stones, liberties);
		} catch(CloneNotSupportedException c) {}

		return null;
	}


	void add_stone(char color, int pos){

		Set<Integer> captured = new HashSet<Integer>();
		for(int n : get_Neighbors(pos) ){

			if(board[n] == opposite(color) && board[n] != EMPTY){

				Chain enemy = get_Chain(n);

				if(enemy.in_Atari()){
					for(int e : enemy.stones){
						board[e] = EMPTY;
						captured.add(e);
					}
				}
			}
		}
		board[pos] = color; 
		
		if(captured.size() == 1)
			ko_point = captured.iterator().next();
		else
			ko_point = -1;

		if(player_turn == WHITE)
			white_captured += captured.size();
		else 
			black_captured += captured.size();

		player_turn = opposite(color);

	}

	void remove_stone(int pos){
		board[pos] = EMPTY;
	}

	public void print_board(){
		for(int i=0;i<size*size;++i){
			if(i%size == 0)
				System.out.print('\n');
			if(board[i] == EMPTY)
				System.out.print('.');
			if(board[i] ==BLACK)
				System.out.print('B');
			if(board[i] == WHITE)
				System.out.print('W');

		}
	}
	List<Integer> get_Neighbors(int pos){
		List<Integer> nghbs = new ArrayList<Integer>();

		if(pos != 0 && pos%size != 0)
			nghbs.add(pos-1) ;
		if(pos != size*size-1 && pos%(size-1) != 0)
			nghbs.add(pos+1);
		if(pos >= size)
			nghbs.add(pos - size);
		if(pos <= size*size - size-1)
			nghbs.add(pos + size);

		return nghbs;
	}

	/*
		This function can be sped up by keeping of track of groups of stones
		There would be no need for a floodfill search and for cloning the board
	*/
	Set<Integer> get_Liberties(int pos){
		Set<Integer> liberties = new HashSet<Integer>();

		char color = board[pos];
		char flood_color = (pos == WHITE) ? BLACK : WHITE;
		board[pos] = flood_color;

		List<Integer> nghbs = get_Neighbors(pos);
		for(int n : nghbs){
			if(board[n] == color)
				liberties.addAll( get_Liberties(n) );
			else if( board[n] == EMPTY)
				liberties.add(n);
		}

		return liberties;
	}


	boolean in_Atari(int pos){
		try{
			Board copy = (Board) this.clone();
			return copy.get_Liberties(pos).size()  == 1;
		} catch (CloneNotSupportedException c) {}

		return false;
	}
	
	public int getBlacksCaptured()
	{
		return black_captured;
	}
	
	public int getWhiteCaptured()
	{
		return white_captured;
	}

}