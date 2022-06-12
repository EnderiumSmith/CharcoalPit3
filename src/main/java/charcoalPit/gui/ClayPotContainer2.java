package charcoalPit.gui;

import charcoalPit.CharcoalPit;
import charcoalPit.core.ModContainerRegistry;
import charcoalPit.recipe.OreKilnRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ClayPotContainer2 extends AbstractContainerMenu{
	
	
	ClayPotHandler pot;
	Inventory inv;
	int slot;
	public ClayPotContainer2(int id, Inventory inv, int slot, Level world) {
		super(ModContainerRegistry.ClayPot, id);
		this.inv=inv;
		this.slot=slot;
		pot=new ClayPotHandler(9, ()->{
			this.inv.getItem(this.slot).addTagElement("inventory", pot.serializeNBT());
		}, inv.player.level);
		if(this.inv.getItem(this.slot).hasTag()&&
				this.inv.getItem(this.slot).getTag().contains("inventory"))
			pot.deserializeNBT(this.inv.getItem(this.slot).getTag().getCompound("inventory"));
		
		for(int i = 0; i < 3; ++i) {
	         for(int j = 0; j < 3; ++j) {
	        	 this.addSlot(new SlotItemHandler(pot, getIndex(j+i*3), 62 + j * 18, 17 + i * 18){
					 @Override
					 public void setChanged() {
						 super.setChanged();
						 inv.getItem(slot).addTagElement("inventory", pot.serializeNBT());
						 inv.getItem(slot).addTagElement("result",OreKilnRecipe.OreKilnGetOutput(pot.serializeNBT(),inv.player.level).serializeNBT());
						 inv.getItem(slot).getTag().putBoolean("empty",OreKilnRecipe.oreKilnIsEmpty(pot));
					 }
				 });
	         }
	      }
		
		for(int k = 0; k < 3; ++k) {
	         for(int i1 = 0; i1 < 9; ++i1) {
	            this.addSlot(new Slot(inv, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
	         }
	      }

	      for(int l = 0; l < 9; ++l) {
	         this.addSlot(new SlotLocked(inv, l, 8 + l * 18, 142, slot));
	      }
		
	}
	
	private int getIndex(int i) {
		if(i<4)
			return i+1;
		if(i==4)
			return 0;
		return i;
	}

	@Override
	public boolean stillValid(Player playerIn) {
		return true;
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
	
	public static class ClayPotHandler extends ItemStackHandler{
		Runnable function;
		Level world;
		
		public ClayPotHandler(int slots,Runnable r,Level world) {
			super(slots);
			function=r;
			this.world=world;
		}
		
		@Override
		public boolean isItemValid(int slot, ItemStack stack) {
			if(slot==0) {
				return false;
				//return stack.is(ItemTags.getAllTags().getTag(new ResourceLocation(CharcoalPit.MODID, "orekiln_fuels")));
			}else {
				return OreKilnRecipe.isValidInput(stack, world);
			}
		}
		
		@Override
		public int getSlotLimit(int slot) {
			if(slot==0)
				return 4;
			return 1;
		}
		
		@Override
		protected void onContentsChanged(int slot) {
			function.run();
		}
	}
	
	public static class SlotLocked extends Slot{

		int lock;
		public SlotLocked(Container inventoryIn, int index, int xPosition, int yPosition, int lock) {
			super(inventoryIn, index, xPosition, yPosition);
			this.lock=lock;
		}
		
		@Override
		public boolean mayPickup(Player playerIn) {
			return lock!=this.getSlotIndex();
		}
		
	}

}
