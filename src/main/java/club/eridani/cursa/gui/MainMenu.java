package club.eridani.cursa.gui;

import club.eridani.cursa.client.FontManager;
import club.eridani.cursa.gui.particle.ParticleManager;
import club.eridani.cursa.utils.AnimationUtils;
import club.eridani.cursa.utils.ClickUtils;
import club.eridani.cursa.utils.RenderUtil;
import net.minecraft.client.gui.*;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.LinkedList;
import java.util.List;

public class MainMenu extends GuiScreen {

    private ResourceLocation background;
    private int animatedX, animatedY;
    private List<CustomButton> buttons;
    private ParticleManager pm;

    public MainMenu() {
        background = new ResourceLocation("orangette/background/mainmenu.png");
    }

    @Override
    public void initGui() {
        buttons = new LinkedList<>();
        pm = new ParticleManager();
        buttons.add(new CustomButton("SinglePlayer", new ResourceLocation("orangette/icon/singleplayer.png"), new GuiWorldSelection(this)));
        buttons.add(new CustomButton("MultiPlayer", new ResourceLocation("orangette/icon/multiplayer.png"), new GuiMultiplayer(this)));
        buttons.add(new CustomButton("Language", new ResourceLocation("orangette/icon/language.png"), new GuiLanguage(this, mc.gameSettings, mc.getLanguageManager())));
        buttons.add(new CustomButton("Settings", new ResourceLocation("orangette/icon/setting.png"), new GuiOptions(this, mc.gameSettings)));
        buttons.add(new CustomButton("AltManager", new ResourceLocation("orangette/icon/altmanager.png"), null/*GuiAltManager.instance*/));
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        mc.getTextureManager().bindTexture(background);
        this.drawModalRectWithCustomSizedTexture(-this.animatedX/4, -this.animatedY/3, 0, 0, sr.getScaledWidth()/3*4, sr.getScaledHeight()/3*4, sr.getScaledWidth()/3*4, sr.getScaledHeight()/3*4);
        //mc.getTextureManager().bindTexture(new ResourceLocation("orangette/logo.png"));
        //Gui.drawModalRectWithCustomSizedTexture(0, 0, 0F, 0F, 125, 49, 125, 49);
        int xOffset = sr.getScaledWidth()/2-180;
        for(CustomButton cb : buttons) {
            cb.drawScreen(xOffset, sr.getScaledHeight()/2-20, mouseX, mouseY);
            xOffset += 80;
        }
        RenderUtil.drawCircle(0, 0, 5, -1);
        FontManager.jelloLargeFont.drawString("Changelog", 4, 4, 0xc0ffffff);
        FontManager.jelloLargeFont.drawString("By Orangette Team", sr.getScaledWidth()-FontManager.jelloLargeFont.getStringWidth("By Orangette Team")-4, sr.getScaledHeight()-12, 0xd0ffffff);
        super.drawScreen(mouseX, mouseY, partialTicks);
        pm.render(mouseX, mouseY, sr);
        animatedX += ((mouseX-animatedX) / 1.8) + 0.1;
        animatedY += ((mouseY-animatedY) / 1.8) + 0.1;
    }

    private class CustomButton {

        private final ResourceLocation resource;
        private final GuiScreen parent;
        private float animatedSize;
        private int posX, posY;
        private final String name;

        public CustomButton(String name, ResourceLocation resource, GuiScreen parent) {
            this.resource = resource;
            this.parent = parent;
            this.name = name;
        }

        public void drawScreen(int posX, int posY, int mouseX, int mouseY) {
            if(ClickUtils.isMouseHovering(posX, posY, 48, 48, mouseX, mouseY)) {
                animatedSize = AnimationUtils.animate(animatedSize, 30);
                FontManager.jelloFont.drawCenteredString(name, posX+25, posY+60, -1);
            }
            else animatedSize = AnimationUtils.animate(animatedSize, 25);
            GL11.glColor4f(1,1, 1,0.75f);
            mc.getTextureManager().bindTexture(resource);
            Gui.drawModalRectWithCustomSizedTexture(posX-(int)animatedSize/2+25, posY-(int)animatedSize/2+25, 0, 0, (int)(animatedSize*1.5f), (int) (animatedSize*1.5f), animatedSize*1.5f, animatedSize*1.5f);
            this.posX = posX;
            this.posY = posY;
        }

        public void onClicked(int mouseX, int mouseY, int mouseButton) {
            if(ClickUtils.isMouseHovering(posX, posY, 48, 48, mouseX, mouseY)) {
                mc.displayGuiScreen(parent);
            }
        }
    }

}
