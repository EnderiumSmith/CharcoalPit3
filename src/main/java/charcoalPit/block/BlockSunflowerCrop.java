package charcoalPit.block;

import charcoalPit.core.ModItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockSunflowerCrop extends BlockCorn{
	public BlockSunflowerCrop(Properties builder) {
		super(builder);
	}
	
	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 24.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 24.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 28.0D, 16.0D)};
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SHAPE_BY_AGE[state.getValue(this.getAgeProperty())];
	}
	
	@Override
	public float getGrowthMultiplier() {
		return 0.75F;
	}
	
	@Override
	protected ItemLike getBaseSeedId() {
		return ModItemRegistry.SunflowerSeeds;
	}
}
