package com.example.u1.medialistview;

/**
 * Created by U1 on 9/15/2016.
 */
import android.app.Activity;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> itemname;
    private final ArrayList<Bitmap> imgid;
    private final ArrayList<String> knownLocationArray;
//    Geocoder geocoder;


    public CustomListAdapter(Activity context, ArrayList<String> itemname, ArrayList<Bitmap> imgid, ArrayList<String> knownLocationArray) {
        super(context, R.layout.list_item, itemname);
        // TODO Auto-generated constructor stub
        Log.v("called","called");
        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
        this.knownLocationArray =knownLocationArray;
//         geocoder = new Geocoder(context, Locale.getDefault());

    }


    @Override
    public View getView(int position,View view,ViewGroup parent) {
//        List<Address> addresses;
//        try {
//            addresses = geocoder.getFromLocation(28.3570, -081.5606, 1);
//            String address = addresses.get(0).getAddressLine(0);
//            Log.v("l",address);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        retriever.setDataSource(Environment.getExternalStorageDirectory() +"/DCIM/Camera/"+itemname.get(position));
//       String test = retriever.extractMetadata(retriever.METADATA_KEY_LOCATION);
//        Log.v("test",""+test);
//        retriever.release();

//        try {
//            exifInterface = new ExifInterface(Environment.getExternalStorageDirectory() +"/DCIM/Camera/"+itemname.get(position) );
//            if(exifInterface != null){
//                Log.v("test","time"+ exifInterface.getAttribute(ExifInterface.TAG_DATETIME));
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_item, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);

        txtTitle.setText(itemname.get(position));

        imageView.setImageBitmap(imgid.get(position));
        if(knownLocationArray.get(position) != null){
            extratxt.setText("Location: "+"\n"+ knownLocationArray.get(position));
        }else{
            extratxt.setText("");
        }

        return rowView;

    };
}
