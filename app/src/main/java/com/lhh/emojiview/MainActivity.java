package com.lhh.emojiview;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.lhh.emoji.beans.EmojiObject;
import com.lhh.emoji.core.loader.EmojiLoader;
import com.lhh.emoji.core.processor.EmojiProcessor;
import com.lhh.emoji.views.EmojiView;

import java.util.List;
import java.util.Map;


public class MainActivity extends Activity {

    private Button mBtnOpenEmoji;
    private EmojiView mEmojiView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEmojiView = (EmojiView)findViewById(R.id.emoji_view);

        mBtnOpenEmoji = (Button)findViewById(R.id.btn_open_emoji);
        mBtnOpenEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmojiLoader.instance().setup(MainActivity.this,new EmojiProcessor.EmojiTaskCallBack() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onFinish(Map<String, String> map, List<List<EmojiObject>> list, Map<String, Drawable> drawableMap) {
                        mEmojiView.init(list);
                    }
                });

            }
        });
    }

}
