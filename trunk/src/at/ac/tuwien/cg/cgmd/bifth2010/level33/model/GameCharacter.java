package at.ac.tuwien.cg.cgmd.bifth2010.level33.model;

import javax.microedition.khronos.opengles.GL10;

public class GameCharacter extends Geometry {
	
	float one = 1.0f/2;
	public GameCharacter(GL10 gl){
		
		 super( gl,Type.Triangles, 36, true, false, false );  
		 
		 
		this.color(0.0f,0.0f,1.0f); // das vordere soll blau werden
		this.color(0.0f,0.0f,1.0f); // das vordere soll blau werden
		this.color(0.0f,0.0f,1.0f); // das vordere soll blau werden
	    this.vertex( 0.0f, one, 0.0f); // oben (vorderes Dreieck)
	    this.vertex(-one,-one, one); // links (vorderes Dreieck)
	    this.vertex( one,-one, one); // rechts (vorderes Dreieck)
	
	    this.color(1.0f,0.0f,0.0f); // das rechte soll rot werden
	    this.color(1.0f,0.0f,0.0f); // das rechte soll rot werden
	    this.color(1.0f,0.0f,0.0f); // das rechte soll rot werden
	    this.vertex( 0.0f, one, 0.0f);  // oben (rechtes Dreieck)
	    this.vertex( one,-one, one);  // links (rechtes Dreieck)
	    this.vertex( one,-one, -one); // rechts (rechtes Dreieck)
	    
	    this.color(0.0f,1.0f,0.0f); // das hintere grün
	    this.color(0.0f,1.0f,0.0f); // das hintere grün
	    this.color(0.0f,1.0f,0.0f); // das hintere grün
	    this.vertex( 0.0f, one, 0.0f);  // oben (hinteres Dreieck)
	    this.vertex( one,-one, -one); // links (hinteres Dreieck)
	    this.vertex(-one,-one, -one); // rechts (hinteres Dreieck)
	 
	    this.color(1.0f,1.0f,0.0f); // und das linke gelb 
	    this.color(1.0f,1.0f,0.0f); // und das linke gelb 
	    this.color(1.0f,1.0f,0.0f); // und das linke gelb 
	    this.vertex( 0.0f, one, 0.0f); // oben (linkes Dreieck)
	    this.vertex(-one,-one,-one); // links (linkes Dreieck)
	    this.vertex(-one,-one, one); // rechts (linkes Dreieck)
		 

	}
	


}
		 
		 
		 
		 
		 
		 

	



