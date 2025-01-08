package btw.community.sockthing.socksthings;

import btw.AddonHandler;
import btw.BTWAddon;
import btw.community.sockthing.socksthings.blocks.NetherrackBlockFalling;
import btw.community.sockthing.socksthings.recipes.SocksThingsRecipes;
import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemMultiTextureTile;

import java.util.Map;

public class SocksThingsAddon extends BTWAddon {
    private static SocksThingsAddon instance;

    public static btw.block.blocks.NetherrackBlockFalling fallingNetherrackGold;
    public final int ID_FALLING_NETHERRACK_GOLD = 3999;
    private Map<String, String> propertyValues;

    // configuration settings
    public static boolean generateNetherGoldOre = true;
    public static boolean requireQuartzForGlass = true;
//    public static boolean enableColoredBeds = true;

    public SocksThingsAddon() {
        super();
    }

    @Override
    public void preInitialize() {
        registerConfigProperties();
    }

    @Override
    public void initialize() {
        AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
        AddonHandler.logMessage("Config: generateNetherGoldOre=" + generateNetherGoldOre);
        AddonHandler.logMessage("Config: requireQuartzForGlass=" + requireQuartzForGlass);

        SocksThingsRecipes.init();

        Item.itemsList[Block.netherrack.blockID] = (new ItemMultiTextureTile(Block.netherrack.blockID - 256, Block.netherrack, new String[] {"netherrack", "gold"})).setUnlocalizedName("netherrack");

        fallingNetherrackGold = new NetherrackBlockFalling(ID_FALLING_NETHERRACK_GOLD, "STABlockNetherGoldOre");
        Item.itemsList[fallingNetherrackGold.blockID] = new ItemBlock(fallingNetherrackGold.blockID - 256);
    }

    private void registerConfigProperties() {
        //Gameplay config
        this.registerProperty("generateNetherGoldOre", "True", "Set the following to False to disable Nether Gold Ore Generation");
        this.registerProperty("requireQuartzForGlass", "True", "Set the following to False to disable the requirement for Quartz to make Glass");
//        this.registerProperty("enableColoredBeds", "True", "Set the following to False to disable Colored Beds");

        //Block IDs
        this.registerProperty("BlockFallingNetherrack", "3999", "***Block IDs***\n\n");
    }

    @Override
    public void handleConfigProperties(Map<String, String> propertyValues) {
        this.propertyValues = propertyValues;

        generateNetherGoldOre = Boolean.parseBoolean(this.propertyValues.get("generateNetherGoldOre"));
        requireQuartzForGlass = Boolean.parseBoolean(this.propertyValues.get("requireQuartzForGlass"));
    }
}