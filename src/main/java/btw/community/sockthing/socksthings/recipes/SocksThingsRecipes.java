package btw.community.sockthing.socksthings.recipes;

import btw.community.sockthing.socksthings.SocksThingsAddon;
import btw.crafting.manager.CrucibleStokedCraftingManager;
import btw.crafting.recipe.RecipeManager;
import btw.item.BTWItems;
import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public class SocksThingsRecipes {
    public static void init(){
        if (SocksThingsAddon.requireQuartzForGlass){
            CrucibleStokedCraftingManager.getInstance().removeRecipe(new ItemStack(Block.glass), new ItemStack[]{new ItemStack(Block.sand)});

            RecipeManager.addStokedCrucibleRecipe(new ItemStack(Block.glass, 8),
                    new ItemStack[]{
                        new ItemStack(Block.sand, 8),
                        new ItemStack(Item.netherQuartz, 1)
                    });
        }
    }
}
