package charcoalPit.core;

import charcoalPit.CharcoalPit;
import charcoalPit.block.*;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.HorseRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.event.RegistryEvent;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

//@EventBusSubscriber(modid=CharcoalPit.MODID, bus=Bus.MOD)
public class ModBlockRegistry {
	
	public static BlockThatch Thatch=new BlockThatch();
	public static RotatedPillarBlock LogPile=new BlockLogPile();
	public static Block WoodAsh=new BlockAsh(),CoalAsh=new BlockAsh(),SandyBrick=new Block(Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).strength(2, 6).requiresCorrectToolForDrops()),
			CokeBlock=new Block(Properties.of(Material.WOOD, MaterialColor.COLOR_BLACK).strength(5F, 6F).requiresCorrectToolForDrops()) {
		public int getFireSpreadSpeed(net.minecraft.world.level.block.state.BlockState state, net.minecraft.world.level.BlockGetter world, net.minecraft.core.BlockPos pos, net.minecraft.core.Direction face) {
			return 5;
		};
		public int getFlammability(net.minecraft.world.level.block.state.BlockState state, net.minecraft.world.level.BlockGetter world, net.minecraft.core.BlockPos pos, net.minecraft.core.Direction face) {
			return 5;
		};
	};
	public static FallingBlock Ash=new FallingBlock(Properties.of(Material.SAND, MaterialColor.COLOR_LIGHT_GRAY).strength(0.5F).sound(SoundType.SAND));
	public static SlabBlock SandySlab=new SlabBlock(Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).strength(2, 6).requiresCorrectToolForDrops());
	public static StairBlock SandyStair=new StairBlock(()->SandyBrick.defaultBlockState(), Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).strength(2, 6).requiresCorrectToolForDrops());
	public static WallBlock SandyWall=new WallBlock(Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).strength(2, 6).requiresCorrectToolForDrops());
	
	public static BlockActivePile ActiveLogPile=new BlockActivePile(false, Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2F).sound(SoundType.WOOD));
	public static BlockActivePile ActiveCoalPile=new BlockActivePile(true, Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).strength(5F, 6F).requiresCorrectToolForDrops());
	public static BlockCreosoteCollector BrickCollector=new BlockCreosoteCollector(Properties.copy(Blocks.BRICKS)), SandyCollector=new BlockCreosoteCollector(Properties.copy(SandyBrick)),
			NetherCollector=new BlockCreosoteCollector(Properties.copy(Blocks.NETHER_BRICKS)),EndCollector=new BlockCreosoteCollector(Properties.copy(Blocks.END_STONE_BRICKS));
	public static BlockCeramicPot CeramicPot=new BlockCeramicPot(MaterialColor.COLOR_ORANGE),WhitePot=new BlockCeramicPot(MaterialColor.TERRACOTTA_WHITE),
			OrangePot=new BlockCeramicPot(MaterialColor.TERRACOTTA_ORANGE),MagentaPot=new BlockCeramicPot(MaterialColor.TERRACOTTA_MAGENTA),
			LightBluePot=new BlockCeramicPot(MaterialColor.TERRACOTTA_LIGHT_BLUE),YellowPot=new BlockCeramicPot(MaterialColor.TERRACOTTA_YELLOW),
			LimePot=new BlockCeramicPot(MaterialColor.TERRACOTTA_LIGHT_GREEN),PinkPot=new BlockCeramicPot(MaterialColor.TERRACOTTA_PINK),
			GrayPot=new BlockCeramicPot(MaterialColor.TERRACOTTA_GRAY),LightGrayPot=new BlockCeramicPot(MaterialColor.TERRACOTTA_LIGHT_GRAY),
			CyanPot=new BlockCeramicPot(MaterialColor.TERRACOTTA_CYAN),PurplePot=new BlockCeramicPot(MaterialColor.TERRACOTTA_PURPLE),
			BluePot=new BlockCeramicPot(MaterialColor.TERRACOTTA_BLUE),BrownPot=new BlockCeramicPot(MaterialColor.TERRACOTTA_BROWN),
			GreenPot=new BlockCeramicPot(MaterialColor.TERRACOTTA_GREEN),RedPot=new BlockCeramicPot(MaterialColor.TERRACOTTA_RED),
			BlackPot=new BlockCeramicPot(MaterialColor.TERRACOTTA_BLACK);
	public static BlockBellows Bellows=new BlockBellows();
	
	public static BlockBarrel Barrel=new BlockBarrel();
	public static BlockMechanicalBellows MechanicalBellows=new BlockMechanicalBellows();
	
	public static BlockLeeks Leeks=new BlockLeeks(Properties.copy(Blocks.WHEAT));
	public static BlockCorn Corn=new BlockCorn(Properties.copy(Blocks.WHEAT));
	public static BlockSunflowerCrop Sunflower=new BlockSunflowerCrop(Properties.copy(Blocks.WHEAT));
	
	public static SaplingBlock AppleSapling=new SaplingBlock(new ModFeatures.AppleTree(),BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS));
	public static SaplingBlock CherrySapling=new SaplingBlock(new ModFeatures.CherryTree(), BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS));
	public static SaplingBlock DragonSapling=new SaplingBlock(new ModFeatures.DragonTree(),BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING));
	public static SaplingBlock ChestnutSapling=new SaplingBlock(new ModFeatures.ChestnutTree(),BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING));
	public static SaplingBlock OliveSapling=new SaplingBlock(new ModFeatures.OliveTree(),BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING));
	public static SaplingBlock OrangeSapling=new SaplingBlock(new ModFeatures.OrangeTree(),BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING));
	public static SaplingBlock EnchantedSapling=new SaplingBlock(new ModFeatures.EnchantedTree(),BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING));
	public static SaplingBlock MapleSapling=new SaplingBlock(new ModFeatures.MapleTree(),BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING));
	public static SaplingBlock JacarandaSapling=new SaplingBlock(new ModFeatures.JacarandaTree(),BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING));
	public static SaplingBlock SakuraSapling=new SaplingBlock(new ModFeatures.SakuraTree(),BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING));
	public static SaplingBlock ForsythiaSapling=new SaplingBlock(new ModFeatures.ForsythiaTree(),BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING));
	public static SaplingBlock RandomSapling=new SaplingBlock(new ModFeatures.RandomTree(),BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING));
	public static SaplingBlock CedarSapling=new SaplingBlock(new ModFeatures.CedarTree(),BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING));
	public static SaplingBlock DouglasSapling=new SaplingBlock(new ModFeatures.DouglasTree(),BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING));
	public static SaplingBlock DecorativeSapling=new SaplingBlock(new ModFeatures.RandomDecorativeTree(),BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING));
	
	public static BlockFruitLeaves AppleLeaves=new BlockFruitLeaves(Properties.copy(Blocks.OAK_LEAVES), null, 0.166F);
	public static BlockFruitLeaves CherryLeaves=new BlockFruitLeaves(Properties.copy(Blocks.BIRCH_LEAVES), null, 0.333F);
	public static BlockFruitLeaves DragonLeaves=new BlockFruitLeaves(Properties.copy(Blocks.BIRCH_LEAVES),null, 0.1F);
	public static BlockFruitLeaves ChestnutLeaves=new BlockFruitLeaves(Properties.copy(Blocks.DARK_OAK_LEAVES),null,0.166F);
	public static BlockFruitLeaves OliveLeaves=new BlockFruitLeaves(Properties.copy(Blocks.ACACIA_LEAVES),null,0.133F);
	public static BlockFruitLeaves OrangeLeaves=new BlockFruitLeaves(Properties.copy(Blocks.ACACIA_LEAVES),null,0.166F);
	public static BlockFruitLeaves EnchantedLeaves=new BlockFruitLeaves(Properties.copy(Blocks.OAK_LEAVES),null,0.166F);
	public static BlockFlowerLeaves MapleLeaves=new BlockFlowerLeaves(Properties.copy(Blocks.OAK_LEAVES),0.166F,Config.CycleFlowerTrees.get()?0:7);
	public static BlockFlowerLeaves JacarandaLeaves=new BlockFlowerLeaves(Properties.copy(Blocks.OAK_LEAVES),0.166F,Config.CycleFlowerTrees.get()?3:7);
	public static BlockFlowerLeaves SakuraLeaves=new BlockFlowerLeaves(Properties.copy(Blocks.OAK_LEAVES),0.166F,Config.CycleFlowerTrees.get()?3:7);
	public static BlockFlowerLeaves ForsythiaLeaves=new BlockFlowerLeaves(Properties.copy(Blocks.OAK_LEAVES),0.166F,Config.CycleFlowerTrees.get()?3:7);
	public static LeavesBlock CedarLeaves=new LeavesBlock(Properties.copy(Blocks.SPRUCE_LEAVES));
	public static LeavesBlock DouglasLeaves=new LeavesBlock(Properties.copy(Blocks.SPRUCE_LEAVES));
	
	public static BlockMapleLog MapleLog=new BlockMapleLog(Properties.of(Material.WOOD,MaterialColor.COLOR_ORANGE).sound(SoundType.WOOD).strength(2.0F));
	
	public static BlockBananaPod BanananaPod=new BlockBananaPod(Properties.copy(Blocks.COCOA));
	public static BlockCocoPod CoconutPod=new BlockCocoPod(Properties.copy(Blocks.COCOA));
	
	public static BlockGenieLight GenieLight=new BlockGenieLight();
	
	public static BlockFeedingThrough FeedingThrough =new BlockFeedingThrough(Properties.copy(Blocks.OAK_PLANKS));
	public static BlockFeedingThrough FeedingThroughBirch =new BlockFeedingThrough(Properties.copy(Blocks.OAK_PLANKS));
	public static BlockFeedingThrough FeedingThroughJungle =new BlockFeedingThrough(Properties.copy(Blocks.OAK_PLANKS));
	public static BlockFeedingThrough FeedingThroughSpruce =new BlockFeedingThrough(Properties.copy(Blocks.OAK_PLANKS));
	public static BlockFeedingThrough FeedingThroughDark =new BlockFeedingThrough(Properties.copy(Blocks.OAK_PLANKS));
	public static BlockFeedingThrough FeedingThroughAcacia =new BlockFeedingThrough(Properties.copy(Blocks.OAK_PLANKS));
	public static BlockFeedingThrough FeedingThroughCrimson =new BlockFeedingThrough(Properties.copy(Blocks.OAK_PLANKS));
	public static BlockFeedingThrough FeedingThroughWarped =new BlockFeedingThrough(Properties.copy(Blocks.OAK_PLANKS));
	
	public static BlockNestBox NestBox=new BlockNestBox(Properties.copy(Blocks.OAK_PLANKS));
	public static BlockBloomeryy Bloomeryy=new BlockBloomeryy(Properties.copy(Blocks.FURNACE));
	public static Block CharcoalBlock=new Block(Properties.copy(Blocks.COAL_BLOCK)){
		@Override
		public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
			return 5;
		}
		
		@Override
		public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
			return 5;
		}
	};
	public static BlockBloom Bloom=new BlockBloom(Properties.copy(Blocks.RAW_IRON_BLOCK));
	public static BlockBlastFurnace BlastFurnace=new BlockBlastFurnace(Properties.copy(Blocks.BLAST_FURNACE));
	public static BlockDistillery Distillery=new BlockDistillery(Properties.copy(Blocks.COPPER_BLOCK));
	public static BlockSteamPress SteamPress=new BlockSteamPress(Properties.copy(Blocks.BLAST_FURNACE));
	
	public static BlockWrathLantern WrathLantern=new BlockWrathLantern(Properties.copy(Blocks.LANTERN));
	public static BlockXPCrystal XPCrystal=new BlockXPCrystal(Properties.copy(Blocks.AMETHYST_CLUSTER));
	public static BlockDwarvenCandle DwarvenCandle=new BlockDwarvenCandle();
	
	/*public static DoorBlock BrickDoor=new DoorBlock(AbstractBlock.Properties.from(Blocks.IRON_DOOR)),
			SandyDoor=new DoorBlock(Properties.from(Blocks.IRON_DOOR)),
			NetherDoor=new DoorBlock(Properties.from(Blocks.IRON_DOOR)),
			EndDoor=new DoorBlock(Properties.from(Blocks.IRON_DOOR));*/
	
	public static BlockCreosote Creosote=new BlockCreosote();
	
	//@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(Thatch.setRegistryName("thatch"),LogPile.setRegistryName("log_pile"),
				WoodAsh.setRegistryName("wood_ash"),CoalAsh.setRegistryName("coal_ash"),
				CokeBlock.setRegistryName("coke"),Ash.setRegistryName("ash"),
				ActiveLogPile.setRegistryName("active_log_pile"),ActiveCoalPile.setRegistryName("active_coal_pile"),
				SandyBrick.setRegistryName("sandy_brick"),SandySlab.setRegistryName("sandy_slab"),SandyStair.setRegistryName("sandy_stair"),SandyWall.setRegistryName("sandy_wall"),
				Creosote.setRegistryName("creosote_oil"),BrickCollector.setRegistryName("brick_collector"),SandyCollector.setRegistryName("sandy_collector"),
				NetherCollector.setRegistryName("nether_collector"),EndCollector.setRegistryName("end_collector"),Bellows.setRegistryName("bellows"),
				Barrel.setRegistryName("barrel"),MechanicalBellows.setRegistryName("mechanical_bellows"),Leeks.setRegistryName("leeks"),Corn.setRegistryName("corn"),
				AppleLeaves.setRegistryName("apple_leaves"),AppleSapling.setRegistryName("apple_sapling"),CherrySapling.setRegistryName("cherry_sapling"),
				CherryLeaves.setRegistryName("cherry_leaves"),DragonSapling.setRegistryName("dragon_sapling"),DragonLeaves.setRegistryName("dragon_leaves"),
				ChestnutSapling.setRegistryName("chestnut_sapling"),ChestnutLeaves.setRegistryName("chestnut_leaves"),BanananaPod.setRegistryName("banana_pod"),
				Sunflower.setRegistryName("sunflower_crop"),CoconutPod.setRegistryName("coconut_pod"),OliveSapling.setRegistryName("olive_sapling"),
				OliveLeaves.setRegistryName("olive_leaves"),OrangeSapling.setRegistryName("orange_sapling"),OrangeLeaves.setRegistryName("orange_leaves"),
				GenieLight.setRegistryName("genie_light"),NestBox.setRegistryName("nest_box"),Bloomeryy.setRegistryName("bloomeryy"),CharcoalBlock.setRegistryName("charcoal_block"),
				Bloom.setRegistryName("bloom"),BlastFurnace.setRegistryName("blast_furnace"),Distillery.setRegistryName("distillery"),SteamPress.setRegistryName("steam_press"),
				WrathLantern.setRegistryName("wrath_lantern"),EnchantedLeaves.setRegistryName("enchanted_leaves"),EnchantedSapling.setRegistryName("enchanted_sapling"),
				MapleLeaves.setRegistryName("maple_leaves"),MapleSapling.setRegistryName("maple_sapling"),MapleLog.setRegistryName("maple_log"),
				JacarandaLeaves.setRegistryName("jacaranda_leaves"),JacarandaSapling.setRegistryName("jacaranda_sapling"),SakuraSapling.setRegistryName("sakura_sapling"),
				SakuraLeaves.setRegistryName("sakura_leaves"),ForsythiaLeaves.setRegistryName("forsythia_leaves"),ForsythiaSapling.setRegistryName("forsythia_sapling"),
				RandomSapling.setRegistryName("random_sapling"),CedarSapling.setRegistryName("cedar_sapling"),CedarLeaves.setRegistryName("cedar_leaves"),
				DouglasSapling.setRegistryName("douglas_sapling"),DouglasLeaves.setRegistryName("douglas_leaves"),DecorativeSapling.setRegistryName("decorative_sapling"),
				XPCrystal.setRegistryName("xp_crystal"),DwarvenCandle.setRegistryName("dwarven_candle"));
		event.getRegistry().registerAll(CeramicPot.setRegistryName("ceramic_pot"),YellowPot.setRegistryName("yellow_pot"),WhitePot.setRegistryName("white_pot"),
				RedPot.setRegistryName("red_pot"),PurplePot.setRegistryName("purple_pot"),PinkPot.setRegistryName("pink_pot"),OrangePot.setRegistryName("orange_pot"),
				MagentaPot.setRegistryName("magenta_pot"),LimePot.setRegistryName("lime_pot"),LightGrayPot.setRegistryName("light_gray_pot"),
				LightBluePot.setRegistryName("light_blue_pot"),GreenPot.setRegistryName("green_pot"),GrayPot.setRegistryName("gray_pot"),CyanPot.setRegistryName("cyan_pot"),
				BrownPot.setRegistryName("brown_pot"),BluePot.setRegistryName("blue_pot"),BlackPot.setRegistryName("black_pot"));
		event.getRegistry().registerAll(FeedingThrough.setRegistryName("feeding_through"), FeedingThroughBirch.setRegistryName("feeding_through_birch"),
				FeedingThroughJungle.setRegistryName("feeding_through_jungle"), FeedingThroughSpruce.setRegistryName("feeding_through_spruce"),
				FeedingThroughDark.setRegistryName("feeding_through_dark"), FeedingThroughAcacia.setRegistryName("feeding_through_acacia"),
				FeedingThroughCrimson.setRegistryName("feeding_through_crimson"), FeedingThroughWarped.setRegistryName("feeding_through_warped"));
	}
	
	//@SubscribeEvent
	public static void datagen(GatherDataEvent event){
		if(event.includeServer()) {
			event.getGenerator().addProvider(new BlockTagsProvider(event.getGenerator(), CharcoalPit.MODID, event.getExistingFileHelper()) {
				@Override
				protected void addTags() {
					this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(CokeBlock, ActiveCoalPile, SandyBrick, SandySlab, SandyStair, SandyWall, BrickCollector, SandyCollector,
							NetherCollector, EndCollector, MechanicalBellows, CeramicPot, YellowPot, WhitePot, RedPot, PurplePot, PinkPot, OrangePot, MagentaPot,
							LimePot, LightGrayPot, LightBluePot, GreenPot, GrayPot, CyanPot, BrownPot, BluePot, BlackPot, Bloomeryy, CharcoalBlock, Bloom, BlastFurnace,
							Distillery, SteamPress, WrathLantern, XPCrystal);
					this.tag(BlockTags.MINEABLE_WITH_AXE).add(LogPile, ActiveLogPile, Bellows, Barrel, BanananaPod, CoconutPod, NestBox,
							FeedingThrough,FeedingThroughAcacia,FeedingThroughBirch,FeedingThroughDark,FeedingThroughCrimson,FeedingThroughJungle,
							FeedingThroughWarped,FeedingThroughSpruce,MapleLog);
					this.tag(BlockTags.MINEABLE_WITH_HOE).add(Thatch, AppleLeaves, CherryLeaves, DragonLeaves, ChestnutLeaves,OrangeLeaves,OliveLeaves,EnchantedLeaves,
							MapleLeaves,JacarandaLeaves,ForsythiaLeaves,CedarLeaves,DouglasLeaves);
					this.tag(BlockTags.MINEABLE_WITH_SHOVEL).add(WoodAsh, CoalAsh, Ash);
				}
			});
		}
	}
	
}
