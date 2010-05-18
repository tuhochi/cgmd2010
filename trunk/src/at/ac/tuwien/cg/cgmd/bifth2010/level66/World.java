package at.ac.tuwien.cg.cgmd.bifth2010.level66;

import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

public class World implements Renderable {

	float _currentPosX;
	float _currentPosY;
	float _currentPosZ;
	
	float _currentRotX;
	float _currentRotY;
	float _currentRotZ;
	
	Vector<Model> _scene;
	
	public World(Context context)
	{
		_currentPosX = 0.0f;
		_currentPosY = 0.0f;
		_currentPosZ = 0.0f;
		
		_scene = new Vector<Model>();
		
		for(int x = 1; x <= 20; x++)
		{
			Model tree1 = new Tree( context );
			tree1.setPosX( -10 + (float)Math.random() * 4 - 2 );
			tree1.setPosY( -6 );
			tree1.setPosZ( -10 * x + (float)Math.random() * 2 - 1 );
			tree1.setScale( 0.5f + (float)Math.random() / 5 - 0.1f );
			_scene.add( tree1 );
			
			Model tree2  = new Tree( context );
			tree2.setPosX( 10 + (float)Math.random() * 4 - 2 );
			tree2.setPosY( -6 );
			tree2.setPosZ( -10 * x + (float)Math.random() * 2 - 1 );
			tree2.setScale( 0.5f + (float)Math.random() / 5 - 0.1f );
			_scene.add( tree2 );
		}
		
	}
	 
	public void update()
	{
		_currentPosZ = _currentPosZ + 1.0f;
		if( _currentPosZ >= 20.0f)
			_currentPosZ = 0.0f;
	}
	
	@Override
	public void render(GL10 gl) {
		
		gl.glLoadIdentity();
		
		gl.glTranslatef( -_currentPosX, -_currentPosY, _currentPosZ );
		
		for(Model model : _scene)
		{
			model.render(gl);
		}
	}

}
