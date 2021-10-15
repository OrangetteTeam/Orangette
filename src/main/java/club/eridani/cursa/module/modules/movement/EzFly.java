package club.eridani.cursa.module.modules.movement;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;

@Module(name = "EzFly", category = Category.MOVEMENT)
public class EzFly extends ModuleBase {

    //Setting<String> mode = setting("Mode","Vanilla",listOf("Vanilla","Creative"));
    //Setting<Double> speed = setting("Speed", 0.5D, 0.1D, 1D).whenAtMode(mode, "Vanilla");

    @Override
    public void onTick() {
        mc.player.capabilities.isFlying = true;
        mc.player.capabilities.allowFlying = true;
        /*switch (mode.getValue()) {
            case "Creative": {
                mc.player.capabilities.isFlying = true;
                mc.player.capabilities.allowFlying = true;
                break;
            }
            case "Vanilla": {
                mc.player.motionY = ((mc.player.movementInput.jump ? 1 : 0) + (mc.player.movementInput.sneak ? -1 : 0)) * speed.getValue();
            }
        }*/
    }

    @Override
    public void onDisable() {
        mc.player.capabilities.isFlying = false;
        mc.player.capabilities.allowFlying = mc.player.isCreative();
        /*switch (mode.getValue()) {
            case "Creative": {
                mc.player.capabilities.isFlying = false;
                mc.player.capabilities.allowFlying = mc.player.isCreative();
                break;
            }
            case "Vanilla": {

            }
        }*/
    }

}
