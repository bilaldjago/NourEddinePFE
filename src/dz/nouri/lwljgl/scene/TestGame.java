package dz.nouri.lwljgl.scene;

import dz.nouri.lwljgl.engine.component.Camera;
import dz.nouri.lwljgl.engine.component.DirectionalLight;
import dz.nouri.lwljgl.engine.component.MeshRenderer;
import dz.nouri.lwljgl.engine.component.PointLight;
import dz.nouri.lwljgl.engine.component.SpotLight;
import dz.nouri.lwljgl.engine.core.Game;
import dz.nouri.lwljgl.engine.core.GameObject;
import dz.nouri.lwljgl.engine.core.Quaternion;
import dz.nouri.lwljgl.engine.core.Vector2f;
import dz.nouri.lwljgl.engine.core.Vector3f;
import dz.nouri.lwljgl.engine.render.Material;
import dz.nouri.lwljgl.engine.render.Mesh;
import dz.nouri.lwljgl.engine.render.Texture;
import dz.nouri.lwljgl.engine.render.Vertex;
import dz.nouri.lwljgl.engine.render.Window;

public class TestGame extends Game {
	public void init() {
		float fieldDepth = 10.0f;
		float fieldWidth = 10.0f;

		Vertex[] vertices = new Vertex[] { new Vertex(new Vector3f(-fieldWidth, 0.0f, -fieldDepth), new Vector2f(0.0f, 0.0f)),
				new Vertex(new Vector3f(-fieldWidth, 0.0f, fieldDepth * 3), new Vector2f(0.0f, 1.0f)), new Vertex(new Vector3f(fieldWidth * 3, 0.0f, -fieldDepth), new Vector2f(1.0f, 0.0f)),
				new Vertex(new Vector3f(fieldWidth * 3, 0.0f, fieldDepth * 3), new Vector2f(1.0f, 1.0f)) };

		int indices[] = { 0, 1, 2, 2, 1, 3 };

		Mesh mesh2 = new Mesh("face1.obj");

		Mesh mesh = new Mesh(vertices, indices, true);
		Material material = new Material();// new Texture("test.png"), new
											// Vector3f(1,1,1), 1, 8);
		material.addTexture("diffuse", new Texture("test.png"));
		material.addFloat("specularIntensity", 1);
		material.addFloat("specularPower", 8);

		Material matFace = new Material();
		
		matFace.addTexture("diffuse", new Texture("face.png"));
		matFace.addFloat("specularIntensity", 0);
		matFace.addFloat("specularPower", 8);
		
		MeshRenderer meshRenderer = new MeshRenderer(mesh, material);

		GameObject planeObject = new GameObject();
		planeObject.addComponent(meshRenderer);
		planeObject.getTransform().getPos().set(0, -1, 5);

		GameObject directionalLightObject = new GameObject();
		DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1),.8f);

		directionalLightObject.addComponent(directionalLight);

		GameObject pointLightObject = new GameObject();
		pointLightObject.addComponent(new PointLight(new Vector3f(0, 1, 0), 0.4f, new Vector3f(0, 0, 1)));

		SpotLight spotLight = new SpotLight(new Vector3f(0, 1, 1), 0.4f, new Vector3f(0, 0, 0.1f), 0.7f);

		GameObject spotLightObject = new GameObject();
		spotLightObject.addComponent(spotLight);

		spotLightObject.getTransform().getPos().set(5, 0, 5);
		spotLightObject.getTransform().setRot(new Quaternion(new Vector3f(0, 1, 0), (float) Math.toRadians(90.0f)));

		addObject(planeObject);
		addObject(directionalLightObject);
		addObject(pointLightObject);
		addObject(spotLightObject);

		GameObject testMesh1 = new GameObject().addComponent(new MeshRenderer(mesh2, matFace));
		
		testMesh1.getTransform().getPos().set(10, 2, 0);
		testMesh1.getTransform().setRot(new Quaternion(new Vector3f(0, 1, 0), (float) Math.toRadians(0)));
		
		testMesh1.getTransform().setScale(new Vector3f(0.1f, 0.1f, 0.1f));

		GameObject camera = new GameObject().addComponent(new Camera((float) Math.toRadians(70.0f), (float) Window.getWidth() / (float) Window.getHeight(), 0.01f, 1000.0f));
		camera.getTransform().getPos().set(0, 0, +5);
		camera.getTransform().getRot().set(0, (float) Math.toRadians(180), 0, 1);
		testMesh1.addChild(camera);

		addObject(testMesh1);

		directionalLight.getTransform().setRot(new Quaternion(new Vector3f(1, 0, 0), (float) Math.toRadians(-45)));
	}
}
