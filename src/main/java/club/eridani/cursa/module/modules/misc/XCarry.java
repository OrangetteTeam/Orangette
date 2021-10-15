package club.eridani.cursa.module.modules.misc;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.event.events.network.PacketEvent;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import net.minecraft.network.play.client.CPacketCloseWindow;

@Module(name = "XCarry", category = Category.MISC)
public class XCarry extends ModuleBase {

    @Override
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketCloseWindow) {
            if (((CPacketCloseWindow) event.getPacket()).windowId == mc.player.inventoryContainer.windowId) {
                event.cancel();
            }
        }
    }

}
