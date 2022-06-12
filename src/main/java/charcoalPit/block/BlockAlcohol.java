package charcoalPit.block;

import charcoalPit.fluid.ModFluidRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockAlcohol extends LiquidBlock{
	
	public BlockAlcohol() {
		super(()->ModFluidRegistry.AlcoholStill, Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops());
	}
	
	@Override
	public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		worldIn.scheduleTick(pos, state.getFluidState().getType(), this.getFluid().getTickDelay(worldIn));
	}
	
	@Override
	public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
			boolean isMoving) {
		worldIn.scheduleTick(pos, state.getFluidState().getType(), this.getFluid().getTickDelay(worldIn));
	}
}
