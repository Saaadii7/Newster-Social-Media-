package com.example.projectfinal;
/**
 * Created by Muzammil Sharif on 3/29/2015.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Muzammil Sharif on 3/15/2015.
 */
public class CustomAdapter extends ArrayAdapter<mydata> {
    Bitmap bitmap;
    Context c;
    int layoutFile;
    ProgressDialog pDialog;
    //mydata[] data;
    ArrayList<mydata> data;
    public CustomAdapter(Context context, int resource, ArrayList<mydata> objects) {

        super(context, resource, objects);
        c = context;
        layoutFile = resource;
        data = objects;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v, row;
        if(convertView == null)
        {
            LayoutInflater inflater = ((Activity)c).getLayoutInflater();
            row = inflater.inflate(R.layout.list, parent, false);
        }
        else
        {
            row = (View) convertView;
        }
        TextView txt = (TextView)row.findViewById(R.id.textView1);     
        txt.setText(data.get(position).Title);
        
        TextView txt1 = (TextView)row.findViewById(R.id.textView2);     
        txt1.setText(data.get(position).Description);
        
        TextView txt3 = (TextView)row.findViewById(R.id.textView4);     
        txt3.setText(data.get(position).Link);
        
        TextView txt4 = (TextView)row.findViewById(R.id.textView3);     
        txt4.setText(data.get(position).Date);
        
        
        ImageView img = (ImageView)row.findViewById(R.id.imageView1);
     if(data.get(position).imgbitmap==null) {
         try {
             bitmap = new LoadImage().execute(data.get(position).Image).get();
         } catch (InterruptedException e) {
             e.printStackTrace();
         } catch (ExecutionException e) {
             e.printStackTrace();
         }
         img.setImageBitmap(bitmap);
     }
     else {
         Bitmap b = BitmapFactory.decodeByteArray(data.get(position).imgbitmap, 0, data.get(position).imgbitmap.length);
         img.setImageBitmap(b);
     }



        return row;
    }
        private class LoadImage extends AsyncTask<String, String, Bitmap> {
            @Override
            protected void onPreExecute() {
            }

            protected Bitmap doInBackground(String... args) {
                try {
                    bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return bitmap;
            }

            protected void onPostExecute(Bitmap image) {
                //if (image != null) {
                    //img.setImageBitmap(image);

                }
            }
        }

//}