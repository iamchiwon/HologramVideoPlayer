package com.iamchiwon.apps.hologramvideoplayer;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;

/**
 * Created by iamchiwon on 2017. 4. 12..
 */

public class FlippableVideoView extends TextureView implements TextureView.SurfaceTextureListener {

    private MediaPlayer mediaPlayer;

    public FlippableVideoView(Context context) {
        super(context);
        init();
    }

    public FlippableVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FlippableVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public FlippableVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mediaPlayer = new MediaPlayer();
        setSurfaceTextureListener(this);
    }

    public void startVideo(Context context, Uri videoUri) {
        try {
            mediaPlayer.setDataSource(context, videoUri);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rotateRight() {
        this.setRotation(90.0f);
    }

    public void rotateLeft() {
        this.setRotation(-90.0f);
    }

    public void flipH() {
        this.setScaleX(-1 * this.getScaleX());
    }

    public void flipV() {
        this.setScaleY(-1 * this.getScaleY());
    }


    /****
     * Implements of SurfaceTextureListener
     */

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        mediaPlayer.setSurface(new Surface(surfaceTexture));
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }
}
