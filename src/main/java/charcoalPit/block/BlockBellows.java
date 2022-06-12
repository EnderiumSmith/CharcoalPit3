package charcoalPit.block;

import java.util.Random;

import charcoalPit.core.ModBlockRegistry;
import charcoalPit.tile.TileBlastFurnace;
import charcoalPit.tile.TileBloomeryy;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

public class BlockBellows extends Block{
	
	public static final DirectionProperty FACING=DirectionalBlock.FACING;
	public static final BooleanProperty PUSH=BooleanProperty.create("push");
	
	public static final VoxelShape NORTH=Shapes.box(0D, 0D, 0D, 1D, 1D, 6D/16D);
	public static final VoxelShape SOUTH=Shapes.box(0D, 0D, 10D/16D, 1D, 1D, 1D);
	public static final VoxelShape WEST=Shapes.box(0D, 0D, 0D, 6D/16D, 1D, 1D);
	public static final VoxelShape EAST=Shapes.box(10D/16D, 0D, 0D, 1D, 1D, 1D);
	public static final VoxelShape UP=Shapes.box(0D, 10D/16D, 0D, 1D, 1D, 1D);
	public static final VoxelShape DOWN=Shapes.box(0D, 0D, 0D, 1D, 6D/16D, 1D);

	public BlockBellows() {
		super(Properties.of(Material.WOOD).strength(2, 3));
		registerDefaultState(defaultBlockState().setValue(PUSH, false));
	}
	
	public BlockBellows(Material m){
		super(Properties.of(m).strength(2, 3));
		registerDefaultState(defaultBlockState().setValue(PUSH, false));
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		if(!state.getValue(PUSH))
			return Shapes.block();
		switch(state.getValue(FACING)) {
		case DOWN:return DOWN;
		case EAST:return EAST;
		case NORTH:return NORTH;
		case SOUTH:return SOUTH;
		case UP:return UP;
		case WEST:return WEST;
		default:return Shapes.block();
		
		}
	}
	
	@Override
	public VoxelShape getOcclusionShape(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return Shapes.empty();
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(FACING, PUSH);
	}
	
	@Override
	public boolean useShapeForLightOcclusion(BlockState state) {
		return true;
	}
	
	public BlockState getStateForPlacement(BlockPlaceContext context) {
	      return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection());
	   }
	
	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player,
			InteractionHand handIn, BlockHitResult hit) {
		if(!state.getValue(PUSH)) {
			if(!worldIn.isClientSide) {
				worldIn.scheduleTick(pos, this, 10);
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}
	
	@Override
	public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand) {
		if(state.getValue(PUSH)) {
			worldIn.setBlockAndUpdate(pos, state.setValue(PUSH, false));
		}else {
			worldIn.setBlockAndUpdate(pos, state.setValue(PUSH, true));
			blow(worldIn, pos, state);
			worldIn.scheduleTick(pos, this, 20);
			worldIn.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 1F, 1F);
		}
	}
	
	public void blow(Level world, BlockPos pos, BlockState state) {
		if(state.getValue(FACING).getAxis().isHorizontal()){
			BlockPos pos2=pos.relative(state.getValue(FACING));
			BlockState front=world.getBlockState(pos2);
			if(front.getBlock()==ModBlockRegistry.Bloomeryy){
				if(front.getValue(BlockBloomeryy.FACING).getOpposite()!=state.getValue(FACING)) {
					//not from front side
					((TileBloomeryy) world.getBlockEntity(pos2)).blastAir();
				}
			}
			if(front.getBlock()==ModBlockRegistry.BlastFurnace){
				if(front.getValue(BlockBloomeryy.FACING).getOpposite()!=state.getValue(FACING)) {
					//not from front side
					((TileBlastFurnace) world.getBlockEntity(pos2)).blastAir();
				}
			}
		}
	}

}
