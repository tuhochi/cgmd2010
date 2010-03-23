package at.ac.tuwien.cg.cgmd.bifth2010.level13;

public class Puke extends GameObject{
	int age = 100;
	
	public void reset() {
		age = 30;
		
	}
	
	public int getAge(){
		return age;
	}
	
	public void increaseAge(){
		age--;
	}
}
