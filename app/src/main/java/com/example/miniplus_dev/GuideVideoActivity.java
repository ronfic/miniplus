package com.example.miniplus_dev;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;


public class GuideVideoActivity extends Activity {

    private String TAG = GuideVideoActivity.class.getSimpleName();

    int sel = 0;

    Button btn_guideCancel;
    ImageButton[] btn_guideSel = new ImageButton[13];


    VideoView videoView;
    MediaController mediaController;

    private void initView() {
        btn_guideCancel = (Button) findViewById(R.id.btn_guideCancel);

        videoView = (VideoView) findViewById(R.id.videoview);

        btn_guideSel[0] = (ImageButton) findViewById(R.id.btn_guide0);
        btn_guideSel[1] = (ImageButton) findViewById(R.id.btn_guide1);
        btn_guideSel[2] = (ImageButton) findViewById(R.id.btn_guide2);
        btn_guideSel[3] = (ImageButton) findViewById(R.id.btn_guide3);
        btn_guideSel[4] = (ImageButton) findViewById(R.id.btn_guide4);
        btn_guideSel[5] = (ImageButton) findViewById(R.id.btn_guide5);
        btn_guideSel[6] = (ImageButton) findViewById(R.id.btn_guide6);
        btn_guideSel[7] = (ImageButton) findViewById(R.id.btn_guide7);
        btn_guideSel[8] = (ImageButton) findViewById(R.id.btn_guide8);
        btn_guideSel[9] = (ImageButton) findViewById(R.id.btn_guide9);
        btn_guideSel[10] = (ImageButton) findViewById(R.id.btn_guide10);
        btn_guideSel[11] = (ImageButton) findViewById(R.id.btn_guide11);
        btn_guideSel[12] = (ImageButton) findViewById(R.id.btn_guide12);


    }

    void selVideo(int sel){

        if(videoView.isPlaying()){
            videoView.pause();
        }

        if(sel == 0)    videoView.setVideoPath("/sdcard/Movies/guide1.mp4");
        else if(sel == 1)    videoView.setVideoPath("/sdcard/Movies/guide2.mp4");
        else if(sel == 2)    videoView.setVideoPath("/sdcard/Movies/guide3.mp4");
        else if(sel == 3)    videoView.setVideoPath("/sdcard/Movies/guide4.mp4");
        else if(sel == 4)    videoView.setVideoPath("/sdcard/Movies/guide5.mp4");
        else if(sel == 5)    videoView.setVideoPath("/sdcard/Movies/guide6.mp4");
        else if(sel == 6)    videoView.setVideoPath("/sdcard/Movies/guide7.mp4");
        else if(sel == 7)    videoView.setVideoPath("/sdcard/Movies/guide8.mp4");
        else if(sel == 8)    videoView.setVideoPath("/sdcard/Movies/guide9.mp4");
        else if(sel == 9)    videoView.setVideoPath("/sdcard/Movies/guide10.mp4");
        else if(sel == 10)    videoView.setVideoPath("/sdcard/Movies/guide11.mp4");
        else if(sel == 11)    videoView.setVideoPath("/sdcard/Movies/guide12.mp4");
        else if(sel == 12)    videoView.setVideoPath("/sdcard/Movies/guide13.mp4");

        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        mediaController.setPadding(50, 0, 50, 150); //상위 레이어의 바닥에서 얼마 만큼? 패딩을 줌
        //videoView.setMediaController(mediaController);
        hideSystemUI();
        videoView.start();
    }


    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

    }


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_video);
        initView();

        selVideo(0);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.seekTo(0);
                videoView.start();
            }
        });

        btn_guideCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(GuideVideoActivity.this, ExerciseActivity.class);

                startActivity(intent);
                finishAffinity();
            }
        });

        btn_guideSel[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selVideo(0);
            }
        });
        btn_guideSel[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selVideo(1);
            }
        });
        btn_guideSel[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selVideo(2);
            }
        });
        btn_guideSel[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selVideo(3);
            }
        });
        btn_guideSel[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selVideo(4);
            }
        });
        btn_guideSel[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selVideo(5);
            }
        });
        btn_guideSel[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selVideo(6);
            }
        });
        btn_guideSel[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selVideo(7);
            }
        });
        btn_guideSel[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selVideo(8);
            }
        });
        btn_guideSel[9].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selVideo(9);
            }
        });
        btn_guideSel[10].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selVideo(10);
            }
        });
        btn_guideSel[11].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selVideo(11);
            }
        });
        btn_guideSel[12].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selVideo(12);
            }
        });





    }


    @Override
    public void onResume() {

        super.onResume();
        hideSystemUI();
    }


    @Override
    public void onPause() {
        super.onPause();

    }


}
