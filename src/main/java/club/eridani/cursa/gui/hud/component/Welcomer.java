package club.eridani.cursa.gui.hud.component;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.gui.hud.Hud;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.setting.Setting;
import club.eridani.cursa.utils.RenderUtil;

import java.awt.*;

@Module(name = "Welcomer", category = Category.HUD)
public class Welcomer extends Hud {

    public Setting<Boolean> move = setting("Move", true);

    public void renderHud() {
        String welcome = "Welcome" + mc.player.getDisplayName().getFormattedText();
        RenderUtil.drawString("Welcome " + mc.player.getDisplayName().getFormattedText(), getX(), getY(), Color.cyan, true);
        setSize(RenderUtil.getStringWidth(welcome), RenderUtil.getStringHeight());
    }

}