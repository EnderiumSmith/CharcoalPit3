package charcoalPit.jei;

import charcoalPit.CharcoalPit;
import charcoalPit.core.MethodHelper;
import charcoalPit.core.ModItemRegistry;
import charcoalPit.recipe.BarrelRecipe;
import charcoalPit.recipe.BloomingRecipe;
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

public class BloomeryRecipeCategory implements IRecipeCategory<BloomingRecipe> {
	
	public static final ResourceLocation UID=new ResourceLocation(CharcoalPit.MODID,"blooming");
	private static final ResourceLocation BARREL_GUI_TEXTURES = new ResourceLocation(CharcoalPit.MODID, "textures/gui/container/bloomeryy_recipe.png");
	public final IGuiHelper guiHelper;
	public final TranslatableComponent title;
	public IDrawableStatic background;
	public IDrawable icon;
	
	public BloomeryRecipeCategory(IGuiHelper helper){
		guiHelper=helper;
		title=new TranslatableComponent("charcoal_pit.jei.blooming");
		icon=guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK,new ItemStack(ModItemRegistry.Bloomeryy));
		background=guiHelper.createDrawable(BARREL_GUI_TEXTURES,0,0,175,85);
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
	public Class<? extends BloomingRecipe> getRecipeClass() {
		return BloomingRecipe.class;
	}
	
	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, BloomingRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT,56,17).addIngredients(VanillaTypes.ITEM_STACK, Arrays.stream(recipe.getIngredient().getItems()).toList());
		ItemStack[] stacks=new ItemStack[]{new ItemStack(ModItemRegistry.Bloom),recipe.getResultItem().copy()};
		builder.addSlot(RecipeIngredientRole.OUTPUT,116,35).addIngredients(VanillaTypes.ITEM_STACK, Arrays.stream(stacks).toList());
	}
}
