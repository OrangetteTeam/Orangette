package club.eridani.cursa.gui.hud.component;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.common.annotations.Parallel;
import club.eridani.cursa.event.events.render.RenderOverlayEvent;
import club.eridani.cursa.gui.hud.Hud;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@Parallel
@Module(name = "Rumia", category = Category.HUD)
public class Rumia extends Hud {

    int awa;
    private final Setting<Integer> Width = setting("Widht", 100, 5, 500);
    private final Setting<Integer> Height = setting("Heigth", 100, 5, 500);
    private final Setting<Integer> Speed = setting("Speed", 3, 1, 5);

    @Override
    public void onEnable() {
        awa = 1;
    }

    @Override
    public void onTick() {
            if (mc.player.ticksExisted % Speed.getValue() == 0) awa += 1;
            if (awa > 141) awa = 1;
        }

    @Override
    public void renderHud() {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            mc.getTextureManager().bindTexture(new ResourceLocation("orangette/" + "awa/" + awa + ".png"));
            RenderHelper.disableStandardItemLighting();
            GlStateManager.enableBlend();
            GlStateManager.disableCull();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            Gui.drawModalRectWithCustomSizedTexture(getX(), getY(), 0F, 0F, Width.getValue(), Height.getValue(), Width.getValue(), Height.getValue());
            GlStateManager.enableCull();
            GlStateManager.disableBlend();

            setSize(Width.getValue(), Height.getValue());
        }
    }
