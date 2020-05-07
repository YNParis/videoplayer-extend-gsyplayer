package com.demos.vedioplaybygsyplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import static com.shuyu.gsyvideoplayer.GSYVideoBaseManager.TAG;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private Context context;
    private String url = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4";
    private String fullKey = "null";

    public PlayerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_palyer, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PlayerViewHolder holder, final int position) {
        //多个播放时必须在setUpLazy、setUp和getGSYVideoManager()等前面设置
        holder.player.setPlayTag(TAG);
        holder.player.setPlayPosition(position);

        boolean isPlaying = holder.player.getCurrentPlayer().isInPlayingState();

        if (!isPlaying) {
            holder.player.setUpLazy(url, false, null, null, "这是title");
        }

        //增加title
        holder.player.getTitleTextView().setVisibility(View.GONE);

        //设置返回键
        holder.player.getBackButton().setVisibility(View.GONE);
        holder.player.setRotateViewAuto(true);
        holder.player.setLockLand(true);
        holder.player.setReleaseWhenLossAudio(false);
        holder.player.setShowFullAnimation(true);
        holder.player.setIsTouchWiget(false);
        holder.player.setNeedLockFull(true);
        holder.player.setVideoAllCallBack(new GSYSampleCallBack() {


            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                super.onQuitFullscreen(url, objects);
                fullKey = "null";
            }

            @Override
            public void onEnterFullscreen(String url, Object... objects) {
                super.onEnterFullscreen(url, objects);
                holder.player.getCurrentPlayer().getTitleTextView().setText((String) objects[0]);
                fullKey = position + "tag";
            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
                super.onAutoComplete(url, objects);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    class PlayerViewHolder extends RecyclerView.ViewHolder {

        StandardGSYVideoPlayer player;

        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            player = itemView.findViewById(R.id.player_item);
        }
    }
}
