package charcoalPit.core;

import charcoalPit.CharcoalPit;
import charcoalPit.recipe.FluidIngredient;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.tags.*;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.data.ForgeItemTagsProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Optional;

public class MethodHelper {
	
	public static final TagKey<Block> COKE_OVEN_WALLS=BlockTags.create(new ResourceLocation(CharcoalPit.MODID, "coke_oven_walls"));
	public static final TagKey<Block> BLOOMERY_WALLS=BlockTags.create(new ResourceLocation(CharcoalPit.MODID, "bloomery_walls"));
	
	public static final TagKey<Item> KILN_STRAW=ItemTags.create(new ResourceLocation(CharcoalPit.MODID, "kiln_straw"));
	public static final TagKey<Item> IRON_ORE=ItemTags.create(new ResourceLocation("forge", "ores/iron"));
	public static final TagKey<Item> RAW_IRON_ORE=ItemTags.create(new ResourceLocation("forge", "raw_materials/iron"));
	public static final TagKey<Item> BLOOMERY_FUEL=ItemTags.create(new ResourceLocation(CharcoalPit.MODID, "bloomery_fuels"));
	public static final TagKey<Item> BLASTING_FUEL=ItemTags.create(new ResourceLocation(CharcoalPit.MODID, "blast_furnace_fuels"));
	public static final TagKey<Item> STEEL=ItemTags.create(new ResourceLocation("forge", "ingots/steel"));
	public static final TagKey<Item> ORICHALCUM=ItemTags.create(new ResourceLocation("forge", "ingots/orichalcum"));
	public static final TagKey<Item> BRONZE=ItemTags.create(new ResourceLocation("forge", "ingots/bronze"));
	public static final TagKey<Item> BULLETS=ItemTags.create(new ResourceLocation(CharcoalPit.MODID, "bullets"));
	public static final TagKey<Item> GUN_POWDER=ItemTags.create(new ResourceLocation(CharcoalPit.MODID, "gun_powder"));
	public static final TagKey<Item> MUSKET=ItemTags.create(new ResourceLocation(CharcoalPit.MODID, "musket"));
	
	public static final TagKey<Fluid> SEED_OIL=FluidTags.create(new ResourceLocation("forge", "seed_oil"));
	public static final TagKey<Fluid> BIODIESEL=FluidTags.create(new ResourceLocation("forge", "biodiesel"));
	public static final TagKey<Fluid> DIESEL=FluidTags.create(new ResourceLocation("forge", "diesel"));
	public static final TagKey<Fluid> KEROSENE=FluidTags.create(new ResourceLocation("forge", "kerosene"));
	
	public static boolean isAvgas(Fluid fluid){
		return FluidIngredient.isFluidInTag(fluid,BIODIESEL)||FluidIngredient.isFluidInTag(fluid,KEROSENE)||FluidIngredient.isFluidInTag(fluid,DIESEL);
	}
	
	public static boolean isBlockInTag(Block block,TagKey<Block> tag){
		return ForgeRegistries.BLOCKS.tags().getTag(tag).contains(block);
	}
	
	public static boolean isBlockInTag(BlockState state,TagKey<Block> tag){
		return isBlockInTag(state.getBlock(),tag);
	}
	
	public static boolean isItemInTag(Item item,TagKey<Item> tag){
		return ForgeRegistries.ITEMS.tags().getTag(tag).contains(item);
	}
	
	public static boolean isItemInTag(ItemStack stack,TagKey<Item> tag){
		return isItemInTag(stack.getItem(),tag);
	}
	
	public static boolean CharcoalPitIsValidBlock(Level world, BlockPos pos, Direction facing, boolean isCoke) {
		BlockState state=world.getBlockState(pos.relative(facing));
		if(state.isFlammable(world, pos.relative(facing), facing.getOpposite())) {
			return false;
		}
		if(isCoke&&!CokeOvenIsValidBlock(state)) {
			return false;
		}
		return state.isFaceSturdy(world, pos.relative(facing), facing.getOpposite());
	}
	
	public static boolean CokeOvenIsValidBlock(BlockState state) {
		Block block=state.getBlock();
		return block==ModBlockRegistry.ActiveCoalPile||ForgeRegistries.BLOCKS.tags().getTag(COKE_OVEN_WALLS).contains(state.getBlock());
	}
	
	public static int calcRedstoneFromInventory(IItemHandler inv) {
	      if (inv == null) {
	         return 0;
	      } else {
	         int i = 0;
	         float f = 0.0F;

	         for(int j = 0; j < inv.getSlots(); ++j) {
	            ItemStack itemstack = inv.getStackInSlot(j);
	            if (!itemstack.isEmpty()) {
	               f += (float)itemstack.getCount() / (float)Math.min(inv.getSlotLimit(j), itemstack.getMaxStackSize());
	               ++i;
	            }
	         }

	         f = f / (float)inv.getSlots();
	         return Mth.floor(f * 14.0F) + (i > 0 ? 1 : 0);
	      }
	}
	
	public static final int TANK_WIDE=16,TANK_SHORT=29,TANK_TALL=58;
	
	public static void renderFluid(AbstractContainerScreen<?> screen, Minecraft mc, PoseStack pMatrixStack, FluidStack fluid, int capacity, int x, int y, final int tank_height, int i, int j){
		y++;
		if(fluid.isEmpty())
			return;
		if(fluid.getFluid().getAttributes().isGaseous())
			capacity=capacity*64;
		int height= Math.max(1,(fluid.getAmount()*tank_height)/capacity);
		RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
		TextureAtlasSprite sprite= mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluid.getFluid().getAttributes().getStillTexture());
		int c=fluid.getFluid().getAttributes().getColor(fluid);
		RenderSystem.setShaderColor((c>>16&255)/255.0F, (c>>8&255)/255.0F, (c&255)/255.0F, 1F/*(c>>24&255)/255f*/);
		while(height>=16){
			innerBlit(pMatrixStack.last().pose(),i+x,i+x+16,j+y-height,j+y+16-height,screen.getBlitOffset(),sprite.getU0(), sprite.getU1(), sprite.getV0(), sprite.getV1());
			height-=16;
		}
		if(height>0){
			innerBlit(pMatrixStack.last().pose(),i+x,i+x+16,j+y-height,j+y,screen.getBlitOffset(),sprite.getU0(), sprite.getU1(), sprite.getV0(), (sprite.getV1()-sprite.getV0())*(height/16F)+ sprite.getV0());
		}
		RenderSystem.setShaderColor(1F,1F,1F,1F);
	}
	
	private static void innerBlit(Matrix4f pMatrix, int pX1, int pX2, int pY1, int pY2, int pBlitOffset, float pMinU, float pMaxU, float pMinV, float pMaxV) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
		bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferbuilder.vertex(pMatrix, (float)pX1, (float)pY2, (float)pBlitOffset).uv(pMinU, pMaxV).endVertex();
		bufferbuilder.vertex(pMatrix, (float)pX2, (float)pY2, (float)pBlitOffset).uv(pMaxU, pMaxV).endVertex();
		bufferbuilder.vertex(pMatrix, (float)pX2, (float)pY1, (float)pBlitOffset).uv(pMaxU, pMinV).endVertex();
		bufferbuilder.vertex(pMatrix, (float)pX1, (float)pY1, (float)pBlitOffset).uv(pMinU, pMinV).endVertex();
		bufferbuilder.end();
		BufferUploader.end(bufferbuilder);
	}
	
	public static void drawFluidTooltip(AbstractContainerScreen<?> screen,FluidStack stack,int cap,int mousex,int mousey, int x1,int y1,int x2,int y2,PoseStack matrix,Component emptyName){
		if(isMouseOverRect(screen,mousex,mousey,x1,x2,y1,y2)) {
			ArrayList<Component> list = new ArrayList<>();
			if(!stack.isEmpty())
				list.add(stack.getDisplayName());
			else
				list.add(emptyName);
			list.add(new TextComponent(stack.getAmount() + "/" + cap + " mB"));
			Optional<TooltipComponent> tooltipComponent = Optional.empty();
			screen.renderTooltip(matrix, list, tooltipComponent, mousex, mousey, ItemStack.EMPTY);
		}
	}
	
	public static boolean isMouseOverRect(AbstractContainerScreen<?> screen,int mouseX,int mouseY,int x1,int x2,int y1,int y2){
		int i = (screen.width - screen.getXSize()) / 2;
		int j = (screen.height - screen.getYSize()) / 2;
		return mouseX>=i+x1&&mouseX<=i+x2+1&&mouseY>=j+y1&&mouseY<=j+y2+1;
	}
	
}
