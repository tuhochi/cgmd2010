package at.ac.tuwien.cg.cgmd.bifth2010.level88;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/*
 * In der Klasse koennen unsere Objecte geladen werden? Vielleicht
 * derzeit auch nur zu Testzwecken f�r das Skelett
 * Ist aber auch f�r den View zust�ndig
 * 
 * TODO das zeichnen sollte als OpenGL befehle gemacht werden!
 */

/**
 * @author Asperger, Radx
 *
 */
public class HeistView extends View{

	
	//Hier mal fuer Testzwecke bzw. koennten die Tiles dann auch Verstecke kennzeichnen?
	/*
	 * Paramter um die Groe�e und Sichtweite der Tiles zu bestimmen
	 * Width/Height in Pixel
	 */
	protected static int l88TileSize;
	
	protected static int l88XTileCount;
	protected static int l88YTileCount;
	
	private static int l88XOffset;
	private static int l88YOffset;
	
	
	
	public HeistView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public HeistView(Context context, AttributeSet attrs){
		super(context, attrs);
		//TODO
	}
	
	public HeistView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		//TODO
	}
	
	
	
	
	
}
