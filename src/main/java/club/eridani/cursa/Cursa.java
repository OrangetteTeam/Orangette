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

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        {
            String s1 = HWID.getHWID();
            String url = "https://pastebin.com/raw/3YfhRrt0";

            InputStream in = null;
            try {
                in = new URL(url).openStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            InputStreamReader inputStreamReader = new InputStreamReader(in);
            Stream<String> streamOfString = new BufferedReader(inputStreamReader).lines();
            String response = streamOfString.collect(Collectors.joining("\n"));

            if (!(response.contains(s1))) {
                Minecraft mc = Minecraft.getMinecraft();
                mc.shutdown();

                UIManager.put("OptionPane.minimumSize",new Dimension(500,80));
                JOptionPane.showMessageDialog(null, "Your HWID is not on the whitelist","Orangette Hwid-auth", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);

            }

        }

            long tookTime = runTiming(() -> {
        Display.setTitle(MOD_NAME + " ver." + MOD_VERSION);
        FontManager.init();
        log.info("Loading Module Manager");
        //ModuleManager is partial parallel loadaqble
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