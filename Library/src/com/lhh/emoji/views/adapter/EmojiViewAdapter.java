package com.lhh.emoji.views.adapter;

import com.lhh.emoji.beans.EmojiObject;

import java.util.ArrayList;

/**
 * Created by linhonghong on 2015/9/9.
 */
public class EmojiViewAdapter implements EmojiBaseAdapter{
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public ArrayList<EmojiObject> getEmojis(int position) {
        return null;
    }

    @Override
    public int getPosition(int position) {
        return 0;
    }

    @Override
    public EmojiObject getEmoji(int index, int position) {
        return null;
    }
}
