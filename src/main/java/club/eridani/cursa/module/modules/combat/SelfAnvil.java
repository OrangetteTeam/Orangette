package club.eridani.cursa.module.modules.combat;

import club.eridani.cursa.client.GUIManager;
import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.event.events.network.PacketEvent;
import club.eridani.cursa.event.events.render.RenderEvent;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.notification.NotificationManager;
import club.eridani.cursa.setting.Setting;
import club.eridani.cursa.utils.*;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.awt.*;

import static org.lwjgl.opengl.GL11.GL_QUADS;

@Module(name = "SelfAnvil", category = Category.COMBAT)
public class SelfAnvil extends ModuleBase {

    public Setting<Boolean> packetPlace = setting("PacketPlace", true);
    public Setting<Boolean> swing = setting("Swing", false);
    public Setting<Boolean> rotate = setting("Rotate", true);
    public Setting<Boolean> render = setting("Render", true);

    public int slot;
    private BlockPos pos;
    public float yaw, pitch;

    @Override
    public void onEnable() {
        slot = -1;
        pos = EntityUtil.getEntityPos(mc.player).add(0, 2,0);
    }

    @Override
    public void onTick() {
        InventoryUtil.push();
        if (!EntityUtil.isPlayerInHole()) {
            error("Not in the hole! disabling");
            return;
        }
        if (slot == -1) {
            slot = InventoryUtil.getBlockHotbar(Blocks.OBSIDIAN);
            if (slot == -1) {
                error("Cannot find obsidian! disabling");
                return;
            }
            slot = InventoryUtil.getBlockHotbar(Blocks.ANVIL);
            if (slot == -1) {
                error("Cannot find anvil! disabling");
                return;
            }
            float[] rotate = EntityUtil.calculateLookAt(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, mc.player);
            this.yaw = rotate[0];
            this.pitch = rotate[1];
        }

        if (getFacing(pos) == null) {
            slot = InventoryUtil.getBlockHotbar(Blocks.OBSIDIAN);
                mc.player.inventory.currentItem = slot;
                mc.playerController.updateController();
            if (swing.getValue()) mc.player.swingArm(EnumHand.MAIN_HAND);
            BlockInteractionHelper.placeBlock(pos.add(1,0,0), packetPlace.getValue());
            slot = InventoryUtil.getBlockHotbar(Blocks.OBSIDIAN);
            mc.player.inventory.currentItem = slot;
            mc.playerController.updateController();
            if (swing.getValue()) mc.player.swingArm(EnumHand.MAIN_HAND);
            BlockInteractionHelper.placeBlock(pos.add(1,-1,0), packetPlace.getValue());
        }

        slot = InventoryUtil.getBlockHotbar(Blocks.ANVIL);
        mc.player.inventory.currentItem = slot;
        mc.playerController.updateController();
        if (swing.getValue()) mc.player.swingArm(EnumHand.MAIN_HAND);
        BlockInteractionHelper.placeBlock(pos, packetPlace.getValue());

        if (BlockUtil.getBlock(pos).equals(Blocks.ANVIL)) {
            info("Done Placing! disabling");
        }
        InventoryUtil.pop();

    }

    @Override
    public void onRenderWorld(RenderEvent event) {
        int color = new Color(GUIManager.getRed(), GUIManager.getGreen(), GUIManager.getBlue(), 60).getRGB();
        if (render.getValue() && pos != null) {
            CursaTessellator.prepare(GL_QUADS);
            CursaTessellator.drawFullBox(pos, 1f, color);
            CursaTessellator.release();
        }
    }

    @Override
    public void onPacketSend(PacketEvent.Send event) {
        if (!rotate.getValue()) return;
        if (event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer packet = (CPacketPlayer) event.getPacket();
            packet.yaw = yaw;
            packet.pitch = pitch;
        }
    }

    public void info(String msg) {
        NotificationManager.info(msg);
        disable();
    }

    public void error(String msg) {
        NotificationManager.error(msg);
        disable();
    }

    public EnumFacing getFacing(BlockPos position) {
        for (EnumFacing f : EnumFacing.values()) {
            if (f.getAxis().equals(EnumFacing.Axis.Y)) continue;

            BlockPos pos = new BlockPos(position).offset(f);

            if (mc.world.isAirBlock(pos.offset(f, 0)) && mc.world.isAirBlock(pos.offset(f, 1)) && mc.world.checkNoEntityCollision(new AxisAlignedBB(pos, pos.offset(f, 2)))) {


            } else {
                for (EnumFacing fa : EnumFacing.values()) {
                    if (fa == f.rotateY().rotateY()) continue;
                    if (mc.world.isAirBlock(pos.offset(f, 1).offset(fa))) {
                        return f;
                    }
                }
            }
        }
        return null;
    }

}
