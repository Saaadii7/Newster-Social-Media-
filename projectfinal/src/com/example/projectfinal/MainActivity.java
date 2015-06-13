package com.example.projectfinal;



import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.splunk.mint.Mint;



import android.R.string;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private EditText e1;
	private EditText e2;
	private TextView t1;
	private static final String TAG_SUCCESS = "success";
	private final Context c = this;
	public static GoogleAnalytics analytics;
	public static Tracker tracker;
	
	 private CallbackManager callbackManager;
	    private LoginButton fbLoginButton;
	    ShareDialog shareDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Mint.initAndStartSession(MainActivity.this,"a5ae6a8c");
		
		 FacebookSdk.sdkInitialize(getApplicationContext());
	        callbackManager = CallbackManager.Factory.create();
	        

	        getFbKeyHash("org.code2care.fbloginwithandroidsdk");

	
		setContentView(R.layout.activity_main);
		
		
		Button crasher = (Button) findViewById(R.id.crash);
        crasher.setBackgroundColor(Color.RED);
        crasher.setTextColor(Color.WHITE);
		
		
		
		
		
		
	    analytics = GoogleAnalytics.getInstance(this);
	    analytics.setLocalDispatchPeriod(1800);

	    tracker = analytics.newTracker("UA-63163904-1"); // Replace with actual tracker/property Id
	    tracker.enableExceptionReporting(true);
	    tracker.enableAdvertisingIdCollection(true);
	    tracker.enableAutoActivityTracking(true);
		
		 fbLoginButton = (LoginButton)findViewById(R.id.fb_login_button);

	        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
	            @Override
	            public void onSuccess(LoginResult loginResult) {

	                System.out.println("Facebook Login Successful!");
	                System.out.println("Logged in user Details : ");
	                System.out.println("--------------------------");
	                System.out.println("User ID  : " + loginResult.getAccessToken().getUserId());
	                System.out.println("Authentication Token : " + loginResult.getAccessToken().getToken());
	                Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_LONG).show();
	            }

	            @Override
	            public void onCancel() {
	                Toast.makeText(MainActivity.this, "Login cancelled by user!", Toast.LENGTH_LONG).show();
	                System.out.println("Facebook Login failed!!");

	            }

	            @Override
	            public void onError(FacebookException e) {
	                Toast.makeText(MainActivity.this, "Login unsuccessful!", Toast.LENGTH_LONG).show();
	                System.out.println("Facebook Login failed!!");
	            }
	        });
	        AdView adView = (AdView) findViewById(R.id.adView);

			AdRequest adRequest = new AdRequest.Builder()
			/* .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
			 .addTestDevice("EC85BA2772B7BD0B2DC8C5EE59FA178C") */
			.build();
			adView.loadAd(adRequest);
			
			tracker.setScreenName("main login");

			tracker.send(new HitBuilders.EventBuilder()
			       .setCategory("UX")
			       .setAction("click")
			       .setLabel("submit")
			       .build());

		//You need this method to be used only once to configure
		//your key hash in your App Console at
		// developers.facebook.com/apps

		

		e1=(EditText)findViewById(R.id.editText1);
		e2=(EditText)findViewById(R.id.editText2);

		ImageView img=(ImageView)findViewById(R.id.imageView1);
		Drawable myDrawable = getResources().getDrawable(R.drawable.logo);
		img.setImageDrawable(myDrawable);
		//t1=(TextView)findViewById(R.id.txtView1);
		
		//deleteAlllogins();	
        //addLogin();
		
		

		Button t=(Button)findViewById(R.id.btn);
		t.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				String user = e1.getText().toString();
				String password = e2.getText().toString();
				String result = null;
				
				try {
					result = new loginuser().execute(user, password).get();
					
					if(result=="success"){
						
						//Toast.makeText(c,"login success",Toast.LENGTH_SHORT).show();
						Intent i = new Intent(getApplicationContext(), Main.class);
						startActivity(i);
					}
					else {
						Toast.makeText(c,"login fail",Toast.LENGTH_SHORT).show();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});


		Button t1=(Button)findViewById(R.id.button1);
		t1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(), signup.class);
				startActivity(i);




			}
		});


		}
	

	 public void getFbKeyHash(String packageName) {

	        try {
	            PackageInfo info = getPackageManager().getPackageInfo(
	                    packageName,
	                    PackageManager.GET_SIGNATURES);
	            for (Signature signature : info.signatures) {
	                MessageDigest md = MessageDigest.getInstance("SHA");
	                md.update(signature.toByteArray());
	                Log.d("YourKeyHash :", Base64.encodeToString(md.digest(), Base64.DEFAULT));
	                System.out.println("YourKeyHash: "+ Base64.encodeToString(md.digest(), Base64.DEFAULT));
	            }
	        } catch (PackageManager.NameNotFoundException e) {

	        } catch (NoSuchAlgorithmException e) {

	        }

	    }

	    @Override
	    protected void onActivityResult(int reqCode, int resCode, Intent i) {
	        callbackManager.onActivityResult(reqCode, resCode, i);
	    }
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void deleteAlllogins () {
		// delete all the records and the table of the database provider
		String URL = "content://com.projectfinal.provider.login/login";
	        Uri friends = Uri.parse(URL);
		int count = getContentResolver().delete(
				 friends, null, null);
		String countNum = "Javacodegeeks: "+ count +" records are deleted.";
		Toast.makeText(getBaseContext(), 
			      countNum, Toast.LENGTH_LONG).show();
		
	}
	
	 public void addLogin() {
	      // Add a new birthday record
	      ContentValues values = new ContentValues();

	      values.put(contentprovi.NAME,e1.getText().toString());
	      

	      Uri uri = getContentResolver().insert(
	    	   contentprovi.CONTENT_URI, values);
	      
	   }

	private static class loginuser extends AsyncTask<String, String, String> {
		
		@Override
		protected String doInBackground(String... params) {
			String user = params[0], password = params[1];

			// Building Parameters
			List<NameValuePair> paramss = new ArrayList<NameValuePair>();
			paramss.add(new BasicNameValuePair("user", user));
			paramss.add(new BasicNameValuePair("pass", password));


			// getting JSON Object
			// Note that create product url accepts POST method

			json_parser j=new json_parser();

			JSONObject json = j.makeHttpRequest("http://projectsmdnu.host22.com/login.php","GET", paramss);


			// check log cat from response
			Log.d("Create Response", json.toString());
			// check for success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

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

		@Override
		protected void onPostExecute(String message) {
			// dismiss the dialog after getting all products

		}
	}

}
