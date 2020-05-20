package com.demos.vedioplaybygsyplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demos.vedioplaybygsyplayer.R;
import com.demos.vedioplaybygsyplayer.bean.VideoModel;
import com.demos.vedioplaybygsyplayer.view.MultiSampleVideo;

import java.util.ArrayList;
import java.util.List;

import static com.shuyu.gsyvideoplayer.GSYVideoBaseManager.TAG;

/**
 * 旧的播放列表adapter
 */
public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private Context context;
    private String fullKey = "null";
    private List<VideoModel> list = new ArrayList<>();

    public PlayerAdapter(Context context) {
        this.context = context;
        for (int i = 0; i < 4; i++) {
            list.add(new VideoModel());
        }
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
        //多个播放时必须在setUpLazy、setUp和getGSYVideoManager()等前面设置
        final String url = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4";

        //多个播放时必须在setUpLazy、setUp和getGSYVideoManager()等前面设置
        holder.player.setPlayTag(TAG);
        holder.player.setPlayPosition(position);

        boolean isPlaying = holder.player.getCurrentPlayer().isInPlayingState();

        if (!isPlaying) {
            holder.player.setUpLazy(url, false, null, null, "这是title");
        }

        holder.player.setRotateViewAuto(true);
        holder.player.setLockLand(true);
        holder.player.setReleaseWhenLossAudio(false);
        holder.player.setShowFullAnimation(true);
        holder.player.setIsTouchWiget(false);
        holder.player.setNeedLockFull(true);
        holder.player.startPlayLogic();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PlayerViewHolder extends RecyclerView.ViewHolder {

        MultiSampleVideo player;

        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            player = itemView.findViewById(R.id.player_item);
        }
    }

}
