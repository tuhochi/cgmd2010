package at.ac.tuwien.cg.cgmd.bifth2010.level55;

public class Quad extends Mesh {

	void init() {
		init (0,0,480,320);
	}
	
	void init(int x, int y, int width, int height) {
		float vertices[] = {
                x, y, 1,
                x+width, y, 1,
                x+width, y+height,  1,
                x, y+height,  1
        };
        
        float texCoords[] = {
                0.0f, 0.0f,
                1.0f, 0.0f,
                1.0f,  1.0f,
                0.0f,  1.0f
        };

        byte indices[] = {
                0, 1, 2,    0, 2, 3
        };
        
        init (vertices, texCoords, indices);
	}
}
