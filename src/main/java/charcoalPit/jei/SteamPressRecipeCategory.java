package charcoalPit.jei;

import charcoalPit.CharcoalPit;
import charcoalPit.core.MethodHelper;
import charcoalPit.core.ModItemRegistry;
import charcoalPit.recipe.DistilleryRecipe;
import charcoalPit.recipe.SquisherRecipe;
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
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;

public class SteamPressRecipeCategory implements IRecipeCategory<SquisherRecipe> {
	
	public static final ResourceLocation UID=new ResourceLocation(CharcoalPit.MODID,"steam_press");
	private static final ResourceLocation BARREL_GUI_TEXTURES = new ResourceLocation(CharcoalPit.MODID, "textures/gui/container/steam_press_recipe.png");
	public final IGuiHelper guiHelper;
	public final TranslatableComponent title;
	public IDrawableStatic background;
	public IDrawable icon;
	public IDrawable tankOverlay,tankOverlay2;
	
	public SteamPressRecipeCategory(IGuiHelper helper){
		guiHelper=helper;
		title=new TranslatableComponent("charcoal_pit.jei.steam_press");
		icon=guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK,new ItemStack(ModItemRegistry.SteamPress));
		background=guiHelper.createDrawable(BARREL_GUI_TEXTURES,0,0,175,85);
		tankOverlay=guiHelper.createDrawable(BARREL_GUI_TEXTURES,176,60,16, 16);
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
	public Class<? extends SquisherRecipe> getRecipeClass() {
		return SquisherRecipe.class;
	}
	
	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, SquisherRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT,65,35).addIngredients(VanillaTypes.ITEM_STACK, Arrays.stream(recipe.input.getItems()).toList());
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 109,17).addIngredient(VanillaTypes.FLUID,new FluidStack(Fluids.WATER,2000)).setFluidRenderer(2000,true,16, 16).setOverlay(tankOverlay,0,0);
		builder.addSlot(RecipeIngredientRole.OUTPUT,57,53).addIngredients(VanillaTypes.FLUID,recipe.output.getAllStacks()).setFluidRenderer(Math.min(8000,recipe.output.amount*2),false,32,MethodHelper.TANK_WIDE).setOverlay(tankOverlay2,0,0);
	}
	
}
