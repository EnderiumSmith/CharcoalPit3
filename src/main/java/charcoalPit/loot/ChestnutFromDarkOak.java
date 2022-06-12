package charcoalPit.loot;

import charcoalPit.core.ModItemRegistry;
import com.google.gson.JsonObject;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;

public class ChestnutFromDarkOak extends LootModifier {
	protected ChestnutFromDarkOak(LootItemCondition[] conditionsIn) {
		super(conditionsIn);
	}
	
	@Nonnull
	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		if(context.hasParam(LootContextParams.BLOCK_STATE)&&context.getParamOrNull(LootContextParams.BLOCK_STATE).getBlock()== Blocks.DARK_OAK_LEAVES) {
			for (int i = 0; i < generatedLoot.size(); i++) {
				if (generatedLoot.get(i).getItem() == Items.APPLE) {
					generatedLoot.remove(i);
					generatedLoot.add(new ItemStack(ModItemRegistry.ChestNut));
					break;
				}
			}
		}
		return generatedLoot;
	}
	
	public static class Serializer extends GlobalLootModifierSerializer<ChestnutFromDarkOak>{
		
		@Override
		public ChestnutFromDarkOak read(ResourceLocation location, JsonObject object, LootItemCondition[] ailootcondition) {
			return new ChestnutFromDarkOak(ailootcondition);
		}
		
		@Override
		public JsonObject write(ChestnutFromDarkOak instance) {
			return null;
		}
	}
}
