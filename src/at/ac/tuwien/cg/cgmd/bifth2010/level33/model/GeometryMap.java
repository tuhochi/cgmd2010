package at.ac.tuwien.cg.cgmd.bifth2010.level33.model;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Color;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector3f;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.model.Geometry.Type;

public class GeometryMap extends Geometry {
	Geometry way;
	
	float one = 1.0f/4;
	Vector3f v0 = new Vector3f(-one, -one, 0);
	Vector3f v1 = new Vector3f(one, -one, 0);
	Vector3f v2 = new Vector3f(one,  one, 0);
	Vector3f v3 = new Vector3f(-one,  one, 0);


	Color w = new Color(0,0.6f,0);

	public GeometryMap(GL10 gl,Geometry way){
		super( gl, Type.Triangles, 6, true, false, false ,null); 
		this.way=way;
		 
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
	
	public void render() {
		way.render();
		super.render();
	}
	
}
