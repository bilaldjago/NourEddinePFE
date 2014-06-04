package dz.nouri.process3d;

import java.util.ArrayList;

public class Mesh {

	private ArrayList<Vector3f> verticies = new ArrayList<>();
	private int[] indices;

	public Mesh(ArrayList<Vector3f> verticies, int[] indices) {
		super();
		this.verticies = verticies;
		this.indices = indices;
	}

	public ArrayList<Vector3f> getVerticies() {
		return verticies;
	}

	public void setVerticies(ArrayList<Vector3f> verticies) {
		this.verticies = verticies;
	}

	public int[] getIndices() {
		return indices;
	}

	public void setIndices(int[] indices) {
		this.indices = indices;
	}

	public void setVertex(int index, Vector3f value) {
		verticies.set(index, value);
	}

	public int vertexCount() {
		return verticies.size();
	}
	
}
