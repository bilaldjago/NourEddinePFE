package test;

import java.awt.Toolkit;

import dz.nouri.process3d.Vector2f;
import dz.nouri.process3d.Vector3f;

public class TestRuntime {

	public static void main(String[] args) {

		Vector2f point = projection(new Vector3f(270, 150, - 320));
		
		System.out.println(point);
		
	}
	
	public static Vector2f projection(Vector3f input) {

		int screenCenterX = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2;
		int screenCenterY = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2;

		float x = input.getX();
		float y = input.getY();

		float temp = 180 / (20 - input.getZ());
		x = screenCenterX + 10 * temp * x;
		y = screenCenterY - 10 * temp * y;
		return new Vector2f(x, y);
	}

}
