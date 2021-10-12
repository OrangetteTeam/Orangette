package club.eridani.cursa.module.modules.render;

import club.eridani.cursa.client.GUIManager;
import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.common.annotations.Parallel;
import club.eridani.cursa.event.events.render.RenderEvent;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.utils.BlockUtil;
import club.eridani.cursa.utils.ColorUtil;
import club.eridani.cursa.utils.CursaTessellator;
import club.eridani.cursa.utils.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import static org.lwjgl.opengl.GL11.GL_QUADS;

@Parallel
@Module(name = "BurrowEsp", category = Category.RENDER)
public class BurrowEsp extends ModuleBase {

    @Override
    public void onRenderWorld(RenderEvent event) {

        for (Entity entity:mc.world.playerEntities){
        BlockPos pos = EntityUtil.getEntityPos(entity);

            int color = ColorUtil.toRGBA(GUIManager.getRed(), GUIManager.getGreen(), GUIManager.getBlue() ,GUIManager.getAlpha());


            if (BlockUtil.getBlock(pos)!=Blocks.AIR){

                CursaTessellator.prepare(GL_QUADS);
                CursaTessellator.drawFullBox(pos, 1f, color);
                CursaTessellator.release();

        }
             }
                 }
                    }
