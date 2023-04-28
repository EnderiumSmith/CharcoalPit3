package charcoalPit.block;

import charcoalPit.core.ModBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.common.IPlantable;

import java.util.Random;

public class BlockMapleLog extends Block {
	public static final BooleanProperty HAS_SAP = BooleanProperty.create("has_sap");
	
	public BlockMapleLog(Properties p_49795_) {
		super(p_49795_);
		this.registerDefaultState(this.getStateDefinition().any().setValue(HAS_SAP,false));
	}
	
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(HAS_SAP);
	}
	
	@Override
	public boolean isRandomlyTicking(BlockState pState) {
		return true;
	}
	
	@Override
	public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRandom) {
		if(!pState.getValue(HAS_SAP)&&pRandom.nextFloat()<0.166F){
			if(isPartOfTree(pLevel,pPos)) {
				pLevel.setBlock(pPos, pState.setValue(HAS_SAP, true), 3);
			}
		}
	}
	
	public static boolean isPartOfTree(Level level, BlockPos pos){
		BlockPos.MutableBlockPos current_pos=pos.below().mutable();
		BlockState current_state=level.getBlockState(current_pos);
		while(current_state.getBlock()== Blocks.ACACIA_LOG&&current_state.getValue(RotatedPillarBlock.AXIS)== Direction.Axis.Y){
			current_pos.move(Direction.DOWN);
			current_state=level.getBlockState(current_pos);
		}
		if(current_state.canSustainPlant(level, pos, Direction.UP, new IPlantable() {
			@Override
			public BlockState getPlant(BlockGetter level, BlockPos pos) {
				return Blocks.OAK_SAPLING.defaultBlockState();
			}
		})){
			current_pos=pos.above().mutable();
			current_state=level.getBlockState(current_pos);
			while(current_state.getBlock()== Blocks.ACACIA_LOG&&current_state.getValue(RotatedPillarBlock.AXIS)== Direction.Axis.Y){
				current_pos.move(Direction.UP);
				current_state=level.getBlockState(current_pos);
			}
			current_pos.move(Direction.DOWN);
			for(Direction dir:Direction.values()){
				if(dir==Direction.DOWN)
					continue;
				current_pos.move(dir);
				current_state=level.getBlockState(current_pos);
				current_pos.move(dir.getOpposite());
				if(current_state.getBlock()!=ModBlockRegistry.MapleLeaves||current_state.getValue(BlockFlowerLeaves.PERSISTENT)){
					return false;
				}
			}
			return true;
		}else{
			return false;
		}
	}
}
