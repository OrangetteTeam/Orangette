package club.eridani.cursa.module.modules.render;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.common.annotations.Parallel;
import club.eridani.cursa.event.events.render.RenderOverlayEvent;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

@Parallel
@Module(name = "CharacterRender", category = Category.RENDER)
public class CharacterRender extends ModuleBase {

    int gif;
    private final Setting<String> mode = setting("AnimationMode", "donarudo", "donarudo", "phymon", "ossan");
    private final Setting<Integer> x = setting("X", 0, 0, 1500);
    private final Setting<Integer> y = setting("Y", 0, 0, 1000);
    private final Setting<Integer> Width = setting("Widht", 100, 5, 500);
    private final Setting<Integer> Height = setting("Heigth", 100, 5, 500);
    private final Setting<Integer> Speed = setting("Speed", 3, 1, 5);

    @Override
    public void onRender(RenderOverlayEvent event) {
        {
            {
                if (mc.player.ticksExisted % Speed.getValue() == 0) gif += 1;
                if (gif > 9) {
                    gif = 0;

                }

                mc.getTextureManager().bindTexture(new ResourceLocation("orangette/" + "donarudo/" + gif + ".png"));
                Gui.drawModalRectWithCustomSizedTexture(x.getValue(), y.getValue(), 0F, 0F, Width.getValue(), Height.getValue(), Width.getValue(), Height.getValue());

            }
        }
    }
}