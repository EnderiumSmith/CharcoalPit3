package charcoalPit.item;

import charcoalPit.core.ModBlockRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stats.Stats;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;

public class ItemBarrel extends BlockItem {
    public ItemBarrel(Block blockIn, Item.Properties builder){
        super(blockIn,builder);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new FluidHandlerItemStack(stack,16000){
            @Override
            public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
                return stack.getFluid().getAttributes().getTemperature()<450&&!stack.getFluid().getAttributes().isGaseous();
            }

            @Override
            public boolean canFillFluidType(FluidStack fluid) {
                return isFluidValid(0,fluid);
            }
        };
    }
    
    @Override
    public int getItemStackLimit(ItemStack stack) {
        if(stack.hasTag()&&stack.getTag().contains("Fluid"))
            return FluidStack.loadFluidStackFromNBT(stack.getTag().getCompound("Fluid")).isEmpty()?super.getItemStackLimit(stack):1;
        return super.getItemStackLimit(stack);
    }
    
    public static boolean isFluidEmpty(ItemStack stack){
        if(stack.hasTag()&&stack.getTag().contains("Fluid")){
            return FluidStack.loadFluidStackFromNBT(stack.getTag().getCompound("Fluid")).isEmpty();
        }
        return true;
    }
    
    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        if(ItemBarrel.isFluidEmpty(stack)&&!context.getPlayer().isShiftKeyDown()){
            HitResult trace=getPlayerPOVHitResult(context.getLevel(),context.getPlayer(), ClipContext.Fluid.SOURCE_ONLY);
            if(trace.getType()==HitResult.Type.BLOCK){
                BlockHitResult blocktrace=(BlockHitResult)trace;
                BlockPos pos=blocktrace.getBlockPos();
                Direction dir=blocktrace.getDirection();
                BlockPos pos2=pos.relative(dir);
                if (context.getLevel().mayInteract(context.getPlayer(), pos) && context.getPlayer().mayUseItemAt(pos2, dir, stack)) {
                    FluidState state=context.getLevel().getFluidState(pos);
                    if(state.getType()==Fluids.WATER&&state.isSource()){
                        int s=0;
                        for(Direction dir2:Direction.Plane.HORIZONTAL){
                            if(context.getLevel().getFluidState(pos.relative(dir2)).getType()==Fluids.WATER&&
                            context.getLevel().getFluidState(pos.relative(dir2)).isSource())
                                s++;
                        }
                        if(s>=2){
                            context.getPlayer().awardStat(Stats.ITEM_USED.get(this));
                            context.getPlayer().playSound(SoundEvents.BUCKET_FILL,1F,1F);
                            ItemStack stack2=new ItemStack(ModBlockRegistry.Barrel);
                            stack2.addTagElement("Fluid",new FluidStack(Fluids.WATER,16000).writeToNBT(new CompoundTag()));
                            ItemHandlerHelper.giveItemToPlayer(context.getPlayer(),stack2);
                            context.getPlayer().getItemInHand(context.getHand()).shrink(1);
                            if(context.getLevel().getBlockState(pos).getBlock() instanceof BucketPickup){
                                ((BucketPickup)context.getLevel().getBlockState(pos).getBlock()).pickupBlock(context.getLevel(),pos,context.getLevel().getBlockState(pos));
                            }
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }
}
