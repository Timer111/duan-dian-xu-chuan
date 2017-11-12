package com.dell.lzb20171111.model;

import com.dell.lzb20171111.api.API;
import com.dell.lzb20171111.api.ApiService;
import com.dell.lzb20171111.bean.MyBean;
import com.dell.lzb20171111.utils.LoggingInterceptor;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by DELL on 2017/11/11.
 */

public class Model implements Imodel {

    private List<MyBean.DataBean> list = new ArrayList<>();
    OnFinish onFinish;

    public interface OnFinish{
        void OnFinishListener( List<MyBean.DataBean> list);
    }

    public void setOnFinish(OnFinish onFinish) {
        this.onFinish = onFinish;
    }

    @Override
    public void RequestSuccess(String url,int page) {
        OkHttpClient client = new OkHttpClient
                .Builder()
                .addInterceptor(new LoggingInterceptor())
                .build();

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);
        final Observable<List<MyBean>> bean = service.getData(page);

        bean.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<MyBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<MyBean> myBeen) {
                        List<MyBean.DataBean> list = myBeen.get(0).getData();
                        onFinish.OnFinishListener(list);
                    }
                });
    }
}
