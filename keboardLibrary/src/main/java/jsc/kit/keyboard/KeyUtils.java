package jsc.kit.keyboard;

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
            KEY_A, KEY_B, KEY_C, KEY_D, KEY_E, KEY_F, KEY_G, KEY_H, KEY_I,KEY_J,//letter
            KEY_K, KEY_L, KEY_M, KEY_N, KEY_O, KEY_P, KEY_Q, KEY_R, KEY_S, KEY_T,
            KEY_U, KEY_V, KEY_W, KEY_X, KEY_Y, KEY_Z,
            KEY_CLOSE, KEY_SCALE, KEY_ABC, KEY_DELETE, KEY_DOT, KEY_NEXT, KEY_SIGNED, KEY_SPACE, KEY_123, KEY_AA, KEY_NUM, KEY_ENTER,//special
            KEY_BLANK, KEY_BLACK//not key
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Key{}
    private static SparseArray<KeyBean> keys = new SparseArray<>();
    private static List<List<KeyBean>> horizontalNumberKeys = new ArrayList<>();
    private static List<List<KeyBean>> ninePalaceNumberKeys = new ArrayList<>();
    private static List<List<KeyBean>> letterKeys = new ArrayList<>();
    private static List<List<KeyBean>> letterNumberKeys = new ArrayList<>();

    static {
        //number
        keys.put(KEY_0, new KeyBean(KEY_0, "0"));
        keys.put(KEY_1, new KeyBean(KEY_1, "1"));
        keys.put(KEY_2, new KeyBean(KEY_2, "2"));
        keys.put(KEY_3, new KeyBean(KEY_3, "3"));
        keys.put(KEY_4, new KeyBean(KEY_4, "4"));
        keys.put(KEY_5, new KeyBean(KEY_5, "5"));
        keys.put(KEY_6, new KeyBean(KEY_6, "6"));
        keys.put(KEY_7, new KeyBean(KEY_7, "7"));
        keys.put(KEY_8, new KeyBean(KEY_8, "8"));
        keys.put(KEY_9, new KeyBean(KEY_9, "9"));
        //character
        keys.put(KEY_SIGNED, new KeyBean(KEY_SIGNED, "-", "-"));
        keys.put(KEY_DOT, new KeyBean(KEY_DOT, "•", "."));
        keys.put(KEY_ABC, new KeyBean(KEY_ABC, "ABC"));
        keys.put(KEY_SCALE, new KeyBean(KEY_SCALE, "Scale", "", R.drawable.key_ico_move));
        keys.put(KEY_CLOSE, new KeyBean(KEY_CLOSE, "Close", "", R.drawable.key_ico_close));
        keys.put(KEY_DELETE, new KeyBean(KEY_DELETE, "Delete", "", R.drawable.key_icon_del));
        keys.put(KEY_NEXT, new KeyBean(KEY_NEXT, KEY_LABEL_NEXT, ""));
        keys.put(KEY_SPACE, new KeyBean(KEY_SPACE, "Space", "\u2000"));
        keys.put(KEY_123, new KeyBean(KEY_123, "123?", ""));
        keys.put(KEY_AA, new KeyBean(KEY_AA, "Aa", ""));
        keys.put(KEY_NUM, new KeyBean(KEY_NUM, "Num", ""));
        keys.put(KEY_ENTER, new KeyBean(KEY_ENTER, "Enter", "\r"));
        //letter
        keys.put(KEY_A, new KeyBean(KEY_A, "a"));
        keys.put(KEY_B, new KeyBean(KEY_B, "b"));
        keys.put(KEY_C, new KeyBean(KEY_C, "c"));
        keys.put(KEY_D, new KeyBean(KEY_D, "d"));
        keys.put(KEY_E, new KeyBean(KEY_E, "e"));
        keys.put(KEY_F, new KeyBean(KEY_F, "f"));
        keys.put(KEY_G, new KeyBean(KEY_G, "g"));
        keys.put(KEY_H, new KeyBean(KEY_H, "h"));
        keys.put(KEY_I, new KeyBean(KEY_I, "i"));
        keys.put(KEY_J, new KeyBean(KEY_J, "j"));
        keys.put(KEY_K, new KeyBean(KEY_K, "k"));
        keys.put(KEY_L, new KeyBean(KEY_L, "l"));
        keys.put(KEY_M, new KeyBean(KEY_M, "m"));
        keys.put(KEY_N, new KeyBean(KEY_N, "n"));
        keys.put(KEY_O, new KeyBean(KEY_O, "o"));
        keys.put(KEY_P, new KeyBean(KEY_P, "p"));
        keys.put(KEY_Q, new KeyBean(KEY_Q, "q"));
        keys.put(KEY_R, new KeyBean(KEY_R, "r"));
        keys.put(KEY_S, new KeyBean(KEY_S, "s"));
        keys.put(KEY_T, new KeyBean(KEY_T, "t"));
        keys.put(KEY_U, new KeyBean(KEY_U, "u"));
        keys.put(KEY_V, new KeyBean(KEY_V, "v"));
        keys.put(KEY_W, new KeyBean(KEY_W, "w"));
        keys.put(KEY_X, new KeyBean(KEY_X, "x"));
        keys.put(KEY_Y, new KeyBean(KEY_Y, "y"));
        keys.put(KEY_Z, new KeyBean(KEY_Z, "z"));
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
            keys1.add(keys.get(KEY_SCALE));
            letterKeys.add(keys1);
            //第二行
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
            keys2.add(keys.get(KEY_CLOSE));
            letterKeys.add(keys2);
            //第三行
            List<KeyBean> keys3 = new ArrayList<>();
            keys3.add(keys.get(KEY_AA));
            keys3.add(keys.get(KEY_Z));
            keys3.add(keys.get(KEY_X));
            keys3.add(keys.get(KEY_C));
            keys3.add(keys.get(KEY_V));
            keys3.add(keys.get(KEY_B));
            keys3.add(keys.get(KEY_N));
            keys3.add(keys.get(KEY_M));
            keys3.add(keys.get(KEY_SIGNED));
            keys3.add(keys.get(KEY_NEXT));
            letterKeys.add(keys3);
            //第四行
            List<KeyBean> keys4 = new ArrayList<>();
            keys4.add(keys.get(KEY_123));
            keys4.add(keys.get(KEY_DOT));
            keys4.add(keys.get(KEY_SPACE));
            keys4.add(keys.get(KEY_DELETE));
            letterKeys.add(keys4);
        }
        updateHorizontalWeight(KEY_NUM, .75f);
        updateHorizontalWeight(KEY_NEXT, 2.0f);
        updateHorizontalWeight(KEY_123, 1.5f);
        updateHorizontalWeight(KEY_SPACE, 5.50f);
        updateHorizontalWeight(KEY_DELETE, 3.0f);
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
            keys3.add(keys.get(KEY_AA));
            keys3.add(keys.get(KEY_Z));
            keys3.add(keys.get(KEY_X));
            keys3.add(keys.get(KEY_C));
            keys3.add(keys.get(KEY_V));
            keys3.add(keys.get(KEY_B));
            keys3.add(keys.get(KEY_N));
            keys3.add(keys.get(KEY_M));
            keys3.add(keys.get(KEY_SIGNED));
            keys3.add(keys.get(KEY_NEXT));
            letterNumberKeys.add(keys3);
            //第五行
            List<KeyBean> keys4 = new ArrayList<>();
            keys4.add(keys.get(KEY_123));
            keys4.add(keys.get(KEY_DOT));
            keys4.add(keys.get(KEY_SPACE));
            keys4.add(keys.get(KEY_DELETE));
            letterNumberKeys.add(keys4);
        }
        updateHorizontalWeight(KEY_NUM, 1.75f);
        updateHorizontalWeight(KEY_NEXT, 2.0f);
        updateHorizontalWeight(KEY_123, 1.5f);
        updateHorizontalWeight(KEY_SPACE, 5.50f);
        updateHorizontalWeight(KEY_DELETE, 3.0f);
        return letterNumberKeys;
    }

    public static void updateHorizontalWeight(@Key int key, float weight) {
        KeyBean bean = keys.get(key);
        bean.setHorizontalWeight(weight);
    }

    public static KeyBean createKey(@Key int key, float horizontalWeight) {
        return createKey(key, "", "", -1, horizontalWeight);
    }

    public static KeyBean createKey(@Key int key, CharSequence label, CharSequence value, int drawable, float horizontalWeight) {
        return new KeyBean(key, label, value, drawable, horizontalWeight);
    }

    @Nullable
    public static KeyBean findKey(@Key int key) {
        return keys.get(key);
    }

    public static boolean isClickableKey(@Key int key) {
        if (isNotKey(key))
            return false;
        return true;
    }

    public static boolean isNumberKey(@Key int key) {
        return key >= KeyUtils.KEY_0 && key <= KeyUtils.KEY_9;
    }

    public static boolean isLetterKey(@Key int key) {
        return key >= KeyUtils.KEY_A && key <= KeyUtils.KEY_Z;
    }

    public static boolean isNotKey(@Key int key) {
        return key >= KeyUtils.KEY_BLANK && key <= KeyUtils.KEY_BLACK;
    }

    public static void init(Window window, @NonNull KeyBoardView keyboardView) {
        init(window, keyboardView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
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
