package club.eridani.cursa.gui.musicplayer;

import club.eridani.cursa.client.FontManager;
import club.eridani.cursa.utils.ColorUtil;
import club.eridani.cursa.utils.RenderUtil;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.math.MathHelper;

public class TextField {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public int width;
    public int height;

    public String text = "";
    public int maxStringLength = 30;
    public int lineScrollOffset;
    public int cursorPosition;
    public int selectionEnd;
    public Predicate<String> validator = Predicates.<String>alwaysTrue();

    public TextField(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void renderTextBox(int x, int y) {
        int i = ColorUtil.toRGBA(0, 0, 0, 255);
        int j = this.cursorPosition - this.lineScrollOffset;
        int k = this.selectionEnd - this.lineScrollOffset;
        String s = mc.fontRenderer.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
        boolean flag = j >= 0 && j <= s.length();
        boolean flag1 = flag;
        int l = x;
        int i1 = y;
        int j1 = l;

        if (k > s.length()) {
            k = s.length();
        }

        if (!s.isEmpty()) {
            String s1 = flag ? s.substring(0, j) : s;
            j1 = mc.fontRenderer.drawString(s1, l, i1, i);
        }

        boolean flag2 = this.cursorPosition < this.text.length() || this.text.length() >= maxStringLength;
        int k1 = j1;

        if (!flag) {
            k1 = j > 0 ? l + this.width : l;
        } else if (flag2) {
            k1 = j1 - 1;
            --j1;
        }

        if (!s.isEmpty() && flag && j < s.length()) {
            j1 = mc.fontRenderer.drawString(s.substring(j), j1, i1, i);
        }

        if (flag1) {
            if (flag2) {
                Gui.drawRect(k1, i1 - 1, k1 + 1, i1 + 1 + mc.fontRenderer.FONT_HEIGHT, -3092272);
            } else {
                //mc.fontRenderer.drawString("_", k1, i1, i);
            }
        }

        if (k != j) {
            int l1 = l + mc.fontRenderer.getStringWidth(s.substring(0, k));
            this.drawSelectionBox(x, y, k1, i1 - 1, l1 - 1, i1 + 1 + mc.fontRenderer.FONT_HEIGHT);
        }

        StringBuilder builder = new StringBuilder();
        for (int o = 0; o < maxStringLength; o++) builder.append("A");
        int jj = mc.fontRenderer.getStringWidth(builder.toString());
        int a = y + mc.fontRenderer.FONT_HEIGHT + 2;
        RenderUtil.drawRect(x - 3, a, x + jj + 3, a + 1, ColorUtil.toRGBA(215, 226, 235, 255));
    }

    private void drawSelectionBox(int x, int y, int startX, int startY, int endX, int endY) {
        if (startX < endX) {
            int i = startX;
            startX = endX;
            endX = i;
        }

        if (startY < endY) {
            int j = startY;
            startY = endY;
            endY = j;
        }

        if (endX > x + this.width) {
            endX = x + this.width;
        }

        if (startX > x + this.width) {
            startX = x + this.width;
        }

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.color(0.0F, 0.0F, 255.0F, 255.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.enableColorLogic();
        GlStateManager.colorLogicOp(GlStateManager.LogicOp.OR_REVERSE);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos((double) startX, (double) endY, 0.0D).endVertex();
        bufferbuilder.pos((double) endX, (double) endY, 0.0D).endVertex();
        bufferbuilder.pos((double) endX, (double) startY, 0.0D).endVertex();
        bufferbuilder.pos((double) startX, (double) startY, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.disableColorLogic();
        GlStateManager.enableTexture2D();
    }

    public void setText(String textIn) {
        if (this.validator.apply(textIn)) {
            if (textIn.length() > this.maxStringLength) {
                this.text = textIn.substring(0, this.maxStringLength);
            } else {
                this.text = textIn;
            }

            this.setCursorPositionEnd();
        }
    }

    public String getText() {
        return this.text;
    }

    public String getSelectedText() {
        int i = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
        int j = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
        return this.text.substring(i, j);
    }

    public void setValidator(Predicate<String> theValidator) {
        this.validator = theValidator;
    }

    public void writeText(String textToWrite) {
        String s = "";
        String s1 = ChatAllowedCharacters.filterAllowedCharacters(textToWrite);
        int i = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
        int j = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
        int k = this.maxStringLength - this.text.length() - (i - j);

        if (!this.text.isEmpty()) {
            s = s + this.text.substring(0, i);
        }

        int l;

        if (k < s1.length()) {
            s = s + s1.substring(0, k);
            l = k;
        } else {
            s = s + s1;
            l = s1.length();
        }

        if (!this.text.isEmpty() && j < this.text.length()) {
            s = s + this.text.substring(j);
        }

        if (this.validator.apply(s)) {
            this.text = s;
            this.moveCursorBy(i - this.selectionEnd + l);
        }
    }

    public void deleteWords(int num) {
        if (!this.text.isEmpty()) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            } else {
                this.deleteFromCursor(this.getNthWordFromCursor(num) - this.cursorPosition);
            }
        }
    }

    public void deleteFromCursor(int num) {
        if (!this.text.isEmpty()) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            } else {
                boolean flag = num < 0;
                int i = flag ? this.cursorPosition + num : this.cursorPosition;
                int j = flag ? this.cursorPosition : this.cursorPosition + num;
                String s = "";

                if (i >= 0) {
                    s = this.text.substring(0, i);
                }

                if (j < this.text.length()) {
                    s = s + this.text.substring(j);
                }

                if (this.validator.apply(s)) {
                    this.text = s;

                    if (flag) {
                        this.moveCursorBy(num);
                    }
                }
            }
        }
    }

    public int getNthWordFromCursor(int numWords) {
        return this.getNthWordFromPos(numWords, this.getCursorPosition());
    }

    public int getNthWordFromPos(int n, int pos) {
        return this.getNthWordFromPosWS(n, pos, true);
    }

    public int getNthWordFromPosWS(int n, int pos, boolean skipWs) {
        int i = pos;
        boolean flag = n < 0;
        int j = Math.abs(n);

        for (int k = 0; k < j; ++k) {
            if (!flag) {
                int l = this.text.length();
                i = this.text.indexOf(32, i);

                if (i == -1) {
                    i = l;
                } else {
                    while (skipWs && i < l && this.text.charAt(i) == ' ') {
                        ++i;
                    }
                }
            } else {
                while (skipWs && i > 0 && this.text.charAt(i - 1) == ' ') {
                    --i;
                }

                while (i > 0 && this.text.charAt(i - 1) != ' ') {
                    --i;
                }
            }
        }

        return i;
    }

    public void moveCursorBy(int num) {
        this.setCursorPosition(this.selectionEnd + num);
    }

    public void setCursorPosition(int pos) {
        this.cursorPosition = pos;
        int i = this.text.length();
        this.cursorPosition = MathHelper.clamp(this.cursorPosition, 0, i);
        this.setSelectionPos(this.cursorPosition);
    }

    public void setCursorPositionZero() {
        this.setCursorPosition(0);
    }

    public void setCursorPositionEnd() {
        this.setCursorPosition(this.text.length());
    }

    public boolean textboxKeyTyped(char typedChar, int keyCode) {

        if (GuiScreen.isKeyComboCtrlA(keyCode)) {
            this.setCursorPositionEnd();
            this.setSelectionPos(0);
            return true;
        } else if (GuiScreen.isKeyComboCtrlC(keyCode)) {
            GuiScreen.setClipboardString(this.getSelectedText());
            return true;
        } else if (GuiScreen.isKeyComboCtrlV(keyCode)) {
            this.writeText(GuiScreen.getClipboardString());
            return true;
        } else if (GuiScreen.isKeyComboCtrlX(keyCode)) {
            GuiScreen.setClipboardString(this.getSelectedText());
            this.writeText("");

            return true;
        } else {
            switch (keyCode) {
                case 14:

                    if (GuiScreen.isCtrlKeyDown()) {
                        this.deleteWords(-1);

                    } else {
                        this.deleteFromCursor(-1);
                    }

                    return true;
                case 199:

                    if (GuiScreen.isShiftKeyDown()) {
                        this.setSelectionPos(0);
                    } else {
                        this.setCursorPositionZero();
                    }

                    return true;
                case 203:

                    if (GuiScreen.isShiftKeyDown()) {
                        if (GuiScreen.isCtrlKeyDown()) {
                            this.setSelectionPos(this.getNthWordFromPos(-1, this.getSelectionEnd()));
                        } else {
                            this.setSelectionPos(this.getSelectionEnd() - 1);
                        }
                    } else if (GuiScreen.isCtrlKeyDown()) {
                        this.setCursorPosition(this.getNthWordFromCursor(-1));
                    } else {
                        this.moveCursorBy(-1);
                    }

                    return true;
                case 205:

                    if (GuiScreen.isShiftKeyDown()) {
                        if (GuiScreen.isCtrlKeyDown()) {
                            this.setSelectionPos(this.getNthWordFromPos(1, this.getSelectionEnd()));
                        } else {
                            this.setSelectionPos(this.getSelectionEnd() + 1);
                        }
                    } else if (GuiScreen.isCtrlKeyDown()) {
                        this.setCursorPosition(this.getNthWordFromCursor(1));
                    } else {
                        this.moveCursorBy(1);
                    }

                    return true;
                case 207:

                    if (GuiScreen.isShiftKeyDown()) {
                        this.setSelectionPos(this.text.length());
                    } else {
                        this.setCursorPositionEnd();
                    }

                    return true;
                case 211:

                    if (GuiScreen.isCtrlKeyDown()) {
                        this.deleteWords(1);

                    } else {
                        this.deleteFromCursor(1);
                    }

                    return true;
                default:

                    if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
                        this.writeText(Character.toString(typedChar));
                        return true;
                    } else {
                        return false;
                    }
            }
        }
    }

    public void setSelectionPos(int position) {
        int i = this.text.length();

        if (position > i) {
            position = i;
        }

        if (position < 0) {
            position = 0;
        }

        this.selectionEnd = position;

        if (this.lineScrollOffset > i) {
            this.lineScrollOffset = i;
        }

        int j = this.getWidth();
        FontRenderer fontRenderer;
        String s = trimStringToWidth(this.text.substring(this.lineScrollOffset), j);
        int k = s.length() + this.lineScrollOffset;

        if (position == this.lineScrollOffset) {
            this.lineScrollOffset -= trimStringToWidth(this.text, j, true).length();
        }

        if (position > k) {
            this.lineScrollOffset += position - k;
        } else if (position <= this.lineScrollOffset) {
            this.lineScrollOffset -= this.lineScrollOffset - position;
        }

        this.lineScrollOffset = MathHelper.clamp(this.lineScrollOffset, 0, i);
    }

    public int getSelectionEnd() {
        return this.selectionEnd;
    }

    public int getCursorPosition() {
        return this.cursorPosition;
    }

    public int getWidth() {
        return this.width;
    }

    public String trimStringToWidth(String text, int width) {
        return this.trimStringToWidth(text, width, false);
    }

    public String trimStringToWidth(String text, int width, boolean reverse) {
        StringBuilder stringbuilder = new StringBuilder();
        int i = 0;
        int j = reverse ? text.length() - 1 : 0;
        int k = reverse ? -1 : 1;
        boolean flag = false;
        boolean flag1 = false;

        for (int l = j; l >= 0 && l < text.length() && i < width; l += k) {
            char c0 = text.charAt(l);
            int i1 = mc.fontRenderer.getStringWidth(Character.toString(c0));

            if (flag) {
                flag = false;

                if (c0 != 'l' && c0 != 'L') {
                    if (c0 == 'r' || c0 == 'R') {
                        flag1 = false;
                    }
                } else {
                    flag1 = true;
                }
            } else if (i1 < 0) {
                flag = true;
            } else {
                i += i1;

                if (flag1) {
                    ++i;
                }
            }

            if (i > width) {
                break;
            }

            if (reverse) {
                stringbuilder.insert(0, c0);
            } else {
                stringbuilder.append(c0);
            }
        }

        return stringbuilder.toString();
    }
}
