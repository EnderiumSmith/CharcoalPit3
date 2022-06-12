package charcoalPit.gui;

import com.mojang.blaze3d.vertex.*;
import com.mojang.blaze3d.systems.RenderSystem;

import charcoalPit.CharcoalPit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Matrix4f;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Optional;

public class BarrelScreen extends AbstractContainerScreen<BarrelContainer>{

	private static final ResourceLocation BARREL_GUI_TEXTURES = new ResourceLocation(CharcoalPit.MODID, "textures/gui/container/barrel.png");
	private static final int[] BUBBLELENGTHS = new int[]{29, 24, 20, 16, 11, 6, 0};
	
	public BarrelScreen(BarrelContainer screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0,BARREL_GUI_TEXTURES);
	      //this.minecraft.getTextureManager().bindForSetup(BARREL_GUI_TEXTURES);
	      int i = (this.width - this.imageWidth) / 2;
	      int j = (this.height - this.imageHeight) / 2;
	      this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
	      renderFluid(matrixStack,i,j);
		
	}
	
	public void renderFluid(PoseStack matrixStack, int i, int j) {
		FluidStack fluid=FluidStack.loadFluidStackFromNBT(this.menu.slots.get(this.menu.slots.size()-1).getItem().getTag().getCompound("fluid"));
		if(fluid.isEmpty())
			return;
		int height=(int)(58*fluid.getAmount()/16000D);
		RenderSystem.setShaderTexture(0,InventoryMenu.BLOCK_ATLAS);
		//Minecraft.getInstance().getTextureManager().bindForSetup(InventoryMenu.BLOCK_ATLAS);
		TextureAtlasSprite sprite=this.minecraft.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluid.getFluid().getAttributes().getStillTexture());
		int c=fluid.getFluid().getAttributes().getColor(fluid);
		RenderSystem.setShaderColor((c>>16&255)/255.0F, (c>>8&255)/255.0F, (c&255)/255.0F, 1F/*(c>>24&255)/255f*/);
		//blit(matrixStack, i+62, j+71-height, this.getBlitOffset(), 16, height+1, sprite);
		while(height>=16) {
			innerBlit(matrixStack.last().pose(), i+62, i+62+16, j+72-height, j+72+16-height, this.getBlitOffset(), sprite.getU0(), sprite.getU1(), sprite.getV0(), sprite.getV1());
			height-=16;
		}
		if(height>0)
			innerBlit(matrixStack.last().pose(), i+62, i+62+16, j+72-height, j+72, this.getBlitOffset(), sprite.getU0(), sprite.getU1(), sprite.getV0(), 
				(sprite.getV1()-sprite.getV0())*(height/16F)+sprite.getV0());
		//innerBlit(matrixStack.getLast().getMatrix(), i+62, i+62+16, j+72-height, j+72, this.getBlitOffset(), sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), sprite.getMaxV());
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		//this.blit(matrixStack, i+62, j+71-height, (int)(sprite.getWidth()*sprite.getMinU()), (int)(sprite.getHeight()*sprite.getMinV()), 16, height);
		/*minecraft.getTextureManager().bindTexture(fluid.getFluid().getAttributes().getStillTexture());
		this.blit(matrixStack, i+62, j+71-height, 0, 0, 16, height);*/
	}
	
	private static void innerBlit(Matrix4f matrix, int x1, int x2, int y1, int y2, int blitOffset, float minU, float maxU, float minV, float maxV) {
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
	      bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
	      bufferbuilder.vertex(matrix, (float)x1, (float)y2, (float)blitOffset).uv(minU, maxV).endVertex();
	      bufferbuilder.vertex(matrix, (float)x2, (float)y2, (float)blitOffset).uv(maxU, maxV).endVertex();
	      bufferbuilder.vertex(matrix, (float)x2, (float)y1, (float)blitOffset).uv(maxU, minV).endVertex();
	      bufferbuilder.vertex(matrix, (float)x1, (float)y1, (float)blitOffset).uv(minU, minV).endVertex();
	      bufferbuilder.end();
	      BufferUploader.end(bufferbuilder);
	   }
	
	@Override
	protected void renderLabels(PoseStack matrixStack, int x, int y) {
		super.renderLabels(matrixStack, x, y);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0,BARREL_GUI_TEXTURES);
	      //this.minecraft.getTextureManager().bindForSetup(BARREL_GUI_TEXTURES);
	      //int i = (this.width - this.xSize) / 2;
	      //int j = (this.height - this.ySize) / 2;
	      this.blit(matrixStack, 62, 14, 176, 47, 16, 71-14);
	    int time=menu.array.get(0);
	    int total=menu.array.get(1);
	    if(total>0&&time>=0) {
	    	int height=(int)(time*14F/total);
	    	this.blit(matrixStack, 97, 36, 176, 2, 18, 14-height);
	    	height=BUBBLELENGTHS[(time)/2%7];
	    	this.blit(matrixStack, 82, 14+29-height, 176, 18+29-height, 12, height);
	    }
	}
	
	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
	      super.render(matrixStack, mouseX, mouseY, partialTicks);
	      this.renderTooltip(matrixStack, mouseX, mouseY);
		FluidStack fluid=FluidStack.loadFluidStackFromNBT(this.menu.slots.get(this.menu.slots.size()-1).getItem().getTag().getCompound("fluid"));
		drawFluidTooltip(fluid,16000,mouseX,mouseY,61,13,78,72,matrixStack);
	   
	}
	
	public void drawFluidTooltip(FluidStack stack,int cap,int mousex,int mousey, int x1,int y1,int x2,int y2,PoseStack matrix){
		if(!stack.isEmpty()&&isMouseOverRect(mousex,mousey,x1,x2,y1,y2)) {
			ArrayList<Component> list = new ArrayList<>();
			list.add(stack.getDisplayName());
			list.add(new TextComponent(stack.getAmount() + "/" + (stack.getFluid().getAttributes().isGaseous() ? 64 * cap : cap) + " mB"));
			Optional<TooltipComponent> tooltipComponent = Optional.empty();
			this.renderTooltip(matrix, list, tooltipComponent, mousex, mousey, ItemStack.EMPTY);
		}
	}
	
	public boolean isMouseOverRect(int mouseX,int mouseY,int x1,int x2,int y1,int y2){
		int i = (this.width - this.getXSize()) / 2;
		int j = (this.height - this.getYSize()) / 2;
		return mouseX>=i+x1&&mouseX<=i+x2+1&&mouseY>=j+y1&&mouseY<=j+y2+1;
	}

}
