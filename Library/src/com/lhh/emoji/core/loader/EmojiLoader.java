package com.lhh.emoji.core.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.Spannable;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.widget.EditText;

import com.lhh.emoji.beans.EmojiObject;
import com.lhh.emoji.core.processor.EmojiProcessor;
import com.lhh.emoji.util.EmojiUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* Created by linhonghong on 2015/8/18.
*/
public class EmojiLoader {

    public static final String JSON_ITEMS = "items";

    public static final String JSON_KEY = "key";

    public static final String JSON_VALUE = "value";

    public static final String JSON_BACKSPACE = "backspace";

    private static EmojiLoader mInstance = null;

    private Map<String, String> mExpressionMap = null;

    private List<List<EmojiObject>> mExpressionArray = null;

    public Map<String ,Drawable> mExpressionDrawableMap;

    private EmojiProcessor mParserTask = null;

    public static final  boolean isUseCache = false;

    private boolean mExpressionMode;

    public static final int ANIMATE_DURATION = 350;

    private static final String MATCHER = "\\[\\w*\\]";

    // 每个表情包都含有emoji.xml的文件索引
    private static final String EXPRESSION_JSON_NAME = "expression.json";

    // 回退标识
    public static final String BACKSPACE = "backspace";

    //删除对象
    public EmojiObject mBackspaceEmojiObject;

    public static final String EXT_SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String APP_KASCEND_PATH = EXT_SDCARD_PATH + "/kascend";
    public static final String APP_DATA_PATH = EXT_SDCARD_PATH + "/emojiview/";

    // 表情包顶级目录名称
    public static final String EMOJI_PATH = APP_DATA_PATH
            + "emoji";

    // 基础表情包目录名称
    public static final String BASE_EMOJI_DIR = "1";

    public static final String BASE_EMOJI_ZIP_NAME = "1.zip";

    public static final String BASE_EMOJI_ZIP_NAME_2 = "2.zip";

    private static final String TAG = "EmojiManager";

    private EmojiLoader() {
    }

    /**
     * 读取表情
     * @param callBack
     */
    public void setup(Context context, EmojiProcessor.ExpressionTaskCallBack callBack){
        addExpressions(context, callBack);
    }

    public void setup(Context context){
        addExpressions(context, new ExpressionManagerCallBack());
    }

    class ExpressionManagerCallBack implements EmojiProcessor.ExpressionTaskCallBack{

        @Override
        public void onStart() {

        }

        @Override
        public void onFinish(Map<String, String> map, List<List<EmojiObject>> list, Map<String, Drawable> drawableMap) {
            if (mExpressionArray != null) {
                mExpressionArray.clear();
            }
            if (mExpressionMap != null) {
                mExpressionMap.clear();
            }
            if(mExpressionDrawableMap != null){
                mExpressionDrawableMap.clear();
            }
            mExpressionArray = list;
            mExpressionMap = map;
            mExpressionDrawableMap = drawableMap;
        }

    }

    public static EmojiLoader instance() {
        if (mInstance == null) {
            synchronized (EmojiLoader.class) {
                if (mInstance == null) {
                    mInstance = new EmojiLoader();
                }
            }
        }
        return mInstance;
    }

    public EmojiObject getBackspaceExpressionObject(){
        if(mBackspaceEmojiObject == null){
            mBackspaceEmojiObject = new EmojiObject();
            mBackspaceEmojiObject.setKey(EmojiLoader.JSON_BACKSPACE);
            mBackspaceEmojiObject.setPath(EmojiLoader.JSON_BACKSPACE);
        }

        return this.mBackspaceEmojiObject;
    }

    private void addExpressions(Context context, EmojiProcessor.ExpressionTaskCallBack callBack) {
        if (mExpressionArray != null) {
            mExpressionArray.clear();
            mExpressionArray = null;
        }
        if (mExpressionMap != null) {
            mExpressionMap.clear();
            mExpressionMap = null;
        }
        if (mExpressionDrawableMap != null) {
            mExpressionDrawableMap.clear();
            mExpressionDrawableMap = null;
        }

        mExpressionMap = new HashMap<String, String>();
        mExpressionDrawableMap = new HashMap<>();
        mExpressionArray = new ArrayList<>();
        mParserTask = new EmojiProcessor(context, callBack);
        new Thread(mParserTask).start();
    }



//    public void setTaskListener(ParserTask.IParserTask task){
//
//        if(mParserTask == null){
//            return;
//        }
//        mParserTask.setParserTask(task);
//    }
//
//    public boolean isEmojiMode() {
//        return mEmojiMode;
//    }
//
//    public void setEmojiMode(boolean emojiMode) {
//        this.mEmojiMode = emojiMode;
//    }
//

    /**
     * 获得加载好的map
     * @return
     */
    public Map<String, String> getExpressionsMap() {
        return this.mExpressionMap;
    }

    /**
     * 获得所有的表情包
     * @return
     */
    public List<List<EmojiObject>> getExpressionArray() {
        return mExpressionArray;
    }

    /**
     * 获得缓存
     * @return
     */
    public Map<String ,Drawable> getExpressionDrawableMap() {
        return mExpressionDrawableMap;
    }


//
//    public List<Integer> getEmojiDirKeys() {
//        if(null != mEmojisArray && mEmojisArray.size() >0){
//            List<Integer> keys = new ArrayList<Integer>(mEmojisArray.size());
//            for (int i = 0; i < mEmojisArray.size(); i++) {
//                int key = mEmojisArray.keyAt(i);
//                keys.add(key);
//            }
//            return keys;
//        }
//        return null;
//    }
//
//    public List<Emojicon> getEmojiList(int key) {
//        if(null != mEmojisArray)
//            return mEmojisArray.get(key);
//        return null;
//    }
//

    /**
     * 输入一个表情到edittext
     * @param editText
     * @param emojiObject
     */
    public static void input(EditText editText, EmojiObject emojiObject) {
        if (editText == null || emojiObject == null) {
            return;
        }

        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();
        if (start < 0) {
            editText.append(emojiObject.getKey());
        } else {
            editText.getText().replace(Math.min(start, end),
                    Math.max(start, end), emojiObject.getKey(), 0,
                    emojiObject.getKey().length());
        }
    }

    public static void backspace(EditText editText) {
        KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0,
                0, KeyEvent.KEYCODE_ENDCALL);
        editText.dispatchKeyEvent(event);
    }
//
//    public int calculateTextCount(CharSequence text) {
//        int count = text.length();
//        Matcher matcher = Pattern.compile(MATCHER).matcher(text);
//        while (matcher.find()) {
//            if (mEmojisMap == null) {
//                break;
//            }
//            if (mEmojisMap.containsKey(matcher.group())) {
//                count -= matcher.group().length();
//                count++;
//            }
//        }
//        return count;
//    }
//
    public boolean addEmoji(Context context, Spannable spannable, int textSize) {

        //用于判断是否有这个表情
        boolean isEmoji = false;
        //先移除旧的span
        ImageSpan[] oldSpans = spannable.getSpans(0, spannable.length(),
                ImageSpan.class);
        for (int i = 0; i < oldSpans.length; i++) {
            spannable.removeSpan(oldSpans[i]);
        }

        Matcher matcher = Pattern.compile(MATCHER).matcher(spannable);
        while (matcher.find()) {
            Drawable drawable = null;
            if (isUseCache && mExpressionDrawableMap != null && mExpressionDrawableMap.containsKey(matcher.group())) {
                //如果使用缓存，使用缓存
                drawable = mExpressionDrawableMap.get(matcher.group());
            }else{
                if(mExpressionMap.containsKey(matcher.group())){
                    //如果没有缓存则判断是否有预读到这个表情信息
                    drawable = getDrawable(context,matcher.group(),
                            mExpressionMap.get(matcher.group()), textSize);
                }
            }
            if (drawable != null) {
                int drawableWidth = drawable.getIntrinsicWidth();
                int drawableHeight = drawable.getIntrinsicHeight();
                int tmpHeight = textSize + (int) EmojiUtils.getRawSize(TypedValue.COMPLEX_UNIT_DIP, 3, context);
                int height = tmpHeight;
                int width = drawableWidth * height / drawableHeight;
                drawable.setBounds(0, 0, width, height);
                spannable.setSpan(new ImageSpan(drawable), matcher.start(),
                        matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            isEmoji = true;
        }

        return isEmoji;
    }

    /**
     * 外部获取drawable
     * @param context
     * @param key
     * @param path
     * @param textSize
     * @return
     */
    private Drawable getDrawable(Context context, String key, String path, int textSize) {
        Drawable drawable = null;
        Bitmap bmp = ImageLoader.getInstance().loadImageSync("file://" + path);//imageloader
//        Bitmap bmp = BitmapFactory.decodeFile(path);
        if (bmp != null) {
            drawable = new BitmapDrawable(context.getResources(), bmp);
            if(isUseCache && !mExpressionDrawableMap.containsKey(key)){
                mExpressionDrawableMap.put(key,drawable);//保存缓存下
            }
        } else {
            Log.e(TAG, "bitmap == null, path = " + path);
//            SP_Manager.Instance().setRebuildEmojiFile(true);//这里出现了表情丢失之类的情况，需要重读表情的信息
        }
        return drawable;
    }


    /**
     * 复制assert中Emoji文件到SD卡中
     * 如果xml文件缺失，或者上一次加载emoji表情出错，将会重新解压压缩包
     *
     * @param context
     */
//    public void copyAssertExpressionFile(final Context context) {
//        if (!Environment.getExternalStorageState().equals(
//                Environment.MEDIA_MOUNTED)) {
//            KasLog.e(TAG, "SD card is not available");
//            return;
//        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    FileUtils.copyAssetFile(context, BASE_EMOJI_ZIP_NAME,
//                            EMOJI_PATH);
//                    File file = new File(EMOJI_PATH + File.separator
//                            + BASE_EMOJI_DIR + File.separator + EXPRESSION_JSON_NAME);
//                    boolean isRebuild = SP_Manager.Instance()
//                            .isRebuildEmojiFile();
//                    if (isRebuild) {
//                        SP_Manager.Instance()
//                                .setRebuildEmojiFile(false);
//                    }
//                    if (!file.exists() || isRebuild) {
//                        FileUtils.upZipFile(new File(EMOJI_PATH
//                                        + File.separator + BASE_EMOJI_ZIP_NAME),
//                                EMOJI_PATH + File.separator + BASE_EMOJI_DIR);
//                    }
//                    File fileNomedia = new File(EMOJI_PATH + File.separator
//                            + BASE_EMOJI_DIR + File.separator + ".nomedia");
//                    if (!fileNomedia.exists()) {
//                        fileNomedia.mkdirs();
//                    }
//
//                    // int nextDirKey =
//                    // getNextEmojiDirKey(EmojiManager.EMOJI_PATH);
//                    int nextDirKey = 2;
//
//                    FileUtils.copyAssetFile(context, BASE_EMOJI_ZIP_NAME_2,
//                            EMOJI_PATH);
//                    File file_2 = new File(EMOJI_PATH + File.separator
//                            + nextDirKey + File.separator + E);
//                    if (!file_2.exists() || isRebuild) {
//                        FileUtils.upZipFile(new File(EMOJI_PATH
//                                        + File.separator + BASE_EMOJI_ZIP_NAME_2),
//                                EMOJI_PATH + File.separator + nextDirKey);
//                    }
//                    fileNomedia = new File(EMOJI_PATH + File.separator
//                            + nextDirKey + File.separator + ".nomedia");
//                    if (!fileNomedia.exists()) {
//                        fileNomedia.mkdirs();
//                    }
//                    setup();
//                } catch (ZipException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }

    /**
     * 取得当前目录下的所有expression.xml文件
     *
     * @param path
     *            当前目录的名称
     *
     * @return
     */
    public static SparseArray<String> getAllExpressionJsons(String path) {
        SparseArray<String> jsonArray = null;
        File dir = new File(path);
        if (dir != null && dir.isDirectory()) {
            jsonArray = new SparseArray<String>();
            File[] fileArray = dir.listFiles();
            for (File fp : fileArray) {
                if (fp != null && fp.isDirectory()) {
                    if (!EmojiUtils.isNumeric(fp.getName())) {
                        continue;
                    }
                    File[] childFiles = fp.listFiles();
                    for (File fc : childFiles) {
                        if (fc != null && fc.exists()) {
                            if (fc.getName().equals(EXPRESSION_JSON_NAME)) {
                                jsonArray.put(EmojiUtils.parseInt(fp.getName()),
                                        path + File.separator + fp.getName()
                                                + File.separator + fc.getName());
                                break;
                            }
                        }
                    }
                }
            }
        }

        return jsonArray;
    }

    /**
     * 遍历该目录下的所有子目录，对比数字目录大小取得最大数字目录并+1返回
     *
     * @param path
     * @return
     */
    public static int getNextEmojiDirKey(String path) {
        int key = -1;
        File dir = new File(path);
        if (dir != null && dir.isDirectory()) {
            File[] fileArray = dir.listFiles();
            for (File f : fileArray) {
                if (f != null && f.isDirectory()) {
                    if (EmojiUtils.isNumeric(f.getName())) {
                        int num = EmojiUtils.parseInt(f.getName());
                        key = key > num ? key : num;
                    }
                }
            }
        }
        return key + 1;
    }

//    public void stop(){
//        mEmojiMode = false;
//    }

    /**
     * call this method to release resource.
     */
    public void release() {

        if (mExpressionMap != null) {
            mExpressionMap.clear();
            mExpressionMap = null;
        }
        if (mExpressionArray != null) {
            mExpressionArray.clear();
            mExpressionArray = null;
        }
        mParserTask = null;
        mInstance = null;

    }
}