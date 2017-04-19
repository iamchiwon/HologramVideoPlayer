package com.iamchiwon.apps.hologramvideoplayer.activity.videoplayer;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.iamchiwon.apps.hologramvideoplayer.R;
import com.iamchiwon.apps.hologramvideoplayer.activity.view.FlippableVideoView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoPlayerActivity extends AppCompatActivity {

    @BindView(R.id.button_box)
    LinearLayout buttonBox;
    @BindView(R.id.button_pause)
    Button buttonPause;
    @BindView(R.id.player1)
    FlippableVideoView player1;
    @BindView(R.id.player2)
    FlippableVideoView player2;
    @BindView(R.id.player3)
    FlippableVideoView player3;
    @BindView(R.id.player4)
    FlippableVideoView player4;
    @BindView(R.id.background)
    RelativeLayout background;

    String filepath;
    boolean isFlip = true;
    boolean isMute = false;
    boolean isPlaying = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_video_player);
        ButterKnife.bind(this);

        filepath = getIntent().getStringExtra("filepath");

        flipToggle();
        player2.sound(false);
        player3.sound(false);
        player4.sound(false);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        buttonBox.setVisibility(View.INVISIBLE);

        Uri videoUri = Uri.fromFile(new File(filepath));

        player1.startVideo(this, videoUri);
        player2.startVideo(this, videoUri);
        player3.startVideo(this, videoUri);
        player4.startVideo(this, videoUri);
    }

    @Override
    protected void onPause() {
        player1.close();
        player2.close();
        player3.close();
        player4.close();
        super.onPause();
    }

    private void flipToggle() {
        isFlip = !isFlip;

        player1.reset();
        player2.reset();
        player3.reset();
        player4.reset();

        if (isFlip) {
            player1.flipV();
            player2.rotateRight();
            player3.flipH();
            player3.rotateLeft();
        } else {
            player1.flipH();
            player2.rotateLeft();
            player3.flipH();
            player3.rotateRight();
            player4.flipH();
            player4.flipV();
        }
    }

    @OnClick(R.id.background)
    public void showingMenu() {
        boolean isVisible = buttonBox.getVisibility() == View.VISIBLE;
        buttonBox.setVisibility(isVisible ? View.INVISIBLE : View.VISIBLE);
    }

    @OnClick(R.id.button_close)
    public void onClose() {
        finish();
    }

    @OnClick(R.id.button_flip)
    public void onFlip() {
        flipToggle();
    }

    @OnClick(R.id.button_mute)
    public void onMute() {
        isMute = !isMute;
        player1.sound(isMute);
    }

    @OnClick(R.id.button_pause)
    public void onTogglePlay() {
        isPlaying = !isPlaying;

        String title = isPlaying ? "Pause" : "Play";
        buttonPause.setText(title);

        if (isPlaying) {
            player1.resume();
            player2.resume();
            player3.resume();
            player4.resume();
        } else {
            player1.pause();
            player2.pause();
            player3.pause();
            player4.pause();
        }
    }
}
