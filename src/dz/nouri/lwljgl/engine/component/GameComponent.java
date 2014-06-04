package dz.nouri.lwljgl.engine.component;

import dz.nouri.lwljgl.engine.core.GameObject;
import dz.nouri.lwljgl.engine.core.Transform;
import dz.nouri.lwljgl.engine.render.RenderingEngine;
import dz.nouri.lwljgl.engine.render.Shader;



public abstract class GameComponent
{
	private GameObject parent;

	public void input(float delta) {}
	public void update(float delta) {}
	public void render(Shader shader, RenderingEngine renderingEngine) {}

	public void setParent(GameObject parent)
	{
		this.parent = parent;
	}

	public Transform getTransform()
	{
		return parent.getTransform();
	}

	public void addToRenderingEngine(RenderingEngine renderingEngine) {}
}

