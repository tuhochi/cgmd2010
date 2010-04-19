package at.ac.tuwien.cg.cgmd.bifth2010.level12.entities;


public class BasicProjectile extends Projectile {
	
	public BasicProjectile( float speed, short dmg ){
		super( speed, dmg );
	}
	
	public BasicProjectile( float speed, short dmg, float x, float y ){
		super( speed, dmg );
		super.setXY(x, y);
	}
}
