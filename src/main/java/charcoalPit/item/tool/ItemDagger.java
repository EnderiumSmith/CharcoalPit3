package charcoalPit.item.tool;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ItemDagger extends ShearsItem {
	/** Modifiers applied when the item is in the mainhand of a user. */
	private final Multimap<Attribute, AttributeModifier> defaultModifiers;
	private final Tier tier;
	
	public ItemDagger(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Item.Properties pProperties) {
		super(pProperties.defaultDurability(pTier.getUses()));
		this.tier=pTier;
		float attackDamage = (float) pAttackDamageModifier + pTier.getAttackDamageBonus();
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", (double) attackDamage, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", (double)pAttackSpeedModifier, AttributeModifier.Operation.ADDITION));
		this.defaultModifiers = builder.build();
	}
	
	@Override
	public int getEnchantmentValue() {
		return tier.getEnchantmentValue();
	}
	
	public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
		return this.tier.getRepairIngredient().test(pRepair) || super.isValidRepairItem(pToRepair, pRepair);
	}
	
	public boolean canAttackBlock(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
		return !pPlayer.isCreative();
	}
	
	public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
		pStack.hurtAndBreak(1, pAttacker, (p_43296_) -> {
			p_43296_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
		});
		return true;
	}
	
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot pEquipmentSlot) {
		return pEquipmentSlot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(pEquipmentSlot);
	}
	
}
