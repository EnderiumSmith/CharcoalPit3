package charcoalPit.item;

import charcoalPit.core.MethodHelper;
import charcoalPit.entity.Airplane;
import charcoalPit.recipe.FluidIngredient;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemAirplane extends Item {
	public ItemAirplane(Properties pProperties) {
		super(pProperties);
	}
	
	@Override
	public boolean isBarVisible(ItemStack pStack) {
		return true;
	}
	
	@Override
	public int getBarWidth(ItemStack pStack) {
		return Math.round((float)getFluid(pStack) * 13.0F / 16000F);
	}
	
	@Override
	public int getBarColor(ItemStack pStack) {
		float stackMaxDamage = 16000F;
		float f = Math.max(0.0F, ((float)getFluid(pStack)) / stackMaxDamage);
		return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
	}
	
	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		return new FluidHandlerItemStack(stack, 16000){
			@Override
			public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
				return MethodHelper.isAvgas(stack.getFluid());
			}
			
			@Override
			public boolean canFillFluidType(FluidStack fluid) {
				return isFluidValid(0,fluid);
			}
		};
	}
	
	public static int getFluid(ItemStack stack){
		if(stack.hasTag()&&stack.getTag().contains("Fluid")){
			return FluidStack.loadFluidStackFromNBT(stack.getTag().getCompound("Fluid")).getAmount();
		}
		return 0;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pFlag) {
		if(stack.hasTag()&&stack.getTag().contains("Fluid")){
			FluidStack fluid=FluidStack.loadFluidStackFromNBT(stack.getTag().getCompound("Fluid"));
			tooltip.add(fluid.getDisplayName().plainCopy().append(new TextComponent(":"+fluid.getAmount())));
		}else{
			tooltip.add(new TextComponent("Empty"));
		}
		super.appendHoverText(stack, pLevel, tooltip, pFlag);
	}
	
	@Override
	public InteractionResult useOn(UseOnContext pContext) {
		BlockPos pos=pContext.getClickedPos().relative(pContext.getClickedFace());
		if (!pContext.getLevel().isClientSide) {
			Airplane plane = new Airplane(pContext.getLevel());
			plane.setPos(pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F);
			ItemStack stack = pContext.getItemInHand();
			if(stack.hasTag()&&stack.getTag().contains("Fluid")) {
				plane.fuel.setFluid(FluidStack.loadFluidStackFromNBT(stack.getTag().getCompound("Fluid")));
			}
			pContext.getLevel().addFreshEntity(plane);
			stack.shrink(1);
		}
		return InteractionResult.SUCCESS;
	}
}
