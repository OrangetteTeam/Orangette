package club.eridani.cursa.mixin.mixins.render;

import club.eridani.cursa.module.modules.render.NoRender;
import net.minecraft.client.renderer.entity.RenderFallingBlock;
import net.minecraft.entity.item.EntityFallingBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderFallingBlock.class)
public class MixinRenderFallingBlock {
    @Inject(method = "doRender", at = @At(value = "HEAD"), cancellable = true)
    private void doRender(EntityFallingBlock entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
        if(NoRender.INSTANCE == null) return;
        if (NoRender.INSTANCE.isEnabled() && NoRender.INSTANCE.fallingBlock.getValue()) info.cancel();
    }
}
