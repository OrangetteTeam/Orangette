package club.eridani.cursa.mixin.mixins.render;

import club.eridani.cursa.module.modules.render.NoRender;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.world.storage.MapData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MapItemRenderer.class)
public class MixinMapItemRenderer {
    @Inject(method = "renderMap", at = @At(value = "HEAD"), cancellable = true)
    public void renderMap(MapData mapdataIn, boolean noOverlayRendering, CallbackInfo info) {
        if(NoRender.INSTANCE == null) return;
        if (NoRender.INSTANCE.isEnabled() && NoRender.INSTANCE.map.getValue()) info.cancel();
    }
}
