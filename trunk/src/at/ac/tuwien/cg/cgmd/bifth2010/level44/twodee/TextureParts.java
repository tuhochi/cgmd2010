package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;


public class TextureParts {
	
	/* Parts from the first 256x256 texture */
	
	public static TexturePart makeRabbitHead(Texture texture) {
		return new TexturePart(texture, 0, 128, 191, 255);
	}
	
	public static TexturePart makeWing(Texture texture) {
		return new TexturePart(texture, 0, 0, 127, 127);
	}
	
	public static TexturePart getBucket(Texture texture) {
		return new TexturePart(texture, 128, 0, 255, 127);
	}
	
	public static TexturePart getCoin(Texture texture) {
		return new TexturePart(texture, 224, 224, 255, 255);
	}

}
