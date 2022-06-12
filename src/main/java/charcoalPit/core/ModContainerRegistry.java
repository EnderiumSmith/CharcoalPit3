package charcoalPit.core;

import charcoalPit.CharcoalPit;
import charcoalPit.gui.*;
import net.minecraft.client.Minecraft;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;


@EventBusSubscriber(modid=CharcoalPit.MODID, bus=Bus.MOD)
public class ModContainerRegistry {
	
	public static MenuType<CeramicPotContainer> CeramicPot=IForgeMenuType.create((id,inv,data)->{
		return new CeramicPotContainer(id,data.readBlockPos(),inv);
	});
	/*public static ContainerType<ClayPotContainer> ClayPot=IForgeContainerType.create((id,inv,data)->{
		return new ClayPotContainer(id, data.readBlockPos(), inv);
	});*/
	public static MenuType<ClayPotContainer2> ClayPot=IForgeMenuType.create((id,inv,data)->{
		return new ClayPotContainer2(id, inv, data.readByte(), null);
	});
	public static MenuType<BarrelContainer> Barrel=IForgeMenuType.create((id,inv,data)->{
		return new BarrelContainer(id, data.readBlockPos(), inv);
	});
	public static MenuType<BloomeryContainer> Bloomery=IForgeMenuType.create((id,inv,data)->{
		return new BloomeryContainer(id,data.readBlockPos(),inv);
	});
	public static MenuType<BlastFurnaceContainer> BlastFurnace=IForgeMenuType.create((id,inv,data)->{
		return new BlastFurnaceContainer(id,data.readBlockPos(),inv);
	});
	public static MenuType<DistilleryContainer> Distillery=IForgeMenuType.create((id,inv,data)->{
		return new DistilleryContainer(id,data.readBlockPos(),inv);
	});
	public static MenuType<SteamPressContainer> SteamPress=IForgeMenuType.create((id,inv,data)->{
		return new SteamPressContainer(id,data.readBlockPos(),inv);
	});
	@SubscribeEvent
	public static void registerContainers(RegistryEvent.Register<MenuType<?>> event) {
		event.getRegistry().registerAll(CeramicPot.setRegistryName("ceramic_pot"),ClayPot.setRegistryName("clay_pot"),Barrel.setRegistryName("barrel"),
				Bloomery.setRegistryName("bloomery"),BlastFurnace.setRegistryName("blast_furnace"),Distillery.setRegistryName("distillery"),
				SteamPress.setRegistryName("steam_press"));
	}
	
	
}
