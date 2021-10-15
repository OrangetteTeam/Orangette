package club.eridani.cursa.module.modules.render;

import club.eridani.cursa.client.GUIManager;
import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.event.events.render.RenderEvent;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.utils.ColorUtil;
import club.eridani.cursa.utils.CursaTessellator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import static org.lwjgl.opengl.GL11.GL_QUADS;

@Module(name = "BlockHighlight", category = Category.RENDER)
public class BlockHighlight extends ModuleBase {

    @Override

    public void onRenderWorld(RenderEvent event) {

        BlockPos pos = mc.objectMouseOver.getBlockPos();
        int color = ColorUtil.toRGBA(GUIManager.getRed(), GUIManager.getGreen(), GUIManager.getBlue(), 60);
        if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK && pos != null) {

            CursaTessellator.prepare(GL_QUADS);
            CursaTessellator.drawFullBox(pos, 1f, color);
            CursaTessellator.release();

        }
    }
}
