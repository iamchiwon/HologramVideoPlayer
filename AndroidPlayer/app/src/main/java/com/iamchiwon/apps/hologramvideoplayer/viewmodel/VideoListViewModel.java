package com.iamchiwon.apps.hologramvideoplayer.viewmodel;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.iamchiwon.apps.hologramvideoplayer.model.VideoFile;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by iamchiwon on 2017. 4. 14..
 */

public class VideoListViewModel {

    ArrayList<VideoFile> videos = new ArrayList<>();

    PublishSubject<ArrayList<VideoFile>> videosSubject = PublishSubject.create();

    public Observable<ArrayList<VideoFile>> rxVideos() {
        return videosSubject;
    }

    public void fetchVideoFiles(Context context) {
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Video.VideoColumns.DATA};
        Cursor c = context.getContentResolver().query(uri, projection, null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                VideoFile item = new VideoFile();
                item.setFilepath(c.getString(0));
                videos.add(item);
            }
            c.close();
        }
    }
}
