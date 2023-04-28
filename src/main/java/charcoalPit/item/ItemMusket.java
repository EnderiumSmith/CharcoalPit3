package charcoalPit.item;

import charcoalPit.core.MethodHelper;
import charcoalPit.enchant.ModEnchantments;
import charcoalPit.item.tool.ModTiers;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ChorusFruitItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemMusket extends Item {
	private final Multimap<Attribute, AttributeModifier> defaultModifiers;
	
	public ItemMusket(Properties pProperties) {
		super(pProperties);
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 4F, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.4F, AttributeModifier.Operation.ADDITION));
		this.defaultModifiers = builder.build();
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
		ItemStack stack=pPlayer.getItemInHand(pUsedHand);
		if(pUsedHand==InteractionHand.MAIN_HAND&&!pPlayer.isInWater()&&isLoaded(stack)){
			if(!pLevel.isClientSide()) {
				validateEnchants(stack);
				shoot(pLevel, pPlayer, pUsedHand, stack);
			}
			makeShootEffects(pLevel,pPlayer);
			if(!pPlayer.isCreative()) {
				if(pPlayer.getRandom().nextFloat()<0.15F*EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE,stack)){
					boolean found_ammo=false;
					boolean found_powder=false;
					for(ItemStack stack1:pPlayer.inventoryMenu.getItems()){
						if(MethodHelper.isItemInTag(stack1,MethodHelper.BULLETS))
							found_ammo=true;
						if(MethodHelper.isItemInTag(stack1,MethodHelper.GUN_POWDER))
							found_powder=true;
					}
					if(found_ammo&&found_powder){
						for(ItemStack stack1:pPlayer.inventoryMenu.getItems()){
							if(found_ammo&&MethodHelper.isItemInTag(stack1,MethodHelper.BULLETS)){
								stack1.shrink(1);
								found_ammo=false;
							}
							if(found_powder&&MethodHelper.isItemInTag(stack1,MethodHelper.GUN_POWDER)){
								stack1.shrink(1);
								found_powder=false;
							}
							if(!found_ammo&&!found_powder){
								break;
							}
						}
					}else{
						setLoaded(stack,false);
					}
				}else {
					setLoaded(stack, false);
				}
				stack.hurtAndBreak(1, pPlayer, (p) -> {
					p.broadcastBreakEvent(pUsedHand);
				});
			}
			pPlayer.getCooldowns().addCooldown(this,10);
			return InteractionResultHolder.consume(stack);
		}
		return super.use(pLevel, pPlayer, pUsedHand);
	}
	
	public void validateEnchants(ItemStack stack){
		Map<Enchantment,Integer> enchants=EnchantmentHelper.getEnchantments(stack);
		Map<Enchantment,Integer> new_enchants=new HashMap<>();
		for(var entry:enchants.entrySet()){
			boolean allow=true;
			for(var entry2:new_enchants.entrySet()){
				if(!checkEnchants(entry.getKey(),entry2.getKey())){
					allow=false;
					break;
				}
			}
			if(allow)
				new_enchants.put(entry.getKey(),entry.getValue());
		}
		stack.getTag().remove("Enchantments");
		EnchantmentHelper.setEnchantments(new_enchants,stack);
	}
	
	public static boolean checkEnchants(Enchantment enchantment1,Enchantment enchantment2){
		//damage
		if(enchantment1==Enchantments.POWER_ARROWS)
			return enchantment2!=Enchantments.MULTISHOT&&enchantment2!=Enchantments.PIERCING;
		if(enchantment1==Enchantments.MULTISHOT)
			return enchantment2!=Enchantments.POWER_ARROWS&&enchantment2!=Enchantments.PIERCING;
		if(enchantment1==Enchantments.PIERCING)
			return enchantment2!=Enchantments.MULTISHOT&&enchantment2!=Enchantments.POWER_ARROWS;
		//bullet
		if(enchantment1==Enchantments.FLAMING_ARROWS)
			return enchantment2!=Enchantments.PUNCH_ARROWS&&enchantment2!=ModEnchantments.SILVER_BULLET;
		if(enchantment1==Enchantments.PUNCH_ARROWS)
			return enchantment2!=Enchantments.FLAMING_ARROWS&&enchantment2!=ModEnchantments.SILVER_BULLET;
		if(enchantment1==ModEnchantments.SILVER_BULLET)
			return enchantment2!=Enchantments.FLAMING_ARROWS&&enchantment2!=Enchantments.PUNCH_ARROWS;
		return true;
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return enchantment == Enchantments.UNBREAKING || enchantment == Enchantments.QUICK_CHARGE ||
				(enchantment == Enchantments.POWER_ARROWS &&
						EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, stack) <= 0 &&
						EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING, stack) <= 0) ||
				(enchantment == Enchantments.MULTISHOT &&
						EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack) <= 0 &&
						EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING, stack) <= 0) ||
				(enchantment == Enchantments.PIERCING &&
						EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, stack) <= 0 &&
						EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack) <= 0) ||
				(enchantment == Enchantments.FLAMING_ARROWS &&
						EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack) <= 0 &&
						EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.SILVER_BULLET, stack) <= 0) ||
				(enchantment == Enchantments.PUNCH_ARROWS &&
						EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) <= 0 &&
						EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.SILVER_BULLET, stack) <= 0) ||
				(enchantment == ModEnchantments.SILVER_BULLET &&
						EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) <= 0 &&
						EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack) <= 0);
	}
	
	@Override
	public int getEnchantmentValue() {
		return ModTiers.STEEL.getEnchantmentValue();
	}
	
	@Override
	public boolean isEnchantable(ItemStack pStack) {
		return true;
	}
	
	@Override
	public UseAnim getUseAnimation(ItemStack pStack) {
		return UseAnim.BOW;
	}
	
	@Override
	public boolean isBarVisible(ItemStack pStack) {
		return super.isBarVisible(pStack)||isLoaded(pStack);
	}
	
	@Override
	public int getBarWidth(ItemStack pStack) {
		return isLoaded(pStack)? 13 : super.getBarWidth(pStack);
	}
	
	@Override
	public int getBarColor(ItemStack pStack) {
		return isLoaded(pStack)? Mth.hsvToRgb(0.6F,1.0F, 1.0F) : super.getBarColor(pStack);
	}
	
	public static boolean isLoaded(ItemStack stack) {
		CompoundTag compoundtag = stack.getTag();
		return compoundtag != null && compoundtag.contains("Loaded");
	}
	
	public static void setLoaded(ItemStack stack, boolean load) {
		CompoundTag compoundtag = stack.getOrCreateTag();
		if(load)
			compoundtag.putBoolean("Loaded", true);
		else
			compoundtag.remove("Loaded");
	}
	
	public static void makeShootEffects(Level level,Player shooter){
		level.playSound(shooter,shooter.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS,1F,1F);
		Vec3 lookVec=shooter.getLookAngle();
		Vec3 pos=shooter.getEyePosition();
		Vec3 origin=pos.add(lookVec);
		for(int i=0;i<64;i++){
			Vec3 spread=getShootVector(shooter,45F);
			if(!level.isClientSide()){
				level.players().stream().filter(x->origin.distanceToSqr(x.position())<32*32).forEach(x->{
					((ServerLevel) level).sendParticles((ServerPlayer) x, ParticleTypes.SMOKE, false, origin.x, origin.y, origin.z, 4, spread.x, spread.y, spread.z, 0.1F);
				});
				//((ServerLevel) level).sendParticles((ServerPlayer) shooter, ParticleTypes.SMOKE, false, origin.x, origin.y, origin.z, 4, spread.x, spread.y, spread.z, 0.1F);
			}
		}
	}
	
	public static void shoot(Level level, Player shooter, InteractionHand hand, ItemStack stack){
		//damage
		int bullets= EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT,stack)>0?3:1;
		float deviation=bullets>1?1.2F:0.6F;
		int piercing=1+EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING,stack);
		int power=EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS,stack);
		//effect
		int flame=EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS,stack);
		int punch=EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS,stack);
		int silver=EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.SILVER_BULLET,stack);
		final Vec3 INF=new Vec3(Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY);
		for(int i=0;i<bullets;i++){
			Vec3 start=new Vec3(shooter.getX(), shooter.getEyeY(), shooter.getZ());
			Vec3 lookVec=getShootVector(shooter,deviation).scale(200);
			Vec3 end=start.add(lookVec);
			BlockHitResult trace=level.clip(new ClipContext(start,end, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY,shooter));
			List<Entity> entities=level.getEntities(shooter,new AABB(start,trace.getLocation()), EntitySelector.NO_CREATIVE_OR_SPECTATOR);
			entities.stream().filter(x->x.getBoundingBox().clip(start,trace.getLocation()).isPresent()).filter(x->!x.getPassengers().contains(shooter))
					.sorted(Comparator.comparingDouble(x->start.distanceToSqr(x.getBoundingBox().clip(start,trace.getLocation()).orElse(INF)))).limit(piercing)
					.forEach(entity->{
						if(entity instanceof EnderMan enderMan){
							for(int t=0;t<64;t++){
								if(enderMan.teleport())
									break;
							}
						}else {
							if (entity instanceof LivingEntity livingEntity) {
								if (livingEntity.getLastHurtByMobTimestamp() == livingEntity.tickCount) {
									livingEntity.invulnerableTime = 0;
								}
							}
							float damage = 15 + shooter.getRandom().nextInt(11);
							if (power > 0) {
								damage *= 1F + 0.25F * (power + 1);
							}
							double distance = start.distanceToSqr(entity.getBoundingBox().clip(start, trace.getLocation()).orElse(INF));
							double chance = Math.max(0.05F, Math.min(0.95F, (16F - distance) / 16F));
							if (shooter.getRandom().nextFloat() < chance) {
								damage *= 1.5F;
							}
							DamageSource damageSource = DamageSource.playerAttack(shooter).setProjectile();
							if (silver > 0) {
								damageSource.bypassMagic();
							}
							if (entity.hurt(damageSource, damage)) {
								if(entity instanceof Player player){
									player.disableShield(true);
								}
								if (flame > 0) {
									entity.setSecondsOnFire(5);
								}
								if (punch > 0) {
									Vec3 vec = lookVec.multiply(1F, 0F, 1F).normalize().scale(0.6D * punch);
									entity.push(vec.x, 0.1F, vec.z);
								}
							}
						}
					});
		}
	}
	
	public static Vec3 getShootVector(LivingEntity shooter, float deviation){
		float angleX=shooter.getXRot();
		float angleY=shooter.getYRot();
		angleX+=shooter.getRandom().nextGaussian()*deviation;
		angleY+=shooter.getRandom().nextGaussian()*deviation;
		return calculateViewVector(angleX,angleY);
	}
	
	public static Vec3 calculateViewVector(float pitch,float yaw){
		float f = pitch * ((float)Math.PI / 180F);
		float f1 = -yaw * ((float)Math.PI / 180F);
		float f2 = Mth.cos(f1);
		float f3 = Mth.sin(f1);
		float f4 = Mth.cos(f);
		float f5 = Mth.sin(f);
		return new Vec3((double)(f3 * f4), (double)(-f5), (double)(f2 * f4));
	}
	
	
	public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
		pStack.hurtAndBreak(1, pAttacker, (p_43296_) -> {
			p_43296_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
		});
		return true;
	}
	
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot pEquipmentSlot) {
		return pEquipmentSlot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(pEquipmentSlot);
	}
	
	@Override
	public boolean isValidRepairItem(ItemStack pStack, ItemStack pRepairCandidate) {
		return ModTiers.STEEL.getRepairIngredient().test(pStack);
	}
}
