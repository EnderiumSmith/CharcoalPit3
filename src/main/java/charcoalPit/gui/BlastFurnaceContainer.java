package charcoalPit.gui;

import charcoalPit.core.ModBlockRegistry;
import charcoalPit.core.ModContainerRegistry;
import charcoalPit.core.ModItemRegistry;
import charcoalPit.tile.TileBlastFurnace;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class BlastFurnaceContainer extends AbstractContainerMenu {
	
	public BlockPos pos;
	public ContainerData array;
	
	public BlastFurnaceContainer(int id, BlockPos pos, Inventory inv){
		this(id,pos,inv,new ItemStackHandler(4){
			@Override
			public boolean isItemValid(int slot, @NotNull ItemStack stack) {
				if(slot== TileBlastFurnace.FLUX){
					return stack.getItem()== ModItemRegistry.Flux;
				}
				if(slot== TileBlastFurnace.ORE){
					return TileBlastFurnace.isOre(stack);
				}
				if(slot== TileBlastFurnace.FUEL){
					return TileBlastFurnace.isFuel(stack);
				}
				return super.isItemValid(slot, stack);
			}
		},new SimpleContainerData(5));
	}
	
	public BlastFurnaceContainer(int id, BlockPos pos, Inventory inv, TileBlastFurnace tile){
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
	
	public BlastFurnaceContainer(int id, BlockPos pos, Inventory inv, ItemStackHandler machine, ContainerData array){
		super(ModContainerRegistry.BlastFurnace,id);
		this.pos=pos;
		
		this.addSlot(new SlotItemHandler(machine,TileBlastFurnace.FLUX,47,17));
		this.addSlot(new SlotItemHandler(machine,TileBlastFurnace.ORE,65,17));
		this.addSlot(new SlotItemHandler(machine,TileBlastFurnace.FUEL,56,53));
		this.addSlot(new SlotItemHandler(machine,TileBlastFurnace.OUT,116,35){
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
		return playerIn.level.getBlockState(pos).getBlock() == ModBlockRegistry.BlastFurnace&&
				playerIn.distanceToSqr((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D) <= 64.0D;
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
			} else if (!this.moveItemStackTo(itemstack1, 0, 3, false)) {
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
