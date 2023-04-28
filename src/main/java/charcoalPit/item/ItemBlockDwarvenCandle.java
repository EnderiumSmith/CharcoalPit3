package charcoalPit.item;

import charcoalPit.core.ModItemRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;

public class ItemBlockDwarvenCandle extends BlockItem {
	public ItemBlockDwarvenCandle(Block pBlock, Properties pProperties) {
		super(pBlock, pProperties);
	}
	
	@Override
	public InteractionResult useOn(UseOnContext pContext) {
		InteractionResult result=super.useOn(pContext);
		if(result.consumesAction()){
			ItemStack stack=pContext.getPlayer().getItemInHand(InteractionHand.OFF_HAND);
			if(stack.getItem()instanceof ItemDynamiteRemote remote){
				remote.toggleTarget(stack,pContext.getClickedPos().relative(pContext.getClickedFace()),pContext.getPlayer());
			}
		}
		return result;
	}
}
