package club.eridani.cursa.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class PlayerUtil {
    public static Minecraft mc = Minecraft.getMinecraft();

    public static double getDistance(Entity e) {
        return mc.player.getDistance(e);
    }

    public static double getDistance(BlockPos pos) {
        return mc.player.getDistance(pos.getX(), pos.getY(), pos.getZ());
    }

    public static double getDistance(Vec3d pos) {
        return mc.player.getDistance(pos.x, pos.y, pos.z);
    }
}
