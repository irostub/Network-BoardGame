
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

public class ClientHandler implements Runnable
{
	private boolean firstConnect;
	private String name;
	private int myTurn;
	private boolean isPlayer = false;
	private SocketChannel sc;
	private ArrayList<SocketChannel> scs;
	GameDriver gameDriver;

	public ClientHandler(SocketChannel sc, GameDriver gd, ArrayList<SocketChannel> scs)
	{
		myTurn = 0;
		this.sc = sc;
		this.gameDriver = gd;
		this.scs = scs;
		firstConnect = true;
		name = null;
	}

	@Override
	public void run()
	{
		System.out.println("ClientHandler Started for " + this.sc);
		String order = "";
		while (true)
		{
			// GET MESSAGE
			order = HelperMethods.receiveMessage(sc);
			// START PROCESSING
			if (firstConnect)
			{
				int playerIdx = 0;
				for (Player i : gameDriver.getPlayers())
				{
					if (order.equals(i.getName()))
					{
						HelperMethods.sendMessage(sc,
								+playerIdx + "/" + gameDriver.getPlayerTurn() + "/Loin Success! Hello " + i.getName());
						scs.add(sc);
						isPlayer = true;
						myTurn = playerIdx;
						name = i.getName();
						break;
					}
					playerIdx++;
				}
				firstConnect = false;
				continue;
			}

			if (order.equalsIgnoreCase("quit"))
			{
				break;
			} else if (!isPlayer)
			{
				HelperMethods.sendMessage(sc, "your not player! goodbye!");
				break;
			} else
			{
				// START GET MY TURN
				if (myTurn == gameDriver.getPlayerTurn())
				{
					if (order.equalsIgnoreCase("y"))
					{
						String temp = gameDriver.movePlayer(name);
						
						if(temp.equals("finish"))
						{
							HelperMethods.sendMessage(sc, name +" is winner. Congratulations! Get dice value" + gameDriver.getDice()+".");
							broadCast(name +" is winner. Congratulations! Last dice value is " + gameDriver.getDice()+". \n\nThanks to play!");
							break;
						}
						String[] result = temp.split("/");
						
						String posResult = "";
						if(result.length == 5)
						{
							posResult = result[0] + "'s current location is " + result[1]
									+ ".\nRolled the dice ! Dice value is " + result[2] + ". \n" + result[4] + "\n"
									+ "After moving " + result[0] + "'s current location is " + result[3] + ".";
						}
						else
						{
							posResult = result[0] + "'s current location is " + result[1]
									+ ".\nRolled the dice ! Dice value is " + result[2] + ". \n" + result[4] + "\n"
									+ "After moving " + result[0] + "'s current location is " + result[3] + ". \n\n*****CATCHING WHOM*****\nAnd Oh! Catch "+ result[5]+". So "+result[5]+" player position is reseted!";
						}
						gameDriver.getBoard().displayBoard();
						
							
						HelperMethods.sendMessage(sc, Integer.toString(gameDriver.getPlayerTurn()) + "/" + posResult);
						broadCast(Integer.toString(gameDriver.getPlayerTurn()) + "/" + posResult);

					} else if (order.equalsIgnoreCase("s"))
					{
						//here if START TRUN Client typing s mean SKIP so need continue
					} else
					{
						HelperMethods.sendMessage(sc, "wrong order! so your turn is skipped. Be careful!");
						broadCast(name + "turn was skipped because of an incorrect command.");
					}
				}
				// END GET MY TURN
				// COUTION!!!!! CAREFUL TURN CONTROL
				// START NOT MY TURN
				if (myTurn != gameDriver.getPlayerTurn())
				{
					// HelperMethods.sendMessage(sc, "not my turn");
				}
				// END NOT MY TURN
			}
			order = "";
		}
		// END PROCESSING

		// ENTER HERE DISCONNECT CLIENT
		System.out.println("ClientHandler Terminated for " + this.sc);
	}

	private void broadCast(String s)
	{
		for (SocketChannel i : scs)
		{
			if (!i.equals(sc))
			{
				HelperMethods.sendMessage(i, s);
			}
		}
	}

}
