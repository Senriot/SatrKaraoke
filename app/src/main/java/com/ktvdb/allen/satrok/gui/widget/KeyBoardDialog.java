package com.ktvdb.allen.satrok.gui.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.databinding.KeyboardDialogBinding;

/**
 * Created by Allen on 15/7/20.
 */
public class KeyBoardDialog
{

    private static AlertDialog   mKeyboardDialag;
    private static SearchKeyword mKeyword;


    public enum KeyboardType
    {
        PINYIN,
        SHOWXIE
    }

    public interface KeyboardListener
    {
        void onTextChanged(String text);

        void onShowed();

        void onDismiss();
    }

    public static void showKeyboard(Context context,
                                    KeyboardListener keyboardListener,
                                    String searchText,
                                    KeyboardType inputType)
    {

        if (mKeyboardDialag == null)
        {
            mKeyword = new SearchKeyword(context);
            mKeyboardDialag = new AlertDialog.Builder(context, R.style.dialog)
                    .setCancelable(true)
                    .setView(mKeyword)
                    .create();
            Window dialogWindow = mKeyboardDialag.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.y = 50;
            dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
            dialogWindow.setAttributes(lp);
        }
        mKeyboardDialag.setOnDismissListener(dialog -> {
            if (keyboardListener != null)
            {
                keyboardListener.onDismiss();
            }
        });
        mKeyword.setListener(keyboardListener);
        mKeyword.setSearchText(searchText);
        mKeyword.setKeyboardType(inputType);
        mKeyboardDialag.show();
        if (keyboardListener != null)
        {
            keyboardListener.onShowed();
        }

    }

    public static void showKeyboard(Context context,
                                    KeyboardListener keyboardListener, String keyword)
    {
        showKeyboard(context, keyboardListener, keyword, null);
    }

    public static class SearchKeyword extends FrameLayout implements TextWatcher,
                                                                     KeyboardView.OnKeyboardActionListener,
                                                                     HWBoardView.OnTextSelectedLinstener
    {

        private final KeyboardDialogBinding mBinding;

        private KeyboardListener mListener;

        RadioGroup radioGroup;

        private PopupWindow mPopupWindow;

        private String searchText;

        private KeyboardType keyboardType;

        private KeyboardView mKeyboardView;

        private HWBoardView hwBoardView;

        public String getSearchText()
        {
            return searchText;
        }

        public void setSearchText(String searchText)
        {
            this.searchText = searchText;
        }

        public KeyboardType getKeyboardType()
        {
            return keyboardType;
        }

        public void setKeyboardType(KeyboardType keyboardType)
        {
            this.keyboardType = keyboardType;
        }

        public void setListener(KeyboardListener mListener)
        {
            this.mListener = mListener;
        }

        public SearchKeyword(Context context)
        {
            this(context, null);
        }

        public SearchKeyword(Context context, AttributeSet attrs)
        {
            this(context, attrs, 0);
        }

        public SearchKeyword(Context context, AttributeSet attrs, int defStyleAttr)
        {
            super(context, attrs, defStyleAttr);
            mBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                                               R.layout.keyboard_dialog,
                                               this,
                                               true);
            mBinding.inputText.addTextChangedListener(this);
            mBinding.inputText.setInputType(InputType.TYPE_NULL);
            mBinding.inputType.setOnClickListener(this::inputTypeSelect);
            mBinding.btnDelAll.setOnClickListener(v -> {
                Editable editable = mBinding.inputText.getText();
                editable.clear();
            });
        }

        void inputTypeSelect(View view)
        {
            if (radioGroup == null)
            {
                radioGroup = (RadioGroup) View.inflate(getContext(),
                                                       R.layout.keyboard_language_popup,
                                                       null);

            }

            radioGroup.check(mKeyboardView.getVisibility() == VISIBLE ? R.id.rb_pinyin : R.id.rb_shouxie);
            radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                if (checkedId == R.id.rb_pinyin)
                {
                    showPinYinKeyboard();
                }
                else
                {
                    showHwKeyboard();
                }
                mPopupWindow.dismiss();
            });
            mPopupWindow = new PopupWindow(getContext());
            mPopupWindow.setFocusable(true);
            mPopupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setContentView(radioGroup);
            mPopupWindow.update();
            mPopupWindow.showAsDropDown(view, 0, -178);
        }


        @Override
        protected void onAttachedToWindow()
        {
            super.onAttachedToWindow();
            mBinding.inputText.setText(searchText);
            if (keyboardType != null && keyboardType == KeyboardType.SHOWXIE)
            {
                showHwKeyboard();
            }
            else
            {
                showPinYinKeyboard();
            }
        }


        private void showPinYinKeyboard()
        {
            if (mKeyboardView == null)
            {
                mKeyboardView = (KeyboardView) mBinding.pinYinViewStub.getViewStub().inflate();
                mKeyboardView.setKeyboard(new Keyboard(getContext(), R.xml.search1));
                mKeyboardView.setOnKeyboardActionListener(this);
            }
            mKeyboardView.setVisibility(VISIBLE);
            if (hwBoardView != null)
            {
                hwBoardView.setVisibility(GONE);
            }
        }

        private void showHwKeyboard()
        {
            if (hwBoardView == null)
            {
                hwBoardView = (HWBoardView) mBinding.showXieViewStub.getViewStub().inflate();
                hwBoardView.setLinstener(this);
            }
            hwBoardView.setVisibility(VISIBLE);
            if (mKeyboardView != null)
            {
                mKeyboardView.setVisibility(GONE);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {

        }


        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            mBinding.btnDelAll.setVisibility(mBinding.inputText.getText()
                                                               .length() > 0 ? VISIBLE : GONE);
            if (mListener != null)
            {
                mListener.onTextChanged(s.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable s)
        {

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
            Editable editable = mBinding.inputText.getText();

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
            int start = mBinding.inputText.getSelectionStart();
            mBinding.inputText.getText().insert(start, text);
        }
    }
}
