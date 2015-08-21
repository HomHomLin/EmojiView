package com.lhh.emoji.core.handler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.lhh.emoji.beans.EmojiObject;
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

    private List<EmojiObject> mExpressionList = null;

    private HashMap<String, String> mExpressionMap = null;

    private HashMap<String, Drawable> mExpressionDrawableMap = null;

    private Context mContext = null;

    private String mPath = null;

    public EmojiHandler(Context context, String path) {
        this.mPath = path;
        this.mContext = context;
        mExpressionList = new ArrayList<EmojiObject>();
        mExpressionMap = new HashMap<String, String>();
        mExpressionDrawableMap = new HashMap<>();
    }

    public List<EmojiObject> getExpressionList() {
        return this.mExpressionList;
    }

    public HashMap<String,Drawable> getExpressionDrawableMap(){
        return this.mExpressionDrawableMap;
    }

    public HashMap<String,String > getExpressionMap(){
        return this.mExpressionMap;
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
                mExpressionList.add(emojiObject);
                //将对象放入map中，用于edittext的判断
                mExpressionMap.put(emojiObject.getKey(), emojiObject.getPath());
                if(EmojiLoader.instance().isUseCache) {
                    //解析图片,用于缓存
                    Bitmap bitmap = ImageLoader.getInstance().loadImageSync("file://" + emojiObject.getPath());
                    mExpressionDrawableMap.put(emojiObject.getKey(),
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

