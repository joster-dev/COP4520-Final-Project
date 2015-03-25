import java.util.*;

public class testBoard{

	public static void main(String[] args){
		Board b = new Board(9,'2');

		Scanner s = new Scanner(System.in);

		char turn = '2';
		int pos = 0;
		AIPlayer aiPlayer = new AIPlayer();
		while(true){

			b.print_board();
			System.out.println();
			
			if (turn == '2'){
				
				pos = s.nextInt();

				while(!b.is_legal(turn, pos)){
					System.out.println("Illegal Move");
					pos = s.nextInt();
				}
			}
			else{
				// AI's turn
				try {
					pos = aiPlayer.move(b);
					while(!b.is_legal(turn, pos)){
						System.out.println("Illegal Move");
						pos = aiPlayer.move(b);
					}
					System.out.println(pos);
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			b.add_stone(turn, pos);
			turn = b.opposite(turn);
		}
	}
}