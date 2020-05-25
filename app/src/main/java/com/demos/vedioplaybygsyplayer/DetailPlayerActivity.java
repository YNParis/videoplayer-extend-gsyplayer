package com.demos.vedioplaybygsyplayer;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demos.vedioplaybygsyplayer.adapter.PlayerAdapter;
import com.demos.vedioplaybygsyplayer.util.CommonUtils;
import com.demos.vedioplaybygsyplayer.view.LandLayoutVideo;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYVideoShotListener;

import java.io.File;
import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class DetailPlayerActivity extends AppCompatActivity {

    @BindView(R.id.single_player)
    LandLayoutVideo detailPlayer;

    @BindView(R.id.rv_multi_player)
    RecyclerView recyclerView;

    @BindView(R.id.root_layout)
    LinearLayout rootView;

    @BindView(R.id.player_view)
    RelativeLayout playerView;

    @BindView(R.id.layout_bottom)
    LinearLayout layoutBottom;

    @BindView(R.id.img_file)
    ImageView imageView;
    @BindView(R.id.titleBar)
    LinearLayout titleBar;

    private PlayerAdapter playerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_player);
        ButterKnife.bind(this);
//        GSYVideoManager.instance().enableRawPlay(getApplicationContext());
        initPlay();
    }

    private void initPlay() {
        if (Constants.isSingle) {
            detailPlayer.setUp(Constants.URL, true, "测试视频");
            detailPlayer.startPlayLogic();
        } else {
            switchScreens(null);
        }
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
        Constants.isSingle = true;
    }

    @Override
    public void onBackPressed() {
        //先返回正常状态
        toast("onback:" + ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
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

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void capture(View view) {
        toast("抓图");
        detailPlayer.taskShotPic(new GSYVideoShotListener() {
            @Override
            public void getBitmap(Bitmap bitmap) {
                try {
                    File file = CommonUtils.saveBitmap(bitmap);
                    if (file != null) {
                        Uri fileUri = Uri.fromFile(file);
                        toast("抓图成功");
                        showCaptureResult();
                        imageView.setImageURI(fileUri);
                    } else {
                        toast("抓图失败");
                    }
                } catch (FileNotFoundException e) {
                    toast("抓图失败");
                    e.printStackTrace();
                }
            }
        });

    }

    private void showCaptureResult() {
        if (findViewById(R.id.capture_result) != null) {
            findViewById(R.id.capture_result).setVisibility(View.VISIBLE);
        } else {
            toast("没找到view");
        }
    }

    public void toFull(View view) {
        Log.e("video", "屏幕方向：" + getRequestedOrientation());
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            Log.e("video", "屏幕方向：SCREEN_ORIENTATION_LANDSCAPE");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            Log.e("video", "屏幕方向：SCREEN_ORIENTATION_PORTRAIT");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            //TODO 隐藏状态栏，导航栏
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果在menifest里面没有设置configChanges属性，就不走这里了
        Log.e("video", "屏幕方向切换");
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.e("video", "当前是竖屏");
            setPortraitViews();
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.e("video", "当前是横屏");
            setLandscapeViews();
        }
    }

    private void setPortraitViews() {


    }

    private void setLandscapeViews() {
        titleBar.setVisibility(View.GONE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        playerView.setLayoutParams(params);
    }

    public void switchScreens(View view) {
        if (detailPlayer.getVisibility() != View.VISIBLE) {
            recyclerView.setVisibility(View.GONE);
            detailPlayer.setVisibility(View.VISIBLE);
            Constants.isSingle = true;
            return;
        }
        if (playerAdapter == null) {
            playerAdapter = new PlayerAdapter(this);
            recyclerView.setAdapter(playerAdapter);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }
        detailPlayer.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        Constants.isSingle = false;
    }

    private void toast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }
}
