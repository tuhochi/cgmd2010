package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Class that provides common functionality
 * 
 * @author thp
 *
 */
public class Util {
	/** The size of a native float variable in bytes */
	private static final int FLOAT_SIZE = 4;

	/**
	 * Convert a float[] to a new FloatBuffer object for usage
	 * in OpenGL ES calls that require native float arrays.
	 * 
	 * @param floats An array of float variables to be converted 
	 * @return A new FloatBuffer object with its position set to zero
	 */
	public static FloatBuffer floatArrayToBuffer(float [] floats) {
		FloatBuffer result;
		ByteBuffer buf;

		buf = ByteBuffer.allocateDirect(floats.length * FLOAT_SIZE);
		buf.order(ByteOrder.nativeOrder());
		result = buf.asFloatBuffer();
		result.put(floats);
		result.rewind();

		return result;
		
	}

}
