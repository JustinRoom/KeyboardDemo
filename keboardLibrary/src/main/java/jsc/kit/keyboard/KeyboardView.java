package jsc.kit.keyboard;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
/*
   Copyright 2019 JustinRoom

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * create time: 2019/3/27 15:01 Wednesday
 *
 * @author jsc
 */
public class KeyboardView extends LinearLayout {

    private static final String TAG = "keyboard";

    /**
     * 仅支持水平方向上拖动
     */
    public static final String ONLY_HORIZONTAL = "horizontal";
    /**
     * 仅支持垂直方向上拖动
     */
    public static final String ONLY_VERTICAL = "vertical";
    /**
     * 支持任意方向上拖动
     */
    public static final String ALL_DIRECTION = "all";
    /**
     * 不支持拖动
     */
    public static final String NONE = "none";

    @StringDef({ONLY_HORIZONTAL, ONLY_VERTICAL, ALL_DIRECTION, NONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DragSupportModel {
    }

    //存储键盘上的按键view
    private SparseArray<KeyView> viewSparseArray = new SparseArray<>();
    //按键点击动画管理器。使用此管理器，避免同一个按键点击过快而造成按键的scale混乱。
    private SparseArray<Animator> animatorSparseArray = new SparseArray<>();
    //滑动松开手指自动滚动到合适的位置
    private Animator autoReboundAnimator = null;
    //使用该软键盘的输入框管理
    private List<EditText> editTexts = new ArrayList<>(20);
    //当前聚焦的输入框
    private EditText focusedEditText = null;
    //输入框touch后聚焦
    private View.OnTouchListener touchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (v instanceof EditText) {
                if (!v.hasFocus())
                    v.requestFocus();
                showKeyboardByEditTextInputType((EditText) v);
            }
            return false;
        }
    };
    //按键点击监听
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            KeyView keyView = (KeyView) v;
            dispatchKeyDownEvent(keyView);
        }
    };
    //按键字体
    private Typeface typeface = null;
    //是否支持拖动
    private @DragSupportModel
    String curDragSupportModel = ONLY_VERTICAL;
    //键盘拖动时touch坐标
    private float touchX;
    private float touchY;
    //是否处于拖动模式
    private boolean intoDragModel;
    private int touchedPointerId = -1;
    private Rect rect = new Rect();
    //按键的基本宽度
    private int keyWidth;
    //按键的基本高度
    private int keyHeight;
    //按键的水平间隙
    private int keyHorizontalSpace;
    //按键的垂直间隙
    private int keyVerticalSpace;
    //存储键盘的尺寸。size[0]为键盘宽度，size[1]为键盘高度
    private int[] size = new int[2];
    //键盘类型。目前只支持三种：数字键盘、字母键盘、数字+字混合键盘
    private @KeyUtils.KeyboardType
    String keyboardType;
    //是否为大写模式
    private boolean upperCase = false;
    //当键盘类型为数字+字混合键盘时，是否显示数字按键
    private boolean showNumberKeys = false;
    //创建按键时监听
    private onCreateKeyListener createKeyListener = null;
    //键盘的显隐监听
    private OnKeyboardListener keyboardListener = null;
    //自定义的按键监听
    private OnKeyDownListener keyDownListener = null;

    //*********************  Constructors ***********************************//
    public KeyboardView(Context context) {
        this(context, null);
    }

    public KeyboardView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        int defaultKeyWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics());
        int defaultKeySpace = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, context.getResources().getDisplayMetrics());
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.KeyboardView, defStyleAttr, 0);
        int keyWidth = a.getDimensionPixelSize(R.styleable.KeyboardView_keyWidth, defaultKeyWidth);
        int keyHeight = a.getDimensionPixelSize(R.styleable.KeyboardView_keyHeight, 0);
        int horizontalSpace = a.getDimensionPixelSize(R.styleable.KeyboardView_keyHorizontalSpace, defaultKeySpace);
        int verticalSpace = a.getDimensionPixelSize(R.styleable.KeyboardView_keyVerticalSpace, defaultKeySpace);
        int boardType = a.getInt(R.styleable.KeyboardView_keyboardType, 0);
        a.recycle();
        if (keyHeight <= 0)
            keyHeight = keyWidth * 3 / 5;
        initKeySize(keyWidth, keyHeight);
        initKeySpace(horizontalSpace, verticalSpace);
        if (isInEditMode())
            switch (boardType) {
                case 0:
                    show(KeyUtils.TYPE_NINE_PALACE_NUMBER);
                    break;
                case 1:
                    showNumberKeys = false;
                    show(KeyUtils.TYPE_LETTER);
                    break;
                case 2:
                    showNumberKeys = true;
                    show(KeyUtils.TYPE_LETTER_NUMBER);
                    break;
                case 3:
                    show(KeyUtils.TYPE_SYMBOL);
                    break;
            }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (autoReboundAnimator != null) {
            autoReboundAnimator.cancel();
        }
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                //当没有进入键盘拖动模式
                if (isCanDrag() && !intoDragModel) {
                    //这里使用PointerId防止多手指touch混乱问题
                    touchedPointerId = ev.getPointerId(0);
                    touchX = ev.getX();
                    touchY = ev.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isCanDrag() && !intoDragModel && touchedPointerId == ev.getPointerId(0)) {
                    float tempX = ev.getX();
                    float tempY = ev.getY();
                    float dx = touchX - tempX;
                    float dy = touchY - tempY;
                    //当在任意方向上滑动大于等于8pixels时进入键盘拖动模式
                    if (Math.abs(dx) >= 8 || Math.abs(dy) >= 8) {
                        if (!isScaled()) {
                            intoDragModel = true;
                            //进入拖动模式后，所有按键不可用
                            enableAllKeys(false);
                        }
                    } else {
                        touchX = tempX;
                        touchY = tempY;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isCanDrag() && !intoDragModel) {
                    autoRebound();
                }
                break;
        }
        return intoDragModel;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //如果键盘是被缩小了，则不处理touch事件
        if (isScaled() || !isCanDrag())
            return super.onTouchEvent(event);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (touchedPointerId == event.getPointerId(0)) {
                    touchX = event.getX();
                    touchY = event.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (touchedPointerId == event.getPointerId(0)) {
                    float tempX = event.getX();
                    float tempY = event.getY();
                    float dx = touchX - tempX;
                    float dy = touchY - tempY;
                    executeDrag(-dx, -dy);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (intoDragModel && touchedPointerId == event.getPointerId(0)) {
                    //抬起手指时，键盘智能复位
                    autoRebound();
                    //退出拖动模式
                    intoDragModel = false;
                    //恢复所有按键可用
                    enableAllKeys(true);
                }
                break;
        }
        return true;
    }

    /**
     * 删除选中的字符串（清空输入框）
     */
    public final void deleteAllInput() {
        final EditText focusedView = getFocusedEditText();
        if (focusedView == null)
            return;
        Editable editable = focusedView.getText();
        int selectionStart = focusedView.getSelectionStart();
        int selectionEnd = focusedView.getSelectionEnd();
        if (selectionEnd > selectionStart) {
            editable.delete(selectionStart, selectionEnd);
        } else {
            editable.delete(0, selectionStart);
            focusedView.setSelection(0);
        }
    }

    public final void deleteInput() {
        final EditText focusedView = getFocusedEditText();
        if (focusedView == null)
            return;
        Editable editable = focusedView.getText();
        int selectionStart = focusedView.getSelectionStart();
        int selectionEnd = focusedView.getSelectionEnd();
        if (selectionEnd > selectionStart) {
            editable.delete(selectionStart, selectionEnd);
        } else {
            int st = selectionStart - 1;
            if (st < 0)
                st = 0;
            editable.delete(st, selectionStart);
            focusedView.setSelection(st);
        }
    }

    public final void insertInput(KeyBean bean) {
        final EditText focusedView = getFocusedEditText();
        if (focusedView == null)
            return;
        Editable editable = focusedView.getText();
        int selectionStart = focusedView.getSelectionStart();
        int selectionEnd = focusedView.getSelectionEnd();
        CharSequence value = bean.getValue();
        if (selectionEnd > selectionStart) {
            editable.replace(selectionStart, selectionEnd, value);
        } else {
            editable.insert(selectionStart, value);
        }
        if (KeyUtils.isLinkedInputSymbolKey(bean.getKey())) {
            int newSelectionStart = focusedView.getSelectionStart();
            if (newSelectionStart - selectionStart == 2)
                focusedView.setSelection(newSelectionStart - 1);
        }
    }

    @Nullable
    private EditText getFocusedEditText() {
        if (focusedEditText == null || !focusedEditText.hasFocus())
            for (EditText e : editTexts) {
                if (e.hasFocus()) {
                    focusedEditText = e;
                    break;
                }
            }
        return focusedEditText;
    }

    /**
     * 初始化按键的宽高
     */
    public void initKeySize(int keyWidth, int keyHeight) {
        this.keyWidth = keyWidth;
        this.keyHeight = keyHeight;
    }

    /**
     * 初始化按键的水平和垂直间距
     */
    public void initKeySpace(int horizontalSpace, int verticalSpace) {
        this.keyHorizontalSpace = horizontalSpace;
        this.keyVerticalSpace = verticalSpace;
    }

    public void initCustomTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    /**
     * 获取键盘的尺寸。
     *
     * @see #size
     */
    public int[] getKeyboardSize() {
        return size;
    }

    /**
     * 创建按键
     */
    private void createKeys() {
        resetKeys();
        List<List<KeyBean>> keys = KeyUtils.loadKeys(getKeyboardType(), getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
        float maxWeight = getLayoutWeight(keys);
        int row = keys.size();
        int column = (int) maxWeight;
        if (maxWeight > column) {
            column++;
            maxWeight = column;
        }
        size[0] = getPaddingLeft() + (keyWidth + 2 * keyHorizontalSpace) * column + getPaddingRight();
        //宽度大于屏幕宽度，重新计算按键的宽度
        if (size[0] > getResources().getDisplayMetrics().widthPixels) {
            int availableWidth = getResources().getDisplayMetrics().widthPixels - getPaddingLeft() - getPaddingRight() - keyHorizontalSpace * 2 * column;
            keyWidth = availableWidth / column;
            size[0] = getPaddingLeft() + (keyWidth + 2 * keyHorizontalSpace) * column + getPaddingRight();
        }
        size[1] = getPaddingTop() + (keyHeight + 2 * keyVerticalSpace) * row + getPaddingBottom();
        for (int i = 0; i < row; i++) {
            List<KeyBean> tempKeys = keys.get(i);
            LinearLayout layout = new LinearLayout(getContext());
            layout.setOrientation(HORIZONTAL);
            layout.setWeightSum(maxWeight);
            addView(layout, new LayoutParams(size[0], LayoutParams.WRAP_CONTENT));
            for (int j = 0; j < tempKeys.size(); j++) {
                createKey(layout, tempKeys.get(j), keyHeight);
            }
        }
        toggleUpperCase(upperCase);
        updateNumKey();
    }

    private void createKey(LinearLayout layout, KeyBean bean, int keyHeight) {
        int key = bean.getKey();
        KeyView keyView = viewSparseArray.get(key);
        boolean isCachedView = false;
        if (keyView == null) {
            isCachedView = true;
            keyView = new KeyView(getContext());
            keyView.setTypeface(typeface);
            keyView.getTextKeyView().setTextColor(getKeyTextColor(key));
            keyView.getTextKeyView().setTextSize(TypedValue.COMPLEX_UNIT_DIP, getKeyTextSize(key));
            keyView.setOnClickListener(clickListener);
            keyView.setOnLongClickListener(key == KeyUtils.KEY_DELETE ? new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    deleteAllInput();
                    return true;
                }
            } : null);
            keyView.setEnabled(KeyUtils.isClickableKey(key));
            keyView.setVisibility(key == KeyUtils.KEY_BLANK ? INVISIBLE : VISIBLE);
            keyView.setBackgroundResource(getKeyBackground(key));
            keyView.setBean(bean);
            if (!KeyUtils.isNotKey(bean.getKey()))
                viewSparseArray.put(key, keyView);
        }
        LayoutParams params = new LayoutParams(0, keyHeight, bean.getHorizontalWeight());
        params.leftMargin = keyHorizontalSpace;
        params.rightMargin = keyHorizontalSpace;
        params.topMargin = keyVerticalSpace;
        params.bottomMargin = keyVerticalSpace;
        layout.addView(keyView, params);
        if (createKeyListener != null)
            createKeyListener.onKeyCreated(isCachedView, keyView, bean);
    }

    private float getLayoutWeight(List<List<KeyBean>> keys) {
        float maxWeight = 0;
        for (int i = 0; i < keys.size(); i++) {
            List<KeyBean> childKeys = keys.get(i);
            float rowWeightSum = 0;
            for (int j = 0; j < childKeys.size(); j++) {
                rowWeightSum = rowWeightSum + childKeys.get(j).getHorizontalWeight();
            }
            maxWeight = Math.max(maxWeight, rowWeightSum);
        }
        return maxWeight;
    }

    private int getKeyTextColor(@KeyUtils.KeyCode int key) {
        return key == KeyUtils.KEY_NEXT ? Color.WHITE : 0xFF0A5028;
    }

    private float getKeyTextSize(int key) {
        return createKeyListener == null ? 16 : createKeyListener.getKeyTextSize(getKeyboardType(), key);
    }

    private int getKeyBackground(int key) {
        if (key == KeyUtils.KEY_NEXT)
            return R.drawable.key_next_key_background_ripple;
        if (KeyUtils.isFunctionKey(key))
            return R.drawable.key_special_key_background_ripple;
        return R.drawable.key_normal_key_background_ripple;
    }

    private void executeKeyboardReLocation(Animator.AnimatorListener listener) {
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(
                this,
                PropertyValuesHolder.ofFloat(View.TRANSLATION_X, getTranslationX(), 0),
                PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, getTranslationX(), 0)
        ).setDuration(300);
        animator.addListener(listener);
        animator.start();
    }


    private void dispatchKeyDownEvent(KeyView keyView) {
//        playClickSound();
        playClickAnimation(keyView);
        boolean needExecuteDefaultKeyDownEvent = keyDownListener == null || !keyDownListener.onKeyDown(this, keyView);
        if (!needExecuteDefaultKeyDownEvent)
            return;
        KeyBean bean = keyView.getBean();
        switch (bean.getKey()) {
            case KeyUtils.KEY_CLOSE://关闭
                closeKeyboard(true);
                break;
            case KeyUtils.KEY_SCALE://键盘缩放
                autoScale();
                break;
            case KeyUtils.KEY_ABC://切换为字母键盘
                show(showNumberKeys ? KeyUtils.TYPE_LETTER_NUMBER : KeyUtils.TYPE_LETTER);
                break;
            case KeyUtils.KEY_DELETE://删除键
                deleteInput();
                break;
            case KeyUtils.KEY_NEXT://聚焦下一个输入框
                autoFocusNextEditText(bean);
                break;
            case KeyUtils.KEY_123://切换为带"ABC"按键的数字键盘
                show(KeyUtils.TYPE_NINE_PALACE_NUMBER_WITH_ABC);
                break;
            case KeyUtils.KEY_AA://切换大小写
                toggleUpperCase(!upperCase);
                break;
            case KeyUtils.KEY_NUM://字母键盘上显隐数字键
                toggleNumberKeys();
                updateNumKey();
                break;
            case KeyUtils.KEY_ENTER:
                break;
            case KeyUtils.KEY_SYMBOL:
                show(KeyUtils.TYPE_SYMBOL);
                break;
            case KeyUtils.KEY_BACK:
                show(showNumberKeys ? KeyUtils.TYPE_LETTER_NUMBER : KeyUtils.TYPE_LETTER);
                break;
            default:
                insertInput(bean);
                break;
        }
    }

    private void enableAllKeys(boolean enable) {
        for (int i = 0; i < viewSparseArray.size(); i++) {
            viewSparseArray.get(viewSparseArray.keyAt(i)).setEnabled(enable);
        }
    }

    private void executeDrag(float dx, float dy) {
        ViewGroup parent = (ViewGroup) getParent();
        int pLeft = parent.getPaddingLeft();
        int pRight = parent.getWidth() - parent.getPaddingRight();
        int pTop = parent.getPaddingTop();
        int pBottom = parent.getHeight() - parent.getPaddingBottom();
        int keyHeightWithSpace = keyHeight + keyVerticalSpace * 2;
        getHitRect(rect);
        if (dx + rect.left < pLeft) {
            dx = pLeft - rect.left;
        }
        if (dx + rect.right > pRight) {
            dx = pRight - rect.right;
        }
        if (dy + rect.top < pTop - (size[1] - keyHeightWithSpace - getPaddingBottom())) {
            dy = pTop - (size[1] - keyHeightWithSpace - getPaddingBottom()) - rect.top;
        }
        if (dy + rect.bottom > pBottom + size[1] - keyHeightWithSpace - getPaddingTop()) {
            dy = pBottom + size[1] - keyHeightWithSpace - getPaddingTop() - rect.bottom;
        }
        switch (curDragSupportModel) {
            case ONLY_HORIZONTAL:
                setTranslationX(getTranslationX() + dx);
                break;
            case ONLY_VERTICAL:
                setTranslationY(getTranslationY() + dy);
                break;
            case ALL_DIRECTION:
                setTranslationX(getTranslationX() + dx);
                setTranslationY(getTranslationY() + dy);
                break;
        }
    }

    private void autoRebound() {
        ViewGroup parent = (ViewGroup) getParent();
        int pLeft = parent.getPaddingLeft();
        int pRight = parent.getWidth() - parent.getPaddingRight();
        int pTop = parent.getPaddingTop();
        int pBottom = parent.getHeight() - parent.getPaddingBottom();
        int keyHeightWithSpace = keyHeight + keyVerticalSpace * 2;
        getHitRect(rect);
        int offset = pBottom - rect.top;
        if (offset > size[1] + keyHeightWithSpace * 2 + getPaddingBottom()) {
            return;
        } else if (offset >= size[1] - keyHeightWithSpace / 2 - getPaddingBottom()) {
            offset = size[1];
        } else if (offset >= keyHeightWithSpace + getPaddingTop()) {
            offset = offset - getPaddingTop();
            int rowCount = offset / keyHeightWithSpace;
            int rest = offset % keyHeightWithSpace;
            if (rest >= keyHeightWithSpace / 2)
                rowCount++;
            offset = rowCount * keyHeightWithSpace + getPaddingTop();
        } else {
            offset = keyHeightWithSpace + getPaddingTop();
        }
        int dy = pBottom - offset - rect.top;
        if (Math.abs(dy) <= keyHeightWithSpace * 2 + getPaddingTop()) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, getTranslationY(), getTranslationY() + dy).setDuration(Math.abs(dy));
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    autoReboundAnimator = animation;
//                    enableAllKeys(false);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    autoReboundAnimator = null;
//                    enableAllKeys(true);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    autoReboundAnimator = null;
//                    enableAllKeys(true);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.start();
        }
    }

    private void autoScale() {
        final float scaleValue = 0.8f;
        if (isScaled()) {
            Animator animator = ObjectAnimator.ofPropertyValuesHolder(this,
                    PropertyValuesHolder.ofFloat(View.SCALE_X, getScaleX(), 1.0f),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, getScaleY(), 1.0f),
                    PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, getTranslationY(), 0)
            ).setDuration(300);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    viewSparseArray.get(KeyUtils.KEY_SCALE).setEnabled(false);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    viewSparseArray.get(KeyUtils.KEY_SCALE).setEnabled(true);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    viewSparseArray.get(KeyUtils.KEY_SCALE).setEnabled(true);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.start();
        } else {
            ScaleAnimation animation = new ScaleAnimation(
                    1.0f, scaleValue,
                    1.0f, scaleValue,
                    ScaleAnimation.RELATIVE_TO_SELF, .5f,
                    ScaleAnimation.RELATIVE_TO_SELF, 1.0f
            );
            animation.setDuration(300);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    viewSparseArray.get(KeyUtils.KEY_SCALE).setEnabled(false);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    setScaleX(scaleValue);
                    setScaleY(scaleValue);
                    viewSparseArray.get(KeyUtils.KEY_SCALE).setEnabled(true);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            startAnimation(animation);
        }
    }

    private boolean isScaled() {
        return getScaleX() < 1.0f;
    }


    //******************************** 键盘显示(或隐藏) start **************************************************//

    public void toggleVisibility() {
        if (getVisibility() == VISIBLE)
            closeKeyboard(true);
        else
            showKeyboard(true);
    }

    public void showKeyboard(boolean withAnimation) {
        if (getVisibility() == VISIBLE)
            return;
        if (isCanDrag() || !withAnimation || getTranslationY() == 0) {
            show();
            return;
        }
        int duration = (int) getTranslationX();
        Animator animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, getTranslationY(), 0).setDuration(Math.abs(duration));
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                setVisibility(VISIBLE);
                enableAllKeys(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                show();
                enableAllKeys(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                show();
                enableAllKeys(true);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    public void closeKeyboard(boolean withAnimation) {
        if (getVisibility() != VISIBLE)
            return;
        if (isCanDrag() || !withAnimation) {
            hide();
            return;
        }
        int offsetY = 0;
        if (getParent() != null) {
            offsetY = ((ViewGroup) getParent()).getHeight() - getTop() + size[1];
        }
        if (offsetY == 0) {
            hide();
            return;
        }
        Animator animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, getTranslationY(), getTranslationY() + offsetY).setDuration(Math.abs(offsetY));
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                enableAllKeys(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                hide();
                enableAllKeys(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                hide();
                enableAllKeys(true);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    private void show() {
        setVisibility(VISIBLE);
        if (keyboardListener != null)
            keyboardListener.onShow(this);
    }

    private void hide() {
        setVisibility(GONE);
        if (keyboardListener != null)
            keyboardListener.onHide(this);
    }

    public void hideIfNecessary() {
        if (getFocusedEditText() == null)
            closeKeyboard(true);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void onResume() {
        final EditText focusedView = getFocusedEditText();
        if (focusedView != null) {
            showKeyboardByEditTextInputType(focusedView);
        }
    }

    public void onPause() {
        closeKeyboard(false);
    }

    public void onDestroy() {
        removeAllEditText();
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }
    }

    //******************************** 键盘显示(或隐藏) end **************************************************//


    private int getStatusBarHeight() {
        int statusBarHeight = 0;
        int resourceId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    private void playClickSound() {
        AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null)
            audioManager.playSoundEffect(SoundEffectConstants.CLICK);
    }

    private void playClickAnimation(KeyView keyView) {
        int key = keyView.getBean().getKey();
        //功能键不执行点击动画
        if (KeyUtils.isFunctionKey(key)
                || KeyUtils.isNotKey(key)
                || key == KeyUtils.KEY_SPACE)
            return;
        Animator oldAnimator = animatorSparseArray.get(key);
        if (oldAnimator != null) {
            oldAnimator.cancel();
        }
        float scaleX = keyView.getScaleX();
        float scaleY = keyView.getScaleY();
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(
                keyView,
                PropertyValuesHolder.ofFloat(View.SCALE_X, scaleX, scaleX * 1.2f, scaleX),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, scaleY, scaleY * 1.2f, scaleY)
        ).setDuration(200);
        animator.addListener(new KeyScaleAnimatorListener(key, scaleX, scaleY));
        animatorSparseArray.put(key, animator);
        animator.start();
    }

    private void toggleUpperCase(boolean upperCase) {
        ensureInitialized();
        this.upperCase = upperCase;
        for (int i = KeyUtils.KEY_A; i <= KeyUtils.KEY_Z; i++) {
            KeyView v = viewSparseArray.get(i);
            if (v != null) {
                v.updateUpperCase(upperCase);
            }
        }
        KeyView upperCaseView = viewSparseArray.get(KeyUtils.KEY_AA);
        if (upperCaseView != null)
            upperCaseView.updateDrawable(upperCase ? R.drawable.key_icon_upper_case : R.drawable.key_icon_lower_case);
    }

    private void updateNumKey() {
        KeyView toggleNumberView = viewSparseArray.get(KeyUtils.KEY_NUM);
        if (toggleNumberView != null)
            toggleNumberView.updateLabel(showNumberKeys ? KeyUtils.KEY_LABEL_HIDE_NUMBER : KeyUtils.KEY_LABEL_SHOW_NUMBER);
    }


    public final void autoFocusNextEditText(KeyBean bean) {
        EditText editText = getFocusedEditText();
        if (editText == null
                || KeyUtils.KEY_LABEL_DONE.equals(bean.getLabel().toString())) {
            hide();
            return;
        }

        editText.clearFocus();
        focusedEditText = null;
        int index = -1;
        for (int i = 0; i < editTexts.size(); i++) {
            if (editText == editTexts.get(i)) {
                index = i;
                break;
            }
        }
        index++;
        editTexts.get(index).requestFocus();
        showKeyboardByEditTextInputType(editTexts.get(index));
    }

    public final void toggleNumberKeys() {
        if (KeyUtils.TYPE_LETTER_NUMBER.equals(getKeyboardType())) {
            showNumberKeys = false;
            show(KeyUtils.TYPE_LETTER);
            return;
        }
        if (KeyUtils.TYPE_LETTER.equals(getKeyboardType())) {
            showNumberKeys = true;
            show(KeyUtils.TYPE_LETTER_NUMBER);
        }
    }

    public void setCreateKeyListener(onCreateKeyListener createKeyListener) {
        this.createKeyListener = createKeyListener;
    }

    public void setKeyboardListener(OnKeyboardListener keyboardListener) {
        this.keyboardListener = keyboardListener;
    }

    public void setKeyDownListener(OnKeyDownListener keyDownListener) {
        this.keyDownListener = keyDownListener;
    }

    public void addAllInputView(View view) {
        if (view == null || view instanceof KeyboardView) return;
        if (view instanceof EditText) {
            addInputView((EditText) view);
            return;
        }
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                addAllInputView(group.getChildAt(i));
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void addInputView(@NonNull EditText editText) {
        if (editTexts.contains(editText))
            return;
        editTexts.add(editText);
        editText.setShowSoftInputOnFocus(false);
        editText.setOnTouchListener(touchListener);
    }

    public void removeAllInputView(View view) {
        if (view == null || view instanceof KeyboardView) return;
        if (view instanceof EditText) {
            removeInputView((EditText) view);
            return;
        }
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                removeAllInputView(group.getChildAt(i));
            }
        }
    }

    public void removeInputView(@NonNull EditText editText) {
        editTexts.remove(editText);
        if (focusedEditText != null && focusedEditText == editText) {
            focusedEditText = null;
            hide();
        }
    }

    public void removeAllEditText() {
        focusedEditText = null;
        editTexts.clear();
    }

    private void showKeyboardByEditTextInputType(EditText editText) {
        int inputType = editText.getInputType();
        int imeOptions = editText.getImeOptions();
        if (inputType == InputType.TYPE_NULL) {
            hide();
            return;
        }
        int action = InputType.TYPE_MASK_CLASS & inputType;
        switch (action) {
            case InputType.TYPE_CLASS_NUMBER:
            case InputType.TYPE_CLASS_PHONE:
            case InputType.TYPE_CLASS_DATETIME:
                show(KeyUtils.TYPE_NINE_PALACE_NUMBER);
                break;
            default:
                String keyboardType = getKeyboardType();
                if (TextUtils.isEmpty(keyboardType)
                        || KeyUtils.TYPE_NINE_PALACE_NUMBER.equals(keyboardType))
                    keyboardType = showNumberKeys ? KeyUtils.TYPE_LETTER_NUMBER : KeyUtils.TYPE_LETTER;
                show(keyboardType);
                break;
        }
        if (editTexts.isEmpty()) {
            editTexts.size();
        }
        if (isLastEditText(editText)
                || (imeOptions & EditorInfo.IME_MASK_ACTION) == EditorInfo.IME_ACTION_DONE) {
            viewSparseArray.get(KeyUtils.KEY_NEXT).updateLabel(KeyUtils.KEY_LABEL_DONE);
        } else {
            viewSparseArray.get(KeyUtils.KEY_NEXT).updateLabel(KeyUtils.KEY_LABEL_NEXT);
        }
    }

    private boolean isLastEditText(@NonNull EditText editText) {
        if (!editTexts.isEmpty()) {
            if (editText == editTexts.get(editTexts.size() - 1))
                return true;
        }
        return false;
    }

    private void show(@KeyUtils.KeyboardType String keyboardType) {
        final String lastKeyboardType = getKeyboardType();
        if (!keyboardType.equals(lastKeyboardType)) {
            setKeyboardType(keyboardType);
            if ((KeyUtils.TYPE_NINE_PALACE_NUMBER.equals(lastKeyboardType)
                    || KeyUtils.TYPE_NINE_PALACE_NUMBER.equals(keyboardType))
                    && (getTranslationX() != 0 || getTranslationY() != 0))
                executeKeyboardReLocation(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        createKeys();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            else
                createKeys();
        }
        showKeyboard(true);
    }

    private void setKeyboardType(@KeyUtils.KeyboardType String keyboardType) {
        this.keyboardType = keyboardType;
        if (keyboardType.equals(KeyUtils.TYPE_LETTER_NUMBER)) {
            showNumberKeys = true;
        } else if (keyboardType.equals(KeyUtils.TYPE_LETTER)) {
            showNumberKeys = false;
        }
    }

    public String getKeyboardType() {
        return keyboardType;
    }

    private boolean isCanDrag() {
        return !TextUtils.equals(curDragSupportModel, NONE);
    }

    public void setDragSupportModel(@DragSupportModel String curDragSupportModel) {
        this.curDragSupportModel = curDragSupportModel;
    }

    public void setDefaultUpperCase(boolean upperCase) {
        this.upperCase = upperCase;
    }

    public void resetKeys() {
        size[0] = 0;
        size[1] = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof ViewGroup) {
                ((ViewGroup) child).removeAllViews();
            }
        }
        removeAllViews();
//        SparseArray<KeyView> cache = viewSparseArray.clone();
//        viewSparseArray.clear();
//        return cache;
    }

    private void ensureInitialized() {
        if (isInEditMode())
            return;
        if (getParent() == null)
            throw new IllegalStateException("Initialize first.");
    }

    public interface OnKeyboardListener {
        void onShow(KeyboardView keyBoardView);

        void onHide(KeyboardView keyBoardView);
    }

    public interface OnKeyDownListener {
        boolean onKeyDown(KeyboardView keyBoardView, KeyView keyView);
    }

    public interface onCreateKeyListener {
        /**
         * @param keyboardType current keyboard type
         * @param key          the key of view was creating
         * @return label text sie. It's unit is dp.
         */
        float getKeyTextSize(@KeyUtils.KeyboardType String keyboardType, @KeyUtils.KeyCode int key);

        void onKeyCreated(boolean isCachedView, KeyView keyView, KeyBean bean);
    }

    private class KeyScaleAnimatorListener implements Animator.AnimatorListener {
        private int key;
        private float scaleX;
        private float scaleY;

        public KeyScaleAnimatorListener(int key, float scaleX, float scaleY) {
            this.key = key;
            this.scaleX = scaleX;
            this.scaleY = scaleY;
        }

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            animatorSparseArray.remove(key);
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            animatorSparseArray.remove(key);
            KeyView view = viewSparseArray.get(key);
            if (view != null) {
                view.setScaleX(scaleX);
                view.setScaleY(scaleY);
            }
        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }
}
