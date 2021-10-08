package club.eridani.cursa.module.modules.player;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.common.annotations.Parallel;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;

@Parallel
@Module(name = "FastmacUse", category = Category.PLAYER)
public class FastUse extends ModuleBase {

    @Override
    public void onTick(){
        if (mc.player == null)return;
        mc.rightClickDelayTimer = 0;

    }

    @Override
    public void onDisable() {

        if (mc.player == null)return;
        mc.rightClickDelayTimer = 10;

    }
}