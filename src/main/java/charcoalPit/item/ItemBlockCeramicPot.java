package charcoalPit.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class ItemBlockCeramicPot extends BlockItem {
	public ItemBlockCeramicPot(Block pBlock, Properties pProperties) {
		super(pBlock, pProperties);
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack) {
		return stack.hasTag()?1:16;
	}
}
