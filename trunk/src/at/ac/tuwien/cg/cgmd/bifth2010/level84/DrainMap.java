package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import java.util.ArrayList;
import java.util.List;

public class DrainMap {

	/**
	 * place and manage the drains in your street
	 */

	
	List<Drain> drainMap = new ArrayList();
	
	public DrainMap()
	{
		initDrains();
	}
	
	/**
	 * init drainmap
	 */
	public void initDrains()
	{
		addDrain(0,2.0f);
		addDrain(1,4.0f);
		addDrain(2,6.0f);
		addDrain(3,7.0f);
		addDrain(4,8.0f);
	}
	
	/**
	 * @param style defines hole shape of drain
	 * @param xPos position of the drain
	 */
	public void addDrain(int style, float xPos)
	{
		Drain d = new Drain(style);
		d.setPosition(xPos);
		drainMap.add(d);
	}
	
}
