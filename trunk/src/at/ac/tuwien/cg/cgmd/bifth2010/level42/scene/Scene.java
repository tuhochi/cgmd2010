package at.ac.tuwien.cg.cgmd.bifth2010.level42.scene;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Config;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Persistable;

/**
 * The Class Scene.
 *
 * @author Alex Druml
 * @author Lukas Roessler
 */
public class Scene implements Persistable
{
	/** The Constant RENDERMODE_VERTEXARRAY. */
	public static final int RENDERMODE_VERTEXARRAY = 0;
	
	/** The Constant RENDERMODE_VBO. */
	public static final int RENDERMODE_VBO = 1;
	
	/** The scene entities. */
	public final ArrayList<SceneEntity> sceneEntities;
	
	/** The rendermode. */
	private int rendermode;

	/** if this is initialized. */
	private boolean initialized;
		
	/**
	 * Instantiates a new scene.
	 */
	public Scene()
	{
		sceneEntities = new ArrayList<SceneEntity>();
		initialized = false;
	}
	
	/**
	 * Inits the scene
	 */
	public void init()
	{
		if(!initialized)
		{
			rendermode = Config.GLES11 ? RENDERMODE_VBO : RENDERMODE_VERTEXARRAY;

			ArrayList<SceneEntity> sceneEntities = this.sceneEntities;
			int size = sceneEntities.size();
			for(int i=0; i<size; i++)
				sceneEntities.get(i).init();

			initialized = true;
		}
	}
	
	/**
	 * De-inits the scene
	 */
	public void deInit()
	{
		initialized = false;
		
		ArrayList<SceneEntity> sceneEntities = this.sceneEntities;
		int size = sceneEntities.size();
		for(int i=0; i<size; i++)
			sceneEntities.get(i).deInit();
	}
	
	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Persistable#persist(java.io.DataOutputStream)
	 */
	public void persist(DataOutputStream dos) throws IOException
	{
		ArrayList<SceneEntity> sceneEntities = this.sceneEntities;
		int size = sceneEntities.size();
		for(int i=0; i<size; i++)
			sceneEntities.get(i).persist(dos);
	}
	
	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Persistable#restore(java.io.DataInputStream)
	 */
	public void restore(DataInputStream dis) throws IOException
	{
		ArrayList<SceneEntity> sceneEntities = this.sceneEntities;
		int size = sceneEntities.size();
		for(int i=0; i<size; i++)
			sceneEntities.get(i).restore(dis);
	}
	
	/**
	 * Render.
	 */
	public void render()
	{
		if(!initialized)
			init();
		SceneEntity m;
		int size = sceneEntities.size();
		for(int i=0;i<size;i++)
		{
			m = sceneEntities.get(i);
			m.render(rendermode);
		}
	}
	
	/**
	 * Update.
	 */
	public void update()
	{
		int size = sceneEntities.size();
		for(int i=0;i<size;i++)
			sceneEntities.get(i).update();
	}
	
	/**
	 * Adds a SceneEntity to the Scene
	 *
	 * @param sceneEntity the scene entity
	 */
	public void add(SceneEntity sceneEntity)
	{
		sceneEntities.add(sceneEntity);
	}
	
	/**
	 * Gets the index'th scene entity.
	 *
	 * @param index the index
	 * @return the scene entity
	 */
	public SceneEntity getSceneEntity(int index)
	{
		return sceneEntities.get(index);
	}
}
