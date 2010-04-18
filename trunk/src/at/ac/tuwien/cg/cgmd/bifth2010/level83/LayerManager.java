package at.ac.tuwien.cg.cgmd.bifth2010.level83;

import javax.microedition.khronos.opengles.GL10;

public class LayerManager implements Drawable {

	int count;
	int size;
	Drawable[] layers;
	
	public LayerManager(int size) {
		this.size = size;
		this.count = 0;
		
		layers = new Drawable[size];
	}
	
	@Override
	public void Dispose(GL10 gl) {
		for(int i=0; i<count;i++)
			layers[i].Dispose(gl);
	}

	@Override
	public void Draw(GL10 gl) {
		for(int i=0; i<count;i++)
			layers[i].Draw(gl);
	}

	@Override
	public void Init(GL10 gl) {
		for(int i=0; i<count; i++)
			layers[i].Init(gl);
	}

}
