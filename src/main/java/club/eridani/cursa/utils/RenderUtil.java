package club.eridani.cursa.utils;

import club.eridani.cursa.client.FontManager;
import club.eridani.cursa.gui.font.CFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.awt.Color;

public class RenderUtil {
    private static Minecraft mc = Minecraft.getMinecraft();

    public static void drawRect(double left, double top, double right, double bottom, int color) {
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }
        float alpha = (float) (color >> 24 & 0xFF) / 255.0f;
        float red = (float) (color >> 16 & 0xFF) / 255.0f;
        float green = (float) (color >> 8 & 0xFF) / 255.0f;
        float blue = (float) (color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(left, bottom, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(right, bottom, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(right, top, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(left, top, 0.0).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRect(float left, float top, float right, float bottom, Color color) {
        float alpha = color.getAlpha() / 255.0f;
        float red = color.getRed() / 255.0f;
        float green = color.getGreen() / 255.0f;
        float blue = color.getBlue() / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(left, bottom, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(right, bottom, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(right, top, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(left, top, 0.0).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }


    public static void drawGradientRect(float left, float top, float right, float bottom, int coltl, int coltr, int colbl,
                                        int colbr) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(right, top, 0).color((coltr & 0x00ff0000) >> 16, (coltr & 0x0000ff00) >> 8,
                (coltr & 0x000000ff), (coltr & 0xff000000) >>> 24).endVertex();
        buffer.pos(left, top, 0).color((coltl & 0x00ff0000) >> 16, (coltl & 0x0000ff00) >> 8, (coltl & 0x000000ff),
                (coltl & 0xff000000) >>> 24).endVertex();
        buffer.pos(left, bottom, 0).color((colbl & 0x00ff0000) >> 16, (colbl & 0x0000ff00) >> 8,
                (colbl & 0x000000ff), (colbl & 0xff000000) >>> 24).endVertex();
        buffer.pos(right, bottom, 0).color((colbr & 0x00ff0000) >> 16, (colbr & 0x0000ff00) >> 8,
                (colbr & 0x000000ff), (colbr & 0xff000000) >>> 24).endVertex();
        tessellator.draw();

        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawRoundedRect(float x, float y, float x2, float y2, final float round, final int color) {
        x += (float) (round / 2.0f + 0.5);
        y += (float) (round / 2.0f + 0.5);
        x2 -= (float) (round / 2.0f + 0.5);
        y2 -= (float) (round / 2.0f + 0.5);
        Gui.drawRect((int) x, (int) y, (int) x2, (int) y2, color);
        RenderUtil.drawCircle(x2 - round / 2.0f, y + round / 2.0f, round, color);
        RenderUtil.drawCircle(x + round / 2.0f, y2 - round / 2.0f, round, color);
        RenderUtil.drawCircle(x + round / 2.0f, y + round / 2.0f, round, color);
        RenderUtil.drawCircle(x2 - round / 2.0f, y2 - round / 2.0f, round, color);
        Gui.drawRect((int) (x - round / 2.0f - 0.5f), (int) (y + round / 2.0f), (int) x2, (int) (y2 - round / 2.0f),
                color);
        Gui.drawRect((int) x, (int) (y + round / 2.0f), (int) (x2 + round / 2.0f + 0.5f), (int) (y2 - round / 2.0f),
                color);
        Gui.drawRect((int) (x + round / 2.0f), (int) (y - round / 2.0f - 0.5f), (int) (x2 - round / 2.0f),
                (int) (y2 - round / 2.0f), color);
        Gui.drawRect((int) (x + round / 2.0f), (int) y, (int) (x2 - round / 2.0f), (int) (y2 + round / 2.0f + 0.5f),
                color);
    }

    public static void drawCircle(double x, double y, float radius, int color) {
        float f = (color >> 24 & 0xFF) / 255.0f;
        float f2 = (color >> 16 & 0xFF) / 255.0f;
        float f3 = (color >> 8 & 0xFF) / 255.0f;
        float f4 = (color & 0xFF) / 255.0f;
        GL11.glColor4f(f2, f3, f4, f);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GL11.glDisable(3553);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.alphaFunc(516, 0.001f);
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder render = tess.getBuffer();
        for (double i = 0; i < 360; ++i) {
            double cs = i * 3.141592653589793 / 180.0;
            double ps = (i - 1.0) * 3.141592653589793 / 180.0;
            double[] outer = {Math.cos(cs) * radius, -Math.sin(cs) * radius, Math.cos(ps) * radius, -Math.sin(ps) * radius};
            render.begin(6, DefaultVertexFormats.POSITION);
            render.pos(x + outer[2], y + outer[3], 0.0).endVertex();
            render.pos(x + outer[0], y + outer[1], 0.0).endVertex();
            render.pos(x, y, 0.0).endVertex();
            tess.draw();
        }
        GlStateManager.resetColor();
        GlStateManager.disableBlend();
        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.disableAlpha();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(3553);
    }


    public static void drawBorderRect(double x, double y, double x1, double y1, int color, double lwidth) {
        drawHLine(x, y, x1, y, (float) lwidth, color);
        drawHLine(x1, y, x1, y1, (float) lwidth, color);
        drawHLine(x, y1, x1, y1, (float) lwidth, color);
        drawHLine(x, y1, x, y, (float) lwidth, color);
    }

    public static void drawHLine(double x, double y, double x1, double y1, float width, int color) {
        float var11 = (color >> 24 & 0xFF) / 255.0F;
        float var6 = (color >> 16 & 0xFF) / 255.0F;
        float var7 = (color >> 8 & 0xFF) / 255.0F;
        float var8 = (color & 0xFF) / 255.0F;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var6, var7, var8, var11);
        GL11.glPushMatrix();
        GL11.glLineWidth(width);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x1, y1);
        GL11.glEnd();
        GL11.glLineWidth(1);
        GL11.glPopMatrix();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.color(1, 1, 1, 1);
    }

    public static void drawGradient(double x, double y, double x2, double y2, int col1, int col2) {
        float f = (col1 >> 24 & 0xFF) / 255.0F;
        float f1 = (col1 >> 16 & 0xFF) / 255.0F;
        float f2 = (col1 >> 8 & 0xFF) / 255.0F;
        float f3 = (col1 & 0xFF) / 255.0F;
        float f4 = (col2 >> 24 & 0xFF) / 255.0F;
        float f5 = (col2 >> 16 & 0xFF) / 255.0F;
        float f6 = (col2 >> 8 & 0xFF) / 255.0F;
        float f7 = (col2 & 0xFF) / 255.0F;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glColor4f(f5, f6, f7, f4);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
        GL11.glColor4d(1.0D, 1.0D, 1.0D, 1.0D);
    }

    public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (col1 >> 24 & 0xFF) / 255.0F;
        float f1 = (col1 >> 16 & 0xFF) / 255.0F;
        float f2 = (col1 >> 8 & 0xFF) / 255.0F;
        float f3 = (col1 & 0xFF) / 255.0F;
        float f4 = (col2 >> 24 & 0xFF) / 255.0F;
        float f5 = (col2 >> 16 & 0xFF) / 255.0F;
        float f6 = (col2 >> 8 & 0xFF) / 255.0F;
        float f7 = (col2 & 0xFF) / 255.0F;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glVertex2d(left, top);
        GL11.glVertex2d(left, bottom);
        GL11.glColor4f(f5, f6, f7, f4);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
        GL11.glColor4d(255, 255, 255, 255);
    }

    public static float drawString(String str, float x, float y, Color color, boolean shadow) {
        CFontRenderer font = FontManager.fontRenderer;
        if (shadow)
            font.drawString(str, x + (0.5), y + (0.5), ColorUtil.toRGBA(new Color(0, 0, 0, 255)), true);
        return font.drawString(str, x, y, ColorUtil.toRGBA(color), false);
    }

    public static float drawString(String str, float x, float y, int color, boolean shadow) {
        CFontRenderer font = FontManager.fontRenderer;
        if (shadow)
            font.drawString(str, x + (0.5), y + (0.5), ColorUtil.toRGBA(new Color(0, 0, 0, 255)), true);
        return font.drawString(str, x, y, color, false);
    }

    public static float getStringWidth(String str) {
        CFontRenderer font = FontManager.fontRenderer;
        return font.getStringWidth(str);
    }

    public static float getStringHeight() {
        CFontRenderer font = FontManager.fontRenderer;
        return (font.getHeight() - 1);
    }

    public static void scissor(float x, float y, float width, float height, float guiScale) {
        float scale = computeGuiScale() * guiScale;
        GL11.glScissor(0, (int) (mc.displayHeight - (y + height) * scale), (int) ((width + x) * scale), (int) (height * scale));
    }

    private static float computeGuiScale() {
        Minecraft mc = Minecraft.getMinecraft();
        int scaleFactor = 1;
        int k = mc.gameSettings.guiScale;
        if (k == 0) k = 1000;
        while (scaleFactor < k && mc.displayWidth / (scaleFactor + 1) >= 320 && mc.displayHeight / (scaleFactor + 1) >= 240)
            ++scaleFactor;
        return scaleFactor;
    }

    private static float guiToScreen(float a) {
        final ScaledResolution scaledresolution = new ScaledResolution(mc);
        final double scaleFactor = scaledresolution.getScaleFactor();
        return (float) (a * scaleFactor);
    }

    public static void drawShader(int x1, int y1, int x2, int y2) {
        int a = 50;
        drawGradientRect(x1 - 5, y1, x1, y2
                , ColorUtil.toRGBA(0, 0, 0, 0), ColorUtil.toRGBA(0, 0, 0, a)
                , ColorUtil.toRGBA(0, 0, 0, 0), ColorUtil.toRGBA(0, 0, 0, a));
        drawGradientRect(x2, y1, x2 + 5, y2
                , ColorUtil.toRGBA(0, 0, 0, a), ColorUtil.toRGBA(0, 0, 0, 0)
                , ColorUtil.toRGBA(0, 0, 0, a), ColorUtil.toRGBA(0, 0, 0, 0));
        drawGradientRect(x1, y1 - 5, x2, y1
                , ColorUtil.toRGBA(0, 0, 0, 0), ColorUtil.toRGBA(0, 0, 0, 0)
                , ColorUtil.toRGBA(0, 0, 0, a), ColorUtil.toRGBA(0, 0, 0, a));
        drawGradientRect(x1, y2, x2, y2 + 5
                , ColorUtil.toRGBA(0, 0, 0, a), ColorUtil.toRGBA(0, 0, 0, a)
                , ColorUtil.toRGBA(0, 0, 0, 0), ColorUtil.toRGBA(0, 0, 0, 0));
        //corner
        drawGradientRect(x1 - 5, y1 - 5, x1, y1
                , ColorUtil.toRGBA(0, 0, 0, 0), ColorUtil.toRGBA(0, 0, 0, 0)
                , ColorUtil.toRGBA(0, 0, 0, 0), ColorUtil.toRGBA(0, 0, 0, a));
        drawGradientRect(x2, y1 - 5, x2 + 5, y1
                , ColorUtil.toRGBA(0, 0, 0, 0), ColorUtil.toRGBA(0, 0, 0, 0)
                , ColorUtil.toRGBA(0, 0, 0, a), ColorUtil.toRGBA(0, 0, 0, 0));
        drawGradientRect(x1 - 5, y2, x1, y2 + 5
                , ColorUtil.toRGBA(0, 0, 0, 0), ColorUtil.toRGBA(0, 0, 0, a)
                , ColorUtil.toRGBA(0, 0, 0, 0), ColorUtil.toRGBA(0, 0, 0, 0));
        drawGradientRect(x2, y2, x2 + 5, y2 + 5
                , ColorUtil.toRGBA(0, 0, 0, a), ColorUtil.toRGBA(0, 0, 0, 0)
                , ColorUtil.toRGBA(0, 0, 0, 0), ColorUtil.toRGBA(0, 0, 0, 0));
    }
}
