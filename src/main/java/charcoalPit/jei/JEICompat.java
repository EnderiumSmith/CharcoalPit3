package charcoalPit.jei;

import charcoalPit.CharcoalPit;
import charcoalPit.core.ModItemRegistry;
import charcoalPit.gui.*;
import charcoalPit.recipe.*;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmeltingRecipe;

import java.util.List;

@JeiPlugin
public class JEICompat implements IModPlugin {
	public static final ResourceLocation UID=new ResourceLocation(CharcoalPit.MODID,"main");
	@Override
	public ResourceLocation getPluginUid() {
		return UID;
	}
	
	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration) {
		registration.useNbtForSubtypes(ModItemRegistry.AlcoholBottle);
	}
	
	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		registration.addRecipeCategories(new BarrelRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new DistilleryRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new SteamPressRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new AlloyRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new BloomeryRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new HotBlastingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new CharcoalPitRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
	}
	
	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(ModItemRegistry.Barrel), BarrelRecipeCategory.UID);
		registration.addRecipeCatalyst(new ItemStack(ModItemRegistry.Distillery), DistilleryRecipeCategory.UID);
		registration.addRecipeCatalyst(new ItemStack(ModItemRegistry.SteamPress),SteamPressRecipeCategory.UID);
		registration.addRecipeCatalyst(new ItemStack(ModItemRegistry.alloy_mold),AlloyRecipeCategory.UID);
		registration.addRecipeCatalyst(new ItemStack(ModItemRegistry.Bloomeryy),BloomeryRecipeCategory.UID);
		registration.addRecipeCatalyst(new ItemStack(ModItemRegistry.Tuyere),HotBlastingRecipeCategory.UID);
		registration.addRecipeCatalyst(new ItemStack(Items.BLAST_FURNACE),HotBlastingRecipeCategory.UID);
		registration.addRecipeCatalyst(new ItemStack(ModItemRegistry.LogPile),CharcoalPitRecipeCategory.UID);
		registration.addRecipeCatalyst(new ItemStack(Items.COAL_BLOCK),CharcoalPitRecipeCategory.UID);
	}
	
	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		registration.addRecipes(new RecipeType<>(BarrelRecipeCategory.UID,BarrelRecipe.class), Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(BarrelRecipe.BARREL_RECIPE));
		registration.addRecipes(new RecipeType<>(DistilleryRecipeCategory.UID,DistilleryRecipe.class),Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(DistilleryRecipe.DISTILLERY_RECIPE));
		registration.addRecipes(new RecipeType<>(SteamPressRecipeCategory.UID, SquisherRecipe.class),Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(SquisherRecipe.SQUISH_RECIPE));
		registration.addRecipes(new RecipeType<>(AlloyRecipeCategory.UID, OreKilnRecipe.class),Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(OreKilnRecipe.ORE_KILN_RECIPE));
		registration.addRecipes(new RecipeType<>(BloomeryRecipeCategory.UID, BloomingRecipe.class),Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(BloomingRecipe.BLOOMING_RECIPE));
		registration.addRecipes(new RecipeType<>(HotBlastingRecipeCategory.UID,TuyereBlastingRecipe.class),Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(TuyereBlastingRecipe.HOT_BLASTING_RECIPE));
		
		registration.addIngredientInfo(List.of(new ItemStack(ModItemRegistry.TreeTap),new ItemStack(ModItemRegistry.MapleSap),new ItemStack(ModItemRegistry.MapleSyrup)),
				VanillaTypes.ITEM_STACK,new TranslatableComponent("charcoal_pit.jei.maple_syrup"));
		registration.addIngredientInfo(List.of(new ItemStack(ModItemRegistry.EthoxideBottle),new ItemStack(ModItemRegistry.VinegarBottle),new ItemStack(ModItemRegistry.AlcoholBottle)),
				VanillaTypes.ITEM_STACK,new TranslatableComponent("charcoal_pit.jei.bottle"));
		registration.addIngredientInfo(new ItemStack(ModItemRegistry.alloy_mold),VanillaTypes.ITEM_STACK,new TranslatableComponent("charcoal_pit.jei.alloy_info"));
		registration.addIngredientInfo(List.of(new ItemStack(ModItemRegistry.LogPile),new ItemStack(Items.CHARCOAL),new ItemStack(Items.COAL_BLOCK),new ItemStack(ModItemRegistry.Coke)),
				VanillaTypes.ITEM_STACK,new TranslatableComponent("charcoal_pit.instruction.build_pit"));
		registration.addIngredientInfo(new ItemStack(ModItemRegistry.plane),VanillaTypes.ITEM_STACK,new TranslatableComponent("charcoal_pit.jei.plane"));
		registration.addIngredientInfo(new ItemStack(ModItemRegistry.XPCrystal),VanillaTypes.ITEM_STACK,new TranslatableComponent("charcoal_pit.jei.xp_crystal"));
		registration.addIngredientInfo(new ItemStack(ModItemRegistry.DynamiteRemote),VanillaTypes.ITEM_STACK,new TranslatableComponent("charcoal_pit.jei.remote"));
		
		registration.addRecipes(new RecipeType<>(CharcoalPitRecipeCategory.UID, SmeltingRecipe.class),
				List.of(new SmeltingRecipe(new ResourceLocation(CharcoalPit.MODID,"charcoal_pit"),"jei_charcoal_pit", Ingredient.of(ModItemRegistry.LogPile),new ItemStack(Items.CHARCOAL,8),0.7F,12000),
						new SmeltingRecipe(new ResourceLocation(CharcoalPit.MODID,"coke_oven"),"jei_coke_oven", Ingredient.of(Items.COAL_BLOCK),new ItemStack(ModItemRegistry.Coke,8),0.7F,24000)));
		
	}
	
	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addRecipeClickArea(BarrelScreen.class,98,35,16,16,BarrelRecipeCategory.UID);
		registration.addRecipeClickArea(DistilleryScreen.class,88,40,9,29,DistilleryRecipeCategory.UID);
		registration.addRecipeClickArea(SteamPressScreen.class,65,35-18,16,16,SteamPressRecipeCategory.UID);
		registration.addRecipeClickArea(BloomeryScreen.class,79,34,24,16,BloomeryRecipeCategory.UID);
		registration.addRecipeClickArea(BlastFurnaceScreen.class,76,34,24,16,HotBlastingRecipeCategory.UID);
	}
}
