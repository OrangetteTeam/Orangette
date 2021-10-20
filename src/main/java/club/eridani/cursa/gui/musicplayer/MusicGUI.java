package club.eridani.cursa.gui.musicplayer;

import club.eridani.cursa.Cursa;
import club.eridani.cursa.client.FontManager;
import club.eridani.cursa.utils.ColorUtil;
import club.eridani.cursa.utils.MusicUtil;
import club.eridani.cursa.utils.RenderUtil;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MusicGUI extends GuiScreen {
    private static CopyOnWriteArrayList<SearchResultComponent> tracks = new CopyOnWriteArrayList<>();
    private static TextField textField = new TextField(180, 10);
    private static SearchModeComponent searchMode;
    private static float scroll, tScroll = 0;
    private float windowX, windowY, windowWidth, windowHeight;
    private float scale, target;
    private float transparency;
    private boolean closing;
    private int textBoxX, textBoxY;
    private boolean searched, searching;
    private int searchTicks;
    private String lastSearched;

    @Override
    public void initGui() {
        if (Minecraft.getMinecraft().entityRenderer.getShaderGroup() != null)
            Minecraft.getMinecraft().entityRenderer.getShaderGroup().deleteShaderGroup();
        Minecraft.getMinecraft().entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));

        searching = false;
        lastSearched = "";
        searched = true;
        searchTicks = 0;
        scale = 10.0F;
        target = 1.0F;
        transparency = 0;
    }

    @Override
    public void onGuiClosed() {
        if (Minecraft.getMinecraft().entityRenderer.getShaderGroup() != null)
            Minecraft.getMinecraft().entityRenderer.getShaderGroup().deleteShaderGroup();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float ticks) {
        ScaledResolution sr = new ScaledResolution(mc);
        GlStateManager.translate(sr.getScaledWidth() / 2, sr.getScaledHeight() / 2, 0);
        GlStateManager.scale(scale, scale, 0);
        GlStateManager.translate(-sr.getScaledWidth() / 2, -sr.getScaledHeight() / 2, 0);
        scale = smoothTrans(scale, target);
        windowWidth = 300;
        windowHeight = 200;
        windowX = getCenter(0, sr.getScaledWidth(), windowWidth);
        windowY = getCenter(0, sr.getScaledHeight(), windowHeight);
        int t = closing ? 0 : 130;
        transparency = smoothTrans(transparency, t);
        //base
        RenderUtil.drawRect(windowX, windowY, windowX + windowWidth + 3, windowY + windowHeight, ColorUtil.toRGBA(250, 250, 250, 255));
        //musicplayer
        FontManager.jelloLargeFont.drawString("MusicPlayer", windowX + 5, windowY + 20, ColorUtil.toRGBA(0, 0, 0, 255), false);
        //textbox
        textBoxX = (int) (windowX + 90);
        textBoxY = (int) (windowY + 25);
        textField.renderTextBox(textBoxX, textBoxY);
        //tracklist
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtil.scissor(windowX + 95, windowY + 40, windowWidth - 90, windowHeight - 40, scale);
        AtomicInteger h = new AtomicInteger((int) (textBoxY + 20));
        tracks.forEach(c -> {
            h.updateAndGet(he -> he + c.doRender((int) textBoxX + 10, (int) (he + scroll), mouseX, mouseY));
        });
        RenderUtil.scissor(windowX + 81, windowY + 20, windowWidth - 81, windowHeight - 20, scale);
        if (searching)
            RenderUtil.drawRect(windowX, windowY, windowX + windowWidth, windowY + windowHeight, ColorUtil.toRGBA(255, 255, 255, 120));
        int trackListX = textBoxX + 10;
        int trackListY = textBoxY + 20;
        int trackListWidth = 200;
        int trackListHeight = h.get() - (textBoxY + 20);
        if (isMouseHovering(mouseX, mouseY, trackListX, trackListY, trackListWidth, trackListHeight)) {
            int dWheel = Mouse.getDWheel();
            if (dWheel < 0) tScroll -= 15;
            else if (dWheel > 0) tScroll += 15;
        }
        scroll = smoothTrans(scroll, tScroll);
        if (tScroll > 0) tScroll = 0;
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        //searchmode
        float modeX = windowX;
        float modeY = windowY + FontManager.jelloLargeFont.getHeight() + 20;
        float modeWidth = 80;
        float modeHeight = windowHeight - (FontManager.jelloLargeFont.getHeight() + 20);
        if (Objects.isNull(searchMode))
            searchMode = new SearchModeComponent(modeWidth, modeHeight);
        searchMode.doRender((int) modeX, (int) modeY, mouseX, mouseY);
        RenderUtil.drawRect(modeX + modeWidth, windowY, modeX + modeWidth + 1, modeY + modeHeight, ColorUtil.toRGBA(215, 226, 235, 255));
        //component
        float panelX = windowX - 1;
        float panelY = windowY + windowHeight - 20;
        float panelWidth = windowWidth + 4;
        float panelHeight = 20;
        //bottom component
        RenderUtil.drawRect(panelX, panelY, panelX + panelWidth, panelY + panelHeight, ColorUtil.toRGBA(15, 22, 38, 255));
        //progressbar
        RenderUtil.drawRect(panelX, panelY, panelX + panelWidth, panelY + 1.5F, ColorUtil.toRGBA(84, 91, 101, 255));
        RenderUtil.drawRect(panelX, panelY, panelX + panelWidth * MusicUtil.getProgress(), panelY + 1.5F, ColorUtil.toRGBA(108, 116, 247, 255));
        searchTicks += 1;
        if (searchTicks > Minecraft.getDebugFPS() && !searched) {
            searchTicks = 0;
            searched = true;
            search(searchMode.isYoutube());
        }

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        tracks.forEach(t -> {
            if (t.y - scroll < windowY + windowHeight - 20)
                t.mouseClicked(mouseX, mouseY, mouseButton);
        });
        searchMode.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(null);
            return;
        }
        textField.textboxKeyTyped(typedChar, keyCode);
        searchTicks = 0;
        searched = false;
    }

    public float getCenter(float a, float b, float c) {
        return a + (b - c) / 2;
    }

    public float smoothTrans(double current, double last) {
        return (float) (current * Minecraft.getMinecraft().timer.renderPartialTicks + (last * (1.0f - Minecraft.getMinecraft().timer.renderPartialTicks)));
    }

    public Boolean isMouseHovering(float mouseX, float mouseY, float cx, float cy, float cw, float ch) {
        return cx < mouseX && cx + cw > mouseX && cy < mouseY && cy + ch > mouseY;
    }

    private void search(boolean youtube) {
        String text = textField.getText();
        if (text.isEmpty() || text.equals(lastSearched)) return;
        searching = true;
        AudioLoadResultHandler handler = new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                searching = false;
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                searching = false;
                tScroll = 0;
                tracks.clear();
                playlist.getTracks().forEach(t -> tracks.add(new SearchResultComponent(t)));
            }

            @Override
            public void noMatches() {
                searching = false;
                Cursa.log.info("No Matches!");
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                searching = false;
                exception.printStackTrace();
            }
        };

        if (youtube) MusicUtil.searchYT(text, handler);
        else MusicUtil.searchSC(text, handler);
        lastSearched = text;
    }
}
