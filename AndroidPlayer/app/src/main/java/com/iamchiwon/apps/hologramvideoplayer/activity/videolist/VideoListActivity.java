package com.iamchiwon.apps.hologramvideoplayer.activity.videolist;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.iamchiwon.apps.hologramvideoplayer.R;
import com.iamchiwon.apps.hologramvideoplayer.activity.videoplayer.VideoPlayerActivity;
import com.iamchiwon.apps.hologramvideoplayer.model.VideoFile;
import com.iamchiwon.apps.hologramvideoplayer.viewmodel.VideoListViewModel;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by iamchiwon on 2017. 4. 12..
 */

public class VideoListActivity extends AppCompatActivity {

    final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 9999;

    @BindView(R.id.videoFileList)
    RecyclerView videoFileList;

    VideoListViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        ButterKnife.bind(this);

        viewModel = new VideoListViewModel();

        VideoFileListAdapter adapter = new VideoFileListAdapter();
        videoFileList.setAdapter(adapter);
        videoFileList.setLayoutManager(new LinearLayoutManager(this));

        checkReadStoragePermission();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_camera) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onPermissionGranted() {
        viewModel.fetchVideoFiles(this);
        viewModel.rxVideos().subscribe(videos -> {
            viewModel.fetchVideoFiles(VideoListActivity.this);
        });
    }

    private void checkReadStoragePermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            onPermissionGranted();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted();
            } else {
                Toast.makeText(this, getString(R.string.need_read_permission_message), Toast.LENGTH_LONG).show();
            }
        }
    }

    class VideoFileListAdapter extends RecyclerView.Adapter<VideoFileListItemVideHolder> {

        @Override
        public VideoFileListItemVideHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_video_list_item, parent, false);
            VideoFileListItemVideHolder holder = new VideoFileListItemVideHolder(v);
            RxView.clicks(v).throttleLast(300, TimeUnit.MILLISECONDS).subscribe(vv -> onClickItem(v));
            return holder;
        }

        @Override
        public void onBindViewHolder(VideoFileListItemVideHolder holder, int position) {
            VideoFile file = viewModel.getVideos().get(position);
            holder.bind(file.getFilepath());
        }

        @Override
        public int getItemCount() {
            return viewModel.getVideos().size();
        }

        private void onClickItem(View view) {
            int position = videoFileList.getChildLayoutPosition(view);
            VideoFile video = viewModel.getVideos().get(position);
            String filepath = video.getFilepath();

            Intent playerIntent = new Intent(VideoListActivity.this, VideoPlayerActivity.class);
            playerIntent.putExtra("filepath", filepath);
            startActivity(playerIntent);
        }
    }

    class VideoFileListItemVideHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.thumbnailImage)
        ImageView thumbnailImage;
        @BindView(R.id.filename)
        TextView filename;
        @BindView(R.id.file_info)
        TextView fileInfo;

        public VideoFileListItemVideHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(String filepath) {
            Glide.with(VideoListActivity.this).load(filepath).centerCrop().into(thumbnailImage);
            filename.setText(viewModel.getFilename(filepath));
            fileInfo.setText(viewModel.getVideoInfoText(VideoListActivity.this, filepath));
        }
    }
}
