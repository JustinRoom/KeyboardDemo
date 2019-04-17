package jsc.kit.keyboard;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

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
public class KeyBoardView extends LinearLayout {

    //存储键盘上的按键view
    private SparseArray<KeyView> viewSparseArray = new SparseArray<>();
    //使用该软键盘的输入框管理
    private List<EditText> editTexts = new ArrayList<>(20);
    //当前聚焦的输入框
    private EditText focusedEditText = null;
    //输入框touch后聚焦
    private View.OnTouchListener touchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (v instanceof EditText) {
                showKeyBoardByEditTextInputType((EditText) v);
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
    //是否支持拖动
    private boolean supportMoving = true;
    //键盘拖动时touch坐标
    private float touchX;
    private float touchY;
    //是否处于拖动模式
    private boolean intoMoveModel;
    private int touchedPointerId = -1;
    //按键的基本宽度
    private int keyWidth;
    //按键的基本高度
    private int keyHeight;
    //按键的间隙
    private int keySpace;
    //存储键盘的尺寸。size[0]为键盘宽度，size[1]为键盘高度
    private int[] size = new int[2];
    private Rect rect = new Rect();
    //数字键盘样式：水平样式、9宫格样式
    private @KeyUtils.KeyBoardType String numberKeyBoardType;
    //键盘类型。目前只支持三种：数字键盘、字母键盘、数字+字混合键盘
    private String keyBoardType = "";
    //是否为大写模式
    private boolean upperCase = false;
    //当键盘类型为数字+字混合键盘时，是否显示数字按键
    private boolean showNumberKeys = false;
    //键盘的显隐监听
    private OnKeyBoardListener keyBoardListener = null;
    //自定义的按键监听
    private OnKeyDownListener keyDownListener = null;

    //*********************  Constructors ***********************************//
    public KeyBoardView(Context context) {
        this(context, null);
    }

    public KeyBoardView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyBoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        int defaultKeyWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 44, context.getResources().getDisplayMetrics());
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.KeyBoardView, defStyleAttr, 0);
        int keyWidth = a.getDimensionPixelSize(R.styleable.KeyBoardView_keyWidth, defaultKeyWidth);
        int keyHeight = a.getDimensionPixelSize(R.styleable.KeyBoardView_keyHeight, 0);
        int keySpace = a.getDimensionPixelSize(R.styleable.KeyBoardView_keySpace, 5);
        int boardType = a.getInt(R.styleable.KeyBoardView_keyBoardType, 0);
        a.recycle();
        if (keyHeight <= 0)
            keyHeight = keyWidth * 3 / 4;
        initKeyBoard(keyWidth, keyHeight, keySpace);
        setNumberKeyBoardType(KeyUtils.TYPE_NINE_PALACE_NUMBER);
        if (isInEditMode())
            switch (boardType) {
                case 0:
                    show(getNumberKeyBoardType());
                    break;
                case 1:
                    showNumberKeys = false;
                    show(KeyUtils.TYPE_LETTER);
                    break;
                case 2:
                    showNumberKeys = true;
                    show(KeyUtils.TYPE_LETTER_NUMBER);
                    break;
            }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //如果键盘是被缩小了，则不拦截touch事件
        if (isScaled() || !supportMoving)
            return super.onInterceptTouchEvent(ev);
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                //当没有进入键盘拖动模式
                if (!intoMoveModel) {
                    //这里使用PointerId防止多手指touch混乱问题
                    touchedPointerId = ev.getPointerId(0);
                    touchX = ev.getX();
                    touchY = ev.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!intoMoveModel && touchedPointerId == ev.getPointerId(0)) {
                    float tempX = ev.getX();
                    float tempY = ev.getY();
                    float dx = touchX - tempX;
                    float dy = touchY - tempY;
                    //当在任意方向上滑动大于等于8pixels时进入键盘拖动模式
                    if (Math.abs(dx) >= 8 || Math.abs(dy) >= 8) {
                        intoMoveModel = true;
                        //进入拖动模式后，所有按键不可用
                        enableAllKeys(false);
                    } else {
                        touchX = tempX;
                        touchY = tempY;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return intoMoveModel;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //如果键盘是被缩小了，则不处理touch事件
        if (isScaled() || !supportMoving)
            return true;
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
                    executeMove(-dx, -dy);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (intoMoveModel && touchedPointerId == event.getPointerId(0)) {
                    //抬起手指时，键盘智能复位
                    autoMove();
                    //退出拖动模式
                    intoMoveModel = false;
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

    private void setKeyBoardType(@KeyUtils.KeyBoardType String keyBoardType) {
        this.keyBoardType = keyBoardType;
        if (keyBoardType.equals(KeyUtils.TYPE_LETTER_NUMBER)) {
            showNumberKeys = true;
        } else if (keyBoardType.equals(KeyUtils.TYPE_LETTER)) {
            showNumberKeys = false;
        }
    }

    public void initKeyBoard(int keyWidth) {
        initKeyBoard(keyWidth, keyWidth * 3 / 4, 5);
    }

    public void initKeyBoard(int keyWidth, int keyHeight, int space) {
        this.keyWidth = keyWidth;
        this.keyHeight = keyHeight;
        this.keySpace = space;
        setPadding(space, space, space, space);
    }

    /**
     * 获取键盘的尺寸。
     *
     * @see #size
     */
    public int[] getKeyBoardSize() {
        return size;
    }

    /**
     * 创建按键
     */
    private void createKeys() {
        final SparseArray<KeyView> cache = resetKeys();
        List<List<KeyBean>> keys = KeyUtils.loadKeys(keyBoardType);
        int row = keys.size();
        int column = 0;
        switch (keyBoardType) {
            case KeyUtils.TYPE_LETTER:
            case KeyUtils.TYPE_LETTER_NUMBER:
                column = 11;
                break;
            case KeyUtils.TYPE_HORIZONTAL_NUMBER:
                column = 12;
                break;
            case KeyUtils.TYPE_NINE_PALACE_NUMBER:
                column = 5;
                break;
        }
        size[0] = keyWidth * column + (column + 1) * keySpace;
        size[1] = keyHeight * row + (row + 1) * keySpace;
        for (int i = 0; i < row; i++) {
            List<KeyBean> tempKeys = keys.get(i);
            LinearLayout layout = new LinearLayout(getContext());
            layout.setOrientation(HORIZONTAL);
            layout.setWeightSum(column);
            addView(layout, new LayoutParams(size[0], LayoutParams.WRAP_CONTENT));
            for (int j = 0; j < tempKeys.size(); j++) {
                KeyBean bean = tempKeys.get(j);
                KeyView keyView = createKeyView(cache, layout, bean, keyHeight, keySpace);
                if (!KeyUtils.isNotKey(bean.getKey())
                        && bean.getKey() != KeyUtils.KEY_SCALE)
                    viewSparseArray.put(bean.getKey(), keyView);
            }
        }
    }

    private KeyView createKeyView(final SparseArray<KeyView> cache, LinearLayout layout, KeyBean bean, int keyHeight, int margin) {
        int key = bean.getKey();
        KeyView keyView = cache.get(key);
        if (keyView == null) {
            keyView = new KeyView(getContext());
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
        }
        LayoutParams params = new LayoutParams(0, keyHeight, bean.getHorizontalWeight());
        params.leftMargin = margin;
        params.rightMargin = margin;
        params.topMargin = margin;
        params.bottomMargin = margin;
        layout.addView(keyView, params);
        return keyView;
    }

    private float getKeyTextSize(int key) {
        if (KeyUtils.isNumberKey(key)
                || KeyUtils.isLetterKey(key))
            return 18;
        if (key == KeyUtils.KEY_AA)
            return 16;
        return 14;
    }

    private int getKeyBackground(int key) {
        if (key == KeyUtils.KEY_ABC
                || key == KeyUtils.KEY_AA
                || key == KeyUtils.KEY_123
                || key == KeyUtils.KEY_DELETE
                || key == KeyUtils.KEY_NEXT)
            return R.drawable.key_special_key_background_ripple;
        return R.drawable.key_normal_key_background_ripple;
    }

    private void executeKeyBoardReLocation(Animator.AnimatorListener listener) {
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
            case KeyUtils.KEY_AA://切换大小写
                toggleUpperCase();
                break;
            case KeyUtils.KEY_ABC://切换字母键
                changeToLetterKeyBoard();
                break;
            case KeyUtils.KEY_NUM://字母键盘上显隐数字键
                toggleNumberKeys();
                break;
            case KeyUtils.KEY_CLOSE://关闭
                hide();
                break;
            case KeyUtils.KEY_DELETE://删除键
                deleteInput();
                break;
            case KeyUtils.KEY_NEXT://聚焦下一个输入框
                autoFocusNextEditText(bean);
                break;
            case KeyUtils.KEY_123://切换为数字键
                changeToNumberKeyBoard();
                break;
            case KeyUtils.KEY_SCALE://键盘缩放
                autoScale();
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

    private void executeMove(float dx, float dy) {
        float totalX = getTranslationX() + dx;
        float totalY = getTranslationY() + dy;
        //限制拖动区域
        if (getParent() != null) {
            View parent = (View) getParent();
            if (size[0] <= parent.getWidth()) {
                float minX = 0 - getLeft();
                float maxX = parent.getWidth() - getRight();
                if (totalX < minX) totalX = minX;
                if (totalX > maxX) totalX = maxX;
            }
            if (size[1] <= parent.getHeight()) {
                float minY = getStatusBarHeight() - getTop();
//                        float maxY = (parent.getHeight() + (size[1] - keyHeight + keySpace * 2)) - getBottom();
                if (totalY < minY) totalY = minY;
//                        if (totalY > maxY) totalY = maxY;
            }
        }
        setTranslationX(totalX);
        setTranslationY(totalY);
    }

    private void autoMove() {
        getLocalVisibleRect(rect);
        int h = rect.height();
        int kh = keyHeight + keySpace * 2;
        if (h < size[1] && getTranslationY() > 0) {
            int index = h / kh;
            if (h >= index * kh + kh / 2) index++;
            if (index == 0) index = 1;
            int yOffset = h - (kh * index + keySpace);
            if (yOffset != 0) {
                ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, getTranslationY(), getTranslationY() + yOffset)
                        .setDuration(300).start();
            }
        }
    }

    private void autoScale() {
        float scaleValue = 0.8f;
        if (isScaled()) {
            if (getTranslationX() == 0 && getTranslationY() == 0) {
                ObjectAnimator.ofPropertyValuesHolder(this,
                        PropertyValuesHolder.ofFloat(View.SCALE_X, getScaleX(), 1.0f),
                        PropertyValuesHolder.ofFloat(View.SCALE_Y, getScaleY(), 1.0f)
                ).setDuration(300).start();
                return;
            }
            float transX = size[0] * scaleValue / 2.0f - size[0] / 2.0f;
            float transY = size[1] * scaleValue / 2.0f - size[1] / 2.0f;
            ObjectAnimator.ofPropertyValuesHolder(this,
                    PropertyValuesHolder.ofFloat(View.SCALE_X, getScaleX(), 1.0f),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, getScaleY(), 1.0f),
                    PropertyValuesHolder.ofFloat(View.TRANSLATION_X, getTranslationX(), getTranslationX() + transX),
                    PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, getTranslationY(), getTranslationY() + transY)
            ).setDuration(300).start();
        } else {
            float transX = size[0] / 2.0f - size[0] * scaleValue / 2;
            float transY = size[1] / 2.0f - size[1] * scaleValue / 2;
            ObjectAnimator.ofPropertyValuesHolder(this,
                    PropertyValuesHolder.ofFloat(View.SCALE_X, getScaleX(), scaleValue),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, getScaleY(), scaleValue),
                    PropertyValuesHolder.ofFloat(View.TRANSLATION_X, getTranslationX(), getTranslationX() + transX),
                    PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, getTranslationY(), getTranslationY() + transY)
            ).setDuration(300).start();
        }
    }

    private boolean isScaled() {
        return getScaleX() < 1.0f;
    }

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
        if (KeyUtils.isNumberKey(key)
                || KeyUtils.isLetterKey(key)
                || key == KeyUtils.KEY_DOT
                || key == KeyUtils.KEY_SIGNED) {
            float scaleX = keyView.getScaleX();
            float scaleY = keyView.getScaleY();
            ObjectAnimator.ofPropertyValuesHolder(
                    keyView,
                    PropertyValuesHolder.ofFloat(View.SCALE_X, scaleX, scaleX * 1.2f, scaleX),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, scaleY, scaleY * 1.2f, scaleY)
            ).setDuration(200).start();
        }
    }

    public final void toggleUpperCase() {
        ensureInitialized();
        upperCase = !upperCase;
        for (int i = KeyUtils.KEY_A; i <= KeyUtils.KEY_Z; i++) {
            KeyView v = viewSparseArray.get(i);
            if (v != null) {
                v.updateUpperCase(upperCase);
            }
        }
        viewSparseArray.get(KeyUtils.KEY_AA).updateLabel(upperCase ? "aA" : "Aa");
    }

    public final void autoFocusNextEditText(KeyBean bean) {
        EditText editText = getFocusedEditText();
        if (editText == null
                || KeyUtils.KEY_LABEL_FINISH.equals(bean.getLabel().toString())) {
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
        showKeyBoardByEditTextInputType(editTexts.get(index));
    }

    public final void changeToLetterKeyBoard() {
        setKeyBoardType(showNumberKeys ? KeyUtils.TYPE_LETTER_NUMBER : KeyUtils.TYPE_LETTER);
        if (getTranslationX() == 0 && getTranslationY() == 0) {
            createKeys();
            return;
        }
        executeKeyBoardReLocation(new Animator.AnimatorListener() {
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
    }

    public final void toggleNumberKeys() {
        if (keyBoardType.equals(KeyUtils.TYPE_LETTER_NUMBER)) {
            setKeyBoardType(KeyUtils.TYPE_LETTER);
            createKeys();
            showNumberKeys = false;
            return;
        }
        if (keyBoardType.equals(KeyUtils.TYPE_LETTER)) {
            setKeyBoardType(KeyUtils.TYPE_LETTER_NUMBER);
            createKeys();
            showNumberKeys = true;
        }
    }

    public final void changeToNumberKeyBoard() {
        setKeyBoardType(getNumberKeyBoardType());
        createKeys();
        if (getTranslationY() > 0) {
            ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, getTranslationY(), 0).setDuration(300).start();
        }
    }

    public void setKeyBoardListener(OnKeyBoardListener keyBoardListener) {
        this.keyBoardListener = keyBoardListener;
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

    private void showKeyBoardByEditTextInputType(EditText editText) {
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
                show(getNumberKeyBoardType());
                break;
            default:
                show(showNumberKeys ? KeyUtils.TYPE_LETTER_NUMBER : KeyUtils.TYPE_LETTER);
                break;
        }
        if (editTexts.isEmpty()) {
            editTexts.size();
        }
        if (isLastEditText(editText)
                || (imeOptions & EditorInfo.IME_MASK_ACTION) == EditorInfo.IME_ACTION_DONE) {
            viewSparseArray.get(KeyUtils.KEY_NEXT).updateLabel(KeyUtils.KEY_LABEL_FINISH);
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

    private void show(@KeyUtils.KeyBoardType String keyBoardType) {
        if (!this.keyBoardType.equals(keyBoardType)) {
            switch (keyBoardType) {
                case KeyUtils.TYPE_HORIZONTAL_NUMBER:
                case KeyUtils.TYPE_NINE_PALACE_NUMBER:
                    changeToNumberKeyBoard();
                    break;
                case KeyUtils.TYPE_LETTER:
                case KeyUtils.TYPE_LETTER_NUMBER:
                    changeToLetterKeyBoard();
                    break;
            }
        }
        show();
    }

    public void show() {
        if (getVisibility() != VISIBLE) {
            setVisibility(VISIBLE);
            if (keyBoardListener != null)
                keyBoardListener.onShow(this);
        }
    }

    public void hide() {
        if (getVisibility() == VISIBLE) {
            setVisibility(GONE);
            if (keyBoardListener != null)
                keyBoardListener.onHide(this);
        }
    }

    public void toggleVisibility() {
        if (getVisibility() == VISIBLE)
            hide();
        else
            show();
    }

    public void hideIfNecessary() {
        if (getFocusedEditText() == null)
            hide();
    }

    public String getNumberKeyBoardType() {
        return numberKeyBoardType;
    }

    /**
     * @param numberKeyBoardType one of {@link KeyUtils#TYPE_HORIZONTAL_NUMBER、{@link KeyUtils#TYPE_NINE_PALACE_NUMBER
     */
    public void setNumberKeyBoardType(@KeyUtils.KeyBoardType String numberKeyBoardType) {
        if (KeyUtils.TYPE_HORIZONTAL_NUMBER.equals(numberKeyBoardType)
                || KeyUtils.TYPE_NINE_PALACE_NUMBER.equals(numberKeyBoardType))
            this.numberKeyBoardType = numberKeyBoardType;
        else
            throw new IllegalArgumentException("Must be one of TYPE_HORIZONTAL_NUMBER、TYPE_NINE_PALACE_NUMBER.");
    }

    public boolean isSupportMoving() {
        return supportMoving;
    }

    public void setSupportMoving(boolean supportMoving) {
        this.supportMoving = supportMoving;
    }

    public void setNumberKeyTextSize(float textDpSize) {
        ensureInitialized();
        for (int i = KeyUtils.KEY_0; i <= KeyUtils.KEY_9; i++) {
            setKeyTextSize(i, textDpSize);
        }
    }

    public void setNumberKeyBackground(@DrawableRes int background) {
        ensureInitialized();
        for (int i = KeyUtils.KEY_0; i <= KeyUtils.KEY_9; i++) {
            setKeyBackground(i, background);
        }
    }

    public void setLetterKeyTextSize(float textDpSize) {
        ensureInitialized();
        for (int i = KeyUtils.KEY_A; i <= KeyUtils.KEY_Z; i++) {
            setKeyTextSize(i, textDpSize);
        }
    }

    public void setLetterKeyBackground(@DrawableRes int background) {
        ensureInitialized();
        for (int i = KeyUtils.KEY_A; i <= KeyUtils.KEY_Z; i++) {
            setKeyBackground(i, background);
        }
    }

    public void setKeyTextSize(int key, float textDpSize) {
        ensureInitialized();
        KeyView view = viewSparseArray.get(key);
        if (view != null) {
            view.getTextKeyView().setTextSize(TypedValue.COMPLEX_UNIT_DIP, textDpSize);
        }
    }

    public void setKeyBackground(@KeyUtils.Key int key, @DrawableRes int background) {
        ensureInitialized();
        KeyView view = viewSparseArray.get(key);
        if (view != null) {
            view.getTextKeyView().setBackgroundResource(background);
        }
    }

    public SparseArray<KeyView> resetKeys() {
        size[0] = 0;
        size[1] = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof ViewGroup) {
                ((ViewGroup) child).removeAllViews();
            }
        }
        removeAllViews();
        SparseArray<KeyView> cache = viewSparseArray.clone();
        viewSparseArray.clear();
        return cache;
    }

    public boolean isShowing() {
        return viewSparseArray.size() > 0 && getVisibility() == VISIBLE;
    }

    public void onResume() {
        final EditText focusedView = getFocusedEditText();
        if (focusedView != null) {
            showKeyBoardByEditTextInputType(focusedView);
        }
    }

    public void onPause() {
        hide();
    }

    public void onDestroy() {
        removeAllEditText();
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }
    }

    private void ensureInitialized() {
        if (getParent() == null)
            throw new IllegalStateException("Initialize first.");
    }

    public interface OnKeyBoardListener {
        void onShow(KeyBoardView keyBoardView);

        void onHide(KeyBoardView keyBoardView);
    }

    public interface OnKeyDownListener {
        boolean onKeyDown(KeyBoardView keyBoardView, KeyView keyView);
    }
}
