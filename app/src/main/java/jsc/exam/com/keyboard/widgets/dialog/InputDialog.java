package jsc.exam.com.keyboard.widgets.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import jsc.exam.com.keyboard.R;
import jsc.kit.keyboard.KeyboardView;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * create time: 2019/3/26 19:00
 *
 * @author jsc
 */
public class InputDialog extends DialogFragment {

    private KeyboardView keyboardView = null;

    public static InputDialog newInstance() {

        Bundle args = new Bundle();

        InputDialog fragment = new InputDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_custom_key_board, container, false);
        initDialog(inflater.getContext());
        initView(inflater.getContext(), root);
        return root;
    }

    protected void initDialog(Context context) {
        Dialog dialog = getDialog();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(null);
            window.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.CENTER;
            //设置Dialog宽度匹配屏幕宽度
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            //设置Dialog高度自适应
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            //将属性设置给窗体
            window.setAttributes(lp);
        }
    }

    private void initView(Context context, View root) {
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //463:327
        int width = context.getResources().getDisplayMetrics().widthPixels * 463 / 667;
        int height = context.getResources().getDisplayMetrics().heightPixels * 327 / 375;
        View contentContainer = root.findViewById(R.id.content_container);
        ViewGroup.LayoutParams params = contentContainer.getLayoutParams();
        params.width = width;
        params.height = height;
        //
        keyboardView = root.findViewById(R.id.key_board_view);
        keyboardView.addAllInputView(root);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        keyboardView.onPause();
        super.onDismiss(dialog);
    }

    @Override
    public void onDestroy() {
        keyboardView.onDestroy();
        super.onDestroy();
    }
}
