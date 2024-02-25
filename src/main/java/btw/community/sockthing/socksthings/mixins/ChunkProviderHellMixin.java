package btw.community.sockthing.socksthings.mixins;

import btw.community.sockthing.socksthings.WorldGenMinableMetadata;
import net.minecraft.src.Block;
import net.minecraft.src.ChunkProviderHell;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sun.misc.Unsafe;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
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

        Random random = copyRandomUnsafe(this.hellRNG);

        WorldGenMinableMetadata worldGenMinable = new WorldGenMinableMetadata(Block.netherrack.blockID, 1, 8, Block.netherrack.blockID);
        int multiX = x * 16;
        int multiZ = z * 16;

        for (int tempIndex = 0; tempIndex < 4; ++tempIndex)
        {
            int xPos = multiX + random.nextInt(16);
            int yPos = random.nextInt(108) + 10;
            int zPos = multiZ + random.nextInt(16);
            worldGenMinable.generate(this.worldObj, random, xPos, yPos, zPos);
        }
    }

    // --- Arminias' Random Code --- //

    private static Unsafe unsafe;
    private static long seedOffset;
    private static long value;

    private static Random copyRandomUnsafe(Random src) {
        try {
            // New Java 17 code, with security circumvention
            // Create a clone via reflection
            Random clone = (Random)src.getClass().getDeclaredConstructor().newInstance();
            // Get the unsafe instance
            if (unsafe == null) {
                Field f = Unsafe.class.getDeclaredField("theUnsafe");
                f.setAccessible(true);
                unsafe = (Unsafe) f.get(null);
            }
            // Seed is an object (AtomicLong)
            // Get the seed via unsafe
            if (seedOffset == 0) {
                seedOffset = unsafe.objectFieldOffset(Random.class.getDeclaredField("seed"));
            }
            Object seed = unsafe.getObject(src, seedOffset);
            // Get the value of the seed
            if (value == 0) {
                value = unsafe.objectFieldOffset(seed.getClass().getDeclaredField("value"));
            }
            long seedValue = unsafe.getLong(seed, value);
            // Create a new seed object (AtomicLong)
            Object newSeed = unsafe.allocateInstance(seed.getClass());
            // Set the value of the new seed
            unsafe.putLong(newSeed, value, seedValue);
            // Set the seed into the clone
            unsafe.putObject(clone, seedOffset, newSeed);
            return clone;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

}
