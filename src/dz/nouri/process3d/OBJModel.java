package dz.nouri.process3d;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import dz.nouri.tools.FDPProcessor;

public class OBJModel {

	private ArrayList<Vector3f> positions;
	private ArrayList<Integer> indecies;
	private static HashMap<String, Integer> map = new HashMap<>();

	private static final String FACEOBJ = "./expressions/face.obj";
	
	public OBJModel() {
		positions = new ArrayList<>();
		indecies = new ArrayList<>();
		loadMesh(FACEOBJ);
		map = FDPProcessor.getFDPVertexIndices("./expressions/source2.fdp");
	}

	private void loadMesh(String fileName) {
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(" ");
				if (tokens[0].equals("v")) {
					positions.add(new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3])));
				} /*
				 * else if (tokens[0].equals("f")) {
				 * indecies.add(Integer.parseInt(tokens[1]));
				 * indecies.add(Integer.parseInt(tokens[2]));
				 * indecies.add(Integer.parseInt(tokens[3])); }
				 */
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

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

}
