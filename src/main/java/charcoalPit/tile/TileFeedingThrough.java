package charcoalPit.tile;

import charcoalPit.block.BlockFeedingThrough;
import charcoalPit.core.ModTileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.animal.Animal;
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

public class TileFeedingThrough extends BlockEntity {
	
	public ItemStackHandler inventory;
	int time;
	public boolean powered;
	
	public TileFeedingThrough(BlockPos pWorldPosition, BlockState pBlockState) {
		super(ModTileRegistry.FeedingThrough, pWorldPosition, pBlockState);
		inventory=new ItemStackHandler(){
			@Override
			protected void onContentsChanged(int slot) {
				setChanged();
				level.setBlock(getBlockPos(),getBlockState().setValue(BlockFeedingThrough.HAS_BAY,!inventory.getStackInSlot(0).isEmpty()),3);
			}
		};
		time=0;
		powered=false;
	}
	
	public void tick(){
		if(!powered) {
			if (time >= 7200) {
				time = 0;
				if (!inventory.getStackInSlot(0).isEmpty()) {
					List<Animal> animals = level.getEntitiesOfClass(Animal.class, new AABB(worldPosition.getX() - 4, worldPosition.getY() - 1, worldPosition.getZ() - 4,
							worldPosition.getX() + 5, worldPosition.getY() + 2, worldPosition.getZ() + 5));
					List<Animal> animals1=level.getEntitiesOfClass(Animal.class, new AABB(worldPosition.getX() - 2, worldPosition.getY() - 1, worldPosition.getZ() - 2,
							worldPosition.getX() + 3, worldPosition.getY() + 2, worldPosition.getZ() + 3));
					if(animals.size()<41&&animals1.size()<13) {
						for (Animal animal : animals) {
							if (animal.canFallInLove() && !animal.isBaby() && animal.getAge()==0 && animal.isFood(inventory.getStackInSlot(0))) {
								if (level.getRandom().nextFloat() < 0.66F)
									animal.setInLove(null);
								inventory.extractItem(0, 1, false);
								if (inventory.getStackInSlot(0).isEmpty())
									break;
							}
						}
						level.setBlock(getBlockPos(), getBlockState().setValue(BlockFeedingThrough.HAS_BAY, !inventory.getStackInSlot(0).isEmpty()), 3);
						setChanged();
					}
				}
			} else {
				time++;
				if(time%20==0)
					setChanged();
			}
		}
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
		pTag.putInt("cooldown",time);
		pTag.putBoolean("powered",powered);
	}
	
	@Override
	public void load(CompoundTag pTag) {
		super.load(pTag);
		inventory.deserializeNBT(pTag.getCompound("inventory"));
		time=pTag.getInt("cooldown");
		powered=pTag.getBoolean("powered");
	}
}
