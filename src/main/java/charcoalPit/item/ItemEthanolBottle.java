package charcoalPit.item;

import charcoalPit.core.ModItemRegistry;
import charcoalPit.potion.ModPotionRegistry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;

public class ItemEthanolBottle extends Item {
	public ItemEthanolBottle() {
		super(new Item.Properties().tab(ModItemRegistry.CHARCOAL_PIT).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE));
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
		Player playerentity = entityLiving instanceof Player ? (Player)entityLiving : null;
		if (playerentity instanceof ServerPlayer) {
			CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)playerentity, stack);
		}
		
		if (!worldIn.isClientSide) {
			MobEffectInstance mobEffect=entityLiving.getEffect(ModPotionRegistry.DRUNK);
			int level=9;
			int duration=20*60*5;
			if(mobEffect!=null){
				level+=mobEffect.getAmplifier()+1;
				duration=mobEffect.getDuration();
			}
			entityLiving.addEffect(new MobEffectInstance(ModPotionRegistry.DRUNK,duration,level));
		}
		
		if (playerentity != null) {
			playerentity.awardStat(Stats.ITEM_USED.get(this));
			if (!playerentity.getAbilities().instabuild) {
				stack.shrink(1);
			}
		}
		
		if (playerentity == null || !playerentity.getAbilities().instabuild) {
			if (stack.isEmpty()) {
				return new ItemStack(Items.GLASS_BOTTLE);
			}
			
			if (playerentity != null) {
				playerentity.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
			}
		}
		
		return stack;
	}
	
	public int getUseDuration(ItemStack pStack) {
		return 32;
	}
	
	public UseAnim getUseAnimation(ItemStack pStack) {
		return UseAnim.DRINK;
	}
	
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
		return ItemUtils.startUsingInstantly(pLevel, pPlayer, pHand);
	}
}
