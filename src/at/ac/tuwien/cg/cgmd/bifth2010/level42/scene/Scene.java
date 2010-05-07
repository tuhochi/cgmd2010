package at.ac.tuwien.cg.cgmd.bifth2010.level42.scene;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.AxisAlignedBox3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Color4;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Sphere;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.MaterialManager.Material;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Config;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.OGLManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Persistable;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.SceneLoader;

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
	
	public static final Model SPHERE = new Model();
	
	static
	{
		int spacing = 15;
		int thetaDegree = 360; // horizontal
		int phiDegree = 180; // vertical
		float[] temp = new float[12];						// 4 vertices with 3 floats each
		// (thetaDegree/spacing)							.. points per horizontal ring
		// (phiDegree/spacing)								.. points per vertical half-ring
		// (thetaDegree/spacing)*(phiDegree/spacing)		.. sum of points = sum of quads
		// ((thetaDegree/spacing)*(phiDegree/spacing))*6	.. sum of vertices (6 per quad)
		int vertexCount = ((thetaDegree/spacing)*(phiDegree/spacing))*6;
		float[] vertnormals = new float[vertexCount*3];
		int vertnormalsIndex = 0;
		
		// create sphere
		for(int theta = -(thetaDegree/2); theta < (thetaDegree/2); theta += spacing)
		{
			for(int phi = -(phiDegree/2); phi < (phiDegree/2); phi += spacing)
			{
				float phiR = (float)Math.toRadians(phi);
				float phi2R = (float)Math.toRadians(phi+spacing);
				float thetaR = (float)Math.toRadians(theta);
				float theta2R = (float)Math.toRadians(theta+spacing);
				
				// upper left vertex
				temp[ 0] = (float)(Math.cos(thetaR)*Math.cos(phiR)); 
				temp[ 1] = (float)(Math.sin(thetaR)*Math.cos(phiR));
				temp[ 2] = (float)(Math.sin(phiR));
				// lower left vertex
				temp[ 3] = (float)(Math.cos(thetaR)*Math.cos(phi2R));
				temp[ 4] = (float)(Math.sin(thetaR)*Math.cos(phi2R));
				temp[ 5] = (float)(Math.sin(phi2R));
				// lower right vertex
				temp[ 6] = (float)(Math.cos(theta2R)*Math.cos(phi2R));
				temp[ 7] = (float)(Math.sin(theta2R)*Math.cos(phi2R));
				temp[ 8] = (float)(Math.sin(phi2R));
				// upper right vertex
				temp[ 9] = (float)(Math.cos(theta2R)*Math.cos(phiR));
				temp[10] = (float)(Math.sin(theta2R)*Math.cos(phiR));
				temp[11] = (float)(Math.sin(phiR));
				
				// upper left vertex
				vertnormals[vertnormalsIndex++] = temp[0];
				vertnormals[vertnormalsIndex++] = temp[1];
				vertnormals[vertnormalsIndex++] = temp[2];
				
				// lower right vertex
				vertnormals[vertnormalsIndex++] = temp[6];
				vertnormals[vertnormalsIndex++] = temp[7];
				vertnormals[vertnormalsIndex++] = temp[8];
				
				// lower left vertex
				vertnormals[vertnormalsIndex++] = temp[3];
				vertnormals[vertnormalsIndex++] = temp[4];
				vertnormals[vertnormalsIndex++] = temp[5];
				
				// upper left vertex
				vertnormals[vertnormalsIndex++] = temp[0];
				vertnormals[vertnormalsIndex++] = temp[1];
				vertnormals[vertnormalsIndex++] = temp[2];
				
				// upper right vertex
				vertnormals[vertnormalsIndex++] = temp[9];
				vertnormals[vertnormalsIndex++] = temp[10];
				vertnormals[vertnormalsIndex++] = temp[11];
				
				// lower right vertex
				vertnormals[vertnormalsIndex++] = temp[6];
				vertnormals[vertnormalsIndex++] = temp[7];
				vertnormals[vertnormalsIndex++] = temp[8];
			}
		}
		
		FloatBuffer vertNormalBuffer = SceneLoader.instance.arrayToBuffer(vertnormals);
		Geometry sphereGeom = new Geometry(vertNormalBuffer, 
				vertNormalBuffer, 
				null, 
				new AxisAlignedBox3(new Vector3(-0.5f,-0.5f,-0.5f), new Vector3(0.5f,0.5f,0.5f)),
				new Sphere(new Vector3(0,0,0), 1),
				vertexCount);
		Material sphereMat = MaterialManager.instance.getMaterial("BoundingSphereMaterial",
				new Color4(0.0f, 0.5f, 0.0f, 0.1f),
				new Color4(0.0f, 0.5f, 0.0f, 0.1f),
				Color4.BLACK,
				Color4.BLACK,
				0,
				null);
		SPHERE.add(sphereGeom, sphereMat);
	}
	
	/** The OpenGL Manager. */
	private final OGLManager oglManager = OGLManager.instance;
	
	/** The scene entities. */
	public final ArrayList<SceneEntity> sceneEntities;
	
	/** The hud. */
	private HUD hud;
	
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
	 * Inits the scene.
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
			
			hud.init();
			SPHERE.init();

			oglManager.compileVBO();
			
			initialized = true;
		}
	}
	
	/**
	 * De-inits the scene.
	 */
	public void deInit()
	{
		initialized = false;
		
		ArrayList<SceneEntity> sceneEntities = this.sceneEntities;
		int size = sceneEntities.size();
		for(int i=0; i<size; i++)
			sceneEntities.get(i).deInit();
		
		hud.deInit();
		SPHERE.deInit();
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
		int size = sceneEntities.size();
		
		for(int i=0;i<size;i++)
			sceneEntities.get(i).render(rendermode);
		
		renderBoundingSpheres();
		
		hud.render(rendermode);
	}
	
	/**
	 * Render bounding spheres.
	 */
	private void renderBoundingSpheres()
	{
		int size = sceneEntities.size();
		for(int i=0;i<size;i++)
			sceneEntities.get(i).renderBoundingSpheres(rendermode);
	}
	
	/**
	 * Update.
	 */
	public void update()
	{
		int size = sceneEntities.size();
		for(int i=0;i<size;i++)
			sceneEntities.get(i).update();
		
		hud.update();
	}
	
	/**
	 * Adds a SceneEntity to the Scene.
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

	/**
	 * Sets the hud.
	 *
	 * @param hud the new hud
	 */
	public void setHud(HUD hud)
	{
		this.hud = hud;
	}
}
