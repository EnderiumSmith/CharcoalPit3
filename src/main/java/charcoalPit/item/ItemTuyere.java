package charcoalPit.item;

import charcoalPit.core.ModBlockRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

public class ItemTuyere extends Item {
	public ItemTuyere(Properties pProperties) {
		super(pProperties);
	}
	
	@Override
	public InteractionResult useOn(UseOnContext pContext) {
		BlockState state=pContext.getLevel().getBlockState(pContext.getClickedPos());
		if(state.getBlock()== Blocks.BLAST_FURNACE){
			pContext.getLevel().setBlock(pContext.getClickedPos(),
					ModBlockRegistry.BlastFurnace.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING,state.getValue(HorizontalDirectionalBlock.FACING)),
					3);
			pContext.getItemInHand().shrink(1);
			pContext.getLevel().playSound(pContext.getPlayer(),pContext.getClickedPos(), SoundEvents.ANVIL_USE, SoundSource.BLOCKS,1F,1F);
			return InteractionResult.SUCCESS;
		}
		return super.useOn(pContext);
	}
}
