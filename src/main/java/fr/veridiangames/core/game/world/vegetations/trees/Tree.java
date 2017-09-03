package fr.veridiangames.core.game.world.vegetations.trees;

import fr.veridiangames.core.game.data.world.WorldGen;
import fr.veridiangames.core.game.world.World;
import fr.veridiangames.core.maths.Vec3i;
import fr.veridiangames.core.utils.Color4f;

import java.util.ArrayList;
import java.util.List;

import static fr.veridiangames.core.maths.Mathf.abs;
import static fr.veridiangames.core.maths.Mathf.round;

public abstract class Tree
{
	public static final String BIG_OAK = "big_oak";
	public static final String OAK = "oak";

	protected World world;
	protected WorldGen gen;
	protected String name;

	protected int trunkSize;
	protected int trunkHeight;

	protected int mainLeavesSize;

	protected List<Vec3i> leaves;
	protected int leavesSize;

	protected int branchSize;
	protected float branchHeightPercent;

	protected Color4f leafBlock;
	protected Color4f woodBlock;

	public Tree(World world, String name)
	{
		this.name = name;
		this.world = world;
		this.gen = world.getWorldGen();
		this.leaves = new ArrayList<>();
	}

	protected void setWoodBlock(Color4f block)
	{
		this.woodBlock = block;
	}

	protected void setLeafBlock(Color4f block)
	{
		this.leafBlock = block;
	}

	protected void setTrunkProperties(int size, int height)
	{
		this.trunkSize = size;
		this.trunkHeight = height;
	}

	protected void setMainLeavesProperties(int size)
	{
		this.mainLeavesSize = size;
	}

	protected void setLeavesProperties(int min, int max, int range, int size, int height, int maxHeight)
	{
		this.leavesSize = size;
		int count = (int) round(gen.getRandom(min, max));
		for (int i = 0; i < count; i++)
		{
			Vec3i pos = new Vec3i();
			pos.x = (int) round(gen.getRandom(-range, range));
			pos.y = (int) round(gen.getRandom(0, maxHeight - height)) + height;
			pos.z = (int) round(gen.getRandom(-range, range));
			leaves.add(pos);
		}
	}

	protected void setBranchProperties(int size, float heightPercent)
	{
		this.branchSize = size;
		this.branchHeightPercent = heightPercent;
	}

	public void generate(Vec3i origin)
	{
		buildMainLeaves(origin, mainLeavesSize);
		buildLeaves(origin, leavesSize);
		buildTrunk(origin, trunkSize, trunkHeight);
		buildBranches(origin);
	}

	protected void buildTrunk(Vec3i origin, int w, int h)
	{
		for (int z = 0; z < w; z++)
		{
			for (int x = 0; x < w; x++)
			{
				for (int y = 0; y < h; y++)
				{
					int xx = origin.x + x - w / 2;
					int yy = origin.y + y;
					int zz = origin.z + z - w / 2;
					addBlock(new Vec3i(xx, yy, zz), woodBlock.copy().add(world.getWorldGen().getRandom() * 0.05f).getARGB());
				}
			}
		}
	}

	protected void buildBranches(Vec3i origin)
	{
		for (int i = 0; i < leaves.size(); i++)
		{
			Vec3i start = origin.copy().add(0, (int)(trunkHeight * branchHeightPercent) + (int)round(gen.getRandom(-3, 3)), 0);
			Vec3i end = leaves.get(i).copy().add(origin);
			float dist = start.distance(end);
			for (int j = 0; j < dist; j++)
			{
				float n = (float)j / (float)dist;
				int xLerp = (int)(start.x + (end.x - start.x) * n);
				int yLerp = (int)(start.y + (end.y - start.y) * n);
				int zLerp = (int)(start.z + (end.z - start.z) * n);
				for (int x = 0; x < branchSize; x++)
				{
					for (int y = 0; y < branchSize; y++)
					{
						for (int z = 0; z < branchSize; z++)
						{
							addBlock(new Vec3i(xLerp + x, yLerp + y, zLerp + z), woodBlock.copy().add(world.getWorldGen().getRandom() * 0.05f).getARGB());
						}
					}
				}
			}
		}
	}

	protected void buildMainLeaves(Vec3i origin, int size)
	{
		for (int x = -size; x < size; x++)
		{
			for (int y = -size; y < size; y++)
			{
				for (int z = -size; z < size; z++)
				{
					Vec3i res = new Vec3i();
					res.x = origin.x + x;
					res.y = origin.y + y + trunkHeight;
					res.z = origin.z + z;

					float dist = abs(x * x + y * y + z * z);
					if (dist <= (size * size))
						addBlock(res, leafBlock.copy().add(world.getWorldGen().getRandom() * 0.05f).getARGB());
				}
			}
		}
	}

	protected void buildLeaves(Vec3i origin, int size)
	{
		for (int i = 0; i < leaves.size(); i++)
		{
			for (int x = -size; x < size; x++)
			{
				for (int y = -size; y < size; y++)
				{
					for (int z = -size; z < size; z++)
					{
						Vec3i pos = leaves.get(i);
						Vec3i res = new Vec3i();
						res.x = origin.x + x + pos.x;
						res.y = origin.y + y + pos.y;
						res.z = origin.z + z + pos.z;

						float dist = abs(x * x + y * y + z * z);
						if (dist <= (size * size - 2))
							addBlock(res, leafBlock.copy().add(world.getWorldGen().getRandom() * 0.05f).getARGB());
					}
				}
			}
		}
	}

	protected void addBlock(Vec3i pos, int block)
	{
		world.addBlock(pos.x, pos.y, pos.z, block);
	}
}
