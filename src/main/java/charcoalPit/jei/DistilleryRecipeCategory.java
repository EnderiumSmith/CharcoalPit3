package charcoalPit.jei;

import charcoalPit.CharcoalPit;
import charcoalPit.core.MethodHelper;
import charcoalPit.core.ModItemRegistry;
import charcoalPit.recipe.BarrelRecipe;
import charcoalPit.recipe.DistilleryRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;

public class DistilleryRecipeCategory implements IRecipeCategory<DistilleryRecipe> {
	
	public static final ResourceLocation UID=new ResourceLocation(CharcoalPit.MODID,"distillery");
	private static final ResourceLocation BARREL_GUI_TEXTURES = new ResourceLocation(CharcoalPit.MODID, "textures/gui/container/distillery_recipe.png");
	public final IGuiHelper guiHelper;
	public final TranslatableComponent title;
	public IDrawableStatic background;
	public IDrawable icon;
	public IDrawable tankOverlay,tankOverlay2;
	
	public DistilleryRecipeCategory(IGuiHelper helper){
		guiHelper=helper;
		title=new TranslatableComponent("charcoal_pit.jei.distillery");
		icon=guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK,new ItemStack(ModItemRegistry.Distillery));
		background=guiHelper.createDrawable(BARREL_GUI_TEXTURES,0,0,175,85);
		tankOverlay=guiHelper.createDrawable(BARREL_GUI_TEXTURES,176,60,16, MethodHelper.TANK_SHORT);
		tankOverlay2=guiHelper.createDrawable(BARREL_GUI_TEXTURES,176,60,32, 16);
	}
	
	@Override
	public Component getTitle() {
		return title;
	}
	
	@Override
	public IDrawable getBackground() {
		return background;
	}
	
	@Override
	public IDrawable getIcon() {
		return icon;
	}
	
	@Override
	public ResourceLocation getUid() {
		return UID;
	}
	
	@Override
	public Class<? extends DistilleryRecipe> getRecipeClass() {
		return DistilleryRecipe.class;
	}
	
	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, DistilleryRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 63,17).addIngredients(VanillaTypes.FLUID,recipe.input.getAllStacks()).setFluidRenderer(Math.min(16000,recipe.input.amount*2),false,32, MethodHelper.TANK_WIDE).setOverlay(tankOverlay2,0,0);
		builder.addSlot(RecipeIngredientRole.OUTPUT,98,40).addIngredients(VanillaTypes.FLUID,recipe.output.getAllStacks()).setFluidRenderer(Math.min(4000,recipe.output.amount*2),false,16,MethodHelper.TANK_SHORT).setOverlay(tankOverlay,0,0);
	}
}
