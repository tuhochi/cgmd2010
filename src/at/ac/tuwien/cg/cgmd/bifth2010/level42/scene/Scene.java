package at.ac.tuwien.cg.cgmd.bifth2010.level42.scene;

import java.util.ArrayList;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Orbit;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.OrbitManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Config;

public class Scene
{
	public static final int RENDERMODE_VERTEXARRAY = 0;
	public static final int RENDERMODE_VBO = 1;
	
	private ArrayList<SceneEntity> sceneEntities;
	private int rendermode;

	private OrbitManager orbitManager;
	private Orbit tempOrbit;
	
	public Scene()
	{
		sceneEntities = new ArrayList<SceneEntity>();
		rendermode = Config.GLES11 ? RENDERMODE_VBO : RENDERMODE_VERTEXARRAY;

		orbitManager = OrbitManager.getInstance();
	}
	
	public void render()
	{
		for(SceneEntity m : sceneEntities)
		{
			//todo
			tempOrbit = orbitManager.getOrbit(m.name);
			if(tempOrbit != null)
				m.setTransformation(tempOrbit.getTransform());
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
