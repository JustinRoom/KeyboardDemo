package jsc.kit.keyboard;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * create time: 2019/3/27 15:34 Wednesday
 *
 * @author jsc
 */
public class KeyView extends FrameLayout {

    private TextView tvKey;
    private ImageView ivKey;
    private KeyBean bean;

    public KeyView(Context context) {
        this(context, null);
    }

    public KeyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.key_key_view, this);
        tvKey = findViewById(R.id.tv_key);
        ivKey = findViewById(R.id.iv_key);
    }

    public KeyBean getBean() {
        return bean;
    }

    public void setBean(KeyBean bean) {
        this.bean = bean;
        if (bean.getDrawable() != -1) {
            ivKey.setVisibility(VISIBLE);
            tvKey.setVisibility(INVISIBLE);
            ivKey.setImageResource(bean.getDrawable());
            tvKey.setText("");
        } else {
            ivKey.setVisibility(INVISIBLE);
            tvKey.setVisibility(VISIBLE);
            ivKey.setImageDrawable(null);
            tvKey.setText(bean.getLabel());
        }
    }

    public void updateUpperCase(boolean upperCase) {
        if (bean != null) {
            if (upperCase) {
                bean.setLabel(bean.getLabel().toString().toUpperCase());
                bean.setValue(bean.getValue().toString().toUpperCase());
            } else {
                bean.setLabel(bean.getLabel().toString().toLowerCase());
                bean.setValue(bean.getValue().toString().toLowerCase());
            }
            tvKey.setText(bean.getLabel());
        }
    }

    public void updateLabel(CharSequence label) {
        if (bean != null) {
            bean.setLabel(label);
            tvKey.setText(label);
        }
    }

    public void updateDrawable(@DrawableRes int drawable) {
        if (bean != null) {
            bean.setDrawable(drawable);
            ivKey.setImageResource(drawable);
        }
    }

    public void setTypeface(Typeface typeface) {
        tvKey.setTypeface(typeface == null ? Typeface.DEFAULT : typeface);
    }

    public ImageView getImageKeyView() {
        return ivKey;
    }

    public TextView getTextKeyView() {
        return tvKey;
    }
}
