package dz.nouri.process3d;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import dz.nouri.tools.FDPProcessor;
import dz.nouri.tools.Utils;

public class OBJModel {

	private ArrayList<Vector3f> positions;
	private ArrayList<Integer> indecies;
	private Mesh mesh;
	private static HashMap<String, Integer> map = new HashMap<>();

	private static final String FACEOBJ = "./expressions/face.obj";
	private static final int DEBUG = 64;

	public OBJModel() {
		positions = new ArrayList<>();
		indecies = new ArrayList<>();
		loadMesh(FACEOBJ);
		resize();
		map = FDPProcessor.getFDPVertexIndices("./expressions/source2.fdp");
	}

	private void loadMesh(String fileName) {
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(" ");
				tokens = Utils.removeEmptyString(tokens);
				if (tokens[0].equals("v")) {
					positions.add(new Vector3f(Float.parseFloat(tokens[1]),
											   Float.parseFloat(tokens[2]),
											   Float.parseFloat(tokens[3])));
				} else if (tokens[0].equals("f")) {
					indecies.add(Integer.parseInt(tokens[1].split("/")[0]) - 1);
					indecies.add(Integer.parseInt(tokens[2].split("/")[0]) - 1);
					indecies.add(Integer.parseInt(tokens[3].split("/")[0]) - 1);
				}
			}
			mesh = new Mesh(positions, Utils.toIntArray(indecies));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private Vector3f[] getLimits() {
		Vector3f currentVertex = new Vector3f();

		// Find the limits of the model
		Vector3f[] limit = new Vector3f[2];
		limit[0] = new Vector3f(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
		limit[1] = new Vector3f(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);
		for (int i = 0; i < positions.size(); i++) {

			currentVertex = positions.get(i);

			// Keep track of limits for normalization
			if (currentVertex.getX() < limit[0].getX())
				limit[0].setX(currentVertex.getX());
			if (currentVertex.getX() > limit[1].getX())
				limit[1].setX(currentVertex.getX());
			if (currentVertex.getY() < limit[0].getY())
				limit[0].setY(currentVertex.getY());
			if (currentVertex.getY() > limit[1].getY())
				limit[1].setY(currentVertex.getY());
			if (currentVertex.getZ() < limit[0].getZ())
				limit[0].setZ(currentVertex.getZ());
			if (currentVertex.getZ() > limit[1].getZ())
				limit[1].setZ(currentVertex.getZ());
		}

		if ((DEBUG & 64) != 0) {
			System.out.println("Model range: (" + limit[0].getX() + "," + limit[0].getY() + "," + limit[0].getZ() + ") to (" + limit[1].getX() + "," + limit[1].getY() + ","
					+ limit[1].getZ() + ")");
		}

		return limit;
	} // End of getLimits

	private Vector3f getLimits2() {
		Vector3f currentVertex = new Vector3f();

		// Find the limits of the model
		Vector3f limit = new Vector3f();
		limit = new Vector3f(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);
		for (int i = 0; i < positions.size(); i++) {

			currentVertex = positions.get(i);

			// Keep track of limits for normalization
			if (Math.abs(currentVertex.getX()) > limit.getX())
				limit.setX(Math.abs(currentVertex.getX()));
			if (Math.abs(currentVertex.getY()) > limit.getY())
				limit.setY(Math.abs(currentVertex.getY()));
			if (Math.abs(currentVertex.getZ()) > limit.getZ())
				limit.setZ(Math.abs(currentVertex.getZ()));
		}

		if ((DEBUG & 64) != 0) {
			System.out.println("Model limit: (" + limit.getX() + "," + limit.getY() + "," + limit.getZ() + ")");
		}

		return limit;
	} // End of getLimits

	private void normalize() {
		Vector3f[] limits = getLimits();
		for (int i = 0; i < positions.size(); i++) {

			if (positions.get(i).getX() >= 0) {
				positions.get(i).setX(positions.get(i).getX() / limits[1].getX());
			} else {
				positions.get(i).setX(Math.abs(positions.get(i).getX()) / limits[0].getX());
			}

			if (positions.get(i).getY() >= 0) {
				positions.get(i).setY(positions.get(i).getY() / limits[1].getY());
			} else {
				positions.get(i).setY(Math.abs(positions.get(i).getY()) / limits[0].getY());
			}

			if (positions.get(i).getZ() >= 0) {
				positions.get(i).setZ(positions.get(i).getZ() / limits[1].getZ());
			} else {
				positions.get(i).setZ(Math.abs(positions.get(i).getZ()) / limits[0].getZ());
			}
		}
	}

	private void normalize2() {
		Vector3f limits = getLimits2();
		for (int i = 0; i < positions.size(); i++) {
			positions.get(i).setX(positions.get(i).getX() / limits.getX());
			positions.get(i).setY(positions.get(i).getY() / limits.getY());
			positions.get(i).setZ(positions.get(i).getZ() / limits.getZ());
		}
	}

	private void resize() {
		float biggestDiff;

		Vector3f[] limit = getLimits();

		// Move object so it's centered on (0,0,0)
		Vector3f offset = new Vector3f(-0.5f * (limit[0].getX() + limit[1].getX()), -0.5f * (limit[0].getY() + limit[1].getY()), -0.5f * (limit[0].getZ() + limit[1].getZ()));

		if ((DEBUG & 64) != 0) {
			System.out.println("Offset amount: (" + offset.getX() + "," + offset.getY() + "," + offset.getZ() + ")");
		}

		// Find the divide-by value for the normalization
		biggestDiff = limit[1].getX() - limit[0].getX();
		if (biggestDiff < limit[1].getY() - limit[0].getY())
			biggestDiff = limit[1].getY() - limit[0].getY();
		if (biggestDiff < limit[1].getZ() - limit[0].getZ())
			biggestDiff = limit[1].getZ() - limit[0].getZ();
		biggestDiff /= 2.0f;

		for (int i = 0; i < positions.size(); i++) {

			positions.get(i).add(offset);

			positions.get(i).setX(positions.get(i).getX() / biggestDiff);
			positions.get(i).setY(positions.get(i).getY() / biggestDiff);
			positions.get(i).setZ(positions.get(i).getZ() / biggestDiff);
		}
	} // End of resize

	public ArrayList<Vector3f> getPositions() {
		return positions;
	}

	public Vector3f getPosition(int index) {
		return positions.get(index);
	}

	public void setPositions(ArrayList<Vector3f> position) {
		this.positions = position;
	}

	public ArrayList<Integer> getIndecies() {
		return indecies;
	}

	public void setIndecies(ArrayList<Integer> indecies) {
		this.indecies = indecies;
	}

	public ArrayList<Vector3f> getFDPs() {
		ArrayList<Vector3f> fdp = new ArrayList<>();
		for (Integer index : map.values()) {
			fdp.add(getPosition(index));
		}
		return fdp;
	}

	public static HashMap<String, Integer> getMap() {
		return map;
	}

	public static void setMap(HashMap<String, Integer> map) {
		OBJModel.map = map;
	}

	public Mesh getMesh() {
		return mesh;
	}
	
}
