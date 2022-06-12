package charcoalPit.block;

import java.util.Random;

import charcoalPit.tile.TileActivePile;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import org.jetbrains.annotations.Nullable;

public class BlockActivePile extends Block implements EntityBlock {

	public final boolean isCoal;
	public BlockActivePile(boolean coal,Properties properties) {
		super(properties);
		isCoal=coal;
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		if(!isCoal) {
			builder.add(BlockStateProperties.AXIS);
		}
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
		return new TileActivePile(p_153215_,p_153216_,isCoal);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level plevel, BlockState pstate, BlockEntityType<T> ptype) {
		return plevel.isClientSide()?null:(level,blockpos,blockstate,t)-> {
			if (t instanceof TileActivePile tile) {
				tile.tick();
			}
		};
	}
	
	@Override
	public boolean isFireSource(BlockState state, LevelReader world, BlockPos pos, Direction side) {
		return true;
	}
	
	@Override
	public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
			boolean isMoving) {
		((TileActivePile)worldIn.getBlockEntity(pos)).isValid=false;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {
		double centerX = pos.getX() + 0.5F;
		double centerY = pos.getY() + 2F;
		double centerZ = pos.getZ() + 0.5F;
		//double d3 = 0.2199999988079071D;
		//double d4 = 0.27000001072883606D;
		worldIn.addParticle(ParticleTypes.SMOKE, centerX+(rand.nextDouble()-0.5), centerY, centerZ+(rand.nextDouble()-0.5), 0.0D, 0.1D, 0.0D);
		worldIn.addParticle(ParticleTypes.SMOKE, centerX+(rand.nextDouble()-0.5), centerY, centerZ+(rand.nextDouble()-0.5), 0.0D, 0.15D, 0.0D);
		worldIn.addParticle(ParticleTypes.SMOKE, centerX+(rand.nextDouble()-0.5), centerY-1, centerZ+(rand.nextDouble()-0.5), 0.0D, 0.1D, 0.0D);
		worldIn.addParticle(ParticleTypes.SMOKE, centerX+(rand.nextDouble()-0.5), centerY-1, centerZ+(rand.nextDouble()-0.5), 0.0D, 0.15D, 0.0D);
	}

}
