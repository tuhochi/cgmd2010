package at.ac.tuwien.cg.cgmd.bifth2010.level33.model;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Color;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector3;

public class Cube extends Geometry {
	float one = 0.2f;
	Vector3 v0 = new Vector3(-one, -one, -one);
	Vector3 v1 = new Vector3(one, -one, -one);
	Vector3 v2 = new Vector3(one,  one, -one);
	Vector3 v3 = new Vector3(-one,  one, -one);
	Vector3 v4 = new Vector3(-one, -one,  one);
	Vector3 v5 = new Vector3(one, -one,  one);
	Vector3 v6 = new Vector3(one,  one,  one);
	Vector3 v7 = new Vector3(-one,  one,  one);
	
	Color b = new Color(0,0,1);
	Color r = new Color(1,0,0);
	Color g = new Color(0,1,0);

	public Cube(GL10 gl){
		
		 super( gl, 36, true, false, false );  
		 
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
		 
		 

		 
		 
		 
		 
		 
		 
//       g = new Geometry( gl, 6, true, false, false );           
//       
//       g.color( 0, 1, 0, 1 );
//       g.vertex( 0f, -0.5f, -5 );
//       g.color( 0, 1, 0, 1 );
//       g.vertex( 1f, -0.5f, -5 );
//       g.color( 0, 1, 0, 1 );
//       g.vertex( 0.5f, 0.5f, -5 );
//       
//       g.color( 1, 0, 0, 1 );
//       g.vertex( -0.5f, -0.5f, -2 );
//       g.color( 1, 0, 0, 1 );
//       g.vertex( 0.5f, -0.5f, -2 );
//       g.color( 1, 0, 0, 1 );
//       g.vertex( 0, 0.5f, -2);
		 
	}


	

		
		


}
