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

	public void waitForLogic()
	{
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

	public void releaseAll()
	{
		lock.lock();
		prerender.signal();
		logic.signal();
		lock.unlock();
	}
}
