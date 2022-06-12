package charcoalPit.loot;

import com.google.gson.JsonObject;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;

public class DragonFromAcacia extends LootModifier {
	
	Item item;
	
	protected DragonFromAcacia(LootItemCondition[] conditionsIn, Item item) {
		super(conditionsIn);
		this.item=item;
	}
	
	@Nonnull
	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		if(context.hasParam(LootContextParams.BLOCK_STATE)&&context.getParamOrNull(LootContextParams.BLOCK_STATE).getBlock()== Blocks.ACACIA_LEAVES){
			generatedLoot.add(new ItemStack(this.item));
		}
		return generatedLoot;
	}
	
	public static class Serializer extends GlobalLootModifierSerializer<DragonFromAcacia>{
		
		@Override
		public DragonFromAcacia read(ResourceLocation location, JsonObject object, LootItemCondition[] ailootcondition) {
			Item straw= ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(object, "item")));
			return new DragonFromAcacia(ailootcondition,straw);
		}
		
		@Override
		public JsonObject write(DragonFromAcacia instance) {
			return null;
		}
	}
}
