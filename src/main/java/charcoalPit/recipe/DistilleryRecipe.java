package charcoalPit.recipe;

import charcoalPit.CharcoalPit;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DistilleryRecipe implements Recipe<Container> {
	
	public static final ResourceLocation DISTILLERY=new ResourceLocation(CharcoalPit.MODID, "distill");
	public static final RecipeType<DistilleryRecipe> DISTILLERY_RECIPE=RecipeType.register(DISTILLERY.toString());
	
	public static final Serializer SERIALIZER=new Serializer();
	
	public ResourceLocation id;
	public FluidIngredient input,output;
	public int time;
	
	public DistilleryRecipe(ResourceLocation id,FluidIngredient in,FluidIngredient out,int t){
		this.id=id;
		input=in;
		output=out;
		time=t;
	}
	
	public static DistilleryRecipe getRecipe(FluidStack stack,Level level){
		if(stack.isEmpty())
			return null;
		List<DistilleryRecipe> recipes=level.getRecipeManager().getAllRecipesFor(DISTILLERY_RECIPE);
		for(DistilleryRecipe recipe:recipes){
			if(recipe.input.test(stack.getFluid()))
				return recipe;
		}
		return null;
	}
	
	public static boolean isValidInput(FluidStack stack,Level level){
		if(stack.isEmpty())
			return false;
		List<DistilleryRecipe> recipes=level.getRecipeManager().getAllRecipesFor(DISTILLERY_RECIPE);
		for(DistilleryRecipe recipe:recipes){
			if(recipe.input.test(stack.getFluid()))
				return true;
		}
		return false;
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
		return id;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return DISTILLERY_RECIPE;
	}
	
	public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<DistilleryRecipe>{
		
		@Override
		public DistilleryRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
			FluidIngredient in=FluidIngredient.readJson(GsonHelper.getAsJsonObject(pSerializedRecipe,"input"));
			if(in.amount<0)
				throw new JsonParseException("input fluid amount cannot be lower than 0");
			FluidIngredient out=FluidIngredient.readJson(GsonHelper.getAsJsonObject(pSerializedRecipe,"output"));
			if(out.amount<0)
				throw new JsonParseException("output fluid amount cannot be lower than 0");
			if(out.getFluid()== Fluids.EMPTY)
				throw new JsonParseException("output fluid is empty or invalid");
			int t=GsonHelper.getAsInt(pSerializedRecipe,"time");
			return new DistilleryRecipe(pRecipeId,in,out,t);
		}
		
		@Nullable
		@Override
		public DistilleryRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
			FluidIngredient in=FluidIngredient.readBuffer(pBuffer);
			FluidIngredient out=FluidIngredient.readBuffer(pBuffer);
			int t=pBuffer.readInt();
			return new DistilleryRecipe(pRecipeId,in,out,t);
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf pBuffer, DistilleryRecipe pRecipe) {
			pRecipe.input.writeBuffer(pBuffer);
			pRecipe.output.writeBuffer(pBuffer);
			pBuffer.writeInt(pRecipe.time);
		}
	}
}
