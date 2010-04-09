package at.ac.tuwien.cg.cgmd.bifth2010.level42.scene;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Config;

public class Scene
{
	public static final int RENDERMODE_VERTEXARRAY = 0;
	public static final int RENDERMODE_VBO = 1;
	
	public final ArrayList<SceneEntity> sceneEntities;
	private int rendermode;
	private boolean initialized;
		
	public Scene()
	{
		sceneEntities = new ArrayList<SceneEntity>();
		initialized = false;
	}
	
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
	
	public void deInit()
	{
		initialized = false;
		
		ArrayList<SceneEntity> sceneEntities = this.sceneEntities;
		int size = sceneEntities.size();
		for(int i=0; i<size; i++)
			sceneEntities.get(i).deInit();
	}
	
	public void persist(DataOutputStream dos) throws IOException
	{
		ArrayList<SceneEntity> sceneEntities = this.sceneEntities;
		int size = sceneEntities.size();
		for(int i=0; i<size; i++)
			sceneEntities.get(i).persist(dos);
	}
	
	public void restore(DataInputStream dis) throws IOException
	{
		ArrayList<SceneEntity> sceneEntities = this.sceneEntities;
		int size = sceneEntities.size();
		for(int i=0; i<size; i++)
			sceneEntities.get(i).restore(dis);
	}
	
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
	
	public void update()
	{
		int size = sceneEntities.size();
		for(int i=0;i<size;i++)
			sceneEntities.get(i).update();
	}
	
	public void add(SceneEntity sceneEntity)
	{
		sceneEntities.add(sceneEntity);
	}
	
	public SceneEntity getSceneEntity(int index)
	{
		return sceneEntities.get(index);
	}
}
