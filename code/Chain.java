import java.util.*;

public class Chain{
	Set<Integer> stones;
	Set<Integer> liberties;
	public Chain(Set<Integer> s, Set<Integer> l){
		stones = s;
		liberties = l;
	}
	boolean in_Atari(){
		return liberties.size() == 1;
	}
}