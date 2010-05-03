package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;


public class TextureParts {
	
	public static TexturePart makeRabbitHead(Texture texture) {
		return new TexturePart(texture, 70, 519, 70+100, 519+72);
	}
	
	public static TexturePart makeWing(Texture texture) {
		return new TexturePart(texture, 0, 519, 0+55, 519+60);
	}
	
	public static TexturePart makeBucketBack(Texture texture) {
		return new TexturePart(texture, 180, 519, 180+61, 519+58);
	}
	
	public static TexturePart makeBucket(Texture texture) {
		return new TexturePart(texture, 260, 519, 260+61, 519+54);
	}

	public static TexturePart makeCoin(Texture texture) {
		return new TexturePart(texture, 340, 519, 340+19, 519+19);
	}
	
	public static TexturePart makeSky(Texture texture) {
		return new TexturePart(texture, 0, 0, 0+10, 0+320);
	}

	public static TexturePart makeCloud(Texture texture, int id) {
		int pos = id%3;
		
		switch (pos) {
			case 1:
				return new TexturePart(texture, 280, 0, 280+133, 0+188);
			case 2:
				return new TexturePart(texture, 20, 218, 20+200, 218+144);
			default:
				return new TexturePart(texture, 20, 0, 20+250, 0+195);
		}
	}

	public static TexturePart makeMeadow(Texture texture) {
		return new TexturePart(texture, 0, 382, 0+930, 382+130);
	}

	public static TexturePart makeHills(Texture texture) {
		return new TexturePart(texture, 234, 242, 234+790, 242+120);
	}

	public static TexturePart makeMountains(Texture texture) {
		return new TexturePart(texture, 420, 0, 420+600, 0+210);
	}

	public static TexturePart makeCrosshairsRed(Texture texture) {
		return new TexturePart(texture, 976, 426, 976+48, 426+48);
	}
	
	public static TexturePart makeCrosshairsGreen(Texture texture) {
		return new TexturePart(texture, 976, 376, 976+48, 376+48);
	}
	
	public static TexturePart makeAimBarBackground(Texture texture) {
		return new TexturePart(texture, 370, 519, 370+185, 519+24);
	}
	
	public static TexturePart makeAimBarForeground(Texture texture) {
		return new TexturePart(texture, 370, 549, 370+185, 549+24);
	}
	
	public static TexturePart makeIntroBackground(Texture texture) {
		return new TexturePart(texture,1024-480,1024-320,480,320);
	}
}
