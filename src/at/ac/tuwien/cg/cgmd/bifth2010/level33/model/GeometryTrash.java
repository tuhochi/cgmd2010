package at.ac.tuwien.cg.cgmd.bifth2010.level33.model;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Color;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector3f;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.model.Geometry.Type;

public class GeometryTrash extends Geometry {

	
	float one = 1.0f/4;
	Vector3f v0 = new Vector3f(-one, -one, 0);
	Vector3f v1 = new Vector3f(one, -one, 0);
	Vector3f v2 = new Vector3f(one,  one, 0);
	Vector3f v3 = new Vector3f(-one,  one, 0);


	Color w = new Color(0,1,0);

	public GeometryTrash(GL10 gl){
		super( gl, Type.Triangles, 6, true, false, false,null ); 
		 
		 // simple Quad //
		 //    
		 //      0--------3
		 //     /        /
		 //    /        /
		 //   1--------2
		 
		 this.color(w);this.color(w);this.color(w); this.color(w);this.color(w);this.color(w);
		 this.vertex( v0 ); this.vertex( v3 ); this.vertex( v1 );	
		 this.vertex( v3 ); this.vertex( v2 ); this.vertex( v1 );	
		
	}

}
