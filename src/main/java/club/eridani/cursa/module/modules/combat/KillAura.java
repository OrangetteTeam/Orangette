package club.eridani.cursa.module.modules.combat;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.event.events.network.PacketEvent;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;
import club.eridani.cursa.utils.RotationUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;

@Module(name = "KillAura", category = Category.COMBAT)
public class KillAura extends ModuleBase {

    public Setting<Double> range = setting("Range", 6.0, 0.1, 10);

    private float[] rotations;

    @Override
    public void onTick() {
        EntityLivingBase target = (EntityLivingBase) mc.world.loadedEntityList.stream().filter((entity) ->
                entity instanceof EntityLivingBase &&
                        entity.isEntityAlive() &&
                        entity.getDistance(mc.player) <= range.getValue() &&
                        entity != mc.player
        ).findFirst().orElse(null);

        if (target != null) {
            rotations = RotationUtil.getRotationsAAC(target);
            if (mc.player.getCooledAttackStrength(0.0f) > 0.9f) {
                mc.getConnection().sendPacket(new CPacketUseEntity(target));
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.resetCooldown();
            }
        }
    }

    @Override
    public void onPacketSend(PacketEvent.Send event) {
        if(rotations != null && event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer player = (CPacketPlayer) event.getPacket();
            player.yaw = rotations[0];
            player.pitch = rotations[1];
            RotationUtil.lastRotations[0] = player.yaw;
            RotationUtil.lastRotations[1] = player.pitch;
            rotations = null;
        }
    }
}