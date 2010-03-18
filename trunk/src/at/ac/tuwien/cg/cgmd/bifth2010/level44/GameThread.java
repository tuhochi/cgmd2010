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
			scene.queueEvent(new Runnable() {
				public void run() {
					head.setPosition((float)(scene.getWidth()/2 + scene.getWidth()/5*Math.sin((double)(System.currentTimeMillis()/10000.))), (float)(scene.getHeight()/3));
					head.setScale((float)(.5+.5*Math.abs(Math.sin((double)(System.currentTimeMillis()/5000.)))));
					head.setWingAngle((float)(Math.sin((double)(System.currentTimeMillis()/100.))*45));
					head.setRotation((float)(10-Math.sin((double)(System.currentTimeMillis()/1000.))*20));
				}
			});
			try {
				sleep(10);
			} catch (InterruptedException ie) {}
		}
	}
	
	public void doQuit() {
		this.quit = true;
	}
}
