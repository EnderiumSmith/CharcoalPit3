package charcoalPit.item;

import charcoalPit.core.ModItemRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemMortarPestle extends Item {
	
	public ItemMortarPestle() {
		super(new Properties().tab(ModItemRegistry.CHARCOAL_PIT).durability(64));
	}
	
	@Override
	public ItemStack getContainerItem(ItemStack itemStack) {
		ItemStack stack=new ItemStack(this,1);
		stack.setDamageValue(itemStack.getDamageValue()+1);
		return stack;
	}
	
	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return stack.getDamageValue()<63;
	}
}
