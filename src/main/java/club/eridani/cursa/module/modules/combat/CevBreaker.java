package club.eridani.cursa.module.modules.combat;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.notification.NotificationManager;
import club.eridani.cursa.setting.Setting;
import club.eridani.cursa.utils.BlockInteractionHelper;
import club.eridani.cursa.utils.EntityUtil;
import club.eridani.cursa.utils.InventoryUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

@Module(name = "CevBreaker", category = Category.COMBAT)
public class CevBreaker extends ModuleBase {


    int ob, crystal;
    EntityPlayer enemy;

    @Override
    public void onEnable() {
        enemy = EntityUtil.getNearestPlayer();

    }

    @Override

    public void onTick() {
        if (enemy == null) {
            error("Don't have a target! disabling");
            disable();
            return;
        }

        BlockPos[] block = new BlockPos[]{};
        block = new BlockPos[]{
                new BlockPos(0, 0, 1),
                new BlockPos(0, 1, 1),
                new BlockPos(0, 2, 1),
                new BlockPos(0, 2, 0)
        };

        if (nullCheck()) disable();
        BlockPos pos = new BlockPos(enemy);


        ob = InventoryUtil.getBlockHotbar(Blocks.OBSIDIAN);//黒曜石をHotbarから探す
        if (ob == -1) {
            error("Cannot find obsidian! disabling");
            return;
        }
        mc.player.inventory.currentItem = ob;//上で探したものに切り替え
        mc.playerController.updateController();//操作を更新

        for (BlockPos add : block) {
            BlockInteractionHelper.placeBlock(pos.add(add), false);//Block設置

        }
        crystal = InventoryUtil.getItemHotbar(Items.END_CRYSTAL);
        mc.player.inventory.currentItem = crystal;
        if (crystal == -1) {
            error("Cannot find crystal disabling");
            return;
        }
        BlockInteractionHelper.placeBlock(pos.add(0, 3, 0), false);

        int pick;

        pick = InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE);//ピッケルを探す
        if (pick == -1) {
            error("Cannot find PickAxe! disabling");
        }
        mc.player.inventory.currentItem = pick;//上で探したものに切り替え
        mc.playerController.updateController();//操作を更新

        mc.playerController.onPlayerDamageBlock(pos.add(0, 2, 0), EnumFacing.UP);

        {
            int i = 0;
            for (Entity target : mc.world.loadedEntityList) {
                if (target instanceof EntityEnderCrystal) {
                    mc.playerController.attackEntity(mc.player, target);
                    i++;

                }

            }

        }

    }

    public void error(String msg) {
        NotificationManager.error(msg);
        disable();
    }

}