package charcoalPit.core;

import charcoalPit.CharcoalPit;
import charcoalPit.block.BlockCeramicPot;
import charcoalPit.entity.Airplane;
import charcoalPit.item.ItemMusket;
import charcoalPit.item.tool.ModTiers;
import charcoalPit.recipe.SquisherRecipe;
import charcoalPit.tile.TileWrathLantern;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.entity.player.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.event.world.BlockEvent.NeighborNotifyEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;

@EventBusSubscriber(modid=CharcoalPit.MODID, bus=Bus.FORGE)
public class PileIgnitr {
	
	
	@SubscribeEvent
	public static void checkIgnition(NeighborNotifyEvent event){
		if(!event.isCanceled()&&
				event.getWorld().getBlockState(event.getPos()).getBlock()==Blocks.FIRE){
			for(Direction facing:event.getNotifiedSides()){
				BlockPos pos=event.getPos().relative(facing);
				if(event.getWorld().getBlockState(pos).getBlock()==ModBlockRegistry.LogPile){
					//found log pile to ignite
					igniteLogs(event.getWorld(),pos);
					
				}else if(event.getWorld().getBlockState(pos).getBlock()==Blocks.COAL_BLOCK){
					//found coal pile to ignite
					igniteCoal(event.getWorld(),pos);
				}
			}
		}
	}
	public static void igniteLogs(LevelAccessor world, BlockPos pos){
		if(world.getBlockState(pos).getBlock()==ModBlockRegistry.LogPile){
			world.setBlock(pos, ModBlockRegistry.ActiveLogPile.defaultBlockState().setValue(BlockStateProperties.AXIS, world.getBlockState(pos).getValue(BlockStateProperties.AXIS)),2);
			Direction[] neighbors=Direction.values();
			for(int i=0;i<neighbors.length;i++){
				igniteLogs(world, pos.relative(neighbors[i]));
			}
		}
	}
	public static void igniteCoal(LevelAccessor world, BlockPos pos){
		if(world.getBlockState(pos).getBlock()==Blocks.COAL_BLOCK){
			world.setBlock(pos, ModBlockRegistry.ActiveCoalPile.defaultBlockState(),2);
			Direction[] neighbors=Direction.values();
			for(int i=0;i<neighbors.length;i++){
				igniteCoal(world, pos.relative(neighbors[i]));
			}
		}
	}
	@SubscribeEvent(priority=EventPriority.HIGH)
	public static void placeKiln(PlayerInteractEvent.RightClickBlock event) {
		//undye pots
		if(!event.isCanceled()&&event.getWorld().getBlockState(event.getPos()).getBlock()==Blocks.WATER_CAULDRON&&
				event.getWorld().getBlockState(event.getPos()).getValue(LayeredCauldronBlock.LEVEL)>0) {
			Block block=Block.byItem(event.getItemStack().getItem());
			if(block instanceof BlockCeramicPot&&block!=ModBlockRegistry.CeramicPot) {
				ItemStack stack=new ItemStack(ModBlockRegistry.CeramicPot, 1);
				stack.setTag(event.getItemStack().getTag());
				event.getPlayer().setItemInHand(event.getHand(), stack);
				event.getWorld().setBlockAndUpdate(event.getPos(), Blocks.CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, event.getWorld().getBlockState(event.getPos()).getValue(LayeredCauldronBlock.LEVEL)-1));
				event.setUseBlock(Result.DENY);
				event.setUseItem(Result.DENY);
			}
		}
	}
	@SubscribeEvent
	public static void addLoot(LootTableLoadEvent event) {
		if(event.getName().toString().equals("minecraft:chests/end_city_treasure")){
			event.getTable().addPool(LootPool.lootPool().add(LootTableReference.lootTableReference(new ResourceLocation(CharcoalPit.MODID, "end_aeternalis"))).build());
		}
		if(event.getName().toString().equals("minecraft:entities/chicken")){
			event.getTable().addPool(LootPool.lootPool().add(LootTableReference.lootTableReference(new ResourceLocation(CharcoalPit.MODID,"leeks"))).build());
		}
		if(event.getName().toString().equals("minecraft:entities/squid")){
			event.getTable().addPool(LootPool.lootPool().add(LootTableReference.lootTableReference(new ResourceLocation(CharcoalPit.MODID,"calamari"))).build());
		}
		if(event.getName().toString().equals("minecraft:chests/simple_dungeon")){
			event.getTable().addPool(LootPool.lootPool().add(LootTableReference.lootTableReference(new ResourceLocation(CharcoalPit.MODID,"saplings"))).build());
		}
		if(event.getName().toString().equals("minecraft:chests/abandoned_mineshaft")){
			event.getTable().addPool(LootPool.lootPool().add(LootTableReference.lootTableReference(new ResourceLocation(CharcoalPit.MODID,"saplings"))).build());
		}
		if(event.getName().toString().equals("minecraft:chests/stronghold_corridor")){
			event.getTable().addPool(LootPool.lootPool().add(LootTableReference.lootTableReference(new ResourceLocation(CharcoalPit.MODID,"saplings"))).build());
		}
		if(event.getName().toString().equals("minecraft:chests/stronghold_crossing")){
			event.getTable().addPool(LootPool.lootPool().add(LootTableReference.lootTableReference(new ResourceLocation(CharcoalPit.MODID,"saplings"))).build());
		}
	}
	@SubscribeEvent
	public static void savePotInventory(ItemCraftedEvent event) {
		if(Block.byItem(event.getCrafting().getItem()) instanceof BlockCeramicPot) {
			for(int i=0;i<event.getInventory().getContainerSize();i++) {
				if(Block.byItem(event.getInventory().getItem(i).getItem()) instanceof BlockCeramicPot) {
					event.getCrafting().setTag(event.getInventory().getItem(i).getTag());
					return;
				}
			}
		}
	}
	@SubscribeEvent
	public static void furnaceFuel(FurnaceFuelBurnTimeEvent event) {
		if(event.getItemStack().getItem()==ModItemRegistry.CreosoteBucket)
			event.setBurnTime(4800);
		if(event.getItemStack().getItem()==ModItemRegistry.BioDieselBucket)
			event.setBurnTime(25600);
		if(event.getItemStack().getItem()==ModItemRegistry.CharcoalBlock)
			event.setBurnTime(16000);
		if(event.getItemStack().getItem()==ModItemRegistry.EthanolBucket)
			event.setBurnTime(15200);
	}
	
	@SubscribeEvent
	public static void mineCopper(PlayerEvent.HarvestCheck event){
		Block block=event.getTargetBlock().getBlock();
		if(block==Blocks.COPPER_ORE||block==Blocks.DEEPSLATE_COPPER_ORE||block==Blocks.RAW_COPPER_BLOCK){
			if(event.getPlayer().getInventory().getSelected().isCorrectToolForDrops(Blocks.COAL_ORE.defaultBlockState()))
				event.setCanHarvest(true);
		}
	}
	
	@SubscribeEvent
	public static void rapairTrident(AnvilUpdateEvent event){
		if(event.getLeft().isDamageableItem()&&event.getLeft().getItem()== Items.TRIDENT&&MethodHelper.isItemInTag(event.getRight(),MethodHelper.ORICHALCUM)){
			int l2 = Math.min(event.getLeft().getDamageValue(), event.getLeft().getMaxDamage() / 4);
			ItemStack stack1=event.getLeft().copy();
			int i3;
			for(i3=0;l2>0&&i3<event.getRight().getCount();i3++){
				int j3=stack1.getDamageValue()-l2;
				stack1.setDamageValue(j3);
				l2=Math.min(stack1.getDamageValue(),stack1.getMaxDamage()/4);
			}
			event.setMaterialCost(i3);
			event.setCost(event.getCost()+i3);
			stack1.setRepairCost(AnvilMenu.calculateIncreasedRepairCost(stack1.getBaseRepairCost()));
			event.setOutput(stack1);
		}else if(event.getLeft().isDamageableItem()&&event.getRight().getItem()==ModItemRegistry.NetherShard){
			ItemStack stack1=event.getLeft().copy();
			stack1.setDamageValue(0);
			event.setMaterialCost(1);
			event.setCost(event.getCost()+1);
			stack1.setRepairCost(AnvilMenu.calculateIncreasedRepairCost(stack1.getBaseRepairCost()));
			event.setOutput(stack1);
		}
	}
	
	@SubscribeEvent
	public static void aquaAffinity(PlayerEvent.BreakSpeed event){
		if(event.getPlayer().isEyeInFluid(FluidTags.WATER)&&isOrichalcumTool(event.getPlayer().getMainHandItem().getItem())&&!EnchantmentHelper.hasAquaAffinity(event.getPlayer())){
			event.setNewSpeed(event.getNewSpeed()*5);
		}
	}
	
	public static boolean isOrichalcumTool(Item item){
		if(item instanceof DiggerItem tool){
			return tool.getTier()== ModTiers.ORICHALCUM;
		}
		return false;
	}
	
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void RenderMusket(RenderPlayerEvent.Pre event){
		if(event.getPlayer().getMainHandItem().getItem()==ModItemRegistry.musket&&!event.getPlayer().swinging&& ItemMusket.isLoaded(event.getPlayer().getMainHandItem())){
			if(event.getPlayer().getMainArm()== HumanoidArm.RIGHT)
				event.getRenderer().getModel().rightArmPose= HumanoidModel.ArmPose.BOW_AND_ARROW;
			else
				event.getRenderer().getModel().leftArmPose=HumanoidModel.ArmPose.BOW_AND_ARROW;
		}
	}
	
	@SubscribeEvent
	public static void WrathLanternKill(LivingDeathEvent event){
		if(!event.getEntityLiving().level.isClientSide()&&event.getSource().getEntity() instanceof LivingEntity &&!(event.getSource().getEntity() instanceof Player)){
			//entity exists but not player
			Level level=event.getEntityLiving().level;
			BlockPos entityPos=event.getEntityLiving().blockPosition();
			for(int i=-1;i<2;i+=1){
				for(int j=-1;j<2;j+=1){
					LevelChunk chunk=level.getChunkAt(entityPos.offset(i*16,0,j*16));
					for(var entry:chunk.getBlockEntities().entrySet()){
						if(entry.getValue() instanceof TileWrathLantern){
							BlockPos pos=entry.getValue().getBlockPos();
							if(Math.abs(pos.getX()-entityPos.getX())<=4&&Math.abs(pos.getY()-entityPos.getY())<=4&&Math.abs(pos.getZ()-entityPos.getZ())<=4){
								event.getEntityLiving().setLastHurtByPlayer(FakePlayerFactory.getMinecraft((ServerLevel) level));
								return;
							}
						}
					}
				}
			}
		}
	}
	
	/*@SubscribeEvent
	public static void FixArrowDamageEvent(LivingDamageEvent event){
		if(!event.isCanceled()&&event.getSource().getDirectEntity() instanceof Arrow arrow){
			if(arrow.potion==Potions.EMPTY)
				return;
			if(event.getEntityLiving().getMobType()==MobType.UNDEAD){
				for(MobEffectInstance effectInstance:arrow.potion.getEffects()){
					if(effectInstance.getEffect()==MobEffects.HEAL){
						event.getEntityLiving().invulnerableTime=0;
					}
				}
			}else{
				for(MobEffectInstance effectInstance:arrow.potion.getEffects()){
					if(effectInstance.getEffect()==MobEffects.HARM){
						event.getEntityLiving().invulnerableTime=0;
					}
				}
			}
		}
	}*/
	
	@SubscribeEvent
	public static void CropTrades(VillagerTradesEvent event){
		Trades.CropTrades(event);
	}
	
	@SubscribeEvent
	public static void SaplingTrades(WandererTradesEvent event){
		Trades.SaplingTrades(event);
	}
}
