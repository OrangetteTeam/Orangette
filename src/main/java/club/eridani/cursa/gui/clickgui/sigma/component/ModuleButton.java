package club.eridani.cursa.gui.clickgui.sigma.component;

import club.eridani.cursa.gui.clickgui.sigma.Component;
import club.eridani.cursa.gui.clickgui.sigma.SigmaGui;
import club.eridani.cursa.gui.hud.HudEditor;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.module.modules.client.HUD;
import club.eridani.cursa.utils.ColorUtil;
import club.eridani.cursa.utils.RenderUtil;

import java.awt.Color;

public class ModuleButton extends Component {
    private ModuleBase module;

    public ModuleButton(ModuleBase module) {
        this.module = module;
        this.width = 105;
        this.height = 18;
    }

    public int render(int x, int y, int mouseX, int mouseY, boolean doMouseAction) {
        this.x = x;
        this.y = y;
        if (module.isEnabled()) RenderUtil.drawRect(x, y, x + width, y + height, new Color(6, 167, 255, 255));
        drawString(module.name, x + 10, getCenter(y, height, getFontHeight()), module.isEnabled() ? ColorUtil.toRGBA(255, 255, 255, 255) : ColorUtil.toRGBA(0, 0, 0, 255));
        if (isMouseHovering(mouseX, mouseY) && doMouseAction)
            RenderUtil.drawRect(x, y, x + width, y + height, new Color(190, 190, 190, 60));
        return height;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isMouseHovering(mouseX, mouseY)) {
            if (mouseButton == 0) module.toggle();
            if (mouseButton == 1) {
                if(HUD.INSTANCE.isEnabled()) {
                    HudEditor.INSTANCE.setSettingModule(module);
                    HudEditor.INSTANCE.toggleSettingWindowVisible();
                }
                else {
                    SigmaGui.INSTANCE.setSettingModule(module);
                    SigmaGui.INSTANCE.toggleSettingWindowVisible();
                }
            }
        }
    }
}
