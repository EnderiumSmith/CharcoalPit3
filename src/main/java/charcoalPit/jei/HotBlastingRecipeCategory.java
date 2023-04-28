package charcoalPit.jei;

import charcoalPit.CharcoalPit;
import charcoalPit.core.MethodHelper;
import charcoalPit.core.ModItemRegistry;
import charcoalPit.recipe.BloomingRecipe;
import charcoalPit.recipe.TuyereBlastingRecipe;
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
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Arrays;

public class HotBlastingRecipeCategory implements IRecipeCategory<TuyereBlastingRecipe> {
	
	public static final ResourceLocation UID=new ResourceLocation(CharcoalPit.MODID,"hot_blasting");
	private static final ResourceLocation BARREL_GUI_TEXTURES = new ResourceLocation(CharcoalPit.MODID, "textures/gui/container/blast_furnace_recipe.png");
	public final IGuiHelper guiHelper;
	public final TranslatableComponent title;
	public IDrawableStatic background;
	public IDrawable icon;
	
	public HotBlastingRecipeCategory(IGuiHelper helper){
		guiHelper=helper;
		title=new TranslatableComponent("charcoal_pit.jei.hot_blasting");
		icon=guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK,new ItemStack(ModItemRegistry.Tuyere));
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
	public Class<? extends TuyereBlastingRecipe> getRecipeClass() {
		return TuyereBlastingRecipe.class;
	}
	
	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, TuyereBlastingRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT,47,17).addIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModItemRegistry.Flux));
		builder.addSlot(RecipeIngredientRole.INPUT,65,17).addIngredients(VanillaTypes.ITEM_STACK, Arrays.stream(recipe.getIngredient().getItems()).toList());
		builder.addSlot(RecipeIngredientRole.INPUT,56,53).addIngredients(VanillaTypes.ITEM_STACK, Arrays.stream(Ingredient.of(MethodHelper.BLASTING_FUEL).getItems()).toList());
		builder.addSlot(RecipeIngredientRole.OUTPUT,116,35).addIngredient(VanillaTypes.ITEM_STACK,recipe.getResultItem().copy());
	}
}
