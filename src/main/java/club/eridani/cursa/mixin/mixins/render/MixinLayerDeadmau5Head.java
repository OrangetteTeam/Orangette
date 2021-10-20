package club.eridani.cursa.mixin.mixins.render;

import club.eridani.cursa.Cursa;
import club.eridani.cursa.client.ModuleManager;
import club.eridani.cursa.module.modules.render.MickeyMouse;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerDeadmau5Head;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(LayerDeadmau5Head.class)
public class MixinLayerDeadmau5Head {

    @Redirect(method = "doRenderLayer(Lnet/minecraft/client/entity/AbstractClientPlayer;FFFFFFF)V", at = @At(value = "INVOKE" , target = "Ljava/lang/String;equals(Ljava/lang/Object;)Z"))
    public boolean equals(String s, Object anObject) {
        return Objects.requireNonNull(ModuleManager.getModuleByClass(MickeyMouse.class)).isEnabled();
    }

    @Redirect(method = "doRenderLayer(Lnet/minecraft/client/entity/AbstractClientPlayer;FFFFFFF)V", at = @At(value = "INVOKE" , target = "Lnet/minecraft/client/entity/AbstractClientPlayer;hasSkin()Z"))
    public boolean hasSkin(AbstractClientPlayer abstractClientPlayer) {
        return true;
    }

    @Redirect(method = "doRenderLayer(Lnet/minecraft/client/entity/AbstractClientPlayer;FFFFFFF)V", at = @At(value = "INVOKE" , target = "Lnet/minecraft/client/renderer/entity/RenderPlayer;bindTexture(Lnet/minecraft/util/ResourceLocation;)V"))
    public void bind(RenderPlayer renderPlayer, ResourceLocation p_110776_1_) {
        renderPlayer.bindTexture(new ResourceLocation("orangette/deadmau5.png"));
    }

}
