package jsc.exam.com.keyboard.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jsc.exam.com.keyboard.R;
import jsc.exam.com.keyboard.widgets.dialog.InputDialog;
import jsc.kit.keyboard.KeyBoardView;
import jsc.kit.keyboard.KeyUtils;

/**
 * <br>Email:1006368252@qq.com
 * <br>QQ:1006368252
 * create time: 2019/2/24 09:34 Sunday
 *
 * @author jsc
 */
public class KeyboardFragment extends BaseFragment implements View.OnClickListener {

    KeyBoardView keyboardView = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_keyboard, container, false);
        keyboardView = new KeyBoardView(root.getContext());
        int keyWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics());
        int keyHeight = keyWidth * 3 / 4;
        int keySpace = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        keyboardView.initKeyBoard(keyWidth, keyHeight, keySpace);
        keyboardView.addAllInputView(root);
        keyboardView.setCurDragSupportModel(KeyBoardView.ALL_DIRECTION);
//        keyboardView.setNumberKeyBoardType(KeyUtils.TYPE_HORIZONTAL_NUMBER);
//        keyboardView.setSupportMoving(false);
//        keyboardView.setCreateKeyListener(new KeyBoardView.onCreateKeyListener() {
//            @Override
//            public float getKeyTextSize(String keyboardType, int key) {
//                if (KeyUtils.isNumberKey(key))
//                    return 20;
//                if (KeyUtils.isLetterKey(key)
//                        || key == KeyUtils.KEY_AA
//                        || key == KeyUtils.KEY_123)
//                    return 16;
//                return 14;
//            }
//        });
        KeyUtils.init(getActivity(), keyboardView);

        root.findViewById(R.id.btn_toggle).setOnClickListener(this);
        root.findViewById(R.id.btn_dialog).setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_dialog) {
            showDialog();
        } else {
            keyboardView.toggleVisibility();
        }
    }

    @Override
    void onLoadData(Context context) {

    }

    @Override
    public void onResume() {
        super.onResume();
        keyboardView.onResume();
    }

    @Override
    public void onPause() {
        Log.i("KeyboardFragment", "onPause: ");
        keyboardView.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i("KeyboardFragment", "onStop: ");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        keyboardView.onDestroy();
        super.onDestroy();
    }

    private void showDialog() {
        keyboardView.onPause();
        InputDialog.newInstance().show(getChildFragmentManager(), "dialog");
    }
}
