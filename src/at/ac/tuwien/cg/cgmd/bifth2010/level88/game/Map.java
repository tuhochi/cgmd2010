package at.ac.tuwien.cg.cgmd.bifth2010.level88.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.util.Quad;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.util.Vector2;

/**
 * The class representing the whole level
 * @author Asperger, Radax
 */
public class Map {
	/**
	 * The class representing one cell of the map with all the different variables
	 * @author Asperger, Radax
	 */
	public class MapCell {
		
		/**
		 * Constructor 
		 */
		public MapCell() {
			x = y = -1;
			groundRotation = 0;
			groundTexture = R.drawable.l88_street_none;
			houseTexture = -1;
			isStreetForPolice=false;
			isStreetForBunny=false;
			isPolicePresent=false;
			numStreetNeighboursPolice=0;
		}

		public int x, y;
		public float translateX, translateY;
		public int groundTexture;
		public int houseTexture;
		public int groundRotation; // Tex-Coords

		public boolean isStreetForPolice;
		public boolean isStreetForBunny;
		public boolean isPolicePresent;
		public int numStreetNeighboursPolice;
		public char type;
	}

	private Game game;
	public MapCell[][] cells;
	public Vector2 groundXDir, groundYDir;
	private Quad groundQuad;
	public Quad houseQuad;


	/**
	 * Constructor
	 * @param _game Instance of the Game class to get the important context informations
	 * 
	 */
	public Map(Game _game) {
		game = _game;

		float norm;
		groundYDir = new Vector2(-229, -169);
		norm = 1.0f / groundYDir.length();
		groundYDir.mult(norm);
		groundXDir = new Vector2(329, -131);
		groundXDir.mult(norm);
		groundQuad = new Quad(new Vector2(), groundXDir, groundYDir);

		Vector2 houseXDir = new Vector2(660, 0);
		houseXDir.mult(norm);
		Vector2 houseYDir = new Vector2(0, -538);
		houseYDir.mult(norm);
		Vector2 houseQuadBase = new Vector2();
		houseQuadBase.x = -groundXDir.x;
		houseQuad = new Quad(houseQuadBase, houseXDir, houseYDir);

	}

	
	/**
	 * Update ability of the map
	 * @param elapsedSeconds
	 */
	public void update(float elapsedSeconds) {

	}

	
	/**
	 * Draw the map
	 * @param gl OpenGL-information to use in the context
	 */
	public void draw(GL10 gl) {
		groundQuad.vbos.set(gl);
		for(int x=0; x<cells.length; x++) {
			for(int y=0; y<cells[0].length; y++) {
				gl.glMatrixMode(GL10.GL_MODELVIEW);
				gl.glPushMatrix();
				gl.glTranslatef(cells[x][y].translateX, cells[x][y].translateY, 0);
				gl.glMatrixMode(GL10.GL_TEXTURE);
				gl.glPushMatrix();
				gl.glRotatef(cells[x][y].groundRotation*90, 0, 0, 1);

				game.textures.bind(cells[x][y].groundTexture);
				gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);

				gl.glPopMatrix();
				gl.glMatrixMode(GL10.GL_MODELVIEW);
				gl.glPopMatrix();
			}
		}

		houseQuad.vbos.set(gl);
		for(int x=cells.length-1; x>=0; x--) {
			for(int y=cells[0].length-1; y>=0; y--) {
				if( cells[x][y].houseTexture != -1 ) {
					gl.glPushMatrix();
					gl.glTranslatef(cells[x][y].translateX, cells[x][y].translateY, 0);

					game.textures.bind(cells[x][y].houseTexture);
					gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);

					gl.glPopMatrix();
				}
			}
		}
	}

	public void movePolice(int fromX, int fromY, int toX, int toY) {
		cells[fromX][fromY].isPolicePresent = false;
		cells[toX][toY].isPolicePresent = true;
	}

	/**
	 * Loading of the levelinformation of a txt-file
	 */
	public void load(){
		InputStream is = game.context.getResources().openRawResource(R.raw.l88_level);
		InputStreamReader irs = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(irs);

		ArrayList<String> values = new ArrayList<String>();
		Random r = new Random();

		try {
			String zeile = br.readLine();

			while(zeile != null){
				values.add(zeile);
				zeile = br.readLine();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		
		int h,w;
		h = values.size() + 2;
		w = values.get(0).length() + 2;

		cells = new MapCell[w][h];
		for(int x=0; x<cells.length; x++) {
			for(int y=0; y<cells[0].length; y++) {
				cells[x][y] = new MapCell();
				cells[x][y].x = x;
				cells[x][y].y = y;
				cells[x][y].translateX = groundXDir.x*x + groundYDir.x*y;
				cells[x][y].translateY = groundXDir.y*x + groundYDir.y*y;
			}
		}

		for(int y=0; y<values.size(); y++){
			char[] zeichen = values.get(y).toCharArray();
			for(int x=0; x<values.get(y).length(); x++){

				switch(zeichen[x]){
				case 'X':
				case 'x':

					int t = Math.abs((r.nextInt()%5));
					switch(t){
					case 0:
						cells[x+1][values.size()-y].houseTexture = R.drawable.l88_house_block1;
						break;
					case 1:
						cells[x+1][values.size()-y].houseTexture = R.drawable.l88_house_block2;
						break;
					case 2:
						cells[x+1][values.size()-y].houseTexture = R.drawable.l88_house_block3;
						break;
					case 3:
						cells[x+1][values.size()-y].houseTexture = R.drawable.l88_house_block4;
						break;
					case 4:
						cells[x+1][values.size()-y].houseTexture = R.drawable.l88_house_block5;
						break;
					}
					cells[x+1][values.size()-y].type = 'x';
					break;
				case ' ':
					//Straße
					cells[x+1][values.size()-y].groundTexture = R.drawable.l88_street_none;
					cells[x+1][values.size()-y].type = 's';
					cells[x+1][values.size()-y].isStreetForBunny = true;
					cells[x+1][values.size()-y].isStreetForPolice = true;
					break;
				case '1':
					cells[x+1][values.size()-y].groundTexture = R.drawable.l88_street_none;
					cells[x+1][values.size()-y].type = 's';
					game.addStash(x+1, values.size()-y, 1);
					cells[x+1][values.size()-y].isStreetForBunny = true;
					cells[x+1][values.size()-y].isStreetForPolice = true;
					break;
				case '2':
					cells[x+1][values.size()-y].groundTexture = R.drawable.l88_street_none;
					cells[x+1][values.size()-y].type = 's';
					game.addStash(x+1, values.size()-y, 2);
					cells[x+1][values.size()-y].isStreetForBunny = true;
					cells[x+1][values.size()-y].isStreetForPolice = true;
					break;
				case '3':
					cells[x+1][values.size()-y].groundTexture = R.drawable.l88_street_none;
					cells[x+1][values.size()-y].type = 's';
					game.addStash(x+1, values.size()-y, 3);
					cells[x+1][values.size()-y].isStreetForBunny = true;
					cells[x+1][values.size()-y].isStreetForPolice = true;
					break;
				case 'B':
				case 'b':
					cells[x+1][values.size()-y].groundTexture = R.drawable.l88_street_none;
					cells[x+1][values.size()-y].type = 's';
					game.bunny.setPosition(x+1, values.size()-y);
					cells[x+1][values.size()-y].isStreetForBunny = true;
					cells[x+1][values.size()-y].isStreetForPolice = true;
					break;
				case 'P':
				case 'p':
					cells[x+1][values.size()-y].groundTexture = R.drawable.l88_street_none;
					cells[x+1][values.size()-y].type = 's';
					game.addPolice(x+1, values.size()-y);
					cells[x+1][values.size()-y].isStreetForBunny = true;
					cells[x+1][values.size()-y].isStreetForPolice = true;
					cells[x+1][values.size()-y].isPolicePresent = true;
					break;


				}

			}//end for
		}//end for


		//Nun die Straßenorientierung anpassen
		/*
		 * 1 ist oben
		 * 2 ist rechts
		 * 4 ist unten
		 * 8 ist links
		 * binäres zählen
		 */
		int counter = 0;

		//startend immer von eins, da wir rund um den Level ne Puffer zone bauen
		for(int x = 1; x <cells.length-1; x++ ){
			for(int y = 1; y < cells[x].length-1; y++){

				//zuvor abfragen ob dieses Feld ein Straßenfeld ist
				if(cells[x][y].type == 's'){
					counter = 0;

					if(cells[x-1][y].isStreetForPolice ) cells[x][y].numStreetNeighboursPolice++;
					if(cells[x+1][y].isStreetForPolice ) cells[x][y].numStreetNeighboursPolice++;
					if(cells[x][y-1].isStreetForPolice ) cells[x][y].numStreetNeighboursPolice++;
					if(cells[x][y+1].isStreetForPolice ) cells[x][y].numStreetNeighboursPolice++;
					
					if(cells[x-1][y].type == 's'){
						counter += 8;
					}
					if(cells[x+1][y].type == 's'){
						counter += 2;
					}
					if(cells[x][y-1].type == 's'){
						counter += 4;
					}
					if(cells[x][y+1].type == 's'){
						counter += 1;
					}


					//Straßenanordnung
					switch(counter){
					case 1:
						cells[x][y].groundTexture = R.drawable.l88_street_end;
						cells[x][y].groundRotation = 2;
						break;
					case 2:
						cells[x][y].groundTexture = R.drawable.l88_street_end;
						cells[x][y].groundRotation = 1;
						break;
					case 3:
						cells[x][y].groundTexture = R.drawable.l88_street_turn;
						cells[x][y].groundRotation = 1;
						break;
					case 4:
						cells[x][y].groundTexture = R.drawable.l88_street_end;
						cells[x][y].groundRotation = 0;
						break;
					case 5:
						cells[x][y].groundTexture = R.drawable.l88_street_straight;
						cells[x][y].groundRotation = 0;
						break;
					case 6:
						cells[x][y].groundTexture = R.drawable.l88_street_turn;
						cells[x][y].groundRotation = 0;
						break;
					case 7:
						cells[x][y].groundTexture = R.drawable.l88_street_tjunction;
						cells[x][y].groundRotation = 0;
						break;
					case 8:
						cells[x][y].groundTexture = R.drawable.l88_street_end;
						cells[x][y].groundRotation = 3;
						break;
					case 9:
						cells[x][y].groundTexture = R.drawable.l88_street_turn;
						cells[x][y].groundRotation = 2;
						break;
					case 10:
						cells[x][y].groundTexture = R.drawable.l88_street_straight;
						cells[x][y].groundRotation = 1;
						break;
					case 11:
						cells[x][y].groundTexture = R.drawable.l88_street_tjunction;
						cells[x][y].groundRotation = 1;
						break;
					case 12:
						cells[x][y].groundTexture = R.drawable.l88_street_turn;
						cells[x][y].groundRotation = 3;
						break;
					case 13:
						cells[x][y].groundTexture = R.drawable.l88_street_tjunction;
						cells[x][y].groundRotation = 2;
						break;
					case 14:
						cells[x][y].groundTexture = R.drawable.l88_street_tjunction;
						cells[x][y].groundRotation = 3;
						break;
					case 15:
						cells[x][y].groundTexture = R.drawable.l88_street_junction;
						cells[x][y].groundRotation = 0;
						break;

					}//end switch
				}
			}
		}
	}
}
