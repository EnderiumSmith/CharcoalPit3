package charcoalPit.fluid;

import charcoalPit.CharcoalPit;
import charcoalPit.core.ModBlockRegistry;
import charcoalPit.core.ModItemRegistry;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fluids.ForgeFlowingFluid.Properties;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import java.util.function.BiFunction;

import net.minecraftforge.fluids.FluidAttributes.Builder;

@EventBusSubscriber(modid=CharcoalPit.MODID, bus=Bus.MOD)
public class ModFluidRegistry {
	
	public static final ResourceLocation creosote_still=new ResourceLocation(CharcoalPit.MODID, "block/creosote_still"),
			creosote_flowing=new ResourceLocation(CharcoalPit.MODID, "block/creosote_flow");
	public static ForgeFlowingFluid CreosoteStill, CreosoteFlowing;
	public static ForgeFlowingFluid.Properties Creosote;
	
	public static final ResourceLocation alcohol_still=new ResourceLocation("minecraft:block/water_still"),
			alcohol_flowing=new ResourceLocation("minecraft:block/water_flow");
	public static ForgeFlowingFluid AlcoholStill, AlcoholFlowing;
	public static ForgeFlowingFluid.Properties Alcohol;

	public static final ResourceLocation vinegar_still=new ResourceLocation("minecraft:block/water_still"),
			vinegar_flowing=new ResourceLocation("minecraft:block/water_flow");
	public static ForgeFlowingFluid VinegarStill, VinegarFlowing;
	public static ForgeFlowingFluid.Properties Vinegar;
	
	public static ForgeFlowingFluid CausticLyeStill;
	public static ForgeFlowingFluid.Properties CausticLye;
	
	public static ForgeFlowingFluid OliveOilStill;
	public static ForgeFlowingFluid.Properties OliveOil;
	
	public static ForgeFlowingFluid WalnutOilStill;
	public static ForgeFlowingFluid.Properties WalnutOil;
	
	public static ForgeFlowingFluid SunflowerOilStill;
	public static ForgeFlowingFluid.Properties SunflowerOil;
	
	public static ForgeFlowingFluid BioDieselStill;
	public static ForgeFlowingFluid.Properties BioDiesel;
	
	public static ForgeFlowingFluid EthanolStill;
	public static ForgeFlowingFluid.Properties Ethanol;
	
	public static ForgeFlowingFluid EthoxideStill;
	public static ForgeFlowingFluid.Properties Ethoxide;
	
	public static ForgeFlowingFluid SeedOilStill;
	public static ForgeFlowingFluid.Properties SeedOil;
	
	@SubscribeEvent
	public static void registerFluids(RegistryEvent.Register<Fluid> event) {
		Creosote=new Properties(()->CreosoteStill, ()->CreosoteFlowing, FluidAttributes.builder(creosote_still, creosote_flowing).density(800).viscosity(2000))
				.bucket(()->ModItemRegistry.CreosoteBucket).block(()->ModBlockRegistry.Creosote);
		CreosoteStill=new ForgeFlowingFluid.Source(Creosote);
		CreosoteStill.setRegistryName("creosote_still");
		CreosoteFlowing=new ForgeFlowingFluid.Flowing(Creosote);
		CreosoteFlowing.setRegistryName("creosote_flowing");
		
		Alcohol=new Properties(()->AlcoholStill, ()->AlcoholFlowing, AlcoholProperties.builder(alcohol_still, alcohol_flowing).density(950).color(0xFFFFFF));
		AlcoholStill=new ForgeFlowingFluid.Source(Alcohol);
		AlcoholFlowing=new ForgeFlowingFluid.Flowing(Alcohol);
		AlcoholStill.setRegistryName("alcohol_still");
		AlcoholFlowing.setRegistryName("alcohol_flowing");

		Vinegar=new Properties(()->VinegarStill,()->VinegarFlowing, FluidAttributes.builder(vinegar_still,vinegar_flowing).color(0xFFCEB301)).bucket(()->ModItemRegistry.VinegarBucket);
		VinegarStill=new ForgeFlowingFluid.Source(Vinegar);
		VinegarFlowing=new ForgeFlowingFluid.Flowing(Vinegar);
		VinegarStill.setRegistryName("vinegar_still");
		VinegarFlowing.setRegistryName("vinegar_flowing");
		
		CausticLye=new Properties(()->CausticLyeStill,()->null,FluidAttributes.builder(vinegar_still,vinegar_flowing));
		CausticLyeStill=new ForgeFlowingFluid.Source(CausticLye);
		CausticLyeStill.setRegistryName("caustic_lye");
		
		OliveOil=new Properties(()->OliveOilStill,()->null,FluidAttributes.builder(vinegar_still,vinegar_flowing).color(0x6e750e).density(900)).bucket(()->ModItemRegistry.OliveOilBucket);
		OliveOilStill=new ForgeFlowingFluid.Source(OliveOil);
		OliveOilStill.setRegistryName("olive_oil");
		
		WalnutOil=new Properties(()->WalnutOilStill,()->null,FluidAttributes.builder(vinegar_still,vinegar_flowing).color(0xFDAA48).density(900)).bucket(()->ModItemRegistry.WalnutOilBucket);
		WalnutOilStill=new ForgeFlowingFluid.Source(WalnutOil);
		WalnutOilStill.setRegistryName("walnut_oil");
		
		SunflowerOil=new Properties(()->SunflowerOilStill,()->null,FluidAttributes.builder(vinegar_still,vinegar_flowing).color(0xDBB40C).density(900)).bucket(()->ModItemRegistry.SunflowerOilBucket);
		SunflowerOilStill=new ForgeFlowingFluid.Source(SunflowerOil);
		SunflowerOilStill.setRegistryName("sunflower_oil");
		
		BioDiesel=new Properties(()->BioDieselStill,()->null,FluidAttributes.builder(vinegar_still,vinegar_flowing).color(0xf97306).density(900)).bucket(()->ModItemRegistry.BioDieselBucket);
		BioDieselStill=new ForgeFlowingFluid.Source(BioDiesel);
		BioDieselStill.setRegistryName("bio_diesel");
		
		Ethanol=new Properties(()->EthanolStill,()->null,FluidAttributes.builder(vinegar_still,vinegar_flowing).color(0xe6daa6).density(790)).bucket(()->ModItemRegistry.EthanolBucket);
		EthanolStill=new ForgeFlowingFluid.Source(Ethanol);
		EthanolStill.setRegistryName("ethanol");
		
		Ethoxide=new Properties(()->EthoxideStill,()->null,FluidAttributes.builder(vinegar_still,vinegar_flowing).color(0x95d0fc).density(800)).bucket(()->ModItemRegistry.EthoxideBucket);
		EthoxideStill=new ForgeFlowingFluid.Source(Ethoxide);
		EthoxideStill.setRegistryName("ethoxide");
		
		SeedOil=new Properties(()->SeedOilStill,()->null,FluidAttributes.builder(vinegar_still,vinegar_flowing).color(0xc7fdb5).density(900)).bucket(()->ModItemRegistry.SeedOilBucket);
		SeedOilStill=new ForgeFlowingFluid.Source(SeedOil);
		SeedOilStill.setRegistryName("seed_oil");

		event.getRegistry().registerAll(CreosoteStill,CreosoteFlowing,AlcoholStill,AlcoholFlowing,VinegarStill,VinegarFlowing,CausticLyeStill,OliveOilStill,
				WalnutOilStill,SunflowerOilStill,BioDieselStill,EthanolStill,EthoxideStill,SeedOilStill);
	}

	public static class AlcoholProperties extends FluidAttributes{
		AlcoholProperties(Builder builder, Fluid fluid){super(builder, fluid);}

		@Override
		public int getColor(FluidStack stack) {
			if(stack.hasTag()&&stack.getTag().contains("CustomPotionColor")){
				return stack.getTag().getInt("CustomPotionColor")+0xFF000000;
			}
			return 0xFFFFFFFF;
		}
		public static Builder builder(ResourceLocation stillTexture, ResourceLocation flowingTexture) {
			return new Builder2(stillTexture, flowingTexture, AlcoholProperties::new);
		}
		public  static class Builder2 extends Builder{
			public Builder2(ResourceLocation stillTexture, ResourceLocation flowingTexture, BiFunction<Builder,Fluid,FluidAttributes> factory) {
				super(stillTexture,flowingTexture,factory);
			}
		}
	}
	
}
