package com.dell.lzb20171111.presenter;

import com.dell.lzb20171111.bean.MyBean;
import com.dell.lzb20171111.model.Model;
import com.dell.lzb20171111.view.Iview;

import java.util.List;

/**
 * Created by DELL on 2017/11/11.
 */

public class Presenter implements Model.OnFinish {

    Iview iview;
    Model model;

    public Presenter(Iview iview) {
        this.iview = iview;
        this.model = new Model();
        model.setOnFinish(this);
    }

    public void getUrl(String url,int page){
        model.RequestSuccess(url,page);
    }

    @Override
    public void OnFinishListener(List<MyBean.DataBean> list) {
        iview.showSuccess(list);
    }
}
