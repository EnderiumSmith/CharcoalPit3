package charcoalPit.recipe;

import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import charcoalPit.CharcoalPit;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class BarrelRecipe implements Recipe<Container>{
	
	public static final ResourceLocation BARREL=new ResourceLocation(CharcoalPit.MODID, "barrel");
	public static final RecipeType<BarrelRecipe> BARREL_RECIPE=RecipeType.register(BARREL.toString());
	
	public static final Serializer SERIALIZER=new Serializer();
	
	public final ResourceLocation id;
	public Ingredient item_in, item_out;
	public FluidIngredient fluid_in, fluid_out;
	public int in_amount, out_amount, flags ,time;
	public CompoundTag nbt_out;
	//flags 1-void excess fluid in 10-void excess item out 100-has item output 1000-has fluid output 10000-has nbt
	
	public BarrelRecipe(ResourceLocation id, Ingredient iin, Ingredient iout, FluidIngredient fin, FluidIngredient fout, int in, int out, int flags, CompoundTag nbt, int time) {
		this.id=id;
		this.item_in=iin;
		this.item_out=iout;
		this.fluid_in=fin;
		this.fluid_out=fout;
		this.in_amount=in;
		this.out_amount=out;
		this.flags=flags;
		this.nbt_out=nbt;
		this.time=time;
	}
	
	public static BarrelRecipe getRecipe(ItemStack item, FluidStack fluid, Level world) {
		if(item.isEmpty()||fluid.isEmpty())
			return null;
		List<BarrelRecipe> recipes=world.getRecipeManager().getAllRecipesFor(BARREL_RECIPE);
		for(BarrelRecipe recipe:recipes) {
			if(recipe.item_in.test(item)&&recipe.fluid_in.test(fluid.getFluid()))
				return recipe;
		}
		return null;
	}
	
	public static boolean isValidItem(ItemStack stack, Level world) {
		if(stack.isEmpty())
			return false;
		List<BarrelRecipe> recipes=world.getRecipeManager().getAllRecipesFor(BARREL_RECIPE);
		for(BarrelRecipe recipe:recipes) {
			if(recipe.item_in.test(stack))
				return true;
		}
		return false;
	}

	@Override
	public boolean matches(Container inv, Level worldIn) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ItemStack assemble(Container inv) {
		// TODO Auto-generated method stub
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ItemStack getResultItem() {
		// TODO Auto-generated method stub
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
		return BARREL_RECIPE;
	}
	
	public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<BarrelRecipe>{

		@Override
		public BarrelRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			int flags=0;
			Ingredient iin=Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "item_in"));
			int in=1;
			if(json.has("in_amount")) {
				in=GsonHelper.getAsInt(json, "in_amount");
				if(in<0)
					in=1;
			}
			Ingredient iout=null;
			int out=1;
			if(json.has("item_out")) {
				iout=Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "item_out"));
				flags=flags|0b100;
				if(json.has("out_amount")) {
					out=GsonHelper.getAsInt(json, "out_amount");
					if(out<1)
						out=1;
				}
			}
			FluidIngredient fin=FluidIngredient.readJson(GsonHelper.getAsJsonObject(json, "fluid_in"));
			if(fin.amount<0)
				throw new JsonParseException("input fluid amount cannot be lower than 0");
			FluidIngredient fout=null;
			if(json.has("fluid_out")) {
				fout=FluidIngredient.readJson(GsonHelper.getAsJsonObject(json, "fluid_out"));
				flags=flags|0b1000;
				if(fout.amount<0)
					throw new JsonParseException("output fluid amount cannot be lower then 0");
			}
			int flags2=0;
			if(json.has("flags")) {
				flags2=GsonHelper.getAsInt(json, "flags");
				flags2=flags2&0b11;
				flags=flags|flags2;
			}
			CompoundTag nbt=null;
			if((flags&0b100)==0b100&&json.has("nbt")) {
				try {
					nbt=TagParser.parseTag(GsonHelper.getAsString(json, "nbt"));
					flags=flags|0b10000;
				} catch (CommandSyntaxException e) {
					throw new JsonParseException(e);
				}
			}
			int time=GsonHelper.getAsInt(json, "time");
			return new BarrelRecipe(recipeId, iin, iout, fin, fout, in, out, flags, nbt, time);
		}

		@Override
		public BarrelRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			int flags=buffer.readInt();
			Ingredient iin=Ingredient.fromNetwork(buffer);
			int in=buffer.readInt();
			Ingredient iout=null;
			int out=1;
			CompoundTag nbt=null;
			if((flags&0b100)==0b100) {
				iout=Ingredient.fromNetwork(buffer);
				out=buffer.readInt();
				if((flags&0b10000)==0b10000) {
					nbt=buffer.readNbt();
				}
			}
			FluidIngredient fin=FluidIngredient.readBuffer(buffer);
			FluidIngredient fout=null;
			if((flags&0b1000)==0b1000) {
				fout=FluidIngredient.readBuffer(buffer);
			}
			int time=buffer.readInt();
			return new BarrelRecipe(recipeId, iin, iout, fin, fout, in, out, flags, nbt, time);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, BarrelRecipe recipe) {
			buffer.writeInt(recipe.flags);
			recipe.item_in.toNetwork(buffer);
			buffer.writeInt(recipe.in_amount);
			if((recipe.flags&0b100)==0b100) {
				recipe.item_out.toNetwork(buffer);
				buffer.writeInt(recipe.out_amount);
				if((recipe.flags&0b10000)==0b10000) {
					buffer.writeNbt(recipe.nbt_out);
				}
			}
			recipe.fluid_in.writeBuffer(buffer);
			if((recipe.flags&0b1000)==0b1000) {
				recipe.fluid_out.writeBuffer(buffer);
			}
			buffer.writeInt(recipe.time);
		}
		
	}
	
}
