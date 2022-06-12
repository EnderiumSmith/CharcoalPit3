package charcoalPit.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemHandlerHelper;

public class ItemKebabs extends Item{
	
	public ItemKebabs(Item.Properties prop){
		super(prop);
	}
	
	public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
		if(stack.getCount()>1) {
			if (entityLiving instanceof Player)
				ItemHandlerHelper.giveItemToPlayer((Player) entityLiving, this.getContainerItem(stack).copy());
			return super.finishUsingItem(stack, worldIn, entityLiving);
		}
		entityLiving.eat(worldIn,stack);
		return this.getContainerItem(stack).copy();
	}
}
