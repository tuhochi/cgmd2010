package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;

import java.io.Serializable;

public abstract class TimingTask implements Serializable
{
	private static final long serialVersionUID = 6964775302644047821L;
	public float time;
	public float remainingTime;
	public boolean isDead=false;
	public abstract void update(float dt);
	public abstract void run();
}