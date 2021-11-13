package club.eridani.cursa.gui.clickgui.sigma.component;

import club.eridani.cursa.gui.clickgui.sigma.Component;
import club.eridani.cursa.setting.Setting;
import club.eridani.cursa.setting.settings.BooleanSetting;
import club.eridani.cursa.utils.ColorUtil;
import club.eridani.cursa.utils.RenderUtil;

public class BooleanButton extends Component {
    private BooleanSetting setting;
    private float buttonX, buttonY, buttonWidth, buttonHeight, eclipseX, targetX = 0;

    public BooleanButton(Setting setting) {
        this.setting = (BooleanSetting) setting;
        this.width = 200;
        this.height = 20;
    }

    @Override
    public int doRender(int x, int y, int mouseX, int mouseY) {
        this.x = x;
        this.y = y;
        drawString(setting.getName(), x + 10, getCenter(y, height, getFontHeight()), ColorUtil.toRGBA(0, 0, 0, 255));
        buttonX = this.x + this.width - 30;
        buttonY = getCenter(y, this.height, 10);
        buttonWidth = 22;
        buttonHeight = 10;
        RenderUtil.drawRoundedRect(buttonX, buttonY, buttonX + buttonWidth, buttonY + buttonHeight, 3, setting.getValue() ? ColorUtil.toRGBA(6, 167, 255, 255) : ColorUtil.toRGBA(200, 200, 200, 255));
        targetX = setting.getValue() ? buttonX + 6 : buttonX + buttonWidth - 6;
        if (eclipseX == 0) eclipseX = targetX;
        eclipseX += (targetX - eclipseX) * 0.3;
        RenderUtil.drawCircle(eclipseX, buttonY + 5, 4, ColorUtil.toRGBA(250, 250, 250, 255));

        return this.height;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isMouseHovering(mouseX, mouseY, buttonX, buttonY, buttonWidth, buttonHeight) && mouseButton == 0)
            setting.setValue(!setting.getValue());
    }
}
