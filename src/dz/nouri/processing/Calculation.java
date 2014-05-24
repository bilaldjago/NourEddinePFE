package dz.nouri.processing;

import java.util.ArrayList;

import Jama.Matrix;
import dz.nouri.process3d.Vector2f;
import dz.nouri.process3d.Vector3f;

public final class Calculation {

	// Calculate h parameter
	// sqrt[(||sj - si||)� + scj�]
	public static float calcH(ArrayList<Vector2f> points, int i, int j) {

		Vector2f res = points.get(j).sub(points.get(i)); // sj - si

		float sum = (float) Math.pow(res.norme(), 2); // (||sj - si||)�

		ArrayList<Float> floats = new ArrayList<>();

		for(int index = 0; index < points.size(); index++) {
			if(index != j)
				floats.add(points.get(index).sub(points.get(j)).norme());
		}
		
		float min = floats.get(0);
		
		for(int index = 1; index < floats.size(); index++)
			if(floats.get(index) < min) min = floats.get(index);
		return (float) Math.sqrt(sum + Math.pow(min, 2)); // sqrt[(||sj - si||)� + scj�]

	}

	// Calculate the H matrix
	public static Matrix calculateHMatrix(ArrayList<Vector2f> pointList) {
		Matrix res = new Matrix(pointList.size(), pointList.size());
		for (int i = 0; i < pointList.size(); i++)
			for (int j = 0; j < pointList.size(); j++)
				res.set(i, j, calcH(pointList, i, j));
		return res;
	}

	// Calculate Wx, Wy and Wz and puts them in a single ArrayList of vector3f
	public static ArrayList<Vector3f> calculateW(ArrayList<Vector3f> fdps3D, ArrayList<Vector2f> pointList) {
		return mul(calculateHMatrix(pointList).inverse(), fdps3D);
	}

	// Calculate the multiplication of the inverse of the H matrix and the FDPs
	// Vector
	private static ArrayList<Vector3f> mul(Matrix mat, ArrayList<Vector3f> fdps) {
		ArrayList<Vector3f> res = new ArrayList<>();
		for (int i = 0; i < fdps.size(); i++) {
			float wx = 0, wy = 0, wz = 0;
			for (int j = 0; j < fdps.size(); j++) {
				wx += (mat.get(i, j) + 0.001) * fdps.get(j).getX();
				wy += (mat.get(i, j) + 0.001) * fdps.get(j).getY();
				wz += (mat.get(i, j) + 0.001) * fdps.get(j).getZ();
			}
			res.add(new Vector3f(wx, wy, wz));
		}
		return res;
	}

	// calculate the new Tj Feature points in a frame!
	public static Vector3f calculateTj(ArrayList<Vector3f> w, ArrayList<Vector2f> points, int j) {
		Vector3f tj = null;
		float x  = 0, y = 0, z = 0;
		
		for (int i = 0; i < w.size(); i++) {
			float h = calcH(points, i, j);
			System.out.println(h);
			x += w.get(i).getX() * h;
			y += w.get(i).getY() * h;
			z += w.get(i).getZ() * h;
		}
		tj = new Vector3f(x, y, z);
		return tj;
	}

}