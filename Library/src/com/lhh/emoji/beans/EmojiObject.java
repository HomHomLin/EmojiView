package com.lhh.emoji.beans;

/**
 * Created by linhonghong on 2015/8/18.
 */
public class EmojiObject {

    private String path;//表情的路径

    private String key;//表情的key

    public EmojiObject() {
        path = "";
        key = "";
    }

    public EmojiObject(String path, String key) {
        this.path = path;
        this.key = key;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        String oKey = ((EmojiObject)o).getKey();
        return oKey.equals(this.key);
    }
}