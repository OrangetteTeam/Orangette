package club.eridani.cursa.utils;

import club.eridani.cursa.client.FontManager;
import club.eridani.cursa.gui.font.CFontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class RenderUtil {
    public static void drawRect(float left, float top, float right, float bottom , int color) {
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

    public static void drawRect(float left, float top, float right, float bottom , Color color) {
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

}
