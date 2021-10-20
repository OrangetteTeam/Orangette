package club.eridani.cursa.gui.musicplayer;

import club.eridani.cursa.gui.sigma.Component;
import club.eridani.cursa.utils.ColorUtil;
import club.eridani.cursa.utils.RenderUtil;
import net.minecraft.client.Minecraft;

import java.awt.*;

public class SearchModeComponent extends Component {
    private boolean isYoutube;
    private float ytY, scY;
    private float comHeight;
    private float rectY, rY = -1;

    public SearchModeComponent(float width, float height) {
        this.width = (int) width;
        this.height = (int) height;
        comHeight = getFontHeight();
        isYoutube = true;
    }

    @Override
    public int doRender(int _x, int y, int mouseX, int mouseY) {
        this.x = _x + 1;
        this.y = y;

        if (rectY == -1) {
            rectY = getRectY();
            rY = rectY;
        }
        rectY = getRectY();
        rY = smoothTrans(rY, rectY);
        RenderUtil.drawRect(x, rY - 5, x + width, rY + comHeight + 5, new Color(6, 167, 255, 255));

        ytY = y + 20;
        drawString("Youtube", x + 15, ytY, isYoutube ? ColorUtil.toRGBA(255, 255, 255, 255) : ColorUtil.toRGBA(0, 0, 0, 255));
        if (isMouseHovering(mouseX, mouseY, x, ytY, width, comHeight))
            RenderUtil.drawRect(x, ytY - 5, x + width, ytY + comHeight, new Color(190, 190, 190, 60));

        scY = y + 40;
        drawString("SoundCloud", x + 15, scY, !isYoutube ? ColorUtil.toRGBA(255, 255, 255, 255) : ColorUtil.toRGBA(0, 0, 0, 255));
        if (isMouseHovering(mouseX, mouseY, x, scY, width, comHeight))
            RenderUtil.drawRect(x, scY - 5, x + width + 5, scY + comHeight + 5, new Color(190, 190, 190, 60));

        return height;
    }

    private float getRectY() {
        return isYoutube ? ytY : scY;
    }

    public float smoothTrans(double current, double last) {
        return (float) (current * Minecraft.getMinecraft().timer.renderPartialTicks + (last * (1.0f - Minecraft.getMinecraft().timer.renderPartialTicks)));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isMouseHovering(mouseX, mouseY, x, ytY, width, comHeight, 5))
            isYoutube = true;

        if (isMouseHovering(mouseX, mouseY, x, scY, width, comHeight, 5))
            isYoutube = false;
    }

    public boolean isYoutube(){
        return isYoutube;
    }
}
