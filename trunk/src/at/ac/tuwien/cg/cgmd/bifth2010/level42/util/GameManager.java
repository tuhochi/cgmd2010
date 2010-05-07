package at.ac.tuwien.cg.cgmd.bifth2010.level42.util;

public class GameManager {

	private int parts;
	private int score;
	private boolean scoreHasChanged;
	
	public static GameManager instance;
	
	public GameManager(int parts){
		this.score = 0;
		this.parts = parts;
	
		this.scoreHasChanged = false;
		
		instance = this;
	}
	
	public synchronized void setScore(float scorePercent){
		this.score = Math.round((scorePercent*parts)/100);
		scoreHasChanged=true;
	}
	
	public synchronized float getScorePercent(){
		return (score*100)/parts;
	}
	public synchronized int getScore(){
		return score;
	}
	public synchronized boolean isComplete(){
		return score==parts;
	}
	public synchronized void incScore(){
		score++;
		scoreHasChanged=true;
	}
	
	public boolean scoreHasChanged(){
		return scoreHasChanged;
	}
	
}
