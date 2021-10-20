package club.eridani.cursa.mixin.mixins.entity;

import club.eridani.cursa.module.modules.render.RattedSkin;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public class MixinAbstractClientPlayer {
    @Inject(method = "hasSkin" , at = @At(value = "RETURN"), cancellable = true)
    public void hasSkin(CallbackInfoReturnable<Boolean> cir){
        if(isEnabled()) cir.setReturnValue(true);
    }

    @Inject(method = "getLocationSkin()Lnet/minecraft/util/ResourceLocation;" , at = @At(value = "RETURN") , cancellable = true)
    private void getLocationSkin(CallbackInfoReturnable<ResourceLocation> cir) {
        if(isEnabled()) cir.setReturnValue(getLocation());
    }

    @Inject(method = "getLocationSkin(Ljava/lang/String;)Lnet/minecraft/util/ResourceLocation;" , at = @At(value = "RETURN") , cancellable = true)
    private static void getLocationSkinByUserName(CallbackInfoReturnable<ResourceLocation> cir) {
        if(isEnabled()) cir.setReturnValue(getLocation());
    }

    @Inject(method = "getSkinType" , at = @At(value = "RETURN") , cancellable = true)
    private void getSkinType(CallbackInfoReturnable<String> cir){
        if(isEnabled()) cir.setReturnValue("default");
    }

    private static boolean isEnabled(){
        return RattedSkin.INSTANCE.isEnabled();
    }

    private static ResourceLocation getLocation(){
        return new ResourceLocation(RattedSkin.INSTANCE.skin.getValue().equals("Natsumir_EN_82") ? "orangette/skin/natsumi.png" : "orangette/skin/yosshi.png");
    }
}
