package club.eridani.cursa.module.modules.combat;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.event.events.network.PacketEvent;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;

@Module(name = "GodBow", category = Category.COMBAT)
public class GodBow extends ModuleBase {

    @Override
    public void onPacketSend(PacketEvent.Send e) {
        if (!(e.getPacket() instanceof CPacketPlayerDigging)) return;
        CPacketPlayerDigging packet = (CPacketPlayerDigging) e.getPacket();
        if (packet.getAction() != CPacketPlayerDigging.Action.RELEASE_USE_ITEM) return;
        if (mc.player.inventory.getStackInSlot(mc.player.inventory.currentItem).getItem() != Items.BOW) return;
        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));
        for (int i = 0; i < 100; i++) {
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 1E-10, mc.player.posZ, true));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1E-10, mc.player.posZ, false));
        }
    }

}
