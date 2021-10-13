package club.eridani.cursa.module.modules.movement;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.common.annotations.Parallel;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;

@Parallel
@Module(name = "ReverseStep", category = Category.MOVEMENT)
public class ReverseStep extends ModuleBase{

    public Setting<Double> Height = setting("Height" , 6.0 , 1.0 , 15.0);

    public void onUpdate() {
        if (nullCheck()) return;
        try {
        } catch (Exception ignored) {
            return;
        }

        if (mc.player.onGround) {
            for (double y = 0.0; y < this.Height.getValue() + 0.5; y += 0.01) {
                if (!mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, -y, 0.0)).isEmpty()) {
                    mc.player.motionY = -10.0;
                    break;
                }
            }
        }
    }
}


