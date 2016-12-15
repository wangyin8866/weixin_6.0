package com.wy.weinxin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试1.0asdasdasdasdasdasd
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private ViewPager mViewPager;
    private List<Fragment> mTabs = new ArrayList<>();
    private String[] mTitles = {"First Fragment!", "Second Fragment!", "Third Fragment!", "Fourth Fragment!"};
    private FragmentPagerAdapter mAdapter;

    private List<ChangeColorWithText> mTabIndicators = new ArrayList<ChangeColorWithText>();
    ChangeColorWithText one, two, three, four;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setOverflowButton();
        initView();
        initDatas();
        mViewPager.setAdapter(mAdapter);
        initEvent();
    }

    /**
     * 初始化所有事件
     */
    private void initEvent() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.e("tag", "onPageScrolled: "+position+" "+positionOffset+" "+positionOffsetPixels );
                /**
                 * 从第一页到第二页：position=0;positionOffset:0.0~1.0 ;左滑
                 *从第二页到第一页：position=0;positionOffset:1.0~0.0 ;右滑
                 */
                if (positionOffset>0) {
                    ChangeColorWithText left = mTabIndicators.get(position);
                    ChangeColorWithText right = mTabIndicators.get(position + 1);
                    left.setIconAlpha(1 - positionOffset);
                    right.setIconAlpha(positionOffset);
                }


            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initDatas() {
        for (String title : mTitles) {
            TabFragment tabFragment = new TabFragment();
            Bundle bundle = new Bundle();
            bundle.putString(TabFragment.TITLE, title);
            tabFragment.setArguments(bundle);
            mTabs.add(tabFragment);
        }
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mTabs.get(position);
            }

            @Override
            public int getCount() {
                return mTabs.size();
            }
        };

    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.id_viewPager);
        one = (ChangeColorWithText) findViewById(R.id.id_indicator_one);
        two = (ChangeColorWithText) findViewById(R.id.id_indicator_two);
        three = (ChangeColorWithText) findViewById(R.id.id_indicator_three);
        four = (ChangeColorWithText) findViewById(R.id.id_indicator_four);
        mTabIndicators.add(one);
        mTabIndicators.add(two);
        mTabIndicators.add(three);
        mTabIndicators.add(four);
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        one.setIconAlpha(1.0f);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * 通过反射使OverflowButton一直显示（默认不显示）
     */

    private void setOverflowButton() {
        try {
            ViewConfiguration configuration = ViewConfiguration.get(this);
            Field field = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            field.setAccessible(true);
            field.setBoolean(configuration, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置menu显示icon
     *
     * @param featureId
     * @param menu
     * @return
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public void onClick(View v) {
        /**
         * 与其他点击事件分开，防止调用restOtherTabs();
         */
        clickTab(v);
        /**
         * 其他点击事件
         */
        switch (v.getId()) {

        }

    }

    /**
     * 点击TAB
     * @param v
     */
    private void clickTab(View v) {
        restOtherTabs();
        switch (v.getId()) {
            case R.id.id_indicator_one:
                mTabIndicators.get(0).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(0,false);
                break;
            case R.id.id_indicator_two:
                mTabIndicators.get(1).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(1,false);
                break;
            case R.id.id_indicator_three:
                mTabIndicators.get(2).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(2,false);
                break;
            case R.id.id_indicator_four:
                mTabIndicators.get(3).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(3,false);
                break;

        }
    }

    private void restOtherTabs() {
        for (int i = 0; i < mTabIndicators.size(); i++) {
            mTabIndicators.get(i).setIconAlpha(0);

        }
    }

}
