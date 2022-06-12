package charcoalPit.item;

import charcoalPit.core.ModItemRegistry;
import charcoalPit.potion.ModPotionRegistry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.stats.Stats;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.Level;

public class ItemAlcoholBottle extends PotionItem{

	public ItemAlcoholBottle() {
		super(new Item.Properties().tab(ModItemRegistry.CHARCOAL_PIT_FOODS).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE));
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
		Player playerentity = entityLiving instanceof Player ? (Player)entityLiving : null;
	      if (playerentity instanceof ServerPlayer) {
	         CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)playerentity, stack);
	      }

	      if (!worldIn.isClientSide) {
	         for(MobEffectInstance effectinstance : PotionUtils.getMobEffects(stack)) {
	            if (effectinstance.getEffect().isInstantenous()) {
	               effectinstance.getEffect().applyInstantenousEffect(playerentity, playerentity, entityLiving, effectinstance.getAmplifier(), 1.0D);
	            } else {
	            	if(effectinstance.getEffect()==ModPotionRegistry.DRUNK){
	            		MobEffectInstance effect2=entityLiving.getEffect(ModPotionRegistry.DRUNK);
	            		int l=0;
	            		if(effect2!=null) {
							l = effect2.getAmplifier() + 1 + effectinstance.getAmplifier();
							entityLiving.addEffect(new MobEffectInstance(ModPotionRegistry.DRUNK, effect2.getDuration(), l));
						}else
							entityLiving.addEffect(new MobEffectInstance(effectinstance));
					}else {
						entityLiving.addEffect(new MobEffectInstance(effectinstance));
					}
	            }
	         }
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

	@Override
	public boolean isFoil(ItemStack stack) {
		return stack.isEnchanted();
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if(group==ModItemRegistry.CHARCOAL_PIT_FOODS) {

			if(!didInit)
				this.initItems();

			items.add(cider);
			items.add(golden_cider);
			items.add(chorus_cider);
			items.add(vodka);
			items.add(beetroot_beer);
			items.add(beer);
			items.add(sweetberry_wine);
			items.add(warped_wine);
			items.add(mead);
			items.add(rum);
			items.add(honey_dewois);
			//items.add(spider_spirit);
			items.add(glowberry_wine);
		}
	}

	public static boolean didInit=false;

	public void initItems()
	{
		didInit=true;
		cider=PotionUtils.setPotion(new ItemStack(this), ModPotionRegistry.CIDER);
		golden_cider=PotionUtils.setPotion(new ItemStack(this), ModPotionRegistry.GOLDEN_CIDER);
		vodka=PotionUtils.setPotion(new ItemStack(this), ModPotionRegistry.VODKA);
		beetroot_beer=PotionUtils.setPotion(new ItemStack(this), ModPotionRegistry.BEETROOT_BEER);
		sweetberry_wine=PotionUtils.setPotion(new ItemStack(this), ModPotionRegistry.SWEETBERRY_WINE);
		mead=PotionUtils.setPotion(new ItemStack(this), ModPotionRegistry.MEAD);
		beer=PotionUtils.setPotion(new ItemStack(this), ModPotionRegistry.BEER);
		rum=PotionUtils.setPotion(new ItemStack(this), ModPotionRegistry.RUM);
		chorus_cider=PotionUtils.setPotion(new ItemStack(this), ModPotionRegistry.CHORUS_CIDER);
		//spider_spirit=PotionUtils.setPotion(new ItemStack(this), ModPotionRegistry.SPIDER_SPIRIT);
		honey_dewois=PotionUtils.setPotion(new ItemStack(this), ModPotionRegistry.HONEY_DEWOIS);
		warped_wine=PotionUtils.setPotion(new ItemStack(this), ModPotionRegistry.WARPED_WINE);
		glowberry_wine=PotionUtils.setPotion(new ItemStack(this),ModPotionRegistry.GLOWBERRY_WINE);

		cider.getTag().putInt("CustomPotionColor", 0xE50000);
		golden_cider.getTag().putInt("CustomPotionColor", 0xDBB40C);
		vodka.getTag().putInt("CustomPotionColor", 0xE6DAA6);
		beetroot_beer.getTag().putInt("CustomPotionColor", 0x840000);
		sweetberry_wine.getTag().putInt("CustomPotionColor", 0x06470C);
		mead.getTag().putInt("CustomPotionColor", 0xFAC205);
		beer.getTag().putInt("CustomPotionColor", 0xFDAA48);
		rum.getTag().putInt("CustomPotionColor", 0x650021);
		chorus_cider.getTag().putInt("CustomPotionColor", 0x9A0EAA);
		honey_dewois.getTag().putInt("CustomPotionColor", 0xF97306);
		warped_wine.getTag().putInt("CustomPotionColor", 0x0485D1);
		//spider_spirit.getTag().putInt("CustomPotionColor", 0xA5A502);
		glowberry_wine.getTag().putInt("CustomPotionColor",0xFDAA48);
	}

	public static ItemStack cider;
	public static ItemStack golden_cider;
	public static ItemStack vodka;
	public static ItemStack beetroot_beer;
	public static ItemStack sweetberry_wine;
	public static ItemStack mead;
	public static ItemStack beer;
	public static ItemStack rum;
	public static ItemStack chorus_cider;
	//public static ItemStack spider_spirit;
	public static ItemStack honey_dewois;
	public static ItemStack warped_wine;
	public static ItemStack glowberry_wine;

}
