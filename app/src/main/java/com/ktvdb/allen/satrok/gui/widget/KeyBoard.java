package com.ktvdb.allen.satrok.gui.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.PixelFormat;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.databinding.KeyboardBinding;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Allen on 15/7/20.
 */
public class KeyBoard extends FrameLayout implements KeyboardView.OnKeyboardActionListener,
                                                     HWBoardView.OnTextSelectedLinstener,
                                                     TextWatcher
{

    private boolean clearFormUser = false;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        mBtnDelAll.setVisibility(mInputText.getText().length() > 0 ? VISIBLE : GONE);
        if (clearFormUser && s.length() == 0) return;
        if (onTextChangedLinstener != null)
        {
            onTextChangedLinstener.onTextChanged(s.toString());
        }
    }

    @Override
    public void afterTextChanged(Editable s)
    {

    }

    public interface OnTextChangedLinstener
    {
        void onTextChanged(String text);
    }

    //    private KeyboardModel   model;
    private KeyboardBinding mBinding;

    @Bind(R.id.inputText)
    EditText     mInputText;
    @Bind(R.id.btn_del_all)
    ImageButton  mBtnDelAll;
    @Bind(R.id.input)
    LinearLayout mInput;
    @Bind(R.id.keyboard_view)
    KeyboardView mKeyboardView;
    @Bind(R.id.hwBoardView)
    HWBoardView  hwBoardView;

    RadioGroup radioGroup;

    private PopupWindow mPopupWindow;

    private OnTextChangedLinstener onTextChangedLinstener;

    public OnTextChangedLinstener getOnTextChangedLinstener()
    {
        return onTextChangedLinstener;
    }

    public void setOnTextChangedLinstener(OnTextChangedLinstener onTextChangedLinstener)
    {
        this.onTextChangedLinstener = onTextChangedLinstener;
    }

    public KeyBoard(Context context)
    {
        super(context);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                                           R.layout.keyboard,
                                           this,
                                           true);
        ButterKnife.bind(this);
        mInputText.addTextChangedListener(this);
        mKeyboardView.setKeyboard(new Keyboard(context, R.xml.search1));
        mKeyboardView.setOnKeyboardActionListener(this);
        mInputText.setInputType(InputType.TYPE_NULL);
        hwBoardView.setLinstener(this);
    }

    @OnClick(R.id.inputType)
    void inputTypeSelect(View view)
    {
        if (radioGroup == null)
        {
            radioGroup = (RadioGroup) View.inflate(getContext(),
                                                   R.layout.keyboard_language_popup,
                                                   null);

            radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                if (checkedId == R.id.rb_pinyin)
                {
                    hwBoardView.setVisibility(GONE);
                    mKeyboardView.setVisibility(VISIBLE);
                }
                else
                {
                    hwBoardView.setVisibility(VISIBLE);
                    mKeyboardView.setVisibility(GONE);
                }

                if (mPopupWindow != null)
                {
                    mPopupWindow.dismiss();
                }
            });
        }


        mPopupWindow = new PopupWindow(getContext());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setContentView(radioGroup);
        mPopupWindow.update();
        mPopupWindow.showAsDropDown(view, 0, -178);
    }


    @OnClick(R.id.btn_del_all)
    void clear()
    {
        clearFormUser = false;
        Editable editable = mInputText.getText();
        editable.clear();
    }

    @Override
    public void onPress(int primaryCode)
    {

    }

    @Override
    public void onRelease(int primaryCode)
    {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes)
    {
        Editable editable = mInputText.getText();

        if (primaryCode == Keyboard.KEYCODE_DELETE)
        {
            if (editable != null && editable.length() > 0)
            {
                editable.delete(editable.length() - 1, editable.length());
            }
        }
        else
        {
            assert editable != null;
            editable.append(Character.toString((char) primaryCode));
        }


    }

    @Override
    public void onText(CharSequence text)
    {

    }

    @Override
    public void swipeLeft()
    {

    }

    @Override
    public void swipeRight()
    {

    }

    @Override
    public void swipeDown()
    {

    }

    @Override
    public void swipeUp()
    {

    }

    @Override
    public void OnTextSelectedLinstener(String text)
    {
        int start = mInputText.getSelectionStart();
        mInputText.getText().insert(start, text);
//        model.addSearchText(text);
//        mBtnDelAll.setVisibility(model.getSearchText().length() > 0 ? VISIBLE : GONE);
    }

    @OnClick(R.id.rootView)
    void closeKeyboard()
    {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        windowManager.removeView(this);
    }


    private static KeyBoard keyBoard;

    public static void showKeyboard(Context context, OnTextChangedLinstener textChangedLinstener)
    {
        showKeyboard(context, textChangedLinstener, "");
    }

    public static void showKeyboard(Context context,
                                    OnTextChangedLinstener textChangedLinstener,
                                    String searchText)
    {
        showKeyboard(context, textChangedLinstener, searchText, true);
    }

    public static void showKeyboard(Context context,
                                    OnTextChangedLinstener textChangedLinstener,
                                    String searchText,
                                    boolean isPinyin)
    {
        if (keyBoard == null)
        {
            keyBoard = new KeyBoard(context);
        }
        keyBoard.clearFormUser = true;
        keyBoard.mBinding.inputText.setText("");
        keyBoard.setOnTextChangedLinstener(textChangedLinstener);
        keyBoard.mKeyboardView.setVisibility(isPinyin ? VISIBLE : GONE);
        keyBoard.hwBoardView.setVisibility(isPinyin ? GONE : VISIBLE);
        WindowManager              windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams mParams       = new WindowManager.LayoutParams();
        mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;// 系统提示window
        mParams.format = PixelFormat.TRANSLUCENT;// 支持透明
        mParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;// 焦点
        mParams.width = WindowManager.LayoutParams.MATCH_PARENT;//窗口的宽和高
        mParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        windowManager.addView(keyBoard, mParams);
    }
}
