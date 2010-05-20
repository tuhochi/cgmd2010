package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;
import java.util.Properties;

import android.content.Context;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.LevelActivity;

/**
 * This class provides saving and loading from properties to persistently save 
 * settings user has chosen. 
 * 
 * @author Markus Ernst
 * @author Florian Felberbauer
 */
 
public class PersistentSettings {

	/** The ORIENTATION_SENSOR string to provide consistent naming of the properties. */
	public static String ORIENTATION_SENSOR = "ORIENTATION_SENSOR"; 
	
	/** The ON string. */
	public static String ON ="ON"; 
	
	/** The OFF string. */
	public static String OFF = "OFF"; 
	
	/** The root file to write to. */
	private File file; 
	/** The FileOutputStream to write the properties to . */
	private FileOutputStream fos;  
	/** The FileInputStream to read the properties from */
	private FileInputStream fis;
	/** The properties to store and read the settings to/from. */
	private Properties properties; 
	/** The filename for the file stored in Context s application package. */
	private String filename; 
	/** The instance for singleton. */
	public static PersistentSettings instance = new PersistentSettings(); 
	
	/**
	 * Instantiates a new persistent settings and sets up all required files and streams
	 */
	public PersistentSettings() {
		try {
				properties = new Properties();		
				filename = "lvl23Properties";
				fos = LevelActivity.instance.openFileOutput(filename, Context.MODE_PRIVATE);
				// reading properties from FileInputStream here 
				fis = LevelActivity.instance.openFileInput(filename);
				properties.load(fis);
				fis.close();
				
					
					
		} catch(FileNotFoundException f) {
			Log.e("PersistentSettings", "File not Found: "+filename+"\n"+f.getStackTrace());
		} catch(IOException e) {
			Log.e("PersistentSettings", "Error im Konstruktor beim initialisieren!"+e.getStackTrace());
		} catch(Throwable t) {
			Log.e("PersistentSettings", "Unbekannter Fehler im Konstruktor"+t.getStackTrace());
		}
	}
	
	/**
	 * Saves the properties to disk.
	 */
	public void saveToDisk() {
		try {
//			out = new BufferedWriter(fileWriter);
//			out.write("");
//			out.close();
			fos = LevelActivity.instance.openFileOutput(filename, Context.MODE_PRIVATE);
			properties.store(fos, "no comment");
			fos.close();
			
		} catch (IOException e) {
			Log.e("PersistentSettings", "Fehler beim Lesen von der SD Karte!"+e.getStackTrace());
		} catch (Throwable t) {
			Log.e("PersistentSettings", "Unbekannter Fehler beim Lesen von der SD Karte!\n"+t.getStackTrace());
		}	
		
	}
	
	/**
	 * Reads the properties from disk.
	 */
	public void readFromDisk() {
	try {
		fis = LevelActivity.instance.openFileInput(filename);
		properties.load(fis);
		fis.close();
		
	} catch (IOException e) {
		Log.e("PersistentSettings", "Fehler beim Lesen von der SD Karte!"+e.getStackTrace());
		e.printStackTrace();
	} catch (Throwable t) {
		Log.e("PersistentSettings", "Unbekannter Fehler beim Lesen von der SD Karte!\n"+t.getStackTrace());
	}
	}
	
	
	/**
	 * Gets the value for a given key.
	 *
	 * @param key the key
	 * @return the value
	 */
	public String getValue(String key) {
		
		Log.v(key, "Wert ist: "+properties.getProperty(key, ""));
		return properties.getProperty(key, "");
	}
	
	/**
	 * Checks whether a given value exists in the properties.
	 *
	 * @param value the value
	 * @return true, if successful
	 */
	public boolean containsValue(String value) {
		return properties.contains(value);	
	}
	
	/**
	 * Checks if the properties contains a key/value pair to a given key.
	 *
	 * @param key the key
	 * @return true, if successful
	 */
	public boolean containsKey(String key) {
		return properties.contains(key); 
	}
	
	/**
	 * Saves a key/value pair to properties.
	 *
	 * @param key the key
	 * @param value the value
	 */
	public void saveProperty(String key, String value) {
		properties.setProperty(key, value);
		Log.v(key, "Wert wird gespeichert: "+key+"/"+value);
	}
	
	/**
	 * Removes the key/value pair to a given key from properties.
	 *
	 * @param key the key
	 */
	public void removeProperty(String key) {
		properties.remove(key); 
	}

	
}


