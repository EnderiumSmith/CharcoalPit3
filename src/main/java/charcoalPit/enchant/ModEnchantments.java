package charcoalPit.enchant;

import charcoalPit.core.ModItemRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.RegistryEvent;

public class ModEnchantments {
	
	public static Enchantment SILVER_BULLET=new Enchantment(Enchantment.Rarity.RARE,
			EnchantmentCategory.create("musket",x->x==ModItemRegistry.musket),
			new EquipmentSlot[]{EquipmentSlot.MAINHAND}) {
		
		@Override
		public int getMinCost(int pLevel) {
			return 25;
		}
		
		@Override
		public int getMaxCost(int pLevel) {
			return 50;
		}
		
		@Override
		protected boolean checkCompatibility(Enchantment pOther) {
			return pOther!=Enchantments.FLAMING_ARROWS&&pOther!=Enchantments.PUNCH_ARROWS;
		}
	};
	
	public static void registerEnchants(RegistryEvent.Register<Enchantment> event){
		event.getRegistry().registerAll(SILVER_BULLET.setRegistryName("silver_bullet"));
	}
}
