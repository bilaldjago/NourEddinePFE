package dz.nouri.lwljgl.scene;

import dz.nouri.lwljgl.engine.core.CoreEngine;

public class Main {

	public static void main(String[] args) {
		CoreEngine engine = new CoreEngine(800, 600, 30, new TestGame());
		engine.createWindow("Test Game 3D");
		engine.start();
	}

}
