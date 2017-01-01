package com.feiyu.scripsaying.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by HONGDA on 2017/1/1.
 */
public class DisCoverPagerAdapter extends PagerAdapter {
    private List<View> scripViews;

    public DisCoverPagerAdapter(List<View> scripViews) {
        this.scripViews = scripViews;
    }

    @Override
    public int getCount() {
        return scripViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(scripViews.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(scripViews.get(position));
        return scripViews.get(position);
    }
}
