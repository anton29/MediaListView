package com.example.u1.medialistview;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by U1 on 9/16/2016.
 */
public class ViewFragment extends Fragment {
    public static final String EXTRA_URL ="url";
    String videoPath;
    VideoView viewView;
    String path;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        videoPath = getArguments().getString("videoPath");
        View view = inflater.inflate(R.layout.video_fragment,
                container, false);
//        Bundle bundle = this.getArguments();
//        if (bundle != null) {
//            videoPath = (String) bundle.get("videoPath");
//            Log.v("fragment", videoPath);
//        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.v("onActivityCreated","onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = this.getArguments();
        viewView = (VideoView) getView().findViewById(R.id.videoView);
        startVideo(videoPath);
//        if (bundle != null) {
//            videoPath = (String) bundle.get("videoPath");
//            Log.v("fragment", videoPath);
//            startVideo(videoPath);
//        }
//        startVideo(videoPath);
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            String link = bundle.getString("url");
//            setText(link);
//        }
    }

    public void startVideo(String url) {

       path ="/storage/emulated/0/DCIM/Camera/20160905_133537.mp4";
        Bundle bundle = this.getArguments();

        if (bundle != null ) {
            videoPath = (String) bundle.get("videoPath");
            Log.v("fragment", videoPath);
//            startVideo(videoPath);
            path ="/storage/emulated/0/DCIM/Camera/" + videoPath;
            final VideoView video = (VideoView) getView().findViewById(R.id.videoView);

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
//        /storage/emulated/0/DCIM/Camera/20160905_133537.mp4
//       viewView.setVideoURI(Uri.parse(path));
//        viewView.start();
    }
}
