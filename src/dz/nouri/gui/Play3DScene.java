package dz.nouri.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Light;
import javax.media.j3d.TransformGroup;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.universe.SimpleUniverse;

@SuppressWarnings("serial")
public class Play3DScene extends JInternalFrame implements Runnable {

	private ArrayList<String> expressionList = null;
	private SimpleUniverse su;

	public Play3DScene(String path) {

		addInternalFrameListener(new InternalFrameAdapter() {
			public void internalFrameClosing(InternalFrameEvent event) {
				setVisible(false);
			}
		});

		setBounds(10, 10, 400, 400);
		setTitle("Scène 3D");

		setLayout(new BorderLayout());
		Canvas3D can3d = new Canvas3D(
				SimpleUniverse.getPreferredConfiguration());
		add(can3d, BorderLayout.CENTER);

		su = new SimpleUniverse(can3d);
		su.getViewingPlatform().setNominalViewingTransform();

		expressionList = getExpressions();

		setVisible(true);
		
		if (expressionList != null)
			new Thread(this).start();
		else
			System.err.println("File is empty!");
	}

	private BranchGroup load3DObject(String path) {
		ObjectFile loader = new ObjectFile(ObjectFile.RESIZE);
		try {
			return loader.load(path).getSceneGroup();
		} catch (FileNotFoundException | IncorrectFormatException
				| ParsingErrorException e) {
			e.printStackTrace();
			return null;
		}
	}

	private BranchGroup createSceneBranch(String obj3D) {

		BranchGroup scene = new BranchGroup();

		BoundingSphere bounds = new BoundingSphere(new Point3d(), 100);

		// Ambient light configuration...
		Light ambientLight = new AmbientLight(new Color3f(Color.WHITE));
		ambientLight.setInfluencingBounds(bounds);
		scene.addChild(ambientLight);

		// Direction light (Sun) configuration...
		Light directionalLight = new DirectionalLight(new Color3f(Color.WHITE),
				new Vector3f(1, -1, -1));
		directionalLight.setInfluencingBounds(bounds);
		scene.addChild(directionalLight);

		TransformGroup Transformation_De_La_Souris = new TransformGroup();
		Transformation_De_La_Souris
				.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		Transformation_De_La_Souris
				.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		MouseRotate Rotation = new MouseRotate(Transformation_De_La_Souris);
		Rotation.setSchedulingBounds(new BoundingSphere());
		scene.addChild(Rotation);

		MouseTranslate Translation = new MouseTranslate(
				Transformation_De_La_Souris);
		Translation.setSchedulingBounds(new BoundingSphere());
		scene.addChild(Translation);

		MouseZoom Zoom = new MouseZoom(Transformation_De_La_Souris);
		Zoom.setSchedulingBounds(new BoundingSphere());
		scene.addChild(Zoom);

		Transformation_De_La_Souris.addChild(load3DObject(obj3D));
		scene.addChild(Transformation_De_La_Souris);

		return scene;

	}

	private ArrayList<String> getExpressions() {
		ArrayList<String> res = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(
				new File("./data/expression.nouri")))) {
			String line = reader.readLine();

			if (!line.equals("")) {
				StringTokenizer token = new StringTokenizer(line, "|");
				while (token.hasMoreTokens()) {
					res.add(token.nextToken());
				}
			} else {
				return null;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return res;

	}

	public void run() {
		for (int i = 0; true; i++) {
//			su.cleanup();
			BranchGroup scene = createSceneBranch("./expressions/"
					+ expressionList.get(i) + ".obj");
			scene.compile();
			su.addBranchGraph(scene);

			if (i % expressionList.size() == 0)
				i = 0;

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
		}
	}

}
