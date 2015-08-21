package com.lhh.emoji.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by linhonghong on 2015/8/21.
 */
public class EmojiImageView extends ImageView {

    public EmojiImageView(Context context) {
        super(context);
    }

    public EmojiImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void loadLocalImageNoshowImageOnLoading(String path, int defaultRes, ImageView imageView){
        if(null == path || path.length() == 0){
            imageView.setImageResource(defaultRes);
            return;
        }
        String uri = path;
        if(!uri.startsWith("file://")){
            uri = "file://" + uri;
        }
        ImageLoader.getInstance().displayImage(uri,imageView);
    }

}