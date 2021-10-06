package club.eridani.cursa.client;

import club.eridani.cursa.common.annotations.Module;
import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

public class RPCManager {
    public static RPCManager INSTANCE;
    private Thread _thread = null;

    public static void init(){
        INSTANCE = new RPCManager();
    }

    public void enable()
    {
        DiscordRPC lib = DiscordRPC.INSTANCE;
        String applicationId = "855296693708652564";
        String steamId = "";
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        lib.Discord_Initialize(applicationId, handlers, true, steamId);
        DiscordRichPresence presence = new DiscordRichPresence();
        presence.startTimestamp = System.currentTimeMillis() / 1000; // epoch second
        lib.Discord_UpdatePresence(presence);
        presence.largeImageText = "";
        _thread = new Thread(() ->
        {
            while (!Thread.currentThread().isInterrupted())
            {
                lib.Discord_RunCallbacks();
                presence.details = "AAAAAAAAAAA";
                presence.state = "BBBBBBBBBBBBB";
                presence.largeImageKey = "logo";
                presence.largeImageText = "CCCCCCCCCCCCC";
                try {
                    lib.Discord_UpdatePresence(presence);
                    Thread.sleep(3000);
                }
                catch (InterruptedException ignored) {
                }
            }
        }, "RPC-Callback-Handler");

        _thread.start();
    }

    public void disable()
    {
        DiscordRPC.INSTANCE.Discord_Shutdown();
        _thread = null;
    }
}
