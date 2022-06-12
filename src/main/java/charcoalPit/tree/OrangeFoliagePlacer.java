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

public class OrangeFoliagePlacer extends FoliagePlacer {
	
	public static final Codec<OrangeFoliagePlacer> CODEC= RecordCodecBuilder.create((arg1)->{
		return foliagePlacerParts(arg1).apply(arg1,OrangeFoliagePlacer::new);
	});
	
	public OrangeFoliagePlacer(IntProvider pRadius, IntProvider pOffset) {
		super(pRadius, pOffset);
	}
	
	@Override
	protected FoliagePlacerType<?> type() {
		return ModFeatures.ORANGE_PLACER;
	}
	
	@Override
	protected void createFoliage(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, Random pRandom, TreeConfiguration pConfig, int pMaxFreeTreeHeight, FoliageAttachment pAttachment, int pFoliageHeight, int pFoliageRadius, int pOffset) {
		for(int i=pOffset;i>=pOffset-pFoliageHeight;--i){
			this.placeLeavesRow(pLevel,pBlockSetter,pRandom,pConfig,pAttachment.pos(),2,i,pAttachment.doubleTrunk());
		}
	}
	
	@Override
	public int foliageHeight(Random pRandom, int pHeight, TreeConfiguration pConfig) {
		return 3;
	}
	
	@Override
	protected boolean shouldSkipLocation(Random pRandom, int pLocalX, int pLocalY, int pLocalZ, int pRange, boolean pLarge) {
		if(pLocalY==0||pLocalY==-3){
			if(pLocalX==pRange||pLocalZ==pRange)
				if(pLocalX==0||pLocalZ==0) {
					return pRandom.nextFloat() < 0.5F;
				}else
					return true;
		}else if(pLocalX==pRange&&pLocalZ==pRange)
			return pRandom.nextFloat()<0.5F;
		return false;
	}
}
