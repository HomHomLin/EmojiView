package com.lhh.emojiview;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lhh.emoji.beans.EmojiObject;
import com.lhh.emoji.core.loader.EmojiLoader;
import com.lhh.emoji.core.processor.EmojiProcessor;
import com.lhh.emoji.views.EmojiEditText;
import com.lhh.emoji.views.EmojiView;

import java.util.List;
import java.util.Map;


public class MainActivity extends Activity {

    private EmojiEditText mEtEmoji;
    private EmojiView mEmojiView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEmojiView = (EmojiView)findViewById(R.id.emoji_view);
        mEtEmoji = (EmojiEditText)findViewById(R.id.et_emoji);
        mEmojiView.setEditText(mEtEmoji);
        EmojiLoader.instance().setup(MainActivity.this,new EmojiProcessor.EmojiTaskCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish(Map<String, String> map, List<List<EmojiObject>> list, Map<String, Drawable> drawableMap) {
                mEmojiView.init(list);
            }
        });
//
//        mBtnOpenEmoji = (Button)findViewById(R.id.btn_open_emoji);
//        mBtnOpenEmoji.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//            }
//        });
    }

    //    final private TextWatcher mTextWatcher = new TextWatcher() {
//        private CharSequence temp;
//
//        @Override
//        public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
//            temp = s;
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
////            ExpressionEditText etView = metComment;
//
//            if (EmojiManager.instance().calculateTextCount(temp) > MAX_INPUT_COUNT) {
//                String str = String.format(mContext.getString(R.string.STR_MAX_INPUT), MAX_INPUT_COUNT);
//                Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
//
//                EmojiManager.backspace(metComment);
//
//                metComment.setTextKeepState(s);
//            }
//
//        }
//    };

}
