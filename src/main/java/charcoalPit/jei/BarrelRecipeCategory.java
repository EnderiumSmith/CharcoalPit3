package charcoalPit.jei;

import charcoalPit.CharcoalPit;
import charcoalPit.core.MethodHelper;
import charcoalPit.core.ModItemRegistry;
import charcoalPit.recipe.BarrelRecipe;
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

public class BarrelRecipeCategory implements IRecipeCategory<BarrelRecipe> {
	
	public static final ResourceLocation UID=new ResourceLocation(CharcoalPit.MODID,"barrel");
	private static final ResourceLocation BARREL_GUI_TEXTURES = new ResourceLocation(CharcoalPit.MODID, "textures/gui/container/barrel_recipe.png");
	public final IGuiHelper guiHelper;
	public final TranslatableComponent title;
	public IDrawableStatic background;
	public IDrawable icon;
	public IDrawable tankOverlay;
	
	public BarrelRecipeCategory(IGuiHelper helper){
		guiHelper=helper;
		title=new TranslatableComponent("charcoal_pit.jei.barrel");
		icon=guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK,new ItemStack(ModItemRegistry.Barrel));
		background=guiHelper.createDrawable(BARREL_GUI_TEXTURES,0,0,175,85);
		tankOverlay=guiHelper.createDrawable(BARREL_GUI_TEXTURES,176,0,16,MethodHelper.TANK_TALL);
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
	public Class<? extends BarrelRecipe> getRecipeClass() {
		return BarrelRecipe.class;
	}
	
	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, BarrelRecipe recipe, IFocusGroup focuses) {
		ItemStack[] stacks=recipe.item_in.getItems();
		for (ItemStack stack : stacks) {
			stack.setCount(recipe.in_amount);
		}
		builder.addSlot(RecipeIngredientRole.INPUT,80,17).addIngredients(VanillaTypes.ITEM_STACK, Arrays.stream(stacks).toList());
		builder.addSlot(RecipeIngredientRole.INPUT, 44,14).addIngredients(VanillaTypes.FLUID,recipe.fluid_in.getAllStacks()).setFluidRenderer(Math.min(16000,recipe.fluid_in.amount*2),false,16, MethodHelper.TANK_TALL).setOverlay(tankOverlay,0,0);
		if(recipe.item_out!=null){
			ItemStack[] stacks2=recipe.item_out.getItems();
			for (ItemStack stack2 : stacks2) {
				stack2.setCount(recipe.out_amount);
			}
			builder.addSlot(RecipeIngredientRole.OUTPUT,80,53).addIngredients(VanillaTypes.ITEM_STACK, Arrays.stream(stacks2).toList());
		}
		if(recipe.fluid_out!=null){
			builder.addSlot(RecipeIngredientRole.OUTPUT,116,14).addIngredients(VanillaTypes.FLUID,recipe.fluid_out.getAllStacks()).setFluidRenderer(Math.min(16000,recipe.fluid_out.amount*2),false,16,MethodHelper.TANK_TALL).setOverlay(tankOverlay,0,0);
		}
	}
}
