package club.eridani.cursa.gui.clickgui.sigma.component;

import club.eridani.cursa.gui.clickgui.sigma.Component;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.utils.ColorUtil;
import club.eridani.cursa.utils.RenderUtil;
import org.lwjgl.input.Keyboard;

import java.awt.Color;

public class BindButton extends Component {
    private ModuleBase module;
    private float keyX, keyY, keyWidth, keyHeight;
    private boolean keyWaiting;

    public BindButton(ModuleBase module) {
        this.module = module;
        this.width = 200;
        this.height = 20;
    }

    @Override
    public int doRender(int x, int y, int mouseX, int mouseY) {
        this.x = x;
        this.y = y;
        drawString("Bind", x + 10, getCenter(y, height, getFontHeight()), ColorUtil.toRGBA(0, 0, 0, 255));
        String key = keyWaiting ? "..." : (module.keyCode == 0x00 ? "NONE" : Keyboard.getKeyName(module.keyCode));
        float _modeX = (this.x + this.width) - getFontWidth(key) - 10;
        float _modeY = getCenter(y, height, getFontHeight());
        int color = ColorUtil.toRGBA(30, 30, 30, 255);
        drawString(key, _modeX, _modeY, color);
        keyX = _modeX - 3;
        keyY = _modeY - 3;
        keyWidth = getFontWidth(key) + 6;
        keyHeight = getFontHeight() + 3;
        if (isMouseHovering(mouseX, mouseY, keyX, keyY, keyWidth, keyHeight))
            RenderUtil.drawRect(keyX, keyY, keyX + keyWidth, keyY + keyHeight, new Color(190, 190, 190, 60));
        return this.height;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isMouseHovering(mouseX, mouseY, keyX, keyY, keyWidth, keyHeight))
            keyWaiting = !keyWaiting;
    }

    @Override
    public void keyTyped(char typedChar, int key) {
        if (!keyWaiting) return;
        if (key == Keyboard.KEY_BACK) module.keyCode = Keyboard.KEY_NONE;
        else module.keyCode = key;
        keyWaiting = false;
    }
}
