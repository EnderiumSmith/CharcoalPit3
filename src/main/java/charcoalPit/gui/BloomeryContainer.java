package charcoalPit.gui;

import charcoalPit.core.ModBlockRegistry;
import charcoalPit.core.ModContainerRegistry;
import charcoalPit.recipe.BloomingRecipe;
import charcoalPit.tile.TileBloomeryy;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class BloomeryContainer extends AbstractContainerMenu {
	
	public BlockPos pos;
	public ContainerData array;
	
	public BloomeryContainer(int id, BlockPos pos, Inventory inv){
		this(id,pos,inv,new ItemStackHandler(3){
			@Override
			public boolean isItemValid(int slot, @NotNull ItemStack stack) {
				if(slot==0)
					return BloomingRecipe.getRecipe(stack, Minecraft.getInstance().level)!=null;
				if(slot==1)
					return TileBloomeryy.isFuel(stack);
				return super.isItemValid(slot,stack);
			}
		},new SimpleContainerData(5));
	}
	
	public BloomeryContainer(int id, BlockPos pos, Inventory inv, TileBloomeryy tile){
		this(id,pos,inv,tile.inventory,new ContainerData() {
			@Override
			public int get(int pIndex) {
				if(pIndex==0)
					return tile.burnTime;
				if(pIndex==1)
					return tile.burnTotal;
				if(pIndex==2)
					return tile.progress;
				if(pIndex==3)
					return tile.processTotal;
				if(pIndex==4)
					return tile.blastAir;
				return 0;
			}
			
			@Override
			public void set(int pIndex, int pValue) {
			
			}
			
			@Override
			public int getCount() {
				return 5;
			}
		});
	}
	
	public BloomeryContainer(int id, BlockPos pos, Inventory inv, ItemStackHandler machine, ContainerData array){
		super(ModContainerRegistry.Bloomery,id);
		this.pos=pos;
		
		this.addSlot(new SlotItemHandler(machine,0,56,17));
		this.addSlot(new SlotItemHandler(machine,1,56,53));
		this.addSlot(new SlotItemHandler(machine,2,116,35){
			@Override
			public boolean mayPlace(@NotNull ItemStack stack) {
				return false;
			}
		});
		
		for(int k = 0; k < 3; ++k) {
			for(int i1 = 0; i1 < 9; ++i1) {
				this.addSlot(new Slot(inv, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
			}
		}
		
		for(int l = 0; l < 9; ++l) {
			this.addSlot(new Slot(inv, l, 8 + l * 18, 142));
		}
		
		this.array=array;
		this.addDataSlots(array);
	}
	
	
	@Override
	public boolean stillValid(Player playerIn) {
		return playerIn.level.getBlockState(pos).getBlock() == ModBlockRegistry.Bloomeryy&&
				playerIn.distanceToSqr((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D) <= 64.0D;
	}
	
	@Override
	public ItemStack quickMoveStack(Player playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (index < 3) {
				if (!this.moveItemStackTo(itemstack1, 3, 39, true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemstack1, 0, 2, false)) {
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
