package club.eridani.cursa.mixin.mixins.render;

import club.eridani.cursa.module.modules.render.NoRender;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.entity.projectile.EntityArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderArrow.class)
public class MixinRenderArrow {
    @Inject(method = "doRender", at = @At("HEAD"), cancellable = true)
    public void doRender(EntityArrow entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
        if(NoRender.INSTANCE == null) return;
        if (NoRender.INSTANCE.isEnabled() && NoRender.INSTANCE.arrow.getValue()) info.cancel();
    }
}
