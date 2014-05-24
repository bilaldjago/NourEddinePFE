package dz.nouri.process3d;

public class Quaternion {

	private float x, y, z, w;

	public Quaternion(float x, float y, float z, float w) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public float getW() {
		return w;
	}

	public void setW(float w) {
		this.w = w;
	}
	
	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z + w * w);
	}
	
	public Quaternion normalize() {
		x /= length();
		y /= length();
		z /= length();
		w /= length();
		return this;
	}
	
	public Quaternion conjugate() {
		return new Quaternion(-x, -y, -z, w);
	}
	
	public Quaternion mul(Quaternion r) {
		float W = w * r.getW() - x * r.getX() - y * r.getY() - z * r.getZ();
		float X = x * r.getW() + w * r.getX() + y * r.getZ() - z * r.getY();
		float Y = y * r.getW() + w * r.getY() + z * r.getX() - x * r.getZ();
		float Z = z * r.getW() + w * r.getZ() + x * r.getY() - y * r.getX();
		return new Quaternion(X, Y, Z, W);
	}
	
	public Quaternion mul(Vector3f r) {
		float W = -x * r.getX() - y * r.getY() - z * r.getZ();
		float X =  w * r.getX() + y * r.getZ() - z * r.getY();
		float Y =  w * r.getY() + z * r.getX() - x * r.getZ();
		float Z =  w * r.getZ() + x * r.getY() - y * r.getX();
		return new Quaternion(X, Y, Z, W);
	}
	
}
