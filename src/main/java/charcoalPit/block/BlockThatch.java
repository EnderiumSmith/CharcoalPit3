package charcoalPit.block;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockThatch extends Block{
	public BlockThatch() {
		super(Properties.of(Material.WOOD, MaterialColor.COLOR_YELLOW).strength(0.5F).sound(SoundType.GRASS));
	}
	
	@Override
	public void fallOn(Level worldIn, BlockState p_152427_, BlockPos pos, Entity entityIn, float fallDistance) {
		entityIn.causeFallDamage(fallDistance, 0.2F, DamageSource.FALL);
	}
	
	@Override
	public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return 100;
	}
	
	@Override
	public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return 60;
	}
	
	@Override
	public boolean isFlammable(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return true;
	}

}
