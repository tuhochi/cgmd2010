package at.ac.tuwien.cg.cgmd.bifth2010.level55;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

public class TilesVBO extends Mesh {
	
	int coinCount;
	Coin coins[];
	
	public TilesVBO(GL10 gl, float sizeFactor, int x, int y, int width, int height, int[][] tiles_vector, Texture texture, int texRows, int texCols) {
		/*
		+-+-+-+-+
		+-+-+-+-+
		+-+-+-+-+
		+-+-+-+-+
		+-+-+-+-+
		*/
			
		int maxX=tiles_vector.length;
		int maxY=tiles_vector[0].length;
		
		float tileWidth=1.0f/(float)texCols;
		float tileHeight=1.0f/(float)texRows;
		
		float halfTexelWidth=texture.texelWidth/2.0f;
		float halfTexelHeight=texture.texelHeight/2.0f;
		
		int tilesCount=0;
		
		coinCount=0;
		
		for (int i=x; i<Math.min(maxX,(x+width)); i++) {
			for (int j=y; j<Math.min(maxY, (y+height)); j++) {
				
				int type=tiles_vector[i][j];
	
				if (type!=-1) {
					tilesCount++;
					if (type==TileLayer.activeCoin_typeId || type==TileLayer.inactiveCoin_typeId) {
						coinCount++;
					}
				}
			}
		}
		
		coins=new Coin[coinCount];
		
		FloatBuffer vertices = ByteBuffer.allocateDirect(tilesCount*12*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		FloatBuffer texCoords = ByteBuffer.allocateDirect(tilesCount*8*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		ShortBuffer indices = ByteBuffer.allocateDirect(tilesCount*6*2).order(ByteOrder.nativeOrder()).asShortBuffer();
		
		int count=0;
		coinCount=0;
		for (int i=x; i<Math.min(maxX,(x+width)); i++) {
			for (int j=y; j<Math.min(maxY, (y+height)); j++) {
				
				int type=tiles_vector[i][j];
	
				if (type!=-1) {
					if (type==TileLayer.activeCoin_typeId || type==TileLayer.inactiveCoin_typeId) {
						int rowInTileSet=TileLayer.activeCoin_typeId/texCols;
						int colInTileSet=TileLayer.activeCoin_typeId-rowInTileSet*texCols;
						
						float texX1_A=tileWidth*colInTileSet+halfTexelWidth;
						float texX2_A=tileWidth+tileWidth*colInTileSet-halfTexelWidth;
						
						float texY1_A=tileHeight*rowInTileSet+halfTexelHeight;
						float texY2_A=tileHeight+tileHeight*rowInTileSet-halfTexelHeight;
						
						rowInTileSet=TileLayer.inactiveCoin_typeId/texCols;
						colInTileSet=TileLayer.inactiveCoin_typeId-rowInTileSet*texCols;
						
						float texX1_I=tileWidth*colInTileSet+halfTexelWidth;
						float texX2_I=tileWidth+tileWidth*colInTileSet-halfTexelWidth;
						
						float texY1_I=tileHeight*rowInTileSet+halfTexelHeight;
						float texY2_I=tileHeight+tileHeight*rowInTileSet-halfTexelHeight;
						
						coins[coinCount]=new Coin(gl, i, j, type==TileLayer.activeCoin_typeId,
								i*sizeFactor, j*sizeFactor, (i+1.0f)*sizeFactor, (j+1.0f)*sizeFactor,
								texX1_A, texY1_A, texX2_A, texY2_A,
								texX1_I, texY1_I, texX2_I, texY2_I);
						coinCount++;
					} else {
						
						int rowInTileSet=type/texCols;
						int colInTileSet=type-rowInTileSet*texCols;
						
						float texX1=tileWidth*colInTileSet+halfTexelWidth;
						float texX2=tileWidth+tileWidth*colInTileSet-halfTexelWidth;
						
						float texY1=tileHeight*rowInTileSet+halfTexelHeight;
						float texY2=tileHeight+tileHeight*rowInTileSet-halfTexelHeight;
					
						final int vertexIndex=count*12;
						final int texCoordIndex=count*8;
						final int indexIndex=count*6;
						
						vertices.put(vertexIndex,i*sizeFactor);
						vertices.put(vertexIndex+1,j*sizeFactor);
						vertices.put(vertexIndex+2,-1.0f);
						texCoords.put(texCoordIndex,texX1);
						texCoords.put(texCoordIndex+1,texY1);
						
						vertices.put(vertexIndex+3,(i+1.0f)*sizeFactor);
						vertices.put(vertexIndex+4,j*sizeFactor);
						vertices.put(vertexIndex+5,-1.0f);
						texCoords.put(texCoordIndex+2,texX2);
						texCoords.put(texCoordIndex+3,texY1);
						
						vertices.put(vertexIndex+6,(i+1.0f)*sizeFactor);
						vertices.put(vertexIndex+7,(j+1.0f)*sizeFactor);
						vertices.put(vertexIndex+8,-1.0f);
						texCoords.put(texCoordIndex+4,texX2);
						texCoords.put(texCoordIndex+5,texY2);
						
						vertices.put(vertexIndex+9,i*sizeFactor);
						vertices.put(vertexIndex+10,(j+1.0f)*sizeFactor);
						vertices.put(vertexIndex+11,-1.0f);
						texCoords.put(texCoordIndex+6,texX1);
						texCoords.put(texCoordIndex+7,texY2);
						
						int numVertices=count*4+4;
						
						indices.put(indexIndex,(short) (numVertices-4));
						indices.put(indexIndex+1,(short) (numVertices-3));
						indices.put(indexIndex+2,(short) (numVertices-2));
						
						indices.put(indexIndex+3,(short) (numVertices-4));
						indices.put(indexIndex+4,(short) (numVertices-2));
						indices.put(indexIndex+5,(short) (numVertices-1));
						
						count++;
					}
				}
			}
		}
		
		if (tilesCount>0) {
			super.init(gl, vertices, texCoords, indices);
		}
	}
	
	public void draw(GL10 gl) {
		for (int i=0; i<coinCount; i++) {
			coins[i].draw(gl);
		}
		super.draw(gl);
	}

}
