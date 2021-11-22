package club.eridani.cursa.gui.hud.component;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.gui.hud.Hud;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.Cursa;
import club.eridani.cursa.setting.Setting;
import club.eridani.cursa.utils.RenderUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

@Module(name = "WaterMark" , category = Category.HUD)
public class WaterMark extends Hud {

    int Logo;
    int Text;
    public static final String MOD_NAME = "Orangette";
    public static final String MOD_VERSION = "0.15.1";
    Setting<String> mode = setting("mode","Logo","Logo","Text");

    @Override
    public void onEnable(){
        Logo = 0;
        Text = 1;
    }
    @Override
    public void renderHud() {
        switch (mode.getValue()){

            case "Logo":
                mc.getTextureManager().bindTexture(new ResourceLocation("orangette/logo.png"));
                RenderHelper.disableStandardItemLighting();
                GlStateManager.enableBlend();
                GlStateManager.disableCull();
                GlStateManager.color(1.0F , 1.0F , 1.0F , 1.0F);
                Gui.drawModalRectWithCustomSizedTexture(getX(), getY(), 0F, 0F, 125, 49, 125, 49);
                GlStateManager.enableCull();
                GlStateManager.disableBlend();

                setSize(125 , 49);
                break;

            case "Text":
                String str =  MOD_NAME + " " + "ver" + MOD_VERSION;
                RenderUtil.drawString(str, getX(), getY(), Color.cyan, true);
                setSize(RenderUtil.getStringWidth(str), RenderUtil.getStringHeight());
        }
    }
}
