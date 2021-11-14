package club.eridani.cursa.module.modules.player;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.concurrent.event.Listener;
import club.eridani.cursa.event.events.player.PlayerDamageBlockEvent;
import club.eridani.cursa.event.events.render.RenderEvent;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;
import club.eridani.cursa.utils.*;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.GL_QUADS;

@Module(name = "SpeedMine" , category = Category.PLAYER)
public class SpeedMine extends ModuleBase {
    private final Timer breakSuccess = new Timer();

    private final Setting<Boolean> creativeMode = setting("CreativeMode", true);
    private final Setting<Boolean> ghostHand = setting("GhostHand", true);
    private final Setting<Boolean> render = setting("Render", true);
    private final Setting<Integer> r = setting("Red" , 255 , 0 , 255);
    private final Setting<Integer> g = setting("Green" , 0 , 0 , 255);
    private final Setting<Integer> b = setting("Blue" , 0 , 0 , 255);
    private final Setting<Integer> a = setting("Alpha" , 60 , 0 , 255);

    private final List<Block> godBlocks = Arrays.asList(Blocks.AIR, Blocks.FLOWING_LAVA, Blocks.LAVA, Blocks.FLOWING_WATER, Blocks.WATER, Blocks.BEDROCK);
    private boolean cancelStart = false;
    private EnumFacing facing;
    public BlockPos breakPos;

    @Override
    public void onTick() {
        if (nullCheck())
            return;
        if (this.creativeMode.getValue() && this.cancelStart && !this.godBlocks.contains(mc.world.getBlockState(this.breakPos).getBlock()))
            if (this.ghostHand.getValue() && InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE) != -1) {
                int slotMain = mc.player.inventory.currentItem;
                if (mc.world.getBlockState(this.breakPos).getBlock() == Blocks.OBSIDIAN) {
                    if (this.breakSuccess.passed(1234L)) {
                        mc.player.connection.sendPacket(new CPacketHeldItemChange(InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE)));
                        mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.breakPos, this.facing));
                        mc.player.connection.sendPacket(new CPacketHeldItemChange(slotMain));
                    }
                } else {
                    mc.player.inventory.currentItem = InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE);
                    mc.playerController.updateController();
                    mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.breakPos, this.facing));
                    mc.player.inventory.currentItem = slotMain;
                    mc.playerController.updateController();
                }
            } else {
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.breakPos, this.facing));
            }
    }

    @Override
    public void onRenderWorld(RenderEvent event) {
        if (render.getValue() && !Objects.isNull(breakPos)) {
            CursaTessellator.prepare(GL_QUADS);
            CursaTessellator.drawFullBox(breakPos, 1f, ColorUtil.toARGB(r.getValue() , g.getValue() , b.getValue() , a.getValue()));
            CursaTessellator.release();
        }
    }

    @Listener
    public void onBlockEvent(PlayerDamageBlockEvent event) {
        if (nullCheck())
            return;
        if (BlockUtil.canBreak(event.pos)) {
            this.cancelStart = false;
            this.breakPos = event.pos;
            this.breakSuccess.reset();
            this.facing = event.facing;
            if (this.breakPos != null) {
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, this.breakPos, this.facing));
                this.cancelStart = true;
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.breakPos, this.facing));
                event.cancel();
            }
        }
    }
}
