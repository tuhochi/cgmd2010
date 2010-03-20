package at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit;

import java.util.HashMap;
import java.util.Iterator;

public class OrbitManager {
	
	private HashMap<String,Orbit> map;
	private static OrbitManager instance;
	
	public static OrbitManager getInstance()
	{
		if(instance == null)
			instance = new OrbitManager();
		return instance;
	}
	
	public OrbitManager()
	{
		map = new HashMap<String, Orbit>();
	}
	
	public void addOrbit(String entity,Orbit orbit)
	{
		map.put(entity,orbit);
	}
	
	public Orbit getOrbit(String entity)
	{
		return map.get(entity);
	}
	
	public void updateOrbits(float dt)
	{
		Iterator<Orbit> it = map.values().iterator();
		while(it.hasNext())
			it.next().updatePos(dt);
	}
}
