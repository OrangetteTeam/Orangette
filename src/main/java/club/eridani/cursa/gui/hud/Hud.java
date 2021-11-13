package club.eridani.cursa.gui.hud;

import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;
import club.eridani.cursa.setting.settings.IntSetting;
import net.minecraft.client.gui.ScaledResolution;

public class Hud extends ModuleBase {
    private final Setting<Integer> x = setting("X" , 0 , 5000 , 0);
    private final Setting<Integer> y = setting("Y" , 0 , 5000 , 0);

    private int width , height = 0;

    public void renderHud() {
    }

    //do not override this method
    public void onRender(ScaledResolution s) {
        ((IntSetting)x).setMax(s.getScaledWidth());
        ((IntSetting)y).setMax(s.getScaledHeight());
        renderHud();
    }

    public void setSize(int width , int height) {
        this.width = width;
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
