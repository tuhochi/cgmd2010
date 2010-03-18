package at.ac.tuwien.cg.cgmd.bifth2010.level42.scene;

import java.util.ArrayList;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Config;

//static imports
import static android.opengl.GLES10.*;

public class Scene
{
	public static final int RENDERMODE_VERTEXARRAY = 0;
	public static final int RENDERMODE_VBO = 1;
	
	private ArrayList<Model> entities;
	private int rendermode;
	
	public Scene()
	{
		entities = new ArrayList<Model>();
		rendermode = Config.GLES11 ? RENDERMODE_VBO : RENDERMODE_VERTEXARRAY;
	}
	
	public void render()
	{
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_NORMAL_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        
		for(Model m : entities)
			m.render(rendermode);
		
		glDisableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_NORMAL_ARRAY);
		glDisableClientState(GL_TEXTURE_COORD_ARRAY);
	}
	
	public void addModel(Model model)
	{
		entities.add(model);
	}
}
