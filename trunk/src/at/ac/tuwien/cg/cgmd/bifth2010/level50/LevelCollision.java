package at.ac.tuwien.cg.cgmd.bifth2010.level50;

import java.util.HashMap;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class LevelCollision {
	private int tileSizeX = 30;
	private int tileSizeY = 30;
	private final float tileCountInv = 1.0f/5.0f;
	
	private int collisionArray[];
	private int width, height;
	private String coinState = "";
	private HashMap<Integer, LevelObject> coins = new HashMap<Integer, LevelObject>();
	private HashMap<Integer, LevelObject> levelParts = new HashMap<Integer, LevelObject>();
	
	public LevelCollision(GL10 gl, Context context, int id) {
		LoadTexture(gl, id, context);
	}
	
	private void LoadTexture(GL10 gl, int id, Context context)
	{			
		Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), id);
		width = bmp.getWidth();
		height = bmp.getHeight();
		int pixels[] = new int[width*height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);	
		collisionArray=new int[width*height];
		for(int i=0; i<height; i++)
        {
             for(int j=0; j<width; j++)
             {
                  //correction of R and B
                  int pix=pixels[i*width+j];
                  int pb=(pix>>16)&0xff;
                  int pr=(pix<<16)&0x00ff0000;
                  int px1=(pix&0xff00ff00) | pr | pb;
                  //correction of rows
                  collisionArray[i*width+j]=px1;
                  if ((px1&0x00ffffff) == 0x00ff0000) { //blue
                	  float texcoord[] = { //empty coin
                			  tileCountInv,  2*tileCountInv,
                			  tileCountInv,  tileCountInv,
                			  2*tileCountInv,  tileCountInv,
                			  2*tileCountInv,  2*tileCountInv
                   	  };
                   	  LevelObject coin = new LevelObject(gl, context, this, j*tileSizeX, i*tileSizeY, tileSizeX, tileSizeY, R.drawable.l50_tiles, texcoord);
                   	  coins.put(i*width+j,coin);
//					} else if ((px1&0x00ffffff) == 0x00ffffff) { //white
//						float texcoord[] = { //blue background
//							   0.0f,  1.0f,
//						       0.0f,  0.75f,
//						       0.25f,  0.75f,
//						       0.25f,  1.0f
//						};
//						LevelObject levelPart = new LevelObject(gl, context, this, j*tileSizeX, i*tileSizeY, tileSizeX, tileSizeY, R.drawable.l50_tiles, texcoord);
//						levelParts.put(i*width+j,levelPart);
					} else if ((px1&0x00ffffff) == 0x0000aaaa) { //dark yellow
						float texcoord[] = { //middle floor
								   tileCountInv,  3*tileCountInv,
								   tileCountInv,  2*tileCountInv,
								   2*tileCountInv,  2*tileCountInv,
							       2*tileCountInv,  3*tileCountInv
							};
							LevelObject levelPart = new LevelObject(gl, context, this, j*tileSizeX, i*tileSizeY, tileSizeX, tileSizeY, R.drawable.l50_tiles, texcoord);
							levelParts.put(i*width+j,levelPart);
					} else if ((px1&0x00ffffff) == 0x0000ffff) { //yellow
						float texcoord[] = { //crates
								   0.0f,  2*tileCountInv,
							       0.0f,  tileCountInv,
							       tileCountInv,  tileCountInv,
							       tileCountInv,  2*tileCountInv
							};
							LevelObject levelPart = new LevelObject(gl, context, this, j*tileSizeX, i*tileSizeY, tileSizeX, tileSizeY, R.drawable.l50_tiles, texcoord);
							levelParts.put(i*width+j,levelPart);
					} else if ((px1&0x00ffffff) == 0x0000dddd) {
						float texcoord[] = { //right floor
								2*tileCountInv,  3*tileCountInv,
								2*tileCountInv,  2*tileCountInv,
								3*tileCountInv,  2*tileCountInv,
								3*tileCountInv,  3*tileCountInv
							};
							LevelObject levelPart = new LevelObject(gl, context, this, j*tileSizeX, i*tileSizeY, tileSizeX, tileSizeY, R.drawable.l50_tiles, texcoord);
							levelParts.put(i*width+j,levelPart);
					} else if ((px1&0x00ffffff) == 0x00005555) {
						float texcoord[] = { //left floor
								   0.0f,  3*tileCountInv,
							       0.0f,  2*tileCountInv,
							       tileCountInv,  2*tileCountInv,
							       tileCountInv,  3*tileCountInv
							};
							LevelObject levelPart = new LevelObject(gl, context, this, j*tileSizeX, i*tileSizeY, tileSizeX, tileSizeY, R.drawable.l50_tiles, texcoord);
							levelParts.put(i*width+j,levelPart);
					} else if ((px1&0x00ffffff) == 0x00330033) {
						float texcoord[] = { //left tree
								2*tileCountInv,  2*tileCountInv,
								2*tileCountInv,  tileCountInv,
							    3*tileCountInv,  tileCountInv,
							    3*tileCountInv,  2*tileCountInv
							};
							LevelObject levelPart = new LevelObject(gl, context, this, j*tileSizeX, i*tileSizeY, tileSizeX, tileSizeY, R.drawable.l50_tiles, texcoord);
							levelParts.put(i*width+j,levelPart);
					} else if ((px1&0x00ffffff) == 0x00aa00aa) {
						float texcoord[] = { //middle tree
								3*tileCountInv,  2*tileCountInv,
								3*tileCountInv,  tileCountInv,
							    4*tileCountInv,  tileCountInv,
							    4*tileCountInv,  2*tileCountInv
							};
							LevelObject levelPart = new LevelObject(gl, context, this, j*tileSizeX, i*tileSizeY, tileSizeX, tileSizeY, R.drawable.l50_tiles, texcoord);
							levelParts.put(i*width+j,levelPart);
					} else if ((px1&0x00ffffff) == 0x00cc00cc) {
						float texcoord[] = { //right tree
								4*tileCountInv,  2*tileCountInv,
								4*tileCountInv,  tileCountInv,
							    5*tileCountInv,  tileCountInv,
							    5*tileCountInv,  2*tileCountInv
							};
							LevelObject levelPart = new LevelObject(gl, context, this, j*tileSizeX, i*tileSizeY, tileSizeX, tileSizeY, R.drawable.l50_tiles, texcoord);
							levelParts.put(i*width+j,levelPart);
					} else if ((px1&0x00ffffff) == 0x00ff00ff) {
						float texcoord[] = { //tree trunk
								3*tileCountInv,  tileCountInv,
								3*tileCountInv,  0.0f,
							    4*tileCountInv,  0.0f,
							    4*tileCountInv,  tileCountInv
							};
							LevelObject levelPart = new LevelObject(gl, context, this, j*tileSizeX, i*tileSizeY, tileSizeX, tileSizeY, R.drawable.l50_tiles, texcoord);
							levelParts.put(i*width+j,levelPart);
					}
             }
        }  
	}
	
	public int TestCollision(int x,int y) {
		if (0<=x && x<width && 0<=y && y<height) {
			int returnVal = collisionArray[y*width+x];
			if (coins.containsKey(y*width+x)){
				int i = y*width+x;
				float texcoord[] = {
					       0.0f,  1.0f,
					       0.0f,  0.0f,
					       1.0f,  0.0f,
					       1.0f,  1.0f
					};
				coins.get(i).changeTexture(R.drawable.l00_coin,texcoord);
				collisionArray[i] = 0x00ffff00; //don't bother with coin anymore
				levelParts.put(i,coins.get(i));
				coins.remove(i);
				coinState += (int)Math.ceil(Math.log10(i))+""+i;
			}
			return returnVal;
		} else {
			return 0x00ffffff;
		}
	}
	
	public void draw(GL10 gl, float bx, float by) {
		for (LevelObject coin : coins.values()) {
			//if (Math.abs(coin.getPositionX()-bx)<tileSizeX*10 && Math.abs(coin.getPositionY()-by)<tileSizeY*10)
				coin.draw(gl);
		}
		for (LevelObject part : levelParts.values()) {
			//if (Math.abs(part.getPositionX()-bx)<tileSizeX*10 && Math.abs(part.getPositionY()-by)<tileSizeY*10)
				part.draw(gl);
		}
	}	
	
	public void reset() {
		for (int i = 0; i<height*width; i++) {
			if (levelParts.containsKey(i) && collisionArray[i] == 0x00ffff00) {
	 			float texcoord[] = {
	      			  tileCountInv,  2*tileCountInv,
	      			  tileCountInv,  tileCountInv,
	      			  2*tileCountInv,  tileCountInv,
	      			  2*tileCountInv,  2*tileCountInv
	         	};
	 			levelParts.get(i).changeTexture(R.drawable.l50_tiles,texcoord);
	 			coins.put(i,levelParts.get(i));
	 			levelParts.remove(i);
	 			collisionArray[i] = 0x00ff0000;
	 			coinState = "";
			}
		}
	}
	
	public void scale(float xscale, float yscale) {
		for (LevelObject coin : coins.values()) {
			coin.scale(xscale,yscale);
		}
		for (LevelObject part : levelParts.values()) {
			part.scale(xscale,yscale);
		}
		tileSizeX*=xscale;
		tileSizeY*=yscale;
	}
	
	public float getHeight() {return height;}
	public float getWigth() {return width;}
	public String getCoinState() {return coinState;}
	public void setCoinState(String state) {
		coinState=state;
		for (int actpos=0; actpos<coinState.length();) {
			int numpos = Integer.parseInt(coinState.substring(actpos, actpos+1));
			actpos++;
			int i = Integer.parseInt(coinState.substring(actpos, actpos+numpos));
			actpos+=numpos;
			float texcoord[] = {
				       0.0f,  1.0f,
				       0.0f,  0.0f,
				       1.0f,  0.0f,
				       1.0f,  1.0f
				};
			coins.get(i).changeTexture(R.drawable.l00_coin,texcoord);
			collisionArray[i] = 0x00ffff00; //don't bother with coin anymore
			levelParts.put(i,coins.get(i));
			coins.remove(i);
		}
	}
}
