package at.ac.tuwien.cg.cgmd.bifth2010.level12;

public final class Definitions {
	public static final int SOUND_CHANNEL_NUMBER = 25; /** how many soundchannels are created for playing sound from the soundpool */
	public static final int ANIMTE_CYCLE_TIME = 250; /** ms to change to the next texture in an animation cycle */
	
	public static int FIELD_SEGMENT_LENGTH = 10; /** field segments in the x-directions */
	public static final short FIELD_HEIGHT_SEGMENTS = 5;	/** lanes == segments in the y-direction */
	public static final int COLLISION_DETECTION_TIMEOUT = 100; /** every .. ms the collision detection is done */
	

	public static final int STARTING_MONEY = 0; /** Money the player "wins" at the beginning */
	public static final int STARTING_IRON = 30; /** Iron the player starts width */
	public static final int GAME_ROUND_WAIT_TIME = 20000; /** wait time btween the rounds */
	public static final int GAME_START_TIME = 5000; /** timer when the first wave of enemies spawns */
	public static final short MAX_ROUND_NUMBER = 5; /** number of rounds */
	
	/** tower IDS for the buttons and setting a tower */
	public static final short BASIC_TOWER = 0;
	public static final short ADVANCED_TOWER = 1;
	public static final short HYPER_TOWER = 2;
	public static final int FREEZE_TOWER = 3;
	
	/** tower pool amount */
	public static final short BASIC_TOWER_POOL = 10;
	public static final short ADVANCED_TOWER_POOL = 10;
	public static final short HYPER_TOWER_POOL = 10;
	public static final int FREEZE_TOWER_POOL = 10;
	
		/** basic tower values */
		public static final short BASIC_TOWER_RADIUS = 35; /** radius of the quad */
		public static final short BASIC_PROJECTILE_POOL = 1; /** pool of projectiles created */
		public static final short BASIC_PROJECTILE_SPEED = 60; /** speed of the projectile */
		public static final short BASIC_PROJECTILE_RADIUS = 15; /** radius of the projectile quad */
		public static final int BASIC_TOWER_SHOOTING_INTERVALL = 500; /** shooting intervall of the tower */
		public static final short BASIC_PROJECTILE_DAMAGE = 10; /** projectile damage */
		public static final short BASIC_TOWER_IRON_NEED = 10; /** price of a basic tower */
		
		/**advanced tower value */
		public static final short ADVANCED_TOWER_RADIUS = 35;
		public static final short ADVANCED_PROJECTILE_POOL = 1;
		public static final short ADVANCED_PROJECTILE_SPEED = 50;
		public static final short ADVANCED_PROJECTILE_RADIUS = 25;
		public static final int ADVANCED_TOWER_SHOOTING_INTERVALL = 500;
		public static final short ADVANCED_PROJECTILE_DAMAGE = 30;
		public static final short ADVANCED_TOWER_IRON_NEED = 30;
		
		/** hyper tower values */
		public static final short HYPER_TOWER_RADIUS = 35;
		public static final short HYPER_PROJECTILE_POOL = 1;
		public static final short HYPER_PROJECTILE_SPEED = 75;
		public static final short HYPER_PROJECTILE_RADIUS = 17;
		public static final int HYPER_TOWER_SHOOTING_INTERVALL = 500;
		public static final short HYPER_PROJECTILE_DAMAGE = 60;
		public static final short HYPER_TOWER_IRON_NEED = 60;
		
		/** freeze tower values */
		public static final short FREEZE_TOWER_RADIUS = 35;
		public static final short FREEZE_PROJECTILE_POOL = 1;
		public static final short FREEZE_PROJECTILE_SPEED = 75;
		public static final short FREEZE_PROJECTILE_RADIUS = 17;
		public static final int FREEZE_TOWER_SHOOTING_INTERVALL = 500;
		public static final short FREEZE_PROJECTILE_DAMAGE = 0;
		public static final short FREEZE_PROJECTILE_SLOWING = 70; /** how much percent is the enemie slown */
		public static final short FREEZE_TOWER_IRON_NEED = 60;
	
		
	//Enemies:	
	//public static final short CARRIER_POOL = 5; 

	
	/** enemy ids */
	public static final short FIRST_ROUND_ENEMIE_NUMBER = 6;
	public static final short SECOND_ROUND_ENEMIE_NUMBER = 10;
	public static final short THIRD_ROUND_ENEMIE_NUMBER = 7;
	public static final short FOURTH_ROUND_ENEMIE_NUMBER = 10;
	public static final int FIFTH_ROUND_ENEMIE_NUMBER = 12;
	public static final int SIXTH_ROUND_ENEMIE_NUMBER = 5;
	public static final int SEVENTH_ROUND_ENEMIE_NUMBER = 15;

		/** first enemy stats */
		public static final short FIRST_ROUND_ENEMIE_HP = 10; /** hitpoints */
		public static final short FIRST_ROUND_ENEMIE_RADIUS = 55; /** quad radius */
		public static final short FIRST_ROUND_ENEMIE_MONEY = 50; /** money the enemy carries */
		public static final short FIRST_ROUND_ENEMIE_SPEED = 60; /** speed at which the enemy travels */
		public static final short FIRST_ROUND_ENEMIE_IRON = 15; /** iron dropped when the enemy is killed */
	
		/** seconds enemy stats */
		public static final short SECOND_ROUND_ENEMIE_HP = 30;
		public static final short SECOND_ROUND_ENEMIE_STRENGTH = 20;
		public static final short SECOND_ROUND_ENEMIE_RADIUS = 55;
		public static final short SECOND_ROUND_ENEMIE_MONEY = 50;
		public static final short SECOND_ROUND_ENEMIE_SPEED = 70;
		public static final short SECOND_ROUND_ENEMIE_IRON = 40;
	
		//Round Three:
		public static final short THIRD_ROUND_ENEMIE_HP = 60;
		public static final short THIRD_ROUND_ENEMIE_STRENGTH = 30;
		public static final short THIRD_ROUND_ENEMIE_RADIUS = 55;
		public static final short THIRD_ROUND_ENEMIE_MONEY = 50;
		public static final short THIRD_ROUND_ENEMIE_SPEED = 70;
		public static final short THIRD_ROUND_ENEMIE_IRON = 70;
	
		//Round Four:
		public static final short FOURTH_ROUND_ENEMIE_HP = 150;
		public static final short FOURTH_ROUND_ENEMIE_STRENGTH = 40;
		public static final short FOURTH_ROUND_ENEMIE_RADIUS = 60;
		public static final short FOURTH_ROUND_ENEMIE_MONEY = 50;
		public static final short FOURTH_ROUND_ENEMIE_SPEED = 90;
		public static final short FOURTH_ROUND_ENEMIE_IRON = 120;
		
}
