package club.eridani.cursa.mixin.mixins.render;

import club.eridani.cursa.module.modules.render.NoRender;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public class MixinGuiIngame {
    @Inject(method = "renderPortal", at = @At(value = "HEAD"), cancellable = true)
    public void renderPortal(float timeInPortal, ScaledResolution scaledRes, CallbackInfo info) {
        if(NoRender.INSTANCE == null) return;
        if (NoRender.INSTANCE.isEnabled() && NoRender.INSTANCE.portal.getValue()) info.cancel();
    }
}
