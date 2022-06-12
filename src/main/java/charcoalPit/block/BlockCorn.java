package charcoalPit.block;

import charcoalPit.core.ModItemRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ItemLike;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import java.util.Random;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockCorn extends CropBlock {
	
	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 24.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 24.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 32.0D, 16.0D)};
	
	
	public BlockCorn(Properties builder) {
		super(builder);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SHAPE_BY_AGE[state.getValue(this.getAgeProperty())];
	}
	
	@Override
	protected int getBonemealAgeIncrease(Level worldIn) {
		return Mth.nextInt(worldIn.random, 1, 3);
	}
	
	@Override
	public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
		return (getAge(state)<5||worldIn.getBlockState(pos.relative(Direction.UP)).isAir())&&super.canSurvive(state,worldIn,pos);
	}
	
	public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, Random random) {
		if (!worldIn.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
		if (worldIn.getRawBrightness(pos, 0) >= 9) {
			int i = this.getAge(state);
			if (isValidBonemealTarget(worldIn,pos,state,worldIn.isClientSide)) {
				float f = getGrowthSpeed(this, worldIn, pos);
				f=f*getGrowthMultiplier();
				if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, random.nextInt((int)(25.0F / f) + 1) == 0)) {
					worldIn.setBlock(pos, this.getStateForAge(i + 1), 2);
					net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
				}
			}
		}
		
	}
	
	public float getGrowthMultiplier(){
		return 0.5F;
	}
	
	@Override
	public boolean isValidBonemealTarget(BlockGetter worldIn, BlockPos pos, BlockState state, boolean isClient) {
		return super.isValidBonemealTarget(worldIn,pos,state,isClient)&&worldIn.getBlockState(pos.relative(Direction.UP)).isAir();
	}
	
	@Override
	public boolean collisionExtendsVertically(BlockState state, BlockGetter world, BlockPos pos, Entity collidingEntity) {
		return true;
	}
	
	@Override
	protected ItemLike getBaseSeedId() {
		return ModItemRegistry.Corn;
	}
}
