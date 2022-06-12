package charcoalPit.gui;

import charcoalPit.core.ModBlockRegistry;
import charcoalPit.core.ModContainerRegistry;
import charcoalPit.recipe.BarrelRecipe;
import charcoalPit.tile.TileBarrel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.core.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class BarrelContainer extends AbstractContainerMenu{

	public BlockPos pos;
	public ItemStackHandler fluid_tag;
	public FluidStack fluid;
	public IFluidHandler tank;
	public ContainerData array;
	public BarrelContainer(int id, BlockPos pos, Inventory inv) {
		super(ModContainerRegistry.Barrel, id);
		this.pos=pos;
		TileBarrel tile=((TileBarrel)inv.player.level.getBlockEntity(pos));
		
		tank=tile.tank;
		fluid_tag=new ItemStackHandler();
		fluid_tag.setStackInSlot(0, new ItemStack(Items.PAPER));
		fluid=tank.getFluidInTank(0).copy();
		fluid_tag.getStackInSlot(0).addTagElement("fluid", fluid.writeToNBT(new CompoundTag()));
		
		this.addSlot(new SlotItemHandler(tile.input, 0, 98, 17) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent()||
						BarrelRecipe.isValidItem(stack, tile.getLevel())||stack.getItem()==Items.GLASS_BOTTLE;
			}
			
			@Override
			public void setChanged() {
				super.setChanged();
				tile.setChanged();
				tile.valid=false;
			}
		});
		this.addSlot(new SlotItemHandler(tile.output, 0, 98, 53) {
			@Override
			public boolean mayPlace(ItemStack stack) {
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
	      
	      this.addSlot(new SlotItemHandler(fluid_tag, 0, 0, 0) {
	    	  @Override
	    	public boolean mayPlace(ItemStack stack) {
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
	      array=new ContainerData() {
			
			@Override
			public int getCount() {
				return 2;
			}
			
			@Override
			public void set(int arg0, int arg1) {
				if(arg0==0)
					tile.process=arg1;
				else
					tile.total=arg1;
				
			}
			
			@Override
			public int get(int arg0) {
				if(arg0==0)
					return tile.process;
				else
					return tile.total;
			}
		};
		this.addDataSlots(array);
	}
	
	@Override
	public void broadcastChanges() {
		if(!tank.getFluidInTank(0).isFluidStackIdentical(fluid)) {
			fluid=tank.getFluidInTank(0).copy();
			fluid_tag.getStackInSlot(0).addTagElement("fluid", fluid.writeToNBT(new CompoundTag()));
		}
		super.broadcastChanges();
	}

	@Override
	public boolean stillValid(Player playerIn) {
		return playerIn.level.getBlockState(pos).getBlock() == ModBlockRegistry.Barrel&&
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
	            if (!this.moveItemStackTo(itemstack1, 2, 37, true)) {
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
