package com.quaie.wms.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private HengXiangDaiJinDuProgress mHxdprogress;
    private YuanXingDaiJinDuProgress mYxdprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        yibuyibu();
    }

    private void initView() {
        mHxdprogress = (HengXiangDaiJinDuProgress) findViewById(R.id.hxdprogress);
        mYxdprogress = (YuanXingDaiJinDuProgress) findViewById(R.id.yxdprogress);
    }

    private void yibuyibu() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mHxdprogress.getProgress() < 100 && mYxdprogress.getProgress() < 100) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mHxdprogress.incrementProgressBy(1);
                    mYxdprogress.incrementProgressBy(1);

                }
            }
        }).start();
    }
}
