package charcoalPit.block;

import charcoalPit.tile.TileWrathLantern;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockWrathLantern extends LanternBlock implements EntityBlock {
	public BlockWrathLantern(Properties p_49795_) {
		super(p_49795_);
	}
	
	@Override
	public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
		super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
		pTooltip.add(new TextComponent("EvE kills count as PvE").withStyle(ChatFormatting.DARK_RED));
		pTooltip.add(new TextComponent("Area: 9x9x9").withStyle(ChatFormatting.DARK_GRAY));
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new TileWrathLantern(pPos,pState);
	}
}
