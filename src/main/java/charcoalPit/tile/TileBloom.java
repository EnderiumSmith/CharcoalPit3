package charcoalPit.tile;

import charcoalPit.core.ModTileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileBloom extends BlockEntity {
	
	public ItemStack items=ItemStack.EMPTY;
	public int workCount;
	
	public TileBloom(BlockPos pWorldPosition, BlockState pBlockState) {
		super(ModTileRegistry.Bloom, pWorldPosition, pBlockState);
		workCount=0;
	}
	
	public void dropInventory(){
		Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), items);
	}
	
	@Override
	protected void saveAdditional(CompoundTag pTag) {
		super.saveAdditional(pTag);
		pTag.put("items",items.serializeNBT());
		pTag.putInt("work",workCount);
	}
	
	@Override
	public void load(CompoundTag pTag) {
		super.load(pTag);
		items=ItemStack.of(pTag.getCompound("items"));
		workCount=pTag.getInt("work");
	}
}
