package charcoalPit.tile;

import charcoalPit.block.BlockBloomeryy;
import charcoalPit.core.MethodHelper;
import charcoalPit.core.ModItemRegistry;
import charcoalPit.core.ModTileRegistry;
import charcoalPit.recipe.BloomingRecipe;
import charcoalPit.recipe.TuyereBlastingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.antlr.v4.runtime.atn.SemanticContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TileBlastFurnace extends BlockEntity {
	
	public ItemStackHandler inventory;
	public int progress,processTotal,burnTime,burnTotal,blastAir;
	public static final int FLUX=0,ORE=1,FUEL=2,OUT=3;
	public boolean needsTuyere=false,burnsHot=false;
	
	public TileBlastFurnace(BlockPos pWorldPosition, BlockState pBlockState) {
		super(ModTileRegistry.BlastFurnace,pWorldPosition, pBlockState);
		inventory=new ItemStackHandler(4){
			@Override
			protected void onContentsChanged(int slot) {
				if(slot==FLUX||slot==ORE)
					if(getStackInSlot(FLUX).isEmpty()||getStackInSlot(ORE).isEmpty()){
						progress=0;
						processTotal=0;
						blastAir=0;
						setActive(false);
					}
				setChanged();
			}
			
			@Override
			public boolean isItemValid(int slot, @NotNull ItemStack stack) {
				if(slot==FLUX){
					return stack.getItem()==ModItemRegistry.Flux;
				}
				if(slot==ORE){
					return isOre(stack,level);
				}
				if(slot==FUEL)
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
					if((blastAir>0&&burnsHot)||!needsTuyere){
						progress++;
					}
				}else{
					if(progress>0) {
						progress--;
					}
				}
			}else{
				//done smelting
				if(needsTuyere){
					TuyereBlastingRecipe recipe=TuyereBlastingRecipe.getRecipe(inventory.getStackInSlot(ORE),level);
					if(recipe!=null){
						inventory.insertItem(OUT,recipe.getResultItem().copy(),false);
						inventory.extractItem(FLUX,1,false);
						inventory.extractItem(ORE,1,false);
						processTotal=0;
						progress=0;
						setChanged();
					}
				}else{
					List<BlastingRecipe> recipes=level.getRecipeManager().getAllRecipesFor(RecipeType.BLASTING);
					for(BlastingRecipe recipe:recipes){
						if(recipe.getIngredients().get(0).test(inventory.getStackInSlot(ORE))){
							inventory.insertItem(OUT,recipe.getResultItem().copy(),false);
							inventory.extractItem(ORE,1,false);
							progress=0;
							processTotal=0;
							setChanged();
							break;
						}
					}
				}
				//try smelt
				if(!inventory.getStackInSlot(ORE).isEmpty()) {
					boolean foundTuyereRecipe = false;
					if (inventory.getStackInSlot(FLUX).getCount() > 0) {
						TuyereBlastingRecipe recipe = TuyereBlastingRecipe.getRecipe(inventory.getStackInSlot(ORE),level);
						if(recipe!=null){
							if(inventory.getStackInSlot(OUT).isEmpty()||
									inventory.insertItem(OUT,recipe.getResultItem(),true).isEmpty()){
								foundTuyereRecipe=true;
								progress=0;
								processTotal=recipe.getCookingTime()-1;
								needsTuyere=true;
								if(burnTime>0)
									setActive(true);
								setChanged();
							}
						}
					}
					if(!foundTuyereRecipe){
						List<BlastingRecipe> recipes=level.getRecipeManager().getAllRecipesFor(RecipeType.BLASTING);
						for(BlastingRecipe recipe:recipes){
							if(!recipe.getResultItem().isEmpty()&&recipe.getIngredients().get(0).test(inventory.getStackInSlot(ORE))){
								if(inventory.getStackInSlot(OUT).isEmpty()||
										inventory.insertItem(OUT,recipe.getResultItem(),true).isEmpty()){
									progress=0;
									processTotal=recipe.getCookingTime()-1;
									needsTuyere=false;
									if(burnTime>0)
										setActive(true);
									setChanged();
								}
								break;
							}
						}
					}
				}
				//
			}
		}else{
			//try smelt
			if(!inventory.getStackInSlot(ORE).isEmpty()) {
				boolean foundTuyereRecipe = false;
				if (inventory.getStackInSlot(FLUX).getCount() > 0) {
					TuyereBlastingRecipe recipe = TuyereBlastingRecipe.getRecipe(inventory.getStackInSlot(ORE),level);
					if(recipe!=null){
						if(inventory.getStackInSlot(OUT).isEmpty()||
							inventory.insertItem(OUT,recipe.getResultItem(),true).isEmpty()){
							foundTuyereRecipe=true;
							progress=0;
							processTotal=recipe.getCookingTime()-1;
							needsTuyere=true;
							if(burnTime>0)
								setActive(true);
							setChanged();
						}
					}
				}
				if(!foundTuyereRecipe){
					List<BlastingRecipe> recipes=level.getRecipeManager().getAllRecipesFor(RecipeType.BLASTING);
					for(BlastingRecipe recipe:recipes){
						if(!recipe.getResultItem().isEmpty()&&recipe.getIngredients().get(0).test(inventory.getStackInSlot(ORE))){
							if(inventory.getStackInSlot(OUT).isEmpty()||
								inventory.insertItem(OUT,recipe.getResultItem(),true).isEmpty()){
								progress=0;
								processTotal=recipe.getCookingTime()-1;
								needsTuyere=false;
								if(burnTime>0)
									setActive(true);
								setChanged();
							}
							break;
						}
					}
				}
			}
			
		}
		if(blastAir>0){
			blastAir--;
		}
		if(burnTime>0){
			burnTime--;
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
	
	public static boolean isOre(ItemStack stack, Level level){
		TuyereBlastingRecipe recipe=TuyereBlastingRecipe.getRecipe(stack,level);
		if(recipe!=null)
			return true;
		List<BlastingRecipe> recipes=level.getRecipeManager().getAllRecipesFor(RecipeType.BLASTING);
		for(BlastingRecipe recipe1:recipes){
			if(recipe1.getIngredients().get(0).test(stack))
				return true;
		}
		return false;
	}
	
	public static boolean isFuel(ItemStack stack){
		return ForgeHooks.getBurnTime(stack,RecipeType.BLASTING)>0;
	}
	
	public static boolean isHotFuel(ItemStack stack){
		return MethodHelper.isItemInTag(stack,MethodHelper.BLASTING_FUEL);
	}
	
	public void tryBurn(){
		if(!needsTuyere||(blastAir>0&&isHotFuel(inventory.getStackInSlot(FUEL)))){
			int i= ForgeHooks.getBurnTime(inventory.getStackInSlot(FUEL), RecipeType.BLASTING)/2;
			if(i>0){
				burnTime=i;
				burnTotal=i;
				burnsHot=isHotFuel(inventory.getStackInSlot(FUEL));
				ItemStack container=ItemStack.EMPTY;
				if(inventory.getStackInSlot(FUEL).hasContainerItem()){
					container=inventory.getStackInSlot(FUEL).getContainerItem().copy();
				}
				inventory.extractItem(FUEL, 1, false);
				if(!container.isEmpty()){
					container=inventory.insertItem(FUEL,container,false);
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
		Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), inventory.getStackInSlot(3));
	}
	
	static Capability<IItemHandler> ITEM = CapabilityManager.get(new CapabilityToken<>(){});
	public LazyOptional<IItemHandler> out=LazyOptional.of(()->new IItemHandler() {
		@Override
		public int getSlots() {
			return 4;
		}
		
		@NotNull
		@Override
		public ItemStack getStackInSlot(int slot) {
			return inventory.getStackInSlot(slot);
		}
		
		@NotNull
		@Override
		public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
			if(slot<OUT){
				return inventory.insertItem(slot,stack,simulate);
			}
			return stack;
		}
		
		@NotNull
		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			if(slot==OUT)
				return inventory.extractItem(slot,amount,simulate);
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
		pTag.putBoolean("needsTuyere",needsTuyere);
		pTag.putBoolean("burnsHot",burnsHot);
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
		needsTuyere=pTag.getBoolean("needsTuyere");
		burnsHot=pTag.getBoolean("burnsHot");
	}
	
}
