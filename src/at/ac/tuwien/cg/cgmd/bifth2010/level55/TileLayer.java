package at.ac.tuwien.cg.cgmd.bifth2010.level55;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.util.Log;

public class TileLayer {	
	int TILE_HEIGHT=32;
	int TILE_WIDTH=32;
			
	float duration = 0;
	
	float posX;
	float posY;
	
	float scrollFactor=1.0f;
	float sizeFactor=1.0f;
	
	Texture texture;
	
	static float screenWidth;
	static float screenHeight;

	public void init(GL10 gl, float _scrollFactor, float _sizeFactor, int levelResource, int textureResource, int texRows, int texCols, Context context) {
		scrollFactor=_scrollFactor;
		sizeFactor=_sizeFactor;
		
		texture=new Texture();
		texture.create(textureResource);
		
		loadLevel(levelResource, context);
		
		createVBOs(gl, texRows, texCols);
	}
	
	public int getTypeAt(int x, int y) {
		return tiles_vector[x][y];
	}
	
	private void loadLevel(int levelResource, Context context) {
		Log.d("TileLayer", "loadLevel");
		
		BufferedReader levelStream=new BufferedReader(new InputStreamReader(context.getResources().openRawResource(levelResource)));
		String line;
		try {
			line = levelStream.readLine();
			if (line!=null) {
				StringTokenizer st = new StringTokenizer(line);
				if (st.hasMoreTokens()) {
					numTilesX=Integer.parseInt(st.nextToken());
				}
				if (st.hasMoreTokens()) {
					numTilesY=Integer.parseInt(st.nextToken());
				}
			}
			tiles_vector=new int[numTilesX+1][numTilesY+1];
			
			line = levelStream.readLine();
			
			int row=0;
			int col=0;
			
			while(line!=null) {
				StringTokenizer st = new StringTokenizer(line);
				col=0;
				while (st.hasMoreTokens()) {
					tiles_vector[col][row]=Integer.parseInt(st.nextToken());
					col++;
				}
				
				line=levelStream.readLine();
				row++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("loadLevel", "fileError");
		}
	}
	
	private void createVBOs(GL10 gl, int texRows, int texCols) {
		maxVBOPosX=(int) Math.ceil((double)numTilesX/VBO_WIDTH);
		maxVBOPosY=(int) Math.ceil((double)numTilesY/VBO_HEIGHT);
		
		vbo_vector=new TilesVBO[maxVBOPosX+1][maxVBOPosY+1];
		
		Log.d("TileLayer", "createVBOs");
		

		for (int i=0; i<maxVBOPosX; i++) {
			for (int j=0; j<maxVBOPosY; j++) {
				vbo_vector[i][j]=new TilesVBO(gl, sizeFactor, i*VBO_WIDTH, j*VBO_HEIGHT, VBO_WIDTH, VBO_HEIGHT, tiles_vector, texture, texRows, texCols);
			}
		}
	}
	
	public void update(float dT) {
		duration+=dT*0.001;
	}
	
	public void draw(GL10 gl)
    {
		texture.bind(gl);
		
		float tempPosX=posX*scrollFactor;
		float tempPosY=posY*scrollFactor;
		
		int minVisibleVBO_x=Math.max((int) (-tempPosX/(VBO_WIDTH*sizeFactor)), 0);
		int maxVisibleVBO_x=Math.min((int) ((screenWidth-tempPosX-1)/(VBO_WIDTH*sizeFactor)),maxVBOPosX-1);
		
		int minVisibleVBO_y=Math.max((int) (-tempPosY/(VBO_HEIGHT*sizeFactor)), 0);
		int maxVisibleVBO_y=Math.min((int) ((screenHeight-tempPosY-1)/(VBO_HEIGHT*sizeFactor)), maxVBOPosY-1);
		
		gl.glLoadIdentity();
		gl.glTranslatef(tempPosX, tempPosY, 0.0f);
		// bind tilesatlas
		// apply view frustum culling (adjust loops)
		for (int i=minVisibleVBO_x; i<=maxVisibleVBO_x; i++) {
			for (int j=minVisibleVBO_y; j<=maxVisibleVBO_y; j++) {
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
