package charcoalPit.item;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class ItemAnimalCage extends Item {
	public ItemAnimalCage(Properties pProperties) {
		super(pProperties);
	}
	
	@Override
	public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity entity, InteractionHand pUsedHand) {
		if(pStack.hasTag())
			return InteractionResult.PASS;
		if(entity instanceof Animal||entity instanceof AbstractVillager){
			if ((entity.getBbHeight() <= 0.875F && entity.getBbWidth() <= 0.875F)||
					entity.getClass()==Parrot.class||
					(entity.getClass()==Villager.class && entity.isBaby())) {
				//can fit in box;
				if(pPlayer.level.isClientSide){
					return InteractionResult.SUCCESS;
				}
				ItemStack stack = new ItemStack(this);
				CompoundTag data = new CompoundTag();
				entity.ejectPassengers();
				if (entity.save(data)) {
					entity.remove(Entity.RemovalReason.UNLOADED_WITH_PLAYER);
					stack.addTagElement("AnimalData", data);
					stack.getTag().putString("AnimalName",entity.getDisplayName().getString());
					pPlayer.getItemInHand(pUsedHand).shrink(1);
					ItemHandlerHelper.giveItemToPlayer(pPlayer,stack,pPlayer.getInventory().selected);
					return InteractionResult.SUCCESS;
				} else {
					return InteractionResult.FAIL;
				}
			}
		}
		return InteractionResult.PASS;
	}
	
	@Override
	public InteractionResult useOn(UseOnContext pContext) {
		BlockPos pos=pContext.getClickedPos().relative(pContext.getClickedFace());
		ItemStack stack=pContext.getItemInHand();
		if(stack.hasTag()&&stack.getTag().contains("AnimalData")){
			if(pContext.getLevel().isClientSide)
				return InteractionResult.SUCCESS;
			CompoundTag data=stack.getTag().getCompound("AnimalData");
			Optional<Entity> entity=EntityType.create(data,pContext.getLevel());
			entity.ifPresent((animal)->{
				animal.setPos(pos.getX()+0.5D,pos.getY(),pos.getZ()+0.5F);
				pContext.getLevel().addFreshEntity(animal);
			});
			stack.shrink(1);
			ItemHandlerHelper.giveItemToPlayer(pContext.getPlayer(), new ItemStack(this),pContext.getPlayer().getInventory().selected);
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack) {
		return stack.hasTag()?1:16;
	}
	
	@Override
	public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
		if(pStack.hasTag()&&pStack.getTag().contains("AnimalName")){
			pTooltipComponents.add(new TextComponent(pStack.getTag().getString("AnimalName")));
		}
		super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
	}
}
