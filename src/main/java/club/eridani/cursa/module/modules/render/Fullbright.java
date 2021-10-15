package club.eridani.cursa.module.modules.render;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;

@Module(name = "Fullbright", category = Category.RENDER)
public class Fullbright extends ModuleBase {

    Setting<Integer> gamma = setting("gamma", 200, 10, 500);

    @Override
    public void onTick() {
        mc.gameSettings.gammaSetting = gamma.getValue();
    }

    @Override
    public void onDisable() {
        mc.gameSettings.gammaSetting = gamma.getValue();
    }

}

