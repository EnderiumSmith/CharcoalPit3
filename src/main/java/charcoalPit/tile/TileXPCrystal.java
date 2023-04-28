package charcoalPit.tile;

import charcoalPit.block.BlockXPCrystal;
import charcoalPit.core.ModBlockRegistry;
import charcoalPit.core.ModItemRegistry;
import charcoalPit.core.ModTileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class TileXPCrystal extends BlockEntity {
	public static final int XP_TO_LV5=55;
	public static final int XP_TO_LV10=160;
	public static final int XP_TO_LV20=550;
	public static final int XP_TO_LV30=1395;
	public static final int XP_TO_LV40=2920;
	public static final int XP_TO_LV50=5345;
	public static final int MAX_XP=5580;
	public int XP;
	int delay;
	
	public TileXPCrystal(BlockPos pWorldPosition, BlockState pBlockState) {
		super(ModTileRegistry.XPCrystal, pWorldPosition, pBlockState);
		XP=0;
		delay=0;
	}
	
	public void tick(){
		delay--;
		if(delay<=0){
			delay=200;
			if(XP<MAX_XP){
				List<ExperienceOrb> orbs=level.getEntitiesOfClass(ExperienceOrb.class,new AABB(worldPosition.getX()-4,worldPosition.getY()-4, worldPosition.getZ()-4,
						worldPosition.getX()+5, worldPosition.getY()+5, worldPosition.getZ()+5));
				for(ExperienceOrb orb:orbs){
					if(orb.value<=MAX_XP-XP){
						XP+=orb.value;
						orb.kill();
					}
				}
				setChanged();
				updateShape();
			}
			if(XP>XP_TO_LV50){
				for(Direction dir:Direction.values()){
					if(tryPlaceCrystal(dir,worldPosition))
						break;
				}
				BlockPos bellow=worldPosition.below();
				for(Direction dir:Direction.values()){
					if(tryPlaceCrystal(dir,bellow))
						break;
				}
			}
		}
	}
	
	public boolean tryPlaceCrystal(Direction dir,BlockPos pos){
		BlockPos pos1=pos.relative(dir);
		BlockState state=level.getBlockState(pos1);
		if(state.isAir()){
			for(Direction direction:Direction.values()){
				BlockPos pos2=pos1.relative(direction);
				if(level.getBlockState(pos2).isFaceSturdy(level,pos2,direction.getOpposite())){
					level.setBlock(pos1, ModBlockRegistry.XPCrystal.defaultBlockState().setValue(BlockXPCrystal.FACING,direction.getOpposite()),3);
					TileXPCrystal tile= (TileXPCrystal) level.getBlockEntity(pos1);
					if(tile!=null) {
						tile.XP = 550;
						tile.setChanged();
						XP -= 550;
						setChanged();
					}
					return true;
				}
			}
		}
		return false;
	}
	
	public void updateShape(){
		int size=XP/XP_TO_LV30;
		if(size>3)
			size=3;
		if(size!=getBlockState().getValue(BlockXPCrystal.SIZE)){
			level.setBlock(worldPosition,getBlockState().setValue(BlockXPCrystal.SIZE,size),3);
		}
	}
	
	public void dropInventory(){
		do{
			ItemStack stack=new ItemStack(ModItemRegistry.XPCrystal);
			stack.getOrCreateTag().putInt("XP",Math.min(XP_TO_LV30,XP));
			XP-=Math.min(XP_TO_LV30,XP);
			Containers.dropItemStack(level,worldPosition.getX(),worldPosition.getY(),worldPosition.getZ(),stack);
		}while (XP>0);
	}
	
	@Override
	protected void saveAdditional(CompoundTag pTag) {
		super.saveAdditional(pTag);
		pTag.putInt("XP",XP);
	}
	
	@Override
	public void load(CompoundTag pTag) {
		super.load(pTag);
		XP=pTag.getInt("XP");
	}
}
