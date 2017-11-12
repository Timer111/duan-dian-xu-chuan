package com.dell.lzb20171111.api;

import com.dell.lzb20171111.bean.MyBean;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by DELL on 2017/11/11.
 */

public interface ApiService {

    @GET("page_{page}.json")
    Observable<List<MyBean>> getData(@Path("page") int page);
}
