package club.eridani.cursa.module.modules.client;

import club.eridani.cursa.client.ConfigManager;
import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.common.annotations.Parallel;
import club.eridani.cursa.gui.CursaClickGUI;
import club.eridani.cursa.gui.sigma.SigmaGUI;
import club.eridani.cursa.gui.vapelite.VapeLiteClickGUI;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;
import org.lwjgl.input.Keyboard;

@Parallel
@Module(name = "ClickGUI", category = Category.CLIENT, keyCode = Keyboard.KEY_O)
public class ClickGUI extends ModuleBase {
    public Setting<String> gui = setting("Type" , "Sigma" , "Cursa" , "Vape" , "Sigma");

    public static ClickGUI instance;

    public ClickGUI() {
        instance = this;
    }

    @Override
    public void onEnable() {
        if (nullCheck()) return;

        if (gui.getValue().equals("Cursa") && !(mc.currentScreen instanceof CursaClickGUI))
            mc.displayGuiScreen(new CursaClickGUI());
        if (gui.getValue().equals("Vape") && !(mc.currentScreen instanceof VapeLiteClickGUI))
            mc.displayGuiScreen(new VapeLiteClickGUI());
        if (gui.getValue().equals("Sigma") && !(mc.currentScreen instanceof SigmaGUI))
            mc.displayGuiScreen(new SigmaGUI());
    }

    @Override
    public void onDisable() {
        if (mc.currentScreen != null && mc.currentScreen instanceof CursaClickGUI) {
            mc.displayGuiScreen(null);
        }
        ConfigManager.saveAll();
    }


}
