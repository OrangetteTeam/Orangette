package club.eridani.cursa.mixin.mixins.client;

import club.eridani.cursa.Cursa;
import club.eridani.cursa.client.ConfigManager;
import club.eridani.cursa.event.decentraliized.DecentralizedClientTickEvent;
import club.eridani.cursa.event.events.client.*;
import club.eridani.cursa.gui.mainmenu.MainMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.crash.CrashReport;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Inject(method = "displayGuiScreen", at = @At("HEAD"), cancellable = true)
    public void displayGuiScreen(GuiScreen guiScreenIn, CallbackInfo info) {
        if (Minecraft.getMinecraft().currentScreen != null) {
            GuiScreenEvent.Closed screenEvent = new GuiScreenEvent.Closed(Minecraft.getMinecraft().currentScreen);
            Cursa.EVENT_BUS.post(screenEvent);
            GuiScreenEvent.Displayed screenEvent1 = new GuiScreenEvent.Displayed(guiScreenIn);
            Cursa.EVENT_BUS.post(screenEvent1);
        }
    }

    @Inject(method = "runGameLoop", at = @At("HEAD"))
    public void runGameLoop(CallbackInfo ci) {
        Cursa.EVENT_BUS.post(new GameLoopEvent());
    }

    @Inject(method = "runTickKeyboard", at = @At(value = "INVOKE_ASSIGN", target = "org/lwjgl/input/Keyboard.getEventKeyState()Z", remap = false))
    private void onKeyEvent(CallbackInfo ci) {
        if (Minecraft.getMinecraft().currentScreen != null)
            return;

        boolean down = Keyboard.getEventKeyState();
        int key = Keyboard.getEventKey();
        char ch = Keyboard.getEventCharacter();

        //Prevent from toggling all modules,when switching languages.
        if (key != Keyboard.KEY_NONE)
            Cursa.EVENT_BUS.post(down ? new KeyEvent(key, ch) : new InputUpdateEvent(key, ch));
    }

    @Inject(method = "runTick", at = @At("RETURN"))
    public void onTick(CallbackInfo ci) {
        if (Minecraft.getMinecraft().player != null) {
            DecentralizedClientTickEvent.instance.post(null);
            Cursa.EVENT_BUS.post(new TickEvent());
        }
    }

    @Inject(method = "init", at = @At("HEAD"))
    public void onInitMinecraft(CallbackInfo ci) {
        Cursa.EVENT_BUS.register(Cursa.getInstance());
    }

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;checkGLError(Ljava/lang/String;)V", ordinal = 0, shift = At.Shift.BEFORE))
    public void onPreInit(CallbackInfo callbackInfo) {
        Cursa.EVENT_BUS.post(new InitializationEvent.PreInitialize());
    }

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;checkGLError(Ljava/lang/String;)V", ordinal = 2, shift = At.Shift.AFTER))
    public void onInit(CallbackInfo ci) {
        Cursa.EVENT_BUS.post(new InitializationEvent.Initialize());
    }

    @Inject(method = "init", at = @At("RETURN"))
    public void onPostInit(CallbackInfo ci) {
        Cursa.EVENT_BUS.post(new InitializationEvent.PostInitialize());
    }

    @Redirect(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayCrashReport(Lnet/minecraft/crash/CrashReport;)V"))
    public void displayCrashReport(Minecraft minecraft, CrashReport crashReport) {
        save();
    }

    @Inject(method = "shutdown", at = @At("HEAD"))
    public void shutdown(CallbackInfo info) {
        save();
    }

    private void save() {
        System.out.println("Shutting down: saving " + Cursa.MOD_NAME + " configuration");
        ConfigManager.saveAll();
        System.out.println("Configuration saved.");
    }

    @Shadow
    public abstract void displayGuiScreen(@Nullable GuiScreen guiScreenIn);

    @Inject(method = "displayGuiScreen", at = @At(value = "HEAD") , cancellable = true)
    public void displayMainMenu(@Nullable GuiScreen guiScreenIn , CallbackInfo ci) {
        if(guiScreenIn == null || guiScreenIn instanceof GuiMainMenu) {
            displayGuiScreen(new MainMenu());
            ci.cancel();
        }

    }

}
