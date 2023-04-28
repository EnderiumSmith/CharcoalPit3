package charcoalPit.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class ItemBlockFlowerLeaves extends BlockItem {
	public ItemBlockFlowerLeaves(Block pBlock, Properties pProperties) {
		super(pBlock, pProperties);
	}
	
	@Override
	public String getDescriptionId(ItemStack stack) {
		if(stack.hasTag()&&stack.getTag().contains("stage")){
			if(stack.getTag().getInt("stage")==7){
				return super.getDescriptionId(stack).concat("_flower");
			}
		}
		return super.getDescriptionId(stack);
	}
}
