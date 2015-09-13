package com.lhh.emoji.core.processor;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import com.lhh.emoji.beans.EmojiObject;
import com.lhh.emoji.core.config.EmojiLoaderConfiguration;
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

    private static final String TAG = "EmojiProcessor";

    private Map<String, String> mEmojiAllMap = null;

    private Map<String, Drawable> mEmojiDrawableMap = null;

    private List<List<EmojiObject>> mEmojiAllArray = null;

    private EmojiTaskCallBack mCallBack;

    private Context mContext;

    private static final int ON_FINISH = 1;
    private static final int ON_START = 2;


    public interface EmojiTaskCallBack{
        public void onStart();
        public void onFinish(Map<String, String> map, List<List<EmojiObject>> list, Map<String, Drawable> drawableMap);
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            if(what == ON_START){
                mCallBack.onStart();
            }else if(what == ON_FINISH){
                mCallBack.onFinish(mEmojiAllMap,mEmojiAllArray,mEmojiDrawableMap);
            }
        }
    };

    @Override
    public void run() {

        mHandler.sendMessage(Message.obtain(mHandler,ON_START));
//        mCallBack.onStart();

        SparseArray<String> xmlArray = EmojiLoader
                .getAllEmojiJsons(EmojiLoader.instance().getBuilder().getEmojiPath());
        if (xmlArray == null || xmlArray.size() <= 0) {
            mHandler.sendMessage(Message.obtain(mHandler,ON_FINISH));
//            mCallBack.onFinish(mEmojiAllMap,mEmojiAllArray,mEmojiDrawableMap);
            return;
        }
        for (int i = 0; i < xmlArray.size(); i++) {
            int key = xmlArray.keyAt(i);
            String path = xmlArray.get(key);
            EmojiHandler handler = parse(path);
            if (handler != null) {
                if (mEmojiAllArray != null) {
                    mEmojiAllArray.add(handler.getEmojiList());
                } else {
                    mHandler.sendMessage(Message.obtain(mHandler,ON_FINISH));
//                    mCallBack.onFinish(mEmojiAllMap,mEmojiAllArray,mEmojiDrawableMap);
                    return;
                }
                if (mEmojiAllMap != null) {
                    mEmojiAllMap.putAll(handler.getEmojiMap());
                } else {
                    mHandler.sendMessage(Message.obtain(mHandler,ON_FINISH));
//                    mCallBack.onFinish(mEmojiAllMap,mEmojiAllArray,mEmojiDrawableMap);
                    return;
                }
                if (mEmojiDrawableMap != null) {
                    mEmojiDrawableMap.putAll(handler.getEmojiDrawableMap());
                } else {
                    mHandler.sendMessage(Message.obtain(mHandler,ON_FINISH));
//                    mCallBack.onFinish(mEmojiAllMap,mEmojiAllArray,mEmojiDrawableMap);
                    return;
                }

                //简单校验文件数目与json文件提供的数量是否一致，如果不一致重新解压此包
                if (null != mEmojiAllArray && mEmojiAllArray.size() > 0 &&
                        !checkEmojiFile(EmojiLoader.instance().getBuilder().getEmojiPath() + File.separator
                                + key, mEmojiAllArray.size())) {
                    try {
                        File zipFile = null;
                        if (key == 1) {
                            zipFile = new File(EmojiLoader.instance().getBuilder().getEmojiPath()
                                    + File.separator
                                    + EmojiLoader.BASE_EMOJI_ZIP_NAME);
                        } else if (key == 2) {
                            zipFile = new File(EmojiLoader.instance().getBuilder().getEmojiPath()
                                    + File.separator
                                    + EmojiLoader.BASE_EMOJI_ZIP_NAME_2);
                        }
                        if (zipFile != null && zipFile.exists()) {
                            FileUtils.upZipFile(zipFile,
                                    EmojiLoader.instance().getBuilder().getEmojiPath() + File.separator
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
        mHandler.sendMessage(Message.obtain(mHandler,ON_FINISH));
//        mCallBack.onFinish(mEmojiAllMap,mEmojiAllArray,mEmojiDrawableMap);
    }

    public EmojiProcessor(Context context , EmojiTaskCallBack callBack) {
        mContext = context;
        mEmojiAllMap = new HashMap<>();
        mEmojiAllArray = new ArrayList<>();
        mEmojiDrawableMap = new HashMap<>();
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
