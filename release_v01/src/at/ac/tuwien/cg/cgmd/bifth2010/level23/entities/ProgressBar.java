package at.ac.tuwien.cg.cgmd.bifth2010.level23.entities;

import static android.opengl.GLES10.GL_MODELVIEW;
import static android.opengl.GLES10.GL_TEXTURE;
import static android.opengl.GLES10.GL_TRIANGLE_STRIP;
import static android.opengl.GLES10.glDrawArrays;
import static android.opengl.GLES10.glMatrixMode;
import static android.opengl.GLES10.glPopMatrix;
import static android.opengl.GLES10.glPushMatrix;
import static android.opengl.GLES10.glScalef;
import static android.opengl.GLES10.glTexCoordPointer;
import static android.opengl.GLES10.glTranslatef;
import static android.opengl.GLES10.glVertexPointer;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level23.render.RenderView;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.GeometryManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.Settings;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.TextureAtlas;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.Vector2;

/**
 * The Class ProgressBar is used to render a progressbar
 *
 * @author Markus Ernst
 * @author Florian Felberbauer
 */

public class ProgressBar
{
	/** The dimensions of the progressbar. */
	Vector2 dimension;
	/** The texture part of the progressbar. */
	TexturePart texture;
	/** The position of the progressbar. */
	Vector2 position;
	/** The vertexbuffer of the progressbar. */
	private FloatBuffer vertexBuffer;
	/** The vbo id of the progressbar. */
	private int vboId;
	/** GeometryManager creating vertexbuffers */
	private GeometryManager geometryManager = GeometryManager.instance;
	
	/**
	 * Default Constructor
	 */
	public ProgressBar()
	{
		dimension = new Vector2(40,3*RenderView.instance.getAspectRatio());
		
		position = new Vector2(50-dimension.x/2f,RenderView.instance.getTopBounds()-5*RenderView.instance.getAspectRatio()-dimension.y);
	}
	
	/**
	 * Loads the vertex and texturebuffers
	 */
	public void preprocess()
	{
		GeometryManager geometryManager = GeometryManager.instance; 
		vertexBuffer = geometryManager.createVertexBufferQuad(dimension.x, dimension.y);
		texture = TextureAtlas.instance.getProgressBarTextur();
		
		if(Settings.GLES11Supported) 
		{
			vboId = geometryManager.createVBO(vertexBuffer, texture.texCoords);
		}
	}
	
	/**
	 * Renders the progressbar
	 * @param progress the progress to be shown by the bar (0-1)
	 */
	public void render(float progress) 
	{
		//render background bar
		glPushMatrix();
		
		glTranslatef(position.x, position.y, 0);

		if (!Settings.GLES11Supported) {
			glTexCoordPointer(2, GL10.GL_FLOAT, 0, texture.texCoords);
			glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
			glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

		} else {
			geometryManager.bindVBO(vboId);
			glDrawArrays(GL_TRIANGLE_STRIP, 0, 4); // 4 vertices
		}

		glPopMatrix();
	
		//render foreground bar
		glMatrixMode(GL_TEXTURE);
		glPushMatrix();
		
		glTranslatef(0, -texture.dimension.y, 0);

		glMatrixMode(GL_MODELVIEW);
		glPushMatrix();
		
		glScalef(progress,1,1);
		glTranslatef(position.x/progress, position.y, 0);

		if (!Settings.GLES11Supported) {
			glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

		} else {
			glDrawArrays(GL_TRIANGLE_STRIP, 0, 4); // 4 vertices
		}
		
		glMatrixMode(GL_TEXTURE);
		glPopMatrix();
		
		glMatrixMode(GL_MODELVIEW);
		glPopMatrix();
	}
}
