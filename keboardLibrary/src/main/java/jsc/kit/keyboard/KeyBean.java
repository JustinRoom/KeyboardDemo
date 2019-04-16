package jsc.kit.keyboard;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * create time: 2019/4/8 14:10 Monday
 *
 * @author jsc
 */
public class KeyBean {
    private int key;
    private CharSequence label;
    private CharSequence value;
    private int drawable;
    private float horizontalWeight;

    public KeyBean(int key, CharSequence label) {
        this(key, label, label, -1);
    }

    public KeyBean(int key, CharSequence label, CharSequence value) {
        this(key, label, value, -1);
    }

    public KeyBean(int key, CharSequence label, CharSequence value, int drawable) {
        this(key, label, value, drawable, 1.0f);
    }

    public KeyBean(int key, CharSequence label, CharSequence value, int drawable, float horizontalWeight) {
        this.key = key;
        this.label = label;
        this.value = value;
        this.drawable = drawable;
        this.horizontalWeight = horizontalWeight;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public CharSequence getLabel() {
        return label;
    }

    public void setLabel(CharSequence label) {
        this.label = label;
    }

    public CharSequence getValue() {
        return value;
    }

    public void setValue(CharSequence value) {
        this.value = value;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public float getHorizontalWeight() {
        return horizontalWeight;
    }

    public void setHorizontalWeight(float horizontalWeight) {
        this.horizontalWeight = horizontalWeight;
    }
}
