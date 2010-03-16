package at.ac.tuwien.cg.cgmd.bifth2010.level42.scene;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

//static imports
import static android.opengl.GLES10.*;

public class Scene
{
	public static final int RENDERMODE_VERTEXARRAY = 0;
	public static final int RENDERMODE_VBO = 1;
	public static final int rendermode = RENDERMODE_VERTEXARRAY;
	
	private ArrayList<Model> entities;
	
	public Scene()
	{
		entities = new ArrayList<Model>();
	}
	
	public void render()
	{
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_NORMAL_ARRAY);
		//glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		
		
		//material test
		float matAmbient[] = new float[] { 0.1f, 0.1f, 0.1f, 1.0f };
		float matDiffuse[] = new float[] { 0.9f, 0.1f, 0.1f, 1.0f };
		
		glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, matAmbient, 0);
        glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, matDiffuse, 0);
        /// 
        
		for(Model m : entities)
			m.render(rendermode);
		
		
		glDisableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_NORMAL_ARRAY);
		//glDisableClientState(GL_TEXTURE_COORD_ARRAY);
	}
	
	public void addModel(Model model)
	{
		entities.add(model);
	}
}
