package com.lhh.emoji.core.processor;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.SparseArray;

import com.lhh.emoji.beans.EmojiObject;
import com.lhh.emoji.core.handler.EmojiHandler;
import com.lhh.emoji.core.loader.EmojiLoader;
import com.lhh.emoji.util.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipException;

/**
 * Created by linhonghong on 2015/8/18.
 */
public class EmojiProcessor implements Runnable{

    private static final String TAG = "ExpressionTask";

    private Map<String, String> mExpressionAllMap = null;

    private Map<String, Drawable> mExpressionDrawableMap = null;

    private List<List<EmojiObject>> mExpressionAllArray = null;

    private ExpressionTaskCallBack mCallBack;

    private Context mContext;

    public interface ExpressionTaskCallBack{
        public void onStart();
        public void onFinish(Map<String, String> map, List<List<EmojiObject>> list, Map<String, Drawable> drawableMap);
    }

    @Override
    public void run() {

        mCallBack.onStart();

        SparseArray<String> xmlArray = EmojiLoader
                .getAllExpressionJsons(EmojiLoader.EMOJI_PATH);
        if (xmlArray == null || xmlArray.size() <= 0) {
            mCallBack.onFinish(mExpressionAllMap,mExpressionAllArray,mExpressionDrawableMap);
            return;
        }
        for (int i = 0; i < xmlArray.size(); i++) {
            int key = xmlArray.keyAt(i);
            String path = xmlArray.get(key);
            EmojiHandler handler = parse(path);
            if (handler != null) {
                if (mExpressionAllArray != null) {
                    mExpressionAllArray.add(handler.getExpressionList());
                } else {
                    mCallBack.onFinish(mExpressionAllMap,mExpressionAllArray,mExpressionDrawableMap);
                    return;
                }
                if (mExpressionAllMap != null) {
                    mExpressionAllMap.putAll(handler.getExpressionMap());
                } else {
                    mCallBack.onFinish(mExpressionAllMap,mExpressionAllArray,mExpressionDrawableMap);
                    return;
                }
                if (mExpressionDrawableMap != null) {
                    mExpressionDrawableMap.putAll(handler.getExpressionDrawableMap());
                } else {
                    mCallBack.onFinish(mExpressionAllMap,mExpressionAllArray,mExpressionDrawableMap);
                    return;
                }

                //简单校验文件数目与json文件提供的数量是否一致，如果不一致重新解压此包
                if (null != mExpressionAllArray && mExpressionAllArray.size() > 0 &&
                        !checkEmojiFile(EmojiLoader.EMOJI_PATH + File.separator
                                + key, mExpressionAllArray.size())) {
                    try {
                        File zipFile = null;
                        if (key == 1) {
                            zipFile = new File(EmojiLoader.EMOJI_PATH
                                    + File.separator
                                    + EmojiLoader.BASE_EMOJI_ZIP_NAME);
                        } else if (key == 2) {
                            zipFile = new File(EmojiLoader.EMOJI_PATH
                                    + File.separator
                                    + EmojiLoader.BASE_EMOJI_ZIP_NAME_2);
                        }
                        if (zipFile != null && zipFile.exists()) {
                            FileUtils.upZipFile(zipFile,
                                    EmojiLoader.EMOJI_PATH + File.separator
                                            + key);
                        }
                    } catch (ZipException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            } else {
                Log.d(TAG, "parse json file error,path = " + path);
            }
        }
        mCallBack.onFinish(mExpressionAllMap,mExpressionAllArray,mExpressionDrawableMap);
    }

    public EmojiProcessor(Context context , ExpressionTaskCallBack callBack) {
        mContext = context;
        mExpressionAllMap = new HashMap<>();
        mExpressionAllArray = new ArrayList<>();
        mExpressionDrawableMap = new HashMap<>();
        mCallBack = callBack;
    }

    public boolean checkEmojiFile(String path, int size) {
        boolean ret = false;
        File file = new File(path);
        if (file != null && file.exists()) {
            File[] childFiles = file.listFiles();
            ret = childFiles.length == size;
        }
        return ret;
    }

    private EmojiHandler parse(String path) {
        File file = new File(path);
        EmojiHandler handler = null;
        if (file != null && file.exists()) {
            try {
                StringBuffer stringBuffer = readJsonFile(file);
                handler = new EmojiHandler(mContext, file.getParent());
                handler.startJson(stringBuffer.toString());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return handler;
    }

    public StringBuffer readJsonFile(File file) throws Exception{
        StringBuffer stringBuffer = new StringBuffer();
        String line = null ;
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
        while( (line = br.readLine())!= null ) {
            stringBuffer.append(line);
        }
        br.close();
        return stringBuffer;
    }

}
