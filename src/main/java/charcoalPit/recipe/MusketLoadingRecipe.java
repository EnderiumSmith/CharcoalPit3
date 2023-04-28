package charcoalPit.recipe;

import charcoalPit.core.MethodHelper;
import charcoalPit.core.ModItemRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.Level;

public class MusketLoadingRecipe extends CustomRecipe {
	
	public MusketLoadingRecipe(ResourceLocation pId) {
		super(pId);
	}
	
	public static final SimpleRecipeSerializer<MusketLoadingRecipe> SERIALIZER=new SimpleRecipeSerializer<>(MusketLoadingRecipe::new);
	
	@Override
	public boolean matches(CraftingContainer pContainer, Level pLevel) {
		int i=0;
		int j=0;
		int k=0;
		for(int l=0;l<pContainer.getContainerSize();l++){
			ItemStack stack=pContainer.getItem(l);
			if(!stack.isEmpty()){
				if(stack.getItem()== ModItemRegistry.musket){
					i++;
					if(stack.hasTag()&&stack.getTag().contains("Loaded")){
						return false;
					}
				}else if(MethodHelper.isItemInTag(stack,MethodHelper.GUN_POWDER)){
					j++;
				}else if(MethodHelper.isItemInTag(stack,MethodHelper.BULLETS)){
					k++;
				}else{
					return false;
				}
			}
		}
		return i==1&&j==1&&k==1;
	}
	
	@Override
	public boolean isSpecial() {
		return false;
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.of(Ingredient.EMPTY,Ingredient.of(ModItemRegistry.musket),Ingredient.of(MethodHelper.GUN_POWDER),Ingredient.of(MethodHelper.BULLETS));
	}
	
	@Override
	public ItemStack getResultItem() {
		ItemStack stack=new ItemStack(ModItemRegistry.musket);
		stack.getOrCreateTag().putBoolean("Loaded",true);
		return stack;
	}
	
	@Override
	public ItemStack assemble(CraftingContainer pContainer) {
		ItemStack stack=ItemStack.EMPTY;
		for(int i=0;i<pContainer.getContainerSize();i++){
			if(pContainer.getItem(i).getItem()==ModItemRegistry.musket){
				stack=pContainer.getItem(i).copy();
				stack.getOrCreateTag().putBoolean("Loaded",true);
				break;
			}
		}
		return stack;
	}
	
	@Override
	public boolean canCraftInDimensions(int pWidth, int pHeight) {
		return pHeight*pWidth>=3;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
}
