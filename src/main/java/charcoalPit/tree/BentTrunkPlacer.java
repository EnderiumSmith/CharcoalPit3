package charcoalPit.tree;

import charcoalPit.core.ModFeatures;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class BentTrunkPlacer extends TrunkPlacer {
	
	public static final Codec<BentTrunkPlacer> CODEC= RecordCodecBuilder.create((arg1)->{
		return trunkPlacerParts(arg1).apply(arg1,BentTrunkPlacer::new);
	});
	
	public BentTrunkPlacer(int p_i232060_1_, int p_i232060_2_, int p_i232060_3_) {
		super(p_i232060_1_, p_i232060_2_, p_i232060_3_);
	}
	
	@Override
	protected TrunkPlacerType<?> type() {
		return null;
		//return ModFeatures.BENT_PLACER;
	}
	
	@Override
	public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, Random pRandom,
															int pFreeTreeHeight, BlockPos pPos, TreeConfiguration pConfig) {
		setDirtAt(pLevel,pBlockSetter,pRandom,pPos.below(),pConfig);
		List<FoliagePlacer.FoliageAttachment> list = Lists.newArrayList();
		Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(pRandom);
		int i = pFreeTreeHeight - pRandom.nextInt(4) - 1;
		int j = 3 - pRandom.nextInt(3);
		BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();
		int k = pPos.getX();
		int l = pPos.getZ();
		int i1 = 0;
		
		for(int j1 = 0; j1 < pFreeTreeHeight; ++j1) {
			int k1 = pPos.getY() + j1;
			if (j1 >= i && j > 0) {
				k += direction.getStepX();
				l += direction.getStepZ();
				--j;
			}
			
			if(placeLog(pLevel,pBlockSetter,pRandom,blockpos$mutable.set(k, k1, l),pConfig)){
				i1 = k1 + 1;
			}
		}
		
		list.add(new FoliagePlacer.FoliageAttachment(new BlockPos(k, i1, l), 1, false));
		k = pPos.getX();
		l = pPos.getZ();
		
		return list;
	}
	
	/*@Override
	public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedRW p_230382_1_, Random p_230382_2_, int p_230382_3_, BlockPos p_230382_4_, Set<BlockPos> p_230382_5_, BoundingBox p_230382_6_, TreeConfiguration p_230382_7_) {
		setDirtAt(p_230382_1_, p_230382_4_.below());
		List<FoliagePlacer.FoliageAttachment> list = Lists.newArrayList();
		Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(p_230382_2_);
		int i = p_230382_3_ - p_230382_2_.nextInt(4) - 1;
		int j = 3 - p_230382_2_.nextInt(3);
		BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();
		int k = p_230382_4_.getX();
		int l = p_230382_4_.getZ();
		int i1 = 0;
		
		for(int j1 = 0; j1 < p_230382_3_; ++j1) {
			int k1 = p_230382_4_.getY() + j1;
			if (j1 >= i && j > 0) {
				k += direction.getStepX();
				l += direction.getStepZ();
				--j;
			}
			
			if (placeLog(p_230382_1_, p_230382_2_, blockpos$mutable.set(k, k1, l), p_230382_5_, p_230382_6_, p_230382_7_)) {
				i1 = k1 + 1;
			}
		}
		
		list.add(new FoliagePlacer.FoliageAttachment(new BlockPos(k, i1, l), 1, false));
		k = p_230382_4_.getX();
		l = p_230382_4_.getZ();
		
		return list;
	}*/
}
