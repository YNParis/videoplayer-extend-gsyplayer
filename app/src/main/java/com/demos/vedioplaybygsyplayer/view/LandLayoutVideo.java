package com.demos.vedioplaybygsyplayer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.demos.vedioplaybygsyplayer.R;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

/**
 * 播放view
 */
public class LandLayoutVideo extends StandardGSYVideoPlayer {

    private boolean isLinkScroll = false;

    private Context context;
    private View captureResultView;

    /**
     * 1.5.0开始加入，如果需要不同布局区分功能，需要重载
     */
    public LandLayoutVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
        this.context = context;
    }

    public LandLayoutVideo(Context context) {
        super(context);
        this.context = context;
    }

    public LandLayoutVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }


    @Override
    protected void init(Context context) {
        super.init(context);
        post(new Runnable() {
            @Override
            public void run() {
                gestureDetector = new GestureDetector(getContext().getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        touchDoubleUp();
                        return super.onDoubleTap(e);
                    }

                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        if (!mChangePosition && !mChangeVolume && !mBrightness) {
                            onClickUiToggle();
                        }
                        return super.onSingleTapConfirmed(e);
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {
                        super.onLongPress(e);
                        //长按，删除view显示
                        showDeleteView();
                    }
                });
            }
        });
    }

    private void showDeleteView() {
        //删除view
        Toast.makeText(context, "删除", Toast.LENGTH_SHORT).show();
    }

    //这个必须配置最上面的构造才能生效
    @Override
    public int getLayoutId() {
        if (mIfCurrentIsFullscreen) {
            Log.e("single_video", "-------single_video_land");
            return R.layout.single_video_land;
        }
        Log.e("single_video", "-------single_video_normal");
        return R.layout.single_video_normal;
    }

    @Override
    protected void updateStartImage() {
        if (mIfCurrentIsFullscreen) {
            if (mStartButton instanceof ImageView) {
                ImageView imageView = (ImageView) mStartButton;
                if (mCurrentState == CURRENT_STATE_PLAYING) {
                    imageView.setImageResource(R.drawable.video_click_pause_selector);
                } else if (mCurrentState == CURRENT_STATE_ERROR) {
                    imageView.setImageResource(R.drawable.video_click_play_selector);
                } else {
                    imageView.setImageResource(R.drawable.video_click_play_selector);
                }
            }
        } else {
            super.updateStartImage();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isLinkScroll && !isIfCurrentIsFullscreen()) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    protected void resolveNormalVideoShow(View oldF, ViewGroup vp, GSYVideoPlayer gsyVideoPlayer) {
        LandLayoutVideo landLayoutVideo = (LandLayoutVideo) gsyVideoPlayer;
        landLayoutVideo.dismissProgressDialog();
        landLayoutVideo.dismissVolumeDialog();
        landLayoutVideo.dismissBrightnessDialog();
        super.resolveNormalVideoShow(oldF, vp, gsyVideoPlayer);
    }

    public void setLinkScroll(boolean linkScroll) {
        isLinkScroll = linkScroll;
    }


    /**
     * 定义结束后的显示
     */
    @Override
    protected void changeUiToCompleteClear() {
        super.changeUiToCompleteClear();
        setTextAndProgress(0, true);
        //changeUiToNormal();
    }

    @Override
    protected void changeUiToCompleteShow() {
        super.changeUiToCompleteShow();
        setTextAndProgress(0, true);
        //changeUiToNormal();
    }

    public void showCaptureResult() {
        if (captureResultView == null) {
            captureResultView = findViewById(R.id.capture_result);
        }
        if (captureResultView.getVisibility() != VISIBLE) {
            captureResultView.setVisibility(VISIBLE);
        }
    }
}
