package charcoalPit.core;

import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;

public class Trades {
	
	public static VillagerTrades.ItemListing AppleSapling=new BasicItemListing(12,new ItemStack(ModItemRegistry.AppleSapling),6,1);
	public static VillagerTrades.ItemListing CherrySapling=new BasicItemListing(12,new ItemStack(ModItemRegistry.CherrySapling),6,1);
	public static VillagerTrades.ItemListing ChestnutSapling=new BasicItemListing(4,new ItemStack(ModItemRegistry.ChestnutSapling),12,1);
	public static VillagerTrades.ItemListing DragonSapling=new BasicItemListing(16,new ItemStack(ModItemRegistry.DragonSapling),6,1);
	public static VillagerTrades.ItemListing OliveSapling=new BasicItemListing(16,new ItemStack(ModItemRegistry.OliveSapling),6,1);
	public static VillagerTrades.ItemListing OrangeSapling=new BasicItemListing(12,new ItemStack(ModItemRegistry.OrangeSapling),6,1);
	public static VillagerTrades.ItemListing BananaSapling=new BasicItemListing(12,new ItemStack(ModItemRegistry.Bananana),6,1);
	public static VillagerTrades.ItemListing CoconutSapling=new BasicItemListing(12,new ItemStack(ModItemRegistry.Cococonut),6,1);
	
	public static void SaplingTrades(WandererTradesEvent event){
		event.getRareTrades().add(AppleSapling);
		event.getRareTrades().add(CherrySapling);
		event.getRareTrades().add(ChestnutSapling);
		event.getRareTrades().add(DragonSapling);
		event.getRareTrades().add(OliveSapling);
		event.getRareTrades().add(OrangeSapling);
		event.getRareTrades().add(BananaSapling);
		event.getRareTrades().add(CoconutSapling);
	}
	
	//public static VillagerTrades.ItemListing Corn=new BasicItemListing(new ItemStack(ModItemRegistry.Corn,24),ItemStack.EMPTY, new ItemStack(Items.EMERALD),16,2,0.05F);
	public static VillagerTrades.ItemListing Leek=new BasicItemListing(new ItemStack(ModItemRegistry.Leek,20),ItemStack.EMPTY, new ItemStack(Items.EMERALD),16,2,0.05F);
	
	public static VillagerTrades.ItemListing Chocolate=new BasicItemListing(1, new ItemStack(ModItemRegistry.Chocolate,4),12,15, 0.05F);
	public static VillagerTrades.ItemListing Cherry=new BasicItemListing(1,new ItemStack(ModItemRegistry.Cherry,10),16,5,0.05F);
	public static VillagerTrades.ItemListing Dango=new BasicItemListing(1,new ItemStack(ModItemRegistry.Dango,6),12,30,0.05F);
	
	public static VillagerTrades.ItemListing Kebab=new BasicItemListing(1,new ItemStack(ModItemRegistry.Kebabs,6),12,30,0.05F);
	public static VillagerTrades.ItemListing Vinegar=new BasicItemListing(2,new ItemStack(ModItemRegistry.VinegarBottle,4),12,30,0.05F);
	
	public static VillagerTrades.ItemListing Fugu=new BasicItemListing(2,new ItemStack(ModItemRegistry.Fugu,4),12,30,0.05F);
	
	public static void CropTrades(VillagerTradesEvent event){
		if(event.getType()== VillagerProfession.FARMER){
			//event.getTrades().get(1).add(Corn);
			event.getTrades().get(1).add(Leek);
			event.getTrades().get(2).add(Cherry);
			event.getTrades().get(4).add(Chocolate);
			event.getTrades().get(5).add(Dango);
		}
		if(event.getType()==VillagerProfession.BUTCHER){
			event.getTrades().get(4).add(Vinegar);
			event.getTrades().get(5).add(Kebab);
		}
		if(event.getType()==VillagerProfession.FISHERMAN){
			event.getTrades().get(5).add(Fugu);
		}
	}
	
}
