package at.ac.tuwien.cg.cgmd.bifth2010.level33.model;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Color;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector3f;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.model.Geometry.Type;

public class GeometryWay extends Geometry {
	
	float one = 1.0f/2;
	Vector3f v0 = new Vector3f(-one, -one, -one);
	Vector3f v1 = new Vector3f(one, -one, -one);
	Vector3f v2 = new Vector3f(one,  one, -one);
	Vector3f v3 = new Vector3f(-one,  one, -one);


	Color w = new Color(1,1,1);

	public GeometryWay(GL10 gl){
		
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
