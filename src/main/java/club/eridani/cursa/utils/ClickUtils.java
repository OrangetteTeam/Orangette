package club.eridani.cursa.utils;

public class ClickUtils {

    public static boolean isMouseHovering(float x, float y, float width, float height, int mouseX, int mouseY){
        return mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + height;
    }
}
