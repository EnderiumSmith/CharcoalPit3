package charcoalPit.tree;

import charcoalPit.core.ModFeatures;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

import java.util.Random;
import java.util.function.BiConsumer;

public class CedarFoliagePlacer extends FoliagePlacer {
	
	public static final Codec<CedarFoliagePlacer> CODEC= RecordCodecBuilder.create((arg1)->{
		return foliagePlacerParts(arg1).apply(arg1,CedarFoliagePlacer::new);
	});
	
	public CedarFoliagePlacer(IntProvider pRadius, IntProvider pOffset) {
		super(pRadius, pOffset);
	}
	
	@Override
	protected FoliagePlacerType<?> type() {
		return ModFeatures.CEDAR_PLACER;
	}
	
	@Override
	protected void createFoliage(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, Random pRandom, TreeConfiguration pConfig, int pMaxFreeTreeHeight, FoliageAttachment pAttachment, int pFoliageHeight, int pFoliageRadius, int pOffset) {
		tryPlaceLeaf(pLevel,pBlockSetter,pRandom,pConfig,pAttachment.pos().relative(Direction.UP,pOffset+1));
		tryPlaceLeaf(pLevel,pBlockSetter,pRandom,pConfig,pAttachment.pos().relative(Direction.UP,pOffset));
		for(int i=1;i<=pFoliageHeight;i++){
			this.placeLeavesRow(pLevel,pBlockSetter,pRandom,pConfig,pAttachment.pos(),1,pOffset-i,false);
		}
		this.placeLeavesRow(pLevel,pBlockSetter,pRandom,pConfig,pAttachment.pos(),2,pOffset-pFoliageHeight-1,false);
		this.placeLeavesRow(pLevel,pBlockSetter,pRandom,pConfig,pAttachment.pos(),1,pOffset-pFoliageHeight-2,false);
	}
	
	@Override
	public int foliageHeight(Random pRandom, int pHeight, TreeConfiguration pConfig) {
		return pHeight/2;
	}
	
	@Override
	protected boolean shouldSkipLocation(Random pRandom, int pLocalX, int pLocalY, int pLocalZ, int pRange, boolean pLarge) {
		if(pRange>1)
			return pLocalX==pRange||pLocalZ==pRange;
		else {
			return pLocalX==pRange&&pLocalZ==pRange;
		}
	}
}
