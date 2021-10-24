package club.eridani.cursa.module.modules.combat;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;
import club.eridani.cursa.utils.BlockInteractionHelper;
import club.eridani.cursa.utils.EntityUtil;
import club.eridani.cursa.utils.InventoryUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

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

                ob = InventoryUtil.getBlockHotbar(Blocks.OBSIDIAN);//黒曜石をHotbarから探す
                mc.player.inventory.currentItem = ob;//上で探したものに切り替え
                mc.playerController.updateController();//操作を更新
                for (BlockPos add : block) {

                    BlockInteractionHelper.placeBlock(pos.add(add), false);//Block設置
                }
            }
        }
    }
}