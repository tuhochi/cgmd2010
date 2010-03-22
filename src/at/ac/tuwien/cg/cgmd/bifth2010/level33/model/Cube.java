package at.ac.tuwien.cg.cgmd.bifth2010.level33.model;

import javax.microedition.khronos.opengles.GL10;

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

	public Cube(GL10 gl){
		
		 super( gl, 36, false, false, false );  
		 
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
		 
		 this.vertex( v0 ); this.vertex( v3 ); this.vertex( v1 );	
		 this.vertex( v3 ); this.vertex( v2 ); this.vertex( v1 );	
		 
		 this.vertex( v2 ); this.vertex( v3 ); this.vertex( v6 );	
		 this.vertex( v3 ); this.vertex( v6 ); this.vertex( v7 );	
		 
		 this.vertex( v0 ); this.vertex( v3 ); this.vertex( v7 );	
		 this.vertex( v0 ); this.vertex( v4 ); this.vertex( v7 );	
		 
		 this.vertex( v4 ); this.vertex( v5 ); this.vertex( v7 );	
		 this.vertex( v5 ); this.vertex( v6 ); this.vertex( v7 );	
		 
		 this.vertex( v0 ); this.vertex( v1 ); this.vertex( v4 );	
		 this.vertex( v1 ); this.vertex( v4 ); this.vertex( v5 );	
		 
		 this.vertex( v1 ); this.vertex( v2 ); this.vertex( v6 );	
		 this.vertex( v1 ); this.vertex( v5 ); this.vertex( v6 );	
		 
	}


	

		
		


}
