package com.lhh.emoji.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.lhh.emoji.R;
import com.lhh.emoji.beans.EmojiObject;
import com.lhh.emoji.core.loader.EmojiLoader;
import com.lhh.emoji.util.ViewHolder;

import java.util.HashMap;
import java.util.List;

/**
* Created by linhonghong on 2015/8/11.
*/
public class EmojiPager extends RelativeLayout{

    public List<EmojiObject> mExtraData;

    GridView mGridView;

    private Context mContext;

    private ExpressionGvAdapter mAdapter;

    private View mRootView;

    private EditText mEditText;

    public EmojiPager(Context context) {
        super(context);
        initView(context);
    }

    public EmojiPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context){
        mContext = context;
        mRootView = inflate(context, R.layout.emoji_pager, this);
        mGridView = (GridView) mRootView.findViewById(R.id.emoji_grid_view);
    }

    public void init(List<EmojiObject> list,EditText editText){
        mExtraData = list;

        EmojiObject emojiObject = EmojiLoader.instance().getBackspaceExpressionObject();

        if(mExtraData != null && !mExtraData.contains(emojiObject)) {
            //最后一个是删除
            //把删除也加进去
            mExtraData.add(emojiObject);
        }

        mEditText = editText;
        if(mAdapter == null) {
            mAdapter = new ExpressionGvAdapter(mContext);
            mGridView.setAdapter(mAdapter);
        }
        setGridViewListener();
    }

    public void setGridViewListener(){
        if(mEditText == null){
            //如果没有接受绑定的editText对象就不需要监听
            return;
        }
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(mEditText != null && mExtraData != null) {
                    if(mExtraData.get(i).getKey().equals(EmojiLoader.JSON_BACKSPACE)){
                        //删除
                        EmojiLoader.instance().backspace(mEditText);
                    }else {
                        EmojiLoader.instance().input(mEditText, mExtraData.get(i));
                    }
                }
            }
        });
    }

    public class ExpressionGvAdapter extends BaseAdapter{
        protected LayoutInflater mInflater;
        public Context mContext;

        public ExpressionGvAdapter(Context context){
            mContext = context;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mExtraData == null ? 0 : mExtraData.size();
        }

        @Override
        public EmojiObject getItem(int i) {
            return mExtraData == null ? null : mExtraData.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view == null){
                view = mInflater.inflate(R.layout.emoji_item, null);
            }
            EmojiImageView iv = ViewHolder.get(view, R.id.emoji_image_view);
            if(iv != null){
                if(!mExtraData.get(i).getPath().equals(EmojiLoader.BACKSPACE)) {
                    if(EmojiLoader.instance().isUseCache && EmojiLoader.instance().mExpressionDrawableMap == null) {
                        EmojiLoader.instance().mExpressionDrawableMap = new HashMap<>();
                    }
                    if(EmojiLoader.instance().isUseCache && EmojiLoader.instance().mExpressionDrawableMap.containsKey(mExtraData.get(i).getKey())){
                        //如果存在这个缓存
                        iv.setImageDrawable(EmojiLoader.instance().getExpressionDrawableMap().get(mExtraData.get(i).getKey()));
                    }else{
                        //没有缓存需要自己加载
                        iv.loadLocalImageNoshowImageOnLoading(mExtraData.get(i).getPath(), 0,
                                iv);
//                        Bitmap bitmap = ExtraFuncMgr.Instance().loadImageSync("file://" + mExtraData.get(i).getPath());
//                        Drawable drawable = new BitmapDrawable(KasConfigManager.mApplication.getResources(), bitmap);
//                        iv.setImageDrawable(drawable);
//                        if(ExpressionManager.instance().isUseCache ) {
//                            //如果有缓存就保存
//                            ExpressionManager.instance().mExpressionDrawableMap.put(mExtraData.get(i).getKey(),
//                                    drawable);
//                        }
                    }
                }else{
                    //如果是删除
                    iv.setImageResource(R.drawable.emoji_backspace);
                }
            }
            return view;
        }
    }


}
