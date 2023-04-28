package charcoalPit.recipe;

import charcoalPit.core.ModItemRegistry;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;

public class AlloySmeltRecipe extends SmeltingRecipe {
	
	
	public static final Serializer SERIALIZER=new Serializer();
	
	public AlloySmeltRecipe(ResourceLocation pId, String pGroup, Ingredient pIngredient, ItemStack pResult, float pExperience, int pCookingTime) {
		super(pId, pGroup, pIngredient, pResult, pExperience, pCookingTime);
	}
	
	@Override
	public ItemStack getResultItem() {
		return ItemStack.EMPTY;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
	@Override
	public boolean matches(Container pInv, Level pLevel) {
		if(super.matches(pInv,pLevel)){
			ItemStack pot=pInv.getItem(0);
			if(!pot.hasTag()||!pot.getTag().contains("inventory")||pot.getTag().getBoolean("empty")) {
				return false;
			}else{
				if(!ItemStack.of(pot.getTag().getCompound("result")).isEmpty()){
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public ItemStack assemble(Container pInv) {
		ItemStack pot=pInv.getItem(0);
		if(!pot.hasTag()||!pot.getTag().contains("inventory")||pot.getTag().getBoolean("empty")) {
			return super.assemble(pInv);
		}else{
			//ItemStackHandler result=new ItemStackHandler(1);
			//result.setStackInSlot(0,ItemStack.of(pot.getTag().getCompound("result")));
			ItemStack out=new ItemStack(ModItemRegistry.finished_alloy_mold);
			out.addTagElement("result",pot.getTag().getCompound("result"));
			//out.addTagElement("inventory",result.serializeNBT());
			return out;
		}
	}
	
	@Override
	public int getCookingTime() {
		return super.getCookingTime();
	}
	
	public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<AlloySmeltRecipe>{
		
		@Override
		public AlloySmeltRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
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
			return new AlloySmeltRecipe(pRecipeId,s,ingredient,itemstack,f,i);
		}
		
		@Nullable
		@Override
		public AlloySmeltRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
			String s = pBuffer.readUtf();
			Ingredient ingredient = Ingredient.fromNetwork(pBuffer);
			ItemStack itemstack = pBuffer.readItem();
			float f = pBuffer.readFloat();
			int i = pBuffer.readVarInt();
			return new AlloySmeltRecipe(pRecipeId,s,ingredient,itemstack,f,i);
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf pBuffer, AlloySmeltRecipe pRecipe) {
			pBuffer.writeUtf(pRecipe.group);
			pRecipe.ingredient.toNetwork(pBuffer);
			pBuffer.writeItem(pRecipe.result);
			pBuffer.writeFloat(pRecipe.experience);
			pBuffer.writeVarInt(pRecipe.cookingTime);
		}
	}
}
