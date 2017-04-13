package com.iamchiwon.apps.hologramvideoplayer.activity.videolist;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.iamchiwon.apps.hologramvideoplayer.R;
import com.iamchiwon.apps.hologramvideoplayer.viewmodel.VideoListViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by iamchiwon on 2017. 4. 12..
 */

public class VideoListActivity extends AppCompatActivity {

    final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 9999;

    @BindView(R.id.videoFileList)
    ListView videoFileList;

    VideoListViewModel viewModel = new VideoListViewModel();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        ButterKnife.bind(this);

        VideoListAdapter adapter = new VideoListAdapter(this);
        videoFileList.setAdapter(adapter);

        checkReadStoragePermission();
    }

    private void onPermissionGranted() {
        viewModel.fetchVideoFiles(this);
        viewModel.rxVideos().subscribe(videos -> {
            ((VideoListAdapter)videoFileList.getAdapter()).updateList(videos);
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
                Toast.makeText(this, "Read-Storage Permission Needs", Toast.LENGTH_LONG).show();
            }
        }
    }
}
