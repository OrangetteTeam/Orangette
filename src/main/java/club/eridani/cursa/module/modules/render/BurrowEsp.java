package club.eridani.cursa.module.modules.render;

import club.eridani.cursa.client.GUIManager;
import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.event.events.render.RenderEvent;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;
import club.eridani.cursa.utils.BlockUtil;
import club.eridani.cursa.utils.ColorUtil;
import club.eridani.cursa.utils.CursaTessellator;
import club.eridani.cursa.utils.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import static org.lwjgl.opengl.GL11.GL_QUADS;

@Module(name = "BurrowEsp", category = Category.RENDER)
public class BurrowEsp extends ModuleBase {
    public Setting<Integer> rL = setting("Red", 255, 0, 255);
    public Setting<Integer> gL = setting("Green", 26, 0, 255);
    public Setting<Integer> bL = setting("Blue", 42, 0, 255);
    public Setting<Integer> aL = setting("Alpha", 42, 0, 255);
    
    @Override
    public void onRenderWorld(RenderEvent event) {

        for (Entity entity : mc.world.playerEntities) {
            BlockPos pos = EntityUtil.getEntityPos(entity);
            int color = ColorUtil.toRGBA(rL.getValue(),gL.getValue(),bL.getValue(),aL.getValue());
            if (BlockUtil.getBlock(pos) != Blocks.AIR) {
                CursaTessellator.prepare(GL_QUADS);
                CursaTessellator.drawFullBox(pos, 1f, color);
                CursaTessellator.release();
            }
        }
    }

}
