package club.eridani.cursa.mixin.mixins.entity;

import club.eridani.cursa.Cursa;
import club.eridani.cursa.event.events.player.PlayerDamageBlockEvent;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public class MixinPlayerControllerMP {
    @Inject(method = "onPlayerDamageBlock", at = @At("HEAD"), cancellable = true)
    private void onPlayerDamageBlockHook(BlockPos pos, EnumFacing face, CallbackInfoReturnable<Boolean> ci) {
        PlayerDamageBlockEvent event = new PlayerDamageBlockEvent( pos, face);
        Cursa.EVENT_BUS.post(event);
        if(event.isCancelled()) ci.setReturnValue(true);
    }
}
