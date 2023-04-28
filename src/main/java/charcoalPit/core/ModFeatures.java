package charcoalPit.core;

import charcoalPit.CharcoalPit;
import charcoalPit.tree.*;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.kinds.App;
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
import net.minecraft.world.level.levelgen.feature.foliageplacers.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.*;
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

@Mod.EventBusSubscriber(modid=CharcoalPit.MODID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class ModFeatures {
	
	
	public static FoliagePlacerType<DragonFoliagePlacer> DRAGON_PLACER=new FoliagePlacerType<>(DragonFoliagePlacer.CODEC);
	public static FoliagePlacerType<OliveFoliagePlacer> OLIVE_PLACER=new FoliagePlacerType<>(OliveFoliagePlacer.CODEC);
	public static FoliagePlacerType<OrangeFoliagePlacer> ORANGE_PLACER=new FoliagePlacerType<>(OrangeFoliagePlacer.CODEC);
	public static FoliagePlacerType<CedarFoliagePlacer> CEDAR_PLACER=new FoliagePlacerType<>(CedarFoliagePlacer.CODEC);
	public static FoliagePlacerType<DouglasPlacer> DOUGLAS_PLACER=new FoliagePlacerType<>(DouglasPlacer.CODEC);
	//public static FoliagePlacerType<PalmFoliagePlacer> PALM_PLACER=new FoliagePlacerType<>(PalmFoliagePlacer.CODEC);
	
	//public static TrunkPlacerType<BentTrunkPlacer> BENT_PLACER=new TrunkPlacerType<BentTrunkPlacer>(BentTrunkPlacer.CODEC);
	public static TrunkPlacerType<DoubleTrunkPlacer> DOUBLE_PLACER=new TrunkPlacerType<>(DoubleTrunkPlacer.CODEC);
	
	public static TreeDecoratorType<PodDecorator> POD_DECORATOR=new TreeDecoratorType<>(PodDecorator.CODEC);
	
	public static Holder<ConfiguredFeature<?, ?>> APPLE;
	public static Holder<ConfiguredFeature<?, ?>> CHERRY;
	public static Holder<ConfiguredFeature<?, ?>> DRAGON;
	public static Holder<ConfiguredFeature<?, ?>> CHESTNUT;
	public static Holder<ConfiguredFeature<?, ?>> OLIVE;
	public static Holder<ConfiguredFeature<?, ?>> ORANGE;
	public static Holder<ConfiguredFeature<?, ?>> ENCHANTED;
	public static Holder<ConfiguredFeature<?, ?>> MAPLE;
	public static Holder<ConfiguredFeature<?, ?>> JACARANDA;
	public static Holder<ConfiguredFeature<?, ?>> SAKURA;
	public static Holder<ConfiguredFeature<?, ?>> FORSYTHIA;
	public static Holder<ConfiguredFeature<?, ?>> CEDAR;
	public static Holder<ConfiguredFeature<?, ?>> DOUGLAS;
	public static Holder<ConfiguredFeature<?, ?>> DOUGLAS_TALL;
	public static Holder<ConfiguredFeature<?, ?>> COCONUT;
	public static Holder<ConfiguredFeature<?, ?>> BANANA;
	
	@SubscribeEvent
	public static void registerPlacers(RegistryEvent.Register<FoliagePlacerType<?>> event){
		event.getRegistry().registerAll(DRAGON_PLACER.setRegistryName("dragon_placer"),OLIVE_PLACER.setRegistryName("olive_placer"),ORANGE_PLACER.setRegistryName("orange_placer"),
				CEDAR_PLACER.setRegistryName("cedar_placer"),DOUGLAS_PLACER.setRegistryName("douglas_placer"));
	}
	@SubscribeEvent
	public static void registerDecorators(RegistryEvent.Register<TreeDecoratorType<?>> event){
		event.getRegistry().registerAll(POD_DECORATOR.setRegistryName("pod_placer"));
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
		DRAGON = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(CharcoalPit.MODID,"dragon"), new ConfiguredFeature<>(Feature.TREE,((new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider2(Blocks.JUNGLE_LOG.defaultBlockState()), new StraightTrunkPlacer(3, 0, 0), new SimpleStateProvider2(ModBlockRegistry.DragonLeaves.defaultBlockState()), new DragonFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build())));
		CHESTNUT=BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(CharcoalPit.MODID, "chestnut"),new ConfiguredFeature<>(Feature.TREE,((new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider2(Blocks.DARK_OAK_LOG.defaultBlockState()), new DoubleTrunkPlacer(5,2,0), new SimpleStateProvider2(ModBlockRegistry.ChestnutLeaves.defaultBlockState()), new DarkOakFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)), new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty()))).ignoreVines().build())));
		OLIVE=BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(CharcoalPit.MODID,"olive"), new ConfiguredFeature<>(Feature.TREE,((new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider2(Blocks.ACACIA_LOG.defaultBlockState()), new StraightTrunkPlacer(3, 0, 0), new SimpleStateProvider2(ModBlockRegistry.OliveLeaves.defaultBlockState()), new OliveFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build())));
		ORANGE = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(CharcoalPit.MODID,"orange"), new ConfiguredFeature<>(Feature.TREE,((new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider2(Blocks.JUNGLE_LOG.defaultBlockState()),new StraightTrunkPlacer(4, 2, 0), new SimpleStateProvider2(ModBlockRegistry.OrangeLeaves.defaultBlockState()), new OrangeFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build())));
		ENCHANTED=BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE,new ResourceLocation(CharcoalPit.MODID,"enchanted"),new ConfiguredFeature<>(Feature.TREE,((new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider2(Blocks.OAK_LOG.defaultBlockState()),new FancyTrunkPlacer(3,11,0),new SimpleStateProvider2(ModBlockRegistry.EnchantedLeaves.defaultBlockState()),new FancyFoliagePlacer(ConstantInt.of(2),ConstantInt.of(4),4),new TwoLayersFeatureSize(0,0,0))).ignoreVines().build())));
		MAPLE = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(CharcoalPit.MODID,"maple"), new ConfiguredFeature<>(Feature.TREE,((new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider2(Blocks.ACACIA_LOG.defaultBlockState()),new StraightTrunkPlacer(4, 2, 0), new SimpleStateProvider2(ModBlockRegistry.MapleLeaves.defaultBlockState()), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build())));
		JACARANDA = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(CharcoalPit.MODID,"jacaranda"), new ConfiguredFeature<>(Feature.TREE,((new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider2(Blocks.JUNGLE_LOG.defaultBlockState()),new StraightTrunkPlacer(4, 2, 0), new SimpleStateProvider2(ModBlockRegistry.JacarandaLeaves.defaultBlockState()), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build())));
		SAKURA = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(CharcoalPit.MODID,"sakura"), new ConfiguredFeature<>(Feature.TREE,((new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider2(Blocks.BIRCH_LOG.defaultBlockState()), new StraightTrunkPlacer(5, 2, 0), new SimpleStateProvider2(ModBlockRegistry.SakuraLeaves.defaultBlockState()), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build())));
		FORSYTHIA = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(CharcoalPit.MODID,"forsythia"), new ConfiguredFeature<>(Feature.TREE,((new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider2(Blocks.OAK_LOG.defaultBlockState()), new StraightTrunkPlacer(3, 0, 0), new SimpleStateProvider2(ModBlockRegistry.ForsythiaLeaves.defaultBlockState()), new DragonFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build())));
		CEDAR = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(CharcoalPit.MODID,"cedar"), new ConfiguredFeature<>(Feature.TREE,((new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider2(Blocks.BIRCH_LOG.defaultBlockState()), new StraightTrunkPlacer(4, 4, 0), new SimpleStateProvider2(ModBlockRegistry.CedarLeaves.defaultBlockState()), new CedarFoliagePlacer(ConstantInt.of(1),ConstantInt.of(0)), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build())));
		DOUGLAS = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(CharcoalPit.MODID,"douglas"), new ConfiguredFeature<>(Feature.TREE,((new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider2(Blocks.SPRUCE_LOG.defaultBlockState()), new StraightTrunkPlacer(10, 9, 0), new SimpleStateProvider2(ModBlockRegistry.DouglasLeaves.defaultBlockState()), new DouglasPlacer(ConstantInt.of(2),ConstantInt.of(0)), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build())));
		DOUGLAS_TALL = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(CharcoalPit.MODID,"douglas_tall"), new ConfiguredFeature<>(Feature.TREE,((new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider2(Blocks.SPRUCE_LOG.defaultBlockState()), new StraightTrunkPlacer(20, 10, 0), new SimpleStateProvider2(ModBlockRegistry.DouglasLeaves.defaultBlockState()), new DouglasPlacer(ConstantInt.of(2),ConstantInt.of(0)), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build())));
		BANANA = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(CharcoalPit.MODID,"banana"), new ConfiguredFeature<>(Feature.TREE,((new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider2(Blocks.JUNGLE_LOG.defaultBlockState()), new StraightTrunkPlacer(4, 8, 0), new SimpleStateProvider2(Blocks.JUNGLE_LEAVES.defaultBlockState()), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().decorators(ImmutableList.of(new PodDecorator(ModBlockRegistry.BanananaPod.defaultBlockState()))).build())));
		COCONUT = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(CharcoalPit.MODID,"coconut"), new ConfiguredFeature<>(Feature.TREE,((new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider2(Blocks.JUNGLE_LOG.defaultBlockState()), new StraightTrunkPlacer(4, 8, 0), new SimpleStateProvider2(Blocks.JUNGLE_LEAVES.defaultBlockState()), new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().decorators(ImmutableList.of(new PodDecorator(ModBlockRegistry.CoconutPod.defaultBlockState()))).build())));
		
		ModBlockRegistry.AppleLeaves.fruit= Items.APPLE;
		ModBlockRegistry.CherryLeaves.fruit=ModItemRegistry.Cherry;
		ModBlockRegistry.DragonLeaves.fruit=ModItemRegistry.DragonFruit;
		ModBlockRegistry.ChestnutLeaves.fruit=ModItemRegistry.ChestNut;
		ModBlockRegistry.OliveLeaves.fruit=ModItemRegistry.Olives;
		ModBlockRegistry.OrangeLeaves.fruit=ModItemRegistry.Orange;
		ModBlockRegistry.EnchantedLeaves.fruit=Items.GOLDEN_APPLE;
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
	public static class EnchantedTree extends AbstractTreeGrower{
		
		@Nullable
		@Override
		protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(Random randomIn, boolean largeHive) {
			return ENCHANTED;
		}
	}
	public static class MapleTree extends AbstractTreeGrower{
		
		@Nullable
		@Override
		protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(Random randomIn, boolean largeHive) {
			return MAPLE;
		}
	}
	public static class JacarandaTree extends AbstractTreeGrower{
		
		@Nullable
		@Override
		protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(Random randomIn, boolean largeHive) {
			return JACARANDA;
		}
	}
	public static class SakuraTree extends AbstractTreeGrower{
		
		@Nullable
		@Override
		protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(Random randomIn, boolean largeHive) {
			return SAKURA;
		}
	}
	public static class ForsythiaTree extends AbstractTreeGrower{
		
		@Nullable
		@Override
		protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(Random randomIn, boolean largeHive) {
			return FORSYTHIA;
		}
	}
	public static class RandomTree extends AbstractTreeGrower{
		@Nullable
		@Override
		protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(Random randomIn, boolean largeHive) {
			switch (randomIn.nextInt(9)){
				case 0:return APPLE;
				case 1:return CHERRY;
				case 2:return MAPLE;
				case 3:return CHESTNUT;
				case 4:return ORANGE;
				case 5:return OLIVE;
				case 6:return DRAGON;
				case 7:return BANANA;
				case 8:return COCONUT;
				default:return TreeFeatures.SWAMP_OAK;
			}
		}
	}
	public static class CedarTree extends AbstractTreeGrower{
		
		@Nullable
		@Override
		protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(Random randomIn, boolean largeHive) {
			return CEDAR;
		}
	}
	public static class DouglasTree extends AbstractTreeGrower{
		
		@Nullable
		@Override
		protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(Random randomIn, boolean largeHive) {
			return randomIn.nextInt(20)==0?DOUGLAS_TALL:DOUGLAS;
		}
	}
	public static class RandomDecorativeTree extends AbstractTreeGrower{
		
		@Nullable
		@Override
		protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(Random randomIn, boolean largeHive) {
			switch (randomIn.nextInt(5)){
				case 0:return SAKURA;
				case 1:return JACARANDA;
				case 2:return FORSYTHIA;
				case 3:return CEDAR;
				case 4:return DOUGLAS;
				default:return TreeFeatures.JUNGLE_TREE;
			}
		}
	}
	
	public static class SimpleStateProvider2 extends SimpleStateProvider{
		
		public SimpleStateProvider2(BlockState p_68801_) {
			super(p_68801_);
		}
	}
	
}
