package at.ac.tuwien.cg.cgmd.bifth2010.level11;

public class Level extends Thread {

	private boolean isRunning;
	private boolean isPaused;
	
	public Level() {
		
	}
	
	
	
	
	public void run() {
		while (isRunning) {
			while (isPaused && isRunning) {
				
			}
			update();
		}
	}

	private void update() {
		
	}
	
	private void draw() {
		
	}
}
