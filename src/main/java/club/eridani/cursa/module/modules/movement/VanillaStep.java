package club.eridani.cursa.module.modules.movement;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.common.annotations.Parallel;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;

@Parallel
@Module(name = "VanillaStep", category = Category.MOVEMENT)
public class VanillaStep extends ModuleBase {

    Setting<Double> height = setting("Height",2D,0D,10D);

    @Override
    public void onTick() {
        if (mc.player == null) return;
        mc.player.stepHeight = height.getValue().floatValue();
    }

    @Override
    public void onDisable() {
        mc.player.stepHeight = 0.5F;
    }

}
