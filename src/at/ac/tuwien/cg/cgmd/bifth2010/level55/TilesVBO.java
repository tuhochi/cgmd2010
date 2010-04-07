package at.ac.tuwien.cg.cgmd.bifth2010.level55;

import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

public class TilesVBO extends Mesh {
	
	public TilesVBO(GL10 gl, float sizeFactor, int x, int y, int width, int height, int[][] tiles_vector, Texture texture, int texRows, int texCols) {
		/*
		+-+-+-+-+
		+-+-+-+-+
		+-+-+-+-+
		+-+-+-+-+
		+-+-+-+-+
		*/
		
		Vector<Float> vertices=new Vector<Float>();
		Vector<Float> texCoords=new Vector<Float>();
		Vector<Short> indices=new Vector<Short>();
			
		int maxX=tiles_vector.length;
		int maxY=tiles_vector[0].length;
		
		float tileWidth=1.0f/(float)texCols;
		float tileHeight=1.0f/(float)texRows;
		
		float halfTexelWidth=texture.texelWidth/2.0f;
		float halfTexelHeight=texture.texelHeight/2.0f;
		
		int count=0;
		for (int i=x; i<Math.min(maxX,(x+width)); i++) {
			for (int j=y; j<Math.min(maxY, (y+height)); j++) {
				
				int type=tiles_vector[i][j];
	
				if (type!=-1) {
					int rowInTileSet=type/texCols;
					int colInTileSet=type-rowInTileSet*texCols;
					
					float texX1=tileWidth*colInTileSet+halfTexelWidth;
					float texX2=tileWidth+tileWidth*colInTileSet-halfTexelWidth;
					
					float texY1=tileHeight*rowInTileSet+halfTexelHeight;
					float texY2=tileHeight+tileHeight*rowInTileSet-halfTexelHeight;
					
					count++;
					vertices.add(new Float(i*sizeFactor));
					vertices.add(new Float(j*sizeFactor));
					vertices.add(new Float(1.0));
					texCoords.add(new Float(texX1));
					texCoords.add(new Float(texY1));
					
					vertices.add(new Float((i+1.0)*sizeFactor));
					vertices.add(new Float(j)*sizeFactor);
					vertices.add(new Float(1.0));
					texCoords.add(new Float(texX2));
					texCoords.add(new Float(texY1));
					
					vertices.add(new Float((i+1.0)*sizeFactor));
					vertices.add(new Float((j+1.0)*sizeFactor));
					vertices.add(new Float(1.0));
					texCoords.add(new Float(texX2));
					texCoords.add(new Float(texY2));
					
					vertices.add(new Float(i*sizeFactor));
					vertices.add(new Float((j+1.0)*sizeFactor));
					vertices.add(new Float(1.0));
					texCoords.add(new Float(texX1));
					texCoords.add(new Float(texY2));
					
					int numVertices=vertices.size()/3;
					
					indices.add(new Short((short) (numVertices-4)));
					indices.add(new Short((short) (numVertices-3)));
					indices.add(new Short((short) (numVertices-2)));
					
					indices.add(new Short((short) (numVertices-4)));
					indices.add(new Short((short) (numVertices-2)));
					indices.add(new Short((short) (numVertices-1)));
				}
			}
		}
		
		if (count>0) {
			super.init(gl, vertices, texCoords, indices);
		}
	}

}
