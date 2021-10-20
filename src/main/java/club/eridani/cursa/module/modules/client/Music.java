package club.eridani.cursa.module.modules.client;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.gui.musicplayer.MusicGUI;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;

@Module(name = "MusicPlayer" , category = Category.CLIENT)
public class Music extends ModuleBase {
    @Override
    public void onEnable() {
        if(nullCheck()) return;
        if(!(mc.currentScreen instanceof MusicGUI))
            mc.displayGuiScreen(new MusicGUI());
    }
}
