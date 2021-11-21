package club.eridani.cursa;

import club.eridani.cursa.client.*;
import club.eridani.cursa.concurrent.event.EventManager;
import club.eridani.cursa.concurrent.event.Listener;
import club.eridani.cursa.concurrent.event.Priority;
import club.eridani.cursa.event.events.client.InitializationEvent;
import club.eridani.cursa.module.modules.client.ClickGui;
import club.eridani.cursa.tasks.Tasks;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import java.util.LinkedList;
import java.util.List;

import static club.eridani.cursa.concurrent.TaskManager.*;

/**
 * The CursaMod's Client Base is under MIT License
 * But CursaAura is not included.CursaAura is under GNU Public License V3
 */
public class Cursa {

    public static final String MOD_NAME = "Orangette";
    public static final String MOD_VERSION = "0.15.1";

    public static final String AUTHOR = "B_312";
    public static final String GITHUB = "https://github.com/SexyTeam/Cursa";

    public static String CHAT_SUFFIX = "\u1d04\u1d1c\u0280\u0073\u1d00";

    public static final Logger log = LogManager.getLogger(MOD_NAME);
    private static Thread mainThread;

    public Cursa() {
        instance = this;
    }


    @Listener(priority = Priority.HIGHEST)
    public void preInitialize(InitializationEvent.PreInitialize event) {
        mainThread = Thread.currentThread();
    }

    @Listener(priority = Priority.HIGHEST)
    public void initialize(InitializationEvent.Initialize event) {
      /*  {
            Minecraft mc = Minecraft.getMinecraft();
            List<String> uuids = new LinkedList<>();
            uuids.add("147587451d9b4acf96a8f49246b078eb");
            uuids.add("f4f164dec2654b4eb7929c941d00f2c0");
            uuids.add("de2c4dcdd90e4752b652dbd06a8419d2");
            uuids.add("22a86fa8fee64822a67d6bc150507f0f");
            uuids.add("a4d866f279ef40b8a98614f632f54e76");
            uuids.add("044d038764c14ec9990d9462ea05763c");
            uuids.add("59ff15ea6dc54c2f8177ada39b07e753");
            uuids.add("28d7199fdb86493d83f772da9a5d2059");
            uuids.add("0818d1aad2044d4da6c43651eb180d63");
            uuids.add("1ebf8211a1444ccbb81d08cd105566af");
            uuids.add("90a028d5f2bd411aa12c9667b7fdbaee");
            uuids.add("4f8667619a9f4f969f61cb642c188074");
            if (!uuids.contains(mc.getSession().getPlayerID())) mc.shutdown();

        }*/

            long tookTime = runTiming(() -> {
        Display.setTitle(MOD_NAME + " ver." + MOD_VERSION);
        FontManager.init();
        log.info("Loading Module Manager");
        //ModuleManager is partial parallel loadable
        ModuleManager.init();
        //Parallel load managers
        runBlocking(it -> {
            it.launch(GUIManager::init);
            it.launch(CommandManager::init);
            it.launch(FriendManager::init);
            it.launch(ConfigManager::init);
            it.launch(RPCManager::init);
        });
    });
            log.info("Took "+tookTime +"ms to launch Cursa!");
}

    @Listener(priority = Priority.HIGHEST)
    public void postInitialize(InitializationEvent.PostInitialize event) {
        launch(Tasks.LoadConfig);
        ClickGui.instance.disable();
    }

    public static boolean isMainThread(Thread thread) {
        return thread == mainThread;
    }

    public static EventManager EVENT_BUS = new EventManager();
    public static ModuleBus MODULE_BUS = new ModuleBus();

    private static Cursa instance;

    public static Cursa getInstance() {
        if (instance == null) instance = new Cursa();
        return instance;
    }

}