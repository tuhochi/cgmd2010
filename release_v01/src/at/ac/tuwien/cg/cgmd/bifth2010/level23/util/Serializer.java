package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import android.content.Context;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.entities.SceneEntity;

/**
 * The Class Serializer does serializing and deserializing of objects to persist them before the application is destroyed.
 * @author Markus Ernst
 * @author Florian Felberbauer
 */
public class Serializer {

	/** The file output stream. */
	private FileOutputStream fileOutputStream; 
	
	/** The file input stream. */
	private FileInputStream fileInputStream; 
	
	/** The object input stream. */
	private ObjectInputStream objectInputStream; 
	
	/** The object output stream. */
	private ObjectOutputStream objectOutputStream; 
	
	/** The filename to store the file with. */
	private String filename; 
	
	/** The serializer object to pass around. */
	public static Serializer serializer; 
	
	/** The hashmap, storing the serialized objects. */
	private HashMap<Integer, SceneEntity> objects; 
	
	/** The constant for identifying the serialized mainchar. */
	public static int SERIALIZED_MAINCHAR = 0; 
	
	/** The constant for identifying the serialized background. */
	public static int SERIALIZED_BACKGROUND = 1; 
	
	/**
	 * Instantiates a new serializer.
	 *
	 */
	
	public Serializer() {
		serializer = this;
		this.filename = "l23_serialized";
		objects = new HashMap<Integer, SceneEntity>(); 
	}
	
	/**
	 * Gets the single instance of Serializer.
	 *
	 * @return single instance of Serializer
	 */
	public static Serializer getInstance() {
		if (serializer == null)
			serializer = new Serializer(); 
		
		return serializer; 
	}
	
	
	/**
	 * Gets the serialized objects.
	 *
	 * @return the serialized objects
	 */
	
	// because there is no way to eliminate the warning when casting to HashMap<Integer, SceneEntity>
	@SuppressWarnings("unchecked")
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
	
	/**
	 * Serializes objects.
	 *
	 * @param mainChar the main char
	 * @param background the background
	 */
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

	/**
	 * Sets the context.
	 *
	 * @param context the new context
	 */
	public void setContext(Context context) { 
		
	}
}
