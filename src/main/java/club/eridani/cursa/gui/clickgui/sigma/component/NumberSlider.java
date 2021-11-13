package club.eridani.cursa.gui.clickgui.sigma.component;

import club.eridani.cursa.gui.clickgui.sigma.Component;
import club.eridani.cursa.setting.NumberSetting;
import club.eridani.cursa.setting.Setting;
import club.eridani.cursa.setting.settings.DoubleSetting;
import club.eridani.cursa.setting.settings.FloatSetting;
import club.eridani.cursa.setting.settings.IntSetting;
import club.eridani.cursa.utils.ColorUtil;
import club.eridani.cursa.utils.RenderUtil;
import net.minecraft.util.math.MathHelper;

public class NumberSlider extends Component {
    private NumberSetting<?> setting;
    private float sliderX, sliderY, sliderWidth, sliderHeight, eclipseX, eclipseY, eclipseScale;
    private boolean sliding;

    public NumberSlider(Setting setting) {
        this.setting = (NumberSetting<?>) setting;
        this.width = 200;
        this.height = 20;
        this.eclipseScale = 3;
    }

    @Override
    public int doRender(int x, int y, int mouseX, int mouseY) {
        this.x = x;
        this.y = y;
        drawString(setting.getName(), x + 10, getCenter(y, height, getFontHeight()), ColorUtil.toRGBA(0, 0, 0, 255));
        float w = getFontWidth(String.format("%.1f", setting.getValue().doubleValue()));
        drawString(String.format("%.1f", setting.getValue().doubleValue()), x + width - 10 - w, getCenter(y, height, getFontHeight()), ColorUtil.toRGBA(0, 0, 0, 255));
        sliderWidth = 50;
        sliderHeight = 2;
        sliderX = x + width - sliderWidth - 40;
        sliderY = getCenter(y, height, sliderHeight) - 1;
        RenderUtil.drawRect(sliderX, sliderY, sliderX + sliderWidth, sliderY + sliderHeight, ColorUtil.toRGBA(215, 226, 235, 255));
        double percentBar = (setting.getValue().doubleValue() - setting.getMin().doubleValue()) / (setting.getMax().doubleValue() - setting.getMin().doubleValue());
        double tempWidth = (sliderWidth) * percentBar;
        RenderUtil.drawRect(sliderX, sliderY, (float) (sliderX + tempWidth), sliderY + sliderHeight, ColorUtil.toRGBA(6, 167, 255, 255));
        eclipseX = (float) (sliderX + tempWidth + eclipseScale * 0.5F);
        eclipseY = getCenter(sliderY, sliderHeight, eclipseScale * 0.5F) + 1;
        float shaderScale = eclipseScale + 0.5F;
        RenderUtil.drawCircle((float) (sliderX + tempWidth + shaderScale * 0.5F), getCenter(sliderY, sliderHeight, shaderScale * 0.5F) + 1, shaderScale, ColorUtil.toRGBA(0, 0, 0, 40));
        RenderUtil.drawCircle(eclipseX, eclipseY, eclipseScale, ColorUtil.toRGBA(244, 244, 243, 255));
        eclipseScale = isMouseHovering(mouseX, mouseY, eclipseX, eclipseY, eclipseScale, eclipseScale, 3) ? 3.5F : 3F;
        if (this.sliding) {
            double diff = setting.getMax().doubleValue() - setting.getMin().doubleValue();
            double val = setting.getMin().doubleValue() + (MathHelper.clamp((mouseX - (double) (sliderX + 1)) / (double) (sliderWidth - 2), 0, 1)) * diff;
            if (setting instanceof DoubleSetting) ((DoubleSetting) setting).setValue(val);
            else if (setting instanceof FloatSetting) ((FloatSetting) setting).setValue((float) val);
            else if (setting instanceof IntSetting) ((IntSetting) setting).setValue((int) val);
        }
        return height;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && (isMouseHovering(mouseX, mouseY, sliderX, sliderY, sliderWidth, sliderHeight, 3) || isMouseHovering(mouseX, mouseY, eclipseX, eclipseY, eclipseScale, eclipseScale, 3)))
            this.sliding = true;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        sliding = false;
    }
}
