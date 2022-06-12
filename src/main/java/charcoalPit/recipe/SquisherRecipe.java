package charcoalPit.recipe;

import charcoalPit.CharcoalPit;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SquisherRecipe implements Recipe<Container> {
	
	public static final ResourceLocation SQUISH=new ResourceLocation(CharcoalPit.MODID, "squish");
	public static final RecipeType<SquisherRecipe> SQUISH_RECIPE=RecipeType.register(SQUISH.toString());
	
	public static final Serializer SERIALIZER=new Serializer();
	
	public final ResourceLocation ID;
	public Ingredient input;
	public FluidIngredient output;
	
	public SquisherRecipe(ResourceLocation id,Ingredient in,FluidIngredient out){
		ID=id;
		input=in;
		output=out;
	}
	
	public static SquisherRecipe getRecipe(ItemStack in,Level level){
		if(in.isEmpty())
			return null;
		List<SquisherRecipe> recipes=level.getRecipeManager().getAllRecipesFor(SQUISH_RECIPE);
		for(SquisherRecipe recipe:recipes){
			if(recipe.input.test(in))
				return recipe;
		}
		return null;
	}
	
	@Override
	public boolean matches(Container pContainer, Level pLevel) {
		return false;
	}
	
	@Override
	public ItemStack assemble(Container pContainer) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean canCraftInDimensions(int pWidth, int pHeight) {
		return false;
	}
	
	@Override
	public ItemStack getResultItem() {
		return ItemStack.EMPTY;
	}
	
	@Override
	public ResourceLocation getId() {
		return ID;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return SQUISH_RECIPE;
	}
	
	public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<SquisherRecipe>{
		
		@Override
		public SquisherRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
			Ingredient in=Ingredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe,"input"));
			FluidIngredient out=FluidIngredient.readJson(GsonHelper.getAsJsonObject(pSerializedRecipe,"output"));
			return new SquisherRecipe(pRecipeId,in,out);
		}
		
		@Nullable
		@Override
		public SquisherRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
			return new SquisherRecipe(pRecipeId,Ingredient.fromNetwork(pBuffer),FluidIngredient.readBuffer(pBuffer));
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf pBuffer, SquisherRecipe pRecipe) {
			pRecipe.input.toNetwork(pBuffer);
			pRecipe.output.writeBuffer(pBuffer);
		}
	}
	
}
