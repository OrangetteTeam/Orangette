package club.eridani.cursa.module.modules.render;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;

@Module(name = "MickeyMouse" , category = Category.RENDER)
public class MickeyMouse extends ModuleBase {
    public static MickeyMouse INSTANCE;
    public MickeyMouse(){
        INSTANCE = this;
    }
}
