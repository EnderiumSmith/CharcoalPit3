package charcoalPit.tree;

import charcoalPit.core.ModFeatures;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

import java.util.Random;
import java.util.function.BiConsumer;

public class OliveFoliagePlacer extends DragonFoliagePlacer{
	
	public static final Codec<OliveFoliagePlacer> CODEC= RecordCodecBuilder.create((arg1)->{
		return foliagePlacerParts(arg1).apply(arg1,OliveFoliagePlacer::new);
	});
	
	public OliveFoliagePlacer(IntProvider p_i241999_1_, IntProvider p_i241999_2_) {
		super(p_i241999_1_, p_i241999_2_);
	}
	
	@Override
	protected FoliagePlacerType<?> type() {
		return ModFeatures.OLIVE_PLACER;
	}
	
	@Override
	protected void createFoliage(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, Random pRandom, TreeConfiguration pConfig,
								 int pMaxFreeTreeHeight, FoliageAttachment pAttachment, int pFoliageHeight, int pFoliageRadius, int pOffset) {
		int r=1;
		for(int i=pOffset;i>=pOffset-pFoliageHeight;--i){
			this.placeLeavesRow(pLevel,pBlockSetter,pRandom,pConfig,pAttachment.pos(),r,i,pAttachment.doubleTrunk());
			r=r^3;
		}
	}
	
	@Override
	protected boolean shouldSkipLocation(Random pRandom, int pLocalX, int pLocalY, int pLocalZ, int pRange, boolean pLarge){
		if(pLocalY>-2){
			return pLocalX==pRange&&pLocalZ==pRange&&pRandom.nextFloat()<0.75F;
		}
		return pLocalX==pRange&&pLocalZ==pRange&&pRandom.nextFloat()<0.25F;
	}
}
