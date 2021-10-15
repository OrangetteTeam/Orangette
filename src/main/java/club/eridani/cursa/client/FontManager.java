package club.eridani.cursa.client;

import club.eridani.cursa.gui.font.CFont;
import club.eridani.cursa.gui.font.CFontRenderer;

import java.awt.Color;
import java.awt.Font;

public class FontManager {

    public static CFontRenderer iconFont;
    public static CFontRenderer fontRenderer;
    public static CFontRenderer jelloFont, jelloLargeFont;

    public static void init() {
        iconFont = new CFontRenderer(new CFont.CustomFont("/assets/minecraft/fonts/Icon.ttf", 22f, Font.PLAIN), true, false);
        fontRenderer = new CFontRenderer(new CFont.CustomFont("/assets/minecraft/fonts/Comfortaa-Bold.ttf", 18f, Font.PLAIN), true, false);
        jelloFont = new CFontRenderer(new CFont.CustomFont("/assets/minecraft/fonts/JelloLight.ttf", 19, Font.PLAIN), true, false);
        jelloLargeFont = new CFontRenderer(new CFont.CustomFont("/assets/minecraft/fonts/JelloLight.ttf", 23f, Font.PLAIN), true, false);
    }

    public static int getWidth(String str) {
        return fontRenderer.getStringWidth(str);
    }

    public static int getHeight() {
        return fontRenderer.getHeight() + 2;
    }

    public static void draw(String str, int x, int y, int color) {
        fontRenderer.drawString(str, x, y, color);
    }

    public static void draw(String str, int x, int y, Color color) {
        fontRenderer.drawString(str, x, y, color.getRGB());
    }

    public static int getIconWidth() {
        return iconFont.getStringWidth("q");
    }

    public static int getIconHeight() {
        return iconFont.getHeight();
    }

    public static void drawIcon(int x, int y, int color) {
        iconFont.drawString("q", x, y, color);
    }

    public static void drawIcon(int x, int y, Color color) {
        iconFont.drawString("q", x, y, color.getRGB());
    }

}
