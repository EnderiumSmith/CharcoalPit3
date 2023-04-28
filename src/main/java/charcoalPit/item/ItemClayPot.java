package charcoalPit.item;

import java.util.List;

import charcoalPit.core.ModItemRegistry;
import charcoalPit.gui.ClayPotContainer2;
import charcoalPit.recipe.OreKilnRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;

public class ItemClayPot extends Item{
	
	public ItemClayPot() {
		super(new Item.Properties().tab(ModItemRegistry.CHARCOAL_PIT).stacksTo(16));
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		/*if(stack.hasTag()&&stack.getTag().contains("inventory")) {
			ItemStackHandler inv=new ItemStackHandler();
			inv.deserializeNBT(stack.getTag().getCompound("inventory"));
			if(OreKilnRecipe.oreKilnIsEmpty(inv)) {
				tooltip.add(new TextComponent("Empty"));
			}else {
				ItemStack out=OreKilnRecipe.OreKilnGetOutput(stack.getTag().getCompound("inventory"), Minecraft.getInstance().level);
				if(out.isEmpty()) {
					tooltip.add(new TextComponent(ChatFormatting.DARK_RED+"Invalid"+" ("+OreKilnRecipe.oreKilnGetOreAmount(inv)+"/8)"));
				}else {
					Component tx=out.getHoverName().plainCopy().append(new TextComponent(" x"+out.getCount()));
					tx.getStyle().applyFormat(ChatFormatting.GREEN);
					tooltip.add(tx);
				}
			}
		}*/
		if(stack.hasTag()&&stack.getTag().contains("result")){
			if(stack.getTag().getBoolean("empty")){
				tooltip.add(new TextComponent("Empty"));
			}else{
				ItemStack result=ItemStack.of(stack.getTag().getCompound("result"));
				ItemStackHandler inv=new ItemStackHandler();
				inv.deserializeNBT(stack.getTag().getCompound("inventory"));
				if(result.isEmpty()){
					tooltip.add(new TextComponent(ChatFormatting.DARK_RED+"Invalid"+" ("+OreKilnRecipe.oreKilnGetOreAmount(inv)+"/4)"));
				}else{
					tooltip.add(new TextComponent(ChatFormatting.GREEN+"").append(result.getHoverName()).append(new TextComponent(" x"+result.getCount())).withStyle(ChatFormatting.GREEN));
				}
			}
		}
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		if(!worldIn.isClientSide&&playerIn.getItemInHand(handIn).getCount()==1) {
			NetworkHooks.openGui((ServerPlayer) playerIn, new MenuProvider() {
				
				@Override
				public AbstractContainerMenu createMenu(int arg0, Inventory arg1, Player arg2) {
					return new ClayPotContainer2(arg0, arg1, arg2.getInventory().selected, worldIn);
				}
				
				@Override
				public Component getDisplayName() {
					return new TranslatableComponent("screen.charcoal_pit.clay_pot");
				}
			}, buf -> buf.writeByte((byte) playerIn.getInventory().selected));
		}
		return super.use(worldIn, playerIn, handIn);
	}
	
}
