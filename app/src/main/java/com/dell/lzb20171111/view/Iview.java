package com.dell.lzb20171111.view;

import com.dell.lzb20171111.bean.MyBean;

import java.util.List;

/**
 * Created by DELL on 2017/11/11.
 */

public interface Iview {

    void showSuccess(List<MyBean.DataBean> list);
    void showError(String url);
}
