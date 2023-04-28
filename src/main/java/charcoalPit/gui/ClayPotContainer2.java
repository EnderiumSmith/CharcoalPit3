package charcoalPit.gui;

import charcoalPit.CharcoalPit;
import charcoalPit.core.ModContainerRegistry;
import charcoalPit.recipe.OreKilnRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ClayPotContainer2 extends AbstractContainerMenu{
	
	
	ClayPotHandler pot;
	Inventory inv;
	int slot;
	public ClayPotContainer2(int id, Inventory inv, int slot, Level world) {
		super(ModContainerRegistry.ClayPot, id);
		this.inv=inv;
		this.slot=slot;
		pot=new ClayPotHandler(4, ()->{
			this.inv.getItem(this.slot).addTagElement("inventory", pot.serializeNBT());
		}, inv.player.level);
		if(this.inv.getItem(this.slot).hasTag()&&
				this.inv.getItem(this.slot).getTag().contains("inventory"))
			pot.deserializeNBT(this.inv.getItem(this.slot).getTag().getCompound("inventory"));
		
		for(int i = 0; i < 2; ++i) {
	         for(int j = 0; j < 2; ++j) {
	        	 this.addSlot(new SlotItemHandler(pot, j+i*2, 71 + j * 18, 26 + i * 18){
					 @Override
					 public void setChanged() {
						 super.setChanged();
						 if(!OreKilnRecipe.oreKilnIsEmpty(pot)) {
							 inv.getItem(slot).addTagElement("inventory", pot.serializeNBT());
							 inv.getItem(slot).addTagElement("result", OreKilnRecipe.OreKilnGetOutput(pot.serializeNBT(), inv.player.level).serializeNBT());
						 }else{
							 inv.getItem(slot).removeTagKey("inventory");
							 inv.getItem(slot).removeTagKey("result");
						 }
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
	         if (index < 4) {
	            if (!this.moveItemStackTo(itemstack1, 4, 40, true)) {
	               return ItemStack.EMPTY;
	            }
	         } else if (!this.moveItemStackTo(itemstack1, 0, 4, false)) {
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
			return OreKilnRecipe.isValidInput(stack, world);
		}
		
		@Override
		public int getSlotLimit(int slot) {
			return 1;
		}
		
		@Override
		protected void onContentsChanged(int slot) {
			function.run();
		}
		
		@Override
		public CompoundTag serializeNBT() {
			ListTag nbtTagList = new ListTag();
			List<ItemStack> newList=stacks.stream().sorted(Comparator.comparing(a -> a.getItem().getRegistryName())).toList();
			for (int i = 0; i < newList.size(); i++)
			{
				if (!newList.get(i).isEmpty())
				{
					CompoundTag itemTag = new CompoundTag();
					itemTag.putInt("Slot", i);
					newList.get(i).save(itemTag);
					nbtTagList.add(itemTag);
				}
			}
			CompoundTag nbt = new CompoundTag();
			nbt.put("Items", nbtTagList);
			nbt.putInt("Size", newList.size());
			return nbt;
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
