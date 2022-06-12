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

public class FruitFromJungle extends LootModifier {
	
	public Item item_a,item_b;
	
	public FruitFromJungle(LootItemCondition[] iLootConditions, Item item_a,Item item_b){
		super(iLootConditions);
		this.item_a=item_a;
		this.item_b=item_b;
	}
	
	@Nonnull
	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		if(context.hasParam(LootContextParams.BLOCK_STATE)&&context.getParamOrNull(LootContextParams.BLOCK_STATE).getBlock()==Blocks.JUNGLE_LEAVES){
			if(context.getRandom().nextBoolean()){
				generatedLoot.add(new ItemStack(item_a));
			}else{
				generatedLoot.add(new ItemStack(item_b));
			}
		}
		return generatedLoot;
	}
	
	public static class Serializer extends GlobalLootModifierSerializer<FruitFromJungle>{
		
		@Override
		public FruitFromJungle read(ResourceLocation location, JsonObject object, LootItemCondition[] ailootcondition) {
			Item straw= ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(object, "item_a")));
			Item item_b=ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(object, "item_b")));
			return new FruitFromJungle(ailootcondition,straw,item_b);
		}
		
		@Override
		public JsonObject write(FruitFromJungle instance) {
			return null;
		}
	}
}
