package com.lhh.emoji.core.config;

import android.content.Context;
import android.os.Environment;

/**
 * Created by Linhh on 15/9/13.
 */
public final class EmojiLoaderConfiguration {

    private EmojiLoaderConfiguration(){

    }

    public static Builder createDefault(){
        Builder builder = new Builder();
        builder.configFile(Builder.EMOJI_LOADER_CONFIGURATION_BUILDER_DEFAULT_CONFIG_FILE);
        builder.emojiPath(Builder.EMOJI_LOADER_CONFIGURATION_BUILDER_DEFAULT_EMOJI_PATH);
        builder.useCache(Builder.EMOJI_LOADER_CONFIGURATION_BUILDER_DEFAULT_USECACHE);
        return builder;
    }

    public static class Builder{
//        private Context mContext;

        private String mEmojiPath;

        private boolean mUseCache;

        private String mConfigFile;

        public static final String EMOJI_LOADER_CONFIGURATION_BUILDER_DEFAULT_EMOJI_PATH = SystemConfiguration.SYSTEM_CONFIGURATION_SD_PATH + "/emojiview/emoji";

        public static final boolean EMOJI_LOADER_CONFIGURATION_BUILDER_DEFAULT_USECACHE = true;

        public static final String EMOJI_LOADER_CONFIGURATION_BUILDER_DEFAULT_CONFIG_FILE = "emoji.json";

        public Builder(){
            this.mEmojiPath = "";
            this.mUseCache = true;
            this.mConfigFile = "";
        }

        public Builder build(){
            return this;

        }

//        public Builder(Context context){
//            this.mContext = context;
//            this.mEmojiPath = "";
//            this.mUseCache = true;
//            this.mConfigFile = "";
//        }

        /**
         * 表情所在的根目录
         * @param emojiPath
         * @return
         */
        public Builder emojiPath(String emojiPath){
            this.mEmojiPath = emojiPath;
            return this;
        }

        /**
         * 是否对表情进行缓存
         * @param useCache
         * @return
         */
        public Builder useCache(boolean useCache){
            this.mUseCache = useCache;
            return this;
        }

        /**
         * 表情对应文件的文件名
         * @param configFile
         * @return
         */
        public Builder configFile(String configFile){
            this.mConfigFile = configFile;
            return this;
        }

        public String getEmojiPath(){
            return this.mEmojiPath;
        }

        public String getConfigFile(){
            return this.mConfigFile;
        }

        public boolean getUseCache(){
            return this.mUseCache;
        }
    }
}
