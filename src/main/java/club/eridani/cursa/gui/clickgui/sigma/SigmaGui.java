package club.eridani.cursa.gui.clickgui.sigma;

import club.eridani.cursa.client.FontManager;
import club.eridani.cursa.client.ModuleManager;
import club.eridani.cursa.gui.clickgui.sigma.component.BindButton;
import club.eridani.cursa.gui.clickgui.sigma.component.BooleanButton;
import club.eridani.cursa.gui.clickgui.sigma.component.ModeButton;
import club.eridani.cursa.gui.clickgui.sigma.component.NumberSlider;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.module.modules.client.ClickGui;
import club.eridani.cursa.utils.ColorUtil;
import club.eridani.cursa.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class SigmaGui extends GuiScreen {
    public static SigmaGui INSTANCE;
    private static List<Panel> panels = new ArrayList<>();
    private List<Component> components = new ArrayList<>();
    private ModuleBase settingModule;
    private boolean settingWindowVisible = false;
    private float wScale, windowScale, transparency = 0;
    private float target, easing;
    private boolean closing, closingSettingWindow;
    private int windowWidth, windowHeight = 0;
    private float windowX, windowY = 0;
    private float offset, targetOffset;

    @Override
    public void initGui() {
        INSTANCE = this;
        target = 1.0F;
        easing = 10.0F;
        closing = false;
        if (Minecraft.getMinecraft().entityRenderer.getShaderGroup() != null)
            Minecraft.getMinecraft().entityRenderer.getShaderGroup().deleteShaderGroup();
        Minecraft.getMinecraft().entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));

        if (panels.isEmpty()) {
            int sx = 20;
            for (Category c : Category.values()) {
                if (!c.visible) continue;
                panels.add(new Panel(c, sx, 30));
                sx += 120;
            }
        }
    }

    @Override
    public void onGuiClosed() {
        if (Minecraft.getMinecraft().entityRenderer.getShaderGroup() != null)
            Minecraft.getMinecraft().entityRenderer.getShaderGroup().deleteShaderGroup();
        if (ModuleManager.getModule(ClickGui.class).isEnabled()) {
            ModuleManager.getModule(ClickGui.class).disable();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        //main
        ScaledResolution sr = new ScaledResolution(mc);
        GlStateManager.translate(sr.getScaledWidth() / 2, sr.getScaledHeight() / 2, 0);
        GlStateManager.scale(easing, easing, 0);
        GlStateManager.translate(-sr.getScaledWidth() / 2, -sr.getScaledHeight() / 2, 0);
        easing = smoothTrans(easing, target);
        execute(p -> p.render(mouseX, mouseY, easing, !settingWindowVisible));
        if (closing && Math.abs(10.0 - easing) < 1) mc.displayGuiScreen(null);
        //setting window
        int t = settingWindowVisible ? 130 : 0;
        transparency = smoothTrans(transparency, t);
        RenderUtil.drawRect(0, 0, 9999, 9999, ColorUtil.toRGBA(0, 0, 0, (int) transparency));
        if (settingWindowVisible) {
            windowWidth = 200;
            windowHeight = 240;
            windowX = getCenter(0, sr.getScaledWidth(), windowWidth);
            windowY = getCenter(0, sr.getScaledHeight(), windowHeight);
            wScale = smoothTrans(wScale, windowScale);
            GlStateManager.translate(sr.getScaledWidth() / 2, sr.getScaledHeight() / 2, 0);
            GlStateManager.scale(wScale, wScale, 0);
            GlStateManager.translate(-sr.getScaledWidth() / 2, -sr.getScaledHeight() / 2, 0);
            FontManager.jelloLargeFont.drawString(settingModule.name, windowX, windowY - 15, ColorUtil.toRGBA(250, 250, 250, 255), false);
            RenderUtil.drawRoundedRect(windowX, windowY, windowX + windowWidth + 3, windowY + windowHeight, 2, ColorUtil.toRGBA(250, 250, 250, 255));
            //components
            RenderUtil.scissor(windowX, windowY + 5, width, windowHeight - 5, wScale);
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            AtomicInteger h = new AtomicInteger((int) ((int) windowY + 5 + offset));
            components.forEach(c -> h.updateAndGet(he -> c.doRender((int) windowX, he, mouseX, mouseY) + he));
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            //scrollbar
            /*
            float scrollRadio = h.get() < windowHeight ? 1.0F : (float)windowHeight / h.get();
            float scrollWidth = 2;
            float scrollHeight = 224 * scrollRadio;
            float scrollX = windowX + windowWidth - scrollWidth - 3;
            float scrollY = windowY;
            RenderUtil.drawRect(scrollX , scrollY , scrollX + scrollWidth , scrollY + scrollHeight , ColorUtil.toRGBA(55,55,55 , 255));
             */
            if (closingSettingWindow && Math.abs(windowScale - wScale) < 1) {
                settingWindowVisible = false;
                closingSettingWindow = false;
            }
            //scroll
            if (isMouseHovering(mouseX, mouseY, windowX, windowY, windowWidth, windowHeight)) {
                int dWheel = Mouse.getDWheel();
                if (dWheel < 0) targetOffset -= 15;
                else if (dWheel > 0) targetOffset += 15;
            }
            offset += (targetOffset - offset) * 0.3;
            if (targetOffset > 0) targetOffset = 0;
            /*
            if(h.get() < windowHeight){
                if(targetOffset < 0) targetOffset = 0;
            }
            else{
                if(scrollRadio > 1.0) targetOffset = (h.get() - windowHeight) * -1;
            }
             */
        }
    }

    public float getCenter(float a, float b, float c) {
        return a + (b - c) / 2;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (settingWindowVisible && !isMouseHovering(mouseX, mouseY, windowX, windowY, windowWidth, windowHeight))
            toggleSettingWindowVisible();
        if (!settingWindowVisible) execute(p -> p.mouseClicked(mouseX, mouseY, mouseButton));
        else components.forEach(c -> c.mouseClicked(mouseX, mouseY, mouseButton));
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (settingWindowVisible) components.forEach(c -> c.mouseReleased(mouseX, mouseY, state));
        execute(p -> p.mouseReleased(mouseX, mouseY, state));
    }

    public Boolean isMouseHovering(float mouseX, float mouseY, float cx, float cy, float cw, float ch) {
        return cx < mouseX && cx + cw > mouseX && cy < mouseY && cy + ch > mouseY;
    }

    @Override
    public void keyTyped(char typedChar, int key) {
        if (key == Keyboard.KEY_ESCAPE) {
            //if opening setting window we must close it
            if (settingWindowVisible) {
                toggleSettingWindowVisible();
                return;
            }
            target = 10.0F;
            closing = true;
        }
        components.forEach(c -> c.keyTyped(typedChar, key));
    }

    public void toggleSettingWindowVisible() {
        offset = 0;
        targetOffset = 0;
        if (!settingWindowVisible) {
            closingSettingWindow = false;
            settingWindowVisible = true;
            wScale = 10.0F;
            windowScale = 1.0F;
        } else {
            closingSettingWindow = true;
            windowScale = 10.0F;
        }
    }

    public void setSettingModule(ModuleBase module) {
        this.settingModule = module;
        //setting components
        components.clear();
        module.getSettings().forEach(s -> {
            if (s.getValue() instanceof Boolean) components.add(new BooleanButton(s));
            if (s.getValue() instanceof String) components.add(new ModeButton(s));
            if (s.getValue() instanceof Integer || s.getValue() instanceof Double || s.getValue() instanceof Float)
                components.add(new NumberSlider(s));
        });
        components.add(new BindButton(module));
    }

    private void execute(Consumer<? super Panel> t) {
        panels.forEach(t);
    }

    public float smoothTrans(double current, double last) {
        return (float) (current * Minecraft.getMinecraft().timer.renderPartialTicks + (last * (1.0f - Minecraft.getMinecraft().timer.renderPartialTicks)));
    }

    public void swap(Panel instance) {
        int index = panels.indexOf(instance);
        if(index == -1) return;
        Collections.swap(panels , index , 0);
    }
}
