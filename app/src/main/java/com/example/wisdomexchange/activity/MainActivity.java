package com.example.wisdomexchange.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.wisdomexchange.R;
import com.example.wisdomexchange.base.BaseActivity;
import com.example.wisdomexchange.fragment.ChatFragment;
import com.example.wisdomexchange.fragment.NewsFragment;
import com.example.wisdomexchange.fragment.PersonFragment;
import com.example.wisdomexchange.fragment.PoemFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;


/**
 * @Author: PengLiang
 * @Time: 2020/11/21
 * @Description: 主页面
 */
public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private BottomNavigationView mBNav;
    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        switchFragments(0);
        mBNav.setOnNavigationItemSelectedListener(this);
    }

    /**
     初始化Fragment并将Fragment添加到fragments中，此处的fragments是一个Fragment的列表
     private List<Fragment> fragments;
     */
    private void initData() {
        fragments = new ArrayList<>();
        fragments.add(new ChatFragment());
        fragments.add(new PoemFragment());
        fragments.add(new NewsFragment());
        fragments.add(new PersonFragment());
    }


    /**
     * 切换fragment
     */
    private void switchFragments(int index) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            if (i == index) {//显示fragment
                if (fragment.isAdded()) {//如果当前fragment已经创建,直接显示fragment
                    fragmentTransaction.show(fragment);
                } else {//第一创建fragment, 将fragment放入到容器中
                    fragmentTransaction.add(R.id.fl_main, fragment);
                }
            } else {//隐藏剩余fragment
                if (fragment.isAdded()) {
                    fragmentTransaction.hide(fragment);
                }
            }
        }

        fragmentTransaction.commitNowAllowingStateLoss();
    }



    @SuppressLint("WrongConstant")
    private void initView() {
        mBNav = findViewById(R.id.bottom_navigation);

        // 设置大于3个menu文字显示
        mBNav.setLabelVisibilityMode(1);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_chat:
               switchFragments(0);
                break;
            case R.id.action_poem:
                switchFragments(1);
                break;
            case R.id.action_news:
                switchFragments(2);
                break;
            case R.id.action_person:
                switchFragments(3);
                break;
        }
        return true;
    }

}