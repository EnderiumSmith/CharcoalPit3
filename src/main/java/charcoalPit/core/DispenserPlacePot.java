package charcoalPit.core;

import charcoalPit.tile.TileCeramicPot;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.BlockSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;

public class DispenserPlacePot extends DefaultDispenseItemBehavior{
	
	@Override
	protected ItemStack execute(BlockSource source, ItemStack stack) {
		Direction facing = (Direction)source.getBlockState().getValue(DispenserBlock.FACING);
        BlockPos pos=source.getPos().relative(facing);
        if(source.getLevel().getBlockState(pos).getMaterial().isReplaceable()){
        	source.getLevel().setBlockAndUpdate(pos, Block.byItem(stack.getItem()).defaultBlockState());
        	if(stack.hasTag()&&stack.getTag().contains("inventory")){
    			((TileCeramicPot)source.getLevel().getBlockEntity(pos)).inventory.deserializeNBT(stack.getTag().getCompound("inventory"));
    		}
        	source.getLevel().playSound(null, pos, SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 1F, 1F);
        	stack.shrink(1);
        	return stack;
        }else{
	        return stack;
        }
	}
	
}
