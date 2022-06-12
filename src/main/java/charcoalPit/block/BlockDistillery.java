package charcoalPit.block;

import charcoalPit.gui.BarrelContainer;
import charcoalPit.gui.DistilleryContainer;
import charcoalPit.tile.TileDistillery;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class BlockDistillery extends HorizontalDirectionalBlock implements EntityBlock {
	
	public static final BooleanProperty ACTIVE= BlockStateProperties.LIT;
	
	public BlockDistillery(Properties p_54120_) {
		super(p_54120_);
		registerDefaultState(defaultBlockState().setValue(ACTIVE,false));
	}
	
	@Override
	public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
		if(state.getValue(ACTIVE))
			return 15;
		return 0;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(ACTIVE,FACING);
	}
	
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
	}
	
	@Override
	public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
		if (pState.hasBlockEntity() && (!pState.is(pNewState.getBlock()) || !pNewState.hasBlockEntity())) {
			((TileDistillery)pLevel.getBlockEntity(pPos)).dropInventory();
			pLevel.removeBlockEntity(pPos);
		}
	}
	
	public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, Random pRand) {
		if (pState.getValue(ACTIVE)) {
			double d0 = (double)pPos.getX() + 0.5D;
			double d1 = (double)pPos.getY();
			double d2 = (double)pPos.getZ() + 0.5D;
			if (pRand.nextDouble() < 0.1D) {
				pLevel.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
			}
			
			Direction direction = pState.getValue(FACING);
			Direction.Axis direction$axis = direction.getAxis();
			double d3 = 0.52D;
			double d4 = pRand.nextDouble() * 0.6D - 0.3D;
			double d5 = direction$axis == Direction.Axis.X ? (double)direction.getStepX() * 0.52D : d4;
			double d6 = pRand.nextDouble() * 6.0D / 16.0D;
			double d7 = direction$axis == Direction.Axis.Z ? (double)direction.getStepZ() * 0.52D : d4;
			pLevel.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
			pLevel.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
		}
	}
	
	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		if(pLevel.isClientSide)
			return InteractionResult.SUCCESS;
		if(pPlayer.getItemInHand(pHand).getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent()) {
			if (FluidUtil.interactWithFluidHandler(pPlayer, pHand, pLevel, pPos, null))
				return InteractionResult.SUCCESS;
		}
		NetworkHooks.openGui((ServerPlayer)pPlayer, new MenuProvider() {
			
			@Override
			public AbstractContainerMenu createMenu(int arg0, Inventory arg1, Player arg2) {
				return new DistilleryContainer(arg0, pPos, arg1, (TileDistillery)pLevel.getBlockEntity(pPos));
			}
			
			@Override
			public Component getDisplayName() {
				return new TranslatableComponent("screen.charcoal_pit.distillery");
			}
		}, pPos);
		return InteractionResult.SUCCESS;
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new TileDistillery(pPos,pState);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
		return pLevel.isClientSide()?null:(level,blockpos,blockstate,t)-> {
			if (t instanceof TileDistillery tile) {
				tile.tick();
			}
		};
	}
}
