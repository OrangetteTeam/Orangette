package club.eridani.cursa.module.modules.movement;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;
import club.eridani.cursa.utils.EntityUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

@Module(name = "Elevator", category = Category.MOVEMENT)
public class Elevator extends ModuleBase {

    public Setting<Double> height = setting("Height", 3.0, -10.0, 10.0);

    BlockPos pos;

    @Override
    public void onEnable() {
        pos = EntityUtil.getEntityPos(mc.player);
    }

    @Override
    public void onTick() {
        mc.player.setPosition(pos.getX() + .5, pos.getY() + height.getValue(), pos.getZ() + .5);
        disable();
    }
}