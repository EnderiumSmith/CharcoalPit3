package charcoalPit.tile;

import charcoalPit.core.ModTileRegistry;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class TileCreosoteCollector extends BlockEntity{
	
	public FluidTank creosote;
	int tick;
	static Capability<IFluidHandler> FLUID = CapabilityManager.get(new CapabilityToken<>(){});
	boolean flag;
	
	public TileCreosoteCollector(BlockPos pos,BlockState state) {
		super(ModTileRegistry.CreosoteCollector,pos,state);
		tick=0;
		creosote=new FluidTank(8000);
	}
	
	public void tick() {
		if(tick<20){
			tick++;
		}else{
			tick=0;
			flag=false;
			//collect creosote
			if(creosote.getFluidAmount()<creosote.getCapacity()&&this.level.getBlockEntity(this.worldPosition.relative(Direction.UP))instanceof TileActivePile){
				TileActivePile up=(TileActivePile)this.level.getBlockEntity(this.worldPosition.relative(Direction.UP));
				flag=flag||up.creosote.drain(creosote.fill(up.creosote.getFluid(), FluidAction.EXECUTE), FluidAction.EXECUTE).getAmount()>0;
				for(Direction facing:Direction.Plane.HORIZONTAL){
					if(creosote.getFluidAmount()<creosote.getCapacity())
						flag=flag||collectCreosote(this.worldPosition.relative(Direction.UP).relative(facing), facing, 3);
				}
			}
			//chanel creosote
			if(this.level.hasNeighborSignal(this.worldPosition)){
				for(Direction facing:Direction.Plane.HORIZONTAL){
					if(creosote.getFluidAmount()<creosote.getCapacity())
						flag=flag||chanelCreosote(this.worldPosition.relative(facing), facing, 3);
				}
			}
			//output creosote
			if(creosote.getFluidAmount()>0&&this.level.hasNeighborSignal(this.worldPosition)){
				BlockEntity tile=this.level.getBlockEntity(this.worldPosition.relative(Direction.DOWN));
				if(tile!=null){
					tile.getCapability(FLUID, Direction.UP).ifPresent((handler)->{
						flag=flag||creosote.drain(handler.fill(creosote.getFluid(), FluidAction.EXECUTE), FluidAction.EXECUTE).getAmount()>0;
					});
				}
			}
			if(flag)
				setChanged();
		}
		
	}
	public boolean collectCreosote(BlockPos pos, Direction facing, int runs){
		boolean flag=false;
		if(this.level.getBlockEntity(pos)instanceof TileActivePile){
			TileActivePile up=(TileActivePile)this.level.getBlockEntity(pos);
			flag=up.creosote.drain(creosote.fill(up.creosote.getFluid(), FluidAction.EXECUTE), FluidAction.EXECUTE).getAmount()>0;
			if(runs>0&&creosote.getFluidAmount()<creosote.getCapacity())
				flag=flag||collectCreosote(pos.relative(facing), facing, --runs);
		}
		return flag;
	}
	public boolean chanelCreosote(BlockPos pos, Direction facing, int runs){
		boolean flag=false;
		if(this.level.getBlockEntity(pos)instanceof TileCreosoteCollector){
			TileCreosoteCollector up=(TileCreosoteCollector)this.level.getBlockEntity(pos);
			flag=up.creosote.drain(creosote.fill(up.creosote.getFluid(), FluidAction.EXECUTE), FluidAction.EXECUTE).getAmount()>0;
			if(runs>0&&creosote.getFluidAmount()<creosote.getCapacity())
				flag=flag||chanelCreosote(pos.relative(facing), facing, --runs);
		}
		return flag;
	}
	public IFluidHandler external=new IFluidHandler() {
		
		@Override
		public boolean isFluidValid(int tank, FluidStack stack) {
			return creosote.isFluidValid(tank, stack);
		}
		
		@Override
		public int getTanks() {
			return creosote.getTanks();
		}
		
		@Override
		public int getTankCapacity(int tank) {
			return creosote.getTankCapacity(tank);
		}
		
		@Override
		public FluidStack getFluidInTank(int tank) {
			return creosote.getFluidInTank(tank);
		}
		
		@Override
		public int fill(FluidStack resource, FluidAction action) {
			return 0;
		}
		
		@Override
		public FluidStack drain(int maxDrain, FluidAction action) {
			return creosote.drain(maxDrain, action);
		}
		
		@Override
		public FluidStack drain(FluidStack resource, FluidAction action) {
			return creosote.drain(resource, action);
		}
	};
	
	public LazyOptional<IFluidHandler> fluid=LazyOptional.of(()->external);
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if((side==Direction.DOWN||side==null)&&cap.equals(FLUID)) {
			return fluid.cast();
		}
		return super.getCapability(cap, side);
	}
	
	@Override
	public void setRemoved() {
		fluid.invalidate();
		super.setRemoved();
	}
	
	@Override
	protected void saveAdditional(CompoundTag compound) {
		compound.put("creosote", creosote.writeToNBT(new CompoundTag()));
	}
	
	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		creosote.readFromNBT(nbt.getCompound("creosote"));
	}

}
