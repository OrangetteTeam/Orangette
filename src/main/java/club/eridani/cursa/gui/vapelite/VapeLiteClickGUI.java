package club.eridani.cursa.gui.vapelite;

import club.eridani.cursa.Cursa;
import club.eridani.cursa.client.FontManager;
import club.eridani.cursa.client.ModuleManager;
import club.eridani.cursa.gui.font.CFontRenderer;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;
import club.eridani.cursa.setting.settings.BooleanSetting;
import club.eridani.cursa.setting.settings.DoubleSetting;
import club.eridani.cursa.setting.settings.ModeSetting;
import club.eridani.cursa.utils.RenderUtil;
import club.eridani.cursa.utils.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.io.IOException;

public class VapeLiteClickGUI extends GuiScreen {
    private boolean close = false;
    private boolean closed;

    private float dragX, dragY;
    private boolean drag = false;
    private int valuemodx = 0;
    private static float modsRole, modsRoleNow;
    private static float valueRoleNow, valueRole;

    public float lastPercent;
    public float percent;
    public float percent2;
    public float lastPercent2;
    public float outro;
    public float lastOutro;

    private CFontRenderer font;

    public VapeLiteClickGUI(){
        font = FontManager.fontRenderer;
    }


    @Override
    public void initGui() {
        super.initGui();
        percent = 1.33f;
        lastPercent = 1f;
        percent2 = 1.33f;
        lastPercent2 = 1f;
        outro = 1;
        lastOutro = 1;
        valuetimer.reset();
    }


    /*
    主窗口宽度 = 500
    主窗口高度 = 310
    功能列表起始位置 = 100
    功能宽度 = 325(未开values)
    功能起始高度 = 60
     */


    static float windowX = 200, windowY = 200;
    static float width = 500, height = 310;

    static ClickType selectType = ClickType.Home;
    static Category modCategory = Category.COMBAT;
    static ModuleBase selectMod;

    float[] typeXAnim = new float[]{windowX + 10, windowX + 10, windowX + 10, windowX + 10};

    float hy = windowY + 40;

    Timer valuetimer = new Timer();

    public float smoothTrans(double current, double last){
        return (float) (current + (last - current) / (Minecraft.debugFPS / 10));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ScaledResolution sResolution = new ScaledResolution(mc);
        ScaledResolution sr = new ScaledResolution(mc);


        float outro = smoothTrans(this.outro, lastOutro);
        if (mc.currentScreen == null) {
            GlStateManager.translate(sr.getScaledWidth() / 2, sr.getScaledHeight() / 2, 0);
            GlStateManager.scale(outro, outro, 0);
            GlStateManager.translate(-sr.getScaledWidth() / 2, -sr.getScaledHeight() / 2, 0);
        }


        //animation
        percent = smoothTrans(this.percent, lastPercent);
        percent2 = smoothTrans(this.percent2, lastPercent2);


        if (percent > 0.98) {
            GlStateManager.translate(sr.getScaledWidth() / 2, sr.getScaledHeight() / 2, 0);
            GlStateManager.scale(percent, percent, 0);
            GlStateManager.translate(-sr.getScaledWidth() / 2, -sr.getScaledHeight() / 2, 0);
        } else {
            if (percent2 <= 1) {
                GlStateManager.translate(sr.getScaledWidth() / 2, sr.getScaledHeight() / 2, 0);
                GlStateManager.scale(percent2, percent2, 0);
                GlStateManager.translate(-sr.getScaledWidth() / 2, -sr.getScaledHeight() / 2, 0);
            }
        }


        if(percent <= 1.5 && close) {
            percent = smoothTrans(this.percent, 2);
            percent2 = smoothTrans(this.percent2, 2);
        }

        if(percent >= 1.4  &&  close){
            percent = 1.5f;
            closed = true;
            mc.currentScreen = null;
        }


        RenderUtil.drawGradient(0, 0, sResolution.getScaledWidth(), sResolution.getScaledHeight(), new Color(255, 130, 164, 100).getRGB(), new Color(0, 0, 0, 30).getRGB());
//        if (inAnim > 0) {
//            inAnim -= 5000f / mc.debugFPS;
//        } else {
//            inAnim += 0.1f;
//        }
//        if (inAnim > 5) {
//            GlStateManager.translate(inAnim, 0, 0);
//        }

        //拖动
        if (isHovered(windowX, windowY, windowX + width, windowY + 20, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            if (dragX == 0 && dragY == 0) {
                dragX = mouseX - windowX;
                dragY = mouseY - windowY;
            } else {
                windowX = mouseX - dragX;
                windowY = mouseY - dragY;
            }
            drag = true;
        } else if (dragX != 0 || dragY != 0) {
            dragX = 0;
            dragY = 0;
        }


        //绘制主窗口
        RenderUtil.drawRect(windowX, windowY, windowX + width, windowY + height, new Color(21, 22, 25).getRGB());
        if (selectMod == null) {
            font.drawString(Cursa.MOD_NAME, windowX + 20, windowY + height - 20, new Color(77, 78, 84).getRGB());
        }
        //绘制顶部图标
        float typeX = windowX + 20;
        int i = 0;
        for (Enum<?> e : ClickType.values()) {
            if (!isHovered(windowX, windowY, windowX + width, windowY + 20, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                if (typeXAnim[i] != typeX) {
                    typeXAnim[i] += (typeX - typeXAnim[i]) / 20;
                }
            } else {
                if (typeXAnim[i] != typeX) {
                    typeXAnim[i] = typeX;
                }
            }
            if (e != ClickType.Settings) {
                if (e == selectType) {
                    //RenderUtil.drawImage(typeXAnim[i], windowY + 10, 16, 16, new ResourceLocation("client/vapeclickgui/" + e.name() + ".png"), new Color(255, 255, 255));
                    font.drawString(e.name(), typeXAnim[i] + 20, windowY + 15, new Color(255, 255, 255).getRGB());
                    typeX += (32 + font.getStringWidth(e.name() + " "));
                } else {
                    //RenderUtil.drawImage(typeXAnim[i], windowY + 10, 16, 16, new ResourceLocation("client/vapeclickgui/" + e.name() + ".png"), new Color(79, 80, 86));
                    typeX += (32);
                }
            } else {
                //RenderUtil.drawImage(windowX + width - 20, windowY + 10, 16, 16, new ResourceLocation("client/vapeclickgui/" + e.name() + ".png"), e == selectType ? new Color(255, 255, 255) : new Color(79, 80, 86));
            }
            i++;
        }


        if (selectType == ClickType.Home) {
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            GL11.glScissor(0, 2 * ((int) (sr.getScaledHeight_double() - (windowY + height))) + 40, (int) (sr.getScaledWidth_double() * 2), (int) ((height) * 2) - 160);
            if (selectMod == null) {
                //绘制类型列表
                float cateY = windowY + 65;
                for (Category m : Category.values()) {
                    if (m == modCategory) {
                        font.drawString(m.name(), windowX + 20, cateY, -1);
                        RenderUtil.drawBorderRect(windowX + 20, hy + font.getHeight() + 2, windowX + 30, hy + font.getHeight() + 4, new Color(51, 112, 203).getRGB(), 0.5);
                        if (isHovered(windowX, windowY, windowX + width, windowY + 20, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                            hy = cateY;
                        } else {
                            if (hy != cateY) {
                                hy += (cateY - hy) / 20;
                            }
                        }
                    } else {
                        font.drawString(m.name(), windowX + 20, cateY, new Color(108, 109, 113).getRGB());
                    }


                    cateY += 25;
                }
            }
            if (selectMod != null) {
                if (valuemodx > -80) {
                    valuemodx -= 5;
                }
            } else {
                if (valuemodx < 0) {
                    valuemodx += 5;
                }
            }

            if (selectMod != null) {
                RenderUtil.drawBorderRect(windowX + 430 + valuemodx, windowY + 60, windowX + width, windowY + height - 20, new Color(32, 31, 35).getRGB(), 0.5);
                RenderUtil.drawBorderRect(windowX + 430 + valuemodx, windowY + 60, windowX + width, windowY + 85, new Color(39, 38, 42).getRGB(), 0.5);
                //RenderUtil.drawImage(windowX + 435 + valuemodx, windowY + 65, 16, 16, new ResourceLocation("client/vapeclickgui/back.png"), new Color(82, 82, 85));
                if (isHovered(windowX + 435 + valuemodx, windowY + 65, windowX + 435 + valuemodx + 16, windowY + 65 + 16, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                    selectMod = null;
                    valuetimer.reset();
                }


                //滚动
                int dWheel = Mouse.getDWheel();
                if (isHovered(windowX + 430 + (int) valuemodx, windowY + 60, windowX + width, windowY + height - 20, mouseX, mouseY)) {
                    if (dWheel < 0 && Math.abs(valueRole) + 170 < (selectMod.getSettings().size() * 25)) {
                        valueRole -= 32;
                    }
                    if (dWheel > 0 && valueRole < 0) {
                        valueRole += 32;
                    }
                }

                if (valueRoleNow != valueRole) {
                    valueRoleNow += (valueRole - valueRoleNow) / 20;
                    valueRoleNow = (int) valueRoleNow;
                }

                float valuey = windowY + 100 + valueRoleNow;

                if(selectMod == null) {
                    return;
                }

                for (Setting v : selectMod.getSettings()) {
                    if (v instanceof BooleanSetting) {
                        if (valuey + 4 > windowY + 100) {
                            if (((Boolean) v.getValue())) {
                                font.drawString(v.getName(), windowX + 445 + valuemodx, valuey + 4, -1);
                                v.optionAnim = 100;
                                RenderUtil.drawBorderRect(windowX + width - 30, valuey + 2, windowX + width - 10, valuey + 12, 4, new Color(33, 94, 181, (int) (v.optionAnimNow / 100 * 255)).getRGB());
                                RenderUtil.drawCircle(windowX + width - 25 + 10 * (v.optionAnimNow / 100f), valuey + 7, 3.5f, new Color(255, 255, 255).getRGB());
                            } else {
                                font.drawString(v.getName(), windowX + 445 + valuemodx, valuey + 4, new Color(73, 72, 76).getRGB());
                                v.optionAnim = 0;
                                RenderUtil.drawBorderRect(windowX + width - 30, valuey + 2, windowX + width - 10, valuey + 12, 4, new Color(59, 60, 65).getRGB());
                                RenderUtil.drawBorderRect(windowX + width - 29, valuey + 3, windowX + width - 11, valuey + 11, 3, new Color(32, 31, 35).getRGB());
                                RenderUtil.drawCircle(windowX + width - 25 + 10 * (v.optionAnimNow / 100f), valuey + 7, 3.5f, new Color(59, 60, 65).getRGB());
                            }
                            if (isHovered(windowX + width - 30, valuey + 2, windowX + width - 10, valuey + 12, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                                if (valuetimer.delay(300)) {
                                    v.setValue(!(Boolean) v.getValue());
                                    valuetimer.reset();
                                }
                            }
                        }

                        if (v.optionAnimNow != v.optionAnim) {
                            v.optionAnimNow += (v.optionAnim - v.optionAnimNow) / 20;
                        }
                        valuey += 25;
                    }
                }
                for (Setting v : selectMod.getSettings()) {
                    if (v instanceof DoubleSetting) {
                        if (valuey + 4 > windowY + 100) {

                            float present = (float) (((windowX + width - 11) - (windowX + 450 + valuemodx))
                                    * (((Number) v.getValue()).floatValue() - ((DoubleSetting) v).getMin().floatValue())
                                    / (((DoubleSetting) v).getMax().floatValue() - ((DoubleSetting) v).getMin().floatValue()));

                            font.drawString(v.getName(), windowX + 445 + valuemodx, valuey + 5, new Color(73, 72, 76).getRGB());
                            font.drawCenteredString(v.getValue().toString(), windowX + width - 20, valuey + 5, new Color(255, 255, 255).getRGB());
                            RenderUtil.drawRect(windowX + 450 + valuemodx, valuey + 20, windowX + width - 11, valuey + 21.5f, new Color(77, 76, 79).getRGB());
                            RenderUtil.drawRect(windowX + 450 + valuemodx, valuey + 20, windowX + 450 + valuemodx + present, valuey + 21.5f, new Color(43, 116, 226).getRGB());
                            RenderUtil.drawCircle(windowX + 450 + valuemodx + present, valuey + 21f, 5, new Color(32, 31, 35).getRGB());
                            RenderUtil.drawCircle(windowX + 450 + valuemodx + present, valuey + 21f, 5, new Color(32, 31, 35).getRGB());
                            RenderUtil.drawCircle(windowX + 450 + valuemodx + present, valuey + 21f, 4, new Color(44, 115, 224).getRGB());
                            RenderUtil.drawCircle(windowX + 450 + valuemodx + present, valuey + 21f, 4, new Color(44, 115, 224).getRGB());

                            if (isHovered(windowX + 450 + valuemodx, valuey + 18, windowX + width - 11, valuey + 23.5f, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                                float render2 = ((DoubleSetting) v).getMin().floatValue();
                                double max = ((DoubleSetting) v).getMax().doubleValue();
                                double inc = 0.1;
                                double valAbs = (double) mouseX - ((double) (windowX + 450 + valuemodx));
                                double perc = valAbs / (((windowX + width - 11) - (windowX + 450 + valuemodx)));
                                perc = Math.min(Math.max(0.0D, perc), 1.0D);
                                double valRel = (max - render2) * perc;
                                double val = render2 + valRel;
                                val = (double) Math.round(val * (1.0D / inc)) / (1.0D / inc);
                                ((DoubleSetting) v).setValue(Double.valueOf(val));
                            }
                        }
                        valuey += 25;
                    }
                }
                for (Setting v : selectMod.getSettings()) {
                    if (v instanceof ModeSetting) {
                        ModeSetting modeValue = (ModeSetting) v;

                        if (valuey + 4 > windowY + 100 & valuey < (windowY + height)) {
                            RenderUtil.drawBorderRect(windowX + 445 + valuemodx, valuey + 2, windowX + width - 5, valuey + 22, 2, new Color(46, 45, 48).getRGB());
                            RenderUtil.drawBorderRect(windowX + 446 + valuemodx, valuey + 3, windowX + width - 6, valuey + 21, 2, new Color(32, 31, 35).getRGB());
                            font.drawString(v.getName() + ":" + modeValue.getValue(), windowX + 455 + valuemodx, valuey + 10, new Color(230, 230, 230).getRGB());
                            font.drawString(">", windowX + width - 15, valuey + 9, new Color(73, 72, 76).getRGB());
                            if (isHovered(windowX + 445 + valuemodx, valuey + 2, windowX + width - 5, valuey + 22, mouseX, mouseY) && Mouse.isButtonDown(0) && valuetimer.delay(300)) {

                                    v.setValue(modeValue.getModes().get(0));
                                valuetimer.reset();
                            }
                        }
                        valuey += 25;
                    }
                }
                /*for (NewMode v : selectMod.getNewModes()) {
                    NewMode modeValue = (NewMode) v;

                    if (valuey + 4 > windowY + 100 & valuey < (windowY + height)) {
                        RenderUtil.drawRoundedRect(windowX + 445 + valuemodx, valuey + 2, windowX + width - 5, valuey + 22, 2, new Color(46, 45, 48).getRGB());
                        RenderUtil.drawRoundedRect(windowX + 446 + valuemodx, valuey + 3, windowX + width - 6, valuey + 21, 2, new Color(32, 31, 35).getRGB());
                        FontLoaders.F16.drawString(v.getName() + ":" + modeValue.getModeAsString(), windowX + 455 + valuemodx, valuey + 10, new Color(230, 230, 230).getRGB());
                        FontLoaders.F18.drawString(">", windowX + width - 15, valuey + 9, new Color(73, 72, 76).getRGB());
                        if (isHovered(windowX + 445 + valuemodx, valuey + 2, windowX + width - 5, valuey + 22, mouseX, mouseY) && Mouse.isButtonDown(0) && valuetimer.delay(300)) {
                            if (Arrays.binarySearch(modeValue.getModes(), (v.getValue()))
                                    + 1 < modeValue.getModes().length) {
                                v.setValue(modeValue
                                        .getModes()[Arrays.binarySearch(modeValue.getModes(), (v.getValue())) + 1]);
                            } else {
                                v.setValue(modeValue.getModes()[0]);
                            }
                            valuetimer.reset();
                        }
                    }
                    valuey += 25;
                }*/
            }

            float modY = windowY + 70 + modsRoleNow;
            for (ModuleBase m : ModuleManager.getInstance().moduleList) {
                if (m.category != modCategory)
                    continue;

                if (isHovered(windowX + 100 + valuemodx, modY - 10, windowX + 425 + valuemodx, modY + 25, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                    if (valuetimer.delay(300) && modY + 40 > (windowY + 70) && modY < (windowY + height)) {
                        m.toggle();
                        valuetimer.reset();
                    }
                } else if (isHovered(windowX + 100 + valuemodx, modY - 10, windowX + 425 + valuemodx, modY + 25, mouseX, mouseY) && Mouse.isButtonDown(1)) {
                    if (valuetimer.delay(300)) {
                        if (selectMod != m) {
                            valueRole = 0;
                            selectMod = m;
                        } else if (selectMod == m) {
                            selectMod = null;
                        }
                        valuetimer.reset();
                    }
                }

                if (isHovered(windowX + 100 + valuemodx, modY - 10, windowX + 425 + valuemodx, modY + 25, mouseX, mouseY)) {
                    if (m.isEnabled()) {
                        RenderUtil.drawBorderRect(windowX + 100 + valuemodx, modY - 10, windowX + 425 + valuemodx, modY + 25, new Color(43, 41, 45).getRGB(), 0.5);
                    } else {
                        RenderUtil.drawBorderRect(windowX + 100 + valuemodx, modY - 10, windowX + 425 + valuemodx, modY + 25, new Color(35, 35, 35).getRGB(), 0.5);
                    }
                } else {
                    if (m.isEnabled()) {
                        RenderUtil.drawBorderRect(windowX + 100 + valuemodx, modY - 10, windowX + 425 + valuemodx, modY + 25, new Color(36, 34, 38).getRGB(), 0.5);
                    } else {
                        RenderUtil.drawBorderRect(windowX + 100 + valuemodx, modY - 10, windowX + 425 + valuemodx, modY + 25, new Color(32, 31, 33).getRGB(), 0.5);
                    }
                }
                RenderUtil.drawBorderRect(windowX + 100 + valuemodx, modY - 10, windowX + 125 + valuemodx, modY + 25, new Color(37, 35, 39).getRGB(), 0.5);
                RenderUtil.drawBorderRect(windowX + 410 + valuemodx, modY - 10, windowX + 425 + valuemodx, modY + 25, new Color(39, 38, 42).getRGB(), 0.5);
                font.drawString(".", windowX + 416 + valuemodx, modY - 5, new Color(66, 64, 70).getRGB());
                font.drawString(".", windowX + 416 + valuemodx, modY - 1, new Color(66, 64, 70).getRGB());
                font.drawString(".", windowX + 416 + valuemodx, modY + 3, new Color(66, 64, 70).getRGB());

                if (m.isEnabled()) {
                    font.drawString(m.name, windowX + 140 + valuemodx, modY + 5, new Color(220, 220, 220).getRGB());
                    RenderUtil.drawBorderRect(windowX + 100 + valuemodx, modY - 10, windowX + 125 + valuemodx, modY + 25, new Color(41, 117, 221, (int) (m.optionAnimNow / 100f * 255)).getRGB(), 0.5);
                    //RenderUtil.draImage(windowX + 105 + valuemodx, modY, 16, 16, new ResourceLocation("client/vapeclickgui/module.png"), new Color(220, 220, 220), 0.5);
                    m.optionAnim = 100;

                    RenderUtil.drawBorderRect(windowX + 380 + valuemodx, modY + 2, windowX + 400 + valuemodx, modY + 12, 4, new Color(33, 94, 181, (int) (m.optionAnimNow / 100f * 255)).getRGB());
                    RenderUtil.drawCircle(windowX + 385 + 10 * m.optionAnimNow / 100 + valuemodx, modY + 7, 3.5f, new Color(255, 255, 255).getRGB());
                } else {
                    font.drawString(m.name, windowX + 140 + valuemodx, modY + 5, new Color(108, 109, 113).getRGB());
                    //RenderUtil.drawImage(windowX + 105 + valuemodx, modY, 16, 16, new ResourceLocation("client/vapeclickgui/module.png"), new Color(92, 90, 94));
                    m.optionAnim = 0;

                    RenderUtil.drawBorderRect(windowX + 380 + valuemodx, modY + 2, windowX + 400 + valuemodx, modY + 12, 4, new Color(59, 60, 65).getRGB());
                    RenderUtil.drawBorderRect(windowX + 381 + valuemodx, modY + 3, windowX + 399 + valuemodx, modY + 11, 3, new Color(29, 27, 31).getRGB());
                    RenderUtil.drawCircle(windowX + 385 + 10 * m.optionAnimNow / 100 + valuemodx, modY + 7, 3.5f, new Color(59, 60, 65).getRGB());
                }

                if (m.optionAnimNow != m.optionAnim) {
                    m.optionAnimNow += (m.optionAnim - m.optionAnimNow) / 20;
                }


                modY += 40;
            }
            //滚动
            int dWheel2 = Mouse.getDWheel();
            if (isHovered(windowX + 100 + valuemodx, windowY + 60, windowX + 425 + valuemodx, windowY + height, mouseX, mouseY)) {
                if (dWheel2 < 0 && Math.abs(modsRole) + 220 < (ModuleManager.getInstance().getModulesByCategory(modCategory).size() * 40)) {
                    modsRole -= 32;
                }
                if (dWheel2 > 0 && modsRole < 0) {
                    modsRole += 32;
                }
            }

            if (modsRoleNow != modsRole) {
                modsRoleNow += (modsRole - modsRoleNow) / 20;
                modsRoleNow = (int) modsRoleNow;
            }


            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        }
        int dWheel2 = Mouse.getDWheel();
        if (isHovered(windowX + 100 + valuemodx, windowY + 60, windowX + 425 + valuemodx, windowY + height, mouseX, mouseY)) {
            if (dWheel2 < 0 && Math.abs(modsRole) + 220 < (ModuleManager.getInstance().getModulesByCategory(modCategory).size() * 40)) {
                modsRole -= 16;
            }
            if (dWheel2 > 0 && modsRole < 0) {
                modsRole += 16;
            }
        }

        if (modsRoleNow != modsRole) {
            modsRoleNow += (modsRole - modsRoleNow) / 20;
            modsRoleNow = (int) modsRoleNow;
        }

    }

    public int findArray(float[] a, float b) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] == b) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

        //顶部图标
        float typeX = windowX + 20;
        for (Enum<?> e : ClickType.values()) {
            if (e != ClickType.Settings) {
                if (e == selectType) {
                    if (isHovered(typeX, windowY + 10, typeX + 16 + font.getStringWidth(e.name() + " "), windowY + 10 + 16, mouseX, mouseY)) {
                        selectType = (ClickType) e;
                    }
                    typeX += (32 + font.getStringWidth(e.name() + " "));
                } else {
                    if (isHovered(typeX, windowY + 10, typeX + 16, windowY + 10 + 16, mouseX, mouseY)) {
                        selectType = (ClickType) e;
                    }
                    typeX += (32);
                }
            } else {
                if (isHovered(windowX + width - 32, windowY + 10, windowX + width, windowY + 10 + 16, mouseX, mouseY)) {
                    selectType = (ClickType) e;
                }
            }
        }

        if (selectType == ClickType.Home) {
            //类型列表
            float cateY = windowY + 65;
            for (Category m : Category.values()) {

                if (isHovered(windowX, cateY - 8, windowX + 50, cateY + font.getHeight() + 8, mouseX, mouseY)) {
                    if (modCategory != m) {
                        modsRole = 0;
                    }

                    modCategory = m;
                    for (ModuleBase mod : ModuleManager.getInstance().moduleList){
                        mod.optionAnim = 0;
                        mod.optionAnimNow = 0;
                    }
                }

                cateY += 25;
            }

        }


    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if(!closed && keyCode == Keyboard.KEY_ESCAPE){
            close = true;
            mc.mouseHelper.grabMouseCursor();
            mc.inGameHasFocus = true;
            return;
        }

        if(close) {
            this.mc.displayGuiScreen((GuiScreen) null);
        }

        try {
            super.keyTyped(typedChar, keyCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }

    @Override
    public void onGuiClosed(){

    }
}
