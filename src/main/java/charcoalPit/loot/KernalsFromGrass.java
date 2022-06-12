package charcoalPit.loot;

import charcoalPit.CharcoalPit;
import com.google.gson.JsonObject;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;

public class KernalsFromGrass extends LootModifier {
	public Item item;
	
	public KernalsFromGrass(LootItemCondition[] conditionsIn,Item item) {
		super(conditionsIn);
		this.item=item;
	}
	
	@Nonnull
	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		/*if(context.hasParam(LootContextParams.BLOCK_STATE)) {
			if (context.getParamOrNull(LootContextParams.BLOCK_STATE).is(BlockTags.getAllTags().getTag(new ResourceLocation(CharcoalPit.MODID, "straw_grass"))) ||
					context.getParamOrNull(LootContextParams.BLOCK_STATE).is(BlockTags.getAllTags().getTag(new ResourceLocation(CharcoalPit.MODID, "straw_grass_tall"))))
				generatedLoot.add(new ItemStack(item));
		}*/
		return generatedLoot;
	}
	
	public static class Serializer extends GlobalLootModifierSerializer<KernalsFromGrass>{
		
		@Override
		public KernalsFromGrass read(ResourceLocation location, JsonObject object, LootItemCondition[] ailootcondition) {
			Item straw= ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(object, "item")));
			return new KernalsFromGrass(ailootcondition, straw);
		}
		
		@Override
		public JsonObject write(KernalsFromGrass instance) {
			return null;
		}
	}
	
}
