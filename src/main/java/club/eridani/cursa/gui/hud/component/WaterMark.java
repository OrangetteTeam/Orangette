package club.eridani.cursa.gui.hud.component;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.gui.hud.Hud;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.utils.RenderUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@Module(name = "WaterMark" , category = Category.HUD)
public class WaterMark extends Hud {
    public void renderHud() {
        mc.getTextureManager().bindTexture(new ResourceLocation("orangette/logo.png"));
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableBlend();
        GlStateManager.disableCull();
        GlStateManager.color(1.0F , 1.0F , 1.0F , 1.0F);
        Gui.drawModalRectWithCustomSizedTexture(getX(), getY(), 0F, 0F, 125, 49, 125, 49);
        GlStateManager.enableCull();
        GlStateManager.disableBlend();

        setSize(125 , 49);
    }
}
