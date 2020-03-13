
import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class Client
{

	public Client()
	{
		int myTurn = 0;
		int gameTurn = 0;
		
		boolean firstConnect = true;
		System.out.println("Game Client Started...");
		// connect to server use this ip, port
		SocketAddress address = new InetSocketAddress("127.0.0.1", 5100);
		try (SocketChannel sc = SocketChannel.open(address))
		{ // create socket then connect
			System.out.println("Connected to the BoardServer!");
			Scanner in = new Scanner(System.in);
			while (true)
			{
				String input = "";
				// check firstConnect with player argument
				if (firstConnect)
				{
					System.out.print("Enter your id : ");
					input = in.nextLine();
					HelperMethods.sendMessage(sc, input);
					firstConnect = false;
					String[] receive = HelperMethods.receiveMessage(sc).split("/");
					System.out.println(receive[2]);
					
					myTurn = Integer.parseInt(receive[0]);
					gameTurn = 0;
					input = "";

					continue;
				}
				// player order type here
				// first, need typing name after roll dice message
				
				// if quit sent to message for server
				// end while loop exit this client
				//START GET MY TURN
				if(myTurn == gameTurn)
				{
					System.out.println();
					System.out.println("******Now Your Turn!******");
					System.out.print("roll dice(Y/N) : ");
					input = in.nextLine();

					if (input.equals("quit"))
					{
						HelperMethods.sendMessage(sc, input);
						break;
					}
					// if not quit send to message for server
					HelperMethods.sendMessage(sc, input);

					String receive = HelperMethods.receiveMessage(sc);
					if(receive.contains("winner"))
					{
						System.out.println();
						System.out.println("*=*=*=*=*=WINER*=*=*=*=*=*");
						System.out.println();
						System.out.println(receive);
						System.out.println();
						System.out.println("*=*=*=*=*=*=*=*=*=*=*=*=*=*");
						System.out.println();
						return;
					}
					String[] spl = receive.split("/");
					gameTurn = Integer.parseInt(spl[0]);
					
					System.out.println();
					System.out.println("*=*=*=*=*=*=*=*=*=*=*=*=*=*");
					System.out.println(spl[1]);
					System.out.println("*=*=*=*=*=*=*=*=*=*=*=*=*=*");
					System.out.println();
				}
				//END GET MY TURN
				
				//COUTION!!!!! CAREFUL TURN CONTROL
				//START NOT MY TURN
				if(myTurn != gameTurn)
				{
					String receive = HelperMethods.receiveMessage(sc);
					
					if(receive.contains("winner"))
					{
						System.out.println(receive);
						HelperMethods.sendMessage(sc, "quit");
						return;
					}
					String[] spl = receive.split("/");
					gameTurn = Integer.parseInt(spl[0]);
					
					System.out.println();
					System.out.println("*=*=*=*=*=*=*=*=*=*=*=*=*=*");
					System.out.println(spl[1]);
					System.out.println("*=*=*=*=*=*=*=*=*=*=*=*=*=*");
					System.out.println();
					
				}
				//END NOT MY TURN
				
				input = "";
			}
			System.out.println("Game Client is terminated...");

		} catch (IOException e)
		{
		}
	}

	public static void main(String[] args)
	{
		new Client();

	}

}
