package dz.nouri.process3d;

import java.util.ArrayList;

public class Vertex {

	private int index = -1;// index of the Vertex
	private ArrayList<Vector3f> distances = new ArrayList<>();// Array of distances between the vertex and the Feature points
	private ArrayList<Float> weights = new ArrayList<>();// Weights
	private Vector3f v0;// Vertex in original state

	public Vertex(int index, ArrayList<Vector3f> distances, ArrayList<Float> weights, Vector3f v0) {
		this.index = index;
		this.distances = distances;
		this.weights = weights;
		this.v0 = v0;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public ArrayList<Vector3f> getDistances() {
		return distances;
	}

	public void setDistances(ArrayList<Vector3f> distances) {
		this.distances = distances;
	}

	public ArrayList<Float> getWeights() {
		return weights;
	}

	public void setWeights(ArrayList<Float> weights) {
		this.weights = weights;
	};

	public Vector3f getV0() {
		return v0;
	}

	public void setV0(Vector3f v0) {
		this.v0 = v0;
	}

	public Vector3f getV() {
		Vector3f vec = null;
		int x = 0, y = 0, z = 0;
		for (int i = 0; i < distances.size(); i++) {
			x += v0.getX() + weights.get(i) * distances.get(i).getX();
			y += v0.getY() + weights.get(i) * distances.get(i).getY();
			z += v0.getZ() + weights.get(i) * distances.get(i).getZ();
		}
		vec = new Vector3f(x, y, z);
		return vec;
	}

}
