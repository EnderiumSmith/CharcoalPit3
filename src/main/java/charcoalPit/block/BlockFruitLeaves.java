package charcoalPit.block;

import charcoalPit.core.MethodHelper;
import charcoalPit.core.ModBlockRegistry;
import charcoalPit.core.ModItemRegistry;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Containers;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Random;

import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.ItemLike;

public class BlockFruitLeaves extends Block {
	
	public static final IntegerProperty DISTANCE = BlockStateProperties.DISTANCE;
	public static final BooleanProperty PERSISTENT = BlockStateProperties.PERSISTENT;
	public static final IntegerProperty AGE=BlockStateProperties.AGE_7;
	
	public ItemLike fruit;
	public final float tick_chance;
	
	public BlockFruitLeaves(Properties properties,ItemLike fruit,float tick_rate) {
		super(properties);
		this.fruit=fruit;
		this.tick_chance=tick_rate;
		this.registerDefaultState(this.getStateDefinition().any().setValue(DISTANCE,7).setValue(PERSISTENT,false).setValue(AGE,0));
	}
	
	@Override
	public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return 30;
	}
	
	@Override
	public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return 60;
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		if(context instanceof EntityCollisionContext){
			if(((EntityCollisionContext)context).getEntity() instanceof ItemEntity){
				return Shapes.empty();
			}
		}
		return super.getCollisionShape(state,worldIn,pos,context);
	}
	
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 1;
	}
	
	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return true;
	}
	
	public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand) {
		worldIn.setBlock(pos, updateDistance(state, worldIn, pos), 3);
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, Random random) {
		if (!state.getValue(PERSISTENT) && state.getValue(DISTANCE) == 7) {
			dropResources(state, worldIn, pos);
			worldIn.removeBlock(pos, false);
		}
		if(random.nextFloat()<tick_chance&&!state.getValue(PERSISTENT)){
			int stage=state.getValue(AGE);
			boolean empty_leaves=false;
			for(BlockPos mutable:BlockPos.MutableBlockPos.betweenClosed(pos.below(2).north(2).west(2),pos.above(2).south(2).east(2))){
				if(worldIn.getBlockState(mutable).getBlock()==this&&!worldIn.getBlockState(mutable).getValue(PERSISTENT)){
					int stage2=worldIn.getBlockState(mutable).getValue(AGE);
					if(stage2==0)
						empty_leaves=true;
					if(stage2+1==stage||(stage==0&&stage2==7))
						return;
				}
			}
			if(stage<7)
				worldIn.setBlock(pos,state.setValue(AGE,stage+1),2);
			else{
				if(empty_leaves) {
					//if some leaves are harvested start rotting
					worldIn.setBlock(pos, state.setValue(AGE, 0), 2);
					if(random.nextFloat()<0.2F)
						Containers.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(fruit));
				}
			}
		}
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(DISTANCE,PERSISTENT,AGE);
	}
	
	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		if(state.getValue(AGE)==7&&!state.getValue(PERSISTENT)){
			ItemHandlerHelper.giveItemToPlayer(player,new ItemStack(fruit),player.getInventory().selected);
			worldIn.setBlock(pos,state.setValue(AGE,0),2);
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}
	
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		int i = getDistance(facingState) + 1;
		if (i != 1 || stateIn.getValue(DISTANCE) != i) {
			worldIn.scheduleTick(currentPos, this, 1);
		}
		
		return stateIn;
	}
	
	private static BlockState updateDistance(BlockState state, LevelAccessor worldIn, BlockPos pos) {
		int i = 7;
		BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();
		
		for(Direction direction : Direction.values()) {
			blockpos$mutable.setWithOffset(pos, direction);
			i = Math.min(i, getDistance(worldIn.getBlockState(blockpos$mutable)) + 1);
			if (i == 1) {
				break;
			}
		}
		
		return state.setValue(DISTANCE, Integer.valueOf(i));
	}
	
	private static int getDistance(BlockState neighbor) {
		if (MethodHelper.isBlockInTag(neighbor.getBlock(),BlockTags.LOGS)) {
			return 0;
		} else {
			return neighbor.getBlock() instanceof BlockFruitLeaves ? neighbor.getValue(DISTANCE) : 7;
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {
		if (worldIn.isRainingAt(pos.above())) {
			if (rand.nextInt(15) == 1) {
				BlockPos blockpos = pos.below();
				BlockState blockstate = worldIn.getBlockState(blockpos);
				if (!blockstate.canOcclude() || !blockstate.isFaceSturdy(worldIn, blockpos, Direction.UP)) {
					double d0 = (double)pos.getX() + rand.nextDouble();
					double d1 = (double)pos.getY() - 0.05D;
					double d2 = (double)pos.getZ() + rand.nextDouble();
					worldIn.addParticle(ParticleTypes.DRIPPING_WATER, d0, d1, d2, 0.0D, 0.0D, 0.0D);
				}
			}
		}
	}
	
	
	
	private int getAge(ItemStack item){
		if(item.hasTag()&&item.getTag().contains("stage")){
			return item.getTag().getInt("stage");
		}
		return 0;
		
	}
	
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return updateDistance(this.defaultBlockState().setValue(PERSISTENT, true).setValue(AGE,getAge(context.getItemInHand())), context.getLevel(), context.getClickedPos());
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if(group== ModItemRegistry.CHARCOAL_PIT){
			items.add(new ItemStack(this));
			ItemStack stack=new ItemStack(this);
			stack.getOrCreateTag().putInt("stage",3);
			items.add(stack);
			stack=new ItemStack(this);
			stack.getOrCreateTag().putInt("stage",6);
			items.add(stack);
			stack=new ItemStack(this);
			stack.getOrCreateTag().putInt("stage",7);
			items.add(stack);
		}
	}
}
