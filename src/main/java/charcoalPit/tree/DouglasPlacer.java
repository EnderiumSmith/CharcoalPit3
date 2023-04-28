package charcoalPit.tree;

import charcoalPit.core.ModFeatures;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

import java.util.Random;
import java.util.function.BiConsumer;

public class DouglasPlacer extends FoliagePlacer {
	public static final Codec<DouglasPlacer> CODEC= RecordCodecBuilder.create((arg1)->{
		return foliagePlacerParts(arg1).apply(arg1,DouglasPlacer::new);
	});
	
	public DouglasPlacer(IntProvider pRadius, IntProvider pOffset) {
		super(pRadius, pOffset);
	}
	
	@Override
	protected FoliagePlacerType<?> type() {
		return ModFeatures.DOUGLAS_PLACER;
	}
	
	@Override
	protected void createFoliage(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, Random pRandom, TreeConfiguration pConfig, int pMaxFreeTreeHeight, FoliageAttachment pAttachment, int pFoliageHeight, int pFoliageRadius, int pOffset) {
		int par4=pAttachment.pos().getY();
		int t=0;
		for(int k1=par4+pFoliageHeight/3-1;k1<=par4+pFoliageHeight-1;k1++){
			int k2 = k1 - (par4 + pFoliageHeight);
			int z=pFoliageHeight;
			if (pFoliageHeight>20)
				z=20;
			int x = z/10 +1;
			if (k1-par4>pFoliageHeight/2||k1-par4-(pFoliageHeight/3)+2<3)
				x--;
			if(par4+pFoliageHeight-k1<4)
				x=1;
			if(x==z/10+1)
				t++;
			BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
			
			for(int j = -x; j <= x; ++j) {
				for(int k = -x; k <= x; ++k) {
					if ((j!=0||k!=0&&k2!=0)&&
							(Math.abs(j)+Math.abs(k)!=x*2||
									k1-par4>pFoliageHeight/2&&k1-par4<(4*pFoliageHeight/5)||
									k1-par4-pFoliageHeight/3+2==2)&&pRandom.nextInt(20)!=0) {
						blockpos$mutableblockpos.setWithOffset(pAttachment.pos(), j, k2, k);
						tryPlaceLeaf(pLevel, pBlockSetter, pRandom, pConfig, blockpos$mutableblockpos);
					}
				}
			}
		}
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
		for(int k3=pOffset;k3<pOffset+t;k3++){
			blockpos$mutableblockpos.setWithOffset(pAttachment.pos(), 0, k3, 0);
			tryPlaceLeaf(pLevel, pBlockSetter, pRandom, pConfig, blockpos$mutableblockpos);
		}
	}
	
	@Override
	public int foliageHeight(Random pRandom, int pHeight, TreeConfiguration pConfig) {
		return pHeight;
	}
	
	@Override
	protected boolean shouldSkipLocation(Random pRandom, int pLocalX, int pLocalY, int pLocalZ, int pRange, boolean pLarge) {
		return false;
	}
}
