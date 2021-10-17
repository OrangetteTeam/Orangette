package club.eridani.cursa.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotationUtil {

    public static Minecraft mc = Minecraft.getMinecraft();
    public static float[] lastRotations = new float[2];

    public static float[] getRotations(Vec3d from, Vec3d to) {
        double difX = to.x - from.x;
        double difY = (to.y - from.y) * -1.0;
        double difZ = to.z - from.z;
        double dist = MathHelper.sqrt((difX * difX + difZ * difZ));
        return new float[]{(float) MathHelper.wrapDegrees((Math.toDegrees(Math.atan2(difZ, difX)) - 90.0)), (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difY, dist)))};
    }

    public static float[] getRotationsAAC(EntityLivingBase entity) {
        double d1 = Math.sqrt(mc.player.getDistance(entity) * mc.player.getDistance(entity)) / 1.5D;
        double d2 = entity.posX + (entity.posX - entity.prevPosX) * d1 * Math.random() - mc.player.posX +mc.player.motionX*Math.random();
        double d3 = entity.posZ + (entity.posZ - entity.prevPosZ) * d1 * Math.random()  - mc.player.posZ +mc.player.motionZ*Math.random();
        float oldPitch = 0;
        for (double ySearch = 0.15D; ySearch < 1D; ySearch += 0.1D) {
            float currentPitch = (float) -Math.toDegrees(Math.atan2((entity.boundingBox.minY + (entity.boundingBox.maxY - entity.boundingBox.minY) * ySearch)-mc.player.boundingBox.minY-mc.player.getEyeHeight(), Math.hypot(d2, d3)));
            if (oldPitch == 0 || getRotationDifference(new float[] {0, currentPitch}) < getRotationDifference(new float[] {0, oldPitch})) {
                oldPitch = currentPitch;
            }
        }
        return new float[]{(float) Math.toDegrees(Math.atan2(d3, d2)) - 90.0F, oldPitch};
    }

    public static double getRotationDifference(float[] rotation) {
        return getRotationDifference(rotation, lastRotations);
    }
    public static double getRotationDifference(float[] a, float[] b) {
        return Math.hypot(getAngleDifference(a[0], b[0]), a[1] - b[1]);
    }

    private static float getAngleDifference(final float a, final float b) {
        return ((((a - b) % 360F) + 540F) % 360F) - 180F;
    }

    public static float[] getRotationsBlock(BlockPos block, EnumFacing face, boolean Legit) {
        double x = block.getX() + 0.5 - mc.player.posX + (double) face.getXOffset() / 2;
        double z = block.getZ() + 0.5 - mc.player.posZ + (double) face.getZOffset() / 2;
        double y = (block.getY() + 0.5);

        if (Legit)
            y += 0.5;

        double d1 = mc.player.posY + mc.player.getEyeHeight() - y;
        double d3 = MathHelper.sqrt(x * x + z * z);
        float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) (Math.atan2(d1, d3) * 180.0D / Math.PI);

        if (yaw < 0.0F) {
            yaw += 360f;
        }
        return new float[]{yaw, pitch};
    }

}
