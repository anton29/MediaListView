package com.example.u1.medialistview;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends Activity {

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    ListView list;
    ArrayList<String> itemname ;
    Geocoder geocoder;
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
        geocoder = new Geocoder(this, Locale.getDefault());
        requestPermission();
    }



    private class MyTask extends AsyncTask<ArrayList<String> ,Integer, ArrayList<Bitmap> > {
        ProgressDialog progressBarDialog;
        private final Activity parent;
        ArrayList<String> knownLocationArray;



        public MyTask(final Activity parent) {
            this.parent = parent;
        }

        private boolean isNetworkAvailable() {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }

        @Override
        protected void onPreExecute() {
            progressBarDialog = new ProgressDialog(parent);
            progressBarDialog.setTitle("Loading Media ...");
            progressBarDialog.setMessage(" In progress ...");
            progressBarDialog.setProgressStyle(progressBarDialog.STYLE_HORIZONTAL);
            progressBarDialog.setProgress(0);
            progressBarDialog.setMax(itemname.size());
            progressBarDialog.show();
            progressBarDialog.setCancelable(false);
        }

        @Override
        protected ArrayList<Bitmap> doInBackground(ArrayList<String>... arrayLists) {

            ArrayList<Bitmap> thumbImageArray = new ArrayList<Bitmap>();
            knownLocationArray = new ArrayList<String>();
            for(int i = 0;i < itemname.size();i++){
                publishProgress();
                List<Address> addresses;
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(Environment.getExternalStorageDirectory() +"/DCIM/Camera/"+itemname.get(i));
                String location = retriever.extractMetadata(retriever.METADATA_KEY_LOCATION);
//                Log.v("test",""+location);
                Log.v("testing","net");
                Log.v("testing", String.valueOf(""+isNetworkAvailable()));
                if(location != null && isNetworkAvailable() != false){
                    try {
                        String delimiter = "(?=\\b[+-])";
                        String[] splitted = location.split(delimiter);
                        addresses = geocoder.getFromLocation(Double.parseDouble(splitted[0]), Double.parseDouble(splitted[1]), 1);
                        String address = addresses.get(0).getAddressLine(0);

                        knownLocationArray.add(address);
                    } catch (IOException e) {
                        knownLocationArray.add(null);
                        e.printStackTrace();
                    }
                }else{
                    knownLocationArray.add(null);
                }

//                MICRO_KIND type will generate thumbnail of size 96 x 96.
//                MINI_KIND type will generate thumbnail of size 512 x 384.
//                set to 100dp 100dp in xml , micro too small mini un-uniformed sizes
                Bitmap ThumbImage = ThumbnailUtils.createVideoThumbnail(Environment.getExternalStorageDirectory()+ "/DCIM/Camera/" + itemname.get(i), MediaStore.Video.Thumbnails.MICRO_KIND);
                thumbImageArray.add(ThumbImage);
                retriever.release();
            }
            return thumbImageArray;
        }

        @Override
        protected void onProgressUpdate(final Integer... values) {
            progressBarDialog.incrementProgressBy(1);


        }

        @Override
        protected void onPostExecute(ArrayList<Bitmap> bitmapArray){

            progressBarDialog.dismiss();
            CustomListAdapter adapter=new CustomListAdapter(getActivity(), getItemname(), bitmapArray, knownLocationArray);
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



    public void requestPermission() {
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
                perms.put(Manifest.permission.ACCESS_NETWORK_STATE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED) {
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

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("status", "resume");
    }



    @Override
    protected void onPause() {
        super.onPause();
        Log.v("status","pause");
    }

}
