package club.eridani.cursa.module.modules.client;

import club.eridani.cursa.client.ConfigManager;
import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.common.annotations.Parallel;
import club.eridani.cursa.gui.CursaClickGUI;
import club.eridani.cursa.gui.vapelite.VapeLiteClickGUI;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import org.lwjgl.input.Keyboard;

@Parallel
@Module(name = "ClickGUI", category = Category.CLIENT, keyCode = Keyboard.KEY_O)
public class ClickGUI extends ModuleBase {

    public static ClickGUI instance;

    public ClickGUI() {
        instance = this;
    }

    @Override
    public void onEnable() {
        if (mc.player != null) {
            if (!(mc.currentScreen instanceof CursaClickGUI)) {
                mc.displayGuiScreen(new CursaClickGUI());
            }
        }
    }

    @Override
    public void onDisable() {
        if (mc.currentScreen != null && mc.currentScreen instanceof CursaClickGUI) {
            mc.displayGuiScreen(null);
        }
        ConfigManager.saveAll();
    }

}
