package com.lhh.emoji.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.lhh.emoji.core.loader.EmojiLoader;

/**
* Created by linhonghong on 2015/8/19.
*/
public class EmojiEditText extends EditText {

    public EmojiEditText(Context context) {
        super(context);
    }

    public EmojiEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public EmojiEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setText(getText());
    }

    @Override
    protected void onTextChanged(CharSequence text, int start,
                                 int lengthBefore, int lengthAfter) {
        updateText();
    }

    private void updateText() {
        EmojiLoader.instance().addEmoji(getContext(), getText(),(int)getTextSize());
    }

}
