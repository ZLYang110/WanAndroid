package com.zlyandroid.wanandroid.ui.core.bean;

import com.google.gson.Gson;
import com.zlyandroid.wanandroid.util.JsonFormatUtils;

import java.io.Serializable;

public class BaseBean implements Serializable {

    public String toJson() {
        return new Gson().toJson(this);
    }

    public String toFormatJson() {
        return JsonFormatUtils.format(toJson());
    }
}