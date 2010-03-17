package at.ac.tuwien.cg.cgmd.bifth2010.level42.scene;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Color4;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector2;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;

public class Cube extends Model
{
	public Cube()
	{
		super();
		Vector3[] vs = new Vector3[36];
		Vector3[] ns = new Vector3[36];
		Vector2[] ts = new Vector2[36];
		
		/*
		 *    8-----7
		 *   /     /|		y+
		 *  /     / |		^    z-
		 * 4-----3  6		|   ^
		 * |     | /		|  /
		 * |     |/			| /
		 * 1-----2			0------>x+
		 */
		Vector3 v1 = new Vector3(-1,-1, 1);
		Vector3 v2 = new Vector3( 1,-1, 1);
		Vector3 v3 = new Vector3( 1, 1, 1);
		Vector3 v4 = new Vector3(-1, 1, 1);
		Vector3 v5 = new Vector3(-1,-1,-1);
		Vector3 v6 = new Vector3( 1,-1,-1);
		Vector3 v7 = new Vector3( 1, 1,-1);
		Vector3 v8 = new Vector3(-1, 1,-1);
		Vector3 n1 = new Vector3( 0, 0, 1);	// front
		Vector3 n2 = new Vector3( 0, 0,-1);	// back
		Vector3 n3 = new Vector3( 0, 1, 0);	// top
		Vector3 n4 = new Vector3( 0,-1, 0);	// bottom
		Vector3 n5 = new Vector3(-1, 0, 0);	// left
		Vector3 n6 = new Vector3( 1, 0, 0);	// right
		Vector2 t1 = new Vector2(0,0);		// lower left
		Vector2 t2 = new Vector2(1,0);		// lower right
		Vector2 t3 = new Vector2(1,1);		// upper right
		Vector2 t4 = new Vector2(0,1);		// upper left
		
		int vcounter = 0;
		int ncounter = 0;
		int tcounter = 0;
		
		// Front Face
		vs[vcounter++] = v1;
		vs[vcounter++] = v2;
		vs[vcounter++] = v3;
		vs[vcounter++] = v1;
		vs[vcounter++] = v3;
		vs[vcounter++] = v4;
		for(int i=0; i<6; i++)
			ns[ncounter++] = n1;
		ts[tcounter++] = t1;
		ts[tcounter++] = t2;
		ts[tcounter++] = t3;
		ts[tcounter++] = t1;
		ts[tcounter++] = t3;
		ts[tcounter++] = t4;
		
		// Back Face
		vs[vcounter++] = v6;
		vs[vcounter++] = v5;
		vs[vcounter++] = v8;
		vs[vcounter++] = v6;
		vs[vcounter++] = v8;
		vs[vcounter++] = v7;
		for(int i=0; i<6; i++)
			ns[ncounter++] = n2;
		ts[tcounter++] = t1;
		ts[tcounter++] = t2;
		ts[tcounter++] = t3;
		ts[tcounter++] = t1;
		ts[tcounter++] = t3;
		ts[tcounter++] = t4;
		
		// Top Face
		vs[vcounter++] = v4;
		vs[vcounter++] = v3;
		vs[vcounter++] = v7;
		vs[vcounter++] = v4;
		vs[vcounter++] = v7;
		vs[vcounter++] = v8;
		for(int i=0; i<6; i++)
			ns[ncounter++] = n3;
		ts[tcounter++] = t1;
		ts[tcounter++] = t2;
		ts[tcounter++] = t3;
		ts[tcounter++] = t1;
		ts[tcounter++] = t3;
		ts[tcounter++] = t4;
		
		// Bottom Face
		vs[vcounter++] = v5;
		vs[vcounter++] = v6;
		vs[vcounter++] = v2;
		vs[vcounter++] = v5;
		vs[vcounter++] = v2;
		vs[vcounter++] = v1;
		for(int i=0; i<6; i++)
			ns[ncounter++] = n4;
		ts[tcounter++] = t1;
		ts[tcounter++] = t2;
		ts[tcounter++] = t3;
		ts[tcounter++] = t1;
		ts[tcounter++] = t3;
		ts[tcounter++] = t4;
		
		// Left Face
		vs[vcounter++] = v5;
		vs[vcounter++] = v1;
		vs[vcounter++] = v4;
		vs[vcounter++] = v5;
		vs[vcounter++] = v4;
		vs[vcounter++] = v8;
		for(int i=0; i<6; i++)
			ns[ncounter++] = n5;
		ts[tcounter++] = t1;
		ts[tcounter++] = t2;
		ts[tcounter++] = t3;
		ts[tcounter++] = t1;
		ts[tcounter++] = t3;
		ts[tcounter++] = t4;
		
		// Right Face
		vs[vcounter++] = v2;
		vs[vcounter++] = v6;
		vs[vcounter++] = v7;
		vs[vcounter++] = v2;
		vs[vcounter++] = v7;
		vs[vcounter++] = v3;
		for(int i=0; i<6; i++)
			ns[ncounter++] = n6;
		ts[tcounter++] = t1;
		ts[tcounter++] = t2;
		ts[tcounter++] = t3;
		ts[tcounter++] = t1;
		ts[tcounter++] = t3;
		ts[tcounter++] = t4;
		
		
		float[] vdata = new float[vs.length * 3];
		for(int i = 0; i < vs.length; i++)
		{
			vdata[i * 3] = vs[i].x;
			vdata[i * 3 + 1] = vs[i].y;
			vdata[i * 3 + 2] = vs[i].z;
		}
        ByteBuffer vbb = ByteBuffer.allocateDirect(vdata.length*4);
        vbb.order(ByteOrder.nativeOrder());
        vertices = vbb.asFloatBuffer();
        vertices.put(vdata);
        vertices.position(0);
        
        float[] ndata = new float[ns.length * 3];
		for(int i = 0; i < ns.length; i++)
		{
			ndata[i * 3] = ns[i].x;
			ndata[i * 3 + 1] = ns[i].y;
			ndata[i * 3 + 2] = ns[i].z;
		}
        vbb = ByteBuffer.allocateDirect(ndata.length*4);
        vbb.order(ByteOrder.nativeOrder());
        normals = vbb.asFloatBuffer();
        normals.put(ndata);
        normals.position(0);
        
        float[] tdata = new float[ts.length * 2];
		for(int i = 0; i < ts.length; i++)
		{
			tdata[i * 2] = ts[i].x;
			tdata[i * 2 + 1] = ts[i].y;
		}
        vbb = ByteBuffer.allocateDirect(tdata.length*4);
        vbb.order(ByteOrder.nativeOrder());
        texcoords = vbb.asFloatBuffer();
        texcoords.put(tdata);
        texcoords.position(0);

        numVertices = 36;
        
        materialName = materialManager.addMaterial(
        							"StripeBox",
        							new Color4(0.1f, 0.1f, 0.1f),
        							new Color4(0.9f, 0.1f, 0.1f),
        							new Color4(0.1f, 0.1f, 0.1f),
        							R.drawable.l42_box_without_alpha);
        //transformation.addRotateX(90);
        transformation.addRotateY(90);
	}
}
