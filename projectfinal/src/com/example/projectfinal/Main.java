package com.example.projectfinal;

import android.R.color;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.app.Fragment;
import android.app.ListFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class Main extends ActionBarActivity{
    private ArrayList<mydata> listData = new ArrayList<mydata>();
    private ArrayList<mydata> Data = new ArrayList<mydata>();
    private static final String TAG_SUCCESS = "success";

    private CustomAdapter itemAdapter;
    private CustomAdapter itemAdapter1;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mCategoryTitles;
    ProgressDialog pDialog;
    private DatabaseHandler db=new DatabaseHandler(this);
    private SQLiteDatabase database;
    Bitmap bitmap;
    private ListView listView;
    private boolean isoffline=false;
    private Menu mOptionsMenu;
    private CallbackManager callbackManager;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        setContentView(R.layout.main);
        
        
        
        SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()  {
		@Override
		public void onRefresh() {
            listData.clear();     
            new LoadAllProducts().execute();
			
		} 
        });


        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);

        listView = (ListView) this.findViewById(R.id.listView);
        itemAdapter = new CustomAdapter(this,R.layout.customlist, listData);
        Data=db.getAllContacts();
        itemAdapter1 = new CustomAdapter(this,R.layout.customlist,Data);
        listView.setAdapter(itemAdapter);
        registerForContextMenu(listView);

        listData.clear();
        String Url = "";
        String[] params = new String[]{Url};
      //  try {
            new LoadAllProducts().execute(params);
          //  itemAdapter.notifyDataSetChanged();
           // pDialog.dismiss();
      //  } catch (InterruptedException e) {
          //  e.printStackTrace();
       //} catch (ExecutionException e) {
       //     e.printStackTrace();
       // }
/*while(chkwhile==0){}
  for(int i=0;i<listData.size();i++) {
    try {
        bitmap = new LoadImage().execute(listData.get(i).Image).get();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        listData.get(i).imgbitmap = stream.toByteArray();
    } catch (InterruptedException e) {
        e.printStackTrace();
    } catch (ExecutionException e) {
        e.printStackTrace();
   }
}

*/


        ActionBar bar = getSupportActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
                  if(tab.getPosition()==0){
                      isoffline=false;
                      listView.setAdapter(itemAdapter);

                  }
                else {
                      isoffline=true;
                      Data.clear();
                      Data.addAll(db.getAllContacts());
                      itemAdapter1.notifyDataSetChanged();
                      listView.setAdapter(itemAdapter1);

                  }
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

            }
        };


        bar.addTab(bar.newTab().setText("Headlines").setTabListener(tabListener));
        bar.addTab(bar.newTab().setText("Offline News").setTabListener(tabListener));

        mTitle = mDrawerTitle = getTitle();
        mCategoryTitles = getResources().getStringArray(R.array.category_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.draweritem, mCategoryTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getSupportActionBar().setIcon(R.drawable.n);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
         mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            @SuppressLint("NewApi") public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            @SuppressLint("NewApi") @Override
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }


         AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {

             public void onItemClick(AdapterView parent, View v, int position, long id) {
            	 
            	 TextView txt=(TextView)v.findViewById(R.id.textView2);
            	 if(txt.isShown())txt.setVisibility(View.GONE);
            	 else txt.setVisibility(View.VISIBLE);
            	 
            }
         };

          listView.setOnItemClickListener(mMessageClickedHandler);






    }
    	public void share(View v)
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.action , menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch(item.getItemId()){
            case R.id.menu_save:
                if(listData.get(info.position).imgbitmap==null){
                    try {
                        bitmap = new LoadImage().execute(listData.get(info.position).Image).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
                    listData.get(info.position).imgbitmap = stream.toByteArray();

                }
                db.addContact(info.position,listData.get(info.position).Title,listData.get(info.position).Description,listData.get(info.position).imgbitmap);
                //Integer count=Integer.valueOf(R.string.count);
                //count++;
                //R.string.count=Integer.valueOf(count);
               //View it=(View)findViewById(R.menu.menu_main);
               //MenuItem mi=(MenuItem)findViewById(R.id.action_count);
                //mi.setTitle(Integer.valueOf("2"));
                //onCreateOptionsMenu();
               //getSupportActionBar().

               // Menu m=(Menu)findViewById(R.id.men);
                onPrepareOptionsMenu(mOptionsMenu);
                break;
            case R.id.menu_delete:
                View v=listView.getChildAt(info.position);
                TextView t=(TextView)v.findViewById(R.id.textView);
                for(int i=0;i<Data.size();i++){

                   if(t.getText()==Data.get(i).Title){
                       db.deleteContact(Data.get(i).id);
                   }
                }
                //db.deleteContact(info.position);
                Data.clear();
                Data.addAll(db.getAllContacts());
                itemAdapter1.notifyDataSetChanged();
                onPrepareOptionsMenu(mOptionsMenu);
                //listView.setAdapter(itemAdapter1);
                break;
            case R.id.menu_share:
            	String title=listData.get(info.position).Title;
            	 final CharSequence[] items = { "Facebook",
	                "Twitter" ,"Email","SMS","Cancel"};
	 
	        AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
	        builder.setTitle("Share Via!");
	        builder.setItems(items, new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int item) {
	                if (items[item].equals("Facebook")) {
	                	Intent i=new Intent(getApplicationContext(),umer1.class);
	                	i.putExtra("title",listData.get(info.position).Title);
	                	i.putExtra("description",listData.get(info.position).Description);
	                	startActivity(i);
	                } else if (items[item].equals("Twitter")) {
	                	String url = "https://twitter.com/intent/tweet?source=webclient&text="+listData.get(info.position).Title;
	                	Intent i = new Intent(getApplicationContext(),twitter.class);
	                	i.putExtra("url",url);
	                	//i.setData(Uri.parse(url));
	                	startActivity(i);
	                	
	                } else if (items[item].equals("Email")) {
	                	
	                	Intent intent = new Intent(Intent.ACTION_SEND);
	                	intent.setType("text/plain");
	                	//intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
	                	intent.putExtra(Intent.EXTRA_SUBJECT, listData.get(info.position).Title);
	                	intent.putExtra(Intent.EXTRA_TEXT, listData.get(info.position).Description);

	                	try {
	                        startActivity(Intent.createChooser(intent, "Send mail..."));
	                        finish();
	                        Log.i("Finished sending email...", "");
	                     } catch (android.content.ActivityNotFoundException ex) {
	                        Toast.makeText(Main.this, 
	                        "There is no email client installed.", Toast.LENGTH_SHORT).show();
	                     }
	                    //dialog.dismiss();
	                }
	                
	                else if(items[item].equals("SMS")){
	                	Uri uri = Uri.parse("smsto:");
	                    Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
	                    intent.putExtra("sms_body", listData.get(info.position).Title);  
	                    startActivity(intent);
	                	
	                }
	                
	                else if(items[item].equals("Cancel")){
	                	dialog.dismiss();
	                	
	                }
	            }
	        });
	        builder.show();
            	
            	
            	break;
            	
            case R.id.menu_location:
            	Intent i=new Intent(getApplicationContext(),mapnew.class);
            	startActivity(i);
            	
            	
            	break;

        }
        return true;
    }
    private enum RSSXMLTag{
        TITLE, DATE, LINK, CONTENT, GUID, IGNORETAG, IMAGE;
    }


   private class async extends AsyncTask<String, Void , ArrayList<mydata>> {

        private RSSXMLTag currentTag;

        @Override
        protected ArrayList<mydata> doInBackground(String... params) {


            String urlStr = params[0];
            InputStream is = null;
            ArrayList<mydata> postDataList = new ArrayList<mydata>();
            try {
                URL url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setReadTimeout(10 * 1000);
                connection.setConnectTimeout(10 * 1000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                int response = connection.getResponseCode();
               // Log.d("debug", "The response is: " + response);
                is = connection.getInputStream();

                // parse xml after getting the data
                XmlPullParserFactory factory = XmlPullParserFactory
                        .newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(is, null);

                int eventType = xpp.getEventType();
                mydata pdData = null;
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "EEE, DD MMM yyyy HH:mm:ss");
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_DOCUMENT) {

                    } else if (eventType == XmlPullParser.START_TAG) {
                        if (xpp.getName().equals("item")) {
                            pdData = new mydata();
                            currentTag = RSSXMLTag.IGNORETAG;
                        } else if (xpp.getName().equals("title")) {
                            currentTag = RSSXMLTag.TITLE;
                        } else if (xpp.getName().equals("link")) {
                            currentTag = RSSXMLTag.LINK;
                        } else if (xpp.getName().equals("pubDate")) {
                            currentTag = RSSXMLTag.DATE;
                        } else if (xpp.getName().equals("description")) {
                            currentTag = RSSXMLTag.CONTENT;
                        } else if (xpp.getName().equals("url")) {
                            currentTag = RSSXMLTag.IMAGE;
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        if (xpp.getName().equals("item")) {
                            // format the data here, otherwise format data in
                            // AdapterC
                            Date postDate = dateFormat.parse(pdData.Date);
                            pdData.Date = dateFormat.format(postDate);
                            postDataList.add(pdData);
                        } else {
                            currentTag = RSSXMLTag.IGNORETAG;
                        }
                    } else if (eventType == XmlPullParser.TEXT) {
                        String content = xpp.getText();
                        content = content.trim();
                        //Log.d("debug", content);
                        if (pdData != null) {
                            switch (currentTag) {
                                case TITLE:
                                    if (content.length() != 0) {
                                        if (pdData.Title != null) {
                                            pdData.Title += content;
                                        } else {
                                            pdData.Title = content;
                                        }
                                    }
                                    break;
                                case LINK:
                                    if (content.length() != 0) {
                                        if (pdData.Link != null) {
                                            pdData.Link += content;
                                        } else {
                                            pdData.Link = content;
                                        }
                                    }
                                    break;
                                case DATE:
                                    if (content.length() != 0) {
                                        if (pdData.Date != null) {
                                            pdData.Date += content;
                                        } else {
                                            pdData.Date = content;
                                        }
                                    }
                                    break;
                                case IMAGE:
                                    if (content.length() != 0) {
                                        if (pdData.Image != null) {
                                            pdData.Image += content;
                                        } else {
                                            pdData.Image = content;
                                        }

                                    }

                                    break;


                                case CONTENT:
                                    if (content.length() != 0) {
                                        if (pdData.Description != null) {
                                            pdData.Description += content;
                                        } else {
                                            pdData.Description = content;
                                        }
                                    }
                                    break;


                                default:
                                    break;
                            }
                        }
                    }

                    eventType = xpp.next();
                }
               // Log.v("tst", String.valueOf((postDataList.size())));
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return postDataList;
        }
            @Override
        protected void onPostExecute(ArrayList<mydata> result) {
                for(int i=0;i<result.size();i++){
                    listData.add(result.get(i));
                }

                itemAdapter.notifyDataSetChanged();
                //pDialog.dismiss();

                for(int i=0;i<listData.size();i++) {
                    try {
                        bitmap = new LoadImage().execute(listData.get(i).Image).get();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
                        listData.get(i).imgbitmap = stream.toByteArray();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }

                pDialog.dismiss();

            }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(Main.this);
            pDialog.setMessage("Loading ....");
            pDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {


        }



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
        }
    }

    //////////
    private class LoadAllProducts extends AsyncTask<String, String, ArrayList<mydata>> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Main.this);
			pDialog.setMessage("Loading News. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting All products from url
		 * */
		protected ArrayList<mydata> doInBackground(String...parms) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// getting JSON string from URL
            json_parser j=new json_parser();
			JSONObject json = j.makeHttpRequest("http://projectsmdnu.host22.com/get_all_news.php","GET", params);
			ArrayList<mydata> postDataList = new ArrayList<mydata>();
			// Check your log cat for JSON reponse
			Log.d("All Products: ", json.toString());

			try {
				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// products found
					// Getting Array of Products
					JSONArray products = json.getJSONArray("products");

					// looping through All Products
					for (int i = 0; i < products.length(); i++) {
						JSONObject c = products.getJSONObject(i);

						// Storing each json item in variable
						//String id = c.getString(TAG_PID);
						//String name = c.getString(TAG_NAME);
						
						mydata pdData=new mydata();
						
						//pdData.id=Integer.parseInt(c.getString("id"));
						pdData.Title=c.getString("title");
						pdData.Description=c.getString("description");
						pdData.Date=c.getString("date");
						pdData.Link=c.getString("link");
						pdData.Image=c.getString("image");
						
						postDataList.add(pdData);
					}
				} else {
					// no products found
					// Launch Add New product Activity
					Intent i = new Intent(getApplicationContext(),
							MainActivity.class);
					// Closing all previous activities
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return postDataList;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(ArrayList<mydata> result) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
            for(int i=0;i<result.size();i++){
                listData.add(result.get(i));
            }

            itemAdapter.notifyDataSetChanged();

		}

	}

    //////////



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mOptionsMenu=menu;
        return true;
    }


    public boolean onPrepareOptionsMenu(Menu menu) {
        //MenuItem mi=(MenuItem)menu.findViewById(R.id.action_count);
        //menu.clear();
        MenuItem mi=menu.findItem(R.id.action_count);
        String a=String.valueOf(db.getContactsCount());
        mi.setTitle(a);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
       }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            listData.clear();
           // String Url = "http://mcs.geo.tv/jang/jang.xml";
           // String[] params = new String[]{Url};
           // new async().execute(params);
            
            new LoadAllProducts().execute();
        }
        
        if (id == R.id.action_count) {
            // listData.clear();
            // String Url = "http://mcs.geo.tv/jang/jang.xml";
            // String[] params = new String[]{Url};
            // new async().execute(params);
        	Intent i=new Intent(getApplicationContext(),camera.class);
        	startActivity(i);
        	
        	
         }
        

        return super.onOptionsItemSelected(item);
    }



    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    
            selectItem(position);
        }
    }

    private void selectItem(int position) {

        Fragment fragment = null;
        String Url;
        String[] params;
        //listData.clear();
        //new LoadAllProducts().execute();
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        
        
        switch (position) {
            case 0:
            	if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
                listData.clear();
                new LoadAllProducts().execute();}
            case 1:
                //listData.clear();
               // Url = "http://www.urdupoint.com/rss/urdupoint-imp.rss";
                //params = new String[]{Url}
                //new async().execute(params);
               // break;
            case 2:

                break;

            default:
                break;
        }

       // if (fragment != null) {
           // FragmentManager fragmentManager = getFragmentManager();
           // fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            //getActionBar().setTitle(mCategoryTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);

        //} 
    }


    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        //getActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @SuppressLint("NewApi") public static class CreateFragment extends Fragment {

        public CreateFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.drawerfragment, container, false);
            return rootView;
        }

    }



}


