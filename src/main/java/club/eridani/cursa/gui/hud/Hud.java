package club.eridani.cursa.gui.hud;

import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.module.modules.client.HUD;
import club.eridani.cursa.setting.Setting;
import club.eridani.cursa.setting.settings.IntSetting;
import club.eridani.cursa.utils.ColorUtil;
import club.eridani.cursa.utils.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

public class Hud extends ModuleBase {
    private final Setting<Integer> x = setting("X" , 0 , 0 , 5000);
    private final Setting<Integer> y = setting("Y" , 0 , 0 , 5000);

    private boolean dragging = false;
    private int dX , dY;
    private int width , height = 0;

    public void renderHud() {
    }

    public void onRender(ScaledResolution s) {
        if(HUD.INSTANCE.isEnabled()) {
            RenderUtil.drawRect(getX() - 3 , getY() - 3 ,
                    getX() + width + 3 , getY() + height + 3 , ColorUtil.toARGB(0 , 0 , 0 , dragging ? 110 :  80));
        }
        ((IntSetting)x).setMax(s.getScaledWidth());
        ((IntSetting)y).setMax(s.getScaledHeight());

        if(isEnabled())
            renderHud();
    }

    public void update(int mouseX , int mouseY) {
        if(dragging) {
            if(!Mouse.isButtonDown(0) || HUD.INSTANCE.isDisabled()) {
                dragging = false;
            }
            else {
                this.x.setValue(mouseX + dX);
                this.y.setValue(mouseY + dY);
            }
        }
    }

    public boolean mouseClicked(int mouseX , int mouseY , int mouseButton) {
        if(isMouseHovering(mouseX , mouseY , getX() - 3 , getY() - 3 , getX() + width + 3 , getY() + height + 3)){
            dX = getX() - mouseX;
            dY = getY() - mouseY;
            return (dragging = true);
        }

        return false;
    }

    public int getX() {
        return x.getValue();
    }

    public int getY() {
        return y.getValue();
    }

    public void setSize(float width , float height) {
        this.width = (int)width;
        this.height = (int)height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Boolean isMouseHovering(float mouseX, float mouseY, float x1, float y1, float x2, float y2) {
        return x1 < mouseX && x2 > mouseX && y1 < mouseY && y2 > mouseY;
    }
}
