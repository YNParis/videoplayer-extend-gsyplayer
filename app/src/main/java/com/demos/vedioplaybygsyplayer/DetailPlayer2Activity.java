package com.demos.vedioplaybygsyplayer;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.demos.vedioplaybygsyplayer.util.CommonUtils;
import com.demos.vedioplaybygsyplayer.view.LandLayoutVideo;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYVideoShotListener;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;

import java.io.FileNotFoundException;

public class DetailPlayer2Activity extends AppCompatActivity {

    private LandLayoutVideo detailPlayer;
    OrientationUtils orientationUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_player_2);
        detailPlayer = findViewById(R.id.single_player);
        GSYVideoManager.instance().enableRawPlay(getApplicationContext());
        detailPlayer.setUp(Constants.URL, true, "测试视频");
        //设置旋转
        orientationUtils = new OrientationUtils(this, detailPlayer);
        orientationUtils.setEnable(true);
        detailPlayer.startPlayLogic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        detailPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        detailPlayer.onVideoResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    @Override
    public void onBackPressed() {
        //先返回正常状态
        toast("onback:" + ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            toFull(null);
            return;
        }
        //释放所有
        detailPlayer.setVideoAllCallBack(null);
        super.onBackPressed();
    }

    /**
     * 切换清晰度,参考无缝切换样例
     *
     * @param view
     */
    public void switchPixel(View view) {
        detailPlayer.onVideoPause();
        detailPlayer.setUp(Constants.URL_2, true, "测试视频2");
        detailPlayer.onVideoReset();
        detailPlayer.startPlayLogic();
    }

    public void capture(View view) {
        toast("抓图");
        detailPlayer.taskShotPic(new GSYVideoShotListener() {
            @Override
            public void getBitmap(Bitmap bitmap) {
                try {
                    CommonUtils.saveBitmap(bitmap);
                    toast("抓图成功");
                    showCaptureResult();
                } catch (FileNotFoundException e) {
                    toast("抓图失败");
                    e.printStackTrace();
                }
            }
        });

    }

    private void showCaptureResult() {
        //判断是横屏还是竖屏

    }

    public void toFull(View view) {
        orientationUtils.resolveByClick();
    }

    public void switchScreens(View view) {

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        detailPlayer.onConfigurationChanged(this, newConfig, orientationUtils, true, true);
    }

    private void toast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }
}
