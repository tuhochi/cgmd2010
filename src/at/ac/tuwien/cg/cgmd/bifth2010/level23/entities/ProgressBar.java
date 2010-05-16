package at.ac.tuwien.cg.cgmd.bifth2010.level23.entities;

import static android.opengl.GLES10.*;
import static android.opengl.GLES10.GL_MODELVIEW;
import static android.opengl.GLES10.GL_TEXTURE;
import static android.opengl.GLES10.GL_TRIANGLE_STRIP;
import static android.opengl.GLES10.glDrawArrays;
import static android.opengl.GLES10.glMatrixMode;
import static android.opengl.GLES10.glPopMatrix;
import static android.opengl.GLES10.glPushMatrix;
import static android.opengl.GLES10.glTexCoordPointer;
import static android.opengl.GLES10.glTranslatef;
import static android.opengl.GLES10.glVertexPointer;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES11;
import android.opengl.GLSurfaceView;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.render.RenderView;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.GeometryManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.Settings;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.TextureAtlas;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.Vector2;

public class ProgressBar
{
	Vector2 dimension;
	TexturePart texture;
	Vector2 position;
	private FloatBuffer vertexBuffer;
	private int vboID;
	
	public ProgressBar()
	{
		dimension = new Vector2(40,3*RenderView.instance.getAspectRatio());
		
		position = new Vector2(50-dimension.x/2f,RenderView.instance.getTopBounds()-5*RenderView.instance.getAspectRatio()-dimension.y);
	}
	
	public void preprocess()
	{
		GeometryManager geometryManager = GeometryManager.instance; 
		vertexBuffer = geometryManager.createVertexBufferQuad(dimension.x, dimension.y);
		texture = TextureAtlas.instance.getProgressBarTextur();
		
		if(Settings.GLES11Supported) 
		{
			vboID = geometryManager.createVBO(vertexBuffer, texture.texCoords);
		}
	}
	
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
			GLES11.glBindBuffer(GLES11.GL_ARRAY_BUFFER, vboID);

			GLES11.glVertexPointer(3, GL_FLOAT, 0, 0);

			GLES11.glTexCoordPointer(2, GL_FLOAT, 0, 12 * 4); 

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
			glTexCoordPointer(2, GL10.GL_FLOAT, 0, texture.texCoords);
			glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
			glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

		} else {
			GLES11.glBindBuffer(GLES11.GL_ARRAY_BUFFER, vboID);

			GLES11.glVertexPointer(3, GL_FLOAT, 0, 0);

			GLES11.glTexCoordPointer(2, GL_FLOAT, 0, 12 * 4); 

			glDrawArrays(GL_TRIANGLE_STRIP, 0, 4); // 4 vertices
		}
		
		glMatrixMode(GL_TEXTURE);
		glPopMatrix();
		
		glMatrixMode(GL_MODELVIEW);
		glPopMatrix();
	}
}
