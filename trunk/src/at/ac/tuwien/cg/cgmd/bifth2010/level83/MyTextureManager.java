package at.ac.tuwien.cg.cgmd.bifth2010.level83;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;



public class MyTextureManager {
	
	public static MyTextureManager singleton = null;
	Context context;
	int size;
	int count = 0;
	public MyTexture[] textures;
	
	public MyTextureManager(Context context, int size) {
		
		// TODO Auto-generated constructor stub
		this.context = context;
		this.size = size;
		
		textures = new MyTexture[size];
		singleton = this;
	}
	
	public void reload(GL10 gl){
		
		int id;
		for(int i=0; i<count; i++){
			id = textures[i].resourceId;
			
			if(id != 0){
				//Bitmap bitmap = BitmapFactory.decodeStream(context.getAssets().open(file));
				InputStream is = context.getResources().openRawResource(id);
				
				Bitmap bitmap;
				 try {
		                bitmap = BitmapFactory.decodeStream(is);
		            } finally {
		                try {
		                    is.close();
		                } catch (IOException e) {
		                	 Log.e("TextureManager", "Unable to load "+id);
		                     Log.e("Exception",e.getStackTrace().toString());
		                    // System.exit(-1);
		                }
		            }
		        textures[i].loadTexture(bitmap, gl);
		        
		        bitmap.recycle();
		        
		        Log.d("TextureManager", "Loaded "+id);
			}else{
				String file = "";
				
				file = textures[i].file;
				
				try
		        {
		                Bitmap bitmap = BitmapFactory.decodeStream(context.getAssets().open(file));
		                
		                textures[i].loadTexture(bitmap, gl);
		                
		                bitmap.recycle();
		                
		                Log.d("TextureManager", "Loaded "+file);
		     
		        }catch ( Exception ex )
	            {
	                    Log.e("TextureManager", "Unable to load "+file);
	                    Log.e("Exception",ex.getStackTrace().toString());
	                    System.exit(-1);
	            }     
				
			}
			
		}
			
	}
	
	public int addTexturefromResources(int id, GL10 gl){
		
		if (count >= size){
			Log.e("TextureManager","Texture Manager full.");
			return -1;
		}
			
		
        //Bitmap bitmap = BitmapFactory.decodeStream(context.getAssets().open(file));
        InputStream is = context.getResources().openRawResource(id);
        
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(is);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            	 Log.e("TextureManager", "Unable to load "+id);
                 Log.e("Exception",e.getStackTrace().toString());
                // System.exit(-1);
            }
        }
        textures[count] = new MyTexture(id);
        textures[count].loadTexture(bitmap, gl);
        
        bitmap.recycle();
        
        count++;
        
        Log.d("TextureManager", "Loaded "+id);

        return count-1;
	}
	
	public int addTexturefromAssets(String file, GL10 gl){
		
		if (count >= size){
			Log.e("TextureManager","Texture Manager full.");
			return -1;
		}
			
		try
        {
                Bitmap bitmap = BitmapFactory.decodeStream(context.getAssets().open(file));
              
                textures[count] = new MyTexture(file);
                textures[count].loadTexture(bitmap, gl);
                
                bitmap.recycle();
                
                count++;
                
                Log.d("TextureManager", "Loaded "+file);
        }
        catch ( Exception ex )
        {
                Log.e("TextureManager", "Unable to load "+file);
                Log.e("Exception",ex.getStackTrace().toString());
                System.exit(-1);
        }
        
        return count-1;
	}
	public void dispose(){
		for(int i=0; i<count; i++){
			textures[i] = null;
		}
		
		count = 0;
	}
}
