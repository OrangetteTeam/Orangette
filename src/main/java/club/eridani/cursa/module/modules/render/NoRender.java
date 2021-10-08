package club.eridani.cursa.module.modules.render;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.client.renderer.entity.RenderFallingBlock;
import net.minecraft.client.renderer.entity.RenderItemFrame;
import net.minecraft.client.renderer.entity.RenderXPOrb;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Module(name = "NoRender" , category = Category.RENDER)
public class NoRender extends ModuleBase {
    public static NoRender INSTANCE;
    public Setting<Boolean> overlay = setting("Overlay" , true);
    public Setting<Boolean> fog = setting("Fog" , true);
    public Setting<Boolean> armor = setting("Armor" , true);
    public Setting<Boolean> arrow = setting("Arrow" , true);
    public Setting<Boolean> xp = setting("XP" , true);
    public Setting<Boolean> fallingBlock = setting("FallingBlock" , true);
    public Setting<Boolean> map = setting("Map" , true);
    public Setting<Boolean> frame = setting("ItemFrame" , true);

    public NoRender(){
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onRenderBlockOverlay(RenderBlockOverlayEvent event){
        if(overlay.getValue()) event.setCanceled(true);
    }
}
