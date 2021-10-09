package club.eridani.cursa.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

public class Setting<T> {

    private final String name;
    private final T defaultValue;
    protected T value;
    private final List<BooleanSupplier> visibilities = new ArrayList<>();
    public float optionAnimNow, optionAnim;

    public Setting(String name, T defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T valueIn) {
        value = valueIn;
    }

    public Setting<T> when(BooleanSupplier booleanSupplier) {
        this.visibilities.add(booleanSupplier);
        return this;
    }

    public Setting<T> whenAtMode(Setting<String> modeSetting, String mode) {
        return when(() -> modeSetting.getValue().equals(mode));
    }

    public Setting<T> whenFalse(Setting<Boolean> booleanSetting) {
        return when(() -> !booleanSetting.getValue());
    }

    public Setting<T> whenTrue(Setting<Boolean> booleanSetting) {
        return when(booleanSetting::getValue);
    }

    public boolean isVisible() {
        for (BooleanSupplier booleanSupplier : visibilities) {
            if (!booleanSupplier.getAsBoolean()) return false;
        }
        return true;
    }

    public String getName() {
        return name;
    }

}
