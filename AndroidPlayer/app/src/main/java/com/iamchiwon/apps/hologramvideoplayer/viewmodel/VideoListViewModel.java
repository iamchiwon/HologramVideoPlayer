package com.iamchiwon.apps.hologramvideoplayer.viewmodel;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;

import com.iamchiwon.apps.hologramvideoplayer.model.VideoFile;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import lombok.Getter;

/**
 * Created by iamchiwon on 2017. 4. 14..
 */

public class VideoListViewModel {

    @Getter
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

    public String getFilename(String filepath) {
        String filename = filepath;
        int lastIndex = filename.lastIndexOf('/');
        if (lastIndex != -1) {
            filename = filename.substring(lastIndex + 1);
        }
        return filename;
    }

    public String getVideoInfoText(Context context, String filepath) {
        File f = new File(filepath);
        long size = f.getTotalSpace();
        long duration = videoDuration(context, filepath);

        String sizeText = readableFileSize(size);
        String sizeDuration = durationText(duration);

        return sizeDuration + "  (" + sizeText + ")";
    }

    private String readableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    private long videoDuration(Context context, String filepath) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(context, Uri.fromFile(new File(filepath)));
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInMillisec = Long.parseLong(time);
        return timeInMillisec;
    }

    private String durationText(long durationInMillisecond) {
        int duration = (int) (durationInMillisecond / 1000);
        int second = duration % 60;

        duration /= 60;
        int minute = duration % 60;
        int hour = duration / 60;

        return String.format("%02d:%02d:%02d", hour, minute, second);
    }
}
