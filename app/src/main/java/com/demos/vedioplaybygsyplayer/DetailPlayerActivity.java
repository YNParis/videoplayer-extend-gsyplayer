package com.demos.vedioplaybygsyplayer;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demos.vedioplaybygsyplayer.adapter.PlayerAdapter;
import com.demos.vedioplaybygsyplayer.util.CommonUtils;
import com.demos.vedioplaybygsyplayer.view.LandLayoutVideo;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class DetailPlayerActivity extends AppCompatActivity implements View.OnClickListener {

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
    @BindView(R.id.layout_bottom_inside)
    LinearLayout layoutBottomInside;

    @BindView(R.id.img_file)
    ImageView imageView;
    @BindView(R.id.capture_result)
    RelativeLayout captureResult;
    @BindView(R.id.capture_result_inside)
    RelativeLayout captureResultInside;


    @BindView(R.id.titleBar)
    LinearLayout titleBar;
    @BindView(R.id.title_bar_inside)
    LinearLayout titleBarInside;

    @BindView(R.id.btn_list)
    TextView btnList;
    @BindView(R.id.btn_list_inside)
    TextView btnListInside;

    private PlayerAdapter playerAdapter;
    private boolean isSingle = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_player);
        ButterKnife.bind(this);
//        GSYVideoManager.instance().enableRawPlay(getApplicationContext());
        initPlay();
        initClick();
    }

    private void initClick() {
        //初始化后的点击事件
        btnList.setOnClickListener(this);
        btnListInside.setOnClickListener(this);
    }

    private void initPlay() {
        if (isSingle) {
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
        isSingle = true;
    }

    @Override
    public void onBackPressed() {
        //先返回正常状态
        CommonUtils.toast(this, "onback:" + ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            toFull(null);
            return;
        }
        //释放所有
        detailPlayer.setVideoAllCallBack(null);
        super.onBackPressed();
    }

    /**
     * 切换清晰度
     * TODO 待参考无缝切换样例
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
        if (isSingle) {
            CommonUtils.toast(this, "单屏抓图");
            CommonUtils.capture(detailPlayer);
            return;
        }
        CommonUtils.toast(this, "多屏抓图");
        playerAdapter.capture();
    }

    private void showCaptureResult() {
        if (findViewById(R.id.capture_result) != null) {
            findViewById(R.id.capture_result).setVisibility(View.VISIBLE);
        } else {
            CommonUtils.toast(this, "没找到view");
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

    /**
     * 竖屏时修改控件的参数
     */
    private void setPortraitViews() {

        titleBar.setVisibility(View.VISIBLE);
        titleBarInside.setVisibility(View.GONE);
        layoutBottomInside.setVisibility(View.GONE);
        captureResultInside.setVisibility(View.GONE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        playerView.setLayoutParams(params);
        playerView.setOnClickListener(null);

    }

    private void setLandscapeViews() {
        //titleBar隐藏
        titleBar.setVisibility(View.GONE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        playerView.setLayoutParams(params);
        playerView.setOnClickListener(this);
        detailPlayer.setCustomOnClickListener(new LandLayoutVideo.CustomOnClickListener() {
            @Override
            public void onSingleTap() {
                showFunctions();
//                CommonUtils.toast(DetailPlayerActivity.this, "单击");
            }
        });
    }

    public void switchScreens(View view) {
        if (detailPlayer.getVisibility() != View.VISIBLE) {
            recyclerView.setVisibility(View.GONE);
            detailPlayer.setVisibility(View.VISIBLE);
            isSingle = true;
            return;
        }
        if (playerAdapter == null) {
            playerAdapter = new PlayerAdapter(this);
            recyclerView.setAdapter(playerAdapter);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }
        detailPlayer.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        isSingle = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_list:
            case R.id.btn_list_inside:
                //打开播放列表
                openListWindow();
                break;
            default:
                break;
        }
    }

    private void showFunctions() {
        //全屏时显示titlebar和底部功能栏
        //TODO 定时任务，无操作5秒后隐藏
        titleBarInside.setVisibility(View.VISIBLE);
        layoutBottomInside.setVisibility(View.VISIBLE);
    }

    private void hideFunctions() {
        //全屏时显示titlebar和底部功能栏
        //TODO 定时任务，无操作5秒后隐藏
        titleBarInside.setVisibility(View.GONE);
        layoutBottomInside.setVisibility(View.GONE);
    }

    private void openListWindow() {
        CommonUtils.toast(this, "打开列表");
    }
}
