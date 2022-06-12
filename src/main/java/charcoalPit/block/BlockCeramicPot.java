package charcoalPit.block;

import java.util.Arrays;
import java.util.List;

import charcoalPit.core.MethodHelper;
import charcoalPit.gui.CeramicPotContainer;
import charcoalPit.tile.TileBarrel;
import charcoalPit.tile.TileCeramicPot;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraftforge.network.NetworkHooks;

public class BlockCeramicPot extends Block implements EntityBlock {
	
	public static final VoxelShape POT=Shapes.box(2D/16D, 0D, 2D/16D, 14D/16D, 1D, 14D/16D);

	public BlockCeramicPot(MaterialColor color) {
		super(Properties.of(Material.SHULKER_SHELL, color).strength(1.25F,4.2F).sound(SoundType.STONE));
	}
	
	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player,
			InteractionHand handIn, BlockHitResult hit) {
		if(!worldIn.isClientSide) {
			NetworkHooks.openGui((ServerPlayer)player, new MenuProvider() {
				
				@Override
				public AbstractContainerMenu createMenu(int p_createMenu_1_, Inventory p_createMenu_2_, Player p_createMenu_3_) {
					return new CeramicPotContainer(p_createMenu_1_, pos, p_createMenu_2_);
				}
				
				@Override
				public Component getDisplayName() {
					return new TranslatableComponent("screen.charcoal_pit.ceramic_pot");
				}
			}, pos);;
		}
		return InteractionResult.SUCCESS;
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		TileCeramicPot tile=((TileCeramicPot)builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY));
		ItemStack stack=new ItemStack(this);
		stack.addTagElement("inventory", tile.inventory.serializeNBT());
		return Arrays.asList(stack);
	}
	
	@Override
	public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if(stack.hasTag()&&stack.getTag().contains("inventory")) {
			((TileCeramicPot)worldIn.getBlockEntity(pos)).inventory.deserializeNBT(stack.getTag().getCompound("inventory"));
		}
		super.setPlacedBy(worldIn, pos, state, placer, stack);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		worldIn.updateNeighbourForOutputSignal(pos, state.getBlock());
		super.onRemove(state, worldIn, pos, newState, isMoving);
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, BlockGetter worldIn, List<Component> tooltip,
			TooltipFlag flagIn) {
		if(stack.hasTag()&&stack.getTag().contains("inventory")) {
			CompoundTag compoundnbt = stack.getTag().getCompound("inventory");
		    ItemStackHandler items=new ItemStackHandler(9);
		    items.deserializeNBT(compoundnbt);
		    for(int k=0;k<9;k++) {
		    	ItemStack itemstack=items.getStackInSlot(k);
		    	if(!itemstack.isEmpty()) {
		    		MutableComponent iformattabletextcomponent = itemstack.getHoverName().plainCopy();
	                iformattabletextcomponent.append(" x").append(String.valueOf(itemstack.getCount()));
	                tooltip.add(iformattabletextcomponent);
		    	}
		    }
	    }
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	}
	
	@Override
	public PushReaction getPistonPushReaction(BlockState state) {
		return PushReaction.DESTROY;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return POT;
	}
	
	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}
	
	@Override
	public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
		return MethodHelper.calcRedstoneFromInventory(((TileCeramicPot)worldIn.getBlockEntity(pos)).inventory);
	}
	
	@Override
	public ItemStack getCloneItemStack(BlockGetter worldIn, BlockPos pos, BlockState state) {
		TileCeramicPot tile=((TileCeramicPot)worldIn.getBlockEntity(pos));
		ItemStack stack=new ItemStack(this);
		stack.addTagElement("inventory", tile.inventory.serializeNBT());
		return stack;
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TileCeramicPot(pos,state);
	}

}
