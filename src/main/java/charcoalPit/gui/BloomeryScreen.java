package charcoalPit.gui;

import charcoalPit.CharcoalPit;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BloomeryScreen extends AbstractContainerScreen<BloomeryContainer> {
	
	private static final ResourceLocation BLOOMERY_GUI_TEXTURES = new ResourceLocation(CharcoalPit.MODID, "textures/gui/container/bloomeryy.png");
	private static final int[] BUBBLELENGTHS = new int[]{29, 24, 20, 16, 11, 6, 0};
	
	public BloomeryScreen(BloomeryContainer screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);
	}
	
	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
		RenderSystem.setShaderColor(1F,1F,1F,1F);
		RenderSystem.setShaderTexture(0,BLOOMERY_GUI_TEXTURES);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		this.blit(pPoseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
	}
	
	@Override
	protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
		super.renderLabels(pPoseStack, pMouseX, pMouseY);
		RenderSystem.setShaderColor(1F,1F,1F,1F);
		RenderSystem.setShaderTexture(0,BLOOMERY_GUI_TEXTURES);
		int burnTotal=this.menu.array.get(1);
		if(burnTotal==0)
			burnTotal=200;
		int burnTime=this.menu.array.get(0);
		if(burnTime>0) {
			int k = burnTime * 13 / burnTotal;
			this.blit(pPoseStack, 56, 36 + 12 - k, 176, 12 - k, 14, k + 1);
		}
		int processTotal=this.menu.array.get(3);
		if(processTotal!=0){
			int progress=this.menu.array.get(2);
			int l=progress*24/processTotal;
			this.blit(pPoseStack, 79, 34, 176, 14, l + 1, 16);
		}
		int blastAir=this.menu.array.get(4);
		int height=BUBBLELENGTHS[Math.max(0,6-blastAir/110)];
		this.blit(pPoseStack, 40, 37+29-height, 176, 31+29-height, 12, height);
	}
	
	@Override
	public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
		this.renderBg(pPoseStack,pPartialTick,pMouseX,pMouseY);
		super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
		this.renderTooltip(pPoseStack,pMouseX,pMouseY);
	}
}
