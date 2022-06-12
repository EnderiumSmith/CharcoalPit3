package charcoalPit.item.tool;

import charcoalPit.core.MethodHelper;
import charcoalPit.core.ModItemRegistry;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum ModTiers implements Tier {
	
	FLINT(0,130,4F,1F,5,()->{
		return Ingredient.of(Items.FLINT);
	},Tiers.WOOD),
	COPPER(1,165,4.5F,1.25F,14,()->{
		return Ingredient.of(Items.COPPER_INGOT);
	},Tiers.STONE),
	STEEL(2,375,7F,2.5F,7,()->{
		return Ingredient.of(MethodHelper.STEEL);
	},Tiers.IRON),
	ORICHALCUM(2,250,7F,2.0F,18,()->{
		return Ingredient.of(MethodHelper.ORICHALCUM);
	},Tiers.IRON);
	/*WOOD(0, 59, 2.0F, 0.0F, 15, () -> {
		return Ingredient.of(ItemTags.PLANKS);
	}),
	STONE(1, 131, 4.0F, 1.0F, 5, () -> {
		return Ingredient.of(ItemTags.STONE_TOOL_MATERIALS);
	}),
	IRON(2, 250, 6.0F, 2.0F, 14, () -> {
		return Ingredient.of(Items.IRON_INGOT);
	}),
	DIAMOND(3, 1561, 8.0F, 3.0F, 10, () -> {
		return Ingredient.of(Items.DIAMOND);
	}),
	GOLD(0, 32, 12.0F, 0.0F, 22, () -> {
		return Ingredient.of(Items.GOLD_INGOT);
	}),
	NETHERITE(4, 2031, 9.0F, 4.0F, 15, () -> {
		return Ingredient.of(Items.NETHERITE_INGOT);
	});*/
	
	private final int level;
	private final int uses;
	private final float speed;
	private final float damage;
	private final int enchantmentValue;
	private final LazyLoadedValue<Ingredient> repairIngredient;
	public final Tiers tier;
	
	private ModTiers(int level, int durability, float speed, float damage, int enchentability, Supplier<Ingredient> repair,Tiers tier) {
		this.level = level;
		this.uses = durability;
		this.speed = speed;
		this.damage = damage;
		this.enchantmentValue = enchentability;
		this.repairIngredient = new LazyLoadedValue<>(repair);
		this.tier=tier;
	}
	
	public int getUses() {
		return this.uses;
	}
	
	public float getSpeed() {
		return this.speed;
	}
	
	public float getAttackDamageBonus() {
		return this.damage;
	}
	
	public int getLevel() {
		return this.level;
	}
	
	public int getEnchantmentValue() {
		return this.enchantmentValue;
	}
	
	public Ingredient getRepairIngredient() {
		return this.repairIngredient.get();
	}
	
	@javax.annotation.Nullable public net.minecraft.tags.TagKey<net.minecraft.world.level.block.Block> getTag() { return net.minecraftforge.common.ForgeHooks.getTagFromVanillaTier(tier); }
	
}
