package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;

public abstract class TimingTask
{
	public float time;
	public float remainingTime;
	public boolean isDead=false;
	public abstract void update(float dt);
	public abstract void run();
}