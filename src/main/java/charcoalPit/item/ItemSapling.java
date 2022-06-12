package charcoalPit.item;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;

public class ItemSapling extends Item {
	public final BlockState state;
	public ItemSapling(Properties properties, BlockState state) {
		super(properties);
		this.state=state;
	}
	
	public InteractionResult useOn(UseOnContext context) {
		InteractionResult actionresulttype = this.tryPlace(context);
		return !actionresulttype.consumesAction() && this.isEdible() ? this.use(context.getLevel(), context.getPlayer(), context.getHand()).getResult() : actionresulttype;
	}
	
	public InteractionResult tryPlace(UseOnContext context){
		if(state.canSurvive(context.getLevel(),context.getClickedPos().relative(context.getClickedFace()))){
			if(context.getLevel().getBlockState(context.getClickedPos().relative(context.getClickedFace())).getMaterial().isReplaceable()){
				context.getLevel().setBlockAndUpdate(context.getClickedPos().relative(context.getClickedFace()),state);
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.FAIL;
	}
}
