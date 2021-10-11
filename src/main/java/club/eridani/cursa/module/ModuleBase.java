package club.eridani.cursa.module;

import club.eridani.cursa.Cursa;
import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.common.annotations.Parallel;
import club.eridani.cursa.concurrent.decentralization.ListenableImpl;
import club.eridani.cursa.event.events.client.InputUpdateEvent;
import club.eridani.cursa.event.events.network.PacketEvent;
import club.eridani.cursa.event.events.render.RenderEvent;
import club.eridani.cursa.event.events.render.RenderOverlayEvent;
import club.eridani.cursa.notification.NotificationManager;
import club.eridani.cursa.setting.Setting;
import club.eridani.cursa.setting.settings.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ModuleBase extends ListenableImpl {

    public final String name;
    public final Category category;
    public int keyCode;
    public final Parallel annotation = getClass().getAnnotation(Parallel.class);
    public final boolean parallelRunnable = annotation != null && annotation.runnable();

    boolean enabled = false;
    private final List<Setting<?>> settings = new ArrayList<>();

    public List<Setting<?>> getSettings() {
        return settings;
    }

    public Minecraft mc = Minecraft.getMinecraft();

    public float optionAnim, optionAnimNow;

    public ModuleBase() {
        this.name = getAnnotation().name();
        this.category = getAnnotation().category();
        this.keyCode = getAnnotation().keyCode();
    }

    @SafeVarargs
    public final <T> List<T> listOf(T... elements) {
        return Arrays.asList(elements);
    }

    public void toggle() {
        if (isEnabled()) disable();
        else enable();
    }

    public void reload() {
        if (enabled) {
            enabled = false;
            Cursa.MODULE_BUS.unregister(this);
            onDisable();
            enabled = true;
            Cursa.MODULE_BUS.register(this);
            onEnable();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isDisabled() {
        return !enabled;
    }

    public void enable() {
        enabled = true;
        Cursa.MODULE_BUS.register(this);
        subscribe();
        NotificationManager.moduleToggle(this, true);
        onEnable();
    }

    public void disable() {
        enabled = false;
        Cursa.MODULE_BUS.unregister(this);
        unsubscribe();
        NotificationManager.moduleToggle(this, false);
        onDisable();
    }

    public void onPacketReceive(PacketEvent.Receive event) {
    }

    public void onPacketSend(PacketEvent.Send event) {
    }

    public void onTick() {
    }

    public void onRenderTick() {
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onRender(RenderOverlayEvent event) {
    }

    public void onRenderWorld(RenderEvent event) {
    }

    public void onInputUpdate(InputUpdateEvent event) {
    }

    public void onSettingChange(Setting<?> setting) {
    }

    public String getHUDInfo() {
        return "";
    }

    public Setting<Boolean> setting(String name, boolean defaultValue) {
        BooleanSetting value = new BooleanSetting(name, defaultValue);
        settings.add(value);
        return value;
    }

    public Setting<Integer> setting(String name, int defaultValue, int minValue, int maxValue) {
        IntSetting value = new IntSetting(name, defaultValue, minValue, maxValue);
        settings.add(value);
        return value;
    }

    public Setting<Float> setting(String name, float defaultValue, float minValue, float maxValue) {
        FloatSetting value = new FloatSetting(name, defaultValue, minValue, maxValue);
        settings.add(value);
        return value;
    }

    public Setting<Double> setting(String name, double defaultValue, double minValue, double maxValue) {
        DoubleSetting value = new DoubleSetting(name, defaultValue, minValue, maxValue);
        settings.add(value);
        return value;
    }

    public Setting<String> setting(String name, String defaultMode, List<String> modes) {
        ModeSetting value = new ModeSetting(name, defaultMode, modes);
        settings.add(value);
        return value;
    }

    public Setting<String> setting(String name, String defaultMode, String... modes) {
        ModeSetting value = new ModeSetting(name, defaultMode, Arrays.asList(modes));
        settings.add(value);
        return value;
    }

    private Module getAnnotation() {
        if (getClass().isAnnotationPresent(Module.class)) {
            return getClass().getAnnotation(Module.class);
        }
        throw new IllegalStateException("No Annotation on class " + this.getClass().getCanonicalName() + "!");
    }

    public boolean nullCheck(){
        return Objects.isNull(mc.player) || Objects.isNull(mc.world);
    }

}
