package club.eridani.cursa.module.modules.movement;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.common.annotations.Parallel;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;


@Parallel
@Module(name = "AutoSpeedWalk", category = Category.MOVEMENT)
public class AutoSpeedWalk extends ModuleBase {

    public Setting<Double> speed = setting("Speed" , 2.0 , 1.0 , 15.0);

    @Override
    public void onTick() {
        if (mc.player == null) return;
        mc.player.motionX = Math.cos(Math.toRadians(mc.player.rotationYaw + 90)) * speed.getValue();
        mc.player.motionZ = Math.sin(Math.toRadians(mc.player.rotationYaw + 90)) * speed.getValue();
    }}