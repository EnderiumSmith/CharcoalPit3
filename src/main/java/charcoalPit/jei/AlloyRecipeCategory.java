package charcoalPit.jei;

import charcoalPit.CharcoalPit;
import charcoalPit.core.MethodHelper;
import charcoalPit.core.ModItemRegistry;
import charcoalPit.recipe.OreKilnRecipe;
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

public class AlloyRecipeCategory implements IRecipeCategory<OreKilnRecipe> {
	
	public static final ResourceLocation UID=new ResourceLocation(CharcoalPit.MODID,"alloy_mold");
	private static final ResourceLocation BARREL_GUI_TEXTURES = new ResourceLocation(CharcoalPit.MODID, "textures/gui/container/alloy_recipe.png");
	public final IGuiHelper guiHelper;
	public final TranslatableComponent title;
	public IDrawableStatic background;
	public IDrawable icon;
	
	public AlloyRecipeCategory(IGuiHelper helper){
		guiHelper=helper;
		title=new TranslatableComponent("charcoal_pit.jei.alloy_mold");
		icon=guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK,new ItemStack(ModItemRegistry.alloy_mold));
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
	public Class<? extends OreKilnRecipe> getRecipeClass() {
		return OreKilnRecipe.class;
	}
	
	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, OreKilnRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT,71,26).addIngredients(VanillaTypes.ITEM_STACK, Arrays.stream(recipe.input[0].getItems()).toList());
		if(recipe.input.length>=2){
			builder.addSlot(RecipeIngredientRole.INPUT,71+18,26).addIngredients(VanillaTypes.ITEM_STACK, Arrays.stream(recipe.input[1].getItems()).toList());
			if(recipe.input.length>=3){
				builder.addSlot(RecipeIngredientRole.INPUT,71,26+18).addIngredients(VanillaTypes.ITEM_STACK, Arrays.stream(recipe.input[2].getItems()).toList());
				if(recipe.input.length>=4){
					builder.addSlot(RecipeIngredientRole.INPUT,71+18,26+18).addIngredients(VanillaTypes.ITEM_STACK, Arrays.stream(recipe.input[3].getItems()).toList());
				}
			}
		}
		builder.addSlot(RecipeIngredientRole.OUTPUT,107,35).addIngredient(VanillaTypes.ITEM_STACK,new ItemStack(recipe.output.getItems()[0].getItem(),recipe.amount));
	}
}
