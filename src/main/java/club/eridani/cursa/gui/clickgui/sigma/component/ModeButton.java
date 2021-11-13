package club.eridani.cursa.gui.clickgui.sigma.component;

import club.eridani.cursa.gui.clickgui.sigma.Component;
import club.eridani.cursa.setting.Setting;
import club.eridani.cursa.setting.settings.ModeSetting;
import club.eridani.cursa.utils.ColorUtil;
import club.eridani.cursa.utils.RenderUtil;

import java.awt.Color;

public class ModeButton extends Component {
    private ModeSetting setting;
    private float modeX, modeY, modeWidth, modeHeight;

    public ModeButton(Setting setting) {
        this.setting = (ModeSetting) setting;
        this.width = 200;
        this.height = 20;
    }

    @Override
    public int doRender(int x, int y, int mouseX, int mouseY) {
        this.x = x;
        this.y = y;
        drawString(setting.getName(), x + 10, getCenter(y, height, getFontHeight()), ColorUtil.toRGBA(0, 0, 0, 255));
        float _modeX = (this.x + this.width) - getFontWidth(setting.getValue()) - 10;
        float _modeY = getCenter(y, height, getFontHeight());
        int color = ColorUtil.toRGBA(30, 30, 30, 255);
        drawString(setting.getValue(), _modeX, _modeY, color);
        modeX = _modeX - 3;
        modeY = _modeY - 3;
        modeWidth = getFontWidth(setting.getValue()) + 6;
        modeHeight = getFontHeight() + 3;
        if (isMouseHovering(mouseX, mouseY, modeX, modeY, modeWidth, modeHeight))
            RenderUtil.drawRect(modeX, modeY, modeX + modeWidth, modeY + modeHeight, new Color(190, 190, 190, 60));
        return this.height;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int clickedMouseButton) {
        if (isMouseHovering(mouseX, mouseY, modeX, modeY, modeWidth, modeHeight) && clickedMouseButton == 0)
            setting.forwardLoop();
    }
}
