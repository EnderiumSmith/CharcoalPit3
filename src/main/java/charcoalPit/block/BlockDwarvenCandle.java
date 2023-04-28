package charcoalPit.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Random;

public class BlockDwarvenCandle extends DirectionalBlock {
	public static final BooleanProperty PRIMED= BlockStateProperties.LIT;
	
	private static final Map<Direction, VoxelShape> AABBS = Maps.newEnumMap(ImmutableMap.of(
			Direction.NORTH, Block.box(5.5D, 3.0D, 11.0D, 10.5D, 13.0D, 16.0D),
			Direction.SOUTH, Block.box(5.5D, 3.0D, 0.0D, 10.5D, 13.0D, 5.0D),
			Direction.WEST, Block.box(11.0D, 3.0D, 5.5D, 16.0D, 13.0D, 10.5D),
			Direction.EAST, Block.box(0.0D, 3.0D, 5.5D, 5.0D, 13.0D, 10.5D),
			Direction.UP,Block.box(6.0D, 0.0D, 6.0D, 10.0D, 12.0D, 10.0D),
			Direction.DOWN,Block.box(6.0D, 4.0D, 6.0D, 10.0D, 16.0D, 10.0D)));
	
	public BlockDwarvenCandle() {
		super(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().instabreak().sound(SoundType.GRASS));
		registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.UP).setValue(PRIMED,false));
	}
	
	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return AABBS.get(pState.getValue(FACING));
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(FACING,PRIMED);
	}
	
	public void setPrimed(Level level, BlockPos pos,BlockState state){
		level.setBlock(pos,state.setValue(PRIMED,true),3);
		level.playSound(null,pos, SoundEvents.TNT_PRIMED, SoundSource.BLOCKS,1F,1F);
		level.scheduleTick(pos,this,40);
	}
	
	@Override
	public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRandom) {
		if(pState.getValue(PRIMED)){
			pLevel.removeBlock(pPos,false);
			pLevel.explode(null,pPos.getX()+0.5D,pPos.getY()+0.5D,pPos.getZ()+0.5D,3, Explosion.BlockInteraction.BREAK);
		}
	}
	
	@Override
	public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
		if(pLevel.hasNeighborSignal(pPos)){
			setPrimed(pLevel,pPos,pState);
		}
	}
	
	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		ItemStack itemstack = pPlayer.getItemInHand(pHand);
		if (pState.getValue(PRIMED)||(!itemstack.is(Items.FLINT_AND_STEEL) && !itemstack.is(Items.FIRE_CHARGE))) {
			return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
		} else {
			setPrimed(pLevel,pPos,pState);
			Item item = itemstack.getItem();
			if (!pPlayer.isCreative()) {
				if (itemstack.is(Items.FLINT_AND_STEEL)) {
					itemstack.hurtAndBreak(1, pPlayer, (p_57425_) -> {
						p_57425_.broadcastBreakEvent(pHand);
					});
				} else {
					itemstack.shrink(1);
				}
			}
			
			pPlayer.awardStat(Stats.ITEM_USED.get(item));
			return InteractionResult.sidedSuccess(pLevel.isClientSide);
		}
	}
	
	public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, Random pRand) {
		if(pState.getValue(PRIMED)) {
			Direction direction = pState.getValue(FACING);
			double d0 = (double) pPos.getX() + 0.5D;
			double d1 = (double) pPos.getY() + 0.85D;
			double d2 = (double) pPos.getZ() + 0.5D;
			double d3 = 0.22D;
			double d4 = 0.27D;
			Direction direction1 = direction.getOpposite();
			double d5 = 0.17D * direction1.getStepX();
			double d6 = 0.17D * direction1.getStepZ();
			if (direction1.getAxis().isHorizontal()) {
				d5 = 0.17D * direction1.getStepX();
				d6 = 0.17D * direction1.getStepZ();
				d1 +=0.22D;
			}
			if(direction==Direction.DOWN)
				d1=pPos.getY()+0.15D;
			pLevel.addParticle(ParticleTypes.SMOKE, d0 + d5, d1, d2 + d6, 0.0D, 0.0D, 0.0D);
			pLevel.addParticle(ParticleTypes.SMOKE, d0 + d5, d1, d2 + d6, 0.0D, 0.0D, 0.0D);
			pLevel.addParticle(ParticleTypes.SMOKE, d0 + d5, d1, d2 + d6, 0.0D, 0.0D, 0.0D);
			pLevel.addParticle(ParticleTypes.SMOKE, d0 + d5, d1, d2 + d6, 0.0D, 0.0D, 0.0D);
		}
	}
	
	public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
		Direction direction = pState.getValue(FACING);
		BlockPos blockpos = pPos.relative(direction.getOpposite());
		BlockState blockstate = pLevel.getBlockState(blockpos);
		return blockstate.isFaceSturdy(pLevel, blockpos, direction);
	}
	
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		BlockState blockstate = this.defaultBlockState();
		LevelReader levelreader = pContext.getLevel();
		BlockPos blockpos = pContext.getClickedPos();
		Direction[] adirection = pContext.getNearestLookingDirections();
		
		for(Direction direction : adirection) {
				Direction direction1 = direction.getOpposite();
				blockstate = blockstate.setValue(FACING, direction1);
				if (blockstate.canSurvive(levelreader, blockpos)) {
					return blockstate;
				}
		}
		
		return null;
	}
	
	public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
		return pFacing.getOpposite() == pState.getValue(FACING) && !pState.canSurvive(pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : pState;
	}
	
	@Override
	public boolean dropFromExplosion(Explosion pExplosion) {
		return false;
	}
	
	@Override
	public void wasExploded(Level pLevel, BlockPos pPos, Explosion pExplosion) {
		if(!pLevel.isClientSide)
			pLevel.explode(null,pPos.getX()+0.5D,pPos.getY()+0.5D,pPos.getZ()+0.5D,3, Explosion.BlockInteraction.BREAK);
	}
}
