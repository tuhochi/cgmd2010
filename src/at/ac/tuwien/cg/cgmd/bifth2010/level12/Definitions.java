package at.ac.tuwien.cg.cgmd.bifth2010.level12;

public final class Definitions {
	public static final int SOUND_CHANNEL_NUMBER = 25;
	public static final int ANIMTE_CYCLE_TIME = 250;
	
	public static int FIELD_SEGMENT_LENGTH = 10;
	public static final short FIELD_HEIGHT_SEGMENTS = 5;	
	public static final int COLLISION_DETECTION_TIMEOUT = 100;
	
	//Game Mechanics:
	public static final int STARTING_MONEY = 0;
	public static final int STARTING_IRON = 30;
	public static final int GAME_ROUND_WAIT_TIME = 20000;
	public static final int GAME_START_TIME = 5000;
	public static final short MAX_ROUND_NUMBER = 5;
	
	//Number of lanes:
	public static final short LANE_COUNT = 5;
	
	//Towers:
	public static final short BASIC_TOWER = 0;
	public static final short ADVANCED_TOWER = 1;
	public static final short HYPER_TOWER = 2;
	public static final int FREEZE_TOWER = 3;
	
	//Tower Counts:
	public static final short BASIC_TOWER_POOL = 10;
	public static final short ADVANCED_TOWER_POOL = 10;
	public static final short HYPER_TOWER_POOL = 10;
	public static final int FREEZE_TOWER_POOL = 10;
	
		//Basic Tower:
		public static final short BASIC_TOWER_RADIUS = 35;
		public static final short BASIC_PROJECTILE_POOL = 1;
		//Basic Tower Projectiles:
		public static final short BASIC_PROJECTILE_SPEED = 60;
		public static final short BASIC_PROJECTILE_RADIUS = 15;
		
		//Advanced Tower:
		public static final short ADVANCED_TOWER_RADIUS = 35;
		public static final short ADVANCED_PROJECTILE_POOL = 1;
		//Advanced Tower Projectiles:
		public static final short ADVANCED_PROJECTILE_SPEED = 50;
		public static final short ADVANCED_PROJECTILE_RADIUS = 25;
		
		//Hyper Tower:
		public static final short HYPER_TOWER_RADIUS = 35;
		public static final short HYPER_PROJECTILE_POOL = 1;
		//Hyper Tower Projectiles:
		public static final short HYPER_PROJECTILE_SPEED = 75;
		public static final short HYPER_PROJECTILE_RADIUS = 17;
		
		//Freeze Tower:
		public static final short FREEZE_TOWER_RADIUS = 35;
		public static final short FREEZE_PROJECTILE_POOL = 1;
		//Freeze Tower Projectiles:
		public static final short FREEZE_PROJECTILE_SPEED = 75;
		public static final short FREEZE_PROJECTILE_RADIUS = 17;
		
		
		//Damage
		public static final int BASIC_TOWER_SHOOTING_INTERVALL = 50;
		public static final short BASIC_PROJECTILE_DAMAGE = 10;
	
		public static final int ADVANCED_TOWER_SHOOTING_INTERVALL = 50;
		public static final short ADVANCED_PROJECTILE_DAMAGE = 30;
	
		public static final int HYPER_TOWER_SHOOTING_INTERVALL = 50;
		public static final short HYPER_PROJECTILE_DAMAGE = 60;
		
		public static final int FREEZE_TOWER_SHOOTING_INTERVALL = 50;
		public static final short FREEZE_PROJECTILE_DAMAGE = 0;
		public static final short FREEZE_PROJECTILE_SLOWING = 70;
		
		//Price
		public static final short BASIC_TOWER_IRON_NEED = 10;
		public static final short ADVANCED_TOWER_IRON_NEED = 30;
		public static final short HYPER_TOWER_IRON_NEED = 60;
		public static final short FREEZE_TOWER_IRON_NEED = 60;
			
		
	//Enemies:	
	public static final short CARRIER_POOL = 5;

	
	//Enemies:
	public static final short FIRST_ROUND_ENEMIE_NUMBER = 5;
	public static final short SECOND_ROUND_ENEMIE_NUMBER = 10;
	public static final short THIRD_ROUND_ENEMIE_NUMBER = 7;
	public static final short FOURTH_ROUND_ENEMIE_NUMBER = 10;
	public static final int FIFTH_ROUND_ENEMIE_NUMBER = 12;
	public static final int SIXTH_ROUND_ENEMIE_NUMBER = 5;
	public static final int SEVENTH_ROUND_ENEMIE_NUMBER = 15;

		//Round one
		public static final short FIRST_ROUND_ENEMIE_HP = 10;
		public static final short FIRST_ROUND_ENEMIE_STRENGTH = 10;
		public static final short FIRST_ROUND_ENEMIE_RADIUS = 20;
		public static final short FIRST_ROUND_ENEMIE_MONEY = 30;
		public static final short FIRST_ROUND_ENEMIE_SPEED = 60;
		public static final short FIRST_ROUND_ENEMIE_IRON = 15;
	
		//Round Two
		public static final short SECOND_ROUND_ENEMIE_HP = 30;
		public static final short SECOND_ROUND_ENEMIE_STRENGTH = 20;
		public static final short SECOND_ROUND_ENEMIE_RADIUS = 25;
		public static final short SECOND_ROUND_ENEMIE_MONEY = 40;
		public static final short SECOND_ROUND_ENEMIE_SPEED = 70;
		public static final short SECOND_ROUND_ENEMIE_IRON = 40;
	
		//Round Three:
		public static final short THIRD_ROUND_ENEMIE_HP = 60;
		public static final short THIRD_ROUND_ENEMIE_STRENGTH = 30;
		public static final short THIRD_ROUND_ENEMIE_RADIUS = 20;
		public static final short THIRD_ROUND_ENEMIE_MONEY = 20;
		public static final short THIRD_ROUND_ENEMIE_SPEED = 70;
		public static final short THIRD_ROUND_ENEMIE_IRON = 80;
	
		//Round Four:
		public static final short FOURTH_ROUND_ENEMIE_HP = 120;
		public static final short FOURTH_ROUND_ENEMIE_STRENGTH = 40;
		public static final short FOURTH_ROUND_ENEMIE_RADIUS = 20;
		public static final short FOURTH_ROUND_ENEMIE_MONEY = 20;
		public static final short FOURTH_ROUND_ENEMIE_SPEED = 90;
		public static final short FOURTH_ROUND_ENEMIE_IRON = 150;
		
}
