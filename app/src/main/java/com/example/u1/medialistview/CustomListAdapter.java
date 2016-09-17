package com.example.u1.medialistview;

/**
 * Created by U1 on 9/15/2016.
 */
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<String> {
    final int THUMBSIZE = 128;
    private final Activity context;
    private final ArrayList<String> itemname;
    private final ArrayList<Bitmap> imgid;


    public CustomListAdapter(Activity context, ArrayList<String> itemname, ArrayList<Bitmap> imgid) {
        super(context, R.layout.list_item, itemname);
        // TODO Auto-generated constructor stub
        Log.v("called","called");
        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;

    }



//    public ArrayList<Bitmap> bitmaps(ArrayList<String> itemname){
//        Log.v("size" , String.valueOf(itemname.size()));
//        ArrayList<Bitmap> thumbImageArray = new ArrayList<Bitmap>();
//        for(int i = 0;i < itemname.size();i++){
//            Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(
//                    BitmapFactory.decodeFile( Environment.getExternalStorageDirectory()+ "/DCIM/Camera/" + itemname.get(i) ),
//                    THUMBSIZE,
//                    THUMBSIZE);
//            thumbImageArray.add(ThumbImage);
//        }
//        Log.v("array", String.valueOf(thumbImageArray));
//        return thumbImageArray;
//    }

    @Override
    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_item, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);

        txtTitle.setText(itemname.get(position));
//        File file = new File(Environment.getExternalStorageDirectory()+ "/DCIM/Camera/" + itemname.get(position));

//        Log.v("calling", "Create new thubImage");
//        Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(
//                BitmapFactory.decodeFile( Environment.getExternalStorageDirectory()+ "/DCIM/Camera/" + itemname.get(position) ),
//                THUMBSIZE,
//                THUMBSIZE);
//
//        imageView.setImageBitmap(ThumbImage);
        imageView.setImageBitmap(imgid.get(position));
        extratxt.setText("Description "+ itemname.get(position));
        return rowView;

    };
}
