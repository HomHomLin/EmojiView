package com.lhh.emoji.views.adapter;

import com.lhh.emoji.beans.EmojiObject;

import java.util.ArrayList;

/**
 * Created by linhonghong on 2015/9/9.
 */
public interface EmojiBaseAdapter {
    public int getCount();
    public ArrayList<EmojiObject> getEmojis(int position);
    public int getPosition(int position);
    public EmojiObject getEmoji(int index, int position);
}
