package club.eridani.cursa.module.modules.misc;

import club.eridani.cursa.Cursa;
import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

@Module(name = "Spammer", category = Category.MISC)
public class Spammer extends ModuleBase {

    public Setting<Double> delay = setting("Delay", 3.0, 0.1, 10.0);
    Random rnd = new Random();
    String str = "";

    @Override
    public void onEnable() {
        try {
            Scanner scanner = new Scanner(new File("Orangette/config/Orangette_Spam.txt"));
            while (scanner.hasNext()) {
                str = scanner.next();
            }
        } catch (FileNotFoundException e) {
            Cursa.log.info("Error while loading spammer text");
            e.printStackTrace();
            disable();
        }
    }

    @Override
    public void onTick() {
        int r1 = rnd.nextInt(100);
        if (mc.player.ticksExisted * delay.getValue() == 0) {
            mc.player.sendChatMessage(str + " " + r1);
        }
    }

}

