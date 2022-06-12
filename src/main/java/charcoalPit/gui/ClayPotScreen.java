package charcoalPit.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

import charcoalPit.CharcoalPit;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

public class ClayPotScreen extends AbstractContainerScreen<ClayPotContainer2>{
	
	private static final ResourceLocation DISPENSER_GUI_TEXTURES = new ResourceLocation(CharcoalPit.MODID,"textures/gui/container/claypot.png");
	
	public ClayPotScreen(ClayPotContainer2 container, Inventory inv, Component title) {
		super(container, inv, title);
	}
	
	@Override
	protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0,DISPENSER_GUI_TEXTURES);
	      //this.minecraft.getTextureManager().bindForSetup(DISPENSER_GUI_TEXTURES);
	      int i = (this.width - this.imageWidth) / 2;
	      int j = (this.height - this.imageHeight) / 2;
	      this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
		
	}
	
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
	      this.renderBackground(matrixStack);
	      super.render(matrixStack, mouseX, mouseY, partialTicks);
	      this.renderTooltip(matrixStack, mouseX, mouseY);
	   }
	
}
