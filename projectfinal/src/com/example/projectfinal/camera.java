package com.example.projectfinal;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;



import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.os.Build;
import android.provider.MediaStore;

public class camera extends Activity{

Bitmap a;
Spinner spinnerOsversions;
news n;
int serverResponseCode = 0;

private String[] state = { "Select Category","Entertainment", "Sports", "Health", "Science",
  "Literature" };

	private static int REQUEST_CAMERA = 1;
	private static int SELECT_FILE= 2;
	ProgressDialog pDialog;
	
    
    String imgDecodableString;
 
	
	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.camera1);
	        spinnerOsversions = (Spinner) findViewById(R.id.osversions);
	        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,
	          android.R.layout.simple_spinner_item, state);
	        adapter_state
	          .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        spinnerOsversions.setAdapter(adapter_state);
	        
	        
	        
	        
	 }
	 public void awla(View v) {
	        final CharSequence[] items = { "Take Photo", "Choose from Library",
	                "Cancel" };
	 
	        AlertDialog.Builder builder = new AlertDialog.Builder(camera.this);
	        builder.setTitle("Add Photo!");
	        builder.setItems(items, new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int item) {
	                if (items[item].equals("Take Photo")) {
	                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	                    startActivityForResult(intent, REQUEST_CAMERA);
	                } else if (items[item].equals("Choose from Library")) {
	                    Intent intent = new Intent(
	                            Intent.ACTION_PICK,
	                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	                    intent.setType("image/*");
	                    
	                    		 startActivityForResult(intent, SELECT_FILE);
	                } else if (items[item].equals("Cancel")) {
	                    dialog.dismiss();
	                }
	            }
	        });
	        builder.show();
	    }
	    @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    if (resultCode == RESULT_OK) {
	    if (requestCode == REQUEST_CAMERA) {
	    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
	            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
	     
	            File destination = new File(Environment.getExternalStorageDirectory(),
	                    System.currentTimeMillis() + ".jpg");
	     
	            FileOutputStream fo;
	            try {
	                destination.createNewFile();
	                fo = new FileOutputStream(destination);
	                fo.write(bytes.toByteArray());
	                fo.close();
	            } catch (FileNotFoundException e) {
	                e.printStackTrace();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            ImageView imageView = (ImageView) findViewById(R.id.ivImage);
	            imageView.setImageBitmap(thumbnail);
	            a=thumbnail;
	     
	    } 
	    
	    try {
	        // When an Image is picked
	        if (requestCode == SELECT_FILE && resultCode == RESULT_OK
	                && null != data) {
	            // Get the Image from data

	            Uri selectedImage = data.getData();
	            String[] filePathColumn = { MediaStore.Images.Media.DATA };

	            // Get the cursor
	            Cursor cursor = getContentResolver().query(selectedImage,
	                    filePathColumn, null, null, null);
	            // Move to first row
	            cursor.moveToFirst();

	            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	            imgDecodableString = cursor.getString(columnIndex);
	            cursor.close();
	            ImageView imgView = (ImageView) findViewById(R.id.ivImage);
	            // Set the Image in ImageView after decoding the String
	            a=BitmapFactory
	                    .decodeFile(imgDecodableString);
	            imgView.setImageBitmap(BitmapFactory
	                    .decodeFile(imgDecodableString));
	            

	        } else {
	            Toast.makeText(this, "You picked  a Image",
	                    Toast.LENGTH_LONG).show();
	        }
	    } catch (Exception e) {
	        Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
	                .show();
	    }

	    }
	    
	    }
	    public void data(View v)
	    {
	    	
	    	
	    	EditText e1=(EditText) findViewById(R.id.editText1);

	    	EditText e2=(EditText) findViewById(R.id.editText2);

	    	EditText e3=(EditText) findViewById(R.id.editText3);
	    	String d=e1.getText().toString();
	    	String b=e2.getText().toString();
	    	String c=e3.getText().toString();
	    	
	    	Bitmap s=a;
	    	 String state = (String) spinnerOsversions.getSelectedItem();
	    	n=new news(d,b,c,s,state);
	    	
	    	
	    	
	    	
	    	//Context context = getApplicationContext();
	    	//CharSequence text = "News Saved!";
	    	//int duration = Toast.LENGTH_SHORT;

	    	//Toast toast = Toast.makeText(context, text, duration);
	    	//toast.show();
	    	new postnews().execute();
	    	
	    	
	    	
	    	
	    	
	    }
	    
	    
	    private class postnews extends AsyncTask<String, String, String> {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(camera.this);
				pDialog.setMessage("Uploading. Please wait...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.show();
			}
			
			
			@Override
			protected String doInBackground(String... params) {
			

				// Building Parameters
				List<NameValuePair> paramss = new ArrayList<NameValuePair>();
				paramss.add(new BasicNameValuePair("title", n.gettitle()));
				paramss.add(new BasicNameValuePair("link", n.geturl()));
				paramss.add(new BasicNameValuePair("description", n.getdescription()));
				paramss.add(new BasicNameValuePair("category", n.getcategory()));
				
				
				if(n.img!=null){
				
                Bitmap bitmap;
                bitmap=n.getimg();
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream); 
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                String encodedString = Base64.encodeToString(byte_arr, 0);
                JSONObject imageur;
                ///
				try {
					imageur=getImgurContent("496dd6add5f1e5d",encodedString);
					
					try {
						String datafrom=imageur.getString("data");
						JSONObject tmp= new JSONObject(datafrom);
						
						String imagelink=tmp.getString("link");

						
						
						paramss.add(new BasicNameValuePair("image", imagelink));
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                ///
				
				
				
				
				}
				else paramss.add(new BasicNameValuePair("image", ""));
				
                
				
				
				
                int day, month, year;
                int second, minute, hour;
                GregorianCalendar date = new GregorianCalendar();

                day = date.get(Calendar.DAY_OF_MONTH);
                month = date.get(Calendar.MONTH);
                year = date.get(Calendar.YEAR);

                second = date.get(Calendar.SECOND);
                minute = date.get(Calendar.MINUTE);
                hour = date.get(Calendar.HOUR);

                String name=(hour+""+minute+""+second+""+day+""+(month+1)+""+year);
                String tag=name+".png";
                paramss.add(new BasicNameValuePair("filename",tag));
                String imgur="http://projectsmdnu.host22.com/images/"+tag;
                paramss.add(new BasicNameValuePair("imageurl",imgur));
                
                long curdate = System.currentTimeMillis(); 

                SimpleDateFormat sdf = new SimpleDateFormat("MMM MM dd, yyyy h:mm a");
                String dateString = sdf.format(curdate); 
                
                paramss.add(new BasicNameValuePair("date",dateString));
                
                
                
                
				

				// getting JSON Object
				// Note that create product url accepts POST method
				
				json_parser j=new json_parser();
				
				JSONObject json = j.makeHttpRequest("http://projectsmdnu.host22.com/post.php","GET", paramss);
				
							
				// check log cat fro response
				Log.d("Create Response", json.toString());
				// check for success tag
				try {
					int success = json.getInt("success");

					if (success == 1) {
						return("success");
						
					} else {
						return("failure");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				return null;
			}
			
			protected void onPostExecute(String message) {
				// dismiss the dialog after getting all products
				pDialog.dismiss();
				if(message=="success")Toast.makeText(getApplicationContext(),"News Successfully submitted..",Toast.LENGTH_SHORT).show();
				else Toast.makeText(getApplicationContext(),"Operation failed",Toast.LENGTH_SHORT).show();
				
			}
			
				
			
		}	 
	    
	    public static JSONObject getImgurContent(String clientID, String data) throws Exception {
	        URL url;
	        url = new URL("https://api.imgur.com/3/image");
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

	        //String data = URLEncoder.encode("image", "UTF-8") + "="
	                //+ URLEncoder.encode(IMAGE_URL, "UTF-8");

	        conn.setDoOutput(true);
	        conn.setDoInput(true);
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Authorization", "Client-ID " + clientID);
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Content-Type",
	                "application/x-www-form-urlencoded");

	        conn.connect();
	        StringBuilder stb = new StringBuilder();
	        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
	        wr.write(data);
	        wr.flush();

	        // Get the response
	        BufferedReader rd = new BufferedReader(
	                new InputStreamReader(conn.getInputStream()));
	        String line;
	        while ((line = rd.readLine()) != null) {
	            stb.append(line).append("\n");
	        }
	        wr.close();
	        rd.close();
	        


	        //return stb.toString();
	        
	        return new JSONObject(stb.toString());
	    }
	    
	    
	 
	
}
