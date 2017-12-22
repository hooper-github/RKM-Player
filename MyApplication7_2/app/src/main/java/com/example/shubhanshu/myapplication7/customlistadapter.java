/**
 * Created by shubhanshu on 2/9/16.
 */
package com.example.shubhanshu.myapplication7;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

public class customlistadapter extends ArrayAdapter<String> {

    private final String TAG = "customlistadapter";
    private Activity context;
    private ArrayList<String> itemname;
    private long[] imgid;
    private ArrayList<String> artist;
    private ArrayList<String> itemPath;

    //Caching
    MemoryCache memoryCache=new MemoryCache();
    private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());

    public customlistadapter(Activity context, ArrayList<String> itemname, ArrayList<String> artist, long[] imgid, ArrayList<String> itemPath) {
        super(context, R.layout.myplaylist, itemname);
        // TODO Auto-generated constructor stub

        this.artist=artist;
        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
        this.itemPath=itemPath;
    }


    public View getView(int position,View view,ViewGroup parent) {

        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);




        txtTitle.setText(itemname.get(position));
        if(extratxt!=null)
        extratxt.setText(artist.get(position));

        String imgID = String.valueOf(imgid[position]);

        //check if the thumbnail is in the cache
        Bitmap bitmap = memoryCache.get(imgID);
        if(bitmap!=null)
        {
            imageView.setImageBitmap(bitmap);
        }
        else
        {
           // Log.d(TAG, "Default Value set for" + imgID);
            imageView.setImageResource(R.drawable.musi);
            //set the thumbnail in a separate thread
            new BitmapWorkerTask(imgID, imageView).execute(itemPath.get(position));
        }

        return rowView;

    };

    public Bitmap getImage(String path)
    {

        try{
            android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(path);
            byte[] art;
            art = mmr.getEmbeddedPicture();
            Bitmap bmp = BitmapFactory.decodeByteArray(art, 0, art.length);
            art = null;
            mmr.release();
            return  Bitmap.createScaledBitmap(bmp, 60, 60, false);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Bitmap bmp = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.musi);
            return Bitmap.createScaledBitmap(bmp, 60, 60, false);
        }


    }

    class BitmapWorkerTask extends AsyncTask<String, String, Bitmap> {
        private final String thisimgid;
        private final ImageView thisimageview;
        // Decode image in background

        BitmapWorkerTask(String imgid, ImageView view)
        {
            thisimageview = view;
            thisimgid = imgid;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            if(!imageViewReused(thisimageview, thisimgid))
                return null;
            Bitmap bm = getImage(params[0]);
            memoryCache.put(thisimgid, bm);//add to the cache

            if(!imageViewReused(thisimageview, thisimgid))
                return null;

            return bm;
//        int bytes=bm.getByteCount();
//        Log.d("Bytes for " + itemname[position] + ": ", String.valueOf(bytes));
        }

        boolean imageViewReused(ImageView v, String url){
            String tag=imageViews.get(v);
            return (tag==null || !tag.equals(url));
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(bitmap != null && thisimageview != null){
                imageViews.put(thisimageview, thisimgid);
                thisimageview.setImageBitmap(bitmap);
            }

        }
    }

}
