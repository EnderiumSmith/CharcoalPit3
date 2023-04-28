package charcoalPit.core;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
	
	public static ForgeConfigSpec CONFIG;
	
	public static ForgeConfigSpec.IntValue CharcoalTime,CokeTime;
	public static ForgeConfigSpec.IntValue CharcoalCreosote,CokeCreosote;
	
	public static ForgeConfigSpec.BooleanValue CycleFlowerTrees;
	public static ForgeConfigSpec.BooleanValue DowngradeStone;
	
	static {
		ForgeConfigSpec.Builder builder=new ForgeConfigSpec.Builder();
		charcoalPitConfig(builder);
		CONFIG=builder.build();
	}
	
	public static void charcoalPitConfig(ForgeConfigSpec.Builder builder) {
		builder.push("CharcoalPit/CokeOven");
		CharcoalTime=builder.comment("The time charcoal pits take to finish. 1000 Ticks = 1 MC hour").defineInRange("CharcoalTime", 8000, 1000, Integer.MAX_VALUE);
		CokeTime=builder.comment("The time coke ovens take to finish. 1000 Ticks = 1 MC hour").defineInRange("CokeTime", 18000, 1000, Integer.MAX_VALUE);
		CharcoalCreosote=builder.comment("Amount of creosote oil in mB produced per log").defineInRange("CharcoalCreosote", 50, 0, 1000);
		CokeCreosote=builder.comment("Amount of creosote oil in mB produced per coal").defineInRange("CokeCreosote", 100, 0, 1000);
		
		CycleFlowerTrees=builder.comment("If flower tree leaves should cycle between green and blooming variants with time").define("CycleFlowerTrees",true);
		DowngradeStone=builder.comment("If Stone(Flint) tools should be downgraded to wood tier").define("DowngradeStone",true);
		builder.pop();
	}
	
}
