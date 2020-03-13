import java.util.Random;

public class Board {
	private char[] board;
	private int tiles;
	
	public Board(int t) {
		tiles = t;
		board = new char[tiles];
		setupBoard();
	}
	
	private void setupBoard() {
		Random random = new Random(System.currentTimeMillis());
		
		for(int i=0; i<tiles; i++)
		{
			int loc = random.nextInt(100)+1;
			
			
			if(loc <= 30)
			{
				board[i] = 'c';
			}
			else if(loc > 30 && loc <= 40)
			{
				board[i] = 'm';
			}
			else {
				board[i] = '#';
			}
		}
	}
	
	public void displayBoard() 
	{
		System.out.println("========Game Board========");
		
		for(int i = 0; i < tiles-1; i++) {
			System.out.print("["+board[i]+"]-");
			if((i+1)%10 == 0)
			{
				System.out.println();
			}
		}
		System.out.println("["+board[tiles-1]+"]");
	}
	
	
	public char[] getBoard()
	{
		return board;
	}
	public int getTiles()
	{
		return tiles;
	}
}








