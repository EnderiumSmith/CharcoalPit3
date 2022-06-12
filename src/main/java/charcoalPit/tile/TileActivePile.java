package charcoalPit.tile;

import charcoalPit.core.Config;
import charcoalPit.core.MethodHelper;
import charcoalPit.core.ModBlockRegistry;
import charcoalPit.core.ModTileRegistry;
import charcoalPit.fluid.ModFluidRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class TileActivePile extends BlockEntity{

	//18H=18000ticks
	public int invalidTicks;
	public int burnTime;
	public int itemsLeft;
	public boolean isValid;
	public boolean isCoke;
	public FluidTank creosote;
	
	public TileActivePile(BlockPos pos, BlockState state) {
		this(pos,state,false);
	}
	
	public TileActivePile(BlockPos pos, BlockState state, boolean coal) {
		super(ModTileRegistry.ActivePile,pos,state);
		invalidTicks=0;
		burnTime=coal?Config.CokeTime.get()/10:Config.CharcoalTime.get()/10;
		itemsLeft=9;
		isValid=false;
		isCoke=coal;
		creosote=new FluidTank(1000);
	}
	
	public void tick() {
		if(!this.level.isClientSide){
			checkValid();
			if(burnTime>0){
				burnTime--;
				if(burnTime%1000==0)
					setChanged();
			}else{
				if(itemsLeft>0){
					itemsLeft--;
					creosote.fill(new FluidStack(ModFluidRegistry.CreosoteStill, isCoke?Config.CokeCreosote.get():Config.CharcoalCreosote.get()), FluidAction.EXECUTE);
					burnTime=isCoke?Config.CokeTime.get()/10:Config.CharcoalTime.get()/10;
				}else{
					this.level.setBlockAndUpdate(this.worldPosition, isCoke?ModBlockRegistry.CoalAsh.defaultBlockState():ModBlockRegistry.WoodAsh.defaultBlockState());
				}
			}
			if(creosote.getFluidAmount()>0){
				if(this.level.getBlockEntity(this.worldPosition.relative(Direction.DOWN))instanceof TileActivePile){
					TileActivePile down=(TileActivePile)this.level.getBlockEntity(this.worldPosition.relative(Direction.DOWN));
					creosote.drain(down.creosote.fill(creosote.getFluid(), FluidAction.EXECUTE), FluidAction.EXECUTE);
				}
			}
		}
	}
	
	public void checkValid(){
		if(!isValid){
			boolean valid=true;
			Direction[] neighbors=Direction.values();
			//check structure
			for(Direction facing:neighbors){
				if(!MethodHelper.CharcoalPitIsValidBlock(level, this.worldPosition, facing, isCoke)){
					valid=false;
					break;
				}
			}
			if(valid){
				isValid=true;
				invalidTicks=0;
			}else{
				if(invalidTicks<100){
					invalidTicks++;
					for(Direction facing:neighbors){
						//set fire
						BlockState block=this.level.getBlockState(this.worldPosition.relative(facing));
						if(block.isAir()||
								BaseFireBlock.canBePlacedAt(this.level, this.worldPosition.relative(facing),facing)){
							BlockState blockstate1 = BaseFireBlock.getState(this.level, this.worldPosition.relative(facing));
				            this.level.setBlock(this.worldPosition.relative(facing), blockstate1, 11);
						}
					}
				}else{
					this.level.removeBlock(this.worldPosition, false);
				}
			}
		}
	}
	
	@Override
	protected void saveAdditional(CompoundTag compound) {
		compound.putInt("invalid", invalidTicks);
		compound.putInt("time", burnTime);
		compound.putInt("items", itemsLeft);
		compound.putBoolean("valid", isValid);
		compound.putBoolean("coke", isCoke);
		compound.put("creosote", creosote.writeToNBT(new CompoundTag()));
	}
	
	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		invalidTicks=compound.getInt("invalid");
		burnTime=compound.getInt("time");
		itemsLeft=compound.getInt("items");
		isValid=compound.getBoolean("valid");
		isCoke=compound.getBoolean("coke");
		creosote.readFromNBT(compound.getCompound("creosote"));
		setChanged();
	}
}
