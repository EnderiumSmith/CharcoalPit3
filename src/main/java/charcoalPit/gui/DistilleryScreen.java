package charcoalPit.gui;

import charcoalPit.CharcoalPit;
import charcoalPit.core.MethodHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.fluids.FluidStack;

public class DistilleryScreen extends AbstractContainerScreen<DistilleryContainer> {
	
	private static final ResourceLocation DISTILLERY_GUI_TEXTURES = new ResourceLocation(CharcoalPit.MODID, "textures/gui/container/distillery.png");
	private static final int[] BUBBLELENGTHS = new int[]{29, 24, 20, 16, 11, 6, 0};
	
	public DistilleryScreen(DistilleryContainer container, Inventory inv, Component title){
		super(container,inv,title);
	}
	
	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0,DISTILLERY_GUI_TEXTURES);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		this.blit(pPoseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
		MethodHelper.renderFluid(this,this.minecraft,pPoseStack,FluidStack.loadFluidStackFromNBT(this.menu.fluid_tag.getStackInSlot(0).getOrCreateTag().getCompound("fluid1")),
				16000,63,32,MethodHelper.TANK_WIDE,i,j);
		MethodHelper.renderFluid(this,this.minecraft,pPoseStack,FluidStack.loadFluidStackFromNBT(this.menu.fluid_tag.getStackInSlot(0).getOrCreateTag().getCompound("fluid1")),
				16000,63+16,32,MethodHelper.TANK_WIDE,i,j);
		MethodHelper.renderFluid(this,this.minecraft,pPoseStack,FluidStack.loadFluidStackFromNBT(this.menu.fluid_tag.getStackInSlot(0).getOrCreateTag().getCompound("fluid2")),
				4000,98,68,MethodHelper.TANK_SHORT,i,j);
	}
	
	@Override
	protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
		super.renderLabels(pPoseStack, pMouseX, pMouseY);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0,DISTILLERY_GUI_TEXTURES);
		this.blit(pPoseStack,63,17,176,60,32,16);
		this.blit(pPoseStack,98,40,176,60,16,MethodHelper.TANK_SHORT);
		int time=menu.array.get(2);
		int total=menu.array.get(3);
		if(total>0&&time>=0) {
			int height=(int)((total-time)*29F/total);
			this.blit(pPoseStack, 88, 40, 188, 31, 9, 29-height);
			height=BUBBLELENGTHS[6-((time)/2%7)];
			this.blit(pPoseStack, 56, 37+29-height, 176, 31+29-height, 12, height);
		}
		int burnTotal=this.menu.array.get(1);
		if(burnTotal==0)
			burnTotal=200;
		int burnTime=this.menu.array.get(0);
		if(burnTime>0) {
			int k = burnTime * 13 / burnTotal;
			this.blit(pPoseStack, 71, 36 + 12 - k, 176, 12 - k, 14, k + 1);
		}
	}
	
	@Override
	public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
		this.renderBackground(pPoseStack);
		super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
		this.renderTooltip(pPoseStack, pMouseX, pMouseY);
		MethodHelper.drawFluidTooltip(this,FluidStack.loadFluidStackFromNBT(this.menu.fluid_tag.getStackInSlot(0).getOrCreateTag().getCompound("fluid1")),
				16000,pMouseX,pMouseY,62,16,95,33,pPoseStack,new TranslatableComponent("tooltip.charcoal_pit.fluid_empty"));
		MethodHelper.drawFluidTooltip(this,FluidStack.loadFluidStackFromNBT(this.menu.fluid_tag.getStackInSlot(0).getOrCreateTag().getCompound("fluid2")),
				4000,pMouseX,pMouseY,97,39,114,69,pPoseStack,new TranslatableComponent("tooltip.charcoal_pit.fluid_empty"));
	}
}
