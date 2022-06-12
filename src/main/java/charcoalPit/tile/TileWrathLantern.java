package charcoalPit.tile;

import charcoalPit.core.ModTileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileWrathLantern extends BlockEntity {
	public TileWrathLantern(BlockPos pWorldPosition, BlockState pBlockState) {
		super(ModTileRegistry.WrathLantern, pWorldPosition, pBlockState);
	}
}
