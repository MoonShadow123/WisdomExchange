package com.example.wisdomexchange.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wisdomexchange.R;
import com.example.wisdomexchange.adapter.MsgAdapter;
import com.example.wisdomexchange.base.BaseActivity;
import com.example.wisdomexchange.bean.Msg;
import com.example.wisdomexchange.util.HttpUtil;
import com.example.wisdomexchange.util.NetCheckUtil;
import com.example.wisdomexchange.util.Resource;
import com.example.wisdomexchange.util.ToastUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @Author: PengLiang
 * @Time: 2020/11/21
 * @Description: 聊天页面
 */
public class ChatActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private Button sendBtn;
    private EditText inputEt;

    private String content; // 用户输入的内容
    private String replay;  // 服务器返回的内容
    private List<Msg> msgList; // 存储消息的列表
    private MsgAdapter adapter;
    private LinearLayout ll_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView(); // 初始化组件
        initRcy(); // 初始化RecyclerView

        if (!NetCheckUtil.checkNet(this)) {
            ToastUtil.show(this, "网络未连接");
        }
    }

    private void initRcy() {
        msgList = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new MsgAdapter(msgList);
        recyclerView.setAdapter(adapter);

        // 加载第一条提示语句
        addNewMessage(this.getString(R.string.chat_welcome), Msg.TYPE_RECEIVED);
    }

    private void initView() {
        recyclerView = findViewById(R.id.rv_chat);
        sendBtn = findViewById(R.id.btn_chat_send);
        inputEt = findViewById(R.id.et_chat_input);
        ll_chat = findViewById(R.id.ll_chat);

        sendBtn.setOnClickListener(this);
        inputEt.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_chat_send:
                submit();  // 按钮的提交
                break;
            case R.id.et_chat_input:
                downChat();  // 设置弹出输入法键盘不遮挡一部分聊天记录
                break;
        }
    }

    // 设置弹出输入法键盘不遮挡一部分聊天记录
    private void downChat() {
        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                // 判断软键盘是否弹出
                if (oldBottom != -1 && oldBottom > bottom) {
                    recyclerView.requestLayout(); // 重新请求布局
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            // 滚动到最后一个
                            recyclerView.scrollToPosition(msgList.size() - 1);
                        }
                    });
                }
            }
        });
    }

    //     按钮的提交
    private void submit() {
        content = inputEt.getText().toString().trim();
        // 判断网络情况
        if (NetCheckUtil.checkNet(this)) {
            if (!TextUtils.isEmpty(content)) {
                addNewMessage(content, Msg.TYPE_SEND);
                inputEt.setText("");  // 发送消息后输入框清空
                sendHttpRequest();
            } else {
                ToastUtil.show(this,"你没有输入内容");
            }
        } else {
            ToastUtil.show(this, "网络连接不可用 ，请稍后重试");
        }


    }

    // 发送网络请求
    private void sendHttpRequest() {
        // 准备返回的数据
        RequestBody requestBody = new FormBody.Builder()
                .add("key", Resource.TianApiKey)
                .add("question", content)
                .build();

        HttpUtil.sendOkHttpRequest(Resource.Turing, requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                // 返回失败信息
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonData = response.body().string();

                // 解释json数据
                try {
                    JSONObject jsonObject = new JSONObject(jsonData);
                    if (jsonObject.getString("msg").equals("success")) {
                        replay = jsonObject.getJSONArray("newslist").getJSONObject(0).getString("reply");
                    } else {
                        replay = "接口调用次数已用完";
                    }

                    // 返回主线程更新UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addNewMessage(replay, Msg.TYPE_RECEIVED);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    // 添加新的消息到适配器中
    public void addNewMessage(String msg, int type) {
        Msg message = new Msg(msg, type);
        msgList.add(message);
        adapter.notifyItemInserted(msgList.size() - 1);  //表示将adapter的数据的msgList.size() - 1这个位置的那个数据插到msgList.size() - 1这个位置。
        recyclerView.scrollToPosition(msgList.size() - 1);  // 滚动到最后的位置
    }


    // 1.首先设置拦截器，监听点击事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // 监听按下的事件
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            View v = getCurrentFocus();
            View v = ll_chat;
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    // 2.根据LinearLayout所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时和点击发送按钮时没必要隐藏
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof LinearLayout)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l); // 获取在当前窗口内的绝对坐标

            int left = l[0];
            int top = l[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                // 当点击LinearLayout(包括editText和发送按钮)以外的区域，隐藏软件键盘
                return true;
            }
        }

        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    // 3.隐藏输入法
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    // 实现只在冷启动时显示启动页，即点击返回键与点击HOME键退出效果一致
    @Override
    public void onBackPressed() {
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

    @Override
    protected void onPause() {
        ToastUtil.cancelToast(); // 关闭Toast
        super.onPause();
    }
}