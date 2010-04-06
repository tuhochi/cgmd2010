package at.ac.tuwien.cg.cgmd.bifth2010.level55;

import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

public class TilesVBO extends Mesh {
	
	public TilesVBO(GL10 gl, int x, int y, int width, int height, int[][] tiles_vector) {
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
		
		int count=0;
		for (int i=x; i<Math.min(maxX,(x+width)); i++) {
			for (int j=y; j<Math.min(maxY, (y+height)); j++) {
				
				int type=tiles_vector[i][j];
	
				if (type!=0) {
					count++;
					vertices.add(new Float(i));
					vertices.add(new Float(j));
					vertices.add(new Float(1.0));
					texCoords.add(new Float(0.0));
					texCoords.add(new Float(0.0));
					
					vertices.add(new Float(i+1.0));
					vertices.add(new Float(j));
					vertices.add(new Float(1.0));
					texCoords.add(new Float(1.0));
					texCoords.add(new Float(0.0));
					
					vertices.add(new Float(i+1.0));
					vertices.add(new Float(j+1.0));
					vertices.add(new Float(1.0));
					texCoords.add(new Float(1.0));
					texCoords.add(new Float(1.0));
					
					vertices.add(new Float(i));
					vertices.add(new Float(j+1.0));
					vertices.add(new Float(1.0));
					texCoords.add(new Float(0.0));
					texCoords.add(new Float(1.0));
					
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
