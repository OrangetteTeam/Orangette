package club.eridani.cursa.module.modules.misc;

import club.eridani.cursa.client.RPCManager;
import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;

@Module(name = "DiscordRPC" , category = Category.MISC)
public class DiscordRPC extends ModuleBase {
    @Override
    public void onEnable(){
        RPCManager.INSTANCE.enable();
    }

    @Override
    public void onDisable(){
        RPCManager.INSTANCE.disable();
    }
}
