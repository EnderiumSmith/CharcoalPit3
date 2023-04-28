package charcoalPit.item;

import charcoalPit.block.BlockMapleLog;
import charcoalPit.core.ModBlockRegistry;
import charcoalPit.core.ModItemRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemHandlerHelper;

public class ItemTreeTap extends Item {
	public ItemTreeTap(Properties pProperties) {
		super(pProperties);
	}
	
	@Override
	public InteractionResult useOn(UseOnContext pContext) {
		BlockState state=pContext.getLevel().getBlockState(pContext.getClickedPos());
		if(state.getBlock()== Blocks.ACACIA_LOG&& BlockMapleLog.isPartOfTree(pContext.getLevel(),pContext.getClickedPos())){
			pContext.getLevel().setBlock(pContext.getClickedPos(), ModBlockRegistry.MapleLog.defaultBlockState(),3);
			pContext.getLevel().playSound(pContext.getPlayer(),pContext.getClickedPos(), SoundEvents.AXE_STRIP, SoundSource.BLOCKS,1F,1F);
			return InteractionResult.SUCCESS;
		}
		if(state.getBlock()==ModBlockRegistry.MapleLog&&state.getValue(BlockMapleLog.HAS_SAP)){
			for(ItemStack stack:pContext.getPlayer().inventoryMenu.getItems()){
				if(stack.getItem()== Items.GLASS_BOTTLE) {
					stack.shrink(1);
					ItemHandlerHelper.giveItemToPlayer(pContext.getPlayer(), new ItemStack(ModItemRegistry.MapleSap));
					pContext.getLevel().playSound(pContext.getPlayer(),pContext.getClickedPos(),SoundEvents.BOTTLE_FILL,SoundSource.PLAYERS,1F,1F);
					pContext.getLevel().setBlock(pContext.getClickedPos(),state.setValue(BlockMapleLog.HAS_SAP,false),3);
					pContext.getItemInHand().hurtAndBreak(1, pContext.getPlayer(), (p) -> {
						p.broadcastBreakEvent(pContext.getHand());
					});
					return InteractionResult.SUCCESS;
				}
			}
			return InteractionResult.FAIL;
		}
		return InteractionResult.PASS;
	}
}
