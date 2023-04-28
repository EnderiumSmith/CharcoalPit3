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
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;

public class CharcoalPitRecipeCategory implements IRecipeCategory<SmeltingRecipe> {
	
	public static final ResourceLocation UID=new ResourceLocation(CharcoalPit.MODID,"charcoal");
	private static final ResourceLocation BARREL_GUI_TEXTURES = new ResourceLocation(CharcoalPit.MODID, "textures/gui/container/charcoal_pit.png");
	public final IGuiHelper guiHelper;
	public final TranslatableComponent title;
	public IDrawableStatic background;
	public IDrawable icon;
	
	public CharcoalPitRecipeCategory(IGuiHelper helper){
		guiHelper=helper;
		title=new TranslatableComponent("charcoal_pit.jei.charcoal");
		icon=guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK,new ItemStack(Items.CHARCOAL));
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
	public Class<? extends SmeltingRecipe> getRecipeClass() {
		return SmeltingRecipe.class;
	}
	
	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, SmeltingRecipe recipe, IFocusGroup focuses) {
		if(recipe.getResultItem().getItem()==Items.CHARCOAL){
			builder.addSlot(RecipeIngredientRole.INPUT,62,35).addIngredient(VanillaTypes.ITEM_STACK,new ItemStack(ModItemRegistry.LogPile));
			builder.addSlot(RecipeIngredientRole.OUTPUT,116,35).addIngredient(VanillaTypes.ITEM_STACK,new ItemStack(Items.CHARCOAL,8));
			builder.addSlot(RecipeIngredientRole.RENDER_ONLY,62,17).addIngredient(VanillaTypes.ITEM_STACK,new ItemStack(Items.DIRT));
			builder.addSlot(RecipeIngredientRole.RENDER_ONLY,62+18,17+18).addIngredient(VanillaTypes.ITEM_STACK,new ItemStack(Items.DIRT));
			builder.addSlot(RecipeIngredientRole.RENDER_ONLY,62-18,17+18).addIngredient(VanillaTypes.ITEM_STACK,new ItemStack(Items.DIRT));
			builder.addSlot(RecipeIngredientRole.RENDER_ONLY,62,17+36).addIngredient(VanillaTypes.ITEM_STACK,new ItemStack(Items.DIRT));
		}else{
			builder.addSlot(RecipeIngredientRole.INPUT,62,35).addIngredient(VanillaTypes.ITEM_STACK,new ItemStack(Items.COAL_BLOCK));
			builder.addSlot(RecipeIngredientRole.OUTPUT,116,35).addIngredient(VanillaTypes.ITEM_STACK,new ItemStack(ModItemRegistry.Coke,8));
			ArrayList<ItemStack> stacks=new ArrayList<>();
			ForgeRegistries.BLOCKS.tags().getTag(MethodHelper.COKE_OVEN_WALLS).stream().forEach(b->{
				ItemStack block=new ItemStack(b);
				if(!block.isEmpty()){
					stacks.add(block);
				}
			});
			builder.addSlot(RecipeIngredientRole.RENDER_ONLY,62,17).addIngredients(VanillaTypes.ITEM_STACK, stacks);
			builder.addSlot(RecipeIngredientRole.RENDER_ONLY,62+18,17+18).addIngredients(VanillaTypes.ITEM_STACK,stacks);
			builder.addSlot(RecipeIngredientRole.RENDER_ONLY,62-18,17+18).addIngredients(VanillaTypes.ITEM_STACK,stacks);
			builder.addSlot(RecipeIngredientRole.RENDER_ONLY,62,17+36).addIngredients(VanillaTypes.ITEM_STACK,stacks);
		}
	}
	
}
