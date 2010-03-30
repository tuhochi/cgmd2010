package at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit;

import java.util.ArrayList;

public class OrbitManager {
	
	private final ArrayList<Movement> list;
	public static final OrbitManager instance = new OrbitManager();
	
	private OrbitManager()
	{
		list =  new ArrayList<Movement>();
	}
	
	public void addOrbit(Movement orbit)
	{
		list.add(orbit);
	}
	
	public Movement getOrbit(int index)
	{
		return list.get(index);
	}
	
	public void updateOrbits(float dt)
	{
		for(int i=0; i<list.size();i++)
		{
			list.get(i).update(dt);
		}
	}
}
