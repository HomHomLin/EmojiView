package com.lhh.emoji.core.handler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.lhh.emoji.beans.EmojiObject;
import com.lhh.emoji.core.config.EmojiLoaderConfiguration;
import com.lhh.emoji.core.loader.EmojiLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
* Created by linhonghong on 2015/8/18.
*/
public class EmojiHandler implements BaseHandler {

    private List<EmojiObject> mEmojiList = null;

    private HashMap<String, String> mEmojiMap = null;

    private HashMap<String, Drawable> mEmojiDrawableMap = null;

    private Context mContext = null;

    private String mPath = null;

    public EmojiHandler(Context context, String path) {
        this.mPath = path;
        this.mContext = context;
        mEmojiList = new ArrayList<EmojiObject>();
        mEmojiMap = new HashMap<String, String>();
        mEmojiDrawableMap = new HashMap<>();
    }

    public List<EmojiObject> getEmojiList() {
        return this.mEmojiList;
    }

    public HashMap<String,Drawable> getEmojiDrawableMap(){
        return this.mEmojiDrawableMap;
    }

    public HashMap<String,String > getEmojiMap(){
        return this.mEmojiMap;
    }

    @Override
    public void startJson(String json) throws Exception{

        JSONObject obj = new JSONObject(json);
        if(obj.has(EmojiLoader.JSON_ITEMS)){
            JSONArray ja = obj.getJSONArray(EmojiLoader.JSON_ITEMS);
            int size = ja.length();
            for(int i = 0; i < size; i++) {
                JSONObject jo = ja.optJSONObject(i);
                EmojiObject emojiObject = new EmojiObject();
                if(jo.has(EmojiLoader.JSON_KEY)){
                    //设置表情的标识
                    emojiObject.setKey(jo.getString(EmojiLoader.JSON_KEY));
                }
                if(jo.has(EmojiLoader.JSON_VALUE)){
                    //设置表情的文件名路径
                    emojiObject.setPath( mPath + File.separator + jo.getString(EmojiLoader.JSON_VALUE));
                }
                //todo:这里有可能产生重复的内容，可能需要调整
                mEmojiList.add(emojiObject);
                //将对象放入map中，用于edittext的判断
                mEmojiMap.put(emojiObject.getKey(), emojiObject.getPath());
                if(EmojiLoader.instance().getBuilder().getUseCache()) {
                    //解析图片,用于缓存
                    Bitmap bitmap = ImageLoader.getInstance().loadImageSync("file://" + emojiObject.getPath());
                    mEmojiDrawableMap.put(emojiObject.getKey(),
                            new BitmapDrawable(mContext.getResources(), bitmap));
                }
//                if(!ExpressionManager.instance().mExpressionDrawableMap.containsKey(expressionObject.getKey())){
//                    Bitmap bitmap = ExtraFuncMgr.Instance().loadImageSync(expressionObject.getPath());
//                    ExpressionManager.instance().mExpressionDrawableMap.put(expressionObject.getKey(),
//                            new BitmapDrawable(KasConfigManager.mApplication.getResources(), bitmap));
//                }
            }
        }
    }
}

