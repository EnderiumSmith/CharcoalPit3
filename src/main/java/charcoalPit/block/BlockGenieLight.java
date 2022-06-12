package charcoalPit.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;

public class BlockGenieLight extends Block {
	
	protected static final VoxelShape AABB = Block.box(6.0D, 6.0D, 6.0D, 10.0D, 10.0D, 10.0D);
	
	public BlockGenieLight() {
		super(Properties.of(Material.AIR).noCollission().instabreak().sound(SoundType.AMETHYST));
	}
	
	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return AABB;
	}
	
	@Override
	public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
		return 14;
	}
	
	public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, Random pRand) {
		double d0 = (double)pPos.getX() + 0.5D;
		double d1 = (double)pPos.getY() + 0.5D;
		double d2 = (double)pPos.getZ() + 0.5D;
		pLevel.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
		pLevel.addParticle(ParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D);
	}
	
	@Override
	public RenderShape getRenderShape(BlockState pState) {
		return RenderShape.INVISIBLE;
	}
}
