package charcoalPit.tile;

import charcoalPit.block.BlockBloomeryy;
import charcoalPit.block.BlockDistillery;
import charcoalPit.core.ModTileRegistry;
import charcoalPit.recipe.DistilleryRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
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

public class TileDistillery extends BlockEntity {
	
	public FluidTank input,output;
	public ItemStackHandler fuel;
	public int progress,processTotal,burnTime,burnTotal;
	
	public TileDistillery(BlockPos pWorldPosition, BlockState pBlockState) {
		super(ModTileRegistry.Distillery, pWorldPosition, pBlockState);
		input=new FluidTank(16000,fluid->DistilleryRecipe.isValidInput(fluid,level)){
			@Override
			protected void onContentsChanged() {
				setChanged();
			}
		};
		output=new FluidTank(4000){
			@Override
			protected void onContentsChanged() {
				setChanged();
			}
		};
		fuel=new ItemStackHandler(1){
			@Override
			protected void onContentsChanged(int slot) {
				setChanged();
			}
			
			@Override
			public boolean isItemValid(int slot, @NotNull ItemStack stack) {
				return ForgeHooks.getBurnTime(stack,RecipeType.SMELTING)>0;
			}
		};
		processTotal=0;
		progress=0;
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
				DistilleryRecipe recipe=DistilleryRecipe.getRecipe(input.getFluid(),level);
				if(recipe!=null){
					output.fill(new FluidStack(recipe.output.getFluid(),recipe.output.amount,recipe.output.nbt), IFluidHandler.FluidAction.EXECUTE);
					input.drain(recipe.input.amount, IFluidHandler.FluidAction.EXECUTE);
				}
				processTotal=0;
				progress=0;
				setChanged();
			}
		}
		if(processTotal<=0){
			if(!input.isEmpty()){
				DistilleryRecipe recipe=DistilleryRecipe.getRecipe(input.getFluid(),level);
				if(recipe!=null){
					if(input.getFluidAmount()>=recipe.input.amount&&
							output.fill(new FluidStack(recipe.output.getFluid(),recipe.output.amount,recipe.output.nbt), IFluidHandler.FluidAction.SIMULATE)==recipe.output.amount){
						//can fit
						progress=0;
						processTotal=recipe.time;
						setChanged();
					}
				}
			}
		}
		if(burnTime>0){
			burnTime--;
			if(burnTime%20==0)
				setChanged();
		}
		if(processTotal>0&&burnTime<=0){
			int f= ForgeHooks.getBurnTime(fuel.getStackInSlot(0), RecipeType.SMELTING);
			if(f>0){
				ItemStack container=fuel.getStackInSlot(0).getContainerItem().copy();
				burnTime=f;
				burnTotal=f;
				fuel.extractItem(0,1,false);
				if(!container.isEmpty()){
					if(!fuel.getStackInSlot(0).isEmpty()){
						Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), container);
					}else{
						fuel.setStackInSlot(0,container);
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
		Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), fuel.getStackInSlot(0));
	}
	
	public void setActive(boolean active){
		if(getBlockState().getValue(BlockDistillery.ACTIVE)!=active)
			level.setBlock(worldPosition,getBlockState().setValue(BlockDistillery.ACTIVE,active),3);
	}
	
	static Capability<IItemHandler> ITEM = CapabilityManager.get(new CapabilityToken<>(){});
	public LazyOptional<IItemHandler> items=LazyOptional.of(()->new IItemHandler() {
		@Override
		public int getSlots() {
			return 1;
		}
		
		@NotNull
		@Override
		public ItemStack getStackInSlot(int slot) {
			return fuel.getStackInSlot(0);
		}
		
		@NotNull
		@Override
		public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
			return fuel.insertItem(0,stack,simulate);
		}
		
		@NotNull
		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			return ItemStack.EMPTY;
		}
		
		@Override
		public int getSlotLimit(int slot) {
			return fuel.getSlotLimit(slot);
		}
		
		@Override
		public boolean isItemValid(int slot, @NotNull ItemStack stack) {
			return ForgeHooks.getBurnTime(stack,RecipeType.SMELTING)>0;
		}
	});
	
	static Capability<IFluidHandler> FLUID = CapabilityManager.get(new CapabilityToken<>(){});
	public LazyOptional<IFluidHandler> fluids=LazyOptional.of(()->new IFluidHandler() {
		@Override
		public int getTanks() {
			return 2;
		}
		
		@NotNull
		@Override
		public FluidStack getFluidInTank(int tank) {
			if(tank==0)
				return input.getFluid();
			return output.getFluid();
		}
		
		@Override
		public int getTankCapacity(int tank) {
			if(tank==0)
				return 16000;
			return 4000;
		}
		
		@Override
		public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
			if(tank==0)
				return DistilleryRecipe.isValidInput(stack,level);
			return false;
		}
		
		@Override
		public int fill(FluidStack resource, FluidAction action) {
			return input.fill(resource,action);
		}
		
		@NotNull
		@Override
		public FluidStack drain(FluidStack resource, FluidAction action) {
			return output.drain(resource,action);
		}
		
		@NotNull
		@Override
		public FluidStack drain(int maxDrain, FluidAction action) {
			return output.drain(maxDrain,action);
		}
	});
	
	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if(cap==ITEM)
			return items.cast();
		if(cap==FLUID)
			return fluids.cast();
		return super.getCapability(cap, side);
	}
	
	@Override
	public void setRemoved() {
		super.setRemoved();
		items.invalidate();
		fluids.invalidate();
	}
	
	@Override
	protected void saveAdditional(CompoundTag pTag) {
		super.saveAdditional(pTag);
		pTag.put("input",input.writeToNBT(new CompoundTag()));
		pTag.put("output",output.writeToNBT(new CompoundTag()));
		pTag.put("fuel",fuel.serializeNBT());
		pTag.putInt("progress",progress);
		pTag.putInt("processTotal",processTotal);
		pTag.putInt("burnTime",burnTime);
		pTag.putInt("burnTotal",burnTotal);
	}
	
	@Override
	public void load(CompoundTag pTag) {
		super.load(pTag);
		input.readFromNBT(pTag.getCompound("input"));
		output.readFromNBT(pTag.getCompound("output"));
		fuel.deserializeNBT(pTag.getCompound("fuel"));
		progress=pTag.getInt("progress");
		processTotal=pTag.getInt("processTotal");
		burnTime=pTag.getInt("burnTime");
		burnTotal=pTag.getInt("burnTotal");
	}
}
