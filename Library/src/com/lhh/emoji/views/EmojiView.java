package com.lhh.emoji.views;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.lhh.emoji.R;
import com.lhh.emoji.beans.EmojiObject;
import com.lhh.emoji.util.EmojiUtils;

import java.util.ArrayList;
import java.util.List;

/**
* Created by linhonghong on 2015/8/11.
*/
public class EmojiView extends RelativeLayout{

    FineViewPager mViewPager;

    LinearLayout mEmojiRadio;

    private EmojiPagerAdapter mAdapter;

    public Context mContext;

    public List<List<EmojiObject>> mDataList;

    public EditText mEditText;//接受绑定的EditText

    private View mView;

    public EmojiView(Context context) {
        super(context);
    }

    public EmojiView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void initView(Context context){
        mContext = context;
        mView = inflate(context, R.layout.emoji_full_view, this);
        mViewPager = (FineViewPager) mView.findViewById(R.id.fine_view_pager);
        mEmojiRadio = (LinearLayout) mView.findViewById(R.id.emoji_radio);
    }

    public void init(List<List<EmojiObject>> dataList){
        mDataList = dataList;
        if(mAdapter == null) {
            mAdapter = new EmojiPagerAdapter();
        }
        if(dataList != null && dataList.size() > 0) {
            initRadio();
            setListener();
        }

    }

    public void setEditText(EditText editText){
        mEditText = editText;
    }

    private void initRadio(){
        ArrayList<EmojiPager> list = new ArrayList<>();
        for(int i = 0;i < mDataList.size(); i ++){
            list.add(new EmojiPager(mContext));

            ImageView rb = new ImageView(this.mContext);

            if(i == 0){
                rb.setBackgroundResource(R.drawable.ad_radio_mark);
            }else {
                rb.setBackgroundResource(R.drawable.ad_radio_unmark);
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins((int)EmojiUtils.getRawSize(TypedValue.COMPLEX_UNIT_DIP, 4 ,mContext), 0 , (int) EmojiUtils.getRawSize(TypedValue.COMPLEX_UNIT_DIP, 4, mContext),0);

            rb.setClickable(false);
            mEmojiRadio.addView(rb, i, layoutParams);
        }
        mAdapter.mList = list;
        mViewPager.setOffscreenPageLimit(mDataList.size());
        mViewPager.setAdapter(mAdapter);
    }

    private void setListener(){
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                invalidate();
            }

            @Override
            public void onPageSelected(int position) {
                for(int i = 0 ; i < mEmojiRadio.getChildCount() ; i ++ ){
                    if(i == position){
                        ((ImageView)(mEmojiRadio.getChildAt(i))).setBackgroundResource(R.drawable.ad_radio_mark);
                    }else{
                        ((ImageView)(mEmojiRadio.getChildAt(i))).setBackgroundResource(R.drawable.ad_radio_unmark);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public class EmojiPagerAdapter extends PagerAdapter {
        public ArrayList<EmojiPager> mList;

        @Override
        public int getCount() {
            return mList == null ? 0 : mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            EmojiPager item = mList.get(position);
            item.init(mDataList.get(position),mEditText);
            container.addView(item, 0);
            return item;
        }
    }

}
