package club.eridani.cursa.gui.sigma.component;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.gui.sigma.Component;
import club.eridani.cursa.gui.sigma.SigmaGUI;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.utils.ColorUtil;
import club.eridani.cursa.utils.RenderUtil;

import java.awt.*;

public class ModuleButton extends Component {
    private ModuleBase module;
    public ModuleButton(ModuleBase module){
        this.module = module;
        this.width = 105;
        this.height = 18;
    }

    public int render(int x , int y , int mouseX , int mouseY , boolean doMouseAction){
        this.x = x;
        this.y = y;
        if(module.isEnabled()) RenderUtil.drawRect(x , y , x + width , y + height , new Color(6,167,255 , 255));
        drawString(module.name , x + 10 , getCenter(y , height , getFontHeight()) , module.isEnabled() ? ColorUtil.toRGBA(255 , 255 , 255 , 255) : ColorUtil.toRGBA(0 , 0 , 0 , 255));
        if(isMouseHovering(mouseX , mouseY) && doMouseAction)
            RenderUtil.drawRect(x, y, x + width, y + height, new Color(190 , 190 , 190 , 60));
        return height;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(isMouseHovering(mouseX , mouseY)) {
            if(mouseButton == 0) module.toggle();
            if(mouseButton == 1) {
                SigmaGUI.INSTANCE.setSettingModule(module);
                SigmaGUI.INSTANCE.toggleSettingWindowVisible();
            }
        }
    }
}
