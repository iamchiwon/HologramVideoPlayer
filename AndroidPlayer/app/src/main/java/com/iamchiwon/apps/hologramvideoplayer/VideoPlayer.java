package com.iamchiwon.apps.hologramvideoplayer;

import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.TextureView;
import android.view.WindowManager;
import android.widget.VideoView;

public class VideoPlayer extends AppCompatActivity {

    FlippableVideoView player1;
    FlippableVideoView player2;
    FlippableVideoView player3;
    FlippableVideoView player4;

    final String SampleVideoURL = "http://www.androidbegin.com/tutorial/AndroidCommercial.3gp";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_video_player);

        player1 = (FlippableVideoView) findViewById(R.id.player1);
        player2 = (FlippableVideoView) findViewById(R.id.player2);
        player3 = (FlippableVideoView) findViewById(R.id.player3);
        player4 = (FlippableVideoView) findViewById(R.id.player4);

        player1.flipV();
        player2.rotateRight();
        player3.flipH();
        player3.rotateLeft();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        Uri video = Uri.parse(SampleVideoURL);
        player1.startVideo(this, video);
        player2.startVideo(this, video);
        player3.startVideo(this, video);
        player4.startVideo(this, video);
    }
}
