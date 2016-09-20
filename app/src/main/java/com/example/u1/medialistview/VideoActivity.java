package com.example.u1.medialistview;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class VideoActivity extends AppCompatActivity {

//    Activity activity;
//
//    public Activity getVideoActivity() {
//        return activity;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Intent intent = getIntent();
        String value = intent.getStringExtra("key");
        Log.v("passed",value);


        Bundle bundle = new Bundle();
        bundle.putString("videoPath", value);
        ViewFragment fr = new ViewFragment();
        fr.setArguments(bundle);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fooFragment,fr);
        fragmentTransaction.commit();
    }
}
