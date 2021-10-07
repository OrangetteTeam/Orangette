package club.eridani.cursa.module.modules.movement;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.common.annotations.Parallel;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;

@Parallel
@Module(name = "VanillaStep", category = Category.MOVEMENT)
public class VanillaStep extends ModuleBase {

    Setting<Integer> height = setting("Height",2,0,10);

    @Override
    public void onTick() {
        if (mc.player == null) return;
        mc.player.stepHeight = height.getValue();

    }
     }
