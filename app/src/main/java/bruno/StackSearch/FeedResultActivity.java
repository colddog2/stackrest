package bruno.StackSearch;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;


public class FeedResultActivity extends ActionBarActivity {



    public static final double VERSION = 2.2;
    public static final String ENDPOINT =
            "https://api.stackexchange.com/" + VERSION;

    public static Long second = 1000L ;
    public static Long minute = 60*second ;
    public static Long HTTP_TIMEOUT = 10*second ;

    private boolean data_loaded_succesfully, android_tag_applied ;
    private String search_string, tag_searched, json_array_content, device_size ;

    private ProgressDialog mProgressDialog;
    private ListView mListView;
    private Integer search_returns_quantity;
    private List<Topic> ListTopic ;
    private View header;
    private TopicAdapter mAdapter ;
    private Bundle mBundle ;
    private OkHttpClient mOkHttpClient ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);
    }

    @Override
    protected void onResume() {
        super.onResume();


        mListView = (ListView)findViewById(android.R.id.list);

        LayoutInflater mInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            header = mInflater.inflate(R.layout.list_header, null);

        android.support.v7.app.ActionBar menu = getSupportActionBar();
            menu.setDisplayShowHomeEnabled(true);
            menu.setLogo(R.mipmap.ic_launcher);
            menu.setDisplayUseLogoEnabled(true);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    data_loaded_succesfully = false ; //Setting it to false ensures that if something goes wrong during the requestData() method, the activity will start anew
                    requestData();
                } else {
                    startWebView(position);
                }
            }

        }); // End of setOnItemClickListener


        if (data_loaded_succesfully ) {  //The activity was recreated and a savedInstanceState was passed to it, probably due to a screen rotation.
            headerUtil();

            mAdapter = new TopicAdapter(getBaseContext(), R.layout.item_searchresult, ListTopic);
                mListView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

        } else {  // This is the first time the activity gets created.
            mBundle = getIntent().getExtras();

            if(mBundle!=null)  //The condition is met when no data has succesfully been downloaded in the activity.  This check should be superfluous, but I'm keeping it in case this app gets expanded in the future
            {
                android_tag_applied = (Boolean) mBundle.get("android_tag_applied");

                String temp_string = (String) mBundle.get("search_string");
                if (temp_string != null && !temp_string.isEmpty()) { search_string = temp_string; }
                requestData();
            } else {  // This case below should never happen.  I keep it here in case an unforeseen condition does happen, so the user knows they shouldn't stay there forever
                Log.i("Warning", "Warning: something odd happened in onResume ");  // Do nothing
            }   //End of case: (mBundle!=null)

       }  //End of case: (!data_loaded_succesfully)

   }  // End of onResume


    private void startWebView (Integer position) {
        Intent mIntent = new Intent(this, WebViewActivity.class);

        mIntent.putExtra("url", (ListTopic.get(position - 1).getlink()));
        startActivity(mIntent);

    }


    private void requestData() {

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Loading");
        mProgressDialog.setMessage("Please wait while your results are loading");
        mProgressDialog.show();

        if (TextUtils.isEmpty(device_size)) {  device_size = infer_device_size() ;   }

        mOkHttpClient = new OkHttpClient();
            mOkHttpClient.setConnectTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS);
            mOkHttpClient.setWriteTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS);
            mOkHttpClient.setReadTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS);

        RestAdapter mRestAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(ENDPOINT)
                .setClient(new OkClient(mOkHttpClient))
                .build();

        if (android_tag_applied) {  tag_searched = "Android";  }

        Map QueryMap = new HashMap<String,String>();
            QueryMap.put("intitle=", search_string);
            QueryMap.put("tagged=", tag_searched);

        TopicSearchAPI api = mRestAdapter.create(TopicSearchAPI.class);

        api.getFeed(QueryMap, new Callback<MulticlassPOJO>() {

            @Override
            public void success(MulticlassPOJO arg0, Response arg1) {

            search_returns_quantity = arg0.getItems().length;
            Log.i("Banana","Number of items returned: " + Integer.toString(search_returns_quantity) )  ;

                if (search_returns_quantity == 0) {
                    Toast.makeText(getBaseContext(), "Your search yielded no result.  Try another search term.", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss(); //Avoid a window leak by dismissing this dialog.  Be a good citizen!
                    return_to_main_activity();
                } else {
                    /** Note: If the number of search returns is lower than any threshold, that number of returns will be preserved.  The idea here is to prevent 30-item lists on phones */
                    if (device_size.equals("large_tablet")) {
                        if (search_returns_quantity > 26) {  search_returns_quantity = 26 ;  }
                    } else if (device_size.equals("small_tablet")) {
                        if (search_returns_quantity > 19) {  search_returns_quantity = 19 ;  }
                    }  else if (device_size.equals("phone")) {
                        if (search_returns_quantity > 13) {  search_returns_quantity = 13 ;  }
                    }

                    headerUtil();

                    MulticlassPOJO mMulticlassPOJO = arg0;

                    ListTopic = new ArrayList<>();  // ListTopic is the object that will populate the listview

                    for (int i = 0; i < search_returns_quantity-1; i++) {
                        ListTopic.add(new Topic(mMulticlassPOJO.getItems()[i].getOwner().getDisplay_name(),
                                        mMulticlassPOJO.getItems()[i].getOwner().getProfile_image(),
                                        mMulticlassPOJO.getItems()[i].getAnswer_count(),
                                        mMulticlassPOJO.getItems()[i].getTitle(),
                                        mMulticlassPOJO.getItems()[i].getLink())
                                     );  // End of ListTopic.add
                    }  // end of for loop that populates ListTopic


                    mAdapter = new TopicAdapter(getBaseContext(), R.layout.item_searchresult, ListTopic);
                        mListView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();

                    json_array_content = new Gson().toJson(ListTopic);  //Convert ListTopic to JSON so it can be saved in onSaveInstanceState(...)

                    data_loaded_succesfully = true ;
                    mProgressDialog.dismiss();
                }
            }  // End of Callback Success

            @Override
            public void failure(RetrofitError arg0) {
                //Do nothing
            }
        });  //End of api.feed

    }  // End of RequestData() ;



    public String infer_device_size () {

        String size = "";
        DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float widthInches = metrics.widthPixels / metrics.xdpi;
        float heightInches = metrics.heightPixels / metrics.ydpi;

        double diagonalInches = Math.sqrt((widthInches * widthInches) + (heightInches * heightInches));     //a² + b² = c² -- pythagorean theorem

        if (diagonalInches >= 10)       { size = "large_tablet";   }
        else if (diagonalInches >= 7)   { size = "small_tablet";   }
        else if (diagonalInches < 7)    { size = "phone";  }
        else                            { Log.i("Banana", "Something went horribly wrong in infer_device_size().  Sorry.");   }

        return size ;
    }



    @Override
    public void onSaveInstanceState(Bundle Icicle) {
        super.onSaveInstanceState(Icicle);
        Icicle.putBoolean("android_tag_applied", android_tag_applied);
        Icicle.putString("search_string", search_string);
        Icicle.putString("device_size", device_size);

        Icicle.putString("json_array_content", json_array_content);
        Icicle.putBoolean("data_loaded_succesfully", data_loaded_succesfully);
    }

    @Override
    protected void onRestoreInstanceState(Bundle Icicle) {
        super.onRestoreInstanceState(Icicle);

        android_tag_applied = Icicle.getBoolean("android_tag_applied");
        search_string = Icicle.getString("search_string");
        device_size = Icicle.getString("device_size");

        json_array_content = Icicle.getString("json_array_content");
            if (TextUtils.isEmpty(json_array_content)) {
                //Do nothing since the string is empty
            } else {
                //This below creates a list that can be input to the adapter
                Type type = new TypeToken<List<Topic>>(){}.getType();
                ListTopic = new Gson().fromJson(json_array_content, type);

                data_loaded_succesfully = Icicle.getBoolean("data_loaded_succesfully");
            }

    }


    private void headerUtil () {
        if (mListView.getHeaderViewsCount() == 0) {  mListView.addHeaderView(header);   }
    }

    private void return_to_main_activity() {
        Intent mIntent = new Intent(this, MainActivity.class);
        startActivity(mIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_listactivity, menu);
        return true;
    }


}  // End of Activity

