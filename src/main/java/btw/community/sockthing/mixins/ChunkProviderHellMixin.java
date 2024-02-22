package btw.community.sockthing.mixins;

import btw.community.sockthing.WorldGenMinableMetadata;
import net.minecraft.src.Block;
import net.minecraft.src.ChunkProviderHell;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(ChunkProviderHell.class)
public abstract class ChunkProviderHellMixin implements IChunkProvider {

    @Shadow private Random hellRNG;
    @Shadow private World worldObj;

    @Inject(method = "populate",
    at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/src/WorldGenMinable;<init>(III)V",
            shift = At.Shift.BEFORE
    ))
    public void populateWithGoldOre(IChunkProvider par1IChunkProvider, int x, int z, CallbackInfo ci) {

        WorldGenMinableMetadata worldGenMinable = new WorldGenMinableMetadata(Block.netherrack.blockID, 1, 8, Block.netherrack.blockID);
        int multiX = x * 16;
        int multiZ = z * 16;

        for (int tempIndex = 0; tempIndex < 4; ++tempIndex)
        {
            int xPos = multiX + this.hellRNG.nextInt(16);
            int yPos = this.hellRNG.nextInt(108) + 10;
            int zPos = multiZ + this.hellRNG.nextInt(16);
            worldGenMinable.generate(this.worldObj, this.hellRNG, xPos, yPos, zPos);
        }
    }

}
