package charcoalPit.block;

import charcoalPit.tile.TileBloom;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockBloom extends Block implements EntityBlock {
	
	public static final VoxelShape BOX= Shapes.box(2D/16D, 0D, 2D/16D, 14D/16D, 12D/16D, 14D/16D);
	
	public BlockBloom(Properties p_49795_) {
		super(p_49795_);
	}
	
	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return BOX;
	}
	
	@Override
	public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
		return 9;
	}
	
	public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
		if (!pEntity.fireImmune() && pEntity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)pEntity)) {
			pEntity.hurt(DamageSource.HOT_FLOOR, 1.0F);
		}
		
		super.stepOn(pLevel, pPos, pState, pEntity);
	}
	
	@Override
	public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
		TileBloom tile=(TileBloom) level.getBlockEntity(pos);
		if(tile.workCount<tile.items.getCount()){
			state.getBlock().playerWillDestroy(level, pos, state, player);
			player.causeFoodExhaustion(0.01F);
			level.playSound(player, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1F, 1F);
			tile.workCount++;
			return false;
		}
		return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
	}
	
	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.hasBlockEntity() && (!state.is(newState.getBlock()) || !newState.hasBlockEntity())) {
			((TileBloom)worldIn.getBlockEntity(pos)).dropInventory();
			worldIn.removeBlockEntity(pos);
		}
	}
	
	@Override
	public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack stack) {
		if(stack.hasTag()&&stack.getTag().contains("items")) {
			((TileBloom)pLevel.getBlockEntity(pPos)).items=ItemStack.of(stack.getTag().getCompound("items"));
		}
		super.setPlacedBy(pLevel, pPos, pState, pPlacer, stack);
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new TileBloom(pPos,pState);
	}
	
	@Override
	public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
		super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
		if(pStack.hasTag()&&pStack.getTag().contains("items")) {
			ItemStack stack = ItemStack.of(pStack.getTag().getCompound("items"));
			pTooltip.add(new TextComponent("").append(stack.getHoverName()).append(new TextComponent(" x" + stack.getCount())));
		}
	}
}
