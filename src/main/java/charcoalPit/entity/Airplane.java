package charcoalPit.entity;

import charcoalPit.CharcoalPit;
import charcoalPit.core.MethodHelper;
import charcoalPit.core.ModItemRegistry;
import charcoalPit.recipe.FluidIngredient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.Nullable;

public class Airplane extends Mob {
	
	static final ResourceLocation LOOT_TABLE=new ResourceLocation(CharcoalPit.MODID,"entities/airplane");
	
	float deltaEngine=0F;
	float prop_travel=0F;
	public FluidTank fuel;
	private final static EntityDataAccessor<Float> BURN_TIME = SynchedEntityData.defineId(Airplane.class, EntityDataSerializers.FLOAT);
	
	public Airplane(Level level){
		this(ModEntities.AIRPLANE,level);
	}
	
	public Airplane(EntityType<? extends Mob> type, Level level) {
		super(type, level);
		fuel=new FluidTank(16000,fluid-> MethodHelper.isAvgas(fluid.getFluid()));
	}
	
	@Override
	protected ResourceLocation getDefaultLootTable() {
		return LOOT_TABLE;
	}
	
	@Override
	public boolean isPersistenceRequired() {
		return true;
	}
	
	@Override
	public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
		return false;
	}
	
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(BURN_TIME,1F);
	}
	
	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0D)
				.add(Attributes.KNOCKBACK_RESISTANCE,1D);
	}
	
	@Override
	protected void actuallyHurt(DamageSource pDamageSrc, float pDamageAmount) {
		super.actuallyHurt(pDamageSrc, pDamageAmount);
		if(this.isDeadOrDying()){
			this.level.explode(this,this.getX(),this.getY(),this.getZ(),3,true, Explosion.BlockInteraction.DESTROY);
		}
	}
	
	@Nullable
	@Override
	public Entity getControllingPassenger() {
		return this.getFirstPassenger();
	}
	
	@Override
	public boolean canBeControlledByRider() {
		return getControllingPassenger() instanceof Player;
	}
	
	@Override
	protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
		if(!this.isVehicle()&&!pPlayer.isSecondaryUseActive()){
			if(!this.level.isClientSide){
				pPlayer.startRiding(this);
			}
			return InteractionResult.sidedSuccess(this.level.isClientSide);
		}else{
			if(!this.isVehicle()&&pPlayer.isSecondaryUseActive()&&pPlayer.getItemInHand(pHand).isEmpty()){
				ItemStack plane=new ItemStack(ModItemRegistry.plane);
				plane.addTagElement("Fluid",fuel.getFluid().writeToNBT(new CompoundTag()));
				pPlayer.setItemInHand(pHand,plane);
				this.level.playSound(null,pPlayer,SoundEvents.ANVIL_USE,SoundSource.PLAYERS,1F,1F);
				this.remove(RemovalReason.DISCARDED);
			}
		}
		return super.mobInteract(pPlayer, pHand);
	}
	
	@Override
	public double getPassengersRidingOffset() {
		return 0.25F;
	}
	
	@Override
	public void positionRider(Entity pPassenger) {
		super.positionRider(pPassenger);
		float f3 = Mth.sin(this.yBodyRot * ((float)Math.PI / 180F));
		float f = Mth.cos(this.yBodyRot * ((float)Math.PI / 180F));
		float f1 = -0.5F;
		pPassenger.setPos(this.getX() + (double)(f1 * f3),
				this.getY() + this.getPassengersRidingOffset() + pPassenger.getMyRidingOffset(),
				this.getZ() - (double)(f1 * f));
	}
	
	@Override
	public void updateFallFlying() {
		if(!this.level.isClientSide) {
			boolean flag =  !this.isPassenger() && !this.hasEffect(MobEffects.LEVITATION);
			this.setSharedFlag(7,flag);
		}
	}
	
	@Override
	public void travel(Vec3 pTravelVector) {
			boolean engine = false;
			if(!this.level.isClientSide&&this.entityData.get(BURN_TIME)<=0){
				int burn=0;
				if(FluidIngredient.isFluidInTag(fuel.getFluid(),MethodHelper.BIODIESEL))
					burn=25600;
				else if(FluidIngredient.isFluidInTag(fuel.getFluid(),MethodHelper.DIESEL))
					burn=32000;
				else if(FluidIngredient.isFluidInTag(fuel.getFluid(),MethodHelper.KEROSENE))
					burn=28800;
				if(burn>0) {
					this.entityData.set(BURN_TIME,burn/1000F);
					fuel.drain(1, IFluidHandler.FluidAction.EXECUTE);
				}
			}
			if (this.getControllingPassenger() instanceof Player player) {
				float targetY=player.getYRot();
				float targetX=player.getXRot();
				targetX-=5;
				if(this.isOnGround()){
					targetX=-5;
				}
				this.setYRot(this.rotlerp2(this.getYRot(),targetY,3));
				this.setXRot(this.rotlerp2(this.getXRot(),targetX,3));
				this.yBodyRot=this.getYRot();
				if (player.jumping&&this.entityData.get(BURN_TIME)>0) {
					engine = true;
					this.setDeltaMovement(this.getDeltaMovement().add(this.getLookAngle().normalize().scale(0.02F)));
					if(!this.level.isClientSide){
						this.entityData.set(BURN_TIME,this.entityData.get(BURN_TIME)-4);
					}
				}
			}
			if (this.isOnGround() && !engine) {
				this.setDeltaMovement(this.getDeltaMovement().multiply(0.8F, 1F, 0.8F));
			}
			if(engine){
				deltaEngine++;
				this.level.playSound((Player) this.getControllingPassenger(),this, SoundEvents.GHAST_DEATH, SoundSource.PLAYERS,0.5F,2F);
			}
			deltaEngine*=0.95F;
			super.travel(pTravelVector);
	}
	
	private float rotlerp2(float pAngle, float pTargetAngle, float pMaxIncrease) {
		float f = Mth.wrapDegrees(pTargetAngle - pAngle);
		if (f > pMaxIncrease) {
			f = pMaxIncrease;
		}
		
		if (f < -pMaxIncrease) {
			f = -pMaxIncrease;
		}
		
		return pAngle + f;
	}
	
	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}
	
	@Override
	public boolean isAttackable() {
		return false;
	}
	
	@Override
	public boolean hurt(DamageSource pSource, float pAmount) {
		if(pSource.isFire()||pSource.isExplosion()||pSource.isFall()||pSource==DamageSource.FLY_INTO_WALL||pSource==DamageSource.OUT_OF_WORLD)
			return super.hurt(pSource, pAmount);
		else
			return false;
	}
	
	@Override
	public void aiStep() {
		super.aiStep();
		if(this.tickCount%100==0){
			this.heal(1);
		}
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		pCompound.put("fuel",fuel.writeToNBT(new CompoundTag()));
		pCompound.putFloat("burn_time",this.entityData.get(BURN_TIME));
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		fuel.readFromNBT(pCompound.getCompound("fuel"));
		this.entityData.set(BURN_TIME,pCompound.getFloat("burn_time"));
	}
}
