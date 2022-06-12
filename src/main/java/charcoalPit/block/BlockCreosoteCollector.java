package charcoalPit.block;

import java.util.List;

import charcoalPit.tile.TileBarrel;
import charcoalPit.tile.TileCeramicPot;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.lwjgl.glfw.GLFW;

import charcoalPit.tile.TileCreosoteCollector;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.TooltipFlag;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockCreosoteCollector extends Block implements EntityBlock {

	public BlockCreosoteCollector(Properties properties) {
		super(properties);
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, BlockGetter worldIn, List<Component> tooltip,
			TooltipFlag flagIn) {
		if(InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
			tooltip.add(new TextComponent("\u00A77"+"Collects creosote oil produced by log/coal piles above"));
			tooltip.add(new TextComponent("\u00A77"+"Collection area is a 9x9 '+' shape"));
			tooltip.add(new TextComponent("\u00A77"+"Piles need to be connected to funnel"));
			tooltip.add(new TextComponent("\u00A77"+"Creosote oil only flows down between piles"));
			tooltip.add(new TextComponent("\u00A77"+"If redstone signal is applied:"));
			tooltip.add(new TextComponent("\u00A77"+"-Funnel will also collect from neighboring funnels"));
			tooltip.add(new TextComponent("\u00A77"+"-Funnel will auto output creosote oil"));
			tooltip.add(new TextComponent("\u00A77"+"Creosote oil can only be extracted from the bottom"));
			tooltip.add(new TextComponent("\u00A77"+"A line of funnels works best"));
		}else {
			tooltip.add(new TextComponent("\u00A77"+"<Hold Shift>"+"\u00A77"));
		}
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TileCreosoteCollector(pos,state);
	}
	
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level plevel, BlockState pstate, BlockEntityType<T> ptype) {
		return plevel.isClientSide()?null:(level,blockpos,blockstate,t)-> {
			if (t instanceof TileCreosoteCollector tile) {
				tile.tick();
			}
		};
	}
	
	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player,
			InteractionHand handIn, BlockHitResult hit) {
		TileCreosoteCollector tile=(TileCreosoteCollector)worldIn.getBlockEntity(pos);
		if(worldIn.isClientSide)
			return player.getItemInHand(handIn).getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).isPresent()?InteractionResult.SUCCESS:InteractionResult.FAIL;
		else {
			return FluidUtil.interactWithFluidHandler(player, handIn, tile.external)?InteractionResult.SUCCESS:InteractionResult.FAIL;
		}
	}

}
