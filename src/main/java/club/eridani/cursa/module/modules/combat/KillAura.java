package club.eridani.cursa.module.modules.combat;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.common.annotations.Parallel;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;

@Parallel
@Module(name = "KillAura", category = Category.COMBAT)
public class KillAura extends ModuleBase {

    public Setting<Double> range = setting("Range", 6.0, 0.1, 10);
    @Override
    public void onTick() {
        EntityLivingBase target = (EntityLivingBase) mc.world.loadedEntityList.stream().filter((entity) ->
                entity instanceof EntityLivingBase &&
                entity.isEntityAlive() &&
                entity.getDistance(mc.player) <= range.getValue() &&
                entity != mc.player
                ).findFirst().orElse(null);

        if (target != null) {
            if (mc.player.getCooledAttackStrength(0.0f)> 0.9f) {
                mc.getConnection().sendPacket(new CPacketUseEntity(target));
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.resetCooldown();
            }
        }
    }

}