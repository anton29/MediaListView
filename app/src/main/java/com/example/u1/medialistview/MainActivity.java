package com.example.u1.medialistview;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    final int THUMBSIZE = 128;
    ListView list;
    ArrayList<String> itemname ;

//    ArrayList<String> imgid = GetFiles(Environment.getExternalStorageDirectory() + "/DCIM/Camera");
    private int progressStatus = 0;
    private  ArrayList<Bitmap> thumbImageArray =null;


    public ArrayList<String> getItemname() {
        return itemname;
    }

    public Activity getActivity(){
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPerssion();
    }



    private class MyTask extends AsyncTask<ArrayList<String> ,Integer, ArrayList<Bitmap> > {
        ProgressDialog barProgressDialog;
        private final Activity parent;



        public MyTask(final Activity parent) {
            this.parent = parent;

        }

        @Override
        protected void onPreExecute() {
            barProgressDialog = new ProgressDialog(parent);
            barProgressDialog.setTitle("Loading Media ...");
            barProgressDialog.setMessage(" In progress ...");
            barProgressDialog.setProgressStyle(barProgressDialog.STYLE_HORIZONTAL);
            barProgressDialog.setProgress(0);
            barProgressDialog.setMax(itemname.size());
            barProgressDialog.show();
            barProgressDialog.setCancelable(false);
        }

        @Override
        protected ArrayList<Bitmap> doInBackground(ArrayList<String>... arrayLists) {

            ArrayList<Bitmap> thumbImageArray = new ArrayList<Bitmap>();
            for(int i = 0;i < itemname.size();i++){
                publishProgress();
//                Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(
//                        BitmapFactory.decodeFile( Environment.getExternalStorageDirectory()+ "/DCIM/Camera/" + itemname.get(i) ),
//                        THUMBSIZE,
//                        THUMBSIZE);
                // MINI_KIND: 512 x 384 thumbnail
                // MICRO_KIND: 96 x 96 thumbnail
                Bitmap ThumbImage = ThumbnailUtils.createVideoThumbnail(Environment.getExternalStorageDirectory()+ "/DCIM/Camera/" + itemname.get(i), MediaStore.Video.Thumbnails.MINI_KIND);
                thumbImageArray.add(ThumbImage);
            }
//            Log.v("array", String.valueOf(thumbImageArray));
            return thumbImageArray;
        }

        @Override
        protected void onProgressUpdate(final Integer... values) {
            barProgressDialog.incrementProgressBy(1);


        }

        @Override
        protected void onPostExecute(ArrayList<Bitmap> bitmapArray){
//            setThumbImageArray(bitmapArray);

            barProgressDialog.dismiss();

//            Log.v("array task", String.valueOf(bitmapArray.size()));

            CustomListAdapter adapter=new CustomListAdapter(getActivity(), getItemname(), bitmapArray);
            list=(ListView)findViewById(R.id.list);
            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // TODO Auto-generated method stub
                    String Slecteditem= itemname.get(+position);
                    Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();

                    Intent videoView = new Intent(MainActivity.this, VideoActivity.class);
                    videoView.putExtra("key", itemname.get(+position));
                    MainActivity.this.startActivity(videoView);

                }
            });
            super.onPostExecute(bitmapArray);

        }


    }

    public ArrayList<String> GetFiles(String DirectoryPath) {
        ArrayList<String> MyFiles = new ArrayList<String>();
        File f = new File(DirectoryPath);

        f.mkdirs();
        File[] files = f.listFiles();
        if (files.length == 0)
            return null;
        else {
            for (int i=0; i<files.length; i++){
                String mimeType = URLConnection.guessContentTypeFromName(files[i].getAbsolutePath());
                if(mimeType != null && mimeType.indexOf("video") == 0){
                    MyFiles.add(files[i].getName());
                }

            }

        }

        return MyFiles;
    }



    public void requestPerssion () {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("external storage");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_WIFI_STATE))
            permissionsNeeded.add("wifi");
        if (!addPermission(permissionsList, Manifest.permission.INTERNET))
            permissionsNeeded.add("internet");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Build.VERSION.SDK_INT >= 23) {
                                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                }
                            }
                        });
                return;
            }
            if (Build.VERSION.SDK_INT >= 23) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
            return;
        }
        itemname = GetFiles(Environment.getExternalStorageDirectory() + "/DCIM/Camera");
        new MyTask(this).execute(itemname);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
            {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_WIFI_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.INTERNET, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                    itemname = GetFiles(Environment.getExternalStorageDirectory() + "/DCIM/Camera");
                    new MyTask(this).execute(itemname);
//
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }



    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        }
        return true;
    }
}
