package club.eridani.cursa.module.modules.movement;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.concurrent.event.Listener;
import club.eridani.cursa.concurrent.event.Priority;
import club.eridani.cursa.event.events.network.PacketEvent;
import club.eridani.cursa.event.events.render.RenderModelEvent;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;
import club.eridani.cursa.utils.EntityUtil;
import net.minecraft.block.Block;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import static net.minecraft.util.EnumFacing.*;

@Module(name = "Scaffold", category = Category.MOVEMENT)
public class Scaffold extends ModuleBase {

    float yaw, pitch;
    private BlockData data;
    Setting<Boolean> down = setting("downward", true);

    @Override
    public void onEnable() {
        yaw = mc.player.rotationYaw;
        pitch = mc.player.rotationPitch;
    }

    @Override
    public void onPacketSend(PacketEvent.Send event) {
        if (event.packet instanceof CPacketPlayer) {
            CPacketPlayer packet = (CPacketPlayer) event.packet;
            packet.rotating = packet.yaw != packet.yaw || packet.pitch != packet.pitch;
            packet.yaw = yaw;
            packet.pitch = pitch;
        }
    }

    @Override
    public void onTick() {
        int slot = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if (itemStack.getItem() instanceof ItemBlock) {
                if (itemStack.getCount() > 0) {
                    slot = i;
                }
            }
        }
        if (slot == -1) return;
        data = null;
        boolean dow = false;
        if (down.getValue()) {
            KeyBinding.updateKeyBindState();
            dow = mc.gameSettings.keyBindSneak.pressed;
            mc.gameSettings.keyBindSneak.pressed = false;
            mc.player.setSneaking(false);
        }
        float reach = mc.playerController.getBlockReachDistance() + MathHelper.SQRT_2;
        BlockPos player = new BlockPos(mc.player);
        BlockPos target = new BlockPos(mc.player.getPositionVector()).down(dow ? 2 : 1);
        double d = reach;
        EnumFacing faa = null;
        if (mc.world.isAirBlock(target)) {
            for (float x = -mc.playerController.getBlockReachDistance(); x < reach; x++) {
                for (float y = -mc.playerController.getBlockReachDistance(); y < reach; y++) {
                    for (float z = -mc.playerController.getBlockReachDistance(); z < reach; z++) {
                        BlockPos pos = player.add(x, y, z);
                        double dist = target.distanceSq(pos);
                        if (dist >= d) continue;
                        if (!mc.world.isAirBlock(pos)) continue;
                        if (mc.world.collidesWithAnyBlock(Block.FULL_BLOCK_AABB.offset(pos))) continue;
                        boolean aa = true;
                        for (EnumFacing f : EnumFacing.values()) {
                            if (!mc.world.isAirBlock(pos.offset(f))) {
                                aa = false;
                                faa = f;
                                break;
                            }
                        }
                        if (aa) continue;
                        d = dist;
                        data = new BlockData(pos.offset(faa), reverse(faa));
                    }
                }
            }
        }

        if (data != null) {
            float[] a = EntityUtil.calculateLookAt(new Vec3d(data.pos).add(.5, .5, .5).add(new Vec3d(data.face.getDirectionVec()).scale(.5D)), mc.player);
            yaw = a[0];
            pitch = a[1];
            int l = mc.player.inventory.currentItem;
            mc.player.inventory.currentItem = slot;
            mc.playerController.processRightClickBlock(mc.player, mc.world, data.pos, data.face, Vec3d.ZERO, EnumHand.MAIN_HAND);
            mc.player.inventory.currentItem = l;
            mc.getConnection().sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
        }
        mc.player.setRotationYawHead(yaw);
    }

    public EnumFacing reverse(EnumFacing facing) {
        switch (facing) {
            case DOWN:
                return UP;

            case UP:
                return DOWN;

            case NORTH:
                return SOUTH;

            case EAST:
                return WEST;

            case SOUTH:
                return NORTH;

            case WEST:
                return EAST;

            default:
                throw new IllegalStateException("Unable to get CCW facing of " + this);
        }
    }

    public static class BlockData {
        public final BlockPos pos;
        public final EnumFacing face;

        private BlockData(BlockPos pos, EnumFacing face) {
            this.pos = pos;
            this.face = face;
        }
    }

    @Listener(priority = Priority.PARALLEL)
    public void renderModelRotation(RenderModelEvent event) {
        event.rotating = true;
        event.pitch = pitch;
    }
}
