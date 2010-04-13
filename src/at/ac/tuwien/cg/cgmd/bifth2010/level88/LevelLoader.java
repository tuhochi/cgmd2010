package at.ac.tuwien.cg.cgmd.bifth2010.level88;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * 
 * @author Asperger, Radax
 * Levelloader, reads the context of a txt-file to create the level
 */

public class LevelLoader {

	/*
	 Werte, von der txt-Datei
	 X 		Haeuser
	 " "	Straﬂe
	 B		Startposition bunny
	 P		Startposition police
	 123	Verstecke mit Groeﬂenangabe
	 G		Garagenversteck
	 S		Strassensperre
	 W		Waschanlage
	 F		Fuﬂgaengerzone
	 T		Schwere Straﬂensperre
	 N		Nassgraben(Wassergraben)
	 Z		Zugbruecke
	*/
	//TODO Gefahrenabfrage mit ok!!!
	
	boolean ok = true;
	String file = null;
	BufferedReader bf = null;
	String zeile = null;
	ArrayList<String[]> values = new ArrayList<String[]>();
	Random r = null;
	
	//Constructor
	public LevelLoader(String _file){
		file = _file;
		r = new Random();
		
		
		//TODO
		ok = exist();
		
		if(ok){
			load();
		} else {
			//TODO Fehlerbehandlung
		}
			
		
	}
	
	//Ueberpruefen ob das angegeben file existiert
	private boolean exist(){
		
		if(file == null){
			return false;
		}
		
		File aFile = new File(file);
		
		if(aFile.isFile()){
			return true;
		}
		
		return false;
	}
	
	//einlesen des files in eine Arraylist
	private boolean load(){
		
		try {
			bf = new BufferedReader(new FileReader(file));
			zeile = bf.readLine();
			
			while(zeile != null){
				values.add(zeile.split(" "));
				zeile = bf.readLine();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		
		return true;
	}
	
	//return der Groeﬂe der Arraylist
	public int getSize(){
		return values.size();
	}
	
	//Werte aus der Arraylist bekommen
	public String[] getLine(int l){
		return values.get(l);
	}
	
	//Werte zurueckliefern fuer die Objekte im Level TODO
	public int getCellInfo(String info){
		
		if(info.equalsIgnoreCase("X")){
			//Haeuser
			int h = (r.nextInt()%5);
			
			switch(h){
			case 0:
				return R.drawable.l88_house_block1;
			case 1:
				return R.drawable.l88_house_block2;
			case 2:
				return R.drawable.l88_house_block3;
			case 3:
				return R.drawable.l88_house_block4;
			case 4:
				return R.drawable.l88_house_block5;
			}
			
			
		}
		if(info.equalsIgnoreCase(" ")){
			//Straﬂe TODO noch genauer unterteilen
			
		}
		if(info.equalsIgnoreCase("B")){
			//Startpos bunny
			return R.drawable.l88_bunny;
		}
		if(info.equalsIgnoreCase("P")){
			//Startpos police
			return R.drawable.l88_police;
		}
		if(info.equalsIgnoreCase("1")){
			//Versteck Groesse 1
			return R.drawable.l88_stash_yellow;
		}
		if(info.equalsIgnoreCase("2")){
			//Versteck Groesse 2
			return R.drawable.l88_stash_orange;
		}
		if(info.equalsIgnoreCase("3")){
			//Versteck Groesse 3
			return R.drawable.l88_stash_red;
		}
		if(info.equalsIgnoreCase("G")){
			//Garagenversteck
		}
		if(info.equalsIgnoreCase("S")){
			//Strassensperre
		}
		if(info.equalsIgnoreCase("W")){
			//Waschanlage
		}
		if(info.equalsIgnoreCase("F")){
			//Fussgaengerzone
		}
		if(info.equalsIgnoreCase("T")){
			//Schwere Strassensperre
		}
		if(info.equalsIgnoreCase("N")){
			//Wassergraben
			return R.drawable.l88_water;
		}
		if(info.equalsIgnoreCase("Z")){
			//Zugbruecke
		}
		
		
		
		return 0;
	}
	
	
	
	
	
	
}
