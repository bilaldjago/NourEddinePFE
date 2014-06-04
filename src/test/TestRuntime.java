package test;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.universe.SimpleUniverse;

import dz.nouri.process3d.Mesh;
import dz.nouri.process3d.OBJModel;

public class TestRuntime extends JFrame {

	private SimpleUniverse su;
	private Canvas3D can;

	public TestRuntime() throws IOException {
		setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		setSize(800, 600);
		setTitle("Scène 3D");
		setResizable(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		can = new Canvas3D(SimpleUniverse.getPreferredConfiguration());

		add(can, BorderLayout.CENTER);

		su = new SimpleUniverse(can);

		su.addBranchGraph(createSceneBranch());
		su.getViewingPlatform().setNominalViewingTransform();
		setVisible(true);

	}

	private BranchGroup createSceneBranch() {

		BranchGroup scene = new BranchGroup();
		Mesh mesh = new OBJModel().getMesh();

		Point3f point0, point1 , point2;
		QuadArray quadArray = new QuadArray(mesh.vertexCount(), QuadArray.COORDINATES | QuadArray.NORMALS);
		for (int i = 0; i < mesh.vertexCount() - 2; i++) {

			point0 = new Point3f(mesh.getVerticies().get(i).getX(), 
										 mesh.getVerticies().get(i).getY(), 
										 mesh.getVerticies().get(i).getZ());
			
			point1 = new Point3f(mesh.getVerticies().get(i + 1).getX(), 
					 					 mesh.getVerticies().get(i + 1).getY(), 
					 					 mesh.getVerticies().get(i + 1).getZ());
			
			point2 = new Point3f(mesh.getVerticies().get(i + 2).getX(), 
					                     mesh.getVerticies().get(i + 2).getY(), 
					                     mesh.getVerticies().get(i + 2).getZ());
			
			quadArray.setCoordinates(mesh.getIndices()[i], new float[] { point0.x, point0.y, point0.z });
			quadArray.setCoordinates(mesh.getIndices()[i + 1], new float[] { point1.x, point1.y, point1.z });
			quadArray.setCoordinates(mesh.getIndices()[i + 2], new float[] { point2.x, point2.y, point2.z });

			scene.addChild(new Shape3D(quadArray));
		}

		return scene;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					new TestRuntime().setVisible(true);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
