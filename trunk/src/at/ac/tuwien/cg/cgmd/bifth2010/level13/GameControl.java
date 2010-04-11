package at.ac.tuwien.cg.cgmd.bifth2010.level13;

public class GameControl {

	static int consumedBeer = 0;
	static int mistressCounter = 0;
	
	public static void consumeBeer(){
		consumedBeer++;
	}
	
	
	public static void encounterCop(CopObject cop){
		cop.isActive = false;
		consumedBeer = 0;
	}
	
	public static void encounterMistress(MistressObject mistress){
		mistress.isActive = false;
		mistressCounter++;
	}
	
	
}
