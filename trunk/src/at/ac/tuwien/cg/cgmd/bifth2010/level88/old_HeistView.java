package at.ac.tuwien.cg.cgmd.bifth2010.level88;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/*
 * In der Klasse koennen unsere Objecte geladen werden? Vielleicht
 * derzeit auch nur zu Testzwecken für das Skelett
 * Ist aber auch für den View zuständig
 * 
 * TODO das zeichnen sollte als OpenGL befehle gemacht werden!
 */

/**
 * @author Asperger, Radx
 *
 */
public class old_HeistView extends View{

	
	//Hier mal fuer Testzwecke bzw. koennten die Tiles dann auch Verstecke kennzeichnen?
	/*
	 * Paramter um die Groeße und Sichtweite der Tiles zu bestimmen
	 * Width/Height in Pixel
	 */
	protected static int TileSize;
	
	protected static int XTileCount;
	protected static int YTileCount;
	
	private static int XOffset;
	private static int YOffset;
	
	
	
	public old_HeistView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public old_HeistView(Context context, AttributeSet attrs){
		super(context, attrs);
		//TODO
	}
	
	public old_HeistView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		//TODO
	}
	
	
	
	
	
}
