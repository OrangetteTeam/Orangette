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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.GL_QUADS;

@Module(name = "HoleMiner", category = Category.COMBAT)
public class AutoCity extends ModuleBase {
    public Setting<Double> range = setting("Range", 6.0, 1.0, 15.0);
    public Setting<Boolean> swing = setting("Swing", false);
    public Setting<Boolean> rotate = setting("Rotate", true);
    public Setting<Boolean> render = setting("Render", true);

    private int slot;
    private BlockPos pos;
    public float yaw, pitch;

    @Override
    public void onEnable() {
        slot = -1;
        pos = null;
    }

    @Override
    public void onTick() {
        InventoryUtil.push();
        if (slot == -1 || Objects.isNull(pos)) {
            slot = InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE);
            if (slot == -1) {
                error("Cannot find pickaxe! disabling");
                return;
            }
            pos = getCityPos();
            if (Objects.isNull(pos)) {
                error("Cannot find city! disabling");
                return;
            }
            float[] rotate = EntityUtil.calculateLookAt(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, mc.player);
            this.yaw = rotate[0];
            this.pitch = rotate[1];
        }

        mc.player.inventory.currentItem = slot;
        mc.playerController.updateController();
        if (swing.getValue()) mc.player.swingArm(EnumHand.MAIN_HAND);
        mc.playerController.onPlayerDamageBlock(pos, EnumFacing.UP);

        if (BlockUtil.getBlock(pos).equals(Blocks.AIR)) {
            info("Done breaking! disabling");
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

    public BlockPos getCityPos() {
        EntityPlayer player = EntityUtil.getNearestPlayer();
        if (Objects.isNull(player)) return null;

        BlockPos[] offsets = new BlockPos[]{
                new BlockPos(1, 0, 0),
                new BlockPos(-1, 0, 0),
                new BlockPos(0, 0, 1),
                new BlockPos(0, 0, -1)
        };
        List<CityPos> cityPositions = new ArrayList<>();
        BlockPos playerPos = EntityUtil.getEntityPos(player);
        for (BlockPos offset : offsets) {
            BlockPos pos = playerPos.add(offset);
            if (PlayerUtil.getDistance(pos) > range.getValue()) continue;
            if (!BlockUtil.getBlock(pos).equals(Blocks.OBSIDIAN)) continue;
            int level = 0;
            BlockPos a = pos.add(offset);
            if (BlockUtil.getBlock(a).equals(Blocks.AIR)) level = 2;
            else if (BlockUtil.getBlock(a.add(offset)).equals(Blocks.AIR)) level = 1;
            cityPositions.add(new CityPos(pos, level));
        }
        CityPos finallyPos = cityPositions
                .stream().max(Comparator.comparing(e -> e.level)).orElse(null);
        if (Objects.isNull(finallyPos)) return null;
        return finallyPos.pos;
    }

    public void info(String msg) {
        NotificationManager.info(msg);
        disable();
    }

    public void error(String msg) {
        NotificationManager.error(msg);
        disable();
    }

    public class CityPos {
        private BlockPos pos;
        private int level;

        public CityPos(BlockPos pos, int level) {
            this.pos = pos;
            this.level = level;
        }

        public BlockPos getPos() {
            return this.pos;
        }

        public int getLevel() {
            return this.level;
        }
    }
}
