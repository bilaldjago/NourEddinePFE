package dz.nouri.lwljgl.engine.component;

import dz.nouri.lwljgl.engine.render.Material;
import dz.nouri.lwljgl.engine.render.Mesh;
import dz.nouri.lwljgl.engine.render.RenderingEngine;
import dz.nouri.lwljgl.engine.render.Shader;

public class MeshRenderer extends GameComponent
{
	private Mesh mesh;
	private Material material;

	public MeshRenderer(Mesh mesh, Material material)
	{
		this.mesh = mesh;
		this.material = material;
	}

	@Override
	public void render(Shader shader, RenderingEngine renderingEngine)
	{
		shader.bind();
		shader.updateUniforms(getTransform(), material, renderingEngine);
		mesh.draw();
	}
}
