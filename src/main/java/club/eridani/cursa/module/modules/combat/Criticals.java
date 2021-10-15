package club.eridani.cursa.module.modules.combat;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.event.events.network.PacketEvent;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;

@Module(name = "Criticals", category = Category.COMBAT)
public class Criticals extends ModuleBase {

    @Override
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketUseEntity) {
            CPacketUseEntity packet = (CPacketUseEntity) event.getPacket();
            if (packet.getAction() == CPacketUseEntity.Action.ATTACK && mc.player.onGround) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + .1625, mc.player.posZ, false));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
            }
        }
    }

}
