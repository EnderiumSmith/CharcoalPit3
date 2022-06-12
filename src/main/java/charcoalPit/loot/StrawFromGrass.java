package charcoalPit.loot;

import java.util.List;

import com.google.gson.JsonObject;

import charcoalPit.CharcoalPit;
import net.minecraft.world.entity.player.Player;
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

public class StrawFromGrass extends LootModifier{

	public final Item straw;
	protected StrawFromGrass(LootItemCondition[] conditionsIn, Item straw) {
		super(conditionsIn);
		this.straw=straw;
	}
	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		/*if(context.hasParam(LootContextParams.BLOCK_STATE)){
			if(context.getParamOrNull(LootContextParams.BLOCK_STATE).is(BlockTags.getAllTags().getTag(new ResourceLocation(CharcoalPit.MODID, "straw_grass")))) {
				if(context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof Player) {
					((Player)context.getParamOrNull(LootContextParams.THIS_ENTITY)).getMainHandItem().hurtAndBreak(1, (Player)context.getParamOrNull(LootContextParams.THIS_ENTITY), (stack)->{});
					generatedLoot.add(new ItemStack(straw));
				}
			}
			if(context.getParamOrNull(LootContextParams.BLOCK_STATE).is(BlockTags.getAllTags().getTag(new ResourceLocation(CharcoalPit.MODID, "straw_grass_tall")))) {
				if(context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof Player) {
					((Player)context.getParamOrNull(LootContextParams.THIS_ENTITY)).getMainHandItem().hurtAndBreak(1, (Player)context.getParamOrNull(LootContextParams.THIS_ENTITY), (stack)->{});
					generatedLoot.add(new ItemStack(straw,2));
				}
			}
		}*/
		return generatedLoot;
	}
	
	public static class Serializer extends GlobalLootModifierSerializer<StrawFromGrass>{

		@Override
		public StrawFromGrass read(ResourceLocation location, JsonObject object, LootItemCondition[] conditionsIn) {
			Item straw=ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(object, "strawItem")));
			return new StrawFromGrass(conditionsIn, straw);
		}

		@Override
		public JsonObject write(StrawFromGrass instance) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	
}
