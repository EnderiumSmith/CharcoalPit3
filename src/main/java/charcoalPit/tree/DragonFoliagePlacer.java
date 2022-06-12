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

public class DragonFoliagePlacer extends FoliagePlacer {
	
	public static final Codec<DragonFoliagePlacer> CODEC= RecordCodecBuilder.create((arg1)->{
		return foliagePlacerParts(arg1).apply(arg1,DragonFoliagePlacer::new);
	});
	
	public DragonFoliagePlacer(IntProvider p_i241999_1_, IntProvider p_i241999_2_) {
		super(p_i241999_1_, p_i241999_2_);
	}
	
	@Override
	protected FoliagePlacerType<?> type() {
		return ModFeatures.DRAGON_PLACER;
	}
	
	@Override
	protected void createFoliage(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, Random pRandom, TreeConfiguration pConfig,
								 int pMaxFreeTreeHeight, FoliageAttachment pAttachment, int pFoliageHeight, int pFoliageRadius, int pOffset) {
		for(int i=pOffset;i>=pOffset-pFoliageHeight;--i){
			this.placeLeavesRow(pLevel,pBlockSetter,pRandom,pConfig,pAttachment.pos(),1,i,pAttachment.doubleTrunk());
		}
	}
	
	@Override
	public int foliageHeight(Random p_230374_1_, int p_230374_2_, TreeConfiguration p_230374_3_) {
		return 2;
	}
	
	protected boolean shouldSkipLocation(Random pRandom, int pLocalX, int pLocalY, int pLocalZ, int pRange, boolean pLarge){
		if(pLocalY==0){
			return pLocalX==pRange&&pLocalZ==pRange;
		}
		return false;
	}
	
	/*@Override
	protected boolean shouldSkipLocation(Random p_230373_1_, int p_230373_2_, int p_230373_3_, int p_230373_4_, int p_230373_5_, boolean p_230373_6_) {
		if(p_230373_3_==0){
			return p_230373_2_ == p_230373_5_ && p_230373_4_ == p_230373_5_;
		}
		return false;
	}*/
}
