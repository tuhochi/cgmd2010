package at.ac.tuwien.cg.cgmd.bifth2010.level44;

public class GameThread extends Thread {
	private GameScene scene;
	private RabbitHead head;
	private boolean quit;
	
	public GameThread(GameScene scene, RabbitHead head) {
		this.scene = scene;
		this.head = head;
		this.quit = false;
	}
	
	public void run() {
		while (!quit) {
			head.setWingAngle((float)(Math.sin(System.currentTimeMillis()/100)*45));
			try {
				sleep(100);
			} catch (InterruptedException ie) {}
		}
	}
	
	public void doQuit() {
		this.quit = true;
	}
}
