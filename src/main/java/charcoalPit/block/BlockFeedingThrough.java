package charcoalPit.block;

import charcoalPit.tile.TileFeedingThrough;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BlockFeedingThrough extends Block implements EntityBlock {
	
	public static final BooleanProperty ROTATED= BooleanProperty.create("rotated");
	public static final BooleanProperty HAS_BAY= BooleanProperty.create("has_bay");
	
	public static final VoxelShape NORTH= Shapes.box(2D/16D, 0D, 0D, 14D/16D, 0.5D, 1D);
	public static final VoxelShape WEST= Shapes.box(0D, 0D, 2D/16D, 1D, 0.5D, 14D/16D);
	
	public BlockFeedingThrough(Properties p_49795_) {
		super(p_49795_);
	}
	
	@Nullable
	@Override
	public BlockPathTypes getAiPathNodeType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob entity) {
		return BlockPathTypes.FENCE;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(ROTATED,HAS_BAY);
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		return this.defaultBlockState().setValue(ROTATED,pContext.getHorizontalDirection().getAxis()== Direction.Axis.Z).setValue(HAS_BAY,false);
	}
	
	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		if(pState.getValue(ROTATED))
			return WEST;
		else
			return NORTH;
	}
	
	@Override
	public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
		if (pState.hasBlockEntity() && (!pState.is(pNewState.getBlock()) || !pNewState.hasBlockEntity())) {
			((TileFeedingThrough)pLevel.getBlockEntity(pPos)).dropInventory();
			pLevel.removeBlockEntity(pPos);
		}
	}
	
	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		if(pLevel.isClientSide)
			return InteractionResult.SUCCESS;
		else{
			BlockEntity e=pLevel.getBlockEntity(pPos);
			if(e instanceof TileFeedingThrough tile){
				if(pPlayer.getItemInHand(pHand).isEmpty()){
					pPlayer.setItemInHand(pHand,tile.inventory.extractItem(0,64,false));
					pLevel.playSound(null,pPos,SoundEvents.GRASS_BREAK,SoundSource.PLAYERS,1F,1F);
					pLevel.setBlock(pPos,pState.setValue(HAS_BAY,!tile.inventory.getStackInSlot(0).isEmpty()),3);
					return InteractionResult.SUCCESS;
				}else {
					pPlayer.setItemInHand(pHand,tile.inventory.insertItem(0,pPlayer.getItemInHand(pHand),false));
					pLevel.playSound(null,pPos,SoundEvents.GRASS_BREAK,SoundSource.PLAYERS,1F,1F);
					pLevel.setBlock(pPos,pState.setValue(HAS_BAY,!tile.inventory.getStackInSlot(0).isEmpty()),3);
					return InteractionResult.CONSUME;
				}
			}
			return InteractionResult.FAIL;
		}
	}
	
	@Override
	public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
		super.neighborChanged(pState, pLevel, pPos, pBlock, pFromPos, pIsMoving);
		BlockEntity e=pLevel.getBlockEntity(pPos);
		if(e instanceof TileFeedingThrough tile){
			tile.powered=pLevel.hasNeighborSignal(pPos);
		}
	}
	
	@Override
	public boolean hasAnalogOutputSignal(BlockState pState) {
		return true;
	}
	
	@Override
	public int getAnalogOutputSignal(BlockState pState, Level pLevel, BlockPos pPos) {
		BlockEntity e=pLevel.getBlockEntity(pPos);
		if(e instanceof TileFeedingThrough tile){
			return (15*tile.inventory.getStackInSlot(0).getCount())/tile.inventory.getSlotLimit(0);
		}
		return 0;
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new TileFeedingThrough(pPos,pState);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
		return pLevel.isClientSide()?null:(level,blockpos,blockstate,t)-> {
			if (t instanceof TileFeedingThrough tile) {
				tile.tick();
			}
		};
	}
}
