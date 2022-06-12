package charcoalPit.tile;

import charcoalPit.block.BlockNestBox;
import charcoalPit.core.ModTileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TileNestBox extends BlockEntity {
	
	public ItemStackHandler inventory;
	public int cooldown;
	public int incubating;
	
	public TileNestBox(BlockPos pWorldPosition, BlockState pBlockState) {
		super(ModTileRegistry.NestBox, pWorldPosition, pBlockState);
		inventory=new ItemStackHandler(){
			@Override
			protected void onContentsChanged(int slot) {
				setChanged();
				updateShape();
				updateIncubation(true);
			}
			
			@Override
			public boolean isItemValid(int slot, @NotNull ItemStack stack) {
				return stack.getItem()==Items.EGG;
			}
		};
		cooldown=0;
		incubating=0;
	}
	
	public void tick(){
		if(cooldown<=0){
			if(incubating>0){
				List<Animal> animals=level.getEntitiesOfClass(Animal.class,new AABB(worldPosition.getX()-4,worldPosition.getY()-1,worldPosition.getZ()-4,
						worldPosition.getX()+5,worldPosition.getY()+2,worldPosition.getZ()+5));
				if(animals.size()<41&&canIncubate(animals)){
					int chicks=inventory.getStackInSlot(0).getCount();
					for(int i=0;i<inventory.getStackInSlot(0).getCount();i++){
						if(level.random.nextFloat()<0.004)
							chicks+=3;
					}
					for(int y=0;y<chicks;y++){
						Chicken chick= EntityType.CHICKEN.create(level);
						chick.setPos(worldPosition.getX()+0.5D,worldPosition.getY()+0.5D,worldPosition.getZ()+0.5D);
						chick.setBaby(true);
						level.addFreshEntity(chick);
					}
					if(chicks>0)
						level.playSound(null,worldPosition,SoundEvents.TURTLE_EGG_HATCH, SoundSource.BLOCKS,1F,1F);
					inventory.setStackInSlot(0,ItemStack.EMPTY);
					setChanged();
					updateIncubation(true);
					updateShape();
				}else{
					cooldown=6000;
					setChanged();
				}
			}else{
				if(inventory.getStackInSlot(0).getCount()<inventory.getStackInSlot(0).getMaxStackSize()){
					List<Chicken> chickens=level.getEntitiesOfClass(Chicken.class,new AABB(worldPosition.getX()-4,worldPosition.getY()-1,worldPosition.getZ()-4,
							worldPosition.getX()+5,worldPosition.getY()+2,worldPosition.getZ()+5));
					for(Chicken chicken:chickens){
						if(chicken.eggTime<=100&&chicken.getClass()==Chicken.class){
							if(inventory.insertItem(0, new ItemStack(Items.EGG),false).isEmpty()){
								chicken.eggTime=chicken.getRandom().nextInt(6000)+6000;
								chicken.playSound(SoundEvents.CHICKEN_EGG, 1.0F, (chicken.getRandom().nextFloat() - chicken.getRandom().nextFloat()) * 0.2F + 1.0F);
							}
						}
					}
					setChanged();
					updateShape();
					if(inventory.getStackInSlot(0).getCount()==inventory.getStackInSlot(0).getMaxStackSize()){
						incubating=2;
						cooldown=6000;
					}else{
						cooldown=100;
					}
				}else{
					//full
					cooldown=24000;
				}
			}
		}else{
			cooldown--;
			if(incubating>0&&level.random.nextFloat()<0.001F&&inventory.getStackInSlot(0).getCount()>0){
				level.levelEvent(1505, worldPosition, 0);
			}
			if(cooldown%20==0){
				setChanged();
			}
		}
	}
	
	public void updateShape(){
		level.setBlock(getBlockPos(),getBlockState().setValue(BlockNestBox.EGGS,(int)Math.ceil(inventory.getStackInSlot(0).getCount()/4D)),3);
	}
	
	public void updateIncubation(boolean forceReset){
		BlockState state=level.getBlockState(worldPosition.below());
		if(state.getBlock()== Blocks.MAGMA_BLOCK||(state.getBlock()==Blocks.CAMPFIRE&&state.getValue(CampfireBlock.LIT))){
			if(forceReset||incubating==0) {
				incubating = 1;
				cooldown = 6000;
			}
		}else{
			if(forceReset||incubating==1) {
				incubating = 0;
				cooldown = 100;
			}
		}
	}
	
	public boolean canIncubate(List<Animal> animals){
		if(incubating==1){
			return true;
		}else{
			BlockState state=level.getBlockState(worldPosition.below());
			if(state.getBlock()== Blocks.MAGMA_BLOCK||state.getBlock()==Blocks.CAMPFIRE){
				return true;
			}
			int c=0;
			for(Animal animal:animals){
				if(animal.getClass()==Chicken.class){
					c++;
					if(c>=2)
						return true;
				}
			}
		}
		return false;
	}
	
	public void dropInventory() {
		Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), inventory.getStackInSlot(0));
	}
	
	static Capability<IItemHandler> ITEM = CapabilityManager.get(new CapabilityToken<>(){});
	public LazyOptional<IItemHandler> out=LazyOptional.of(()->inventory);
	
	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if(cap==ITEM){
			return out.cast();
		}
		return super.getCapability(cap, side);
	}
	
	@Override
	protected void saveAdditional(CompoundTag pTag) {
		pTag.put("inventory",inventory.serializeNBT());
		pTag.putInt("cooldown",cooldown);
		pTag.putInt("incubating",incubating);
	}
	
	@Override
	public void load(CompoundTag pTag) {
		super.load(pTag);
		inventory.deserializeNBT(pTag.getCompound("inventory"));
		cooldown=pTag.getInt("cooldown");
		incubating=pTag.getInt("incubating");
	}
}
