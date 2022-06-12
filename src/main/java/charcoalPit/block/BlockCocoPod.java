package charcoalPit.block;

import charcoalPit.core.MethodHelper;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import java.util.Random;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class BlockCocoPod extends HorizontalDirectionalBlock implements BonemealableBlock {
	
	public static final IntegerProperty AGE= BlockStateProperties.AGE_2;
	//public static final BooleanProperty DOUBLE=BooleanProperty.create("double");
	
	public static final VoxelShape NORTH1=Block.box(5.0D, 5.0D, 1.0D, 11.0D, 12.0D, 7.0D);
	public static final VoxelShape SOUTH1=Block.box(5.0D, 5.0D, 9.0D, 11.0D, 12.0D, 15.0D);
	public static final VoxelShape EAST1=Block.box(9.0D, 5.0D, 5.0D, 15.0D, 12.0D, 11.0D);
	public static final VoxelShape WEST1=Block.box(1.0D, 5.0D, 5.0D, 7.0D, 12.0D, 11.0D);
	
	public static final VoxelShape NORTH2= Shapes.joinUnoptimized(NORTH1.move(4D/16D,0D,0D),NORTH1.move(-4D/16D,0D,0D), BooleanOp.OR);
	public static final VoxelShape SOUTH2= Shapes.joinUnoptimized(SOUTH1.move(4D/16D,0D,0D),SOUTH1.move(-4D/16D,0D,0D), BooleanOp.OR);
	public static final VoxelShape EAST2= Shapes.joinUnoptimized(EAST1.move(0D,0D,4D/16D),EAST1.move(0D,0D,-4D/16D), BooleanOp.OR);
	public static final VoxelShape WEST2= Shapes.joinUnoptimized(WEST1.move(0D,0D,4D/16D),WEST1.move(0D,0D,-4D/16D), BooleanOp.OR);
	
	public BlockCocoPod(Properties prop){
		super(prop);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING,AGE);
	}
	
	@Override
	public boolean isValidBonemealTarget(BlockGetter worldIn, BlockPos pos, BlockState state, boolean isClient) {
		return state.getValue(AGE)<2;
	}
	
	@Override
	public boolean isBonemealSuccess(Level worldIn, Random rand, BlockPos pos, BlockState state) {
		return true;
	}
	
	@Override
	public void performBonemeal(ServerLevel worldIn, Random rand, BlockPos pos, BlockState state) {
		worldIn.setBlock(pos,state.setValue(AGE,state.getValue(AGE)+1),2);
	}
	
	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return true;
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, Random random) {
		if (random.nextFloat()<0.166F) {
			int i = state.getValue(AGE);
			if (i < 2 && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, worldIn.random.nextInt(5) == 0)) {
				worldIn.setBlock(pos, state.setValue(AGE, i + 1), 2);
				net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
			}
		}
	}
	
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		BlockState blockstate = this.defaultBlockState();
		LevelReader levelreader = pContext.getLevel();
		BlockPos blockpos = pContext.getClickedPos();
		
		for(Direction direction : pContext.getNearestLookingDirections()) {
			if (direction.getAxis().isHorizontal()) {
				blockstate = blockstate.setValue(FACING, direction);
				if (blockstate.canSurvive(levelreader, blockpos)) {
					return blockstate;
				}
			}
		}
		
		return null;
	}
	
	@Override
	public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
		return MethodHelper.isBlockInTag(worldIn.getBlockState(pos.relative(state.getValue(FACING))),BlockTags.JUNGLE_LOGS);
	}
	
	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		return facing == stateIn.getValue(FACING) && !stateIn.canSurvive(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		if(true){
			switch (state.getValue(FACING)){
				case NORTH:return NORTH2;
				case SOUTH:return SOUTH2;
				case EAST:return EAST2;
				default:return WEST2;
			}
		}else{
			switch (state.getValue(FACING)){
				case NORTH:return NORTH1;
				case SOUTH:return SOUTH1;
				case EAST:return EAST1;
				default:return WEST1;
			}
		}
	}
}
