package charcoalPit.block;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.world.level.ItemLike;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.server.level.ServerLevel;

public class BlockAsh extends FallingBlock{

	public BlockAsh() {
		super(Properties.of(Material.SAND, MaterialColor.COLOR_LIGHT_GRAY).strength(0.5F).sound(SoundType.SAND));
	}
	
	@Override
	public int getExpDrop(BlockState state, LevelReader world, BlockPos pos, int fortune, int silktouch) {
		return silktouch==0?this.RANDOM.nextInt(3):0;
	}
	
	public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand) {
	      if (worldIn.isEmptyBlock(pos.below()) || isFree(worldIn.getBlockState(pos.below())) && pos.getY() >= 0) {
	         FallingBlockEntity fallingblockentity = new FallingBlockEntity(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, worldIn.getBlockState(pos)) {
				@Override
	        	public ItemEntity spawnAtLocation(ItemLike itemIn) {
	        		Block.getDrops(state, worldIn, new BlockPos(getX(), getY(), getZ()), null).forEach((stack)->{
	        			popResource(worldIn, new BlockPos(getX(), getY(), getZ()), stack);
	        		});
	        		return null;
	        	}
	         };
	         this.falling(fallingblockentity);
	         worldIn.addFreshEntity(fallingblockentity);
	      }
	   }
	
	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		ResourceLocation resourcelocation = this.getLootTable();
	      if (resourcelocation == BuiltInLootTables.EMPTY) {
	         return Collections.emptyList();
	      } else {
	    	 Entity entity=builder.getOptionalParameter(LootContextParams.THIS_ENTITY);
	    	 int fortune=0;
	    	 if(entity instanceof LivingEntity) {
	    		 fortune=EnchantmentHelper.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE, (LivingEntity)entity);
	    	 }
	         LootContext lootcontext = builder.withParameter(LootContextParams.BLOCK_STATE, state).withLuck(fortune).create(LootContextParamSets.BLOCK);
	         ServerLevel serverworld = lootcontext.getLevel();
	         LootTable loottable = serverworld.getServer().getLootTables().get(resourcelocation);
	         return loottable.getRandomItems(lootcontext);
	      }
	}

}
