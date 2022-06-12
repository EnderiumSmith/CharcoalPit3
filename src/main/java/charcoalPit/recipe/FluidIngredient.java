package charcoalPit.recipe;

import charcoalPit.core.MethodHelper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.tags.*;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidIngredient {
	
	public static boolean isFluidInTag(Fluid fluid,TagKey<Fluid> tag){
		return ForgeRegistries.FLUIDS.tags().getTag(tag).contains(fluid);
	}
	
	public static boolean isFluidInTag(FluidStack stack,TagKey<Fluid> tag){
		return ForgeRegistries.FLUIDS.tags().getTag(tag).contains(stack.getFluid());
	}
	
	public Fluid fluid;
	public TagKey<Fluid> tag;
	public int amount;
	public CompoundTag nbt;
	
	public boolean test(Fluid in) {
		if(fluid!=null&&fluid==in)
			return true;
		if(tag!=null&&isFluidInTag(in,tag))
			return true;
		return false;
	}
	
	public Fluid getFluid() {
		if(fluid!=null&&fluid!=Fluids.EMPTY)
			return fluid;
		if(tag!=null&&!ForgeRegistries.FLUIDS.tags().getTag(tag).isEmpty())
			throw(new JsonParseException("Fluid Tag in output ingredient"));
		return Fluids.EMPTY;
	}
	
	public boolean isEmpty() {
		if(fluid!=null&&fluid!=Fluids.EMPTY)
			return false;
		if(tag!=null)
			return false;
		return true;
	}
	
	public static FluidIngredient readJson(JsonObject json){
		FluidIngredient ingredient=new FluidIngredient();
		if(json.has("fluid")) {
			ResourceLocation f=new ResourceLocation(GsonHelper.getAsString(json, "fluid"));
			Fluid fluid=ForgeRegistries.FLUIDS.getValue(f);
			if(fluid!=null&&fluid!=Fluids.EMPTY)
				ingredient.fluid=fluid;
		}
		if(json.has("tag")) {
			ResourceLocation t=new ResourceLocation(GsonHelper.getAsString(json, "tag"));
			try {
				ingredient.tag= ForgeRegistries.FLUIDS.tags().createTagKey(t);
			}catch (NullPointerException e){
				throw new JsonParseException("invalid fluid tag");
			}
		}
		if(ingredient.isEmpty())
			throw new JsonParseException("invalid fluid");
		if(json.has("amount")) {
			ingredient.amount=GsonHelper.getAsInt(json, "amount");
		}
		if(json.has("nbt")) {
			try {
				ingredient.nbt=TagParser.parseTag(GsonHelper.getAsString(json, "nbt"));
			} catch (CommandSyntaxException e) {
				throw new JsonParseException(e);
			}
		}
		return ingredient;
	}
	
	public void writeBuffer(FriendlyByteBuf buffer) {
		int mode=0;
		if(fluid!=null)
			mode+=1;
		if(tag!=null)
			mode+=2;
		buffer.writeByte(mode);
		if((mode&1)==1) {
			buffer.writeResourceLocation(fluid.getRegistryName());
		}
		if((mode&2)==2) {
			buffer.writeResourceLocation(getFluid().getRegistryName());
		}
		buffer.writeInt(amount);
		if(nbt!=null) {
			buffer.writeBoolean(true);
			buffer.writeNbt(nbt);
		}else {
			buffer.writeBoolean(false);
		}
	}
	
	public static FluidIngredient readBuffer(FriendlyByteBuf buffer) {
		FluidIngredient ingredient=new FluidIngredient();
		int mode=buffer.readByte();
		if(mode!=0) {
			ingredient.fluid=ForgeRegistries.FLUIDS.getValue(buffer.readResourceLocation());
		}
		ingredient.amount=buffer.readInt();
		if(buffer.readBoolean()) {
			ingredient.nbt=buffer.readNbt();
		}
		return ingredient;
	}
	
	
}
