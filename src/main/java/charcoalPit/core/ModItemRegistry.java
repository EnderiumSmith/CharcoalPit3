package charcoalPit.core;

import charcoalPit.fluid.ModFluidRegistry;
import charcoalPit.item.*;
import charcoalPit.item.tool.ModArmorTiers;
import charcoalPit.item.tool.ModTiers;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.data.ForgeItemTagsProvider;
import net.minecraftforge.event.RegistryEvent;

import net.minecraft.world.food.FoodProperties;
import org.jetbrains.annotations.Nullable;

import java.util.List;

//@EventBusSubscriber(modid=CharcoalPit.MODID, bus=Bus.MOD)
public class ModItemRegistry {
	
	public static CreativeModeTab CHARCOAL_PIT=new CreativeModeTab("charcoal_pit") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(Items.CHARCOAL);
		}
	};
	public static CreativeModeTab CHARCOAL_PIT_FOODS=new CreativeModeTab("charcoal_pit_foods") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(ModItemRegistry.Kebabs);
		}
	};

	public static BlockItemFuel Thatch=buildBlockItem(ModBlockRegistry.Thatch,200),LogPile=buildBlockItem(ModBlockRegistry.LogPile,3000),CokeBlock=buildBlockItem(ModBlockRegistry.CokeBlock,32000);
	public static BlockItem WoodAsh=buildBlockItem(ModBlockRegistry.WoodAsh),CoalAsh=buildBlockItem(ModBlockRegistry.CoalAsh),AshBlock=buildBlockItem(ModBlockRegistry.Ash),
			SandyBrick=buildBlockItem(ModBlockRegistry.SandyBrick),SandySlab=buildBlockItem(ModBlockRegistry.SandySlab),SandyStair=buildBlockItem(ModBlockRegistry.SandyStair),SandyWall=buildBlockItem(ModBlockRegistry.SandyWall);
	
	public static ItemFuel Straw=buildItem(CHARCOAL_PIT,50),Coke=buildItem(CHARCOAL_PIT,3200);
	public static Item Ash=buildItem(CHARCOAL_PIT),Aeternalis=new ItemAeternalis();
	public static BoneMealItem Fertilizer=new BoneMealItem(new Item.Properties().tab(CHARCOAL_PIT));
	public static ItemFireStarter FireStarter=new ItemFireStarter();
	public static Item SandyBrickItem=buildItem(CHARCOAL_PIT),NetherBrickItem=buildItem(CHARCOAL_PIT),UnfireSandyBrick=buildItem(CHARCOAL_PIT),UnfiredBrick=buildItem(CHARCOAL_PIT);
	
	public static BlockItem BrickCollector=buildBlockItem(ModBlockRegistry.BrickCollector,CHARCOAL_PIT),
			SandyCollector=buildBlockItem(ModBlockRegistry.SandyCollector,CHARCOAL_PIT),
			NetherCollector=buildBlockItem(ModBlockRegistry.NetherCollector,CHARCOAL_PIT),
			EndCollector=buildBlockItem(ModBlockRegistry.EndCollector,CHARCOAL_PIT);
	public static ItemBlockCeramicPot CeramicPot=buildBlockItemP(ModBlockRegistry.CeramicPot),WhitePot=buildBlockItemP(ModBlockRegistry.WhitePot),
			OrangePot=buildBlockItemP(ModBlockRegistry.OrangePot),MagentaPot=buildBlockItemP(ModBlockRegistry.MagentaPot),
			LightBluePot=buildBlockItemP(ModBlockRegistry.LightBluePot),YellowPot=buildBlockItemP(ModBlockRegistry.YellowPot),
			LimePot=buildBlockItemP(ModBlockRegistry.LimePot),PinkPot=buildBlockItemP(ModBlockRegistry.PinkPot),
			GrayPot=buildBlockItemP(ModBlockRegistry.GrayPot),LightGrayPot=buildBlockItemP(ModBlockRegistry.LightGrayPot),
			CyanPot=buildBlockItemP(ModBlockRegistry.CyanPot),PurplePot=buildBlockItemP(ModBlockRegistry.PurplePot),
			BluePot=buildBlockItemP(ModBlockRegistry.BluePot),BrownPot=buildBlockItemP(ModBlockRegistry.BrownPot),
			GreenPot=buildBlockItemP(ModBlockRegistry.GreenPot),RedPot=buildBlockItemP(ModBlockRegistry.RedPot),
			BlackPot=buildBlockItemP(ModBlockRegistry.BlackPot);
	//public static ItemClayPot ClayPot=new ItemClayPot();
	public static Item ClayPot=new Item(new Item.Properties().tab(CHARCOAL_PIT).stacksTo(16));//no alloy
	//public static ItemCrackedPot CrackedPot=new ItemCrackedPot();
	
	public static ItemBarrel Barrel=new ItemBarrel(ModBlockRegistry.Barrel,new Item.Properties().tab(CHARCOAL_PIT));
	
	public static BucketItem CreosoteBucket=new BucketItem(()->ModFluidRegistry.CreosoteStill, new Item.Properties().tab(CHARCOAL_PIT).stacksTo(1).craftRemainder(Items.BUCKET));
	public static ItemAlcoholBottle AlcoholBottle=new ItemAlcoholBottle();
	public static BucketItem VinegarBucket=new BucketItem(()->ModFluidRegistry.VinegarStill, new Item.Properties().tab(CHARCOAL_PIT).stacksTo(1).craftRemainder(Items.BUCKET));
	public static Item VinegarBottle=new Item(new Item.Properties().tab(CHARCOAL_PIT).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE));
	public static Item Cheese=new Item(new Item.Properties().tab(CHARCOAL_PIT_FOODS).food(new FoodProperties.Builder().nutrition(5).saturationMod(1.2F).build()));
	public static BlockItem Bellows=buildBlockItem(ModBlockRegistry.Bellows);
	public static BlockItem MechanicalBeellows=buildBlockItem(ModBlockRegistry.MechanicalBellows);
	public static ItemKebabs Kebabs=new ItemKebabs(new Item.Properties().tab(CHARCOAL_PIT_FOODS).food(new FoodProperties.Builder().nutrition(5).saturationMod(1.6F).meat().build()).craftRemainder(Items.STICK));
	public static ItemNameBlockItem Leek=new ItemNameBlockItem(ModBlockRegistry.Leeks,new Item.Properties().tab(CHARCOAL_PIT_FOODS).food(new FoodProperties.Builder().nutrition(1).saturationMod(0.6F).fast().build()));
	public static ItemKebabs FarfetchStew=new ItemKebabs(new Item.Properties().tab(CHARCOAL_PIT_FOODS).food(new FoodProperties.Builder().nutrition(8).saturationMod(1.6F).meat().build()).craftRemainder(Items.BOWL));
	public static Item Calamari=new Item(new Item.Properties().tab(CHARCOAL_PIT_FOODS).food(new FoodProperties.Builder().nutrition(1).saturationMod(0.2F).meat().build()));
	public static Item CookedCalamri=new Item(new Item.Properties().tab(CHARCOAL_PIT_FOODS).food(new FoodProperties.Builder().nutrition(4).saturationMod(1.2F).meat().build()));
	//public static Item CookedEgg=new Item(new Item.Properties().tab(CHARCOAL_PIT_FOODS).food(new FoodProperties.Builder().nutrition(3).saturationMod(1.2F).meat().build()));
	public static ItemNameBlockItem Corn=new ItemNameBlockItem(ModBlockRegistry.Corn,new Item.Properties().tab(CHARCOAL_PIT_FOODS).food(new FoodProperties.Builder().nutrition(3).saturationMod(1.2F).build()));
	public static Item PopCorn=new Item(new Item.Properties().tab(CHARCOAL_PIT_FOODS).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.6F).fast().build()));
	public static ItemKebabs CornStew=new ItemKebabs(new Item.Properties().tab(CHARCOAL_PIT_FOODS).food(new FoodProperties.Builder().nutrition(7).saturationMod(1.6F).build()).craftRemainder(Items.BOWL));
	public static Item Sushi=new Item(new Item.Properties().tab(CHARCOAL_PIT_FOODS).food(new FoodProperties.Builder().nutrition(6).saturationMod(1.2F).meat().build()));
	public static Item Fugu=new Item(new Item.Properties().tab(CHARCOAL_PIT_FOODS).food(new FoodProperties.Builder().nutrition(5).saturationMod(2.4F).meat().effect(()->new MobEffectInstance(MobEffects.POISON,1200,3),0.02F).build()));
	public static Item Cherry=new Item(new Item.Properties().tab(CHARCOAL_PIT_FOODS).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.6F).fast().build()));
	public static Item DragonFruit=new Item(new Item.Properties().tab(CHARCOAL_PIT_FOODS).food(new FoodProperties.Builder().nutrition(6).saturationMod(1.2F).alwaysEat().effect(()->new MobEffectInstance(MobEffects.FIRE_RESISTANCE,20*10),1F).build()));
	public static Item ChestNut=new Item(new Item.Properties().tab(CHARCOAL_PIT_FOODS).food(new FoodProperties.Builder().nutrition(3).saturationMod(1.2F).build()));
	//public static Item CookedChestNut=new Item(new Item.Properties().tab(CHARCOAL_PIT_FOODS).food(new FoodProperties.Builder().nutrition(4).saturationMod(1.2F).build()));
	public static ItemNameBlockItem Bananana=new ItemNameBlockItem(ModBlockRegistry.BanananaPod,new Item.Properties().tab(CHARCOAL_PIT_FOODS).food(new FoodProperties.Builder().nutrition(5).saturationMod(0.6F).build()));
	//public static Item Bananana=new Item(new Item.Properties().tab(CHARCOAL_PIT_FOODS).food(new FoodProperties.Builder().nutrition(5).saturationMod(0.6F).build()));
	public static ItemNameBlockItem Cococonut=new ItemNameBlockItem(ModBlockRegistry.CoconutPod,new Item.Properties().tab(CHARCOAL_PIT_FOODS).food(new FoodProperties.Builder().nutrition(4).saturationMod(1.2F).build()).craftRemainder(Items.BOWL));
	public static Item ChocoPoweder=new Item(new Item.Properties().tab(CHARCOAL_PIT_FOODS));
	public static Item Chocolate=new Item(new Item.Properties().tab(CHARCOAL_PIT_FOODS).food(new FoodProperties.Builder().nutrition(4).saturationMod(0.2F).fast().build()));
	public static ItemKebabs Dango=new ItemKebabs(new Item.Properties().tab(CHARCOAL_PIT_FOODS).food(new FoodProperties.Builder().nutrition(6).saturationMod(1.2F).build()).craftRemainder(Items.STICK));
	public static ItemNameBlockItem SunflowerSeeds=new ItemNameBlockItem(ModBlockRegistry.Sunflower,new Item.Properties().tab(CHARCOAL_PIT_FOODS).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.6F).fast().build()));
	public static ItemMortarPestle mortarPestle=new ItemMortarPestle();
	public static Item Flour=buildItem(CHARCOAL_PIT_FOODS);
	public static Item CornFlour=buildItem(CHARCOAL_PIT_FOODS);
	public static Item Croissant=new Item(new Item.Properties().tab(CHARCOAL_PIT_FOODS).food(new FoodProperties.Builder().nutrition(6).saturationMod(1.2F).build()));
	public static Item Olives=buildItem(CHARCOAL_PIT_FOODS);
	public static Item Pickled_Olives=new Item(new Item.Properties().tab(CHARCOAL_PIT_FOODS).food(new FoodProperties.Builder().nutrition(4).saturationMod(1.2F).build()));
	public static Item Orange=new Item(new Item.Properties().tab(CHARCOAL_PIT_FOODS).food(new FoodProperties.Builder().nutrition(4).saturationMod(0.6F).build()));
	public static ItemKebabs SerinanStew=new ItemKebabs(new Item.Properties().tab(CHARCOAL_PIT_FOODS).food(new FoodProperties.Builder().nutrition(10).saturationMod(1.6F).meat().build()).craftRemainder(Items.BOWL));
	public static ItemKebabs BunnyStew=new ItemKebabs(new Item.Properties().tab(CHARCOAL_PIT_FOODS).food(new FoodProperties.Builder().nutrition(8).saturationMod(1.6F).meat().build()).craftRemainder(Items.BOWL));
	
	public static BlockItem AppleSapling=new BlockItem(ModBlockRegistry.AppleSapling,new Item.Properties().tab(CHARCOAL_PIT));
	public static BlockItem CherrySapling=new BlockItem(ModBlockRegistry.CherrySapling,new Item.Properties().tab(CHARCOAL_PIT));
	public static BlockItem DragonSapling=new BlockItem(ModBlockRegistry.DragonSapling, new Item.Properties().tab(CHARCOAL_PIT));
	public static BlockItem ChestnutSapling=new BlockItem(ModBlockRegistry.ChestnutSapling, new Item.Properties().tab(CHARCOAL_PIT));
	public static BlockItem OliveSapling=new BlockItem(ModBlockRegistry.OliveSapling,new Item.Properties().tab(CHARCOAL_PIT));
	public static BlockItem OrangeSapling=new BlockItem(ModBlockRegistry.OrangeSapling,new Item.Properties().tab(CHARCOAL_PIT));
	public static BlockItem EnchantedSapling=new BlockItem(ModBlockRegistry.EnchantedSapling,new Item.Properties().tab(CHARCOAL_PIT).rarity(Rarity.EPIC)){
		@Override
		public boolean isFoil(ItemStack pStack) {
			return true;
		}
		
	};
	public static BlockItem MapleSapling=new BlockItem(ModBlockRegistry.MapleSapling,new Item.Properties().tab(CHARCOAL_PIT));
	public static BlockItem JacarandaSapling=new BlockItem(ModBlockRegistry.JacarandaSapling,new Item.Properties().tab(CHARCOAL_PIT));
	public static BlockItem SakuraSapling=new BlockItem(ModBlockRegistry.SakuraSapling,new Item.Properties().tab(CHARCOAL_PIT));
	public static BlockItem ForsythiaSapling=new BlockItem(ModBlockRegistry.ForsythiaSapling,new Item.Properties().tab(CHARCOAL_PIT));
	public static BlockItem RandomSapling=new BlockItem(ModBlockRegistry.RandomSapling,new Item.Properties().tab(CHARCOAL_PIT).rarity(Rarity.UNCOMMON));
	public static BlockItem CedarSapling=new BlockItem(ModBlockRegistry.CedarSapling,new Item.Properties().tab(CHARCOAL_PIT));
	public static BlockItem DouglasSapling=new BlockItem(ModBlockRegistry.DouglasSapling,new Item.Properties().tab(CHARCOAL_PIT));
	public static BlockItem DecorativeSapling=new BlockItem(ModBlockRegistry.DecorativeSapling,new Item.Properties().tab(CHARCOAL_PIT));
	
	public static ItemBlockLeaves AppleLeaves=new ItemBlockLeaves(ModBlockRegistry.AppleLeaves,new Item.Properties().tab(CHARCOAL_PIT));
	public static ItemBlockLeaves CherryLeaves=new ItemBlockLeaves(ModBlockRegistry.CherryLeaves,new Item.Properties().tab(CHARCOAL_PIT));
	public static ItemBlockLeaves DragonLeaves=new ItemBlockLeaves(ModBlockRegistry.DragonLeaves,new Item.Properties().tab(CHARCOAL_PIT));
	public static ItemBlockLeaves ChestnutLeaves=new ItemBlockLeaves(ModBlockRegistry.ChestnutLeaves,new Item.Properties().tab(CHARCOAL_PIT));
	public static ItemBlockLeaves OliveLeaves=new ItemBlockLeaves(ModBlockRegistry.OliveLeaves,new Item.Properties().tab(CHARCOAL_PIT));
	public static ItemBlockLeaves OrangeLeaves=new ItemBlockLeaves(ModBlockRegistry.OrangeLeaves,new Item.Properties().tab(CHARCOAL_PIT));
	public static ItemBlockLeaves EnchantedLeaves=new ItemBlockLeaves(ModBlockRegistry.EnchantedLeaves,new Item.Properties().tab(CHARCOAL_PIT));
	public static ItemBlockFlowerLeaves MapleLeaves=new ItemBlockFlowerLeaves(ModBlockRegistry.MapleLeaves,new Item.Properties().tab(CHARCOAL_PIT));
	public static ItemBlockFlowerLeaves JacarandaLeaves=new ItemBlockFlowerLeaves(ModBlockRegistry.JacarandaLeaves,new Item.Properties().tab(CHARCOAL_PIT));
	public static ItemBlockFlowerLeaves SakuraLeaves=new ItemBlockFlowerLeaves(ModBlockRegistry.SakuraLeaves,new Item.Properties().tab(CHARCOAL_PIT));
	public static ItemBlockFlowerLeaves ForsythiaLeaves=new ItemBlockFlowerLeaves(ModBlockRegistry.ForsythiaLeaves,new Item.Properties().tab(CHARCOAL_PIT));
	public static BlockItem CedarLeaves=buildBlockItem(ModBlockRegistry.CedarLeaves);
	public static BlockItem DouglasLeaves=buildBlockItem(ModBlockRegistry.DouglasLeaves);
	
	public static ItemOilLamp OilLamp=new ItemOilLamp(ModBlockRegistry.GenieLight,new Item.Properties().tab(CHARCOAL_PIT).stacksTo(1));
	
	public static BucketItem OliveOilBucket=new BucketItem(()->ModFluidRegistry.OliveOilStill, new Item.Properties().tab(CHARCOAL_PIT).stacksTo(1).craftRemainder(Items.BUCKET));
	public static BucketItem WalnutOilBucket=new BucketItem(()->ModFluidRegistry.WalnutOilStill, new Item.Properties().tab(CHARCOAL_PIT).stacksTo(1).craftRemainder(Items.BUCKET));
	public static BucketItem SunflowerOilBucket=new BucketItem(()->ModFluidRegistry.SunflowerOilStill, new Item.Properties().tab(CHARCOAL_PIT).stacksTo(1).craftRemainder(Items.BUCKET));
	public static BucketItem BioDieselBucket=new BucketItem(()-> ModFluidRegistry.BioDieselStill,new Item.Properties().tab(CHARCOAL_PIT).stacksTo(1).craftRemainder(Items.BUCKET));
	public static BucketItem EthanolBucket=new BucketItem(()->ModFluidRegistry.EthanolStill,new Item.Properties().tab(CHARCOAL_PIT).stacksTo(1).craftRemainder(Items.BUCKET));
	public static BucketItem EthoxideBucket=new BucketItem(()->ModFluidRegistry.EthoxideStill,new Item.Properties().tab(CHARCOAL_PIT).stacksTo(1).craftRemainder(Items.BUCKET));
	public static BucketItem SeedOilBucket=new BucketItem(()->ModFluidRegistry.SeedOilStill,new Item.Properties().tab(CHARCOAL_PIT).stacksTo(1).craftRemainder(Items.BUCKET));
	
	public static BlockItem FeedingThrough =buildBlockItem(ModBlockRegistry.FeedingThrough);
	public static BlockItem FeedingThroughBirch=buildBlockItem(ModBlockRegistry.FeedingThroughBirch);
	public static BlockItem FeedingThroughJungle =buildBlockItem(ModBlockRegistry.FeedingThroughJungle);
	public static BlockItem FeedingThroughSpruce =buildBlockItem(ModBlockRegistry.FeedingThroughSpruce);
	public static BlockItem FeedingThroughDark =buildBlockItem(ModBlockRegistry.FeedingThroughDark);
	public static BlockItem FeedingThroughAcacia =buildBlockItem(ModBlockRegistry.FeedingThroughAcacia);
	public static BlockItem FeedingThroughCrimson =buildBlockItem(ModBlockRegistry.FeedingThroughCrimson);
	public static BlockItem FeedingThroughWarped =buildBlockItem(ModBlockRegistry.FeedingThroughWarped);
	
	public static BlockItem NestBox=buildBlockItem(ModBlockRegistry.NestBox);
	public static BlockItem Bloomeryy=buildBlockItem(ModBlockRegistry.Bloomeryy);
	public static BlockItem CharcoalBlock=buildBlockItem(ModBlockRegistry.CharcoalBlock);
	public static BlockItem Bloom=new BlockItem(ModBlockRegistry.Bloom,new Item.Properties().tab(CHARCOAL_PIT).stacksTo(16));
	public static BlockItem BlastFurnace=buildBlockItem(ModBlockRegistry.BlastFurnace);
	public static BlockItem Distillery=buildBlockItem(ModBlockRegistry.Distillery);
	public static BlockItem SteamPress=buildBlockItem(ModBlockRegistry.SteamPress);
	public static BlockItem WrathLantern=buildBlockItem(ModBlockRegistry.WrathLantern);
	public static ItemNameBlockItem XPCrystal=new ItemXPCrystal(ModBlockRegistry.XPCrystal,new Item.Properties().tab(CHARCOAL_PIT).rarity(Rarity.UNCOMMON).stacksTo(1));
	public static ItemBlockDwarvenCandle DwarvenCandle=new ItemBlockDwarvenCandle(ModBlockRegistry.DwarvenCandle,new Item.Properties().tab(CHARCOAL_PIT));
	
	public static ItemAnimalCage AnimalCage=new ItemAnimalCage(new Item.Properties().tab(CHARCOAL_PIT));
	//public static Item LyeBottle=new Item(new Item.Properties().tab(CHARCOAL_PIT).stacksTo(16));
	//public static Item SourAlcoholBottle=new Item(new Item.Properties().tab(CHARCOAL_PIT).stacksTo(16).craftRemainder(LyeBottle));
	public static ItemFuel Glycerine=buildItem(CHARCOAL_PIT,1200);
	public static Item AlloyPigIron=buildItem(CHARCOAL_PIT),AlloySteel=buildItem(CHARCOAL_PIT);
	public static ItemTuyere Tuyere=new ItemTuyere(new Item.Properties().tab(CHARCOAL_PIT));
	public static Item Flux=buildItem(CHARCOAL_PIT),PrismarineDust=buildItem(CHARCOAL_PIT);
	public static Item AlloyOrichalcum=buildItem(CHARCOAL_PIT);
	//public static ItemEthanolBottle EthanolBottle=new ItemEthanolBottle();
	public static Item EthoxideBottle=new Item(new Item.Properties().tab(CHARCOAL_PIT).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE));
	public static Item winglet=buildItem(CHARCOAL_PIT),wing=buildItem(CHARCOAL_PIT),engine=buildItem(CHARCOAL_PIT);
	public static Item NetherShard=new SimpleFoiledItem(new Item.Properties().tab(CHARCOAL_PIT).rarity(Rarity.UNCOMMON)){
		@OnlyIn(Dist.CLIENT)
		@Override
		public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
			super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
			pTooltipComponents.add(new TextComponent("Universal Repair Material. Restores 100% of Durability"));
		}
	};
	public static Item MapleSap=new Item(new Item.Properties().tab(CHARCOAL_PIT).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE));
	public static ItemKebabs MapleSyrup=new ItemKebabs(new Item.Properties().tab(CHARCOAL_PIT_FOODS).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE).food(new FoodProperties.Builder().nutrition(6).saturationMod(1.2F).build())){
		@Override
		public UseAnim getUseAnimation(ItemStack pStack) {
			return UseAnim.DRINK;
		}
	};
	public static Item Pancakes=new Item(new Item.Properties().tab(CHARCOAL_PIT_FOODS).food(new FoodProperties.Builder().nutrition(8).saturationMod(1.6F).build()));
	public static ItemClayPot alloy_mold=new ItemClayPot();
	public static ItemCrackedPot finished_alloy_mold=new ItemCrackedPot();
	public static Item clay_mold=new Item(new Item.Properties().tab(CHARCOAL_PIT).stacksTo(16));
	public static Item AlloyBronze=buildItem(CHARCOAL_PIT);
	public static Item TinyGunpowder=buildItem(CHARCOAL_PIT);
	//public static ItemKebabs TriflavorSkewer=new ItemKebabs(new Item.Properties().tab(CHARCOAL_PIT_FOODS).food(new FoodProperties.Builder().nutrition(10).saturationMod(2.4F).meat().build()).craftRemainder(Items.STICK));
	
	/*public static PickaxeItem FlintPick=new PickaxeItem(ModTiers.FLINT,1,-2.8F,new Item.Properties().tab(CHARCOAL_PIT));
	public static ShovelItem FlintShovel=new ShovelItem(ModTiers.FLINT,1.5F,-3,new Item.Properties().tab(CHARCOAL_PIT));
	public static AxeItem FlintAxe=new AxeItem(ModTiers.FLINT,7,-3.2F,new Item.Properties().tab(CHARCOAL_PIT));
	public static HoeItem FlintHoe=new HoeItem(ModTiers.FLINT,-1,-2,new Item.Properties().tab(CHARCOAL_PIT));
	public static SwordItem FlintSword=new SwordItem(ModTiers.FLINT,3,-2.4F,new Item.Properties().tab(CHARCOAL_PIT));
	//public static ItemDagger FlintDagger=new ItemDagger(ModTiers.FLINT,2,-2,new Item.Properties().tab(CHARCOAL_PIT));*/
	
	public static PickaxeItem CopperPick=new PickaxeItem(ModTiers.COPPER, 1,-2.8F,new Item.Properties().tab(CHARCOAL_PIT));
	public static ShovelItem CopperShovle=new ShovelItem(ModTiers.COPPER,1.5F,-3,new Item.Properties().tab(CHARCOAL_PIT));
	public static AxeItem CopperAxe=new AxeItem(ModTiers.COPPER,6.75F,-3.1F,new Item.Properties().tab(CHARCOAL_PIT));
	public static HoeItem CopperHoe=new HoeItem(ModTiers.COPPER,-1,-2,new Item.Properties().tab(CHARCOAL_PIT));
	public static SwordItem CopperSword=new SwordItem(ModTiers.COPPER,3,-2.4F,new Item.Properties().tab(CHARCOAL_PIT));
	
	public static PickaxeItem SteelPick=new PickaxeItem(ModTiers.STEEL, 1,-2.8F,new Item.Properties().tab(CHARCOAL_PIT));
	public static ShovelItem SteelShovle=new ShovelItem(ModTiers.STEEL,1.5F,-3,new Item.Properties().tab(CHARCOAL_PIT));
	public static AxeItem SteelAxe=new AxeItem(ModTiers.STEEL,5.5F,-3F,new Item.Properties().tab(CHARCOAL_PIT));
	public static HoeItem SteelHoe=new HoeItem(ModTiers.STEEL,-2,-2,new Item.Properties().tab(CHARCOAL_PIT));
	public static SwordItem SteelSword=new SwordItem(ModTiers.STEEL,3,-2.4F,new Item.Properties().tab(CHARCOAL_PIT));
	
	public static ArmorItem SteelHelmet=new ArmorItem(ModArmorTiers.STEEL, EquipmentSlot.HEAD,new Item.Properties().tab(CHARCOAL_PIT));
	public static ArmorItem SteelChestplate=new ArmorItem(ModArmorTiers.STEEL,EquipmentSlot.CHEST,new Item.Properties().tab(CHARCOAL_PIT));
	public static ArmorItem SteelLeggings=new ArmorItem(ModArmorTiers.STEEL,EquipmentSlot.LEGS,new Item.Properties().tab(CHARCOAL_PIT));
	public static ArmorItem SteelBoots=new ArmorItem(ModArmorTiers.STEEL,EquipmentSlot.FEET,new Item.Properties().tab(CHARCOAL_PIT));
	
	public static ArmorItem CopperHelmet=new ArmorItem(ModArmorTiers.COPPER, EquipmentSlot.HEAD,new Item.Properties().tab(CHARCOAL_PIT));
	public static ArmorItem CopperChestplate=new ArmorItem(ModArmorTiers.COPPER,EquipmentSlot.CHEST,new Item.Properties().tab(CHARCOAL_PIT));
	public static ArmorItem CopperLeggings=new ArmorItem(ModArmorTiers.COPPER,EquipmentSlot.LEGS,new Item.Properties().tab(CHARCOAL_PIT));
	public static ArmorItem CopperBoots=new ArmorItem(ModArmorTiers.COPPER,EquipmentSlot.FEET,new Item.Properties().tab(CHARCOAL_PIT));
	
	public static PickaxeItem OrichalcumPick=new PickaxeItem(ModTiers.ORICHALCUM,1,-2.8F,new Item.Properties().tab(CHARCOAL_PIT));
	public static ShovelItem OrichalcumShovel=new ShovelItem(ModTiers.ORICHALCUM,1.5F,-3,new Item.Properties().tab(CHARCOAL_PIT));
	public static AxeItem OrichalcumAxe=new AxeItem(ModTiers.ORICHALCUM,6F,-3F,new Item.Properties().tab(CHARCOAL_PIT));
	public static HoeItem OrichalcumHoe=new HoeItem(ModTiers.ORICHALCUM,-2,-1,new Item.Properties().tab(CHARCOAL_PIT));
	
	public static ArmorItem OrichalcumHelmet=new ArmorItem(ModArmorTiers.ORICHALCUM,EquipmentSlot.HEAD,new Item.Properties().tab(CHARCOAL_PIT));
	public static ArmorItem OrichalcumChestplate=new ArmorItem(ModArmorTiers.ORICHALCUM,EquipmentSlot.CHEST,new Item.Properties().tab(CHARCOAL_PIT));
	public static ArmorItem OrichalcumLeggings=new ArmorItem(ModArmorTiers.ORICHALCUM,EquipmentSlot.LEGS,new Item.Properties().tab(CHARCOAL_PIT));
	public static ArmorItem OrichalcumBoots=new ArmorItem(ModArmorTiers.ORICHALCUM,EquipmentSlot.FEET,new Item.Properties().tab(CHARCOAL_PIT));
	
	public static PickaxeItem BronzePick=new PickaxeItem(ModTiers.BRONZE, 1,-2.8F,new Item.Properties().tab(CHARCOAL_PIT));
	public static ShovelItem BronzeShovle=new ShovelItem(ModTiers.BRONZE,1.5F,-3,new Item.Properties().tab(CHARCOAL_PIT));
	public static AxeItem BronzeAxe=new AxeItem(ModTiers.BRONZE,6F,-3.1F,new Item.Properties().tab(CHARCOAL_PIT));
	public static HoeItem BronzeHoe=new HoeItem(ModTiers.BRONZE,-2,-1,new Item.Properties().tab(CHARCOAL_PIT));
	public static SwordItem BronzeSword=new SwordItem(ModTiers.BRONZE,3,-2.4F,new Item.Properties().tab(CHARCOAL_PIT));
	
	public static ArmorItem BronzeHelmet=new ArmorItem(ModArmorTiers.BRONZE,EquipmentSlot.HEAD,new Item.Properties().tab(CHARCOAL_PIT));
	public static ArmorItem BronzeChestplate=new ArmorItem(ModArmorTiers.BRONZE,EquipmentSlot.CHEST,new Item.Properties().tab(CHARCOAL_PIT));
	public static ArmorItem BronzeLeggings=new ArmorItem(ModArmorTiers.BRONZE,EquipmentSlot.LEGS,new Item.Properties().tab(CHARCOAL_PIT));
	public static ArmorItem BronzeBoots=new ArmorItem(ModArmorTiers.BRONZE,EquipmentSlot.FEET,new Item.Properties().tab(CHARCOAL_PIT));
	
	public static ItemAirplane plane=new ItemAirplane(new Item.Properties().tab(CHARCOAL_PIT).stacksTo(1));
	
	public static ItemMusket musket=new ItemMusket(new Item.Properties().tab(CHARCOAL_PIT).stacksTo(1).durability(375));
	
	public static ItemTreeTap TreeTap=new ItemTreeTap(new Item.Properties().tab(CHARCOAL_PIT).stacksTo(1).durability(64));
	public static ItemDynamiteRemote DynamiteRemote=new ItemDynamiteRemote(new Item.Properties().tab(CHARCOAL_PIT).stacksTo(1));
	
	/*public static TallBlockItem BrickDoor=new TallBlockItem(ModBlockRegistry.BrickDoor,new Item.Properties().group(CHARCOAL_PIT)),
			SandyDoor=new TallBlockItem(ModBlockRegistry.SandyDoor,new Item.Properties().group(CHARCOAL_PIT)),
			NetherDoor=new TallBlockItem(ModBlockRegistry.NetherDoor,new Item.Properties().group(CHARCOAL_PIT)),
			EndDoor=new TallBlockItem(ModBlockRegistry.EndDoor,new Item.Properties().group(CHARCOAL_PIT));*/
	
	
	
	//@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(Thatch.setRegistryName("thatch"), LogPile.setRegistryName("log_pile"), WoodAsh.setRegistryName("wood_ash"), 
				CoalAsh.setRegistryName("coal_ash"), CokeBlock.setRegistryName("coke_block"), AshBlock.setRegistryName("ash_block"), 
				SandyBrick.setRegistryName("sandy_brick"), SandySlab.setRegistryName("sandy_slab"), SandyStair.setRegistryName("sandy_stair"), SandyWall.setRegistryName("sandy_wall"),
				BrickCollector.setRegistryName("brick_collector"),SandyCollector.setRegistryName("sandy_collector"),NetherCollector.setRegistryName("nether_collector"),EndCollector.setRegistryName("end_collector"),
				Barrel.setRegistryName("barrel"),MechanicalBeellows.setRegistryName("mechanical_bellows"),Bellows.setRegistryName("bellows"),
				AppleSapling.setRegistryName("apple_sapling"),CherrySapling.setRegistryName("cherry_sapling"),
				AppleLeaves.setRegistryName("apple_leaves"),CherryLeaves.setRegistryName("cherry_leaves"),
				DragonSapling.setRegistryName("dragon_sapling"),DragonLeaves.setRegistryName("dragon_leaves"),
				ChestnutSapling.setRegistryName("chestnut_sapling"),ChestnutLeaves.setRegistryName("chestnut_leaves"),
				OliveSapling.setRegistryName("olive_sapling"),OliveLeaves.setRegistryName("olive_leaves"),OrangeSapling.setRegistryName("orange_sapling"),
				OrangeLeaves.setRegistryName("orange_leaves"),OilLamp.setRegistryName("oil_lamp"),NestBox.setRegistryName("nest_box"),
				Bloomeryy.setRegistryName("bloomeryy"),CharcoalBlock.setRegistryName("charcoal_block"),Bloom.setRegistryName("bloom"),
				BlastFurnace.setRegistryName("blast_furnace"),Distillery.setRegistryName("distillery"),SteamPress.setRegistryName("steam_press"),
				WrathLantern.setRegistryName("wrath_lantern"),EnchantedSapling.setRegistryName("enchanted_sapling"),EnchantedLeaves.setRegistryName("enchanted_leaves"),
				MapleLeaves.setRegistryName("maple_leaves"),MapleSapling.setRegistryName("maple_sapling"),JacarandaLeaves.setRegistryName("jacaranda_leaves"),
				JacarandaSapling.setRegistryName("jacaranda_sapling"),SakuraSapling.setRegistryName("sakura_sapling"),SakuraLeaves.setRegistryName("sakura_leaves"),
				ForsythiaLeaves.setRegistryName("forsythia_leaves"),ForsythiaSapling.setRegistryName("forsythia_sapling"),RandomSapling.setRegistryName("random_sapling"),
				CedarSapling.setRegistryName("cedar_sapling"),CedarLeaves.setRegistryName("cedar_leaves"),DouglasSapling.setRegistryName("douglas_sapling"),
				DouglasLeaves.setRegistryName("douglas_leaves"),DecorativeSapling.setRegistryName("decorative_sapling"),XPCrystal.setRegistryName("xp_crystal"),
				DwarvenCandle.setRegistryName("dwarven_candle"));
		event.getRegistry().registerAll(Straw.setRegistryName("straw"), Ash.setRegistryName("ash"), Coke.setRegistryName("coke"), 
				Aeternalis.setRegistryName("aeternalis_fuel"), Fertilizer.setRegistryName("fertilizer"), FireStarter.setRegistryName("fire_starter"),
				CreosoteBucket.setRegistryName("creosote_bucket"),ClayPot.setRegistryName("clay_pot"),
				SandyBrickItem.setRegistryName("sandy_brick_item"),UnfireSandyBrick.setRegistryName("unfired_sandy_brick"),NetherBrickItem.setRegistryName("nether_brick_item"),
				UnfiredBrick.setRegistryName("unfired_brick"),AlcoholBottle.setRegistryName("alcohol_bottle"),VinegarBucket.setRegistryName("vinegar_bucket"),
				VinegarBottle.setRegistryName("vinegar_bottle"),Cheese.setRegistryName("cheese"),Kebabs.setRegistryName("kebabs"),
				Leek.setRegistryName("leek"),FarfetchStew.setRegistryName("farfetch_stew"),
				Calamari.setRegistryName("calamari"),CookedCalamri.setRegistryName("cooked_calamari"),
				Corn.setRegistryName("corn"),PopCorn.setRegistryName("popcorn"),CornStew.setRegistryName("corn_stew"),
				Sushi.setRegistryName("sushi"),Fugu.setRegistryName("fugu"),Cherry.setRegistryName("cherry"),DragonFruit.setRegistryName("dragon_fruit"),
				ChestNut.setRegistryName("chestnut"),Bananana.setRegistryName("banana"),Cococonut.setRegistryName("coconut"),
				ChocoPoweder.setRegistryName("choco_powder"),Chocolate.setRegistryName("chocolate"),Dango.setRegistryName("tricolor_dango"),SunflowerSeeds.setRegistryName("sunflower_seeds"),
				mortarPestle.setRegistryName("mortar_pestle"),Flour.setRegistryName("flour"),CornFlour.setRegistryName("corn_flour"),Croissant.setRegistryName("croissant"),
				Olives.setRegistryName("olives"),Pickled_Olives.setRegistryName("pickled_olives"),Orange.setRegistryName("orange"),OliveOilBucket.setRegistryName("olive_oil_bucket"),
				WalnutOilBucket.setRegistryName("walnut_oil_bucket"),SunflowerOilBucket.setRegistryName("sunflower_oil_bucket"),
				AnimalCage.setRegistryName("animal_cage"),BioDieselBucket.setRegistryName("bio_diesel_bucket"),
				Glycerine.setRegistryName("glycerine"),
				AlloyPigIron.setRegistryName("alloy_pig_iron"),AlloySteel.setRegistryName("alloy_steel"),Tuyere.setRegistryName("tuyere"),
				Flux.setRegistryName("flux"),SerinanStew.setRegistryName("serinan_stew"),BunnyStew.setRegistryName("bunny_stew"),
				AlloyOrichalcum.setRegistryName("alloy_orichalcum"),PrismarineDust.setRegistryName("prismarine_dust"),
				EthanolBucket.setRegistryName("ethanol_bucket"),EthoxideBucket.setRegistryName("ethoxide_bucket"),
				EthoxideBottle.setRegistryName("ethoxide_bottle"),SeedOilBucket.setRegistryName("seed_oil_bucket"),
				plane.setRegistryName("airplane"),winglet.setRegistryName("winglet"),wing.setRegistryName("wing"),engine.setRegistryName("engine"),
				NetherShard.setRegistryName("nether_shard"),musket.setRegistryName("musket"),TreeTap.setRegistryName("tree_tap"),
				MapleSap.setRegistryName("maple_sap"),MapleSyrup.setRegistryName("maple_syrup"),Pancakes.setRegistryName("pancakes"),
				alloy_mold.setRegistryName("alloy_mold"),finished_alloy_mold.setRegistryName("finished_alloy_mold"),clay_mold.setRegistryName("clay_mold"),
				AlloyBronze.setRegistryName("alloy_bronze"),DynamiteRemote.setRegistryName("dynamite_remote"),TinyGunpowder.setRegistryName("tiny_gunpowder"));
		event.getRegistry().registerAll(CeramicPot.setRegistryName("ceramic_pot"),YellowPot.setRegistryName("yellow_pot"),WhitePot.setRegistryName("white_pot"),
				RedPot.setRegistryName("red_pot"),PurplePot.setRegistryName("purple_pot"),PinkPot.setRegistryName("pink_pot"),OrangePot.setRegistryName("orange_pot"),
				MagentaPot.setRegistryName("magenta_pot"),LimePot.setRegistryName("lime_pot"),LightGrayPot.setRegistryName("light_gray_pot"),
				LightBluePot.setRegistryName("light_blue_pot"),GreenPot.setRegistryName("green_pot"),GrayPot.setRegistryName("gray_pot"),CyanPot.setRegistryName("cyan_pot"),
				BrownPot.setRegistryName("brown_pot"),BluePot.setRegistryName("blue_pot"),BlackPot.setRegistryName("black_pot"));
		event.getRegistry().registerAll(FeedingThrough.setRegistryName("feeding_through"),FeedingThroughBirch.setRegistryName("feeding_through_birch"),
				FeedingThroughJungle.setRegistryName("feeding_through_jungle"), FeedingThroughSpruce.setRegistryName("feeding_through_spruce"),
				FeedingThroughDark.setRegistryName("feeding_through_dark"), FeedingThroughAcacia.setRegistryName("feeding_through_acacia"),
				FeedingThroughCrimson.setRegistryName("feeding_through_crimson"), FeedingThroughWarped.setRegistryName("feeding_through_warped"));
		event.getRegistry().registerAll(CopperPick.setRegistryName("copper_pick"),CopperShovle.setRegistryName("copper_shovel"),
				CopperAxe.setRegistryName("copper_axe"),CopperHoe.setRegistryName("copper_hoe"),CopperSword.setRegistryName("copper_sword"),
				SteelPick.setRegistryName("steel_pick"),SteelShovle.setRegistryName("steel_shovel"),
				SteelAxe.setRegistryName("steel_axe"),SteelHoe.setRegistryName("steel_hoe"),SteelSword.setRegistryName("steel_sword"),
				SteelHelmet.setRegistryName("steel_helmet"),SteelChestplate.setRegistryName("steel_chestplate"),SteelLeggings.setRegistryName("steel_leggings"),
				SteelBoots.setRegistryName("steel_boots"),CopperHelmet.setRegistryName("copper_helmet"),CopperChestplate.setRegistryName("copper_chestplate"),
				CopperLeggings.setRegistryName("copper_leggings"),CopperBoots.setRegistryName("copper_boots"),
				OrichalcumPick.setRegistryName("orichalcum_pick"),OrichalcumShovel.setRegistryName("orichalcum_shovel"),OrichalcumAxe.setRegistryName("orichalcum_axe"),
				OrichalcumHelmet.setRegistryName("orichalcum_helmet"),OrichalcumChestplate.setRegistryName("orichalcum_chestplate"),
				OrichalcumLeggings.setRegistryName("orichalcum_leggings"),OrichalcumBoots.setRegistryName("orichalcum_boots"),OrichalcumHoe.setRegistryName("orichalcum_hoe"),
				BronzePick.setRegistryName("bronze_pick"),BronzeShovle.setRegistryName("bronze_shovel"),BronzeAxe.setRegistryName("bronze_axe"),BronzeHoe.setRegistryName("bronze_hoe"),
				BronzeSword.setRegistryName("bronze_sword"),BronzeHelmet.setRegistryName("bronze_helmet"),BronzeChestplate.setRegistryName("bronze_chestplate"),
				BronzeLeggings.setRegistryName("bronze_leggings"),BronzeBoots.setRegistryName("bronze_boots"));
		
		
		DispenserBlock.registerBehavior(ModItemRegistry.CeramicPot, new DispenserPlacePot());
		DispenserBlock.registerBehavior(ModItemRegistry.BlackPot, new DispenserPlacePot());
		DispenserBlock.registerBehavior(ModItemRegistry.BluePot, new DispenserPlacePot());
		DispenserBlock.registerBehavior(ModItemRegistry.BrownPot, new DispenserPlacePot());
		DispenserBlock.registerBehavior(ModItemRegistry.CyanPot, new DispenserPlacePot());
		DispenserBlock.registerBehavior(ModItemRegistry.GrayPot, new DispenserPlacePot());
		DispenserBlock.registerBehavior(ModItemRegistry.GreenPot, new DispenserPlacePot());
		DispenserBlock.registerBehavior(ModItemRegistry.LightBluePot, new DispenserPlacePot());
		DispenserBlock.registerBehavior(ModItemRegistry.LightGrayPot, new DispenserPlacePot());
		DispenserBlock.registerBehavior(ModItemRegistry.LimePot, new DispenserPlacePot());
		DispenserBlock.registerBehavior(ModItemRegistry.MagentaPot, new DispenserPlacePot());
		DispenserBlock.registerBehavior(ModItemRegistry.OrangePot, new DispenserPlacePot());
		DispenserBlock.registerBehavior(ModItemRegistry.PinkPot, new DispenserPlacePot());
		DispenserBlock.registerBehavior(ModItemRegistry.PurplePot, new DispenserPlacePot());
		DispenserBlock.registerBehavior(ModItemRegistry.RedPot, new DispenserPlacePot());
		DispenserBlock.registerBehavior(ModItemRegistry.WhitePot, new DispenserPlacePot());
		DispenserBlock.registerBehavior(ModItemRegistry.YellowPot, new DispenserPlacePot());
		
		if(Config.DowngradeStone.get()) {
			((TieredItem) Items.STONE_PICKAXE).tier = ModTiers.FLINT;
			((TieredItem) Items.STONE_SHOVEL).tier = ModTiers.FLINT;
			((TieredItem) Items.STONE_AXE).tier = ModTiers.FLINT;
			((TieredItem) Items.STONE_HOE).tier = ModTiers.FLINT;
			((TieredItem) Items.STONE_SWORD).tier = ModTiers.FLINT;
		}
	}
	
	public static BlockItemFuel buildBlockItem(Block block, int time) {
		return buildBlockItem(block, CHARCOAL_PIT, time);
	}
	
	public static BlockItemFuel buildBlockItem(Block block, CreativeModeTab group, int time) {
		return new BlockItemFuel(block, new Item.Properties().tab(group)).setBurnTime(time);
	}
	
	public static BlockItem buildBlockItem(Block block) {
		return buildBlockItem(block, CHARCOAL_PIT);
	}
	
	public static ItemBlockCeramicPot buildBlockItemP(Block block) {
		return new ItemBlockCeramicPot(block, new Item.Properties().tab(CHARCOAL_PIT).stacksTo(16));
	}
	
	public static BlockItem buildBlockItem(Block block, CreativeModeTab group) {
		return new BlockItem(block, new Item.Properties().tab(group));
	}
	
	public static ItemFuel buildItem(CreativeModeTab group,int time) {
		return new ItemFuel(new Item.Properties().tab(group)).setBurnTime(time);
	}
	
	public static Item buildItem(CreativeModeTab group) {
		return new Item(new Item.Properties().tab(group));
	}
	
}
