package at.ac.tuwien.cg.cgmd.bifth2010.level36;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Paint;

public class GewinnFeld {
	private int[][] gewinne;
	private float[] posX;
	private float[] posY;
	private Random random;
	private final int MAX = 50;

	public GewinnFeld() {
		random = new Random();
		gewinne = new int[3][3];
		posX = new float[3];
		posY = new float[3];

		for (int i = 0; i < gewinne.length; i++) {
			for (int j = 0; j < gewinne.length; j++) {
				gewinne[i][j] = random.nextInt(MAX);
			}
		}
	}

	public Canvas drawFeld(Canvas c) {
		posX[0] = 0 + c.getWidth() / 4;
		posX[1] = posX[0] + c.getWidth() / 4;
		posX[2] = posX[1] + c.getWidth() / 4;
		posY[0] = 0 + c.getHeight() / 4;
		posY[1] = posY[0] + c.getHeight() / 4;
		posY[2] = posY[1] + c.getHeight() / 4;

		for (int i = 0; i < gewinne.length; i++) {
			for (int j = 0; j < gewinne.length; j++) {
				c.drawText(String.valueOf(gewinne[i][j]), posX[i], posY[j], new Paint());
			}
		}
		return c;
	}
}
