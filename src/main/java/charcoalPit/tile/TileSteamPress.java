package charcoalPit.tile;

import charcoalPit.block.BlockDistillery;
import charcoalPit.block.BlockSteamPress;
import charcoalPit.core.ModTileRegistry;
import charcoalPit.recipe.SquisherRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TileSteamPress extends BlockEntity {
	
	public FluidTank water,oil;
	public ItemStackHandler inventory;
	public int progress,processTotal,burnTime,burnTotal;
	
	public TileSteamPress(BlockPos pWorldPosition, BlockState pBlockState) {
		super(ModTileRegistry.SteamPress, pWorldPosition, pBlockState);
		water=new FluidTank(2000,fluid->fluid.getFluid() == Fluids.WATER){
			@Override
			protected void onContentsChanged() {
				setChanged();
			}
		};
		oil=new FluidTank(8000){
			@Override
			protected void onContentsChanged() {
				setChanged();
			}
		};
		inventory=new ItemStackHandler(2){
			@Override
			protected void onContentsChanged(int slot) {
				setChanged();
			}
			
			@Override
			public boolean isItemValid(int slot, @NotNull ItemStack stack) {
				if(slot==0){
					return SquisherRecipe.getRecipe(stack,level)!=null;
				}
				if(slot==1){
					return ForgeHooks.getBurnTime(stack, RecipeType.SMELTING)>0;
				}
				return super.isItemValid(slot, stack);
			}
		};
		progress=0;
		processTotal=0;
		burnTime=0;
		burnTotal=0;
	}
	
	public void tick(){
		if(processTotal>0){
			if(progress<processTotal){
				if(burnTime>0)
					progress++;
				else if(progress>0)
					progress--;
			}
			if(progress>=processTotal){
				//done
				SquisherRecipe recipe=SquisherRecipe.getRecipe(inventory.getStackInSlot(0),level);
				if(recipe!=null){
					oil.fill(new FluidStack(recipe.output.getFluid(),recipe.output.amount,recipe.output.nbt), IFluidHandler.FluidAction.EXECUTE);
					inventory.extractItem(0, 1,false);
					water.drain(2, IFluidHandler.FluidAction.EXECUTE);
				}
				processTotal=0;
				progress=0;
				setChanged();
			}
		}
		if(processTotal<=0){
			if(!inventory.getStackInSlot(0).isEmpty()&&water.getFluidAmount()>=2){
				SquisherRecipe recipe=SquisherRecipe.getRecipe(inventory.getStackInSlot(0),level);
				if(recipe!=null){
					if(oil.fill(new FluidStack(recipe.output.getFluid(),recipe.output.amount,recipe.output.nbt), IFluidHandler.FluidAction.SIMULATE)==recipe.output.amount){
						progress=0;
						processTotal=100;
						setChanged();
					}
				}
			}
		}
		if(burnTime>0){
			burnTime--;
			if(burnTime%20==0){
				setChanged();
			}
		}
		if(processTotal>0&&burnTime<=0){
			int f=ForgeHooks.getBurnTime(inventory.getStackInSlot(1),RecipeType.SMELTING);
			if(f>0){
				ItemStack container=inventory.getStackInSlot(1).getContainerItem().copy();
				burnTime=f;
				burnTotal=f;
				inventory.extractItem(1,1,false);
				if(!container.isEmpty()){
					if(!inventory.getStackInSlot(1).isEmpty()){
						Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), container);
					}else{
						inventory.setStackInSlot(1,container);
					}
				}
				setActive(true);
			}
		}
		if(burnTime==0){
			setActive(false);
			burnTime--;
		}
	}
	
	public void dropInventory(){
		Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), inventory.getStackInSlot(0));
		Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), inventory.getStackInSlot(1));
	}
	
	public void setActive(boolean active){
		if(getBlockState().getValue(BlockSteamPress.ACTIVE)!=active)
			level.setBlock(worldPosition,getBlockState().setValue(BlockSteamPress.ACTIVE,active),3);
	}
	
	static Capability<IItemHandler> ITEM = CapabilityManager.get(new CapabilityToken<>(){});
	public LazyOptional<IItemHandler> item=LazyOptional.of(()->new IItemHandler() {
		@Override
		public int getSlots() {
			return 2;
		}
		
		@NotNull
		@Override
		public ItemStack getStackInSlot(int slot) {
			return inventory.getStackInSlot(slot);
		}
		
		@NotNull
		@Override
		public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
			return inventory.insertItem(slot,stack,simulate);
		}
		
		@NotNull
		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			return ItemStack.EMPTY;
		}
		
		@Override
		public int getSlotLimit(int slot) {
			return inventory.getSlotLimit(slot);
		}
		
		@Override
		public boolean isItemValid(int slot, @NotNull ItemStack stack) {
			return inventory.isItemValid(slot,stack);
		}
	});
	
	static Capability<IFluidHandler> FLUID = CapabilityManager.get(new CapabilityToken<>(){});
	public LazyOptional<IFluidHandler> fluid=LazyOptional.of(()->new IFluidHandler() {
		@Override
		public int getTanks() {
			return 2;
		}
		
		@NotNull
		@Override
		public FluidStack getFluidInTank(int tank) {
			if(tank==0)
				return water.getFluid();
			return oil.getFluid();
		}
		
		@Override
		public int getTankCapacity(int tank) {
			if(tank==0)
				return 1000;
			return oil.getCapacity();
		}
		
		@Override
		public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
			if(tank==0)
				return water.isFluidValid(stack);
			return false;
		}
		
		@Override
		public int fill(FluidStack resource, FluidAction action) {
			return water.fill(resource,action);
		}
		
		@NotNull
		@Override
		public FluidStack drain(FluidStack resource, FluidAction action) {
			return oil.drain(resource,action);
		}
		
		@NotNull
		@Override
		public FluidStack drain(int maxDrain, FluidAction action) {
			return oil.drain(maxDrain,action);
		}
	});
	
	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if(cap==ITEM)
			return item.cast();
		if(cap==FLUID)
			return fluid.cast();
		return super.getCapability(cap, side);
	}
	
	@Override
	public void setRemoved() {
		super.setRemoved();
		item.invalidate();
		fluid.invalidate();
	}
	
	@Override
	protected void saveAdditional(CompoundTag pTag) {
		super.saveAdditional(pTag);
		pTag.put("water",water.writeToNBT(new CompoundTag()));
		pTag.put("oil",oil.writeToNBT(new CompoundTag()));
		pTag.put("inventory",inventory.serializeNBT());
		pTag.putInt("progress",progress);
		pTag.putInt("processTotal",processTotal);
		pTag.putInt("burnTime",burnTime);
		pTag.putInt("burnTotal",burnTotal);
	}
	
	@Override
	public void load(CompoundTag pTag) {
		super.load(pTag);
		water.readFromNBT(pTag.getCompound("water"));
		oil.readFromNBT(pTag.getCompound("oil"));
		inventory.deserializeNBT(pTag.getCompound("inventory"));
		progress=pTag.getInt("progress");
		processTotal=pTag.getInt("processTotal");
		burnTime=pTag.getInt("burnTime");
		burnTotal=pTag.getInt("burnTotal");
	}
}
