package at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit;

import java.util.ArrayList;

public class OrbitManager {
	
	private  ArrayList<Orbit> list;
	private static OrbitManager instance;
	
	public static OrbitManager getInstance()
	{
		if(instance == null)
			instance = new OrbitManager();
		return instance;
	}
	
	public OrbitManager()
	{
		list =  new ArrayList<Orbit>();
	}
	
	public void addOrbit(Orbit orbit)
	{
		list.add(orbit);
	}
	
	public Orbit getOrbit(int index)
	{
		return list.get(index);
	}
	
	public void updateOrbits(float dt)
	{
		for(int i=0; i<list.size();i++)
		{
			list.get(i).updatePos(dt);
		}
	}
}
