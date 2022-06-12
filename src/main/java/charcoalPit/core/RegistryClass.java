package charcoalPit.core;

import charcoalPit.CharcoalPit;
import charcoalPit.entity.Airplane;
import charcoalPit.entity.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid= CharcoalPit.MODID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class RegistryClass {
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		ModBlockRegistry.registerBlocks(event);
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		ModItemRegistry.registerItems(event);
	}
	
	@SubscribeEvent
	public static void registerTileEntity(RegistryEvent.Register<BlockEntityType<?>> event) {
		ModTileRegistry.registerTileEntity(event);
	}
	
	@SubscribeEvent
	public static void dataGen(GatherDataEvent event){
		ModBlockRegistry.datagen(event);
	}
	
	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityType<?>> event){
		ModEntities.registerEntities(event);
	}
	
	@SubscribeEvent
	public static void registerEntityAttributes(EntityAttributeCreationEvent event){
		event.put(ModEntities.AIRPLANE, Airplane.createAttributes().build());
	}
	
}
