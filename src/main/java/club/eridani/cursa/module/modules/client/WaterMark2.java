package club.eridani.cursa.module.modules.client;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.common.annotations.Parallel;
import club.eridani.cursa.event.events.render.RenderOverlayEvent;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

@Parallel
@Module(name = "WaterMark2", category = Category.CLIENT)
public class WaterMark2 extends ModuleBase {

    private final Setting<Integer> x = setting("X", 0, 0, 1500);
    private final Setting<Integer> y = setting("Y", 0, 0, 1000);


    @Override
    public void onRender(RenderOverlayEvent event){

        mc.getTextureManager().bindTexture(new ResourceLocation("orangette/logo.png"));
        Gui.drawModalRectWithCustomSizedTexture(x.getValue(), y.getValue(),0F,0F,125,49,125,49);

    }

}



