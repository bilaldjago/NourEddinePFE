package dz.nouri.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Light;
import javax.media.j3d.PointArray;
import javax.media.j3d.PointAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JInternalFrame;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.universe.SimpleUniverse;

import dz.nouri.process3d.Vector3f;

public class Display3DScene extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	private Canvas3D can;
	private SimpleUniverse su;
	private final ArrayList<Vector3f> fdps;

	public Display3DScene(final ArrayList<Vector3f> fdps) {

		setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		setBounds(10, 10, 400, 400);
		setTitle("Scène 3D");
		setResizable(true);
		this.fdps = fdps;

		setLayout(new BorderLayout());
		can = new Canvas3D(SimpleUniverse.getPreferredConfiguration());

		add(can, BorderLayout.CENTER);

		su = new SimpleUniverse(can);

		su.addBranchGraph(createSceneBranch("./expressions/face.obj"));
		su.addBranchGraph(drawFDPS());
		su.getViewingPlatform().setNominalViewingTransform();

		setVisible(true);
	}

	private BranchGroup drawFDPS() {
		// TODO draw 3D spheres in the place of FDPS
		BranchGroup lineGroup = new BranchGroup();
		Appearance app = new Appearance();
		ColoringAttributes ca = new ColoringAttributes(new Color3f(.0f, 204.0f, .0f), ColoringAttributes.SHADE_FLAT);
		app.setColoringAttributes(ca);

		Point3f[] plaPts = new Point3f[4];
		int count = 0;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				plaPts[count] = new Point3f(i / 10.0f, j / 10.0f, 0);
				count++;
			}
		}
		PointArray pla = new PointArray(4, GeometryArray.COORDINATES);

		pla.setCoordinates(0, plaPts);
		// between here!
		PointAttributes a_point_just_bigger = new PointAttributes();
		a_point_just_bigger.setPointSize(10.0f);// 10 pixel-wide point
		a_point_just_bigger.setPointAntialiasingEnable(true);// now points are
																// sphere-like(not
																// a cube)
		app.setPointAttributes(a_point_just_bigger);
		// and here! sets the point-attributes so it is easily seen.
		Shape3D plShape = new Shape3D(pla, app);
		TransformGroup objRotate = new TransformGroup();
		objRotate.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objRotate.addChild(plShape);
		lineGroup.addChild(objRotate);
		return lineGroup;
	}

	private BranchGroup load3DObject(String path) {
		ObjectFile loader = new ObjectFile(ObjectFile.RESIZE);
		try {
			return loader.load(path).getSceneGroup();
		} catch (FileNotFoundException | IncorrectFormatException | ParsingErrorException e) {
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
		Light directionalLight = new DirectionalLight(/*
													 * new Color3f(Color.WHITE),
													 * new Vector3f(1, -1, -1)
													 */);
		directionalLight.setInfluencingBounds(bounds);
		scene.addChild(directionalLight);

		TransformGroup mouseTransformation = new TransformGroup();
		mouseTransformation.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		mouseTransformation.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		MouseRotate Rotation = new MouseRotate(mouseTransformation);
		Rotation.setSchedulingBounds(new BoundingSphere());
		scene.addChild(Rotation);

		MouseTranslate Translation = new MouseTranslate(mouseTransformation);
		Translation.setSchedulingBounds(new BoundingSphere());
		scene.addChild(Translation);

		MouseZoom Zoom = new MouseZoom(mouseTransformation);
		Zoom.setSchedulingBounds(new BoundingSphere());
		scene.addChild(Zoom);

		mouseTransformation.addChild(load3DObject(obj3D));
		scene.addChild(mouseTransformation);

		return scene;

	}

}
