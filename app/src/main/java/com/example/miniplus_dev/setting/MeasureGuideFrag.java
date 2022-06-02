package com.example.miniplus_dev.setting;


import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListPopupWindow;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.miniplus_dev.MeasureTestActivity;
import com.example.miniplus_dev.MeasureTestActivity2;
import com.example.miniplus_dev.PublicFunctions;
import com.example.miniplus_dev.R;

import java.lang.reflect.Field;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeasureGuideFrag extends Fragment {

    private final String TAG = MeasureGuideFrag.class.getSimpleName();

    public boolean videoFlag;

    PublicFunctions functionBundle = new PublicFunctions();

    View view;

    boolean startFlag;
    int stepNum, selNum;

    VideoView videoView;
    MediaController mediaController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i(TAG, "onCreate!!! Video Frag!!!");
        view = inflater.inflate(R.layout.fragment_measure_guide, container, false);


        getActivityData();

        videoView = (VideoView) view.findViewById(R.id.measure_videoview);

        Log.e(TAG, "selnum + " + selNum + "stepnum + " + stepNum);

        if (selNum == 0) videoView.setVideoPath("/sdcard/Download/push.mp4");
        else if (selNum == 1) videoView.setVideoPath("/sdcard/Download/pull.mp4");
        else if (selNum == 2) videoView.setVideoPath("/sdcard/Download/rotation.mp4");
        else if (selNum == 3) videoView.setVideoPath("/sdcard/Download/uppush.mp4");
        else if (selNum == 4) videoView.setVideoPath("/sdcard/Download/uppull.mp4");
        else if (selNum == 5) videoView.setVideoPath("/sdcard/Download/lowpush.mp4");
        else if (selNum == 6) videoView.setVideoPath("/sdcard/Download/lowpull.mp4");


        mediaController = new MediaController(getActivity());
        mediaController.setAnchorView(videoView);
        mediaController.setPadding(30, 0, 30, 30); //상위 레이어의 바닥에서 얼마 만큼? 패딩을 줌


        //videoView.setMediaController(mediaController);

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(getActivity(), "Can't play this video.", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.seekTo(0);
                videoView.start();
            }
        });
        if (!startFlag) videoStart();
        else videoStop();

        MeasureTestActivity.fragChangeFlag = MeasureTestActivity2.fragChangeFlag = false;
        return view;
    }

    public void videoStop() {
        videoFlag = false;
        videoView.pause();
        Log.i(TAG, "Video Frag Stop");
    }

    public void videoStart() {
        videoFlag = true;
        videoView.start();
        Log.i(TAG, "Video Frag Start");
    }


    private void getActivityData() {
        Bundle bundle = getArguments();
        startFlag = bundle.getBoolean("startFlag");
        selNum = bundle.getInt("selNum");
        stepNum = bundle.getInt("stepNum");

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause!!! Video Frag!!!");
        videoView.pause();
    }
}
