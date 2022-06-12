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

public class PalmFoliagePlacer extends FoliagePlacer {
	
	public static final Codec<PalmFoliagePlacer> CODEC= RecordCodecBuilder.create((arg1)->{
		return foliagePlacerParts(arg1).apply(arg1,PalmFoliagePlacer::new);
	});
	
	public PalmFoliagePlacer(IntProvider p_i241999_1_, IntProvider p_i241999_2_) {
		super(p_i241999_1_, p_i241999_2_);
	}
	
	@Override
	protected FoliagePlacerType<?> type() {
		return null;
		//return ModFeatures.PALM_PLACER;
	}
	
	@Override
	protected void createFoliage(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, Random pRandom, TreeConfiguration pConfig,
								 int pMaxFreeTreeHeight, FoliageAttachment pAttachment, int pFoliageHeight, int pFoliageRadius, int pOffset) {
		int i=pOffset;
		if(i>=pOffset-pFoliageHeight){
			this.placeLeavesRow(pLevel,pBlockSetter,pRandom,pConfig,pAttachment.pos(),1,i,pAttachment.doubleTrunk());
			i--;
			if(i>=pOffset-pFoliageHeight){
				this.placeLeavesRow(pLevel,pBlockSetter,pRandom,pConfig,pAttachment.pos(),3,i,pAttachment.doubleTrunk());
				i--;
				if(i>=pOffset-pFoliageHeight){
					this.placeLeavesRow(pLevel,pBlockSetter,pRandom,pConfig,pAttachment.pos(),4,i,pAttachment.doubleTrunk());
					i--;
				}
			}
		}
	}
	
	/*@Override
	protected void createFoliage(LevelSimulatedRW p_230372_1_, Random p_230372_2_, TreeConfiguration p_230372_3_, int p_230372_4_, FoliageAttachment p_230372_5_, int p_230372_6_, int p_230372_7_, Set<BlockPos> p_230372_8_, int p_230372_9_, BoundingBox p_230372_10_) {
		int i=p_230372_9_;
		if(i>=p_230372_9_-p_230372_6_) {
			this.placeLeavesRow(p_230372_1_, p_230372_2_, p_230372_3_, p_230372_5_.foliagePos(), 1, p_230372_8_, i, p_230372_5_.doubleTrunk(), p_230372_10_);
			i--;
			if(i>=p_230372_9_-p_230372_6_) {
				this.placeLeavesRow(p_230372_1_, p_230372_2_, p_230372_3_, p_230372_5_.foliagePos(), 3, p_230372_8_, i, p_230372_5_.doubleTrunk(), p_230372_10_);
				i--;
				if(i>=p_230372_9_-p_230372_6_) {
					this.placeLeavesRow(p_230372_1_, p_230372_2_, p_230372_3_, p_230372_5_.foliagePos(), 4, p_230372_8_, i, p_230372_5_.doubleTrunk(), p_230372_10_);
					i--;
				}
			}
		}
	}*/
	
	@Override
	public int foliageHeight(Random p_230374_1_, int p_230374_2_, TreeConfiguration p_230374_3_) {
		return 2;
	}
	
	@Override
	protected boolean shouldSkipLocation(Random p_230373_1_, int p_230373_2_, int p_230373_3_, int p_230373_4_, int p_230373_5_, boolean p_230373_6_) {
		if(p_230373_3_==0){
			return p_230373_2_==p_230373_4_&&p_230373_2_>0;
		}
		if(p_230373_3_==-1){
			return p_230373_2_!=0&&p_230373_4_!=0&&!(p_230373_2_==1&&p_230373_4_==1);
		}
		if(p_230373_3_==-2){
			return (p_230373_2_!=0&&p_230373_4_!=0)||(p_230373_2_<3&&p_230373_4_<3);
		}
		return false;
	}
}
