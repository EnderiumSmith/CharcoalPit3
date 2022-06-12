package charcoalPit.item;

import charcoalPit.core.MethodHelper;
import charcoalPit.fluid.ModFluidRegistry;
import charcoalPit.recipe.FluidIngredient;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemOilLamp extends ItemNameBlockItem {
	
	
	public ItemOilLamp(Block p_41579_, Properties p_41580_) {
		super(p_41579_, p_41580_);
	}
	
	@Override
	public boolean isBarVisible(ItemStack pStack) {
		return true;
	}
	
	@Override
	public int getBarWidth(ItemStack pStack) {
		return Math.round((float)getFluid(pStack) * 13.0F / 4000F);
	}
	
	@Override
	public int getBarColor(ItemStack pStack) {
		float stackMaxDamage = 4000F;
		float f = Math.max(0.0F, ((float)getFluid(pStack)) / stackMaxDamage);
		return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
	}
	
	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		return new FluidHandlerItemStack(stack, 4000){
			@Override
			public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
				return FluidIngredient.isFluidInTag(stack, MethodHelper.SEED_OIL);
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
	public InteractionResult place(BlockPlaceContext pContext) {
		if(getFluid(pContext.getItemInHand())<25) {
			return InteractionResult.FAIL;
		}
		if (!pContext.canPlace()) {
			return InteractionResult.FAIL;
		} else {
			BlockPlaceContext blockplacecontext = this.updatePlacementContext(pContext);
			if (blockplacecontext == null) {
				return InteractionResult.FAIL;
			} else {
				BlockState blockstate = this.getPlacementState(blockplacecontext);
				if (blockstate == null) {
					return InteractionResult.FAIL;
				} else if (!this.placeBlock(blockplacecontext, blockstate)) {
					return InteractionResult.FAIL;
				} else {
					BlockPos blockpos = blockplacecontext.getClickedPos();
					Level level = blockplacecontext.getLevel();
					Player player = blockplacecontext.getPlayer();
					ItemStack itemstack = blockplacecontext.getItemInHand();
					BlockState blockstate1 = level.getBlockState(blockpos);
					if (blockstate1.is(blockstate.getBlock())) {
						blockstate1 = this.updateBlockStateFromTag(blockpos, level, itemstack, blockstate1);
						this.updateCustomBlockEntityTag(blockpos, level, player, itemstack, blockstate1);
						blockstate1.getBlock().setPlacedBy(level, blockpos, blockstate1, player, itemstack);
						if (player instanceof ServerPlayer) {
							CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, blockpos, itemstack);
						}
					}
					
					level.gameEvent(player, GameEvent.BLOCK_PLACE, blockpos);
					SoundType soundtype = blockstate1.getSoundType(level, blockpos, pContext.getPlayer());
					level.playSound(player, blockpos, this.getPlaceSound(blockstate1, level, blockpos, pContext.getPlayer()), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
					if (player == null || !player.getAbilities().instabuild) {
						FluidStack fluid=FluidStack.loadFluidStackFromNBT(itemstack.getTag().getCompound("Fluid"));
						fluid.setAmount(fluid.getAmount()-25);
						itemstack.addTagElement("Fluid",fluid.writeToNBT(new CompoundTag()));
					}
					
					return InteractionResult.sidedSuccess(level.isClientSide);
				}
			}
		}
	}
	
	static Capability<IFluidHandler> FLUID = CapabilityManager.get(new CapabilityToken<>(){});
	
	private BlockState updateBlockStateFromTag(BlockPos pPos, Level pLevel, ItemStack pStack, BlockState pState) {
		BlockState blockstate = pState;
		CompoundTag compoundtag = pStack.getTag();
		if (compoundtag != null) {
			CompoundTag compoundtag1 = compoundtag.getCompound("BlockStateTag");
			StateDefinition<Block, BlockState> statedefinition = pState.getBlock().getStateDefinition();
			
			for(String s : compoundtag1.getAllKeys()) {
				Property<?> property = statedefinition.getProperty(s);
				if (property != null) {
					String s1 = compoundtag1.get(s).getAsString();
					blockstate = updateState(blockstate, property, s1);
				}
			}
		}
		
		if (blockstate != pState) {
			pLevel.setBlock(pPos, blockstate, 2);
		}
		
		return blockstate;
	}
	
	private static <T extends Comparable<T>> BlockState updateState(BlockState pState, Property<T> pProperty, String pValueIdentifier) {
		return pProperty.getValue(pValueIdentifier).map((p_40592_) -> {
			return pState.setValue(pProperty, p_40592_);
		}).orElse(pState);
	}
}
