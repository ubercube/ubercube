package fr.veridiangames.core.game.world.vegetations.trees;

import fr.veridiangames.core.game.world.Block;
import fr.veridiangames.core.game.world.World;

public class OakTree extends Tree
{

	public OakTree(World world) {
		super(world, Tree.OAK);
		super.setWoodBlock(Block.WOOD);
		super.setLeafBlock(Block.OAK_LEAF);
		super.setTrunkProperties(2, 10);
		super.setMainLeavesProperties(4);
		super.setLeavesProperties(4, 6, 5, 2, 5, 10);
		super.setBranchProperties(1, 0.5f);
	}
}
