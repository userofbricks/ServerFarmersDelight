package vectorwing.farmersdelight.common.block.entity;

import net.minecraft.block.Block;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.shape.VoxelShape;

public interface Basket extends Inventory
{
	VoxelShape[] COLLECTION_AREA_SHAPES = {
			Block.createCuboidShape(0.0D, -16.0D, 0.0D, 16.0D, 16.0D, 16.0D),    // down
			Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 32.0D, 16.0D),        // up
			Block.createCuboidShape(0.0D, 0.0D, -16.0D, 16.0D, 16.0D, 16.0D),    // north
			Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 32.0D),        // south
			Block.createCuboidShape(-16.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),    // west
			Block.createCuboidShape(0.0D, 0.0D, 0.0D, 32.0D, 16.0D, 16.0D)        // east
	};

	default VoxelShape getFacingCollectionArea(int facingIndex) {
		return COLLECTION_AREA_SHAPES[facingIndex];
	}

	/**
	 * Gets the world X position for this hopper entity.
	 */
	double getLevelX();

	/**
	 * Gets the world Y position for this hopper entity.
	 */
	double getLevelY();

	/**
	 * Gets the world Z position for this hopper entity.
	 */
	double getLevelZ();
}