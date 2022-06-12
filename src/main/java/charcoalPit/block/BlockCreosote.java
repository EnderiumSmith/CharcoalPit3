package charcoalPit.block;

import charcoalPit.fluid.ModFluidRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockCreosote extends LiquidBlock{

	public BlockCreosote() {
		super(()->ModFluidRegistry.CreosoteStill, Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops());
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
	
	@Override
	public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return 100;
	}
	
	@Override
	public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return 5;
	}
	
	@Override
	public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
		if(entityIn instanceof LivingEntity&&
				((LivingEntity)entityIn).getMobType()==MobType.ARTHROPOD) {
			entityIn.hurt(DamageSource.GENERIC, 1);
		}
	}

}
