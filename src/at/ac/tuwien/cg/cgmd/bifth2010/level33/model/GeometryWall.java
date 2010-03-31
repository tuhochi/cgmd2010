package at.ac.tuwien.cg.cgmd.bifth2010.level33.model;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Color;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector3f;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.model.Geometry.Type;

public class GeometryWall extends Geometry {
	float one = 1.0f/2;
	Vector3f v0 = new Vector3f(-one, -one, -one);
	Vector3f v1 = new Vector3f(one, -one, -one);
	Vector3f v2 = new Vector3f(one,  one, -one);
	Vector3f v3 = new Vector3f(-one,  one, -one);
	Vector3f v4 = new Vector3f(-one, -one,  one);
	Vector3f v5 = new Vector3f(one, -one,  one);
	Vector3f v6 = new Vector3f(one,  one,  one);
	Vector3f v7 = new Vector3f(-one,  one,  one);
	
	Color b = new Color(0,0,1);
	Color r = new Color(1,0,0);
	Color g = new Color(0,1,0);

	public GeometryWall(GL10 gl){
		
		 super( gl, Type.Triangles, 36, true, false, false ,null);  
		 
		 // simple cube //
		 //      4--------7
		 //     /I       /I
		 //    / I      / I
		 //   5--------6  I
		 //   I  I     I  I
		 //   I  0--------3
		 //   I /      I /
		 //   I/       I/
		 //   1--------2
		 
		 this.color(g);this.color(g);this.color(g); this.color(g);this.color(g);this.color(g);
		 this.vertex( v0 ); this.vertex( v1 ); this.vertex( v4 );	
		 this.vertex( v1 ); this.vertex( v4 ); this.vertex( v5 );	

		 this.color(g);this.color(g);this.color(g); this.color(g);this.color(g);this.color(g);
		 this.vertex( v2 ); this.vertex( v3 ); this.vertex( v6 );	
		 this.vertex( v3 ); this.vertex( v6 ); this.vertex( v7 );

		 this.color(r);this.color(r);this.color(r); this.color(r);this.color(r);this.color(r);
		 this.vertex( v0 ); this.vertex( v3 ); this.vertex( v1 );	
		 this.vertex( v3 ); this.vertex( v2 ); this.vertex( v1 );	

		 this.color(b);this.color(b);this.color(b); this.color(b);this.color(b);this.color(b);
		 this.vertex( v0 ); this.vertex( v3 ); this.vertex( v7 );	
		 this.vertex( v0 ); this.vertex( v4 ); this.vertex( v7 );	
		 
		 this.color(r);this.color(r);this.color(r); this.color(r);this.color(r);this.color(r);
		 this.vertex( v4 ); this.vertex( v5 ); this.vertex( v7 );	
		 this.vertex( v5 ); this.vertex( v6 ); this.vertex( v7 );	
		 
		 this.color(b);this.color(b);this.color(b); this.color(b);this.color(b);this.color(b);
		 this.vertex( v1 ); this.vertex( v2 ); this.vertex( v6 );	
		 this.vertex( v1 ); this.vertex( v5 ); this.vertex( v6 );	
	}
		 
}
