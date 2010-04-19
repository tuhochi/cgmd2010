package at.ac.tuwien.cg.cgmd.bifth2010.level83;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11Ext;

/**
 * This class is used for to animate items in the {@link MyHexagonGrid}. It is 
 * an implementation of the {@link Animatable} and the {@link Drawable} 
 * interface.
 */
public class ItemAnimated extends Animatable implements Drawable {

	private final int BOMB_ANIMATION = 0;
	private final int NO_ANIMATION = -1;
	
	private int animation = NO_ANIMATION;
	private float endTime;
	private float currentTime;
	private float picTime;
	private boolean detonated = false;
	
	public int gridX,gridY;
	public float x,y,width,height;
	public float alpha=1.0f;
	
	public int[] textures;
	public int numofTextures;
	private int currentTexture = 0;
	
	public int itemId;
	
	public ItemAnimated(int[] textures, float width, float height,int itemId){
		super();
		this.textures = textures;
		this.numofTextures = textures.length;
		this.width = width;
		this.height = height;
		this.itemId = itemId;
	}
	
	public ItemAnimated(int[] resourceId, GL10 gl, float width, float height, int itemId) {
		super();
		numofTextures = resourceId.length;
		textures = new int[numofTextures];
		
		this.itemId = itemId;
		this.width = width;
		this.height = height;
		
		for(int i=0; i< numofTextures; i++){
			textures[i] = MyTextureManager.singleton.addTextureFromResources(resourceId[i], gl);
		}
	}

	/**
	 * Clones the current <code>ItemAnimated</code>.
	 * 
	 * @return	A clone of the current <code>ItemAnimated</code>.
	 */
	public ItemAnimated clone() {
		ItemAnimated item = new ItemAnimated(textures, this.width, this.height, this.itemId);
		return item;
	}
	
	public void addToGrid(AnimationManager manager, int x, int y) {
		this.gridX = x;
		this.gridY = y;
		manager.addAnimatable(this);
	}
	
	public ItemAnimated addCloneToGrid(AnimationManager manager, int x, int y) {
		ItemAnimated item = this.clone();
		item.gridX = x;
		item.gridY = y;
		manager.addAnimatable(item);
		return item;
	}
	
	public void removeFromAnimationManager(AnimationManager manager) {
		manager.removeAnimation(this);
	}
	
	@Override
	public void animate(float deltaTime) {
		switch(animation){
		case BOMB_ANIMATION:
			//Time per pic
			float tpp = 1f/(20f/endTime*currentTime + numofTextures);
			
			//Switch textures?
			if(picTime >= tpp){
				currentTexture = (currentTexture == numofTextures-1)?0:currentTexture+1;
				picTime = tpp-picTime;	//Zeit zur�cksetzen
			}
			
			if(currentTime >= endTime){
				detonated = true;
				animation = NO_ANIMATION;
			}
			currentTime += deltaTime;
			picTime += deltaTime;
			break;
			
		default:
			currentTexture = 0;
			break;
		}
	}
	
	/**
	 * Checks the animation status. If the animation has ended, true is 
	 * returned and the status is reset.
	 * 
	 * @return animationStatus	Status of the animation. True if the animation 
	 * 							has ended, false otherwise. Once true has been 
	 * 							returned, the status is set to false again.
	 */
	public boolean checkStatus(){
		if(detonated){
			detonated = false;
			return true;
		}
		return false;
	}
	
	/**
	 * Starts a bomb animation which ends after the given time 
	 * <code>time</code>.
	 * 
	 * @param time	Animation time in seconds.
	 */
	public void startAnimation(float time){
		
		animation = BOMB_ANIMATION;
		endTime = time;
		currentTime = 0;
		picTime = 0;
	}

	@Override
	public void Dispose(GL10 gl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Draw(GL10 gl) {
		MyTextureManager.singleton.textures[textures[currentTexture]].Bind(gl);
		((GL11Ext) gl).glDrawTexfOES(x,y,0,width, height);
	}

	@Override
	public void Init(GL10 gl) {
		// TODO Auto-generated method stub
		
	}

	

}