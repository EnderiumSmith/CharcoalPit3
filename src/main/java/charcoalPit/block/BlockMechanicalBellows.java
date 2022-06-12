package charcoalPit.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import java.util.Random;

public class BlockMechanicalBellows extends BlockBellows {
	
	public BlockMechanicalBellows(){
		super(Material.STONE);
	}
	
	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		return InteractionResult.PASS;
	}
	
	@Override
	public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if(!worldIn.isClientSide){
			boolean flag=worldIn.hasNeighborSignal(pos);
			if(flag&&!state.getValue(PUSH)){
				worldIn.scheduleTick(pos, this, 10);
			}
		}
	}
	
	@Override
	public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand) {
		if(state.getValue(PUSH)) {
			if(!worldIn.hasNeighborSignal(pos)) {
				worldIn.setBlockAndUpdate(pos, state.setValue(PUSH, false));
			}else{
				worldIn.scheduleTick(pos, this, 1);
			}
		}else {
			if(worldIn.hasNeighborSignal(pos)){
				worldIn.setBlockAndUpdate(pos, state.setValue(PUSH, true));
				blow(worldIn, pos, state);
				worldIn.scheduleTick(pos, this, 20);
				worldIn.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 1F, 1F);
			}
		}
	}
}
