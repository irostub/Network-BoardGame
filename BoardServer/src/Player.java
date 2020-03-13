//TODO need patch score
public class Player {

	private String name;
	private int currentLocation;
	
	public Player(String name) 
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}
	
	public int getCurrentLocation()
	{
		return currentLocation;
	}

	public void setCurrentLocation(int currentLocation)
	{
		this.currentLocation = currentLocation;
	}
}
