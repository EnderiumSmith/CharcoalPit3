package charcoalPit.tile;

import charcoalPit.block.BlockBloomeryy;
import charcoalPit.core.MethodHelper;
import charcoalPit.core.ModItemRegistry;
import charcoalPit.core.ModTileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TileBloomeryy extends BlockEntity {
	
	public ItemStackHandler inventory;
	public int progress,processTotal,burnTime,burnTotal,blastAir;
	
	public TileBloomeryy(BlockPos pWorldPosition, BlockState pBlockState) {
		super(ModTileRegistry.Bloomeryy, pWorldPosition, pBlockState);
		inventory=new ItemStackHandler(3){
			
			@Override
			protected void onContentsChanged(int slot) {
				if(slot==0)
					if(getStackInSlot(0).isEmpty()){
						progress=0;
						processTotal=0;
						blastAir=0;
						setActive(false);
					}
				setChanged();
			}
			
			@Override
			public boolean isItemValid(int slot, @NotNull ItemStack stack) {
				if(slot==0){
					return isOre(stack);
				}
				if(slot==1)
					return isFuel(stack);
				return super.isItemValid(slot,stack);
			}
		};
		progress=0;
		processTotal=0;
		burnTime=0;
		burnTotal=0;
		blastAir=0;
	}
	
	public void tick(){
		if(processTotal>0){
			if(progress<processTotal){
				if(burnTime>0){
					if(blastAir>0){
						progress++;
					}
				}else{
					if(progress>0) {
						progress--;
					}
				}
			}else{
				//done smelting
				if(inventory.getStackInSlot(2).isEmpty()){
					ItemStack stack=new ItemStack(ModItemRegistry.Bloom,1);
					stack.addTagElement("items", new ItemStack(Items.IRON_INGOT, 1).serializeNBT());
					inventory.insertItem(2,stack,false);
					inventory.extractItem(0,1,false);
				}else{
					ItemStack stack=inventory.getStackInSlot(2);
					ItemStack nested=ItemStack.of(stack.getTag().getCompound("items"));
					nested.grow(1);
					stack.getTag().put("items",nested.serializeNBT());
					inventory.extractItem(0,1,false);
				}
				/*int i=Math.min(8,inventory.getStackInSlot(0).getCount());
				ItemStack stack=new ItemStack(ModItemRegistry.Bloom,1);
				stack.addTagElement("items", new ItemStack(Items.IRON_INGOT, i).serializeNBT());
				inventory.insertItem(2,stack,false);
				inventory.extractItem(0,i,false);*/
				processTotal=0;
				progress=0;
				setChanged();
				if(trySmelt()){
					progress=0;
					processTotal=199;
				}
				/*if(isOre(inventory.getStackInSlot(0))) {
					ItemStack stack2 = new ItemStack(ModItemRegistry.Bloom, 1);
					stack2.addTagElement("items", new ItemStack(Items.IRON_INGOT, Math.min(8,inventory.getStackInSlot(0).getCount())).serializeNBT());
					if (inventory.insertItem(2, stack2, true).isEmpty()) {
						progress = 0;
						processTotal = 1599;
						if (burnTime > 0)
							setActive(true);
						setChanged();
					}
				}*/
			}
		}else{
			//try smelt
			if(trySmelt()){
				progress=0;
				processTotal=199;
				if(burnTime>0)
					setActive(true);
				setChanged();
			}
				/*if(isOre(inventory.getStackInSlot(0))) {
					ItemStack stack = new ItemStack(ModItemRegistry.Bloom, 1);
					stack.addTagElement("items", new ItemStack(Items.IRON_INGOT, Math.min(8,inventory.getStackInSlot(0).getCount())).serializeNBT());
					if (inventory.insertItem(2, stack, true).isEmpty()) {
						progress = 0;
						processTotal = 1599;
						if (burnTime > 0)
							setActive(true);
						setChanged();
					}
				}*/
			
		}
		if(blastAir>0)
			blastAir--;
		if(burnTime>0){
			burnTime--;
			if(blastAir>0){
				burnTime--;
			}
			if(burnTime%20==0)
				setChanged();
			if(burnTime<=0){
				if(processTotal>0) {
					tryBurn();
				}
				if(burnTime<=0)
					setActive(false);
			}
		}else if(processTotal>0){
			tryBurn();
			if(burnTime>0){
				setActive(true);
			}
		}
	}
	public boolean trySmelt(){
		if(isOre(inventory.getStackInSlot(0))){
			if(inventory.getStackInSlot(2).isEmpty())
				return true;
			else{
				ItemStack stack=inventory.getStackInSlot(2);
				ItemStack nested=ItemStack.of(stack.getTag().getCompound("items"));
				if(nested.getItem()==Items.IRON_INGOT&&nested.getCount()<nested.getMaxStackSize())
					return true;
			}
		}
		return false;
	}
	
	public static boolean isOre(ItemStack stack){
		return MethodHelper.isItemInTag(stack,MethodHelper.IRON_ORE)||MethodHelper.isItemInTag(stack,MethodHelper.RAW_IRON_ORE);
	}
	
	public static boolean isFuel(ItemStack stack){
		return MethodHelper.isItemInTag(stack,MethodHelper.BLOOMERY_FUEL);
	}
	
	public void tryBurn(){
		if(blastAir>0&&isFuel(inventory.getStackInSlot(1))){
			int i= ForgeHooks.getBurnTime(inventory.getStackInSlot(1), RecipeType.BLASTING);
			if(i>0){
				burnTime=i;
				burnTotal=i;
				ItemStack container=ItemStack.EMPTY;
				if(inventory.getStackInSlot(1).hasContainerItem()){
					container=inventory.getStackInSlot(1).getContainerItem().copy();
				}
				inventory.extractItem(1, 1, false);
				if(!container.isEmpty()){
					container=inventory.insertItem(1,container,false);
					if(!container.isEmpty()){
						Containers.dropItemStack(level,worldPosition.getX(),worldPosition.getY(),worldPosition.getZ(),container);
					}
				}
				
			}
		}
	}
	
	public void blastAir(){
		blastAir+=60;
		if(blastAir>800){
			blastAir=800;
		}
	}
	
	public void setActive(boolean active){
		if(getBlockState().getValue(BlockBloomeryy.ACTIVE)!=active)
			level.setBlock(worldPosition,getBlockState().setValue(BlockBloomeryy.ACTIVE,active),3);
	}
	
	public void dropInventory(){
		Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), inventory.getStackInSlot(0));
		Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), inventory.getStackInSlot(1));
		Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), inventory.getStackInSlot(2));
	}
	
	static Capability<IItemHandler> ITEM = CapabilityManager.get(new CapabilityToken<>(){});
	public LazyOptional<IItemHandler> out=LazyOptional.of(()->new IItemHandler() {
		@Override
		public int getSlots() {
			return 3;
		}
		
		@NotNull
		@Override
		public ItemStack getStackInSlot(int slot) {
			return inventory.getStackInSlot(slot);
		}
		
		@NotNull
		@Override
		public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
			if(slot<2){
				return inventory.insertItem(slot,stack,simulate);
			}
			return stack;
		}
		
		@NotNull
		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			if(slot==2)
				return inventory.extractItem(slot,amount,simulate);
			return ItemStack.EMPTY;
		}
		
		@Override
		public int getSlotLimit(int slot) {
			return inventory.getSlotLimit(slot);
		}
		
		@Override
		public boolean isItemValid(int slot, @NotNull ItemStack stack) {
			return true;
		}
	});
	
	
	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if(cap==ITEM)
			return out.cast();
		return super.getCapability(cap, side);
	}
	
	@Override
	protected void saveAdditional(CompoundTag pTag) {
		super.saveAdditional(pTag);
		pTag.put("inventory",inventory.serializeNBT());
		pTag.putInt("progress",progress);
		pTag.putInt("processTotal",processTotal);
		pTag.putInt("burnTime",burnTime);
		pTag.putInt("burnTotal",burnTotal);
		pTag.putInt("blastAir",blastAir);
	}
	
	@Override
	public void load(CompoundTag pTag) {
		super.load(pTag);
		inventory.deserializeNBT(pTag.getCompound("inventory"));
		progress=pTag.getInt("progress");
		processTotal=pTag.getInt("processTotal");
		burnTime=pTag.getInt("burnTime");
		burnTotal=pTag.getInt("burnTotal");
		blastAir=pTag.getInt("blastAir");
	}
}
