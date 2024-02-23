package btw.community.sockthing.socksthings;

import btw.AddonHandler;
import btw.BTWAddon;
import btw.block.blocks.NetherrackBlockFalling;
import net.minecraft.src.*;

public class SocksThingsAddon extends BTWAddon {
    private static SocksThingsAddon instance;
    public static NetherrackBlockFalling fallingNetherrackGold;
    public final int ID_FALLING_NETHERRACK_GOLD = 2999;
    private SocksThingsAddon() {
        super("Sock's Things Addon", "0.1.0", "STA");
    }

    @Override
    public void initialize() {
        AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");

        Item.itemsList[Block.netherrack.blockID] = (new ItemMultiTextureTile(Block.netherrack.blockID - 256, Block.netherrack, new String[] {"netherrack", "gold"})).setUnlocalizedName("hellrock");

        fallingNetherrackGold = new NetherrackBlockFalling(ID_FALLING_NETHERRACK_GOLD, "STABlockNetherGoldOre");
        Item.itemsList[fallingNetherrackGold.blockID] = new ItemBlock(fallingNetherrackGold.blockID - 256);
    }

    public static SocksThingsAddon getInstance() {
        if (instance == null)
            instance = new SocksThingsAddon();
        return instance;
    }
}
