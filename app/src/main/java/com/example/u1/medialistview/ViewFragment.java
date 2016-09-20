package com.example.u1.medialistview;

import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

/**
 * Created by U1 on 9/16/2016.
 */
public class ViewFragment extends Fragment {
    String videoPath;
    VideoView viewView;
    String path;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_fragment,
                container, false);


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewView = (VideoView) getView().findViewById(R.id.videoView);
        startVideo();
    }

    public void startVideo() {

        Bundle bundle = this.getArguments();

        if (bundle != null ) {
            videoPath = (String) bundle.get("videoPath");
            Log.v("fragment", videoPath);
//            startVideo(videoPath);
            path =Environment.getExternalStorageDirectory() + "/DCIM/Camera/" + videoPath;
            final VideoView video = (VideoView) getView().findViewById(R.id.videoView);

//            auto chage video layout??
//            LinearLayout videoViewLayout = (LinearLayout) getView().findViewById(R.id.videoViewLayout);
//            if (getResources().getConfiguration().orientation == 1)
//            {
//                videoViewLayout.setOrientation(LinearLayout.HORIZONTAL);
//                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                Log.v("org", String.valueOf(1));
//
//            }
//
//            else if(getResources().getConfiguration().orientation == 2) {
//                videoViewLayout.setOrientation(LinearLayout.HORIZONTAL);
//                Log.v("org", String.valueOf(2));
//            }

            MediaController controller = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                controller = new MediaController(getContext());
            }
            controller.setAnchorView(video);
            controller.setMediaPlayer(video);
            video.setMediaController(controller);


            video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    video.requestFocus();
                    video.start();
                }
            });
            video.setVideoURI(Uri.parse(path));
        }

    }
}
