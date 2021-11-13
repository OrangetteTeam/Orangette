package club.eridani.cursa.gui.clickgui.sigma;

import club.eridani.cursa.client.FontManager;
import club.eridani.cursa.client.ModuleManager;
import club.eridani.cursa.gui.clickgui.sigma.component.ModuleButton;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.utils.ColorUtil;
import club.eridani.cursa.utils.RenderUtil;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class Panel extends Component {
    private float offset, targetOffset;
    public float moduleHeight;
    private List<ModuleButton> buttons = new ArrayList<>();
    private Category category;
    private int dX , dY;
    private boolean dragging;

    public Panel(Category category, int x, int y) {
        this.category = category;
        this.x = x;
        this.y = y;
        this.width = 105;
        this.height = 27;
        ModuleManager.getInstance().getModulesByCategory(category)
                .forEach(m -> buttons.add(new ModuleButton(m)));
    }

    public void render(int mouseX, int mouseY, float scale, boolean doMouseAction) {
        int color = ColorUtil.toRGBA(240, 240, 240, 255);
        int fColor = ColorUtil.toRGBA(49, 49, 49);
        RenderUtil.drawShader(x, y, x + width, y + this.height + 200);
        RenderUtil.drawRect(x, y, x + width, y + height, color);
        FontManager.jelloLargeFont.drawString(getName(category.name()), x + 5, getCenter(y, height, FontManager.jelloLargeFont.getHeight()) + 1, fColor);
        RenderUtil.drawRect(x, y + height, x + width, y + height + 200, ColorUtil.toRGBA(250, 250, 250, 255));

        RenderUtil.scissor(x, y + height, width, 200, scale);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        AtomicInteger height = new AtomicInteger(this.y + this.height);
        buttons.forEach(b -> {
            height.updateAndGet(h -> h + b.render(x, (int) (h + offset), mouseX, mouseY, doMouseAction));
        });
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        if(doMouseAction) {
            if(dragging) {
                this.x = mouseX + dX;
                this.y = mouseY + dY;
            }

            if (isMouseHovering(mouseX, mouseY, x, y, width, this.height + 200)) {
                int dWheel = Mouse.getDWheel();
                if (dWheel < 0) targetOffset -= 15;
                else if (dWheel > 0) targetOffset += 15;
            }
        }
        offset += (targetOffset - offset) * 0.3;
        if (targetOffset > 0) targetOffset = 0;
        int a = height.get() - 200 < 0 ? 0 : height.get() - 200;
        if (a < targetOffset) targetOffset = a;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        execute(p -> p.mouseClicked(mouseX, mouseY, mouseButton));
        if(isMouseHovering(mouseX , mouseY)) {
            dX = x - mouseX;
            dY = y - mouseY;
            dragging = true;
            SigmaGui.INSTANCE.swap(this);
        }
    }

    @Override
    public void mouseReleased(int mouseX , int mouseY, int mouseButton) {
        dragging = false;
    }

    private void execute(Consumer<? super ModuleButton> t) {
        buttons.forEach(t);
    }
}
