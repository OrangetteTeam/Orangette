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
@Module(name = "Character", category = Category.HUD)
public class Character extends Hud {

    int donarudo;
    int awa;
    private final Setting<Boolean> Rumia = setting("Rumia", false);
    private final Setting<Boolean> Donarudo = setting("Donarudo", false);
    private final Setting<Integer> RumiaWidth = setting("RumiaWidht", 100, 5, 500);
    private final Setting<Integer> RumiaHeight = setting("RumiaHeigth", 100, 5, 500);
    private final Setting<Integer> DonaWidth = setting("DonaWidht", 100, 5, 500);
    private final Setting<Integer> DonaHeight = setting("DonaHeigth", 100, 5, 500);
    private final Setting<Integer> Speed = setting("Speed", 3, 1, 5);

    @Override
    public void onEnable() {
        donarudo = 0;
        awa = 1;
    }

    @Override
    public void onTick() {
        if (Rumia.getValue().equals(true)) {
            if (mc.player.ticksExisted % Speed.getValue() == 0) donarudo += 1;
            if (donarudo > 9) donarudo = 0;
        }

        if (Donarudo.getValue().equals(true)) {
            if (mc.player.ticksExisted % Speed.getValue() == 0) awa += 1;
            if (awa > 141) awa = 1;
        }
    }

    @Override
    public void renderHud() {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (Donarudo.getValue().equals(true)) {
            mc.getTextureManager().bindTexture(new ResourceLocation("orangette/" + "donarudo/" + donarudo + ".png"));
            RenderHelper.disableStandardItemLighting();
            GlStateManager.enableBlend();
            GlStateManager.disableCull();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            Gui.drawModalRectWithCustomSizedTexture(getX(), getY(), 0F, 0F, DonaWidth.getValue(), DonaHeight.getValue(), DonaWidth.getValue(), DonaHeight.getValue());
            GlStateManager.enableCull();
            GlStateManager.disableBlend();

            setSize(DonaWidth.getValue(), DonaHeight.getValue());
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (Rumia.getValue().equals(true)) {
            mc.getTextureManager().bindTexture(new ResourceLocation("orangette/" + "awa/" + awa + ".gif"));
            RenderHelper.disableStandardItemLighting();
            GlStateManager.enableBlend();
            GlStateManager.disableCull();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            Gui.drawModalRectWithCustomSizedTexture(getX(), getY(), 0F, 0F, RumiaWidth.getValue(), RumiaHeight.getValue(), RumiaWidth.getValue(), DonaHeight.getValue());
            GlStateManager.enableCull();
            GlStateManager.disableBlend();

            setSize(RumiaWidth.getValue(), RumiaHeight.getValue());

        }
    }
}