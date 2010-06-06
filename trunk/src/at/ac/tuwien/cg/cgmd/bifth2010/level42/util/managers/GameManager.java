package at.ac.tuwien.cg.cgmd.bifth2010.level42.util.managers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Persistable;

public class GameManager implements Persistable{

	private int parts;
	private int score;
	private boolean scoreHasChanged;
	
	public static GameManager instance = new GameManager();
	
	public GameManager(){
		this.score = 0;
		this.parts = 0;
		this.scoreHasChanged = false;
	}
	
	public void init(int parts){
		this.score = 0;
		this.parts = parts;
	
		this.scoreHasChanged = false;
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
	
	public synchronized boolean scoreHasChanged(){
		if(scoreHasChanged)
		{
			scoreHasChanged = false;
			return true;
		}
		return false;
	}

	@Override
	public void persist(DataOutputStream dos) throws IOException {
		dos.writeInt(parts);
		dos.writeInt(score);
		dos.writeBoolean(scoreHasChanged);
	}

	@Override
	public void restore(DataInputStream dis) throws IOException {
		parts = dis.readInt();
		score = dis.readInt();
		scoreHasChanged = dis.readBoolean();
	}
	
}
