package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import android.content.Context;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.entities.SceneEntity;

public class Serializer {

	private FileOutputStream fileOutputStream; 
	private FileInputStream fileInputStream; 
	private ObjectInputStream objectInputStream; 
	private ObjectOutputStream objectOutputStream; 
	private String filename; 
	public static Serializer serializer; 
	private HashMap<Integer, SceneEntity> objects; 
	public static int SERIALIZED_MAINCHAR = 0; 
	public static int SERIALIZED_BACKGROUND = 1; 
	private Context context;
	
	public Serializer() {
		serializer = this;
		this.filename = "l23_serialized";
		objects = new HashMap<Integer, SceneEntity>(); 
	}
	
	public static Serializer getInstance() {
		if (serializer == null)
			serializer = new Serializer(); 
		
		return serializer; 
	}
	
	
	public HashMap<Integer, SceneEntity> getSerializedObjects() {
		objects.clear();
		try {
			fileInputStream = new FileInputStream(filename);
			objectInputStream = new ObjectInputStream(fileInputStream);
			Object deserialized = objectInputStream.readObject();
			if (deserialized instanceof HashMap<?, ?>)
				objects = (HashMap<Integer, SceneEntity>)deserialized; 
			//else it is empty, because cleared above... 
			objectInputStream.close();
			fileInputStream.close();
		
		
		} catch (FileNotFoundException fnfe) {
			System.out.println("File "+filename+" not found!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return objects;
	}
	
	public void serializeObjects(SceneEntity mainChar, SceneEntity background) {
		
		objects.put(SERIALIZED_MAINCHAR, mainChar);
		objects.put(SERIALIZED_BACKGROUND, background);
		
		try {
			fileOutputStream = new FileOutputStream(filename);
			objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(objects);
			objectOutputStream.close();
			fileOutputStream.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setContext(Context context) {
		this.context = context; 
		
	}
}
