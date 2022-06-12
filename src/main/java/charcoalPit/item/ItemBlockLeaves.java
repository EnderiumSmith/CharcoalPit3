package charcoalPit.item;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

public class ItemBlockLeaves extends BlockItem {
	
	public ItemBlockLeaves(Block blockIn, Properties builder) {
		super(blockIn, builder);
	}
	
	@Override
	public String getDescriptionId(ItemStack stack) {
		if(stack.hasTag()&&stack.getTag().contains("stage")){
			if(stack.getTag().getInt("stage")==3){
				return super.getDescriptionId(stack).concat("_flower");
			}
			if(stack.getTag().getInt("stage")==6){
				return super.getDescriptionId(stack).concat("_unripe");
			}
			if(stack.getTag().getInt("stage")==7){
				return super.getDescriptionId(stack).concat("_ripe");
			}
		}
		return super.getDescriptionId(stack);
	}
}
