package club.eridani.cursa.mixin.mixins.render;

import club.eridani.cursa.module.modules.render.NoRender;
import net.minecraft.client.renderer.entity.RenderItemFrame;
import net.minecraft.entity.item.EntityItemFrame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderItemFrame.class)
public class MixinRenderItemFrame {
    @Inject(method = "doRender", at = @At(value = "HEAD"), cancellable = true)
    public void doRender(EntityItemFrame entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
        if(NoRender.INSTANCE == null) return;
        if (NoRender.INSTANCE.isEnabled() && NoRender.INSTANCE.frame.getValue()) info.cancel();
    }
}
