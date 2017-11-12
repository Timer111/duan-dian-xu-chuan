package com.dell.lzb20171111;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dell.lzb20171111.api.API;
import com.dell.lzb20171111.bean.MyBean;
import com.dell.lzb20171111.item.ProgressDownloader;
import com.dell.lzb20171111.item.ProgressResponseBody;
import com.dell.lzb20171111.presenter.Presenter;
import com.dell.lzb20171111.view.Iview;

import java.io.File;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

public class MainActivity extends AppCompatActivity implements Iview,ProgressResponseBody.ProgressListener {

    @InjectView(R.id.recycler_view)
    RecyclerView recyclerView;
    @InjectView(R.id.swip_layout)
    SwipeRefreshLayout swipLayout;
    @InjectView(R.id.pb)
    ProgressBar pb;
    private MyAdapter adapter;
    private int page = 1;
    private LinearLayoutManager linearLayoutManager;
    private Presenter p;
    private List<MyBean.DataBean> list;
    public static final String TAG = "MainActivity";
    public static final String PACKAGE_URL = "http://gdown.baidu.com/data/wisegame/df65a597122796a4/weixin_821.apk";
    private long breakPoints;
    private ProgressDownloader downloader;
    private long totalBytes;
    private long contentLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        p = new Presenter(this);
        p.getUrl(API.BASE_URL, page);
    }

    @Override
    public void showSuccess(final List<MyBean.DataBean> list) {
        adapter = new MyAdapter(list, MainActivity.this);
        recyclerView.setAdapter(adapter);


        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition == list.size() - 1) {
                    page++;
                    p.getUrl(API.BASE_URL, page);
                }
            }
        });

        swipLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page++;
                p.getUrl(API.BASE_URL, page);
                swipLayout.setRefreshing(false);
            }
        });


        //点击下载
        adapter.setOnItemClickListener(new MyAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                pb.setVisibility(View.VISIBLE);
                breakPoints = 0L;
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"sample.apk");
                downloader = new ProgressDownloader(PACKAGE_URL,file, (ProgressResponseBody.ProgressListener) MainActivity.this);
                downloader.download(0L);
            }
        });
        adapter.setOnItemLongClickListener(new MyAdapter.OnRecyclerViewItemLongClickListener() {
            @Override
            public void onItemLongClickListener(View view, int position) {
                downloader.pause();
                Toast.makeText(MainActivity.this,"下载暂停",Toast.LENGTH_SHORT).show();
                breakPoints = totalBytes;
            }
        });
    }

    @Override
    public void showError(String url) {

    }

    @Override
    public void onPreExecute(long contentLength) {
        if(this.contentLength == 0L){
            this.contentLength = contentLength;
            pb.setMax((int) (contentLength/1024));
        }
    }

    @Override
    public void update(long totalBytes, boolean done) {
        this.totalBytes = totalBytes+breakPoints;
        pb.setProgress((int) (totalBytes+breakPoints)/1024);
        if(done){
            Observable.empty()
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnCompleted(new Action0() {
                        @Override
                        public void call() {
                            Toast.makeText(MainActivity.this,"下载完成",Toast.LENGTH_SHORT).show();
                        }
                    }).subscribe();
        }
    }
}
