package charcoalPit.core;

import charcoalPit.CharcoalPit;
import charcoalPit.recipe.*;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid=CharcoalPit.MODID, bus=Bus.MOD)
public class ModRecipeRegistry {
	
	@SubscribeEvent
	public static void registerRecipeType(RegistryEvent.Register<RecipeSerializer<?>> event) {
		event.getRegistry().registerAll(OreKilnRecipe.SERIALIZER.setRegistryName("orekiln"), BarrelRecipe.SERIALIZER.setRegistryName("barrel"),
				SquisherRecipe.SERIALIZER.setRegistryName("squish"),AlloySmeltRecipe.SERIALIZER.setRegistryName("alloy"),DistilleryRecipe.SERIALIZER.setRegistryName("distill"),
				MusketLoadingRecipe.SERIALIZER.setRegistryName("musket_loading"),BloomingRecipe.SERIALIZER.setRegistryName("blooming"),TuyereBlastingRecipe.SERIALIZER.setRegistryName("hot_blasting"));
	}
	
}
