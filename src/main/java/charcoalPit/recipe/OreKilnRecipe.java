package charcoalPit.recipe;

import java.util.Iterator;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import charcoalPit.CharcoalPit;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class OreKilnRecipe implements Recipe<Container>{
	
	public static final ResourceLocation OREKILN=new ResourceLocation(CharcoalPit.MODID, "orekiln");
	public static final RecipeType<OreKilnRecipe> ORE_KILN_RECIPE=RecipeType.register(OREKILN.toString());
	
	public static final Serializer SERIALIZER=new Serializer();

	public final ResourceLocation id;
	public Ingredient[] input;
	public Ingredient output;
	public int amount;
	
	public OreKilnRecipe(ResourceLocation id, Ingredient output, int amount, Ingredient... input) {
		this.id=id;
		this.output=output;
		this.amount=amount;
		this.input=input;
	}
	
	//dynamic///////////
	public boolean isInputEqual(ItemStack in, int slot) {
		if(in.isEmpty())
			return false;
		if(slot>=input.length)
			return false;
		return input[slot].test(in);
	}
	
	public boolean isInputEqual(ItemStack in) {
		if(in.isEmpty())
			return false;
		for(int i=0;i<input.length;i++)
			if(input[i].test(in))
				return true;
		return false;
	}
	
	//static////////////
	public static boolean isValidInput(ItemStack stack, Level world) {
		List<OreKilnRecipe> recipes=world.getRecipeManager().getAllRecipesFor(ORE_KILN_RECIPE);
		for(OreKilnRecipe recipe:recipes)
			if(recipe.isInputEqual(stack)&&!recipe.output.test(new ItemStack(Items.BARRIER)))
				return true;
		return false;
	}
	
	public static int oreKilnGetFuelRequired(IItemHandler inventory) {
		return 0;
	}
	
	public static int oreKilnGetFuelAvailable(IItemHandler inventory) {
		return 0;
	}
	
	public static boolean oreKilnIsEmpty(IItemHandler inventory) {
		for(int i=1;i<inventory.getSlots();i++)
			if(!inventory.getStackInSlot(i).isEmpty())
				return false;
		return true;
	}
	
	public static int oreKilnGetOreAmount(IItemHandler inventory) {
		int a=0;
		for(int i=1;i<inventory.getSlots();i++) {
			if(!inventory.getStackInSlot(i).isEmpty())
				a+=inventory.getStackInSlot(i).getCount();
		}
		return a;
	}
	
	public static ItemStack OreKilnGetOutput(CompoundTag nbt, Level world) {
		List<OreKilnRecipe> recipes=world.getRecipeManager().getAllRecipesFor(ORE_KILN_RECIPE);
		ItemStackHandler kiln=new ItemStackHandler();
		for(OreKilnRecipe recipe:recipes) {
			if(recipe.output.isEmpty())
				continue;
			kiln.deserializeNBT(nbt);
			int r=0;
			while(!oreKilnIsEmpty(kiln)) {
				boolean b=false;
				for(int i=0;i<recipe.input.length;i++) {
					boolean e=false;
					for(int j=1;j<kiln.getSlots();j++) {
						if(recipe.isInputEqual(kiln.getStackInSlot(j), i)) {
							e=true;
							kiln.extractItem(j, 1, false);
							break;
						}
					}
					if(!e) {
						b=true;
						break;
					}
				}
				if(b) {
					r=0;
					break;
				}else {
					r++;
				}
			}
			if(r>0&&oreKilnIsEmpty(kiln)) {
				ItemStack out=recipe.output.getItems()[0].copy();
				out.setCount(r*recipe.amount);
				return out;
			}
		}
		return ItemStack.EMPTY;
	}
	
	////////////////////////////////////////////////////////
	//junk
	@Override
	public boolean matches(Container inv, Level worldIn) {
		return false;
	}

	@Override
	public ItemStack assemble(Container inv) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
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
		return ORE_KILN_RECIPE;
	}
	//////////////////////////////////////////////
	
	public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<OreKilnRecipe>{

		@Override
		public OreKilnRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
	         NonNullList<Ingredient> nonnulllist = readIngredients(GsonHelper.getAsJsonArray(json, "ingredients"));
	         if (nonnulllist.isEmpty()) {
	            throw new JsonParseException("No ingredients for shapeless recipe");
	         } else if (nonnulllist.size() > 8) {
	            throw new JsonParseException("Too many ingredients for shapeless recipe the max is " + 8);
	         } else {
	        	Ingredient output=Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "result"));
	        	int amount=GsonHelper.getAsInt(json, "amount");
	            return new OreKilnRecipe(recipeId, output, amount, nonnulllist.toArray(new Ingredient[0]));
	         }
		}
		
		private static NonNullList<Ingredient> readIngredients(JsonArray ingredientArray) {
	         NonNullList<Ingredient> nonnulllist = NonNullList.create();

	         for(int i = 0; i < ingredientArray.size(); ++i) {
	            Ingredient ingredient = Ingredient.fromJson(ingredientArray.get(i));
	            if (!ingredient.isEmpty()) {
	               nonnulllist.add(ingredient);
	            }
	         }

	         return nonnulllist;
	      }

		@Override
		public OreKilnRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			int l=buffer.readInt();
			Ingredient[] in=new Ingredient[l];
			for(int i=0;i<l;i++) {
				in[i]=Ingredient.fromNetwork(buffer);
			}
			int a=buffer.readInt();
			Ingredient o=Ingredient.fromNetwork(buffer);
			return new OreKilnRecipe(recipeId, o, a, in);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, OreKilnRecipe recipe) {
			buffer.writeInt(recipe.input.length);
			for(int i=0;i<recipe.input.length;i++) {
				recipe.input[i].toNetwork(buffer);
			}
			buffer.writeInt(recipe.amount);
			recipe.output.toNetwork(buffer);
		}
		
	}
	

}
