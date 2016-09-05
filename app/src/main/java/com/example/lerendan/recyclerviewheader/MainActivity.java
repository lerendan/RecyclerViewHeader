package com.example.lerendan.recyclerviewheader;

import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView mRecyclerView;
    private MyAdapter mMyAdapter;
    private MyViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private List<String> mList;
    private List<View> views;

    // 图片资源
    private static final int[] pics = {R.layout.view1,R.layout.view2, R.layout.view3, R.layout.view4};

    // 底部小点图片
    private ImageView[] dots;

    // 记录当前选中位置
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //如果是5.0以上的系统，就显示为沉浸栏
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        getSupportActionBar().hide();

        initView();
        initData();


        //为RecyclerView添加HeaderView和FooterView
        setHeaderView(mRecyclerView);
        setFooterView(mRecyclerView);
    }

    private void initView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    //初始化RecyclerView中每个item的数据
    private void initData() {
        mList = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            mList.add("I am a list item " + i);
        }
        mMyAdapter = new MyAdapter(mList);
        mRecyclerView.setAdapter(mMyAdapter);
    }


    private void setHeaderView(RecyclerView view) {
        View header = LayoutInflater.from(this).inflate(R.layout.header, view, false);
        viewPager = (ViewPager) header.findViewById(R.id.vp_header);
        views = new ArrayList<>();
        // 初始化引导页视图列表
        for (int i = 0; i < pics.length; i++) {
            View adView = LayoutInflater.from(this).inflate(pics[i], null);
            views.add(adView);
        }
        viewPagerAdapter = new MyViewPagerAdapter(views);
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.setOnPageChangeListener(new PageChangeListener());
        initDots(header);


        viewPager.setCurrentItem(0);
        //3秒定时
        mAdvertiseHandler.sendEmptyMessageDelayed(0, 2000);

        mMyAdapter.setHeaderView(header);
    }

    private Handler mAdvertiseHandler = new Handler() {
        public void handleMessage(Message msg) {
            viewPager.setCurrentItem((viewPager.getCurrentItem() + 1)%views.size());
            // 每隔三秒发送一次
            mAdvertiseHandler.sendEmptyMessageDelayed(0, 3000);
        }
    };

    private void setFooterView(RecyclerView view) {
        View footer = LayoutInflater.from(this).inflate(R.layout.footer, view, false);
        mMyAdapter.setFooterView(footer);
    }

    private void initDots(View header) {
        LinearLayout ll = (LinearLayout) header.findViewById(R.id.ll);
        dots = new ImageView[pics.length];

        // 循环取得小点图片
        for (int i = 0; i < pics.length; i++) {
            // 得到一个LinearLayout下面的每一个子元素
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(false);// 都设为灰色
            dots[i].setOnClickListener(this);
            dots[i].setTag(i);// 设置位置tag，方便取出与当前位置对应
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(true); // 设置为白色，即选中状态

    }



    @Override
    public void onClick(View v) {
        Toast.makeText(this, v.getTag().toString(), Toast.LENGTH_SHORT).show();
        int position = (Integer) v.getTag();
        setCurView(position);
        setCurDot(position);
    }


    //设置当前view
    private void setCurView(int position) {
        if (position < 0 || position >= pics.length) {
            return;
        }
        viewPager.setCurrentItem(position);
    }

    //设置当前指示点
    private void setCurDot(int position) {
        if (position < 0 || position > pics.length || currentIndex == position) {
            return;
        }
        dots[position].setEnabled(true);
        dots[currentIndex].setEnabled(false);
        currentIndex = position;
    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {
        // 当滑动状态改变时调用
        @Override
        public void onPageScrollStateChanged(int position) {
            // arg0 ==1的时辰默示正在滑动，arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做。
        }

        // 当前页面被滑动时调用
        @Override
        public void onPageScrolled(int position, float arg1, int arg2) {
            // arg0 :当前页面，及你点击滑动的页面
            // arg1:当前页面偏移的百分比
            // arg2:当前页面偏移的像素位置
        }

        // 当新的页面被选中时调用
        @Override
        public void onPageSelected(int position) {
            // 设置底部小点选中状态
            setCurDot(position);
        }
    }

}
