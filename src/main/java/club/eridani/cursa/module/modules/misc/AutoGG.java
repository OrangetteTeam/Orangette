package club.eridani.cursa.module.modules.misc;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.common.annotations.Parallel;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;

@Parallel
@Module(name = "AutoEzzz", category = Category.MISC)
public class AutoGG extends ModuleBase {

    @Override
    public void onTick() {
            Minecraft mc = Minecraft.getMinecraft();
            EntityLivingBase enimy = mc.player.getLastAttackedEntity();
            if (enimy.isDead) {
                mc.player.sendChatMessage("Ezzz " + enimy.getName() + "is an idiot" + "Orangette on Top!!");

            }
        }
    }

