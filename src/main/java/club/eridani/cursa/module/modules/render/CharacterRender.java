package club.eridani.cursa.module.modules.render;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.common.annotations.Parallel;
import club.eridani.cursa.event.events.render.RenderOverlayEvent;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;
import club.eridani.cursa.utils.ChatUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@Parallel
@Module(name = "CharacterRender", category = Category.RENDER)
public class CharacterRender extends ModuleBase {

    int donarudo;
    int awa;
    private final Setting<String> mode = setting("AnimationMode", "donarudo", "donarudo", "awa", "ossan");
    private final Setting<Integer> x = setting("X", 0, 0, 1500);
    private final Setting<Integer> y = setting("Y", 0, 0, 1000);
    private final Setting<Integer> Width = setting("Widht", 100, 5, 500);
    private final Setting<Integer> Height = setting("Heigth", 100, 5, 500);
    private final Setting<Integer> Speed = setting("Speed", 3, 1, 5);

    @Override
    public void onEnable() {
        donarudo = 0;
        awa = 1;
    }

    @Override
    public void onTick() {
        switch (mode.getValue()) {
            case "donarudo":
                if (mc.player.ticksExisted % Speed.getValue() == 0) donarudo += 1;
                if (donarudo > 9) donarudo = 0;
                break;
            case "awa":
                if (mc.player.ticksExisted % Speed.getValue() == 0) awa += 1;
                if (awa > 141) awa = 1;
                break;
        }
    }

    @Override
    public void onRender(RenderOverlayEvent event) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        switch (mode.getValue()) {
            case "donarudo":
                mc.getTextureManager().bindTexture(new ResourceLocation("orangette/" + "donarudo/" + donarudo + ".png"));
                Gui.drawModalRectWithCustomSizedTexture(x.getValue(), y.getValue(), 0F, 0F, Width.getValue(), Height.getValue(), Width.getValue(), Height.getValue());
                break;
            case "awa":
                mc.getTextureManager().bindTexture(new ResourceLocation("orangette/" + "awa/" + awa + ".gif"));
                Gui.drawModalRectWithCustomSizedTexture(x.getValue(), y.getValue(), 0F, 0F, Width.getValue(), Height.getValue(), Width.getValue(), Height.getValue());
                break;
        }
    }
}