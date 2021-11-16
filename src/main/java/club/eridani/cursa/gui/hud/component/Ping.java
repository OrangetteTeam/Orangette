package club.eridani.cursa.gui.hud.component;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.gui.hud.Hud;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.utils.RenderUtil;
import net.minecraft.client.network.NetworkPlayerInfo;

import java.awt.*;

@Module(name = "Ping",category = Category.HUD)
public class Ping extends Hud {

    public void renderHud(){

        NetworkPlayerInfo info = mc.getConnection().getPlayerInfo(mc.player.getUniqueID());
        String str = "Ping "+ info.getResponseTime();
        RenderUtil.drawString(str, getX(), getY(), Color.cyan, true);
        setSize(RenderUtil.getStringWidth(str), RenderUtil.getStringHeight());

    }

}