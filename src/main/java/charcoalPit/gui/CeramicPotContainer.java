package charcoalPit.gui;

import charcoalPit.block.BlockCeramicPot;
import charcoalPit.core.ModContainerRegistry;
import charcoalPit.tile.TileCeramicPot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraftforge.items.SlotItemHandler;

public class CeramicPotContainer extends AbstractContainerMenu{

	public BlockPos pos;
	public CeramicPotContainer(int id, BlockPos pos, Inventory inv) {
		super(ModContainerRegistry.CeramicPot, id);
		this.pos=pos;
		TileCeramicPot tile=((TileCeramicPot)inv.player.level.getBlockEntity(pos));
		
		for(int i = 0; i < 3; ++i) {
	         for(int j = 0; j < 3; ++j) {
	        	 this.addSlot(new SlotItemHandler(tile.inventory, j + i * 3, 62 + j * 18, 17 + i * 18));
	         }
	      }
		
		for(int k = 0; k < 3; ++k) {
	         for(int i1 = 0; i1 < 9; ++i1) {
	            this.addSlot(new Slot(inv, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
	         }
	      }

	      for(int l = 0; l < 9; ++l) {
	         this.addSlot(new Slot(inv, l, 8 + l * 18, 142));
	      }
	}

	@Override
	public boolean stillValid(Player playerIn) {
		return playerIn.level.getBlockState(pos).getBlock() instanceof BlockCeramicPot&&
				playerIn.distanceToSqr((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D) <= 64.0D;
	}
	
	@Override
	public ItemStack quickMoveStack(Player playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
	      Slot slot = this.slots.get(index);
	      if (slot != null && slot.hasItem()) {
	         ItemStack itemstack1 = slot.getItem();
	         itemstack = itemstack1.copy();
	         if (index < 9) {
	            if (!this.moveItemStackTo(itemstack1, 9, 45, true)) {
	               return ItemStack.EMPTY;
	            }
	         } else if (!this.moveItemStackTo(itemstack1, 0, 9, false)) {
	            return ItemStack.EMPTY;
	         }

	         if (itemstack1.isEmpty()) {
	            slot.set(ItemStack.EMPTY);
	         } else {
	            slot.setChanged();
	         }

	         if (itemstack1.getCount() == itemstack.getCount()) {
	            return ItemStack.EMPTY;
	         }

	         slot.onTake(playerIn, itemstack1);
	      }

	      return itemstack;
	}
	
	

}
