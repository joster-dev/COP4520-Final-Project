import java.util.*;

public class testBoard{

	public static void main(String[] args){
		Board b = new Board(9,'2');

		Scanner s = new Scanner(System.in);

		char turn = '2';

		while(true){
			b.print_board();
			System.out.println();
			int pos = s.nextInt();

			while(!b.is_legal(turn, pos)){
				System.out.println("Illegal Move");
				pos = s.nextInt();
			}
			b.add_stone(turn, pos);
			turn = b.opposite(turn);
		}
	}
}