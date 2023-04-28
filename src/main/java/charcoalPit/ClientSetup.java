package charcoalPit;

import charcoalPit.block.BlockFruitLeaves;
import charcoalPit.core.ModBlockRegistry;
import charcoalPit.core.ModContainerRegistry;
import charcoalPit.core.ModItemRegistry;
import charcoalPit.entity.AirplaneModel;
import charcoalPit.entity.AirplaneRenderer;
import charcoalPit.entity.ModEntities;
import charcoalPit.gui.*;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import javax.annotation.Nullable;

@EventBusSubscriber(modid=CharcoalPit.MODID ,bus=Bus.MOD ,value=Dist.CLIENT)
public class ClientSetup {
	
	@SubscribeEvent
	public static void init(FMLClientSetupEvent event) {
		MenuScreens.register(ModContainerRegistry.CeramicPot, CeramicPotScreen::new);
		MenuScreens.register(ModContainerRegistry.ClayPot, ClayPotScreen::new);
		MenuScreens.register(ModContainerRegistry.Barrel, BarrelScreen::new);
		MenuScreens.register(ModContainerRegistry.Bloomery, BloomeryScreen::new);
		MenuScreens.register(ModContainerRegistry.BlastFurnace, BlastFurnaceScreen::new);
		MenuScreens.register(ModContainerRegistry.Distillery,DistilleryScreen::new);
		MenuScreens.register(ModContainerRegistry.SteamPress,SteamPressScreen::new);
		ItemBlockRenderTypes.setRenderLayer(ModBlockRegistry.Leeks, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlockRegistry.Corn,RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlockRegistry.Sunflower,RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlockRegistry.AppleSapling,RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlockRegistry.AppleLeaves,RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlockRegistry.CherrySapling,RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlockRegistry.CherryLeaves,RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlockRegistry.DragonLeaves,RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlockRegistry.DragonSapling,RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlockRegistry.ChestnutSapling,RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlockRegistry.ChestnutLeaves,RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlockRegistry.BanananaPod,RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlockRegistry.CoconutPod,RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlockRegistry.OliveSapling,RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlockRegistry.OliveLeaves,RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlockRegistry.OrangeSapling,RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlockRegistry.OrangeLeaves,RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlockRegistry.EnchantedSapling,RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlockRegistry.EnchantedLeaves,RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlockRegistry.MapleLeaves,RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlockRegistry.MapleSapling,RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlockRegistry.JacarandaLeaves,RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlockRegistry.JacarandaSapling,RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlockRegistry.SakuraLeaves,RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlockRegistry.SakuraSapling,RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlockRegistry.ForsythiaLeaves,RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlockRegistry.ForsythiaSapling,RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlockRegistry.RandomSapling,RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlockRegistry.CedarLeaves,RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlockRegistry.CedarSapling,RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlockRegistry.DouglasLeaves,RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlockRegistry.DouglasSapling,RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlockRegistry.DecorativeSapling,RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlockRegistry.XPCrystal,RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlockRegistry.DwarvenCandle,RenderType.cutout());
		event.enqueueWork(()->{
			ItemProperties.register(ModItemRegistry.AppleLeaves, new ResourceLocation(CharcoalPit.MODID,"stage"),(stack, arg2, entity, pseed)->{
				if(stack.hasTag()&&stack.getTag().contains("stage")){
					if(stack.getTag().getInt("stage")==3)
						return 0.25F;
					if(stack.getTag().getInt("stage")==6){
						return 0.5F;
					}
					if(stack.getTag().getInt("stage")==7){
						return 1F;
					}
				}
				return 0F;
			});
			ItemProperties.register(ModItemRegistry.CherryLeaves, new ResourceLocation(CharcoalPit.MODID,"stage"),(stack,arg2,entity, pseed)->{
				if(stack.hasTag()&&stack.getTag().contains("stage")){
					if(stack.getTag().getInt("stage")==3)
						return 0.25F;
					if(stack.getTag().getInt("stage")==6){
						return 0.5F;
					}
					if(stack.getTag().getInt("stage")==7){
						return 1F;
					}
				}
				return 0F;
			});
			ItemProperties.register(ModItemRegistry.DragonLeaves, new ResourceLocation(CharcoalPit.MODID,"stage"),(stack,arg2,entity, pseed)->{
				if(stack.hasTag()&&stack.getTag().contains("stage")){
					if(stack.getTag().getInt("stage")==3)
						return 0.25F;
					if(stack.getTag().getInt("stage")==6){
						return 0.5F;
					}
					if(stack.getTag().getInt("stage")==7){
						return 1F;
					}
				}
				return 0F;
			});
			ItemProperties.register(ModItemRegistry.ChestnutLeaves, new ResourceLocation(CharcoalPit.MODID,"stage"),(stack,arg2,entity, pseed)->{
				if(stack.hasTag()&&stack.getTag().contains("stage")){
					if(stack.getTag().getInt("stage")==3)
						return 0.25F;
					if(stack.getTag().getInt("stage")==6){
						return 0.5F;
					}
					if(stack.getTag().getInt("stage")==7){
						return 1F;
					}
				}
				return 0F;
			});
			ItemProperties.register(ModItemRegistry.OliveLeaves, new ResourceLocation(CharcoalPit.MODID,"stage"),(stack,arg2,entity, pseed)->{
				if(stack.hasTag()&&stack.getTag().contains("stage")){
					if(stack.getTag().getInt("stage")==3)
						return 0.25F;
					if(stack.getTag().getInt("stage")==6){
						return 0.5F;
					}
					if(stack.getTag().getInt("stage")==7){
						return 1F;
					}
				}
				return 0F;
			});
			ItemProperties.register(ModItemRegistry.OrangeLeaves, new ResourceLocation(CharcoalPit.MODID,"stage"),(stack,arg2,entity, pseed)->{
				if(stack.hasTag()&&stack.getTag().contains("stage")){
					if(stack.getTag().getInt("stage")==3)
						return 0.25F;
					if(stack.getTag().getInt("stage")==6){
						return 0.5F;
					}
					if(stack.getTag().getInt("stage")==7){
						return 1F;
					}
				}
				return 0F;
			});
			ItemProperties.register(ModItemRegistry.EnchantedLeaves, new ResourceLocation(CharcoalPit.MODID,"stage"),(stack,arg2,entity, pseed)->{
				if(stack.hasTag()&&stack.getTag().contains("stage")){
					if(stack.getTag().getInt("stage")==3)
						return 0.25F;
					if(stack.getTag().getInt("stage")==6){
						return 0.5F;
					}
					if(stack.getTag().getInt("stage")==7){
						return 1F;
					}
				}
				return 0F;
			});
			ItemProperties.register(ModItemRegistry.MapleLeaves, new ResourceLocation(CharcoalPit.MODID,"stage"),(stack,arg2,entity, pseed)->{
				if(stack.hasTag()&&stack.getTag().contains("stage")){
					if(stack.getTag().getInt("stage")==7){
						return 1F;
					}
				}
				return 0F;
			});
			ItemProperties.register(ModItemRegistry.JacarandaLeaves, new ResourceLocation(CharcoalPit.MODID,"stage"),(stack,arg2,entity, pseed)->{
				if(stack.hasTag()&&stack.getTag().contains("stage")){
					if(stack.getTag().getInt("stage")==7){
						return 1F;
					}
				}
				return 0F;
			});
			ItemProperties.register(ModItemRegistry.SakuraLeaves, new ResourceLocation(CharcoalPit.MODID,"stage"),(stack,arg2,entity, pseed)->{
				if(stack.hasTag()&&stack.getTag().contains("stage")){
					if(stack.getTag().getInt("stage")==7){
						return 1F;
					}
				}
				return 0F;
			});
			ItemProperties.register(ModItemRegistry.ForsythiaLeaves, new ResourceLocation(CharcoalPit.MODID,"stage"),(stack,arg2,entity, pseed)->{
				if(stack.hasTag()&&stack.getTag().contains("stage")){
					if(stack.getTag().getInt("stage")==7){
						return 1F;
					}
				}
				return 0F;
			});
			ItemProperties.register(ModItemRegistry.alloy_mold, new ResourceLocation(CharcoalPit.MODID,"type"),(stack,arg2,entity, pseed)->{
				if(stack.hasTag()&&stack.getTag().contains("inventory")){
					return 1F;
				}
				return 0F;
			});
		});
	}
	@SubscribeEvent
	public static void registerColors(ColorHandlerEvent.Item event){
		event.getItemColors().register(new ItemColor() {
			@Override
			public int getColor(ItemStack p_getColor_1_, int p_getColor_2_) {
				if(p_getColor_2_==0){
					return PotionUtils.getColor(p_getColor_1_);
				}
				return 0xFFFFFF;
			}
		}, ModItemRegistry.AlcoholBottle);
		
		event.getItemColors().register(new ItemColor() {
			@Override
			public int getColor(ItemStack p_getColor_1_, int p_getColor_2_) {
				if(p_getColor_2_==0){
					return 0x48B518;
				}
				return 0xFFFFFF;
			}
		},ModItemRegistry.AppleLeaves);
		
		event.getItemColors().register(new ItemColor() {
			@Override
			public int getColor(ItemStack p_getColor_1_, int p_getColor_2_) {
				if(p_getColor_2_==0){
					if(p_getColor_1_.hasTag()&&p_getColor_1_.getTag().contains("stage")&&p_getColor_1_.getTag().getInt("stage")==3)
						return 0xFFFFFF;
					return 0x48B518;
				}
				return 0xFFFFFF;
			}
		},ModItemRegistry.CherryLeaves);
		
		event.getItemColors().register(new ItemColor() {
			@Override
			public int getColor(ItemStack p_getColor_1_, int p_getColor_2_) {
				if(p_getColor_2_==0){
					return 0x48B518;
				}
				return 0xFFFFFF;
			}
		},ModItemRegistry.DragonLeaves);
		
		event.getItemColors().register(new ItemColor() {
			@Override
			public int getColor(ItemStack p_getColor_1_, int p_getColor_2_) {
				if(p_getColor_2_==0){
					return 0x48B518;
				}
				return 0xFFFFFF;
			}
		},ModItemRegistry.ChestnutLeaves);
		
		event.getItemColors().register(new ItemColor() {
			@Override
			public int getColor(ItemStack p_getColor_1_, int p_getColor_2_) {
				if(p_getColor_2_==0){
					return 0x48B518;
				}
				return 0xFFFFFF;
			}
		},ModItemRegistry.OliveLeaves);
		
		event.getItemColors().register(new ItemColor() {
			@Override
			public int getColor(ItemStack p_getColor_1_, int p_getColor_2_) {
				if(p_getColor_2_==0){
					return 0x48B518;
				}
				return 0xFFFFFF;
			}
		},ModItemRegistry.OrangeLeaves);
		
		event.getItemColors().register(new ItemColor() {
			@Override
			public int getColor(ItemStack p_getColor_1_, int p_getColor_2_) {
				if(p_getColor_2_==0){
					return 0x48B518;
				}
				return 0xFFFFFF;
			}
		},ModItemRegistry.EnchantedLeaves);
		
		event.getItemColors().register(new ItemColor() {
			@Override
			public int getColor(ItemStack p_getColor_1_, int p_getColor_2_) {
				if(p_getColor_2_==0){
					if(p_getColor_1_.hasTag()&&p_getColor_1_.getTag().contains("stage")&&p_getColor_1_.getTag().getInt("stage")>3)
						return 0xFFFFFF;
					return 0x48B518;
				}
				return 0xFFFFFF;
			}
		},ModItemRegistry.MapleLeaves);
		
		event.getItemColors().register(new ItemColor() {
			@Override
			public int getColor(ItemStack p_getColor_1_, int p_getColor_2_) {
				if(p_getColor_2_==0){
					if(p_getColor_1_.hasTag()&&p_getColor_1_.getTag().contains("stage")&&p_getColor_1_.getTag().getInt("stage")>3)
						return 0xFFFFFF;
					return 0x48B518;
				}
				return 0xFFFFFF;
			}
		},ModItemRegistry.JacarandaLeaves);
		
		event.getItemColors().register(new ItemColor() {
			@Override
			public int getColor(ItemStack p_getColor_1_, int p_getColor_2_) {
				if(p_getColor_2_==0){
					if(p_getColor_1_.hasTag()&&p_getColor_1_.getTag().contains("stage")&&p_getColor_1_.getTag().getInt("stage")>3)
						return 0xFFFFFF;
					return 0x48B518;
				}
				return 0xFFFFFF;
			}
		},ModItemRegistry.SakuraLeaves);
		
		event.getItemColors().register(new ItemColor() {
			@Override
			public int getColor(ItemStack p_getColor_1_, int p_getColor_2_) {
				if(p_getColor_2_==0){
					if(p_getColor_1_.hasTag()&&p_getColor_1_.getTag().contains("stage")&&p_getColor_1_.getTag().getInt("stage")>3)
						return 0xFFFFFF;
					return 0x48B518;
				}
				return 0xFFFFFF;
			}
		},ModItemRegistry.ForsythiaLeaves);
		
		event.getItemColors().register(new ItemColor() {
			@Override
			public int getColor(ItemStack stack, int p_getColor_2_) {
				if(p_getColor_2_==1){
					return Mth.hsvToRgb((Minecraft.getInstance().level.getGameTime()%100)/100F,1F,1F);
				}
				return 0xFFFFFF;
			}
		},ModItemRegistry.RandomSapling);
		
		event.getItemColors().register(new ItemColor() {
			@Override
			public int getColor(ItemStack p_getColor_1_, int p_getColor_2_) {
				if(p_getColor_2_==0){
					return 0x5FC33B;
				}
				return 0xFFFFFF;
			}
		},ModItemRegistry.CedarLeaves);
		
		event.getItemColors().register(new ItemColor() {
			@Override
			public int getColor(ItemStack p_getColor_1_, int p_getColor_2_) {
				if(p_getColor_2_==0){
					return 0x48B518;
				}
				return 0xFFFFFF;
			}
		},ModItemRegistry.DouglasLeaves);
		
		event.getItemColors().register(new ItemColor() {
			@Override
			public int getColor(ItemStack stack, int p_getColor_2_) {
				if(p_getColor_2_==1){
					return Mth.hsvToRgb((Minecraft.getInstance().level.getGameTime()%100)/100F,1F,1F);
				}
				return 0xFFFFFF;
			}
		},ModItemRegistry.DecorativeSapling);
	}
	
	@SubscribeEvent
	public static void registerBlockColors(ColorHandlerEvent.Block event){
		event.getBlockColors().register(new BlockColor() {
			@Override
			public int getColor(BlockState p_getColor_1_, @Nullable BlockAndTintGetter p_getColor_2_, @Nullable BlockPos p_getColor_3_, int p_getColor_4_) {
				if(p_getColor_4_==0){
					return BiomeColors.getAverageFoliageColor(p_getColor_2_,p_getColor_3_);
				}
				return 0xFFFFFF;
			}
		},ModBlockRegistry.AppleLeaves);
		
		event.getBlockColors().register(new BlockColor() {
			@Override
			public int getColor(BlockState p_getColor_1_, @Nullable BlockAndTintGetter p_getColor_2_, @Nullable BlockPos p_getColor_3_, int p_getColor_4_) {
				if(p_getColor_4_==0){
					if(p_getColor_1_.getValue(BlockFruitLeaves.AGE)>1&&p_getColor_1_.getValue(BlockFruitLeaves.AGE)<5)
						return 0xFFFFFF;
					else
						return BiomeColors.getAverageFoliageColor(p_getColor_2_,p_getColor_3_);
				}
				return 0xFFFFFF;
			}
		},ModBlockRegistry.CherryLeaves);
		
		event.getBlockColors().register(new BlockColor() {
			@Override
			public int getColor(BlockState p_getColor_1_, @Nullable BlockAndTintGetter p_getColor_2_, @Nullable BlockPos p_getColor_3_, int p_getColor_4_) {
				if(p_getColor_4_==0){
					return 0x48B518;
				}
				return 0xFFFFFF;
			}
		},ModBlockRegistry.DragonLeaves);
		
		event.getBlockColors().register(new BlockColor() {
			@Override
			public int getColor(BlockState p_getColor_1_, @Nullable BlockAndTintGetter p_getColor_2_, @Nullable BlockPos p_getColor_3_, int p_getColor_4_) {
				if(p_getColor_4_==0){
					return BiomeColors.getAverageFoliageColor(p_getColor_2_,p_getColor_3_);
				}
				return 0xFFFFFF;
			}
		},ModBlockRegistry.ChestnutLeaves);
		
		event.getBlockColors().register(new BlockColor() {
			@Override
			public int getColor(BlockState p_getColor_1_, @Nullable BlockAndTintGetter p_getColor_2_, @Nullable BlockPos p_getColor_3_, int p_getColor_4_) {
				if(p_getColor_4_==0){
					return BiomeColors.getAverageFoliageColor(p_getColor_2_,p_getColor_3_);
				}
				return 0xFFFFFF;
			}
		},ModBlockRegistry.OliveLeaves);
		
		event.getBlockColors().register(new BlockColor() {
			@Override
			public int getColor(BlockState p_getColor_1_, @Nullable BlockAndTintGetter p_getColor_2_, @Nullable BlockPos p_getColor_3_, int p_getColor_4_) {
				if(p_getColor_4_==0){
					return BiomeColors.getAverageFoliageColor(p_getColor_2_,p_getColor_3_);
				}
				return 0xFFFFFF;
			}
		},ModBlockRegistry.OrangeLeaves);
		
		event.getBlockColors().register(new BlockColor() {
			@Override
			public int getColor(BlockState p_getColor_1_, @Nullable BlockAndTintGetter p_getColor_2_, @Nullable BlockPos p_getColor_3_, int p_getColor_4_) {
				if(p_getColor_4_==0){
					return BiomeColors.getAverageFoliageColor(p_getColor_2_,p_getColor_3_);
				}
				return 0xFFFFFF;
			}
		},ModBlockRegistry.EnchantedLeaves);
		
		event.getBlockColors().register(new BlockColor() {
			@Override
			public int getColor(BlockState p_getColor_1_, @Nullable BlockAndTintGetter p_getColor_2_, @Nullable BlockPos p_getColor_3_, int p_getColor_4_) {
				if(p_getColor_4_==0){
					if(p_getColor_1_.getValue(BlockFruitLeaves.AGE)>3)
						return 0xFFFFFF;
					else
						return BiomeColors.getAverageFoliageColor(p_getColor_2_,p_getColor_3_);
				}
				return 0xFFFFFF;
			}
		},ModBlockRegistry.MapleLeaves);
		
		event.getBlockColors().register(new BlockColor() {
			@Override
			public int getColor(BlockState p_getColor_1_, @Nullable BlockAndTintGetter p_getColor_2_, @Nullable BlockPos p_getColor_3_, int p_getColor_4_) {
				if(p_getColor_4_==0){
					if(p_getColor_1_.getValue(BlockFruitLeaves.AGE)>3)
						return 0xFFFFFF;
					else
						return BiomeColors.getAverageFoliageColor(p_getColor_2_,p_getColor_3_);
				}
				return 0xFFFFFF;
			}
		},ModBlockRegistry.JacarandaLeaves);
		
		event.getBlockColors().register(new BlockColor() {
			@Override
			public int getColor(BlockState p_getColor_1_, @Nullable BlockAndTintGetter p_getColor_2_, @Nullable BlockPos p_getColor_3_, int p_getColor_4_) {
				if(p_getColor_4_==0){
					if(p_getColor_1_.getValue(BlockFruitLeaves.AGE)>3)
						return 0xFFFFFF;
					else
						return BiomeColors.getAverageFoliageColor(p_getColor_2_,p_getColor_3_);
				}
				return 0xFFFFFF;
			}
		},ModBlockRegistry.SakuraLeaves);
		
		event.getBlockColors().register(new BlockColor() {
			@Override
			public int getColor(BlockState p_getColor_1_, @Nullable BlockAndTintGetter p_getColor_2_, @Nullable BlockPos p_getColor_3_, int p_getColor_4_) {
				if(p_getColor_4_==0){
					if(p_getColor_1_.getValue(BlockFruitLeaves.AGE)>3)
						return 0xFFFFFF;
					else
						return BiomeColors.getAverageFoliageColor(p_getColor_2_,p_getColor_3_);
				}
				return 0xFFFFFF;
			}
		},ModBlockRegistry.ForsythiaLeaves);
		
		event.getBlockColors().register(new BlockColor() {
			@Override
			public int getColor(BlockState p_getColor_1_, @Nullable BlockAndTintGetter p_getColor_2_, @Nullable BlockPos pos, int p_getColor_4_) {
				if(p_getColor_4_==0&&pos!=null){
					return Mth.hsvToRgb((Math.abs(pos.getX())%16)/16F+(Math.abs(pos.getZ())%16)/16F,1F,1F);
				}
				return 0xFFFFFF;
			}
		},ModBlockRegistry.RandomSapling);
		
		event.getBlockColors().register(new BlockColor() {
			@Override
			public int getColor(BlockState p_getColor_1_, @Nullable BlockAndTintGetter p_getColor_2_, @Nullable BlockPos p_getColor_3_, int p_getColor_4_) {
				if(p_getColor_4_==0){
					return 0x5FC33B;
				}
				return 0xFFFFFF;
			}
		},ModBlockRegistry.CedarLeaves);
		
		event.getBlockColors().register(new BlockColor() {
			@Override
			public int getColor(BlockState p_getColor_1_, @Nullable BlockAndTintGetter p_getColor_2_, @Nullable BlockPos p_getColor_3_, int p_getColor_4_) {
				if(p_getColor_4_==0){
					return BiomeColors.getAverageFoliageColor(p_getColor_2_,p_getColor_3_);
				}
				return 0xFFFFFF;
			}
		},ModBlockRegistry.DouglasLeaves);
		
		event.getBlockColors().register(new BlockColor() {
			@Override
			public int getColor(BlockState p_getColor_1_, @Nullable BlockAndTintGetter p_getColor_2_, @Nullable BlockPos pos, int p_getColor_4_) {
				if(p_getColor_4_==0&&pos!=null){
					return Mth.hsvToRgb((Math.abs(pos.getX())%16)/16F+(Math.abs(pos.getZ())%16)/16F,1F,1F);
				}
				return 0xFFFFFF;
			}
		},ModBlockRegistry.DecorativeSapling);
	}
	
	@SubscribeEvent
	public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event){
		event.registerLayerDefinition(AirplaneModel.LAYER_LOCATION, AirplaneModel::createBodyLayer);
	}
	@SubscribeEvent
	public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event){
		event.registerEntityRenderer(ModEntities.AIRPLANE, AirplaneRenderer::new);
	}
	
}
