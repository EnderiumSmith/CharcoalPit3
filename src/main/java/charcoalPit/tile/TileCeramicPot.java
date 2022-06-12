package charcoalPit.tile;

import charcoalPit.block.BlockCeramicPot;
import charcoalPit.core.ModTileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileCeramicPot extends BlockEntity{
	
	public CeramicPotHandler inventory;
	static Capability<IItemHandler> ITEM_CAP = CapabilityManager.get(new CapabilityToken<>(){});
	public LazyOptional<IItemHandler> out;
	
	public TileCeramicPot(BlockPos pos, BlockState state) {
		super(ModTileRegistry.CeramicPot,pos,state);
		inventory=new CeramicPotHandler(9,()->{
			setChanged();
		});
		out=LazyOptional.of(()->inventory);
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if(cap.equals(ITEM_CAP)) {
			return out.cast();
		}
		return super.getCapability(cap, side);
	}
	
	@Override
	protected void saveAdditional(CompoundTag compound) {
		compound.put("inventory", inventory.serializeNBT());
	}
	
	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		inventory.deserializeNBT(nbt.getCompound("inventory"));
	}
	
	public static class CeramicPotHandler extends ItemStackHandler{
		Runnable function;
		public CeramicPotHandler(int i,Runnable r) {
			super(i);
			function=r;
		}
		@Override
		public boolean isItemValid(int slot, ItemStack stack) {
			return !(Block.byItem(stack.getItem()) instanceof BlockCeramicPot||Block.byItem(stack.getItem()) instanceof ShulkerBoxBlock);
		}
		@Override
		protected void onContentsChanged(int slot) {
			function.run();
		}
	}

}
