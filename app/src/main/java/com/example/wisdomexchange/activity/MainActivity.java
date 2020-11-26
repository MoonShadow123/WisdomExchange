package com.example.wisdomexchange.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.wisdomexchange.R;
import com.example.wisdomexchange.base.BaseActivity;
import com.example.wisdomexchange.fragment.ChatFragment;
import com.example.wisdomexchange.fragment.NewsFragment;
import com.example.wisdomexchange.fragment.PersonFragment;
import com.example.wisdomexchange.fragment.PoemFragment;
import com.example.wisdomexchange.util.ToastUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;


/**
 * @Author: PengLiang
 * @Time: 2020/11/21
 * @Description: 主页面
 */
public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView mBNav;
    private List<Fragment> fragments;
    private long lastClickBackTime;
    private TextView titleTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        switchFragments(0);

        mBNav.setOnNavigationItemSelectedListener(this);

        // Android 5.0以上，设置状态栏颜色
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.toastColor));
        }

        titleTV = findViewById(R.id.tv_title);
        titleTV.setText("聊天");
    }


    /**
     * 初始化Fragment并将Fragment添加到fragments中，此处的fragments是一个Fragment的列表
     * private List<Fragment> fragments;
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
                titleTV.setText("聊天");
                switchFragments(0);
                break;
            case R.id.action_poem:
                titleTV.setText("古诗");
                switchFragments(1);
                break;
            case R.id.action_news:
                titleTV.setText("新闻");
                switchFragments(2);
                break;
            case R.id.action_person:
                titleTV.setText("个人中心");
                switchFragments(3);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //  第二次点击时，相当于用第二次的当前毫秒数 减去 第一次的当前毫秒数 如果第一次点击和第二次点击的时间不超过2000毫秒，则退出，否则不退出
        if (System.currentTimeMillis() - lastClickBackTime > 2000) { // 后退阻断
            ToastUtil.show(this, "再按一次退出");
            lastClickBackTime = System.currentTimeMillis();
        } else {
            // 实现只在冷启动时显示启动页，即点击返回键与点击HOME键退出效果一致
            ToastUtil.cancelToast(); // 关闭Toast
//        super.onBackPressed(); 这句话一定要注释掉，不然又去调用默认的back处理方式了
            // 监听返回键，让返回键实现HOME键的功能
            // 实现HOME键功能，简而言之就是回到桌面，让Activity不销毁，程序后台运行
            Intent intent = new Intent(Intent.ACTION_MAIN);
            //  Intent.FLAG_ACTIVITY_NEW_TASK  使用一个新的 Task 来启动一个 Activity ，但是启动的每个 Activity 都将在一个新的 Task 中 ( 不然 NEW_TASK 这个称号不是白费了吗 )。
            //  这种方式通常使用在 Service 中启动 Activity 的情况，由于在 Service 中不存在 Activity 栈。所以使用该 Flag 来创建一个新的 Activity 栈。并创建新的 Activity 实例 。
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
    }
}