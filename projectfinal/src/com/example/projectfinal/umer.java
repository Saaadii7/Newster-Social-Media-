package com.example.projectfinal;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.BassBoost.Settings;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
public class umer extends Activity {
	
	 CallbackManager callbackManager;
	    ShareDialog shareDialog;
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	    	
	        super.onCreate(savedInstanceState);
	        
	        FacebookSdk.sdkInitialize(getApplicationContext());
	        callbackManager = CallbackManager.Factory.create();
		setContentView(R.layout.ac1);
	        shareDialog = new ShareDialog(this);
	        // this part is optional
	      //  shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {  });
	    
	    
}
	    public void share_button(View v)
	    {
	          	if (ShareDialog.canShow(ShareLinkContent.class)) {
			    		ShareLinkContent linkContent = new ShareLinkContent.Builder()
	    	            .setContentTitle("Hello Facebook")
	    	            .setContentDescription(
	    	                    "The 'Hello Facebook' sample  showcases simple Facebook integration")
	    	            .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
	    	            .build();

			    		shareDialog.show(linkContent);
			    	}
	    	  
	    	 
	    }
	    
	  
	    @Override
	    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	        callbackManager.onActivityResult(requestCode, resultCode, data);
	    }
	    
}
