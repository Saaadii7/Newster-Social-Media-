package com.example.projectfinal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Muzammil Sharif on 4/5/2015.
 */
public class description extends ActionBarActivity{
    ImageView img;
    Bitmap bitmap;
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.description);
    Intent i=this.getIntent();

    TextView a=(TextView)findViewById(R.id.textView2);
    a.setText(i.getStringExtra("Title"));

    TextView txt=(TextView)findViewById(R.id.textView3);
    txt.setText(i.getStringExtra("Description"));

    img = (ImageView)findViewById(R.id.imageView3);
  /*  try {
        bitmap=new LoadImage().execute(i.getStringExtra("Image")).get();
    } catch (InterruptedException e) {
        e.printStackTrace();
    } catch (ExecutionException e) {
        e.printStackTrace();
    }*/

    img.setImageBitmap(BitmapFactory.decodeByteArray(i.getByteArrayExtra("Image"), 0, i.getByteArrayExtra("Image").length));

    //i.g

    getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
    getSupportActionBar().setIcon(R.drawable.n);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);



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
            if (image != null) {
                img.setImageBitmap(image);

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               //NavUtils.navigateUpFromSameTask(this);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
