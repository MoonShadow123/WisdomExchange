package com.example.wisdomexchange.bean;

import kotlin.reflect.KType;

/**
 * @Author: PengLiang
 * @Time: 2020/11/13
 * @Description: 消息实体类
 */
public class Msg {
    public static final int TYPE_RECEIVED = 0;   // 表示接收的消息
    public static final int TYPE_SEND = 1;   // 表示发送的消息

    private String content;
    private int type;

    public Msg(String content, int type) {
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
