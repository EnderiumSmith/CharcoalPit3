package charcoalPit.block;


import charcoalPit.core.MethodHelper;
import net.minecraft.world.item.context.BlockPlaceContext;
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

public class BlockBananaPod extends HorizontalDirectionalBlock implements BonemealableBlock {
	
	public static final IntegerProperty AGE= BlockStateProperties.AGE_2;
	//public static final IntegerProperty COUNT=IntegerProperty.create("count",1,3);
	
	public static VoxelShape North1=Block.box(6.0D, 0.0D, 1.0D, 10.0D, 12.0D, 5.0D);
	public static VoxelShape South1=Block.box(6.0D, 0.0D, 11.0D, 10.0D, 12.0D, 15.0D);
	public static VoxelShape East1=Block.box(11.0D, 0.0D, 6.0D, 15.0D, 12.0D, 10.0D);
	public static VoxelShape West1=Block.box(1.0D, 0.0D, 6.0D, 5.0D, 12.0D, 10.0D);
	
	public static VoxelShape North2=Shapes.joinUnoptimized(North1.move(3D/16D,0D,0D),North1.move(-3D/16D,0D,0D), BooleanOp.OR);
	public static VoxelShape South2=Shapes.joinUnoptimized(South1.move(3D/16D,0D,0D),South1.move(-3D/16D,0D,0D), BooleanOp.OR);
	public static VoxelShape East2=Shapes.joinUnoptimized(East1.move(0D,0D,3D/16D),East1.move(0D,0D,-3D/16D), BooleanOp.OR);
	public static VoxelShape West2=Shapes.joinUnoptimized(West1.move(0D,0D,3D/16D),West1.move(0D,0D,-3D/16D), BooleanOp.OR);
	
	public static VoxelShape North3=Shapes.joinUnoptimized(North1,Shapes.joinUnoptimized(North1.move(5D/16D,0D,0D),North1.move(-5D/16D,0D,0D),BooleanOp.OR),BooleanOp.OR);
	public static VoxelShape South3=Shapes.joinUnoptimized(South1,Shapes.joinUnoptimized(South1.move(5D/16D,0D,0D),South1.move(-5D/16D,0D,0D),BooleanOp.OR),BooleanOp.OR);
	public static VoxelShape East3=Shapes.joinUnoptimized(East1,Shapes.joinUnoptimized(East1.move(0D,0D,5D/16D),East1.move(0D,0D,-5D/16D),BooleanOp.OR),BooleanOp.OR);
	public static VoxelShape West3=Shapes.joinUnoptimized(West1,Shapes.joinUnoptimized(West1.move(0D,0D,5D/16D),West1.move(0D,0D,-5D/16D),BooleanOp.OR),BooleanOp.OR);
	
	public BlockBananaPod(Properties p){
		super(p);
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
		/*switch (state.getValue(COUNT)){
			case 1:{
				switch (state.getValue(FACING)){
					case NORTH:return North1;
					case SOUTH:return South1;
					case EAST:return  East1;
					default:return West1;
				}
			}
			case 2:{
				switch (state.getValue(FACING)){
					case NORTH:return North2;
					case SOUTH:return South2;
					case EAST:return  East2;
					default:return West2;
				}
			}
			case 3:{
				switch (state.getValue(FACING)){
					case NORTH:return North3;
					case SOUTH:return South3;
					case EAST:return  East3;
					default:return West3;
				}
			}
		}*/
		switch (state.getValue(FACING)){
			case NORTH:return North3;
			case SOUTH:return South3;
			case EAST:return  East3;
			default:return West3;
		}
		//return Shapes.empty();
	}
}
