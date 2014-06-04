package dz.nouri.lwljgl.engine.component;

import dz.nouri.lwljgl.engine.core.Vector3f;
import dz.nouri.lwljgl.engine.render.ForwardDirectional;

public class DirectionalLight extends BaseLight
{
	public DirectionalLight(Vector3f color, float intensity)
	{
		super(color, intensity);

		setShader(ForwardDirectional.getInstance());
	}

	public Vector3f getDirection()
	{
		return getTransform().getTransformedRot().getForward();
	}
}
