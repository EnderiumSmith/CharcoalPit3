package charcoalPit.recipe;

import charcoalPit.CharcoalPit;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BloomingRecipe extends AbstractCookingRecipe {
	public static final ResourceLocation BLOOMING=new ResourceLocation(CharcoalPit.MODID, "blooming");
	public static final RecipeType<BloomingRecipe> BLOOMING_RECIPE=RecipeType.register(BLOOMING.toString());
	
	public BloomingRecipe(ResourceLocation pId, String pGroup, Ingredient pIngredient, ItemStack pResult, float pExperience, int pCookingTime) {
		super(BLOOMING_RECIPE, pId, pGroup, pIngredient, pResult, pExperience, pCookingTime);
	}
	
	public static final Serializer SERIALIZER=new Serializer();
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
	public static BloomingRecipe getRecipe(ItemStack stack, Level level){
		if(stack.isEmpty())
			return null;
		List<BloomingRecipe> recipes=level.getRecipeManager().getAllRecipesFor(BLOOMING_RECIPE);
		for(BloomingRecipe recipe:recipes){
			if(recipe.ingredient.test(stack))
				return recipe;
		}
		return null;
	}
	
	public Ingredient getIngredient(){
		return this.ingredient;
	}
	
	public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<BloomingRecipe>{
		
		@Override
		public BloomingRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
			String s = GsonHelper.getAsString(pJson, "group", "");
			JsonElement jsonelement = (JsonElement)(GsonHelper.isArrayNode(pJson, "ingredient") ? GsonHelper.getAsJsonArray(pJson, "ingredient") : GsonHelper.getAsJsonObject(pJson, "ingredient"));
			Ingredient ingredient = Ingredient.fromJson(jsonelement);
			//Forge: Check if primitive string to keep vanilla or a object which can contain a count field.
			if (!pJson.has("result")) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
			ItemStack itemstack;
			if (pJson.get("result").isJsonObject()) itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pJson, "result"));
			else {
				String s1 = GsonHelper.getAsString(pJson, "result");
				ResourceLocation resourcelocation = new ResourceLocation(s1);
				itemstack = new ItemStack(Registry.ITEM.getOptional(resourcelocation).orElseThrow(() -> {
					return new IllegalStateException("Item: " + s1 + " does not exist");
				}));
			}
			float f = GsonHelper.getAsFloat(pJson, "experience", 0.0F);
			int i = GsonHelper.getAsInt(pJson, "cookingtime", 200);
			return new BloomingRecipe(pRecipeId,s,ingredient,itemstack,f,i);
		}
		
		@Nullable
		@Override
		public BloomingRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
			String s = pBuffer.readUtf();
			Ingredient ingredient = Ingredient.fromNetwork(pBuffer);
			ItemStack itemstack = pBuffer.readItem();
			float f = pBuffer.readFloat();
			int i = pBuffer.readVarInt();
			return new BloomingRecipe(pRecipeId,s,ingredient,itemstack,f,i);
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf pBuffer, BloomingRecipe pRecipe) {
			pBuffer.writeUtf(pRecipe.group);
			pRecipe.ingredient.toNetwork(pBuffer);
			pBuffer.writeItem(pRecipe.result);
			pBuffer.writeFloat(pRecipe.experience);
			pBuffer.writeVarInt(pRecipe.cookingTime);
		}
	}
}
