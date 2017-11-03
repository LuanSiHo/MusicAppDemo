package com.hosiluan.musicdemoapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by User on 11/3/2017.
 */

public class ReyclerViewMusicAdapter extends RecyclerView.Adapter<ReyclerViewMusicAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<String> mMusicList;
    private MusicAdapterListener mMusicAdapterListener;

    public ReyclerViewMusicAdapter(Context mContext, ArrayList<String> mMusicList,
                                   MusicAdapterListener mMusicAdapterListener) {
        this.mContext = mContext;
        this.mMusicList = mMusicList;
        this.mMusicAdapterListener = mMusicAdapterListener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.music_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        String songName = mMusicList.get(position);
        holder.songNameTextView.setText(songName);

        holder.playImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMusicAdapterListener.onPlayClick(position);
                holder.playImageButton.setImageResource(R.drawable.ic_pause);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMusicList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{


        TextView songNameTextView;
        ImageButton clockImageButton,playImageButton;

        public MyViewHolder(View itemView) {
            super(itemView);

            songNameTextView = itemView.findViewById(R.id.tv_song_name);
            clockImageButton = itemView.findViewById(R.id.img_btn_clock);
            playImageButton = itemView.findViewById(R.id.img_btn_play);

        }
    }

    public interface MusicAdapterListener {
        void onPlayClick(int position);
    }
}
