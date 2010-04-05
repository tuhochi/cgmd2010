package at.ac.tuwien.cg.cgmd.bifth2010.level55;


import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

public class TileLayer {	
	int TILE_HEIGHT=32;
	int TILE_WIDTH=32;
			
	float duration = 0;
	
	float posX;
	float posY;
	
	float scrollFactor=1.0f;

	public void init(float _scrollFactor) { // tile_height, tile_width,
		scrollFactor=_scrollFactor;
		
		loadLevel();
		
		createVBOs();
	}
	
	public int getTypeAt(int x, int y) {
		return tiles_vector[x][y];
	}
	
	private void loadLevel() {
		numTilesX=10;
		numTilesY=54;
		
		tiles_vector=new int[numTilesX+1][numTilesY+1];
		
		Log.d("TileLayer", "loadLevel");
		
		
		// open file... fill tiles_hashmap
		tiles_vector[2][2]=23;
		tiles_vector[0][2]=23;	
		tiles_vector[7][4]=23;
		// *******************************
	}
	
	private void createVBOs() {
		maxVBOPosX=(int) Math.ceil((double)numTilesX/VBO_WIDTH);
		maxVBOPosY=(int) Math.ceil((double)numTilesY/VBO_HEIGHT);
		
		vbo_vector=new TilesVBO[maxVBOPosX+1][maxVBOPosY+1];
		
		Log.d("TileLayer", "createVBOs");
		

		for (int i=0; i<maxVBOPosX; i++) {
			for (int j=0; j<maxVBOPosY; j++) {
				vbo_vector[i][j]=new TilesVBO(i*VBO_WIDTH, j*VBO_HEIGHT, VBO_WIDTH, VBO_HEIGHT, tiles_vector);
			}
		}
	}
	
	public void update(float dT) {
		duration+=dT*0.001;
	}
	
	public void draw(GL10 gl)
    {
		gl.glLoadIdentity();
		gl.glTranslatef(posX*scrollFactor, posY*scrollFactor, 0.0f);
		// bind tilesatlas
		// apply view frustum culling (adjust loops)
		for (int i=0; i<maxVBOPosX; i++) {
			for (int j=0; j<maxVBOPosY; j++) {
				vbo_vector[i][j].draw(gl);
			}
		}
    }
	
	private final static int VBO_WIDTH=5;
	private final static int VBO_HEIGHT=5;
	
	private int numTilesX=0;
	private int numTilesY=0;
	
	private int maxVBOPosX=0;
	private int maxVBOPosY=0;
	
	public int[][] tiles_vector;
	private TilesVBO[][] vbo_vector;	
}
