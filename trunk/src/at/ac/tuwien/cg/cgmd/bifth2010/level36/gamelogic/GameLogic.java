package at.ac.tuwien.cg.cgmd.bifth2010.level36.gamelogic;

import android.os.Handler;
import at.ac.tuwien.cg.cgmd.bifth2010.level36.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level36.rendering.GameView_New;
import at.ac.tuwien.cg.cgmd.bifth2010.level36.sound.SoundManager;

public class GameLogic extends Thread {
	private LevelActivity la;
	private GameView_New gv;
	private SoundManager sm;
	private long current = 0L;
	private boolean startedTimer = false;
	private Handler handler;
	
	private String outputText;
	private String timeText;
	private String moneyText;
	private String notificationText;
	private int coins;
	private int actualCoinValue;
	private boolean isRunning;
	
	private boolean isPaused;
	private long startTimePaused;
	
	public GameLogic(LevelActivity la, GameView_New gv, SoundManager sm, Handler handler) {
		this.la = la;
		this.gv = gv;
		this.sm = sm;
		this.handler = handler;
		this.coins = 100;
		this.isRunning = false;
		this.isPaused = false;
		this.startTimePaused = 0L;
		this.actualCoinValue = this.gv.getActualCoinValue();
	}
	
	public void startTimer(long seconds) {
		this.current = System.currentTimeMillis();
		this.current += seconds;
		this.startedTimer = true;
	}
	
	public long getActualCounter() {
		return (this.current - System.currentTimeMillis()) / 1000L;
	}
	
	public void run() {
		boolean finished = false;
		this.isRunning = true;
		while (!finished) {
			if (this.gv.getGameRenderer().getOnSurfaceCreated()) {
				if (!startedTimer) 
					startTimer(150000L);
				
				this.outputText = "RUBBING";
				this.moneyText = ""+this.coins+"$";
				
				if (isPaused) {
					if (startTimePaused == 0L) {
						startTimePaused = System.currentTimeMillis();
					}
					this.timeText = "---";
				} else {
					if (startTimePaused > 0L) {
						long diff = System.currentTimeMillis() - this.startTimePaused;
						this.current += diff;
						this.startTimePaused = 0L;
					}
					this.timeText = ""+getActualCounter();
				}
				
				if (this.gv.getGameRenderer().isDiscovered()) {
					this.gv.getGameRenderer().resetRenderer();
					int randNumber = this.gv.getRandomNumber();
					this.gv.getGameRenderer().setRandNumber(randNumber);					
					this.coins -= this.actualCoinValue;
					this.actualCoinValue = randNumber;
				}		
				
				if (getActualCounter() <= 0L) {
					this.timeText = "0";
					finished = true;
				}
				
				if (this.coins <= 0) {
					this.moneyText = "0";
					this.coins = 0;
					finished = true;
				}
				
				//Log.d("aaa", "bbb");
				//this.handler.post(new UIThread(this.time));
				this.handler.sendEmptyMessage(1);
			}
		}
		this.gv.getGameRenderer().disableDrawing();
		this.gv.disableSound();
		this.notificationText = "Ihr Score: "+this.coins;
		this.handler.sendEmptyMessage(2);
		startTimer(5000L);
		while (this.getActualCounter() >= 0L) {
		}
		this.la.quit();
	}
	
	public String getOutputText() {
		return this.outputText;
	}
	
	public String getTimeText() {
		return this.timeText;
	}
	
	public String getMoneyText() {
		return this.moneyText;
	}
	
	public String getNotificationText() {
		return this.notificationText;
	}
	
	public int getCoins() {
		return this.coins;
	}
	
	public void setPaused(boolean paused) {
		if (paused)
			this.sm.pausePlayer();
		
		this.isPaused = paused;
	}
	
	public boolean isRunning() {
		return this.isRunning;
	}
}
