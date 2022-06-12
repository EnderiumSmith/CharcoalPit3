package charcoalPit.block;

import java.util.Arrays;
import java.util.List;

import charcoalPit.core.ModItemRegistry;
import charcoalPit.fluid.ModFluidRegistry;
import charcoalPit.gui.BarrelContainer;
import charcoalPit.tile.TileBarrel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

public class BlockBarrel extends Block implements SimpleWaterloggedBlock, EntityBlock {
	
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	
	public static final VoxelShape BARREL=Shapes.box(2D/16D, 0D, 2D/16D, 14D/16D, 1D, 14D/16D);

	public BlockBarrel() {
		super(Properties.of(Material.WOOD).strength(2, 3));
		registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED,false));
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.WATERLOGGED);
	}
	
	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		return this.defaultBlockState().setValue(WATERLOGGED,fluidstate.getType()==Fluids.WATER);
		
	}
	
	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.getValue(WATERLOGGED)) {
			worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
		}
		return stateIn;
	}
	
	@Override
	public boolean useShapeForLightOcclusion(BlockState state) {
		return true;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return BARREL;
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TileBarrel(pos,state);
	}
	
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level plevel, BlockState pstate, BlockEntityType<T> ptype) {
		return plevel.isClientSide()?null:(level,blockpos,blockstate,t)-> {
			if (t instanceof TileBarrel tile) {
				tile.tick();
			}
		};
	}
	
	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.hasBlockEntity() && (state.getBlock() != newState.getBlock() || !newState.hasBlockEntity())) {
			((TileBarrel)worldIn.getBlockEntity(pos)).dropInventory();
			worldIn.removeBlockEntity(pos);
	    }
	}
	
	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player,
			InteractionHand handIn, BlockHitResult hit) {
		if(worldIn.isClientSide)
			return InteractionResult.SUCCESS;
		if(player.getItemInHand(handIn).getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent()) {
			if(FluidUtil.interactWithFluidHandler(player, handIn, worldIn, pos, null))
					return InteractionResult.SUCCESS;
		}else if(player.getItemInHand(handIn).getItem()==Items.GLASS_BOTTLE){
			TileBarrel tile=((TileBarrel)worldIn.getBlockEntity(pos));
			if(tile.tank.getFluid().getFluid()==ModFluidRegistry.AlcoholStill&&tile.tank.getFluidAmount()>=250){
				player.getItemInHand(handIn).shrink(1);
				ItemStack stack=new ItemStack(ModItemRegistry.AlcoholBottle);
				stack.setTag(tile.tank.getFluid().getTag().copy());
				tile.tank.drain(250, IFluidHandler.FluidAction.EXECUTE);
				ItemHandlerHelper.giveItemToPlayer(player,stack);
				return InteractionResult.SUCCESS;
			}
			if(tile.tank.getFluid().getFluid()==ModFluidRegistry.VinegarStill&&tile.tank.getFluidAmount()>=250){
				player.getItemInHand(handIn).shrink(1);
				ItemStack stack=new ItemStack(ModItemRegistry.VinegarBottle);
				tile.tank.drain(250, IFluidHandler.FluidAction.EXECUTE);
				ItemHandlerHelper.giveItemToPlayer(player,stack);
				return InteractionResult.SUCCESS;
			}
			if(tile.tank.getFluid().getFluid()==ModFluidRegistry.EthanolStill&&tile.tank.getFluidAmount()>=250){
				player.getItemInHand(handIn).shrink(1);
				ItemStack stack=new ItemStack(ModItemRegistry.EthanolBottle);
				tile.tank.drain(250, IFluidHandler.FluidAction.EXECUTE);
				ItemHandlerHelper.giveItemToPlayer(player,stack);
				return InteractionResult.SUCCESS;
			}
			if(tile.tank.getFluid().getFluid()==ModFluidRegistry.EthoxideStill&&tile.tank.getFluidAmount()>=250){
				player.getItemInHand(handIn).shrink(1);
				ItemStack stack=new ItemStack(ModItemRegistry.EthoxideBottle);
				tile.tank.drain(250, IFluidHandler.FluidAction.EXECUTE);
				ItemHandlerHelper.giveItemToPlayer(player,stack);
				return InteractionResult.SUCCESS;
			}
		}
		NetworkHooks.openGui((ServerPlayer)player, new MenuProvider() {
			
			@Override
			public AbstractContainerMenu createMenu(int arg0, Inventory arg1, Player arg2) {
				return new BarrelContainer(arg0, pos, arg1);
			}
			
			@Override
			public Component getDisplayName() {
				return new TranslatableComponent("screen.charcoal_pit.barrel");
			}
		}, pos);
		return InteractionResult.SUCCESS;
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		TileBarrel tile=((TileBarrel)builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY));
		ItemStack stack=new ItemStack(this);
		if(tile.tank.getFluidAmount()>0)
			stack.addTagElement("Fluid", tile.tank.writeToNBT(new CompoundTag()));
		return Arrays.asList(stack);
	}
	
	@Override
	public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if(stack.hasTag()&&stack.getTag().contains("Fluid")) {
			((TileBarrel)worldIn.getBlockEntity(pos)).tank.readFromNBT(stack.getTag().getCompound("Fluid"));
		}
		super.setPlacedBy(worldIn, pos, state, placer, stack);
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, BlockGetter worldIn, List<Component> tooltip,
			TooltipFlag flagIn) {
		if(stack.hasTag()&&stack.getTag().contains("Fluid")){
			FluidStack fluid=FluidStack.loadFluidStackFromNBT(stack.getTag().getCompound("Fluid"));
			tooltip.add(fluid.getDisplayName().plainCopy().append(new TextComponent(":"+fluid.getAmount())));
		}
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	}
	
	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}
	
	@Override
	public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
		int a=((TileBarrel)worldIn.getBlockEntity(pos)).tank.getFluidAmount();
		a+=999;
		a/=1000;
		if(a>8)
			a--;
		return a;
	}



}
