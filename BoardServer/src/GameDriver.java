
import java.util.ArrayList;
import java.util.Random;

public class GameDriver
{

	private final int MAXDICE = 6;

	private Board board;
	private ArrayList<Player> players = new ArrayList<Player>();
	private Random random;
	private int playerTurn;
	private int dice;

	public GameDriver(Board b, Player p1, Player p2)
	{
		board = b;
		players.add(p1);
		players.add(p2);

		playerTurn = 0;
		random = new Random(System.currentTimeMillis());
	}

	public String movePlayer(String name)
	{
		boolean isSearch = false;
		Player target = null;
		String result = "";

		//search players
		for (Player i : players)
		{
			if (i.getName() == name)
			{
				isSearch = true;
				target = i;
				break;
			}
		}
		if (!isSearch)
		{
			System.out.println("Error:Class GameDriver, Method movePlayer, target value");
		}

		//All client renewal turn this variable CAUTION
		playerTurn++;

		//if player turn overflow then reset player turn
		if (playerTurn == players.size())
		{
			playerTurn = 0;
		}

		dice = random.nextInt(MAXDICE) + 1;
		int currentLocation = target.getCurrentLocation();
		int targetLocation = 0;

		// if here
		if ((currentLocation + dice) >= board.getTiles())
		{// GAME END CHECK HERE
			System.out.println("===========FINISH GAME LOG============");
			return "finish"; // end game return string "finish"
		} else if (board.getBoard()[currentLocation + dice] == 'c')
		{
			// ENTER COIN ROOM
			targetLocation = currentLocation + dice + dice;
			target.setCurrentLocation(targetLocation);
			result = target.getName()+"/"  + currentLocation + "/" + dice + "/" + target.getCurrentLocation() + "/"
					+ "You are in the Coin room! Move twice as much as the dice eye!";
		} else if (board.getBoard()[currentLocation + dice] == 'm')
		{// ENTER MONSTER ROOM
			targetLocation = currentLocation - dice;
			if (targetLocation < 0)
			{// PLAYER BACK, BUT PLAYER IS ALREADY POS AT 0
				target.setCurrentLocation(0);
				result = target.getName()+"/" + currentLocation + "/" + dice + "/" + target.getCurrentLocation() +"/"
						
						+ "Opps! You entered the enemy room. Go back as far as the dice eye. There's no place to go back anymore.";

			} else
			{// PLAYER BACK
				target.setCurrentLocation(targetLocation);
				result = target.getName()+"/"  + currentLocation + "/" + dice + "/"+ target.getCurrentLocation()  + "/"
						+ "Opps! You entered the enemy room. Move back as far as the dice eye.";
			}
		} else if (board.getBoard()[currentLocation + dice] == '#')
		{// ENTER NON ROOM
			targetLocation = currentLocation + dice;
			target.setCurrentLocation(targetLocation);
			result = target.getName()+"/"  + currentLocation + "/" + dice + "/" + target.getCurrentLocation() + "/"
					+ "You entered the vacant room. I think it's safe. Moved as much as dice eyes.";
		} else
		{
			System.out.println("Error: Class Player, Method movePlayer, ArrayList boardItem outoflengh Index");
			result = null;
		}
		String catchp = catchPlayer(target);
		//check catchPlayer.
		//need string return "/catchResult" then work in clienthandler
		if(catchp == null)
		{
			return result;
		}
		else 
		{
			return result + catchp;
		}
		
	}

	public String catchPlayer(Player e)
	{
		String result = "";
		boolean isCatch = false;
		
		
		
		for (Player target : players)
		{
			//is not me
			if (!target.equals(e))
			{
				if (target.getCurrentLocation() == e.getCurrentLocation())
				{
					target.setCurrentLocation(0);
					result = target.getName();
					isCatch = true;
				}
			}
		}
		
		
		System.out.println();
		System.out.println("-------[START]CATCH PLAYER LOG-------");
		System.out.println("-is catch? : "+ isCatch);
		System.out.println("-my pos : "+e.getCurrentLocation());
		System.out.println();
		System.out.println();
		System.out.println("-send msg to ClientHandler : /" + result);
		System.out.println("-------[FIN] CATCH PLAYER LOG-------");
		System.out.println();
		
		if(!isCatch)
		{
			return null;
		}
		else
		{	
			return "/"+result;
		}
	}
	
	/*public boolean checkWinner() {
		
		boolean isWin = false;
		
		for(Player i : players)
		{
			if(i.getCurrentLocation()> board.getTiles())
			{
				isWin = true;
				break;
			}
		}
		if(!isWin)
		{
			return isWin;			
		}
		else {
			return isWin;
		}
		
	} dont need this code*/ 
	/*public void playGame()
	{

	}dont need this code*/

	public int getPlayerTurn()
	{
		return playerTurn;
	}

	public void setPlayerTurn(int playerTurn)
	{
		this.playerTurn = playerTurn;
	}

	public Board getBoard()
	{
		return board;
	}

	public ArrayList<Player> getPlayers()
	{
		return players;
	}

	public void setPlayers(ArrayList<Player> players)
	{
		this.players = players;
	}

	public int getDice()
	{
		return dice;
	}

}
