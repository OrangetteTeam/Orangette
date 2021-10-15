package club.eridani.cursa.mixin.mixins.accessor;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public interface AccessorMinecraft {

    @Accessor("rightClickDelayTimer")
    int getRightClickDelayTimer();

    @Accessor("rightClickDelayTimer")
    void setRightClickDelayTimer(int value);

}
