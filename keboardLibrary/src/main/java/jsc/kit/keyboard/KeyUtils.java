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

    public static final String TYPE_NINE_PALACE_NUMBER = "ninePalaceNumber";
    public static final String TYPE_NINE_PALACE_NUMBER_WITH_ABC = "ninePalaceNumberWithABC";
    public static final String TYPE_LETTER = "letter";
    public static final String TYPE_LETTER_NUMBER = "letterNumber";
    public static final String TYPE_SYMBOL = "symbol";

    public static final String KEY_LABEL_NEXT = "Next";
    public static final String KEY_LABEL_DONE = "Done";
    public static final String KEY_LABEL_SHOW_NUMBER = "Show";
    public static final String KEY_LABEL_HIDE_NUMBER = "Hide";

    @StringDef({
            TYPE_NINE_PALACE_NUMBER,
            TYPE_NINE_PALACE_NUMBER_WITH_ABC,
            TYPE_LETTER,
            TYPE_LETTER_NUMBER,
            TYPE_SYMBOL
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface KeyboardType {
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
    //letter
    public static final int KEY_A = 0x010;
    public static final int KEY_B = 0x011;
    public static final int KEY_C = 0x012;
    public static final int KEY_D = 0x013;
    public static final int KEY_E = 0x014;
    public static final int KEY_F = 0x015;
    public static final int KEY_G = 0x016;
    public static final int KEY_H = 0x017;
    public static final int KEY_I = 0x018;
    public static final int KEY_J = 0x019;
    public static final int KEY_K = 0x020;
    public static final int KEY_L = 0x021;
    public static final int KEY_M = 0x022;
    public static final int KEY_N = 0x023;
    public static final int KEY_O = 0x024;
    public static final int KEY_P = 0x025;
    public static final int KEY_Q = 0x026;
    public static final int KEY_R = 0x027;
    public static final int KEY_S = 0x028;
    public static final int KEY_T = 0x029;
    public static final int KEY_U = 0x030;
    public static final int KEY_V = 0x031;
    public static final int KEY_W = 0x032;
    public static final int KEY_X = 0x033;
    public static final int KEY_Y = 0x034;
    public static final int KEY_Z = 0x035;
    //symbol
    public static final int KEY_SYMBOL_038 = 0x038;
    public static final int KEY_SYMBOL_039 = 0x039;
    public static final int KEY_SYMBOL_040 = 0x040;
    public static final int KEY_SYMBOL_041 = 0x041;
    public static final int KEY_SYMBOL_042 = 0x042;
    public static final int KEY_SYMBOL_043 = 0x043;
    public static final int KEY_SYMBOL_044 = 0x044;
    public static final int KEY_SYMBOL_045 = 0x045;
    public static final int KEY_SYMBOL_046 = 0x046;
    public static final int KEY_SYMBOL_047 = 0x047;
    public static final int KEY_SYMBOL_048 = 0x048;
    public static final int KEY_SYMBOL_049 = 0x049;
    public static final int KEY_SYMBOL_050 = 0x050;
    public static final int KEY_SYMBOL_051 = 0x051;
    public static final int KEY_SYMBOL_052 = 0x052;
    public static final int KEY_SYMBOL_053 = 0x053;
    public static final int KEY_SYMBOL_054 = 0x054;
    public static final int KEY_SYMBOL_055 = 0x055;
    public static final int KEY_SYMBOL_056 = 0x056;
    public static final int KEY_SYMBOL_057 = 0x057;
    public static final int KEY_SYMBOL_058 = 0x058;
    public static final int KEY_SYMBOL_059 = 0x059;
    public static final int KEY_SYMBOL_060 = 0x060;
    public static final int KEY_SYMBOL_061 = 0x061;
    public static final int KEY_SYMBOL_062 = 0x062;
    public static final int KEY_SYMBOL_063 = 0x063;
    public static final int KEY_SYMBOL_064 = 0x064;
    public static final int KEY_SYMBOL_065 = 0x065;
    public static final int KEY_SYMBOL_066 = 0x066;
    public static final int KEY_SYMBOL_067 = 0x067;
    public static final int KEY_SYMBOL_068 = 0x068;
    public static final int KEY_SPACE = 0x070;
    //function
    public static final int KEY_CLOSE = 0x090;
    public static final int KEY_SCALE = 0x091;
    public static final int KEY_ABC = 0x092;
    public static final int KEY_DELETE = 0x093;
    public static final int KEY_NEXT = 0x094;
    public static final int KEY_123 = 0x095;
    public static final int KEY_AA = 0x096;
    public static final int KEY_NUM = 0x097;
    public static final int KEY_ENTER = 0x098;
    public static final int KEY_SYMBOL = 0x099;
    public static final int KEY_BACK = 0x100;
    //not key
    public static final int KEY_BLANK = 0x126;
    public static final int KEY_BLACK = 0x127;

    @IntDef({
            //number
            KEY_0, KEY_1, KEY_2, KEY_3, KEY_4, KEY_5, KEY_6, KEY_7, KEY_8, KEY_9,
            //letter
            KEY_A, KEY_B, KEY_C, KEY_D, KEY_E, KEY_F, KEY_G, KEY_H, KEY_I, KEY_J,
            KEY_K, KEY_L, KEY_M, KEY_N, KEY_O, KEY_P, KEY_Q, KEY_R, KEY_S, KEY_T,
            KEY_U, KEY_V, KEY_W, KEY_X, KEY_Y, KEY_Z,
            //symbol
            KEY_SYMBOL_038, KEY_SYMBOL_039,
            KEY_SYMBOL_040, KEY_SYMBOL_041, KEY_SYMBOL_042, KEY_SYMBOL_043, KEY_SYMBOL_044,
            KEY_SYMBOL_045, KEY_SYMBOL_046, KEY_SYMBOL_047, KEY_SYMBOL_048, KEY_SYMBOL_049,
            KEY_SYMBOL_050, KEY_SYMBOL_051, KEY_SYMBOL_052, KEY_SYMBOL_053, KEY_SYMBOL_054,
            KEY_SYMBOL_055, KEY_SYMBOL_056, KEY_SYMBOL_057, KEY_SYMBOL_058, KEY_SYMBOL_059,
            KEY_SYMBOL_060, KEY_SYMBOL_061, KEY_SYMBOL_062, KEY_SYMBOL_063, KEY_SYMBOL_064,
            KEY_SYMBOL_065, KEY_SYMBOL_066, KEY_SYMBOL_067, KEY_SYMBOL_068, KEY_SPACE,
            //function
            KEY_CLOSE, KEY_SCALE, KEY_ABC, KEY_DELETE, KEY_NEXT,
            KEY_123, KEY_AA, KEY_NUM, KEY_ENTER, KEY_SYMBOL, KEY_BACK,
            //not key
            KEY_BLANK, KEY_BLACK
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface KeyCode {
    }

    private static SparseArray<KeyBean> keys = new SparseArray<>();
    private static List<List<KeyBean>> ninePalaceNumberKeys = new ArrayList<>();
    private static List<List<KeyBean>> ninePalaceNumberKeysWithABC = new ArrayList<>();
    private static List<List<KeyBean>> letterKeys = new ArrayList<>();
    private static List<List<KeyBean>> letterNumberKeys = new ArrayList<>();
    private static List<List<KeyBean>> symbolKeys = new ArrayList<>();

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
        //special
        putKey(new KeyBean(KEY_SYMBOL_038, "•", "."));
        putKey(new KeyBean(KEY_SYMBOL_039, "-"));

        putKey(new KeyBean(KEY_SYMBOL_040, "!"));
        putKey(new KeyBean(KEY_SYMBOL_041, "@"));
        putKey(new KeyBean(KEY_SYMBOL_042, "#"));
        putKey(new KeyBean(KEY_SYMBOL_043, "$"));
        putKey(new KeyBean(KEY_SYMBOL_044, "%"));
        putKey(new KeyBean(KEY_SYMBOL_045, "^"));
        putKey(new KeyBean(KEY_SYMBOL_046, "&"));
        putKey(new KeyBean(KEY_SYMBOL_047, "*"));
        putKey(new KeyBean(KEY_SYMBOL_048, "("));
        putKey(new KeyBean(KEY_SYMBOL_049, ")"));

        putKey(new KeyBean(KEY_SYMBOL_050, "["));
        putKey(new KeyBean(KEY_SYMBOL_051, "]"));
        putKey(new KeyBean(KEY_SYMBOL_052, "{"));
        putKey(new KeyBean(KEY_SYMBOL_053, "}"));
        putKey(new KeyBean(KEY_SYMBOL_054, "\\"));
        putKey(new KeyBean(KEY_SYMBOL_055, "/"));
        putKey(new KeyBean(KEY_SYMBOL_056, ";"));
        putKey(new KeyBean(KEY_SYMBOL_057, ":"));
        putKey(new KeyBean(KEY_SYMBOL_058, "'"));
        putKey(new KeyBean(KEY_SYMBOL_059, "\""));

        putKey(new KeyBean(KEY_SYMBOL_060, "<"));
        putKey(new KeyBean(KEY_SYMBOL_061, ">"));
        putKey(new KeyBean(KEY_SYMBOL_062, ","));
        putKey(new KeyBean(KEY_SYMBOL_063, "`"));
        putKey(new KeyBean(KEY_SYMBOL_064, "~"));
        putKey(new KeyBean(KEY_SYMBOL_065, "+"));
        putKey(new KeyBean(KEY_SYMBOL_066, "="));
        putKey(new KeyBean(KEY_SYMBOL_067, "?"));
        putKey(new KeyBean(KEY_SYMBOL_068, "_"));
        putKey(new KeyBean(KEY_SPACE, "Space", "\u2000"));
        //function
        putKey(new KeyBean(KEY_ABC, "ABC"));
        putKey(new KeyBean(KEY_SCALE, "Scale", "", R.drawable.key_icon_scale));
        putKey(new KeyBean(KEY_CLOSE, "Close", "", R.drawable.key_icon_close));
        putKey(new KeyBean(KEY_DELETE, "Delete", "", R.drawable.key_icon_del));
        putKey(new KeyBean(KEY_NEXT, KEY_LABEL_NEXT, ""));
        putKey(new KeyBean(KEY_123, "123?", ""));
        putKey(new KeyBean(KEY_AA, "Aa", "", R.drawable.key_icon_lower_case));
        putKey(new KeyBean(KEY_NUM, KEY_LABEL_SHOW_NUMBER, ""));
        putKey(new KeyBean(KEY_ENTER, "Enter", "\r"));
        putKey(new KeyBean(KEY_SYMBOL, "Sym", ""));
        putKey(new KeyBean(KEY_BACK, "Back", ""));

    }

    private static void putKey(@NonNull KeyBean keyBean) {
        keys.put(keyBean.getKey(), keyBean);
    }

    public static List<List<KeyBean>> loadKeys(String inputType) {
        List<List<KeyBean>> keys = null;
        switch (inputType) {
            case TYPE_NINE_PALACE_NUMBER:
                keys = getNinePalaceNumberKeys();
                break;
            case TYPE_NINE_PALACE_NUMBER_WITH_ABC:
                keys = getNinePalaceNumberKeysWithABC();
                break;
            case TYPE_LETTER:
                keys = getLetterKeys();
                break;
            case TYPE_LETTER_NUMBER:
                keys = getLetterNumberKeys();
                break;
            case TYPE_SYMBOL:
                keys = getSymbolKeys();
                break;
        }
        return keys;
    }

    public static List<List<KeyBean>> getNinePalaceNumberKeys() {
        if (ninePalaceNumberKeys.isEmpty()) {
            //第一行
            List<KeyBean> keys0 = new ArrayList<>();
            keys0.add(keys.get(KEY_1));
            keys0.add(keys.get(KEY_2));
            keys0.add(keys.get(KEY_3));
            keys0.add(keys.get(KEY_CLOSE));
            ninePalaceNumberKeys.add(keys0);
            //第二行
            List<KeyBean> keys1 = new ArrayList<>();
            keys1.add(keys.get(KEY_4));
            keys1.add(keys.get(KEY_5));
            keys1.add(keys.get(KEY_6));
            keys1.add(keys.get(KEY_SYMBOL_039));
            ninePalaceNumberKeys.add(keys1);
            //第三行
            List<KeyBean> keys2 = new ArrayList<>();
            keys2.add(keys.get(KEY_7));
            keys2.add(keys.get(KEY_8));
            keys2.add(keys.get(KEY_9));
            keys2.add(keys.get(KEY_DELETE));
            ninePalaceNumberKeys.add(keys2);
            //第四行
            List<KeyBean> keys3 = new ArrayList<>();
            keys3.add(keys.get(KEY_SYMBOL_038));
            keys3.add(keys.get(KEY_0));
            keys3.add(keys.get(KEY_NEXT));
            ninePalaceNumberKeys.add(keys3);
        }
        updateHorizontalWeight(KEY_SYMBOL_039, 1.0f);
        updateHorizontalWeight(KEY_DELETE, 1.0f);
        updateHorizontalWeight(KEY_NEXT, 2.0f);
        return ninePalaceNumberKeys;
    }

    public static List<List<KeyBean>> getNinePalaceNumberKeysWithABC() {
        if (ninePalaceNumberKeysWithABC.isEmpty()) {
            //第一行
            List<KeyBean> keys0 = new ArrayList<>();
            keys0.add(keys.get(KEY_1));
            keys0.add(keys.get(KEY_2));
            keys0.add(keys.get(KEY_3));
            keys0.add(keys.get(KEY_CLOSE));
            ninePalaceNumberKeysWithABC.add(keys0);
            //第二行
            List<KeyBean> keys1 = new ArrayList<>();
            keys1.add(keys.get(KEY_4));
            keys1.add(keys.get(KEY_5));
            keys1.add(keys.get(KEY_6));
            keys1.add(keys.get(KEY_SYMBOL_039));
            ninePalaceNumberKeysWithABC.add(keys1);
            //第三行
            List<KeyBean> keys2 = new ArrayList<>();
            keys2.add(keys.get(KEY_7));
            keys2.add(keys.get(KEY_8));
            keys2.add(keys.get(KEY_9));
            keys2.add(keys.get(KEY_DELETE));
            ninePalaceNumberKeysWithABC.add(keys2);
            //第四行
            List<KeyBean> keys3 = new ArrayList<>();
            keys3.add(keys.get(KEY_ABC));
            keys3.add(keys.get(KEY_0));
            keys3.add(keys.get(KEY_SYMBOL_038));
            keys3.add(keys.get(KEY_NEXT));
            ninePalaceNumberKeysWithABC.add(keys3);
        }
        updateHorizontalWeight(KEY_SYMBOL_039, 1.0f);
        updateHorizontalWeight(KEY_DELETE, 1.0f);
        updateHorizontalWeight(KEY_NEXT, 1.0f);
        return ninePalaceNumberKeysWithABC;
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
            keys2.add(keys.get(KEY_SYMBOL_039));
            keys2.add(keys.get(KEY_Z));
            keys2.add(keys.get(KEY_X));
            keys2.add(keys.get(KEY_C));
            keys2.add(keys.get(KEY_V));
            keys2.add(keys.get(KEY_B));
            keys2.add(keys.get(KEY_N));
            keys2.add(keys.get(KEY_M));
            keys2.add(keys.get(KEY_SYMBOL_038));
            keys2.add(keys.get(KEY_DELETE));
            letterKeys.add(keys2);
            //第四行
            List<KeyBean> keys3 = new ArrayList<>();
            keys3.add(keys.get(KEY_AA));
            keys3.add(keys.get(KEY_123));
            keys3.add(keys.get(KEY_SYMBOL));
            keys3.add(keys.get(KEY_SPACE));
            keys3.add(keys.get(KEY_NEXT));
            letterKeys.add(keys3);
        }
        updateHorizontalWeight(KEY_NUM, 1.75f);
        updateHorizontalWeight(KEY_SYMBOL_039, 1.25f);
        updateHorizontalWeight(KEY_DELETE, 1.75f);
        updateHorizontalWeight(KEY_AA, 1.75f);
        updateHorizontalWeight(KEY_SPACE, 5.0f);
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
            keys0.add(keys.get(KEY_CLOSE));
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
            keys1.add(keys.get(KEY_SCALE));
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
            keys3.add(keys.get(KEY_SYMBOL_039));
            keys3.add(keys.get(KEY_Z));
            keys3.add(keys.get(KEY_X));
            keys3.add(keys.get(KEY_C));
            keys3.add(keys.get(KEY_V));
            keys3.add(keys.get(KEY_B));
            keys3.add(keys.get(KEY_N));
            keys3.add(keys.get(KEY_M));
            keys3.add(keys.get(KEY_SYMBOL_038));
            keys3.add(keys.get(KEY_DELETE));
            letterNumberKeys.add(keys3);
            //第五行
            List<KeyBean> keys4 = new ArrayList<>();
            keys4.add(keys.get(KEY_AA));
            keys4.add(keys.get(KEY_123));
            keys4.add(keys.get(KEY_SYMBOL));
            keys4.add(keys.get(KEY_SPACE));
            keys4.add(keys.get(KEY_NEXT));
            letterNumberKeys.add(keys4);
        }
        updateHorizontalWeight(KEY_NUM, 1.75f);
        updateHorizontalWeight(KEY_SYMBOL_039, 1.25f);
        updateHorizontalWeight(KEY_DELETE, 1.75f);
        updateHorizontalWeight(KEY_AA, 1.75f);
        updateHorizontalWeight(KEY_SPACE, 5.0f);
        updateHorizontalWeight(KEY_NEXT, 2.25f);
        return letterNumberKeys;
    }

    public static List<List<KeyBean>> getSymbolKeys() {
        if (symbolKeys.isEmpty()) {
            //第一行
            List<KeyBean> keys0 = new ArrayList<>();
            keys0.add(keys.get(KEY_SYMBOL_040));
            keys0.add(keys.get(KEY_SYMBOL_041));
            keys0.add(keys.get(KEY_SYMBOL_042));
            keys0.add(keys.get(KEY_SYMBOL_043));
            keys0.add(keys.get(KEY_SYMBOL_044));
            keys0.add(keys.get(KEY_SYMBOL_045));
            keys0.add(keys.get(KEY_SYMBOL_046));
            keys0.add(keys.get(KEY_CLOSE));
            symbolKeys.add(keys0);
            //第二行
            List<KeyBean> keys1 = new ArrayList<>();
            keys1.add(keys.get(KEY_SYMBOL_047));
            keys1.add(keys.get(KEY_SYMBOL_048));
            keys1.add(keys.get(KEY_SYMBOL_049));
            keys1.add(keys.get(KEY_SYMBOL_050));
            keys1.add(keys.get(KEY_SYMBOL_051));
            keys1.add(keys.get(KEY_SYMBOL_052));
            keys1.add(keys.get(KEY_SYMBOL_053));
            keys1.add(keys.get(KEY_SYMBOL_068));
            symbolKeys.add(keys1);
            //第三行
            List<KeyBean> keys2 = new ArrayList<>();
            keys2.add(keys.get(KEY_SYMBOL_054));
            keys2.add(keys.get(KEY_SYMBOL_055));
            keys2.add(keys.get(KEY_SYMBOL_056));
            keys2.add(keys.get(KEY_SYMBOL_057));
            keys2.add(keys.get(KEY_SYMBOL_058));
            keys2.add(keys.get(KEY_SYMBOL_059));
            keys2.add(keys.get(KEY_SYMBOL_060));
            keys2.add(keys.get(KEY_SYMBOL_061));
            symbolKeys.add(keys2);
            //第四行
            List<KeyBean> keys3 = new ArrayList<>();
            keys3.add(keys.get(KEY_BACK));
            keys3.add(keys.get(KEY_SYMBOL_062));
            keys3.add(keys.get(KEY_SYMBOL_063));
            keys3.add(keys.get(KEY_SYMBOL_064));
            keys3.add(keys.get(KEY_SYMBOL_065));
            keys3.add(keys.get(KEY_SYMBOL_066));
            keys3.add(keys.get(KEY_SYMBOL_067));
            keys3.add(keys.get(KEY_DELETE));
            symbolKeys.add(keys3);
        }
        updateHorizontalWeight(KEY_DELETE, 1f);
        return symbolKeys;
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

    public static boolean isSymbolKey(@KeyCode int key) {
        return key >= KeyUtils.KEY_SYMBOL_038 && key <= KeyUtils.KEY_SPACE;
    }

    public static boolean isFunctionKey(@KeyCode int key) {
        return key >= KeyUtils.KEY_CLOSE && key <= KeyUtils.KEY_BACK;
    }

    public static boolean isNotKey(@KeyCode int key) {
        return key >= KeyUtils.KEY_BLANK && key <= KeyUtils.KEY_BLACK;
    }


    public static void init(Activity activity, @NonNull KeyboardView keyboardView) {
        init(activity == null ? null : activity.getWindow(), keyboardView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
    }

    public static void init(Window window, @NonNull KeyboardView keyboardView) {
        init(window, keyboardView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
    }

    public static void init(Activity activity, @NonNull KeyboardView keyboardView, int gravity) {
        init(activity == null ? null : activity.getWindow(), keyboardView, gravity);
    }

    public static void init(Window window, @NonNull KeyboardView keyboardView, int gravity) {
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
