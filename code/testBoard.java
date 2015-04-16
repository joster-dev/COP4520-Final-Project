import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class testBoard{

	static boolean COLLECT_DATA = true;
	public static void main(String[] args) throws IOException{
		
		BufferedWriter output = COLLECT_DATA ? getBufferedWriter("SequentialRBFM.txt") : null;
		
		Board b = new Board(9,'2');

		Scanner s = new Scanner(System.in);

		char turn = '2';
		int pos = 0;
		AIPlayer aiPlayer = new AIPlayer();
		int count=0;
		while(true){
			
			b.print_board();
			System.out.println();

			if (turn == '2'){
				
				String inputPos = s.next();
				if (inputPos.compareTo("Q") == 0) break;
				
				pos = Integer.parseInt(inputPos);

				while(!b.is_legal(turn, pos)){
					System.out.println("Illegal Move");
					pos = s.nextInt();
				}
			}
			else{
				count++;
				// AI's turn
				try {
					long startTime = System.currentTimeMillis();
					pos = aiPlayer.move(b,output);
					while(!b.is_legal(turn, pos)){
						//System.out.println("Illegal Move at " + pos );
						pos = aiPlayer.move(b, output);
					}
					long elapsedTime = System.currentTimeMillis() - startTime;
					System.out.println(pos);
					if (COLLECT_DATA){
						try {
							output.write("Time elapsed for move " + count + ": " +Long.toString(elapsedTime));
							output.newLine();
							output.newLine();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			b.add_stone(turn, pos);
			turn = b.opposite(turn);
		}
		
		if (COLLECT_DATA){
			try {
				output.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public static BufferedWriter getBufferedWriter(String fileName){
		File file = new File(fileName);
		BufferedWriter output = null;
		try {
			
			if (COLLECT_DATA)
				output = new BufferedWriter(new FileWriter(file));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return output;
	}
}