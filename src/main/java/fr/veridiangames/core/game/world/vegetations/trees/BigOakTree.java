package fr.veridiangames.core.game.world.vegetations.trees;

import fr.veridiangames.core.game.world.Block;
import fr.veridiangames.core.game.world.World;

public class BigOakTree extends Tree
{
	public BigOakTree(World world)
	{
		super(world, Tree.BIG_OAK);
		super.setWoodBlock(Block.BIG_OAK_WOOD);
		super.setLeafBlock(Block.BIG_OAK_LEAF);
		super.setTrunkProperties(3, 20);
		super.setMainLeavesProperties(7);
		super.setLeavesProperties(6, 10, 10, 5, 15, 20);
		super.setBranchProperties(2, 0.25f);
	}
}
