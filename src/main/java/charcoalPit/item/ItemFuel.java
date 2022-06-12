package charcoalPit.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;

public class ItemFuel extends Item{

	public int burnTime;
	public ItemFuel(Properties properties) {
		super(properties);
	}
	
	public ItemFuel setBurnTime(int t) {
		burnTime=t;
		return this;
	}
	
	@Override
	public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
		return burnTime;
	}

}
