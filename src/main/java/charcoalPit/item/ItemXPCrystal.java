package charcoalPit.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemXPCrystal extends ItemNameBlockItem {
	public static final int XP_TO_LV30=1395;
	public static final int XP_TO_LV10=160;
	
	public ItemXPCrystal(Block p_41579_, Properties p_41580_) {
		super(p_41579_, p_41580_);
	}
	
	@Override
	public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
		super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
		if(pStack.hasTag()){
			pTooltip.add(new TextComponent(ChatFormatting.GREEN+"XP:"+pStack.getTag().getInt("XP")));
		}
	}
	
	@Override
	public boolean isFoil(ItemStack pStack) {
		return true;
	}
	
	@Override
	public boolean isBarVisible(ItemStack pStack) {
		return true;
	}
	
	@Override
	public int getBarWidth(ItemStack pStack) {
		return Math.round((float)getXP(pStack) * 13.0F / XP_TO_LV30);
	}
	
	@Override
	public int getBarColor(ItemStack pStack) {
		float f = Math.max(0.0F, ((float)getXP(pStack)) / XP_TO_LV30);
		return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
	}
	
	public int getXP(ItemStack stack){
		return stack.getOrCreateTag().getInt("XP");
	}
	
	@Override
	public UseAnim getUseAnimation(ItemStack pStack) {
		return UseAnim.EAT;
	}
	
	@Override
	public int getUseDuration(ItemStack pStack) {
		return 16;
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
		int XP=getXP(pStack);
		if(pLivingEntity instanceof Player player){
			player.giveExperiencePoints(Math.min(XP_TO_LV10,XP));
			XP-=Math.min(XP_TO_LV10,XP);
			pStack.getTag().putInt("XP",XP);
			if(XP==0)
				pStack.shrink(1);
		}
		return pStack;
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
		ItemStack stack=pPlayer.getItemInHand(pUsedHand);
		if(getXP(stack)>0){
			pPlayer.startUsingItem(pUsedHand);
			return InteractionResultHolder.consume(stack);
		}
		return InteractionResultHolder.fail(stack);
	}
}
