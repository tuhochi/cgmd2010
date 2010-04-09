package at.ac.tuwien.cg.cgmd.bifth2010.level88;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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
	
	//Constructor
	public LevelLoader(String _file){
		file = _file;
		
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
	
}
