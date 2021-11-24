package club.eridani.cursa.mixin.mixins.accessor;

import net.minecraft.client.resources.IResourcePack;
import net.minecraftforge.fml.client.SplashProgress;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;

@Mixin(SplashProgress.class)
public interface AccessorSplashProgress {
    @Accessor(value = "thread", remap = false)
    static void setThread(Thread thread) {
        throw new AssertionError();
    }

    @Accessor(value = "miscPack", remap = false)
    static IResourcePack getMiscPack() {
        throw new AssertionError();
    }

    @Accessor(value = "done", remap = false)
    static boolean getDone() {
        throw new AssertionError();
    }

    @Accessor(value = "pause", remap = false)
    static boolean getPause() {
        throw new AssertionError();
    }

    @Accessor(value = "lock", remap = false)
    static Lock getLock() {
        throw new AssertionError();
    }

    @Accessor(value = "mutex", remap = false)
    static Semaphore getMutex() {
        throw new AssertionError();
    }

}
