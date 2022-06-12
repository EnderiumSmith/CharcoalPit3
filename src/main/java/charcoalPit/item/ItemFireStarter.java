package charcoalPit.item;

import java.util.Optional;
import java.util.function.Predicate;

import charcoalPit.core.ModItemRegistry;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;

public class ItemFireStarter extends Item{

	public ItemFireStarter() {
		super(new Properties().tab(ModItemRegistry.CHARCOAL_PIT));
	}
	
	@Override
	public int getUseDuration(ItemStack stack) {
		return 30;
	}
	
	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		Vec3 eyePos=new Vec3(playerIn.getX(), playerIn.getEyeY(), playerIn.getZ());
		Vec3 rangedLookRot=playerIn.getLookAngle().scale(playerIn.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue());
		Vec3 lookVec=eyePos.add(rangedLookRot);
		BlockHitResult trace=worldIn.clip(new ClipContext(eyePos, lookVec, Block.OUTLINE, Fluid.NONE, playerIn));
		ItemStack stack= playerIn.getItemInHand(handIn);
		if(trace.getType()==Type.BLOCK) {
			EntityHitResult trace2=ItemFireStarter.rayTraceEntities(worldIn, null, eyePos, trace.getLocation(), new AABB(eyePos, trace.getLocation()), x->true);
			if(trace2==null) {
				playerIn.startUsingItem(handIn);
				return new InteractionResultHolder<ItemStack>(InteractionResult.SUCCESS, stack);
			}else {
				return new InteractionResultHolder<ItemStack>(InteractionResult.FAIL, stack);
			}
		}else {
			return new InteractionResultHolder<ItemStack>(InteractionResult.FAIL, stack);
		}
	}
	
	@Override
	public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
		Vec3 eyePos=new Vec3(player.getX(), player.getEyeY(), player.getZ());
		Vec3 rangedLookRot=player.getLookAngle().scale(player.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue());
		Vec3 lookVec=eyePos.add(rangedLookRot);
		BlockHitResult trace=player.level.clip(new ClipContext(eyePos, lookVec, Block.OUTLINE, Fluid.NONE, player));
		EntityHitResult trace2=ItemFireStarter.rayTraceEntities(player.level, null, eyePos, trace.getLocation(), new AABB(eyePos, trace.getLocation()), x->true);
		if(!player.level.isClientSide) {
			if(trace.getType()==Type.BLOCK&&trace2==null) {
				if(count==1) {
					BlockPos hit=new BlockPos(trace.getBlockPos().relative(trace.getDirection()));
					if(CampfireBlock.canLight(player.level.getBlockState(trace.getBlockPos()))){
						player.level.playSound(null, trace.getBlockPos(), SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1F, player.level.getRandom().nextFloat()*0.4F+0.8F);
						player.level.setBlock(trace.getBlockPos(), player.level.getBlockState(trace.getBlockPos()).setValue(BlockStateProperties.LIT, Boolean.valueOf(true)), 11);
						stack.shrink(1);
					}else if(BaseFireBlock.canBePlacedAt(player.level, hit, Direction.UP)) {
						BlockState blockstate1 = BaseFireBlock.getState(player.level, hit);
						player.level.setBlockAndUpdate(hit, blockstate1);
						player.level.playSound(null, hit, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1F, player.level.getRandom().nextFloat()*0.4F+0.8F);
						stack.shrink(1);
					}else {
						player.releaseUsingItem();
					}
				}
			}else {
				player.releaseUsingItem();
			}
		}else {
			if(trace.getType()==Type.BLOCK&&trace2==null) {
				player.level.addParticle(ParticleTypes.SMOKE, trace.getLocation().x, trace.getLocation().y, trace.getLocation().z, 0, 0, 0);
			}
		}
	}
	
	public static EntityHitResult rayTraceEntities(Level worldIn, Entity projectile, Vec3 startVec, Vec3 endVec, AABB boundingBox, Predicate<Entity> filter) {
	      double d0 = Double.MAX_VALUE;
	      Entity entity = null;

	      for(Entity entity1 : worldIn.getEntities(projectile, boundingBox, filter)) {
	         AABB axisalignedbb = entity1.getBoundingBox();
	         Optional<Vec3> optional = axisalignedbb.clip(startVec, endVec);
	         if (optional.isPresent()) {
	            double d1 = startVec.distanceToSqr(optional.get());
	            if (d1 < d0) {
	               entity = entity1;
	               d0 = d1;
	            }
	         }
	      }

	      return entity == null ? null : new EntityHitResult(entity);
	   }
	
}
