package com.example.projectfinal;



import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class signup extends Activity {
	EditText e1;
	EditText e2;
	EditText e3;
	private static final String TAG_SUCCESS = "success";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		
		e1=(EditText)findViewById(R.id.editTxt1);
		e2=(EditText)findViewById(R.id.editTxt2);
		e3=(EditText)findViewById(R.id.editTxt3);
		
		
		
		
		
		//t1=(TextView)findViewById(R.id.txtView1);	
		Button t=(Button)findViewById(R.id.bton1);
		t.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
					new asynsignup().execute();
					
									
			}
		});
		
	}
	
	private class asynsignup extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			String name = e1.getText().toString();
			String user = e2.getText().toString();
			String pass= e3.getText().toString();
			

			// Building Parameters
			List<NameValuePair> paramss = new ArrayList<NameValuePair>();
			paramss.add(new BasicNameValuePair("name", name));
			paramss.add(new BasicNameValuePair("user", user));
			paramss.add(new BasicNameValuePair("pass", pass));

			// getting JSON Object
			// Note that create product url accepts POST method
			
			json_parser j=new json_parser();
			
			JSONObject json = j.makeHttpRequest("http://projectsmdnu.host22.com/register.php","GET", paramss);
			
						
			// check log cat fro response
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
		
		protected void onPostExecute(String message) {
			if(message=="success")Toast.makeText(getApplicationContext(),"register success",Toast.LENGTH_SHORT).show();
			else Toast.makeText(getApplicationContext(),"register fail",Toast.LENGTH_SHORT).show();
			
		}
		
			
		
	}
	
	
	
	
}
