package fr.veridiangames.client.rendering.renderers.game.entities.players.skeleton;

import fr.veridiangames.core.maths.Mat4;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.utils.Color4f;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public abstract class Bone
{
	protected Transform transform;
	protected Vec3 scale;
	protected Bone parent;
	protected List<Bone> childs;
	protected Color4f color;

	public Bone(Transform transform, Bone parent)
	{
		this.transform = new Transform(transform.getPosition(), transform.getRotation(), new Vec3(1, 1, 1));
		this.scale = transform.getLocalScale();
		this.parent = parent;
		this.color = Color4f.WHITE.copy();
	}

	protected abstract void update();

	public void updateChilds()
	{
		for (Bone bone : this.getChilds())
		{
			bone.update();
			bone.updateChilds();
		}
	}

	public void setBufferData(FloatBuffer buffer)
	{
		buffer.put(transform.toMatrix().mul(Mat4.scale(scale)).getComponents());
		buffer.put(color.toArray());
		if (childs != null)
			setChildsBuffer(buffer);
	}

	public void setChildsBuffer(FloatBuffer buffer)
	{
		for (Bone bone : this.getChilds())
		{
			bone.setParent(this);
			bone.setBufferData(buffer);
		}
	}

	public Bone getParent() {
		return parent;
	}

	public Transform getTransform() {
		return transform;
	}

	public List<Bone> getChilds() {
		return childs;
	}

	public void setParent(Bone parent) {
		this.parent = parent;
		this.transform.setParent(parent.transform);
	}

	public void setTransform(Transform transform) {
		this.transform = transform;
	}

	public void setChild(List<Bone> childs) {
		this.childs = childs;
	}

	public void addChild(Bone bone)
	{
		if (this.childs == null)
			this.childs = new ArrayList<>();
		this.childs.add(bone);
	}

	public Color4f getColor() {
		return color;
	}

	public void setColor(Color4f color) {
		this.color = color.copy();
	}

	public void setParentTransform(Transform parentTransform) {
		Quat quat = new Quat(0, parentTransform.getRotation().y, 0, parentTransform.getRotation().w);
//		System.out.println(parentTransform.getRotation());
		this.transform.setParent(new Transform(parentTransform.getPosition(), quat));
	}
}