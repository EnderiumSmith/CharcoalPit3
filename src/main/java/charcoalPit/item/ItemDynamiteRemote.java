package charcoalPit.item;

import charcoalPit.block.BlockDwarvenCandle;
import charcoalPit.core.ModBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ItemDynamiteRemote extends Item {
	public ItemDynamiteRemote(Properties pProperties) {
		super(pProperties);
	}
	
	public void toggleTarget(ItemStack stack, BlockPos pos, Player player){
		if(!stack.getOrCreateTag().contains("targets")){
			stack.getTag().putIntArray("targets", new int[0]);
		}
		int[] targets=stack.getTag().getIntArray("targets");
		for(int i=0;i<targets.length;i+=3){
			if(pos.getX()==targets[i]&&pos.getY()==targets[i+1]&&pos.getZ()==targets[i+2]){
				int[] newTargets=new int[targets.length-3];
				int z=0;
				for(int y=0;y<targets.length;y++){
					if(y-i<0||y-i>2){
						newTargets[z]=targets[y];
						z++;
					}
				}
				stack.getTag().putIntArray("targets", newTargets);
				if(player!=null){
					player.displayClientMessage(new TextComponent("Removed Candle"),true);
				}
				return;
			}
		}
		int[] newTargets=new int[targets.length+3];
		for(int i=0;i<targets.length;i++){
			newTargets[i]=targets[i];
		}
		newTargets[targets.length]=pos.getX();
		newTargets[targets.length+1]=pos.getY();
		newTargets[targets.length+2]=pos.getZ();
		if(player!=null){
			player.displayClientMessage(new TextComponent("Added Candle"),true);
		}
		stack.getTag().putIntArray("targets", newTargets);
	}
	
	@Override
	public InteractionResult useOn(UseOnContext pContext) {
		if(!pContext.getLevel().isClientSide&&pContext.getLevel().getBlockState(pContext.getClickedPos()).getBlock()== ModBlockRegistry.DwarvenCandle){
			toggleTarget(pContext.getItemInHand(),pContext.getClickedPos(),pContext.getPlayer());
			return InteractionResult.CONSUME;
		}
		return InteractionResult.CONSUME;
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
		ItemStack stack=pPlayer.getItemInHand(pUsedHand);
		
		if(stack.hasTag()&&stack.getTag().contains("targets")){
			pLevel.playSound(pPlayer,pPlayer.blockPosition(), SoundEvents.FLINTANDSTEEL_USE, SoundSource.PLAYERS,1F,1F);
			int[] targets=stack.getTag().getIntArray("targets");
			for(int i=0;i<targets.length;i+=3){
				BlockState state=pLevel.getBlockState(new BlockPos(targets[i], targets[i+1], targets[i+2]));
				if(state.getBlock()==ModBlockRegistry.DwarvenCandle&&!state.getValue(BlockDwarvenCandle.PRIMED)){
					pLevel.setBlock(new BlockPos(targets[i], targets[i+1], targets[i+2]), state.setValue(BlockDwarvenCandle.PRIMED, true),3);
					pLevel.playSound(pPlayer, targets[i], targets[i+1], targets[i+2], SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
					pLevel.scheduleTick(new BlockPos(targets[i], targets[i+1], targets[i+2]),ModBlockRegistry.DwarvenCandle,40);
				}
			}
			stack.getTag().putIntArray("targets", new int[0]);
			return new InteractionResultHolder<>(InteractionResult.SUCCESS,stack);
		}
		return new InteractionResultHolder<>(InteractionResult.PASS,stack);
	}
}
