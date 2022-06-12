package charcoalPit.core;

import charcoalPit.CharcoalPit;
import charcoalPit.tree.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.grower.AbstractMegaTreeGrower;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.item.Items;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.GiantTrunkPlacer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import javax.annotation.Nullable;
import java.util.OptionalInt;
import java.util.Random;
import java.util.function.BiConsumer;

import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.ThreeLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.DarkOakFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.DarkOakTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

@Mod.EventBusSubscriber(modid=CharcoalPit.MODID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class ModFeatures {
	
	
	public static FoliagePlacerType<DragonFoliagePlacer> DRAGON_PLACER=new FoliagePlacerType<>(DragonFoliagePlacer.CODEC);
	public static FoliagePlacerType<OliveFoliagePlacer> OLIVE_PLACER=new FoliagePlacerType<>(OliveFoliagePlacer.CODEC);
	public static FoliagePlacerType<OrangeFoliagePlacer> ORANGE_PLACER=new FoliagePlacerType<>(OrangeFoliagePlacer.CODEC);
	//public static FoliagePlacerType<PalmFoliagePlacer> PALM_PLACER=new FoliagePlacerType<>(PalmFoliagePlacer.CODEC);
	
	//public static TrunkPlacerType<BentTrunkPlacer> BENT_PLACER=new TrunkPlacerType<BentTrunkPlacer>(BentTrunkPlacer.CODEC);
	public static TrunkPlacerType<DoubleTrunkPlacer> DOUBLE_PLACER=new TrunkPlacerType<>(DoubleTrunkPlacer.CODEC);
	
	public static Holder<ConfiguredFeature<?, ?>> APPLE;
	public static Holder<ConfiguredFeature<?, ?>> CHERRY;
	public static Holder<ConfiguredFeature<?, ?>> DRAGON;
	public static Holder<ConfiguredFeature<?, ?>> CHESTNUT;
	public static Holder<ConfiguredFeature<?, ?>> OLIVE;
	public static Holder<ConfiguredFeature<?, ?>> ORANGE;
	
	@SubscribeEvent
	public static void registerPlacers(RegistryEvent.Register<FoliagePlacerType<?>> event){
		event.getRegistry().registerAll(DRAGON_PLACER.setRegistryName("dragon_placer"),OLIVE_PLACER.setRegistryName("olive_placer"),ORANGE_PLACER.setRegistryName("orange_placer"));
	}
	
	
	@SubscribeEvent
	public static void register(FMLCommonSetupEvent event){
		
		//Registry.register(Registry.TRUNK_PLACER_TYPES, new ResourceLocation(CharcoalPit.MODID), BENT_PLACER);
		Registry.register(Registry.TRUNK_PLACER_TYPES, new ResourceLocation(CharcoalPit.MODID), DOUBLE_PLACER);
		/*
		APPLE= FeatureUtils.register("charcoal_pit:apple",Feature.TREE,(new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider2(Blocks.OAK_LOG.defaultBlockState()),new StraightTrunkPlacer(4, 2, 0), new SimpleStateProvider2(ModBlockRegistry.AppleLeaves.defaultBlockState()), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build());
		CHERRY= FeatureUtils.register("charcoal_pit:cherry",Feature.TREE,(new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider2(Blocks.BIRCH_LOG.defaultBlockState()), new StraightTrunkPlacer(5, 2, 0), new SimpleStateProvider2(ModBlockRegistry.CherryLeaves.defaultBlockState()), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build());
		DRAGON= FeatureUtils.register("charcoal_pit:dragon",Feature.TREE,(new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider2(Blocks.ACACIA_LOG.defaultBlockState()), new StraightTrunkPlacer(3, 0, 0), new SimpleStateProvider2(ModBlockRegistry.DragonLeaves.defaultBlockState()), new DragonFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build());
		CHESTNUT= FeatureUtils.register("charcoal_pit:walnut",Feature.TREE,(new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider2(Blocks.DARK_OAK_LOG.defaultBlockState()), new DoubleTrunkPlacer(5,2,0), new SimpleStateProvider2(ModBlockRegistry.ChestnutLeaves.defaultBlockState()), new DarkOakFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)), new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty()))).ignoreVines().build());
		OLIVE= FeatureUtils.register("charcoal_pit:olive",Feature.TREE,(new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider2(Blocks.ACACIA_LOG.defaultBlockState()), new StraightTrunkPlacer(3, 0, 0), new SimpleStateProvider2(ModBlockRegistry.OliveLeaves.defaultBlockState()), new OliveFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build());
		ORANGE= FeatureUtils.register("charcoal_pit:orange",Feature.TREE,(new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider2(Blocks.JUNGLE_LOG.defaultBlockState()),new StraightTrunkPlacer(4, 2, 0), new SimpleStateProvider2(ModBlockRegistry.OrangeLeaves.defaultBlockState()), new OrangeFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build());
		*/
		APPLE = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(CharcoalPit.MODID,"apple"), new ConfiguredFeature<>(Feature.TREE,((new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider2(Blocks.OAK_LOG.defaultBlockState()),new StraightTrunkPlacer(4, 2, 0), new SimpleStateProvider2(ModBlockRegistry.AppleLeaves.defaultBlockState()), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build())));
		CHERRY = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(CharcoalPit.MODID,"cherry"), new ConfiguredFeature<>(Feature.TREE,((new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider2(Blocks.BIRCH_LOG.defaultBlockState()), new StraightTrunkPlacer(5, 2, 0), new SimpleStateProvider2(ModBlockRegistry.CherryLeaves.defaultBlockState()), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build())));
		DRAGON = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(CharcoalPit.MODID,"dragon"), new ConfiguredFeature<>(Feature.TREE,((new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider2(Blocks.ACACIA_LOG.defaultBlockState()), new StraightTrunkPlacer(3, 0, 0), new SimpleStateProvider2(ModBlockRegistry.DragonLeaves.defaultBlockState()), new DragonFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build())));
		CHESTNUT=BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(CharcoalPit.MODID, "chestnut"),new ConfiguredFeature<>(Feature.TREE,((new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider2(Blocks.DARK_OAK_LOG.defaultBlockState()), new DoubleTrunkPlacer(5,2,0), new SimpleStateProvider2(ModBlockRegistry.ChestnutLeaves.defaultBlockState()), new DarkOakFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)), new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty()))).ignoreVines().build())));
		OLIVE=BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(CharcoalPit.MODID,"olive"), new ConfiguredFeature<>(Feature.TREE,((new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider2(Blocks.ACACIA_LOG.defaultBlockState()), new StraightTrunkPlacer(3, 0, 0), new SimpleStateProvider2(ModBlockRegistry.OliveLeaves.defaultBlockState()), new OliveFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build())));
		ORANGE = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(CharcoalPit.MODID,"orange"), new ConfiguredFeature<>(Feature.TREE,((new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider2(Blocks.JUNGLE_LOG.defaultBlockState()),new StraightTrunkPlacer(4, 2, 0), new SimpleStateProvider2(ModBlockRegistry.OrangeLeaves.defaultBlockState()), new OrangeFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build())));
		
		ModBlockRegistry.AppleLeaves.fruit= Items.APPLE;
		ModBlockRegistry.CherryLeaves.fruit=ModItemRegistry.Cherry;
		ModBlockRegistry.DragonLeaves.fruit=ModItemRegistry.DragonFruit;
		ModBlockRegistry.ChestnutLeaves.fruit=ModItemRegistry.ChestNut;
		ModBlockRegistry.OliveLeaves.fruit=ModItemRegistry.Olives;
		ModBlockRegistry.OrangeLeaves.fruit=ModItemRegistry.Orange;
	}
	
	public static class AppleTree extends AbstractTreeGrower{
		
		@Nullable
		@Override
		protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(Random randomIn, boolean largeHive) {
			return APPLE;
		}
	}
	public static class CherryTree extends AbstractTreeGrower{
		
		@Nullable
		@Override
		protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(Random randomIn, boolean largeHive) {
			return CHERRY;
		}
	}
	public static class DragonTree extends AbstractTreeGrower{
		
		@Nullable
		@Override
		protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(Random randomIn, boolean largeHive) {
			return DRAGON;
		}
	}
	public static class OliveTree extends AbstractTreeGrower{
		
		@Nullable
		@Override
		protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(Random randomIn, boolean largeHive) {
			return OLIVE;
		}
	}
	public static class OrangeTree extends AbstractTreeGrower{
		
		@Nullable
		@Override
		protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(Random randomIn, boolean largeHive) {
			return ORANGE;
		}
	}
	public static class ChestnutTree extends AbstractMegaTreeGrower {
		
		@Nullable
		@Override
		protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(Random randomIn, boolean largeHive) {
			return null;
		}
		
		@Nullable
		@Override
		protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredMegaFeature(Random rand) {
			return CHESTNUT;
		}
	}
	
	public static class SimpleStateProvider2 extends SimpleStateProvider{
		
		public SimpleStateProvider2(BlockState p_68801_) {
			super(p_68801_);
		}
	}
	
}
