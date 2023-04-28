package charcoalPit.block;

import charcoalPit.tile.TileBarrel;
import charcoalPit.tile.TileXPCrystal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.lwjgl.system.CallbackI;

import javax.annotation.Nullable;

public class BlockXPCrystal extends AmethystBlock implements EntityBlock {
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	public static final IntegerProperty SIZE= IntegerProperty.create("size",0,3);
	public VoxelShape[][] shapes=new VoxelShape[4][6];
	
	public BlockXPCrystal(Properties p_151999_) {
		super(p_151999_);
		registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED,false).setValue(FACING, Direction.UP).setValue(SIZE,0));
		assembleHitbox(0,3,4);
		assembleHitbox(1,4,3);
		assembleHitbox(2,5,3);
		assembleHitbox(3,7,3);
	}
	
	public void assembleHitbox(int a,int b,int c){
		this.shapes[a][4] = Block.box((double)c, 0.0D, (double)c, (double)(16 - c), (double)b, (double)(16 - c));
		this.shapes[a][5] = Block.box((double)c, (double)(16 - b), (double)c, (double)(16 - c), 16.0D, (double)(16 - c));
		this.shapes[a][0] = Block.box((double)c, (double)c, (double)(16 - b), (double)(16 - c), (double)(16 - c), 16.0D);
		this.shapes[a][1] = Block.box((double)c, (double)c, 0.0D, (double)(16 - c), (double)(16 - c), (double)b);
		this.shapes[a][2] = Block.box(0.0D, (double)c, (double)c, (double)b, (double)(16 - c), (double)(16 - c));
		this.shapes[a][3] = Block.box((double)(16 - b), (double)c, (double)c, 16.0D, (double)(16 - c), (double)(16 - c));
	}
	
	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		int size=pState.getValue(SIZE);
		switch (pState.getValue(FACING)){
			case NORTH -> {return shapes[size][0];
			}
			case SOUTH -> {return shapes[size][1];
			}
			case EAST -> {return shapes[size][2];
			}
			case WEST -> {return shapes[size][3];
			}
			case UP -> {return shapes[size][4];
			}
			case DOWN -> {return shapes[size][5];
			}
		}
		return shapes[0][0];
	}
	
	@Override
	public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
		int size=state.getValue(SIZE);
		if(size==0)
			return 1;
		if(size==1)
			return 2;
		if(size==2)
			return 4;
		return 8;
	}
	
	public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
		Direction direction = pState.getValue(FACING);
		BlockPos blockpos = pPos.relative(direction.getOpposite());
		return pLevel.getBlockState(blockpos).isFaceSturdy(pLevel, blockpos, direction);
	}
	
	public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
		if (pState.getValue(WATERLOGGED)) {
			pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
		}
		
		return pDirection == pState.getValue(FACING).getOpposite() && !pState.canSurvive(pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(pState, pDirection, pNeighborState, pLevel, pCurrentPos, pNeighborPos);
	}
	
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		LevelAccessor levelaccessor = pContext.getLevel();
		BlockPos blockpos = pContext.getClickedPos();
		return this.defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf(levelaccessor.getFluidState(blockpos).getType() == Fluids.WATER)).setValue(FACING, pContext.getClickedFace());
	}
	
	public FluidState getFluidState(BlockState pState) {
		return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
	}
	
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(WATERLOGGED, FACING,SIZE);
	}
	
	public PushReaction getPistonPushReaction(BlockState pState) {
		return PushReaction.DESTROY;
	}
	
	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.hasBlockEntity() && (!state.is(newState.getBlock()) || !newState.hasBlockEntity())) {
			((TileXPCrystal)worldIn.getBlockEntity(pos)).dropInventory();
			worldIn.removeBlockEntity(pos);
		}
	}
	
	@Override
	public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @org.jetbrains.annotations.Nullable LivingEntity pPlacer, ItemStack stack) {
		if(stack.hasTag()&&stack.getTag().contains("XP")) {
			((TileXPCrystal)pLevel.getBlockEntity(pPos)).XP=stack.getTag().getInt("XP");
		}
		super.setPlacedBy(pLevel, pPos, pState, pPlacer, stack);
	}
	
	@Override
	public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
		if(!pLevel.isClientSide()&&pEntity instanceof Player player){
			TileXPCrystal tile=(TileXPCrystal)pLevel.getBlockEntity(pPos);
			int cap=TileXPCrystal.MAX_XP- tile.XP;
			if(cap>0){
				if(player.totalExperience==0){
					if(player.experienceLevel>0) {
						player.giveExperienceLevels(-1);
						int player_xp = player.getXpNeededForNextLevel();
						int xp_to_transfer = Math.min(7, player_xp);
						xp_to_transfer = Math.min(xp_to_transfer, cap);
						tile.XP += xp_to_transfer;
						player_xp -= xp_to_transfer;
						player.giveExperiencePoints(player_xp);
					}
				}else{
					int xp_to_transfer=Math.min(7,player.totalExperience);
					xp_to_transfer=Math.min(xp_to_transfer,cap);
					tile.XP+=xp_to_transfer;
					player.giveExperiencePoints(-xp_to_transfer);
				}
			}
		}
	}
	
	@org.jetbrains.annotations.Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new TileXPCrystal(pPos,pState);
	}
	
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level plevel, BlockState pstate, BlockEntityType<T> ptype) {
		return plevel.isClientSide()?null:(level,blockpos,blockstate,t)-> {
			if (t instanceof TileXPCrystal tile) {
				tile.tick();
			}
		};
	}
}
