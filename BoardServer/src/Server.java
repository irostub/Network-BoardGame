
import static java.net.StandardSocketOptions.SO_RCVBUF;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.io.PrintWriter;

public class Server
{

	public Server()
	{
		ArrayList <SocketChannel> scs = new ArrayList<SocketChannel>(); //scs is clienthandlers thread list. just init. then add scs in clienthandler object
		ArrayList <GameDriver> drivers = new ArrayList<GameDriver>();
		
		System.out.println("Game Server Started...");
		
		//INIT GAMEDRIVER
		int pCount = 0;
		Board board = new Board(50);
		Player tom = new Player("tom");
		Player jerry = new Player("jerry");
		GameDriver driver = new GameDriver(board, tom, jerry);

		try
		{
			ServerSocketChannel ssc = ServerSocketChannel.open();
			ssc.bind(new InetSocketAddress(5100));
			ssc.setOption(SO_RCVBUF, 1024); //receive buf size 1024 byte
			ssc.configureBlocking(true); //blocking true
			boolean running = true;

			while (running)
			{
				if (pCount >= 2) //this game max player. if change init player then pcount too change
				{
					break;
				}
				System.out.println("Waiting for client ...");
				
				SocketChannel sc = ssc.accept(); //blocked here
				
				
				new Thread(new ClientHandler(sc, driver, scs)).start();
				pCount++;
			}

		} catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		new Server();
	}
}
