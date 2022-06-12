package charcoalPit.block;

import charcoalPit.tile.TileFeedingThrough;
import charcoalPit.tile.TileNestBox;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BlockNestBox extends Block implements EntityBlock {
	
	public static final IntegerProperty EGGS= IntegerProperty.create("eggs",0,4);
	
	protected static final VoxelShape BOTTOM_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
	
	public BlockNestBox(Properties p_49795_) {
		super(p_49795_);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(EGGS);
	}
	
	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return BOTTOM_AABB;
	}
	
	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		if(pLevel.isClientSide)
			return InteractionResult.SUCCESS;
		else{
			BlockEntity e=pLevel.getBlockEntity(pPos);
			if(e instanceof TileNestBox tile){
				if(pPlayer.getItemInHand(pHand).isEmpty()){
					pPlayer.setItemInHand(pHand,tile.inventory.extractItem(0,64,false));
					pLevel.playSound(null,pPos, SoundEvents.CHICKEN_EGG, SoundSource.PLAYERS,1F,1F);
					return InteractionResult.SUCCESS;
				}else {
					pPlayer.setItemInHand(pHand,tile.inventory.insertItem(0,pPlayer.getItemInHand(pHand),false));
					pLevel.playSound(null,pPos,SoundEvents.CHICKEN_EGG,SoundSource.PLAYERS,1F,1F);
					return InteractionResult.CONSUME;
				}
			}
			return InteractionResult.FAIL;
		}
	}
	
	@Override
	public boolean hasAnalogOutputSignal(BlockState pState) {
		return true;
	}
	
	@Override
	public int getAnalogOutputSignal(BlockState pState, Level pLevel, BlockPos pPos) {
		BlockEntity e=pLevel.getBlockEntity(pPos);
		if(e instanceof TileNestBox tile){
			return 15*tile.inventory.getStackInSlot(0).getCount()/tile.inventory.getSlotLimit(0);
		}
		return 0;
	}
	
	@Override
	public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
		super.neighborChanged(pState, pLevel, pPos, pBlock, pFromPos, pIsMoving);
		if(pPos.below().equals(pFromPos)){
			BlockEntity e=pLevel.getBlockEntity(pPos);
			if(e instanceof TileNestBox tile){
				tile.updateIncubation(false);
			}
		}
	}
	
	@Override
	public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
		if (pState.hasBlockEntity() && (!pState.is(pNewState.getBlock()) || !pNewState.hasBlockEntity())) {
			((TileNestBox)pLevel.getBlockEntity(pPos)).dropInventory();
			pLevel.removeBlockEntity(pPos);
		}
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new TileNestBox(pPos,pState);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
		return pLevel.isClientSide()?null:(level,blockpos,blockstate,t)-> {
			if (t instanceof TileNestBox tile) {
				tile.tick();
			}
		};
	}
}
