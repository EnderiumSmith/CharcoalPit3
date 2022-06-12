package charcoalPit.item;

import java.util.List;

import charcoalPit.core.ModItemRegistry;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public class ItemAeternalis extends Item{

	public ItemAeternalis() {
		super(new Properties().tab(ModItemRegistry.CHARCOAL_PIT).rarity(Rarity.EPIC));
	}
	
	@Override
	public boolean isFoil(ItemStack stack) {
		return true;
	}
	
	@Override
	public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
		return 200;
	}
	
	@Override
	public ItemStack getContainerItem(ItemStack itemStack) {
		return new ItemStack(this);
	}
	
	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return true;
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		tooltip.add(new TextComponent(ChatFormatting.LIGHT_PURPLE+"An ancient relic that poseses the power to burn eternal"));
	}

}
