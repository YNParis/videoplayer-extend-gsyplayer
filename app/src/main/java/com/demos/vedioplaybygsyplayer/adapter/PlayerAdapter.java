package com.demos.vedioplaybygsyplayer.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demos.vedioplaybygsyplayer.R;
import com.demos.vedioplaybygsyplayer.bean.VideoModel;
import com.demos.vedioplaybygsyplayer.util.CommonUtils;
import com.demos.vedioplaybygsyplayer.view.MultiSampleVideo;

import java.util.ArrayList;
import java.util.List;

import static com.shuyu.gsyvideoplayer.GSYVideoBaseManager.TAG;

/**
 * 播放列表adapter
 */
public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private Context context;
    private String fullKey = "null";
    private List<VideoModel> list = new ArrayList<>();
    private List<MultiSampleVideo> playerList = new ArrayList<>();
    private PlayerViewClickListener listener;
    private PlayerViewSelectedListener selectedListener;
    private List<RelativeLayout> rootViews = new ArrayList<>();
    private int beforePosition = -1;
    private int currentPosition = -1;

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
        playerList.add(holder.player);
        rootViews.add(holder.rootView);
        //多个播放时必须在setUpLazy、setUp和getGSYVideoManager()等前面设置
        holder.player.setPlayTag(TAG);
        holder.player.setPlayPosition(position);
        boolean isPlaying = holder.player.getCurrentPlayer().isInPlayingState();
        if (!isPlaying) {
            setPlayerResource(holder.player, url);
        }
        holder.player.setRotateViewAuto(true);
        holder.player.setLockLand(true);
        holder.player.setReleaseWhenLossAudio(false);
        holder.player.setShowFullAnimation(true);
        holder.player.setIsTouchWiget(false);
        holder.player.setNeedLockFull(true);
        holder.player.startPlayLogic();
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.delete(position);
                }
                v.setVisibility(View.GONE);
            }
        });
        holder.player.setMultiPlayerViewClickListener(new MultiSampleVideo.MultiPlayerViewClickListener() {
            @Override
            public void onClick() {
                holder.rootView.setBackground(context.getDrawable(R.drawable.border_selected));
                onItemSelected(position);
                if (listener != null) listener.onClick(position);
                holder.btnDelete.setVisibility(View.GONE);
            }

            @Override
            public void onLongClick() {
                holder.btnDelete.setVisibility(View.VISIBLE);
                if (listener != null) listener.onLongClick(position);
            }
        });
    }

    public void onItemSelected(int position) {
        CommonUtils.toast(context, "onItemSelected " + position);
        beforePosition = currentPosition;
        currentPosition = position;
        if (beforePosition != -1) {
            rootViews.get(beforePosition).setBackground(null);
        }
        if (currentPosition == beforePosition) {
            currentPosition = -1;
        }
    }

    public void modifyChannel(int position, String url) {
        playerList.get(position).onVideoPause();
        setPlayerResource(playerList.get(position), url);
        playerList.get(position).startPlayLogic();

        onItemSelected(position);
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    private void setPlayerResource(MultiSampleVideo player, String url) {
        player.setUpLazy(url, false, null, null, "");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PlayerViewHolder extends RecyclerView.ViewHolder {

        MultiSampleVideo player;
        ImageView btnDelete;
        RelativeLayout rootView;

        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            player = itemView.findViewById(R.id.player_item);
            btnDelete = itemView.findViewById(R.id.btn_delete_play_window);
            rootView = itemView.findViewById(R.id.root_view);
        }
    }

    public void capture() {
        int i = 0;
        for (MultiSampleVideo player : playerList) {
            CommonUtils.capture(player);
            Log.e("file", "成功" + (++i));
        }
    }

    public interface PlayerViewClickListener {
        void onClick(int position);

        void onLongClick(int position);

        void delete(int position);
    }

    public void setPlayerViewClickListener(PlayerViewClickListener listener) {
        this.listener = listener;
    }

    public interface PlayerViewSelectedListener {
        void onSelected(int beforePosition, int currentPosition);

        void onLongClick(int position);

        void delete(int position);
    }

    public void setPlayerViewSelectedListener(PlayerViewSelectedListener listener) {
        this.selectedListener = listener;
    }


}
