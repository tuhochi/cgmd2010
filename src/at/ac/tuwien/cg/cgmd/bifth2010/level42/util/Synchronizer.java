package at.ac.tuwien.cg.cgmd.bifth2010.level42.util;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Synchronizer
{
	private ReentrantLock lock = new ReentrantLock();
	private Condition prerender = lock.newCondition();
	private Condition logic = lock.newCondition();

	// init lastLogicFrame one lower than lastPreRenderFrame,
	// to let the logic frame run once before starting to render
	private int lastLogicFrame = -1;
	private int lastPreRenderFrame = 0;
	public boolean running = true;

	public void reset()
	{
		lock.lock();
		prerender.signal();
		logic.signal();
		lastLogicFrame = -1;
		lastPreRenderFrame = 0;
		lock.unlock();
	}
	
	public void waitForLogic()
	{
		if(!running)
			return;
		lock.lock();
		try
		{
			while(lastPreRenderFrame!=lastLogicFrame)
				logic.await();
		}
		catch(InterruptedException e)
		{
		}
		lock.unlock();
	}

	public void waitForPreRender()
	{
		if(!running)
			return;
		lock.lock();
		try
		{
			while(lastPreRenderFrame==lastLogicFrame)
				prerender.await();
		}
		catch(InterruptedException e)
		{
		}
		lock.unlock();
	}

	public void preRenderDone()
	{
		lock.lock();
		lastPreRenderFrame++;
		prerender.signal();
		lock.unlock();
	}

	public void logicDone()
	{
		lock.lock();
		lastLogicFrame++;
		logic.signal();
		lock.unlock();
	}

	public void setActive(boolean running)
	{
		if(this.running == running)
			return;
		
		this.running = running;
		
		if(!running)
		{
			lock.lock();
			prerender.signal();
			logic.signal();
			lock.unlock();
		}
	}
	
	public void blockLogic(boolean block)
	{
		
	}
}
