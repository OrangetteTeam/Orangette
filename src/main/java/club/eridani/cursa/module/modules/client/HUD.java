package club.eridani.cursa.module.modules.client;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.gui.hud.HudEditor;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;

@Module(name = "HudEditor" , category = Category.CLIENT)
public class HUD extends ModuleBase {
    public static HUD INSTANCE;

    public HUD() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        if(nullCheck()) {
            disable();
            return;
        }

        mc.displayGuiScreen(new HudEditor());
    }

    @Override
    public void onTick() {
        if(!(mc.currentScreen instanceof HudEditor)) {
            disable();
        }
    }
}
