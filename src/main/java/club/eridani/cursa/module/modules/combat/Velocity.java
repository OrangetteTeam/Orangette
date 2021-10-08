package club.eridani.cursa.module.modules.combat;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.event.events.network.PacketEvent;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;

@Module(name = "Velocity" , category = Category.COMBAT)
public class Velocity extends ModuleBase {
    @Override
    public void onPacketReceive(PacketEvent.Receive event){
        Entity entity;
        SPacketEntityStatus packet;
        if (event.getPacket() instanceof SPacketEntityVelocity) {
            if (((SPacketEntityVelocity) event.getPacket()).getEntityID() == mc.player.entityId) {
                event.cancel();
                return;
            }
        }
        if (event.getPacket() instanceof SPacketEntityStatus
                && (packet = (SPacketEntityStatus) event.getPacket()).getOpCode() == 31
                && (entity = packet.getEntity(mc.world)) instanceof EntityFishHook) {
            EntityFishHook fishHook = (EntityFishHook) entity;
            if (fishHook.caughtEntity == mc.player) {
                event.cancel();
            }
        }
        if (event.getPacket() instanceof SPacketExplosion) {
            SPacketExplosion velocity_ = (SPacketExplosion) event.getPacket();
            velocity_.motionX *= 0;
            velocity_.motionY *= 0;
            velocity_.motionZ *= 0;
        }
    }
}
