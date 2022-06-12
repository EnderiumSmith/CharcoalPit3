package charcoalPit.block;

import charcoalPit.core.ModItemRegistry;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.ItemLike;

public class BlockLeeks extends CropBlock {
	
	public BlockLeeks(Properties builder) {
		super(builder);
	}
	
	@Override
	protected ItemLike getBaseSeedId() {
		return ModItemRegistry.Leek;
	}
}
