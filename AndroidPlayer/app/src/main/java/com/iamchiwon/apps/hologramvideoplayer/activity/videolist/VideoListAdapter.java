package com.iamchiwon.apps.hologramvideoplayer.activity.videolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iamchiwon.apps.hologramvideoplayer.R;
import com.iamchiwon.apps.hologramvideoplayer.model.VideoFile;

import java.util.ArrayList;

/**
 * Created by iamchiwon on 2017. 4. 14..
 */

public class VideoListAdapter extends BaseAdapter {

    Context context;
    ArrayList<VideoFile> videos;

    public VideoListAdapter(Context context) {
        this.context = context;
        videos = new ArrayList<>();
    }

    public void updateList(ArrayList<VideoFile> videos) {
        this.videos = videos;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return videos.size();
    }

    @Override
    public VideoFile getItem(int i) {
        return videos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return videos.get(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.view_video_list_item, viewGroup, false);

            view.setTag(0, view.findViewById(R.id.thumbnailImage));
            view.setTag(1, view.findViewById(R.id.filename));
        }

        ImageView thumbnailImage = (ImageView) view.getTag(0);
        TextView filename = (TextView) view.getTag(1);

        VideoFile info = getItem(i);
        filename.setText(info.getFilepath());

        return view;
    }
}
