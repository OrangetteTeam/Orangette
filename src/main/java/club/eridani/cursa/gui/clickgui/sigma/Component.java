package club.eridani.cursa.gui.clickgui.sigma;

import club.eridani.cursa.client.FontManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class Component {
    public Minecraft mc = Minecraft.getMinecraft();
    public FontRenderer fontRenderer = mc.fontRenderer;
    public int x , y , width , height;

    public int doRender(int x , int y, int mouseX , int mouseY){
        return 0;
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton){
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
    }

    public void keyTyped(char typedChar , int key) {
    }

    public Boolean isMouseHovering(int mouseX , int mouseY) {
        return x < mouseX && x + width > mouseX && y < mouseY && y + height > mouseY;
    }

    public Boolean isMouseHovering(float mouseX , float mouseY , float cx , float cy , float cw , float ch) {
        return cx < mouseX && cx + cw > mouseX && cy < mouseY && cy + ch > mouseY;
    }

    public Boolean isMouseHovering(float mouseX , float mouseY , float cx , float cy , float cw , float ch , float diff) {
        return cx - diff < mouseX && cx + cw + diff > mouseX && cy - diff < mouseY && cy + ch + diff > mouseY;
    }

    public float getCenter(float a , float b , float c){
        return a + (b - c) / 2;
    }

    public void drawString(String str , float x , float y , int color){
        FontManager.jelloFont.drawString(str , x , y , color);
    }

    public float getFontWidth(String str){
        return FontManager.jelloFont.getStringWidth(str);
    }

    public float getFontHeight(){
        return FontManager.jelloFont.getHeight();
    }

    public String getName(String name){
        String r = "";
        boolean a = false;
        for(char c : name.toCharArray()){
            if(!a) r += String.valueOf(c).toUpperCase();
            else r += String.valueOf(c).toLowerCase();
            a = true;
        }
        return r;
    }
}
