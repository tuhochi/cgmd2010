package at.ac.tuwien.cg.cgmd.bifth2010.level12.entities;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

/*
 * enemy class.
 * contains enemy properties that determine its behaviour
 * and all necessary display and interaction methods.
 * 
 * @author Johannes Sorger
 */
public class Zombie extends GLObject {
	//zombie properties
	private float xpos; //position on lane
	private float lane; //lane
	private float offset; //offset in x direction for collision detection
	private short hp;
	private double speed;
	public boolean active; //false when zombie is in queue
	private short strength; //how much damage it can do
	private int type; //zombie type

	private FloatBuffer mColorBuffer;
	
	/**
	 * class constructor. sets the enemy's status to inactive.
	 */
	public Zombie(){
		active = false;
		//TODO: vertex, texture setup => eher bei init wegen typ

		
		/*
		float[] vertices = {
				-0.5f, -0.5f, 0.0f,  //Vertex 0
				 0.5f, -0.5f, 0.0f,  //v1
				-0.5f,  0.5f, 0.0f,  //v2
				 0.5f,  0.5f, 0.0f,  //v3
		};
		ByteBuffer i = ByteBuffer.allocateDirect( vertices.length * 4 );
		i.order( ByteOrder.nativeOrder() );
		mVerticesBuffer = i.asFloatBuffer();
		mVerticesBuffer.put( vertices );
		mVerticesBuffer.position(0);
		
		short[] indices = {
				0,1,3, 0,3,2
		};
		ByteBuffer k = ByteBuffer.allocateDirect( indices.length * 2 );
		k.order( ByteOrder.nativeOrder() );
		mIndicesBuffer = k.asShortBuffer();
		mIndicesBuffer.put( indices );
		mIndicesBuffer.position(0);
		*/
		
		float vertices[] = {
				 1.0f,   1.0f, 0.0f, 
				-1.0f,  -1.0f, 0.0f,
				 1.0f,  -1.0f, 0.0f,
		};
		ByteBuffer vertbuf = ByteBuffer.allocateDirect( vertices.length * 4 ); // 4 = sizeof(int)
		vertbuf.order( ByteOrder.nativeOrder());
		mVerticesBuffer = vertbuf.asFloatBuffer();
		mVerticesBuffer.put( vertices );	
		mVerticesBuffer.position( 0 );
		

		short[] indices = { 0, 1, 2 };
		
		ByteBuffer indbuf = ByteBuffer.allocateDirect( indices.length * 2 ); // 2 = sizeof(short)
		indbuf.order( ByteOrder.nativeOrder());
		mIndicesBuffer = indbuf.asShortBuffer();
		mIndicesBuffer.put( indices );
		mIndicesBuffer.position( 0 );	
		//mIndicesLength = indices.length;
		
		float[] colors = {
				1.0f, 0.0f, 0.0f, 1.0f,
				0.0f, 1.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f, 1.0f	
		};
		ByteBuffer cbb = ByteBuffer.allocateDirect( colors.length * 4 );
		cbb.order( ByteOrder.nativeOrder() );
		mColorBuffer = cbb.asFloatBuffer();
		mColorBuffer.put( colors );
		mColorBuffer.position( 0 );
	}
	
	/**
	 * called when an enemy is picked from the queue.
	 * sets it's status as active and initializes it according to type.
	 *  
	 * @param x - enemy type
	 * @param y - sets the lane on which the enemy is moving
	 */
	public void initZombie(float x, float y, int type){
		active = true;
		this.lane = y;
		this.type = type;
		xpos = x; //TODO: placeholder
		if(x == 0){
			offset = 5;  //TODO: only placeholder values
			hp = 100;
			speed = 0.01;
			strength = 5;
			
		}
		else if(x == 1){
			offset = 5;  //TODO: only placeholder values
			hp = 100;
			speed = 0.04;
			strength = 5;
			
		}
		else if(x == 2){
			offset = 5;  //TODO: only placeholder values
			hp = 100;
			speed = 0.07;
			strength = 5;
			
		}
	}
	
	/**
	 * when an enemy object is not needed anymore it is deactivated before
	 * being returned to the queue.
	 */
	public void deactivateZombie(){
		active = false;
	}
	
	/**
	 * draws the enemy object according to its position.
	 */
	public void draw(GL10 gl){
		//pushmatrix => translate zu position => super.draw() => popmatrix?
		gl.glEnableClientState( GL10.GL_VERTEX_ARRAY );
		int pointDivisor = 3; //x,y,z
		gl.glVertexPointer( pointDivisor, GL10.GL_FLOAT, 0, mVerticesBuffer);
		
		gl.glEnableClientState( GL10.GL_COLOR_ARRAY );
		int colorDivisor = 4; //r,g,b,a
		gl.glColorPointer( colorDivisor, GL10.GL_FLOAT, 0, mColorBuffer );	

		gl.glDrawElements(GL10.GL_TRIANGLES, 3, GL10.GL_UNSIGNED_SHORT, mIndicesBuffer );
		
		gl.glDisableClientState( GL10.GL_COLOR_ARRAY );
		gl.glLineWidth(4);
		gl.glColor4f(1.0f, 0.5f, 0.7f, 1.0f);
		gl.glDrawElements(GL10.GL_LINE_LOOP, 3, GL10.GL_UNSIGNED_SHORT, mIndicesBuffer);
		gl.glDisableClientState( GL10.GL_VERTEX_ARRAY );
		/*
		//TODO
		gl.glPushMatrix();
		gl.glTranslatef(xpos, lane, 0);
		gl.glEnableClientState( GL10.GL_VERTEX_ARRAY );
		//int pointDivisor = 3; //x,y,z
		gl.glVertexPointer( 3, GL10.GL_FLOAT, 0, mVertexBuffer);
		
		gl.glEnableClientState( GL10.GL_COLOR_ARRAY );
		//int colorDivisor = 4; //r,g,b,a
		gl.glColorPointer( 4, GL10.GL_FLOAT, 0, mColorBuffer );	

		gl.glDrawElements(GL10.GL_TRIANGLES, indexcount, GL10.GL_UNSIGNED_SHORT, mIndicesBuffer );
		
		gl.glDisableClientState( GL10.GL_COLOR_ARRAY );
		gl.glLineWidth(4);
		gl.glColor4f(1.0f, 0.5f, 0.7f, 1.0f);
		gl.glDrawElements(GL10.GL_LINE_LOOP, indexcount, GL10.GL_UNSIGNED_SHORT, mIndicesBuffer);
		gl.glDisableClientState( GL10.GL_VERTEX_ARRAY );
		gl.glPopMatrix();
		*/
	}
	
	/**
	 * moves enemy object according to speed.
	 */
	public void move(){
		xpos += speed;
	}
}
