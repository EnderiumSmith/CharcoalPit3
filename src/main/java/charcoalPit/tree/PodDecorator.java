package charcoalPit.tree;

import charcoalPit.core.ModFeatures;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class PodDecorator extends TreeDecorator {
	public static final Codec<PodDecorator> CODEC = Codec.unit(Blocks.COCOA.defaultBlockState()).fieldOf("pod").xmap(PodDecorator::new, (p_69989_) -> {
		return p_69989_.pod;
	}).codec();
	
	final BlockState pod;
	
	public PodDecorator(BlockState state){
		pod=state;
	}
	
	@Override
	protected TreeDecoratorType<?> type() {
		return ModFeatures.POD_DECORATOR;
	}
	
	@Override
	public void place(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, Random pRandom, List<BlockPos> pLogPositions, List<BlockPos> pLeafPositions) {
		int i = pLogPositions.get(0).getY();
		pLogPositions.stream().filter((p_69980_) -> {
			return p_69980_.getY() - i <= 2;
		}).forEach((p_161728_) -> {
			for(Direction direction : Direction.Plane.HORIZONTAL) {
				if (pRandom.nextFloat() <= 0.25F) {
					Direction direction1 = direction.getOpposite();
					BlockPos blockpos = p_161728_.offset(direction1.getStepX(), 0, direction1.getStepZ());
					if (Feature.isAir(pLevel, blockpos)) {
						pBlockSetter.accept(blockpos, pod.setValue(CocoaBlock.FACING, direction));
					}
				}
			}
			
		});
	}
}
