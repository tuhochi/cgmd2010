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
import at.ac.tuwien.cg.cgmd.bifth2010.level88.util.Vertexbuffers;

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
			isStreetForPolice=false;
			isStreetForBunny=false;
			isPolicePresent=false;
			numStreetNeighboursPolice=0;
			numStreetNeighboursBunny=0;
		}

		/** Set true, if the police is capable to cross this cell*/
		public boolean isStreetForPolice;
		/** Set true, if the bunny is capable to cross this cell*/
		public boolean isStreetForBunny;
		/** Set true, if the police is at this cell*/
		public boolean isPolicePresent;
		/** Number of neighbourstreets of the police*/
		public int numStreetNeighboursPolice;
		/** Number of neighbourstreets of the bunny*/
		public int numStreetNeighboursBunny;
		/** Set the type of the cell*/
		public char type;
	}

	/** Game class instance of the level*/
	private Game game;
	/** map of the level*/
	public MapCell[][] cells;
	/** direction of the level for flipping the map*/
	public Vector2 groundXDir, groundYDir;
	/** direction of the houses for flipping the map*/
	public Vector2 houseXDir, houseYDir, houseQuadBase;
	/** buffer to save the whole map*/
	public Vertexbuffers vbos;
	/** counter of vertices*/
	public int numVertices;

	/**
	 * Constructor
	 * @param _game Instance of the Game class to get the important context informations
	 * 
	 */
	public Map(Game _game) {
		game = _game;

		float norm;
		groundYDir = new Vector2(-0.81f, -0.59f);
		groundXDir = new Vector2(1.16f, -0.46f);

		houseXDir = new Vector2(2.23f, 0);
		//houseYDir = new Vector2(0, -1.89f);
		houseYDir = new Vector2(0, -2.23f);
		houseQuadBase = new Vector2();
		houseQuadBase.x = -groundXDir.x;
	}

	/**
	 * Update the map
	 * @param elapsedSeconds
	 */
	public void update(float elapsedSeconds) {

	}

	
	/**
	 * Draw the map
	 * @param gl OpenGL-information to use in the context
	 */
	public void draw(GL10 gl) {

		vbos.set(gl);
		game.textures.bind(R.drawable.l88_atlas);
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, numVertices);
	}

	
	/**
	 * Move the police from one position to the next to save the presence in the map
	 * @param fromX x-position of the old position
	 * @param fromY y-position of the old position
	 * @param toX x-position of the new position
	 * @param toY y-position of the new position
	 */
	public void movePolice(int fromX, int fromY, int toX, int toY) {
		cells[fromX][fromY].isPolicePresent = false;
		cells[toX][toY].isPolicePresent = true;
	}

	/**
	 * Loading of the level information of a txt-file
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
			}
		}

		int numHouses=0;
		for(int y=0; y<values.size(); y++){
			char[] zeichen = values.get(y).toCharArray();
			for(int x=0; x<values.get(y).length(); x++){

				switch(zeichen[x]){
				case 'X':
				case 'x':
					cells[x+1][values.size()-y].type = 'x';
					numHouses++;
					break;
				case ' ':
					//Straße
					cells[x+1][values.size()-y].type = 's';
					cells[x+1][values.size()-y].isStreetForBunny = true;
					cells[x+1][values.size()-y].isStreetForPolice = true;
					break;
				case '1':
					cells[x+1][values.size()-y].type = 's';
					game.addStash(x+1, values.size()-y, 1);
					cells[x+1][values.size()-y].isStreetForBunny = true;
					cells[x+1][values.size()-y].isStreetForPolice = true;
					break;
				case '2':
					cells[x+1][values.size()-y].type = 's';
					game.addStash(x+1, values.size()-y, 2);
					cells[x+1][values.size()-y].isStreetForBunny = true;
					cells[x+1][values.size()-y].isStreetForPolice = true;
					break;
				case '3':
					cells[x+1][values.size()-y].type = 's';
					game.addStash(x+1, values.size()-y, 3);
					cells[x+1][values.size()-y].isStreetForBunny = true;
					cells[x+1][values.size()-y].isStreetForPolice = true;
					break;
				case 'B':
				case 'b':
					cells[x+1][values.size()-y].type = 's';
					game.bunny.initPosition(x+1, values.size()-y);
					cells[x+1][values.size()-y].isStreetForBunny = true;
					cells[x+1][values.size()-y].isStreetForPolice = true;
					break;
				case 'F':
				case 'f':
					cells[x+1][values.size()-y].type = 'f';
					cells[x+1][values.size()-y].isStreetForBunny = false;
					cells[x+1][values.size()-y].isStreetForPolice = true;
					numHouses++;
					break;
				case 'T':
				case 't':
					cells[x+1][values.size()-y].type = 't';
					cells[x+1][values.size()-y].isStreetForBunny = true;
					cells[x+1][values.size()-y].isStreetForPolice = false;
					numHouses++;
					break;
				case 'G':
				case 'g':
					cells[x+1][values.size()-y].type = 'g';
					cells[x+1][values.size()-y].isStreetForBunny = true;
					cells[x+1][values.size()-y].isStreetForPolice = true;
					numHouses++;
					break;
				case 'P':
				case 'p':
					cells[x+1][values.size()-y].type = 's';
					game.addPolice(x+1, values.size()-y);
					cells[x+1][values.size()-y].isStreetForBunny = true;
					cells[x+1][values.size()-y].isStreetForPolice = true;
					cells[x+1][values.size()-y].isPolicePresent = true;
					break;
				}
			}//end for
		}//end for

		numVertices = (w*h + numHouses)*6;
		Vector2[] vertices = new Vector2[numVertices];
		Vector2[] texCoords = new Vector2[numVertices];

		int i=0;
		float tx, ty, dt, mt;
		dt = 1.0f/8.0f;
		mt = 1.0f/512.0f;

		for(int x=0; x<w; x++)
		{
			for(int y=0; y<h; y++)
			{
				tx = 0.0f;
				ty = 0.0f;

				if( x>0 && x<w-1 && y>0 && y<h-1 )
				{
					if(cells[x-1][y].isStreetForPolice ) cells[x][y].numStreetNeighboursPolice++;
					if(cells[x+1][y].isStreetForPolice ) cells[x][y].numStreetNeighboursPolice++;
					if(cells[x][y-1].isStreetForPolice ) cells[x][y].numStreetNeighboursPolice++;
					if(cells[x][y+1].isStreetForPolice ) cells[x][y].numStreetNeighboursPolice++;
					
					if(cells[x-1][y].isStreetForBunny ) cells[x][y].numStreetNeighboursBunny++;
					if(cells[x+1][y].isStreetForBunny ) cells[x][y].numStreetNeighboursBunny++;
					if(cells[x][y-1].isStreetForBunny ) cells[x][y].numStreetNeighboursBunny++;
					if(cells[x][y+1].isStreetForBunny ) cells[x][y].numStreetNeighboursBunny++;
				}
				
				if( x>0 && x<w-1 && y>0 && y<h-1 && cells[x][y].type == 's'){
					int counter = 0;

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
					case 1: // dead end
						ty = 3.0f;
						break;
					case 2: // dead end
						tx = 1.0f;
						break;
					case 3: // turn
						tx = 1.0f;
						ty = 3.0f;
						break;
					case 4: // dead end
						ty = 1.0f;
						break;
					case 5: // straight
						ty = 2.0f;
						break;
					case 6: // turn
						tx = 1.0f;
						ty = 1.0f;
						break;
					case 7: // t-junction
						tx = 1.0f;
						ty = 2.0f;
						break;
					case 8: // dead end
						tx = 3.0f;
						break;
					case 9: // turn
						tx = 3.0f;
						ty = 3.0f;
						break;
					case 10: // straight
						tx = 2.0f;
						break;
					case 11: // t-junction
						tx = 2.0f;
						ty = 3.0f;
						break;
					case 12: // turn
						tx = 3.0f;
						ty = 1.0f;
						break;
					case 13: // t-junction
						tx = 3.0f;
						ty = 2.0f;
						break;
					case 14: // t-junction
						tx = 2.0f;
						ty = 1.0f;
						break;
					case 15: // junction
						tx = ty = 2.0f;
						break;

					}//end switch
				}
				
				Vector2 v1 = new Vector2(groundXDir.x*x + groundYDir.x*y, groundXDir.y*x + groundYDir.y*y);
				Vector2 v2 = Vector2.add(v1, groundXDir);
				Vector2 v3 = Vector2.add(Vector2.add(v1, groundXDir), groundYDir);
				Vector2 v4 = Vector2.add(v1, groundYDir);
				Vector2 t1 = new Vector2(tx*dt+mt,			(ty+1.0f)*dt-mt);
				Vector2 t2 = new Vector2((tx+1.0f)*dt-mt,	(ty+1.0f)*dt-mt);
				Vector2 t3 = new Vector2((tx+1.0f)*dt-mt,	ty*dt+mt);
				Vector2 t4 = new Vector2(tx*dt+mt,			ty*dt+mt);
				
				vertices[6*i  ] = v1;
				vertices[6*i+1] = v2;
				vertices[6*i+2] = v3;
				vertices[6*i+3] = v1;
				vertices[6*i+4] = v3;
				vertices[6*i+5] = v4;
				
				texCoords[6*i  ] = t1;
				texCoords[6*i+1] = t2;
				texCoords[6*i+2] = t3;
				texCoords[6*i+3] = t1;
				texCoords[6*i+4] = t3;
				texCoords[6*i+5] = t4;
				
				i++;
			}
		}
		
		for(int x=cells.length-1; x>=0; x--)
		{
			for(int y=cells[0].length-1; y>=0; y--)
			{
				boolean containsHouse = true;
				Vector2 v1, v2, v3, v4, t1, t2, t3, t4;
				v1 = v2 = v3 = v4 = t1 = t2 = t3 = t4 = new Vector2();
				tx = 0.0f;
				ty = 0.0f;

				v1 = new Vector2(houseQuadBase.x + groundXDir.x*x + groundYDir.x*y, houseQuadBase.y + groundXDir.y*x + groundYDir.y*y);
				v2 = Vector2.add(v1, houseXDir);
				v3 = Vector2.add(Vector2.add(v1, houseXDir), houseYDir);
				v4 = Vector2.add(v1, houseYDir);
				
				if( cells[x][y].type == 'x' )
				{
					tx = (float)(r.nextInt(8));
					ty = 5.0f;
				}
				else if( cells[x][y].type == 'f' )
				{
					if( r.nextInt(2) == 0 )
					{
						tx = 1.0f;
					}
					else
					{
						tx = 4.0f;
					}
					ty = 7.0f;
				}
				else if( cells[x][y].type == 't' )
				{
					if( r.nextInt(2) == 0 )
					{
						tx = 3.0f;
					}
					else
					{
						tx = 2.0f;
					}
					ty = 7.0f;
				}
				else if( cells[x][y].type == 'g' )
				{
					tx = 6.0f;
					ty = 7.0f;
				}
				else
				{
					containsHouse = false;
				}

				if( containsHouse )
				{
					t1 = new Vector2(tx*dt+mt,			(ty+1.0f)*dt-mt);
					t2 = new Vector2((tx+1.0f)*dt-mt,	(ty+1.0f)*dt-mt);
					t3 = new Vector2((tx+1.0f)*dt-mt,	ty*dt+mt);
					t4 = new Vector2(tx*dt+mt,			ty*dt+mt);
					
					vertices[6*i  ] = v1;
					vertices[6*i+1] = v2;
					vertices[6*i+2] = v3;
					vertices[6*i+3] = v1;
					vertices[6*i+4] = v3;
					vertices[6*i+5] = v4;

					texCoords[6*i  ] = t1;
					texCoords[6*i+1] = t2;
					texCoords[6*i+2] = t3;
					texCoords[6*i+3] = t1;
					texCoords[6*i+4] = t3;
					texCoords[6*i+5] = t4;
					
					i++;
				}
			}
		}

		vbos = new Vertexbuffers();
		vbos.setData(Vertexbuffers.Type.POSITION, vertices);
		vbos.setData(Vertexbuffers.Type.TEX_COORD, texCoords);
	}
}
