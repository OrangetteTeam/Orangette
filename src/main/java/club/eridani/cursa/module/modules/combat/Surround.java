package club.eridani.cursa.module.modules.combat;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.concurrent.event.Listener;
import club.eridani.cursa.event.events.client.KeyEvent;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;
import club.eridani.cursa.utils.BlockInteractionHelper;
import club.eridani.cursa.utils.EntityUtil;
import club.eridani.cursa.utils.InventoryUtil;
import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Module(name = "Surround", category = Category.COMBAT)
public class Surround extends ModuleBase {

    int ob;
    EntityPlayer entity;


    @Override
    public void onTick() {
        {
            BlockPos[] block = new BlockPos[]{};
            block = new BlockPos[]{
                    new BlockPos(1, 0, 0),
                    new BlockPos(-1, 0, 0),
                    new BlockPos(0, 0, 1),
                    new BlockPos(0, 0, -1),

                    new BlockPos(1, -1, 0),
                    new BlockPos(-1, -1, 0),
                    new BlockPos(0, -1, 1),
                    new BlockPos(0, -1, -1),

                    new BlockPos(0, -1, 0)
            };

            {

                entity = mc.player;
                if (entity == null) disable();
                BlockPos pos = new BlockPos(entity);

                mc.player.setPosition(pos.getX() + .5, pos.getY(), pos.getZ() + 0.5);

                ob = InventoryUtil.getBlockHotbar(Blocks.OBSIDIAN);//黒曜石をHotbarから探す
                if (ob == -1) {
                    disable();
                    return;
                }
                mc.player.inventory.currentItem = ob;//上で探したものに切り替え
                mc.playerController.updateController();//操作を更新
                for (BlockPos add : block) {

                    BlockInteractionHelper.placeBlock(pos.add(add), false);//Block設置
                }
            }
        }
    }

    @Listener
    public void onKeyEvent(KeyEvent event) {
        if (event.getKey() == mc.gameSettings.keyBindJump.getKeyCode()) {
            disable();
        }
    }
}