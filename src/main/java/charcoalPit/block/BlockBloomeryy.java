package charcoalPit.block;

import charcoalPit.gui.BloomeryContainer;
import charcoalPit.gui.CeramicPotContainer;
import charcoalPit.tile.TileBloomeryy;
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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class BlockBloomeryy extends HorizontalDirectionalBlock implements EntityBlock {
	
	public static final BooleanProperty ACTIVE= BlockStateProperties.LIT;
	
	public BlockBloomeryy(Properties p_54120_) {
		super(p_54120_);
		registerDefaultState(defaultBlockState().setValue(ACTIVE,false));
	}
	
	@Override
	public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
		if(state.getValue(ACTIVE))
			return 15;
		return 0;
	}
	
	public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
		if (!pEntity.fireImmune() && pEntity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)pEntity) && pState.getValue(ACTIVE)) {
			pEntity.hurt(DamageSource.HOT_FLOOR, 2.0F);
		}
		
		super.stepOn(pLevel, pPos, pState, pEntity);
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
			((TileBloomeryy)pLevel.getBlockEntity(pPos)).dropInventory();
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
			
			double centerX = (double)pPos.getX() + 0.5F;
			double centerY = (double)pPos.getY() + 2F;
			double centerZ = (double)pPos.getZ() + 0.5F;
			//double d3 = 0.2199999988079071D;
			//double d4 = 0.27000001072883606D;
			pLevel.addParticle(ParticleTypes.SMOKE, centerX+(pRand.nextDouble()-0.5), centerY-1, centerZ+(pRand.nextDouble()-0.5), 0.0D, 0.1D, 0.0D);
			pLevel.addParticle(ParticleTypes.SMOKE, centerX+(pRand.nextDouble()-0.5), centerY-1, centerZ+(pRand.nextDouble()-0.5), 0.0D, 0.15D, 0.0D);
			pLevel.addParticle(ParticleTypes.SMOKE, centerX+(pRand.nextDouble()-0.5), centerY-1, centerZ+(pRand.nextDouble()-0.5), 0.0D, 0.1D, 0.0D);
			pLevel.addParticle(ParticleTypes.SMOKE, centerX+(pRand.nextDouble()-0.5), centerY-1, centerZ+(pRand.nextDouble()-0.5), 0.0D, 0.15D, 0.0D);
			
		}
	}
	
	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		if(!pLevel.isClientSide){
			NetworkHooks.openGui((ServerPlayer)pPlayer, new MenuProvider() {
				
				@Override
				public AbstractContainerMenu createMenu(int p_createMenu_1_, Inventory p_createMenu_2_, Player p_createMenu_3_) {
					return new BloomeryContainer(p_createMenu_1_, pPos, p_createMenu_2_, (TileBloomeryy) pLevel.getBlockEntity(pPos));
				}
				
				@Override
				public Component getDisplayName() {
					return new TranslatableComponent("screen.charcoal_pit.bloomery");
				}
			}, pPos);;
		}
		return InteractionResult.SUCCESS;
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new TileBloomeryy(pPos,pState);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
		return pLevel.isClientSide()?null:(level,blockpos,blockstate,t)-> {
			if (t instanceof TileBloomeryy tile) {
				tile.tick();
			}
		};
	}
}
