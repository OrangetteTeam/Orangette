package club.eridani.cursa.setting;

public class NumberSetting<T extends Number> extends Setting<T> {

    private final T min;
    private T max;

    public NumberSetting(String name, T defaultValue, T min, T max) {
        super(name, defaultValue);
        this.min = min;
        this.max = max;
    }

    public T getMin() {
        return min;
    }

    public T getMax() {
        return max;
    }

    public void setMax(T m) {
        this.max = m;
    }

    public boolean isInRange(Number valueIn) {
        return valueIn.doubleValue() <= max.doubleValue() && valueIn.doubleValue() >= min.doubleValue();
    }

}
