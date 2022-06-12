package charcoalPit.gui;

import charcoalPit.core.ModBlockRegistry;
import charcoalPit.core.ModContainerRegistry;
import charcoalPit.tile.TileDistillery;
import charcoalPit.tile.TileSteamPress;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class SteamPressContainer extends AbstractContainerMenu {
	
	public BlockPos pos;
	public ContainerData array;
	public ItemStackHandler fluid_tag;
	//server only
	public FluidTank tank1,tank2;
	
	
	public SteamPressContainer(int id, BlockPos pos, Inventory pInv){
		this(id,pos,pInv,new ItemStackHandler(2),new SimpleContainerData(4));
	}
	
	public SteamPressContainer(int id, BlockPos pos, Inventory pInv, TileSteamPress tile){
		this(id, pos, pInv, tile.inventory, new ContainerData() {
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
				return 0;
			}
			
			@Override
			public void set(int pIndex, int pValue) {
			
			}
			
			@Override
			public int getCount() {
				return 4;
			}
		});
		tank1=tile.water;
		tank2=tile.oil;
	}
	
	public SteamPressContainer(int id, BlockPos pos, Inventory pInv, ItemStackHandler handler, ContainerData data){
		super(ModContainerRegistry.SteamPress,id);
		this.pos=pos;
		
		this.addSlot(new SlotItemHandler(handler,0,65,35));
		this.addSlot(new SlotItemHandler(handler,1,91,53));
		
		for(int k = 0; k < 3; ++k) {
			for(int i1 = 0; i1 < 9; ++i1) {
				this.addSlot(new Slot(pInv, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
			}
		}
		
		for(int l = 0; l < 9; ++l) {
			this.addSlot(new Slot(pInv, l, 8 + l * 18, 142));
		}
		
		fluid_tag=new ItemStackHandler(1);
		fluid_tag.setStackInSlot(0,new ItemStack(Items.PAPER));
		fluid_tag.getStackInSlot(0).addTagElement("fluid1", FluidStack.EMPTY.writeToNBT(new CompoundTag()));
		fluid_tag.getStackInSlot(0).addTagElement("fluid2",FluidStack.EMPTY.writeToNBT(new CompoundTag()));
		
		this.addSlot(new SlotItemHandler(fluid_tag,0,0,0){
			@Override
			public boolean mayPlace(@NotNull ItemStack stack) {
				return false;
			}
			
			@Override
			public boolean mayPickup(Player playerIn) {
				return false;
			}
			
			@Override
			@OnlyIn(Dist.CLIENT)
			public boolean isActive() {
				return false;
			}
		});
		
		this.array=data;
		this.addDataSlots(array);
	}
	
	@Override
	public void broadcastChanges() {
		if(!tank1.getFluid().isFluidStackIdentical(FluidStack.loadFluidStackFromNBT(fluid_tag.getStackInSlot(0).getOrCreateTag().getCompound("fluid1")))){
			fluid_tag.getStackInSlot(0).getTag().put("fluid1",tank1.getFluid().writeToNBT(new CompoundTag()));
		}
		if(!tank2.getFluid().isFluidStackIdentical(FluidStack.loadFluidStackFromNBT(fluid_tag.getStackInSlot(0).getOrCreateTag().getCompound("fluid2")))){
			fluid_tag.getStackInSlot(0).getTag().put("fluid2",tank2.getFluid().writeToNBT(new CompoundTag()));
		}
		super.broadcastChanges();
	}
	
	
	@Override
	public boolean stillValid(Player playerIn) {
		return playerIn.level.getBlockState(pos).getBlock() == ModBlockRegistry.SteamPress&&
				playerIn.distanceToSqr((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D) <= 64.0D;
	}
	
	@Override
	public ItemStack quickMoveStack(Player playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (index < 2) {
				if (!this.moveItemStackTo(itemstack1, 2, 38, true)) {
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
