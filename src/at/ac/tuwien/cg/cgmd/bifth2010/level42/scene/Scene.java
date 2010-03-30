package at.ac.tuwien.cg.cgmd.bifth2010.level42.scene;

import java.util.ArrayList;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Config;

public class Scene
{
	public static final int RENDERMODE_VERTEXARRAY = 0;
	public static final int RENDERMODE_VBO = 1;
	
	private final ArrayList<SceneEntity> sceneEntities;
	private final int rendermode;
		
	public Scene()
	{
		sceneEntities = new ArrayList<SceneEntity>();
		rendermode = Config.GLES11 ? RENDERMODE_VBO : RENDERMODE_VERTEXARRAY;
	}
	
	public void render()
	{
		SceneEntity m;
		int size = sceneEntities.size();
		for(int i=0;i<size;i++)
		{
			m = sceneEntities.get(i);
			m.render(rendermode);
		}
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
