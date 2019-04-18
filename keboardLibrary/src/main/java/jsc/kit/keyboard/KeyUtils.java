package jsc.kit.keyboard;

import android.app.Activity;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * create time: 2019/4/8 14:21 Monday
 *
 * @author jsc
 */
public class KeyUtils {

    public static final String TYPE_HORIZONTAL_NUMBER = "horizontalNumber";
    public static final String TYPE_NINE_PALACE_NUMBER = "ninePalaceNumber";
    public static final String TYPE_LETTER = "letter";
    public static final String TYPE_LETTER_NUMBER = "letterNumber";

    public static final String KEY_LABEL_NEXT = "Next";
    public static final String KEY_LABEL_FINISH = "Fini";

    @StringDef({TYPE_HORIZONTAL_NUMBER, TYPE_NINE_PALACE_NUMBER, TYPE_LETTER, TYPE_LETTER, TYPE_LETTER_NUMBER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface KeyBoardType {
    }

    //number
    public static final int KEY_0 = 0x000;
    public static final int KEY_1 = 0x001;
    public static final int KEY_2 = 0x002;
    public static final int KEY_3 = 0x003;
    public static final int KEY_4 = 0x004;
    public static final int KEY_5 = 0x005;
    public static final int KEY_6 = 0x006;
    public static final int KEY_7 = 0x007;
    public static final int KEY_8 = 0x008;
    public static final int KEY_9 = 0x009;
    //character
    public static final int KEY_CLOSE = 0x100;
    public static final int KEY_SCALE = 0x101;
    public static final int KEY_ABC = 0x102;
    public static final int KEY_DELETE = 0x103;
    public static final int KEY_DOT = 0x104;
    public static final int KEY_NEXT = 0x105;
    public static final int KEY_SIGNED = 0x106;
    public static final int KEY_SPACE = 0x108;
    public static final int KEY_123 = 0x109;
    public static final int KEY_AA = 0x110;
    public static final int KEY_NUM = 0x111;
    public static final int KEY_ENTER = 0x112;
    //letter
    public static final int KEY_A = 0x2000;
    public static final int KEY_B = 0x2001;
    public static final int KEY_C = 0x2002;
    public static final int KEY_D = 0x2003;
    public static final int KEY_E = 0x2004;
    public static final int KEY_F = 0x2005;
    public static final int KEY_G = 0x2006;
    public static final int KEY_H = 0x2007;
    public static final int KEY_I = 0x2008;
    public static final int KEY_J = 0x2009;
    public static final int KEY_K = 0x2010;
    public static final int KEY_L = 0x2011;
    public static final int KEY_M = 0x2012;
    public static final int KEY_N = 0x2013;
    public static final int KEY_O = 0x2014;
    public static final int KEY_P = 0x2015;
    public static final int KEY_Q = 0x2016;
    public static final int KEY_R = 0x2017;
    public static final int KEY_S = 0x2018;
    public static final int KEY_T = 0x2019;
    public static final int KEY_U = 0x2020;
    public static final int KEY_V = 0x2021;
    public static final int KEY_W = 0x2022;
    public static final int KEY_X = 0x2023;
    public static final int KEY_Y = 0x2024;
    public static final int KEY_Z = 0x2025;
    //
    public static final int KEY_BLANK = 0x0900;
    public static final int KEY_BLACK = 0x0901;

    @IntDef({
            KEY_0, KEY_1, KEY_2, KEY_3, KEY_4, KEY_5, KEY_6, KEY_7, KEY_8, KEY_9,//number
            KEY_A, KEY_B, KEY_C, KEY_D, KEY_E, KEY_F, KEY_G, KEY_H, KEY_I, KEY_J,//letter
            KEY_K, KEY_L, KEY_M, KEY_N, KEY_O, KEY_P, KEY_Q, KEY_R, KEY_S, KEY_T,
            KEY_U, KEY_V, KEY_W, KEY_X, KEY_Y, KEY_Z,
            KEY_CLOSE, KEY_SCALE, KEY_ABC, KEY_DELETE, KEY_DOT, KEY_NEXT, KEY_SIGNED, KEY_SPACE, KEY_123, KEY_AA, KEY_NUM, KEY_ENTER,//special
            KEY_BLANK, KEY_BLACK//not key
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface KeyCode {
    }

    private static SparseArray<KeyBean> keys = new SparseArray<>();
    private static List<List<KeyBean>> horizontalNumberKeys = new ArrayList<>();
    private static List<List<KeyBean>> ninePalaceNumberKeys = new ArrayList<>();
    private static List<List<KeyBean>> letterKeys = new ArrayList<>();
    private static List<List<KeyBean>> letterNumberKeys = new ArrayList<>();

    static {
        //number
        putKey(new KeyBean(KEY_0, "0"));
        putKey(new KeyBean(KEY_1, "1"));
        putKey(new KeyBean(KEY_2, "2"));
        putKey(new KeyBean(KEY_3, "3"));
        putKey(new KeyBean(KEY_4, "4"));
        putKey(new KeyBean(KEY_5, "5"));
        putKey(new KeyBean(KEY_6, "6"));
        putKey(new KeyBean(KEY_7, "7"));
        putKey(new KeyBean(KEY_8, "8"));
        putKey(new KeyBean(KEY_9, "9"));
        //character
        putKey(new KeyBean(KEY_SIGNED, "-", "-"));
        putKey(new KeyBean(KEY_DOT, "•", "."));
        putKey(new KeyBean(KEY_ABC, "ABC"));
        putKey(new KeyBean(KEY_SCALE, "Scale", "", R.drawable.key_icon_scale));
        putKey(new KeyBean(KEY_CLOSE, "Close", "", R.drawable.key_icon_close));
        putKey(new KeyBean(KEY_DELETE, "Delete", "", R.drawable.key_icon_del));
        putKey(new KeyBean(KEY_NEXT, KEY_LABEL_NEXT, ""));
        putKey(new KeyBean(KEY_SPACE, "Space", "\u2000"));
        putKey(new KeyBean(KEY_123, "123?", ""));
        putKey(new KeyBean(KEY_AA, "Aa", "", R.drawable.key_icon_lower_case));
        putKey(new KeyBean(KEY_NUM, "Num", ""));
        putKey(new KeyBean(KEY_ENTER, "Enter", "\r"));
        //letter
        putKey(new KeyBean(KEY_A, "a"));
        putKey(new KeyBean(KEY_B, "b"));
        putKey(new KeyBean(KEY_C, "c"));
        putKey(new KeyBean(KEY_D, "d"));
        putKey(new KeyBean(KEY_E, "e"));
        putKey(new KeyBean(KEY_F, "f"));
        putKey(new KeyBean(KEY_G, "g"));
        putKey(new KeyBean(KEY_H, "h"));
        putKey(new KeyBean(KEY_I, "i"));
        putKey(new KeyBean(KEY_J, "j"));
        putKey(new KeyBean(KEY_K, "k"));
        putKey(new KeyBean(KEY_L, "l"));
        putKey(new KeyBean(KEY_M, "m"));
        putKey(new KeyBean(KEY_N, "n"));
        putKey(new KeyBean(KEY_O, "o"));
        putKey(new KeyBean(KEY_P, "p"));
        putKey(new KeyBean(KEY_Q, "q"));
        putKey(new KeyBean(KEY_R, "r"));
        putKey(new KeyBean(KEY_S, "s"));
        putKey(new KeyBean(KEY_T, "t"));
        putKey(new KeyBean(KEY_U, "u"));
        putKey(new KeyBean(KEY_V, "v"));
        putKey(new KeyBean(KEY_W, "w"));
        putKey(new KeyBean(KEY_X, "x"));
        putKey(new KeyBean(KEY_Y, "y"));
        putKey(new KeyBean(KEY_Z, "z"));
    }

    private static void putKey(@NonNull KeyBean keyBean) {
        keys.put(keyBean.getKey(), keyBean);
    }

    public static List<List<KeyBean>> loadKeys(String inputType) {
        List<List<KeyBean>> keys = null;
        switch (inputType) {
            case TYPE_HORIZONTAL_NUMBER:
                keys = getHorizontalNumberKeys();
                break;
            case TYPE_NINE_PALACE_NUMBER:
                keys = getNinePalaceNumberKeys();
                break;
            case TYPE_LETTER:
                keys = getLetterKeys();
                break;
            case TYPE_LETTER_NUMBER:
                keys = getLetterNumberKeys();
                break;

        }
        return keys;
    }

    public static List<List<KeyBean>> getHorizontalNumberKeys() {
        if (horizontalNumberKeys.isEmpty()) {
            //第一行
            List<KeyBean> keys0 = new ArrayList<>();
            keys0.add(keys.get(KEY_1));
            keys0.add(keys.get(KEY_2));
            keys0.add(keys.get(KEY_3));
            keys0.add(keys.get(KEY_4));
            keys0.add(keys.get(KEY_5));
            keys0.add(keys.get(KEY_6));
            keys0.add(keys.get(KEY_7));
            keys0.add(keys.get(KEY_8));
            keys0.add(keys.get(KEY_9));
            keys0.add(keys.get(KEY_0));
            keys0.add(keys.get(KEY_DELETE));
            keys0.add(keys.get(KEY_NEXT));
            horizontalNumberKeys.add(keys0);
        }
        updateHorizontalWeight(KEY_DELETE, 1.0f);
        updateHorizontalWeight(KEY_NEXT, 1.0f);
        return horizontalNumberKeys;
    }

    public static List<List<KeyBean>> getNinePalaceNumberKeys() {
        if (ninePalaceNumberKeys.isEmpty()) {
            //第一行
            List<KeyBean> keys0 = new ArrayList<>();
            keys0.add(keys.get(KEY_1));
            keys0.add(keys.get(KEY_2));
            keys0.add(keys.get(KEY_3));
            keys0.add(keys.get(KEY_SCALE));
            ninePalaceNumberKeys.add(keys0);
            //第二行
            List<KeyBean> keys1 = new ArrayList<>();
            keys1.add(keys.get(KEY_4));
            keys1.add(keys.get(KEY_5));
            keys1.add(keys.get(KEY_6));
            keys1.add(keys.get(KEY_CLOSE));
            ninePalaceNumberKeys.add(keys1);
            //第三行
            List<KeyBean> keys2 = new ArrayList<>();
            keys2.add(keys.get(KEY_7));
            keys2.add(keys.get(KEY_8));
            keys2.add(keys.get(KEY_9));
            keys2.add(keys.get(KEY_NEXT));
            ninePalaceNumberKeys.add(keys2);
            //第四行
            List<KeyBean> keys3 = new ArrayList<>();
            keys3.add(keys.get(KEY_ABC));
            keys3.add(keys.get(KEY_0));
            keys3.add(keys.get(KEY_DOT));
            keys3.add(keys.get(KEY_DELETE));
            ninePalaceNumberKeys.add(keys3);
        }
        updateHorizontalWeight(KEY_ABC, 1.0f);
        updateHorizontalWeight(KEY_NEXT, 1.0f);
        updateHorizontalWeight(KEY_DELETE, 1.0f);
        return ninePalaceNumberKeys;
    }

    public static List<List<KeyBean>> getLetterKeys() {
        if (letterKeys.isEmpty()) {
            //第一行
            List<KeyBean> keys0 = new ArrayList<>();
            keys0.add(keys.get(KEY_Q));
            keys0.add(keys.get(KEY_W));
            keys0.add(keys.get(KEY_E));
            keys0.add(keys.get(KEY_R));
            keys0.add(keys.get(KEY_T));
            keys0.add(keys.get(KEY_Y));
            keys0.add(keys.get(KEY_U));
            keys0.add(keys.get(KEY_I));
            keys0.add(keys.get(KEY_O));
            keys0.add(keys.get(KEY_P));
            keys0.add(keys.get(KEY_CLOSE));
            letterKeys.add(keys0);
            //第二行
            List<KeyBean> keys1 = new ArrayList<>();
            keys1.add(createKey(KEY_BLACK, .25f));
            keys1.add(keys.get(KEY_A));
            keys1.add(keys.get(KEY_S));
            keys1.add(keys.get(KEY_D));
            keys1.add(keys.get(KEY_F));
            keys1.add(keys.get(KEY_G));
            keys1.add(keys.get(KEY_H));
            keys1.add(keys.get(KEY_J));
            keys1.add(keys.get(KEY_K));
            keys1.add(keys.get(KEY_L));
            keys1.add(keys.get(KEY_NUM));
            letterKeys.add(keys1);
            //第三行
            List<KeyBean> keys2 = new ArrayList<>();
            keys2.add(keys.get(KEY_SIGNED));
            keys2.add(keys.get(KEY_Z));
            keys2.add(keys.get(KEY_X));
            keys2.add(keys.get(KEY_C));
            keys2.add(keys.get(KEY_V));
            keys2.add(keys.get(KEY_B));
            keys2.add(keys.get(KEY_N));
            keys2.add(keys.get(KEY_M));
            keys2.add(keys.get(KEY_DOT));
            keys2.add(keys.get(KEY_DELETE));
            letterKeys.add(keys2);
            //第四行
            List<KeyBean> keys3 = new ArrayList<>();
            keys3.add(keys.get(KEY_AA));
            keys3.add(keys.get(KEY_SPACE));
            keys3.add(keys.get(KEY_NEXT));
            letterKeys.add(keys3);
        }
        updateHorizontalWeight(KEY_NUM, 1.75f);
        updateHorizontalWeight(KEY_SIGNED, 1.25f);
        updateHorizontalWeight(KEY_DELETE, 1.75f);
        updateHorizontalWeight(KEY_AA, 1.75f);
        updateHorizontalWeight(KEY_SPACE, 7.0f);
        updateHorizontalWeight(KEY_NEXT, 2.25f);


        return letterKeys;
    }

    public static List<List<KeyBean>> getLetterNumberKeys() {
        if (letterNumberKeys.isEmpty()) {
            //第一行
            List<KeyBean> keys0 = new ArrayList<>();
            keys0.add(keys.get(KEY_1));
            keys0.add(keys.get(KEY_2));
            keys0.add(keys.get(KEY_3));
            keys0.add(keys.get(KEY_4));
            keys0.add(keys.get(KEY_5));
            keys0.add(keys.get(KEY_6));
            keys0.add(keys.get(KEY_7));
            keys0.add(keys.get(KEY_8));
            keys0.add(keys.get(KEY_9));
            keys0.add(keys.get(KEY_0));
            keys0.add(keys.get(KEY_SCALE));
            letterNumberKeys.add(keys0);
            //第二行
            List<KeyBean> keys1 = new ArrayList<>();
            keys1.add(keys.get(KEY_Q));
            keys1.add(keys.get(KEY_W));
            keys1.add(keys.get(KEY_E));
            keys1.add(keys.get(KEY_R));
            keys1.add(keys.get(KEY_T));
            keys1.add(keys.get(KEY_Y));
            keys1.add(keys.get(KEY_U));
            keys1.add(keys.get(KEY_I));
            keys1.add(keys.get(KEY_O));
            keys1.add(keys.get(KEY_P));
            keys1.add(keys.get(KEY_CLOSE));
            letterNumberKeys.add(keys1);
            //第三行
            List<KeyBean> keys2 = new ArrayList<>();
            keys2.add(createKey(KEY_BLACK, .25f));
            keys2.add(keys.get(KEY_A));
            keys2.add(keys.get(KEY_S));
            keys2.add(keys.get(KEY_D));
            keys2.add(keys.get(KEY_F));
            keys2.add(keys.get(KEY_G));
            keys2.add(keys.get(KEY_H));
            keys2.add(keys.get(KEY_J));
            keys2.add(keys.get(KEY_K));
            keys2.add(keys.get(KEY_L));
            keys2.add(keys.get(KEY_NUM));
            letterNumberKeys.add(keys2);
            //第四行
            List<KeyBean> keys3 = new ArrayList<>();
            keys3.add(keys.get(KEY_SIGNED));
            keys3.add(keys.get(KEY_Z));
            keys3.add(keys.get(KEY_X));
            keys3.add(keys.get(KEY_C));
            keys3.add(keys.get(KEY_V));
            keys3.add(keys.get(KEY_B));
            keys3.add(keys.get(KEY_N));
            keys3.add(keys.get(KEY_M));
            keys3.add(keys.get(KEY_DOT));
            keys3.add(keys.get(KEY_DELETE));
            letterNumberKeys.add(keys3);
            //第五行
            List<KeyBean> keys4 = new ArrayList<>();
            keys4.add(keys.get(KEY_AA));
            keys4.add(keys.get(KEY_SPACE));
            keys4.add(keys.get(KEY_NEXT));
            letterNumberKeys.add(keys4);
        }
        updateHorizontalWeight(KEY_NUM, 1.75f);
        updateHorizontalWeight(KEY_SIGNED, 1.25f);
        updateHorizontalWeight(KEY_DELETE, 1.75f);
        updateHorizontalWeight(KEY_AA, 1.75f);
        updateHorizontalWeight(KEY_SPACE, 7.0f);
        updateHorizontalWeight(KEY_NEXT, 2.25f);
        return letterNumberKeys;
    }

    public static void updateHorizontalWeight(@KeyCode int key, float weight) {
        KeyBean bean = keys.get(key);
        bean.setHorizontalWeight(weight);
    }

    public static KeyBean createKey(@KeyCode int key, float horizontalWeight) {
        return createKey(key, "", "", -1, horizontalWeight);
    }

    public static KeyBean createKey(@KeyCode int key, CharSequence label, CharSequence value, int drawable, float horizontalWeight) {
        return new KeyBean(key, label, value, drawable, horizontalWeight);
    }

    @Nullable
    public static KeyBean findKey(@KeyCode int key) {
        return keys.get(key);
    }

    public static boolean isClickableKey(@KeyCode int key) {
        if (isNotKey(key))
            return false;
        return true;
    }

    public static boolean isNumberKey(@KeyCode int key) {
        return key >= KeyUtils.KEY_0 && key <= KeyUtils.KEY_9;
    }

    public static boolean isLetterKey(@KeyCode int key) {
        return key >= KeyUtils.KEY_A && key <= KeyUtils.KEY_Z;
    }

    public static boolean isSpecialKey(@KeyCode int key) {
        if (key == KeyUtils.KEY_ABC
                || key == KeyUtils.KEY_SCALE
                || key == KeyUtils.KEY_AA
                || key == KeyUtils.KEY_123
                || key == KeyUtils.KEY_CLOSE
                || key == KeyUtils.KEY_DELETE)
            return true;
        return false;
    }

    public static boolean isNotKey(@KeyCode int key) {
        return key >= KeyUtils.KEY_BLANK && key <= KeyUtils.KEY_BLACK;
    }

    public static void init(Activity activity, @NonNull KeyBoardView keyboardView) {
        init(activity == null ? null : activity.getWindow(), keyboardView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
    }

    public static void init(Window window, @NonNull KeyBoardView keyboardView) {
        init(window, keyboardView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
    }

    public static void init(Activity activity, @NonNull KeyBoardView keyboardView, int gravity) {
        init(activity == null ? null : activity.getWindow(), keyboardView, gravity);
    }

    public static void init(Window window, @NonNull KeyBoardView keyboardView, int gravity) {
        if (window == null)
            return;

        if (keyboardView.getParent() != null) {
            throw new IllegalStateException("Initialized already.");
        }
        View view = window.getDecorView();
        if (view instanceof FrameLayout) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = gravity;
            FrameLayout decorView = (FrameLayout) view;
            decorView.addView(keyboardView, params);
            keyboardView.setVisibility(View.GONE);
        }
    }
}
