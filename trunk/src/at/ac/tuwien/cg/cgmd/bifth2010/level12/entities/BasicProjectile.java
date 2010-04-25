package at.ac.tuwien.cg.cgmd.bifth2010.level12.entities;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.TextureManager;


public class BasicProjectile extends Projectile {
	
	public BasicProjectile( float speed, short dmg ){
		super( speed, dmg );
	}
	
	public BasicProjectile( float speed, short dmg, float x, float y ){
		super( speed, dmg );
		super.setXY(x, y);
	}
	
	public void draw(GL10 gl){
		TextureManager.getSingletonObject().setTexture(R.drawable.l12_icon);
		super.draw(gl);
	}
}
