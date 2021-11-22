package club.eridani.cursa.mixin.mixins.render;

import club.eridani.cursa.module.modules.render.NoRender;
import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngameForge.class)
public class MixinGuiIngameForge {
    @Inject(method = "renderCrosshairs", at = @At(value = "HEAD"), cancellable = true, remap = false)
    public void renderCrosshairs(float partialTicks, CallbackInfo info) {
        if (NoRender.INSTANCE == null) return;
        if (NoRender.INSTANCE.isEnabled() && NoRender.INSTANCE.crossHair.getValue()) info.cancel();
    }
}
