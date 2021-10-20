package club.eridani.cursa.module.modules.render;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;
import net.minecraft.client.renderer.entity.RenderPlayer;

@Module(name = "RattedSkin" , category = Category.RENDER)
public class RattedSkin extends ModuleBase {
    public static RattedSkin INSTANCE;

    public Setting<String> skin = setting("Skin" ,  "Natsumir_EN_82" , "Natsumir_EN_82"  , "YOSSHILOL");

    public RattedSkin(){
        INSTANCE = this;
    }
}
